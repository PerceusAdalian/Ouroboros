package com.ouroboros.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import com.ouroboros.Ouroboros;
import com.ouroboros.objects.instances.HealthCrystal;

public class HealthCrystalRecipe
{
	static ItemStack item;
	public static void init()
	{
		item = new HealthCrystal().toItemStack();
	}
	
	public static void register()
	{
		NamespacedKey key = new NamespacedKey(Ouroboros.instance, "hp_crystal");
		ShapelessRecipe recipe = new ShapelessRecipe(key, item);
		
		recipe.addIngredient(Material.EMERALD);
		recipe.addIngredient(Material.GHAST_TEAR);
		
		Bukkit.addRecipe(recipe);
	}
	
	public static ItemStack getFinalItem()
	{
		return item;
	}
}
