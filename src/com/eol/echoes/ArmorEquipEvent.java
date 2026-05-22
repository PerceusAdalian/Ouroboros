package com.eol.echoes;

public class ArmorEquipEvent
{
//    public static void register(Plugin plugin)
//    {
//        Bukkit.getPluginManager().registerEvents(new Listener()
//        {
//            @EventHandler
//            public void onArmorChange(PlayerArmorChangeEvent e)
//            {
//                Player    p       = e.getPlayer();
//                ItemStack oldItem = e.getOldItem();
//                ItemStack newItem = e.getNewItem();
// 
//                // Remove passives from the armor piece being taken off
//                if (EchoManager.isArmorEcho(oldItem))
//                {
//                    EchoManifest oldManifest = EchoManager.getCodec(oldItem);
//                    if (oldManifest != null)
//                    {
//                        for (Modifier mod : oldManifest.getPassiveModifiers())
//                        {
//                            if (mod.condition().satisfies(p, null, p.getWorld()))
//                                ResolveEchoInteract.removeEquippedEffects((PassiveModifier) mod, p);
//                        }
//                    }
//                }
// 
//                // Apply passives from the armor piece being put on.
//                // Deferred one tick so the inventory state is settled before we read it.
//                if (EchoManager.isArmorEcho(newItem))
//                {
//                    Bukkit.getScheduler().runTaskLater(plugin, () ->
//                    {
//                        EchoManifest newManifest = EchoManager.getCodec(newItem);
//                        if (newManifest == null) return;
// 
//                        for (Modifier mod : newManifest.getPassiveModifiers())
//                        {
//                            if (mod.condition().satisfies(p, null, p.getWorld()))
//                                ResolveEchoInteract.resolveEquippedEffects((PassiveModifier) mod, p);
//                        }
//                    }, 1L);
//                }
//            }
// 
//            // Clear all armor-sourced passives on quit and death,
//            // exactly as EchoHeldEvent does for weapon passives.
//            @EventHandler
//            public void onQuit(PlayerQuitEvent e)
//            {
//                ResolveEchoInteract.clearArmorEffects(e.getPlayer());
//            }
// 
//            @EventHandler
//            public void onDeath(PlayerDeathEvent e)
//            {
//                ResolveEchoInteract.clearArmorEffects(e.getEntity());
//            }
// 
//        }, plugin);
//    }
// 
//    /**
//     * Re-applies all armor Echo passives currently equipped by the player.
//     * Call this after a world change or respawn so equipped armor effects
//     * are restored without requiring the player to re-equip each piece.
//     */
//    public static void reapplyArmorEffects(Player p)
//    {
//        ItemStack[] armorContents = p.getInventory().getArmorContents();
//        for (ItemStack piece : armorContents)
//        {
//            if (!EchoManager.isArmorEcho(piece)) continue;
//            EchoManifest manifest = EchoManager.getCodec(piece);
//            if (manifest == null) continue;
// 
//            for (Modifier mod : manifest.getPassiveModifiers())
//            {
//                if (mod.condition().satisfies(p, null, p.getWorld()))
//                    ResolveEchoInteract.resolveEquippedEffects((PassiveModifier) mod, p);
//            }
//        }
//    }

}
