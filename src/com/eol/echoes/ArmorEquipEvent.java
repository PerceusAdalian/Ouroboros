package com.eol.echoes;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;

/**
 * EchoArmorEvent manages the passive effect lifecycle for equipped armor Echoes.
 *
 * Armor pieces don't move through the hotbar so PlayerItemHeldEvent doesn't fire
 * for them. Instead we hook InventoryClickEvent, which covers every way a player
 * can equip or unequip a piece: clicking directly into an armor slot, shift-clicking
 * from any inventory, and dragging. Because the inventory state at event time can
 * be mid-transaction we defer the "apply new effects" half by one tick, mirroring
 * the same pattern used in EchoHeldEvent#onItemPickup.
 *
 * PlayerJoinEvent restores effects for pieces already worn when the player logs in,
 * since no click event fires for armor that is simply on the character at connect time.
 *
 * Quit and death clear only armor-sourced effects via
 * {@link ResolveEchoInteract#clearArmorEffects(Player)}. Weapon effects are cleared
 * separately by EchoHeldEvent, so the two systems don't interfere.
 */
public class ArmorEquipEvent
{
    public static void register(Plugin plugin)
    {
        Bukkit.getPluginManager().registerEvents(new Listener()
        {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent e)
            {
                if (!(e.getWhoClicked() instanceof Player p)) return;

                // We only care about interactions that can move armor on or off the body.
                // This covers:
                //   - Clicking directly in an armor slot (ARMOR inventory type, slot 0-3)
                //   - Shift-clicking from player inventory or any other open inventory
                //   - The player's own inventory screen (CRAFTING type covers that)
                InventoryType type = e.getInventory().getType();
                boolean isArmorSlotClick = (type == InventoryType.CRAFTING || type == InventoryType.PLAYER)
                        && e.getSlot() >= 36 && e.getSlot() <= 39; // bukkit armor slot indices
                boolean isShiftClick = e.isShiftClick();

                if (!isArmorSlotClick && !isShiftClick) return;

                // Snapshot what's currently in each armor slot before the transaction settles.
                // We remove effects for whatever is there now, then reapply after one tick
                // for whatever ends up there.
                ItemStack[] armorBefore = snapshotArmor(p);
                removeArmorEffects(p, armorBefore);

                Bukkit.getScheduler().runTaskLater(plugin, () ->
                {
                    ItemStack[] armorAfter = snapshotArmor(p);
                    applyArmorEffects(p, armorAfter);
                }, 1L);
            }

            // Restore effects for armor already worn when the player logs in.
            @EventHandler
            public void onJoin(PlayerJoinEvent e)
            {
                Player p = e.getPlayer();
                Bukkit.getScheduler().runTaskLater(plugin, () ->
                        applyArmorEffects(p, snapshotArmor(p)), 1L);
            }

            @EventHandler
            public void onQuit(PlayerQuitEvent e)
            {
                ResolveEchoInteract.clearArmorEffects(e.getPlayer());
            }

            @EventHandler
            public void onDeath(PlayerDeathEvent e)
            {
                ResolveEchoInteract.clearArmorEffects(e.getEntity());
            }

        }, plugin);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static ItemStack[] snapshotArmor(Player p)
    {
        return new ItemStack[]{
            p.getInventory().getHelmet(),
            p.getInventory().getChestplate(),
            p.getInventory().getLeggings(),
            p.getInventory().getBoots()
        };
    }

    private static void applyArmorEffects(Player p, ItemStack[] pieces)
    {
        for (ItemStack piece : pieces)
        {
            if (!EchoManager.isArmorEcho(piece)) continue;
            EchoManifest manifest = EchoManager.getCodec(piece);
            if (manifest == null) continue;

            for (Modifier mod : manifest.getPassiveModifiers())
            {
                if (mod.condition().satisfies(p, null, p.getWorld()))
                    ResolveEchoInteract.resolveEquippedEffects((PassiveModifier) mod, p);
            }
        }
    }

    private static void removeArmorEffects(Player p, ItemStack[] pieces)
    {
        for (ItemStack piece : pieces)
        {
            if (!EchoManager.isArmorEcho(piece)) continue;
            EchoManifest manifest = EchoManager.getCodec(piece);
            if (manifest == null) continue;

            for (Modifier mod : manifest.getPassiveModifiers())
            {
                if (mod.condition().satisfies(p, null, p.getWorld()))
                    ResolveEchoInteract.removeEquippedEffects((PassiveModifier) mod, p);
            }
        }
    }
}