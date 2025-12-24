package com.ouroboros.utils;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.ouroboros.enums.AbilityMaterialClass;

public class AbilityObjectCategory 
{
	public static final Set<Material> swords = EnumSet.of(
			Material.WOODEN_SWORD,Material.STONE_SWORD,Material.IRON_SWORD,Material.GOLDEN_SWORD,
			Material.DIAMOND_SWORD,Material.NETHERITE_SWORD);
	public static final Set<Material> axes = EnumSet.of(
			Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE,
			Material.DIAMOND_AXE, Material.NETHERITE_AXE);
	public static final Set<Material> pickaxes = EnumSet.of(
			Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE,
			Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE);
	public static final Set<Material> shovels = EnumSet.of(
			Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL,
			Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL);
	public static final Set<Material> scythes = EnumSet.of(
			Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE,
			Material.DIAMOND_HOE, Material.NETHERITE_HOE);
	public static final Set<Material> bows = EnumSet.of(Material.BOW, Material.CROSSBOW);
	public static final Material fishing = Material.FISHING_ROD;
	public static final Material shears = Material.SHEARS;
	public static final Material trident = Material.TRIDENT;
	public static final Material mace = Material.MACE;
	public static final Material brush = Material.BRUSH;

	public static boolean canAccept(Player player, AbilityMaterialClass category)
	{
		ItemStack held = player.getInventory().getItem(EquipmentSlot.HAND);
		return AbilityMaterialClass.get(category).contains(held.getType()) ? true : false;
	}
	
}
