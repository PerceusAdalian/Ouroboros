package com.ouroboros.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import com.ouroboros.Ouroboros;
import com.ouroboros.objects.instances.ManaGem;

public class ManaGemRecipe
{
	static ItemStack item;
	public static void init()
	{
		item = new ManaGem().toItemStack();
	}
	
	public static void register()
	{
		NamespacedKey key = new NamespacedKey(Ouroboros.instance, "mana_gem");
		ShapelessRecipe recipe = new ShapelessRecipe(key, item);
		
		recipe.addIngredient(Material.LAPIS_LAZULI);
		recipe.addIngredient(Material.GHAST_TEAR);
		
		Bukkit.addRecipe(recipe);
	}
	
	public static ItemStack getFinalItem()
	{
		return item;
	}
}
