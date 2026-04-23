package com.ouroboros.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.ouroboros.Ouroboros;

public class ItemCollector 
{
	public static void remove(PlayerInteractEvent e, int value)
	{
		if (Ouroboros.debug == false)
		{
			ItemStack stack = e.getPlayer().getInventory().getItemInOffHand();
			int newAmount = stack.getAmount() - value;
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				stack.setAmount(newAmount < 0 ? 0 : newAmount), 1);
		}
	}
	
	public static void remove(PlayerInteractEvent e) 
	{
		if (Ouroboros.debug == false) 
		{
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
				e.getItem().setAmount(e.getItem().getAmount() - 1), 1);			
		}
	}
	
	public static void removeAll(PlayerInteractEvent e)
	{
		if (Ouroboros.debug == false)
		{
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
				e.getItem().setAmount(0), 1);
		}
	}
}
