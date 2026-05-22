package com.eol.echoes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.eol.echoes.instances.AbstractEOL;
import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.PassiveModifier;
import com.eol.enums.EchoMaterial;
import com.eol.enums.PassiveEchoEffect;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.objects.instances.ScrapMateria;
import com.ouroboros.utils.Chance;
import com.ouroboros.utils.InventoryUtils;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class EchoManager
{
    private static final Set<UUID>    durabilityLock = new HashSet<>();
    private static final NamespacedKey cancelArrow   = new NamespacedKey(Ouroboros.instance, "cancel_arrow");

    public enum DurabilityOperation
    {
        SUBTRACT, ADD, RESTORE, DIMINISH, SETMAX,
    }

    public enum EchoDataOperation
    {
        ATTACK, ATTACK_SPEED, CRIT_RATE, CRIT_MODIFIER, DURABILITY, MAX_DURABILITY,
    }

    // -------------------------------------------------------------------------
    // Identity helpers
    // -------------------------------------------------------------------------

    public static boolean isEcho(ItemStack item)
    {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getPersistentDataContainer().has(EchoManifestCodec.MANIFEST_KEY, PersistentDataType.STRING);
    }

    /** Returns true if the item is an armor-type Echo. */
    public static boolean isArmorEcho(ItemStack item)
    {
        EchoManifest manifest = getCodec(item);
        return manifest != null && manifest.isArmorEcho();
    }

    /** Returns true if the item is a weapon/tool-type Echo. */
    public static boolean isWeaponEcho(ItemStack item)
    {
        EchoManifest manifest = getCodec(item);
        return manifest != null && manifest.isWeaponEcho();
    }

    public static EchoManifest getCodec(ItemStack item)
    {
        if (item == null || !item.hasItemMeta()) return null;
        String str = item.getItemMeta().getPersistentDataContainer()
                .get(EchoManifestCodec.MANIFEST_KEY, PersistentDataType.STRING);
        if (str == null) return null;
        return EchoManifestCodec.fromJson(str);
    }

    public static EchoData getEchoData(ItemStack item)
    {
        if (!isEcho(item)) return null;
        EchoManifest codec = getCodec(item);
        if (codec == null) return null;
        return codec.baseStats();
    }

    public static ArmorData getArmorData(ItemStack item)
    {
        EchoManifest codec = getCodec(item);
        if (codec == null) return null;
        return codec.armorStats();
    }

    public static Number getEchoData(ItemStack item, EchoDataOperation op)
    {
        EchoData data = getEchoData(item);
        if (data == null) return 0.0;
        return switch (op)
        {
            case ATTACK       -> data.getAttack();
            case ATTACK_SPEED -> data.getAttackRating();
            case CRIT_RATE    -> data.getCritRate();
            case CRIT_MODIFIER -> data.getCritModifier();
            case DURABILITY    -> data.getCurrentDurability();
            case MAX_DURABILITY -> data.getMaxDurability();
        };
    }

    // -------------------------------------------------------------------------
    // Durability — shared logic for both Echo types
    // -------------------------------------------------------------------------

    public static void modifyDurability(Player player, ItemStack stack, DurabilityOperation op, int value, boolean setOffhand)
    {
        if (!isEcho(stack)) return;

        EchoManifest manifest = EchoManifestCodec.read(stack);
        if (manifest == null) return;

        int oldCurrent = manifest.getCurrentDurability();
        int maxDur     = manifest.getMaxDurability();
        int current    = computeDurability(oldCurrent, maxDur, op, value);

        EchoManifest updatedManifest = withDurability(manifest, current);
        EchoManifestCodec.write(stack, updatedManifest);

        ItemMeta meta = stack.getItemMeta();
        if (meta != null)
        {
            if (meta.getPersistentDataContainer().has(AbstractEOL.eolKey))
            {
                patchDurabilityLine(meta, current, maxDur);
            }
            else
            {
                List<String> lore = buildLore(updatedManifest);
                meta.setLore(lore);
            }
            stack.setItemMeta(meta);
        }

        if (current <= 10 && current > 0)
            EntityEffects.playSound(player, Sound.BLOCK_CHAIN_STEP, SoundCategory.MASTER);

        final int finalDurability = current;
        durabilityLock.add(player.getUniqueId());
        Bukkit.getScheduler().runTask(Ouroboros.instance, () ->
        {
            if (finalDurability <= 0)
            {
                player.getInventory().setItemInMainHand(null);
                EntityEffects.playSound(player, Sound.ENTITY_BREEZE_INHALE, SoundCategory.MASTER);
                ObsParticles.drawWisps(player.getLocation(), player.getWidth(), player.getHeight(), 5, Particle.ASH, null);
                Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
                {
                    EntityEffects.playSound(player, Sound.ITEM_SHIELD_BREAK, SoundCategory.MASTER);
                    ObsParticles.drawWisps(player.getLocation(), player.getWidth(), player.getHeight(), 5,
                            Particle.BLOCK_CRUMBLE, Material.STONE.createBlockData());
                    PrintUtils.PrintToActionBar(player, PrintUtils.color(ObsColors.CELESTIO) + "&cYour Echo has shattered..");
                }, 20);

                AbstractObsObject scrap  = new ScrapMateria();
                ItemStack scrapStack     = scrap.toItemStack();
                scrapStack.setAmount(manifest.rarity().getRarity() * 5);
                InventoryUtils.add(player, scrapStack);
            }
            else
            {
                if (setOffhand) player.getInventory().setItemInOffHand(stack);
                else player.getInventory().setItemInMainHand(stack);
            }
            durabilityLock.remove(player.getUniqueId());
        });
    }

    public static ItemStack modifyDurabilityAndReturn(ItemStack stack, DurabilityOperation operation, int value)
    {
        if (!isEcho(stack)) return stack;

        EchoManifest manifest = EchoManifestCodec.read(stack);
        if (manifest == null) return stack;

        int current = computeDurability(manifest.getCurrentDurability(), manifest.getMaxDurability(), operation, value);

        EchoManifest updatedManifest = withDurability(manifest, current);
        EchoManifestCodec.write(stack, updatedManifest);

        ItemMeta meta = stack.getItemMeta();
        if (meta != null)
        {
            if (meta.getPersistentDataContainer().has(AbstractEOL.eolKey))
                patchDurabilityLine(meta, current, manifest.getMaxDurability());
            else
                meta.setLore(buildLore(updatedManifest));
            stack.setItemMeta(meta);
        }

        return stack;
    }

    // -------------------------------------------------------------------------
    // Ability management (weapon/tool echoes only)
    // -------------------------------------------------------------------------

    public static ItemStack equipAbility(ItemStack item, String abilityKey)
    {
        if (item == null || abilityKey == null || abilityKey.isBlank()) return null;

        EchoManifest manifest = EchoManifestCodec.read(item);
        if (manifest == null)                return null;
        if (manifest.isArmorEcho())          return null;
        if (manifest.hasLockedAbility())     return null;
        if (!manifest.hasElementiumSlot())   return null;

        EchoManifest updated = withEquippedAbility(manifest, abilityKey);
        ItemStack rebuilt    = EchoForge.rebuild(updated);
        if (rebuilt == null) return null;

        item.setType(rebuilt.getType());
        item.setItemMeta(rebuilt.getItemMeta());
        return item;
    }

    public static ItemStack removeAbility(ItemStack item)
    {
        if (item == null) return null;

        EchoManifest manifest = EchoManifestCodec.read(item);
        if (manifest == null)              return null;
        if (manifest.isArmorEcho())        return null;
        if (manifest.hasLockedAbility())   return null;
        if (!manifest.hasEquippedAbility()) return null;

        EchoManifest updated = withEquippedAbility(manifest, null);
        ItemStack rebuilt    = EchoForge.rebuild(updated);
        if (rebuilt == null) return null;

        item.setType(rebuilt.getType());
        item.setItemMeta(rebuilt.getItemMeta());
        return item;
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private static int computeDurability(int current, int max, DurabilityOperation op, int value)
    {
        return switch (op)
        {
            case ADD      -> Math.min(current + value, max);
            case SUBTRACT -> Math.max(0, current - value);
            case RESTORE  -> (int) Math.min(current + (value / 100.0) * max, max);
            case DIMINISH -> (int) Math.max(0, current - (value / 100.0) * max);
            case SETMAX   -> max;
        };
    }

    /**
     * Returns a new EchoManifest with updated durability, routing correctly for
     * weapon vs armor echo without changing any other fields.
     */
    private static EchoManifest withDurability(EchoManifest manifest, int newCurrent)
    {
        if (manifest.isArmorEcho())
        {
            ArmorData updated = manifest.armorStats().withDurability(newCurrent);
            return new EchoManifest(
                    manifest.echoId(), manifest.rarity(), updated,
                    manifest.modifiers(), manifest.echoForm(), manifest.echoMaterial());
        }
        else
        {
            EchoData old = manifest.baseStats();
            EchoData updated = new EchoData(
                    old.getAttack(), old.getAttackRating(),
                    old.getCritRate(), old.getCritModifier(),
                    old.getMaxDurability(), newCurrent);
            return new EchoManifest(
                    manifest.echoId(), manifest.rarity(), updated, null,
                    manifest.modifiers(), manifest.slotType(),
                    manifest.equippedAbilityKey(), manifest.lockedAbilityKey(),
                    manifest.echoForm(), manifest.echoMaterial());
        }
    }

    /** Builds lore for any echo type. */
    private static List<String> buildLore(EchoManifest manifest)
    {
        if (manifest.isArmorEcho())
            return EchoLoreBuilder.buildArmor(manifest, manifest.echoMaterial());
        if (manifest.echoMaterial() != null)
            return EchoLoreBuilder.build(manifest, manifest.echoMaterial());
        return EchoLoreBuilder.build(manifest,
                manifest.echoMaterial() == EchoMaterial.BOW || manifest.echoMaterial() == EchoMaterial.CROSSBOW);
    }

    /** Patches only the durability line in an existing lore list (used for EOL items). */
    private static void patchDurabilityLine(ItemMeta meta, int current, int max)
    {
        List<String> lore = meta.getLore();
        if (lore == null) return;
        String durabilityLine = PrintUtils.ColorParser("&b&lDurability&r&f: "
                + EchoLoreBuilder.rollQualityColor(current, 0, max)
                + current + "&r&7/" + max);
        for (int i = 0; i < lore.size(); i++)
        {
            if (ChatColor.stripColor(lore.get(i)).startsWith("Durability:"))
            {
                lore.set(i, durabilityLine);
                break;
            }
        }
        meta.setLore(lore);
    }

    private static EchoManifest withEquippedAbility(EchoManifest old, String abilityKey)
    {
        return new EchoManifest(
                old.echoId(), old.rarity(), old.baseStats(), null,
                old.modifiers(), old.slotType(),
                abilityKey, old.lockedAbilityKey(),
                old.echoForm(), old.echoMaterial());
    }

    // -------------------------------------------------------------------------
    // Event registration
    // -------------------------------------------------------------------------

    public static void registerEventHelpers(Plugin plugin)
    {
        Bukkit.getPluginManager().registerEvents(new Listener()
        {
            @EventHandler
            public void onEnchantAttempt(EnchantItemEvent e)
            {
                if (EchoManager.isEcho(e.getItem())) e.setCancelled(true);
            }

            @EventHandler
            public void onEnchantViewAttempt(PrepareItemEnchantEvent e)
            {
                if (EchoManager.isEcho(e.getItem())) e.setCancelled(true);
            }

            @EventHandler
            public void onSmithViewAttempt(PrepareSmithingEvent e)
            {
                if (EchoManager.isEcho(e.getResult())) e.setResult(null);
            }

            @EventHandler
            public void onSmithAttempt(SmithItemEvent e)
            {
                if (EchoManager.isEcho(e.getCurrentItem())) e.setCancelled(true);
            }

            @EventHandler
            public void onAnvilPrepare(PrepareAnvilEvent e)
            {
                if (EchoManager.isEcho(e.getResult())) e.setResult(null);
            }

            @EventHandler
            public void onGrindstone(PrepareGrindstoneEvent e)
            {
                if (EchoManager.isEcho(e.getResult())) e.setResult(null);
            }

            @EventHandler
            public void onCraftPrepare(PrepareItemCraftEvent e)
            {
                for (ItemStack ingredient : e.getInventory().getMatrix())
                {
                    if (EchoManager.isEcho(ingredient))
                    {
                        e.getInventory().setResult(null);
                        return;
                    }
                }
            }

            @EventHandler
            public void onItemCraftAttempt(CraftItemEvent e)
            {
                ItemStack result = e.getRecipe().getResult();
                if (EchoFormResolver.ALL_ECHO_MATERIALS.contains(result.getType()))
                    e.setCancelled(true);
            }

            @EventHandler
            public void onInventoryClick(InventoryClickEvent e)
            {
                if (!(e.getWhoClicked() instanceof Player)) return;
                ItemStack cursor  = e.getCursor();
                ItemStack current = e.getCurrentItem();
                InventoryType type = e.getInventory().getType();
                if (type == InventoryType.FURNACE      || type == InventoryType.BLAST_FURNACE
                        || type == InventoryType.SMOKER     || type == InventoryType.BREWING
                        || type == InventoryType.BEACON     || type == InventoryType.LOOM
                        || type == InventoryType.CARTOGRAPHY || type == InventoryType.STONECUTTER)
                {
                    if (EchoManager.isEcho(cursor) || EchoManager.isEcho(current))
                        e.setCancelled(true);
                }
            }

            @EventHandler
            public void onBowEchoShoot(EntityShootBowEvent e)
            {
                if (!(e.getEntity() instanceof Player p)) return;
                if (!EchoManager.isEcho(e.getBow())) return;

                EchoManifest codec = EchoManager.getCodec(e.getBow());
                if (codec == null) return;

                PassiveModifier infinity   = codec.getPassiveModifier(PassiveEchoEffect.INFINITY);
                PassiveModifier ignoreArrow = codec.getPassiveModifier(PassiveEchoEffect.IGNORE_ARROW);

                boolean shouldNegate = false;
                if (ResolveEchoInteract.negate_arrow_consumption.contains(p.getUniqueId())
                        && infinity != null && infinity.condition().satisfies(p, null, p.getWorld()))
                    shouldNegate = true;
                else if (ResolveEchoInteract.ignore_arrow.contains(p.getUniqueId())
                        && ignoreArrow != null && ignoreArrow.condition().satisfies(p, null, p.getWorld()))
                {
                    if (Chance.of(ignoreArrow.magnitude() * 100))
                        shouldNegate = true;
                }

                if (!shouldNegate) return;
                e.getProjectile().getPersistentDataContainer().set(cancelArrow, PersistentDataType.BYTE, (byte) 1);
            }

            @EventHandler
            public void onArrowHit(ProjectileHitEvent e)
            {
                if (!(e.getEntity() instanceof Arrow arrow)) return;
                if (!(arrow.getShooter() instanceof Player p)) return;

                boolean hitMob     = e.getHitEntity() instanceof LivingEntity;
                boolean hasArrowKey = arrow.getPersistentDataContainer().has(cancelArrow, PersistentDataType.BYTE);
                if (hitMob && hasArrowKey)
                {
                    boolean refunded = false;
                    for (int i = 0; i < p.getInventory().getSize(); i++)
                    {
                        ItemStack slot = p.getInventory().getItem(i);
                        if (slot != null && slot.getType() == Material.ARROW)
                        {
                            slot.setAmount(slot.getAmount() + 1);
                            refunded = true;
                            break;
                        }
                    }
                    if (!refunded) p.getInventory().addItem(new ItemStack(Material.ARROW, 1));
                    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> arrow.remove(), 10);
                }
                else
                {
                    if (ResolveEchoInteract.recycle_arrows.contains(p.getUniqueId())) return;
                    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> arrow.remove(), 10);
                }
            }

            @EventHandler
            public void onVanillaItemCraft(CraftItemEvent e)
            {
                ItemStack result = e.getRecipe().getResult();
                if (EchoFormResolver.ALL_ECHO_MATERIALS.contains(result.getType()))
                    e.setCancelled(true);
            }

        }, plugin);
    }
}