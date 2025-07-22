package com.ouroboros.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;

public class ObsObjectCastHandler 
{
	public static void register(JavaPlugin plugin) 
    {
        Bukkit.getPluginManager().registerEvents(new Listener() 
        {
        	@EventHandler
        	public boolean onCast(PlayerInteractEvent e) 
        	{
        		ItemStack held = e.getPlayer().getInventory().getItem(EquipmentSlot.HAND);
        		
        		if (e.getHand() == null) return false;
        		if (e.getHand().equals(EquipmentSlot.OFF_HAND) || !e.getHand().equals(EquipmentSlot.HAND)) return false;
        		if (held == null || held.getType().equals(Material.AIR)) return false;		
        		if (!held.getItemMeta().getPersistentDataContainer().has(AbstractObsObject.obsObject)) return false;
        		
        		if (ObjectRegistry.itemRegistry.get(held.getItemMeta().getPersistentDataContainer().get(AbstractObsObject.obsObject, PersistentDataType.STRING)).cast(e)) 
        		{
        			if (Ouroboros.debug) 
        			{
        				PrintUtils.OBSConsoleDebug(e.getPlayer().getName()+" used: "+held.getItemMeta().getDisplayName());
        			}
        			return true;
        		}
        		return false;
        	}
        	
        }, plugin);
    }
}
