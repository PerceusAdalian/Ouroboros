package com.ouroboros.utils;

import org.bukkit.Material;

import com.ouroboros.enums.ElementType;

public class ResolveCombatElement 
{
	public static ElementType getFromMaterial(Material m)
	{
		ElementType eType = switch(m)
		{
			case WOODEN_SWORD,STONE_SWORD,IRON_SWORD,GOLDEN_SWORD, COPPER_HOE, 
			COPPER_SWORD,DIAMOND_SWORD,NETHERITE_SWORD,WOODEN_HOE, STONE_HOE, 
			IRON_HOE, GOLDEN_HOE,DIAMOND_HOE, NETHERITE_HOE 						-> eType = ElementType.SLASH;
			case WOODEN_AXE, STONE_AXE, IRON_AXE, GOLDEN_AXE, COPPER_AXE,
			DIAMOND_AXE, NETHERITE_AXE 												-> eType = ElementType.SEVER;
			case TRIDENT, WOODEN_PICKAXE, STONE_PICKAXE, IRON_PICKAXE, 
			GOLDEN_PICKAXE,DIAMOND_PICKAXE, NETHERITE_PICKAXE, COPPER_PICKAXE,
			WOODEN_SPEAR,COPPER_SPEAR,IRON_SPEAR,GOLDEN_SPEAR,
			DIAMOND_SPEAR, NETHERITE_SPEAR											-> eType = ElementType.PIERCE;
			case WOODEN_SHOVEL, STONE_SHOVEL, IRON_SHOVEL, COPPER_SHOVEL,
			GOLDEN_SHOVEL,DIAMOND_SHOVEL, NETHERITE_SHOVEL, AIR, MACE 				-> eType = ElementType.BLUNT;
			
			default 																-> eType = ElementType.NEUTRAL;
		};
		return eType;
	}
}
