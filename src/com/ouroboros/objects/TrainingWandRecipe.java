package com.ouroboros.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import com.ouroboros.Ouroboros;
import com.ouroboros.objects.instances.TrainingWand;

public class TrainingWandRecipe 
{
	static ItemStack item;
	public static void init()
	{
		item = new TrainingWand().toItemStack();
	}
	
	public static void register()
	{
		NamespacedKey key = new NamespacedKey(Ouroboros.instance, "trainingwand");
		ShapelessRecipe recipe = new ShapelessRecipe(key, item);
		
		recipe.addIngredient(Material.STICK);
		recipe.addIngredient(Material.RED_DYE);
		
		Bukkit.addRecipe(recipe);
	}
	
	public static ItemStack getFinalItem()
	{
		return item;
	}
}
