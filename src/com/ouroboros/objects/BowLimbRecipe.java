package com.ouroboros.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

import com.eol.enums.MateriaType;
import com.eol.materia.Materia;
import com.ouroboros.Ouroboros;

public class BowLimbRecipe
{
	static ItemStack item;
	public static void init()
	{
		item = Materia.generateUnrefinedMateria(MateriaType.BOW);
	}
	
	public static void register()
	{
		NamespacedKey key = new NamespacedKey(Ouroboros.instance, "bow_limb");
		ShapelessRecipe recipe = new ShapelessRecipe(key, item);
		
		recipe.addIngredient(Material.STICK);
		recipe.addIngredient(Material.STICK);
		
		Bukkit.addRecipe(recipe);
	}
	
	public static ItemStack getFinalItem()
	{
		return item;
	}
}
