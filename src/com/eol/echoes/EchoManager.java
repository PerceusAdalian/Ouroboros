package com.eol.echoes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
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

import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.PassiveModifier;
import com.eol.enums.EchoMaterial;
import com.eol.enums.PassiveEchoEffect;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.Chance;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class EchoManager 
{
	private static final Set<UUID> durabilityLock = new HashSet<>();

	public enum DurabilityOperation
	{
		SUBTRACT,
		ADD,
		RESTORE,
		DIMINISH,
		SETMAX
	}
	
	/**
     * Convenience: returns true if the item carries a manifest (i.e. is an Echo).
     */
    public static boolean isEcho(ItemStack item)
    {
        if (item == null) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        
        return meta.getPersistentDataContainer().has(EchoManifestCodec.MANIFEST_KEY, PersistentDataType.STRING);
    }
    
    /**
     * Convenience: returns the assumed echo's manifest codec.
     */
    public static EchoManifest getCodec(ItemStack item)
	{
	    if (item == null || !item.hasItemMeta()) return null;
	    
	    String str = item.getItemMeta().getPersistentDataContainer().get(EchoManifestCodec.MANIFEST_KEY, PersistentDataType.STRING);
	    if (str == null) return null;
	    
	    return EchoManifestCodec.fromJson(str);
	}
	
	/**
	 * @param player
	 * @param stack -- Assumes an item stack that may or may not be an echo.
	 * @param durabilityType -- An operation type: 
	 * @DurabilityOperation1 ADD -> Adds the value to the durability. 
	 * @DurabilityOperation2 SUBTRACT -> Subtracts the value from the durability. 
	 * @DurabilityOperation3 RESTORE -> Restores durability as a percent of the value back to the echo.
	 * @DurabilityOperation4 DIMINISH -> Reduces durability as a percent of the value from the echo.
	 * @DurabilityOperation5 SETMAX -> Sets the echo's durability to it's base default max.
	 * @param value
	 */
	public static void modifyDurability(Player player, ItemStack stack, DurabilityOperation operation, int value, boolean setOffhand)
	{
	    if (!isEcho(stack)) return;
	    
	    EchoManifest manifest = EchoManifestCodec.read(stack);
	    if (manifest == null) return;

	    EchoData data = manifest.baseStats();

	    int current = 0;
	    if (operation == DurabilityOperation.ADD)
	    	current = Math.min(data.getCurrentDurability() + value, data.getMaxDurability());
	    else if (operation == DurabilityOperation.SUBTRACT) 
	    	current = Math.max(0, data.getCurrentDurability() - value);
	    else if (operation == DurabilityOperation.RESTORE) 
	    	current = (int) Math.min(data.getCurrentDurability() + (value / 100.0) * data.getMaxDurability(), data.getMaxDurability());
	    else if (operation == DurabilityOperation.DIMINISH)
	    	current = (int) Math.max(0, data.getCurrentDurability() - (value / 100.0) * data.getMaxDurability());
	    else if (operation == DurabilityOperation.SETMAX)
	    	current = data.getMaxDurability();
	    
	    EchoData updatedEchoData = new EchoData(
	        data.getAttack(), data.getAttackRating(),
	        data.getCritRate(), data.getCritModifier(),
	        data.getMaxDurability(), current);

	    EchoManifest updatedManifest = new EchoManifest(
	        manifest.echoId(), manifest.rarity(), updatedEchoData,
	        manifest.modifiers(), manifest.slotType(),
	        manifest.equippedAbilityKey(), manifest.lockedAbilityKey(),
	        manifest.echoForm(), manifest.echoMaterial());

	    EchoManifestCodec.write(stack, updatedManifest);

	    ItemMeta meta = stack.getItemMeta();
	    if (meta != null)
	    {
	        List<String> lore = manifest.echoMaterial() != null
	            ? EchoLoreBuilder.build(updatedManifest, manifest.echoMaterial())
	            : EchoLoreBuilder.build(updatedManifest, updatedManifest.echoMaterial() == EchoMaterial.BOW ? true : false);
	        meta.setLore(lore);
	        stack.setItemMeta(meta);
	    }
	    
	    durabilityLock.add(player.getUniqueId());
	    Bukkit.getScheduler().runTask(Ouroboros.instance, () ->
	    {
	    	if (setOffhand) player.getInventory().setItemInOffHand(stack);
	    	else player.getInventory().setItemInMainHand(stack);
	        durabilityLock.remove(player.getUniqueId());
	    });
	    
	    if (current <= 0)
	    {
	        player.getInventory().setItemInMainHand(null);
	        EntityEffects.playSound(player, Sound.ENTITY_BREEZE_INHALE, SoundCategory.MASTER);
	        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	        {
	            EntityEffects.playSound(player, Sound.ITEM_SHIELD_BREAK, SoundCategory.MASTER);
	            PrintUtils.PrintToActionBar(player, PrintUtils.color(ObsColors.CELESTIO) + "&cYour Echo has shattered..");
	        }, 5);
	    }
	}
	
	/**
     * Equips an ability onto a procedural Echo, and attempts to rebuild the item stack.
     * Fails silently (returns false) if the echo has a locked ability, no slot, or the key is blank.
     * Also fails if the item could not be rebuilt; but that's an edge case.
     */
	public ItemStack equipAbility(ItemStack item, String abilityKey)
	{
	    if (item == null || abilityKey == null || abilityKey.isBlank()) return null;

	    EchoManifest manifest = EchoManifestCodec.read(item);
	    if (manifest == null) return null;
	    if (manifest.hasLockedAbility()) return null;
	    if (!manifest.hasElementiumSlot()) return null;

	    EchoManifest updated = withEquippedAbility(manifest, abilityKey);

	    ItemStack rebuilt = EchoForge.rebuild(updated);
	    if (rebuilt == null) return null;

	    // Copy rebuilt item back into the original stack
	    item.setType(rebuilt.getType());
	    item.setItemMeta(rebuilt.getItemMeta());
	    return item;
	}
    
    /**
     * Removes the equipped ability from a procedural Echo.
     * Will not remove a locked ability.
     * Attempts to also rebuild the item, and silently fails if any operation results in a null item stack.
     */
	public ItemStack removeAbility(ItemStack item)
	{
	    if (item == null) return null;

	    EchoManifest manifest = EchoManifestCodec.read(item);
	    if (manifest == null) return null;
	    if (manifest.hasLockedAbility()) return null;
	    if (!manifest.hasEquippedAbility()) return null;

	    EchoManifest updated = withEquippedAbility(manifest, null);

	    ItemStack rebuilt = EchoForge.rebuild(updated);
	    if (rebuilt == null) return null;

	    item.setType(rebuilt.getType());
	    item.setItemMeta(rebuilt.getItemMeta());
	    return item;
	}

    /**
     * Simply returns an echo manifest with the old data wrapped alongside an input ability's key (internal name).
     */
    private EchoManifest withEquippedAbility(EchoManifest old, String abilityKey) 
    {
        return new EchoManifest(
            old.echoId(),
            old.rarity(),
            old.baseStats(),
            old.modifiers(),
            old.slotType(),
            abilityKey,
            old.lockedAbilityKey(),
            old.echoForm(),
            old.echoMaterial()
        );
    }
    
    public static void registerEventHelpers(Plugin plugin)
    {
    	Bukkit.getPluginManager().registerEvents(new Listener() 
    	{
    		// Prevents echo enchant attempts
        	@EventHandler
        	public void onEnchantAttempt(EnchantItemEvent e)
        	{
        		if (EchoManager.isEcho(e.getItem()))
        		{
        			e.setCancelled(true);
        			return;
        		}
        	}
        	
        	// Prevents starting an enchant craft on echoes
        	@EventHandler
        	public void onEchantViewAttempt(PrepareItemEnchantEvent e)
        	{
        		if (EchoManager.isEcho(e.getItem()))
        		{
        			e.setCancelled(true);
        			return;
        		}
        	}
        	
        	// Prevent Echo Renaming/Enchant Events
        	@EventHandler
        	public void onSmithViewAttempt(PrepareSmithingEvent e)
        	{
        		if (EchoManager.isEcho(e.getResult()))
        		{
        			e.setResult(null);
        			return;
        		}
        	}
        	
        	// Prevent echo smith attempts
        	@EventHandler
        	public void onSmithAttempt(SmithItemEvent e)
        	{
        		if (EchoManager.isEcho(e.getCurrentItem()))
        		{
        			e.setCancelled(true);
        			return;
        		}
        	}
        	
        	// Prevent anvil repairs/renames/combinations
        	@EventHandler
        	public void onAnvilPrepare(PrepareAnvilEvent e)
        	{
        	    if (EchoManager.isEcho(e.getResult()))
        	    {
        	        e.setResult(null);
        	        return;
        	    }
        	}

        	// Prevent grindstone from stripping echoes
        	@EventHandler
        	public void onGrindstone(PrepareGrindstoneEvent e)
        	{
        		if (EchoManager.isEcho(e.getResult()))
	    	    {
	    	        e.setResult(null);
	    	        return;
	    	    }
        	}

        	// Prevent echoes from being used as crafting ingredients or outputs
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
        	
        	// Prevents echo relevant items from being crafted
        	@EventHandler
        	public void onItemCraftAttempt(CraftItemEvent e)
        	{
        	    ItemStack result = e.getRecipe().getResult();
        	    if (EchoFormResolver.ALL_ECHO_MATERIALS.contains(result.getType()))
        	    {
        	        e.setCancelled(true);
        	        return;
        	    }
        	}

        	// Prevent echoes from being placed in brewing stands, furnaces, etc.
        	@EventHandler
        	public void onInventoryClick(InventoryClickEvent e)
        	{
        	    if (!(e.getWhoClicked() instanceof Player)) return;
        	    ItemStack cursor = e.getCursor();
        	    ItemStack current = e.getCurrentItem();

        	    InventoryType type = e.getInventory().getType();
        	    if (type == InventoryType.FURNACE 	  	  || type == InventoryType.BLAST_FURNACE
	        	     || type == InventoryType.SMOKER  	  || type == InventoryType.BREWING
	        	     || type == InventoryType.BEACON  	  || type == InventoryType.LOOM
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

        	    boolean shouldNegate = false;

        	    if (ResolveEchoInteract.negate_arrow_consumption.contains(p.getUniqueId()) 
        	    	&& codec.containsPassiveModifier(PassiveEchoEffect.INFINITY))
        	    {
        	        shouldNegate = true;
        	    }
        	    else if (ResolveEchoInteract.ignore_arrow.contains(p.getUniqueId())
        	        && codec.containsPassiveModifier(PassiveEchoEffect.IGNORE_ARROW))
        	    {
        	        PassiveModifier mod = codec.getPassiveModifier(PassiveEchoEffect.IGNORE_ARROW);
        	        if (mod != null && Chance.of(mod.magnitude() * 100))
        	            shouldNegate = true;
        	    }

        	    if (!shouldNegate) return;

        	    e.setCancelled(true);

        	    Arrow original = (Arrow) e.getProjectile();
        	    Arrow refired = p.getWorld().spawn(original.getLocation(), Arrow.class);
        	    refired.setVelocity(original.getVelocity());
        	    refired.setShooter(p);
        	    refired.setCritical(original.isCritical());
        	    refired.setDamage(original.getDamage());
        	    refired.setPickupStatus(AbstractArrow.PickupStatus.DISALLOWED);

        	    Bukkit.getScheduler().runTask(Ouroboros.instance, () ->
        	    {
        	        ItemStack offhand = p.getInventory().getItemInOffHand();
        	        if (offhand.getType() == Material.ARROW)
        	        {
        	            offhand.setAmount(offhand.getAmount() + 1);
        	            p.getInventory().setItemInOffHand(offhand);
        	        }
        	        else p.getInventory().addItem(new ItemStack(Material.ARROW, 1));
        	    });
        	}
        	
        	@EventHandler
        	public void onVanillaItemCraft(CraftItemEvent e)
        	{
        	    ItemStack result = e.getRecipe().getResult();
        	    if (EchoFormResolver.ALL_ECHO_MATERIALS.contains(result.getType()))
        	    {
        	    	e.setCancelled(true);
        	    	return;
        	    }
        	}
        	
    	}, plugin);
    }
}
