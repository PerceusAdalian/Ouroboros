package com.ouroboros.utils;

import org.bukkit.Material;

import com.ouroboros.enums.ElementType;

public class ResolveCombatElement 
{
	public static ElementType getFromMaterial(Material m)
	{
		ElementType eType = switch(m)
		{
			case Material.WOODEN_SWORD,Material.STONE_SWORD,Material.IRON_SWORD,Material.GOLDEN_SWORD,
			Material.DIAMOND_SWORD,Material.NETHERITE_SWORD,Material.WOODEN_HOE, Material.STONE_HOE, 
			Material.IRON_HOE, Material.GOLDEN_HOE,Material.DIAMOND_HOE, Material.NETHERITE_HOE 		-> eType = ElementType.SLASH;
			case Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE,
			Material.DIAMOND_AXE, Material.NETHERITE_AXE 												-> eType = ElementType.SEVER;
			case Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, 
			Material.GOLDEN_PICKAXE,Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE 				-> eType = ElementType.PIERCE;
			case Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, 
			Material.GOLDEN_SHOVEL,Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL, 
			Material.AIR, Material.MACE 																-> eType = ElementType.BLUNT;
			case Material.TRIDENT -> ElementType.IMPALE;
			
			default 																					-> eType = ElementType.NEUTRAL;
		};
		return eType;
	}
}
