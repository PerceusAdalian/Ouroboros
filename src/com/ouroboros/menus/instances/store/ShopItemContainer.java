package com.ouroboros.menus.instances.store;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;

public class ShopItemContainer 
{
	public static final Map<Material, Integer> itemTable = new HashMap<>();
	
	public static void init() 
	{
		itemTable.put(Material.DIRT, 5);
		itemTable.put(Material.OAK_WOOD, 10);
		itemTable.put(Material.BREAD, 15);
	}
}
