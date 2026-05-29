package com.ouroboros.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryUtils 
{
	public static Set<Integer> getOpenSlots(Player p)
	{
		Set<Integer> openSlots = new HashSet<>();
		
		PlayerInventory inv = p.getInventory();
		ItemStack[] contents = inv.getStorageContents();
		
		for (int i = 0; i < contents.length; i++)
		{
			if (contents[i] == null || contents[i].getType() == Material.AIR)
			{
				openSlots.add(i);
			}
		}
		
		return openSlots;
	}
	
	public static void add(Player p, ItemStack stack)
	{
		Map<Integer, ItemStack> leftover = p.getInventory().addItem(stack);
		leftover.values().forEach(drop -> p.getWorld().dropItemNaturally(p.getLocation(), drop));
	}
	
	public static void addRecursively(Player p, ItemStack stack, int amount)
	{
		int maxStack = stack.getMaxStackSize();
		while (amount > 0)
		{
			int chunk = Math.min(amount, maxStack);
			ItemStack portion = stack.clone();
			portion.setAmount(chunk);
			add(p, portion);
			amount -= chunk;
		}
	}
	
}
