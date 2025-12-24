package com.ouroboros.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.Ouroboros;

public class ItemCollector 
{
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
		if (!e.getPlayer().isOp() && Ouroboros.debug != true)
		{
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
				e.getItem().setAmount(0), 1);
		}
	}
}
