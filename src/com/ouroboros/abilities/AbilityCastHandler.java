package com.ouroboros.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.accounts.PlayerData;

public class AbilityCastHandler implements Listener
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
        		
        		for (AbstractOBSAbility ability : AbilityRegistry.abilityRegistry.values()) 
        		{
        			if (PlayerData.getPlayer(e.getPlayer().getUniqueId()).getAbility(ability).isActive() && ability.cast(e)) 
        			{
        				return true;
        			}
        		}
        		
        		return false;
        	}
        	
        }, plugin);
    }
}
