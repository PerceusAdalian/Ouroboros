package com.ouroboros.utils;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionType;

public class XpUtils 
{
	public static int getXp(LivingEntity entity) 
	{
		return switch(entity.getType()) 
		{
			// Boss Mobs
			case ENDER_DRAGON                  -> 1500;
			case WITHER                        -> 2250;
			case WARDEN 					   -> 3500;
			case ELDER_GUARDIAN                -> 2500;
			case ALLAY 						   -> 500;
			
			// Human
			case VILLAGER,WANDERING_TRADER     -> 0;
			// Human Enemy
			case EVOKER,ILLUSIONER,PILLAGER,
			VINDICATOR,WITCH,ZOMBIE_VILLAGER,
			DROWNED                            -> 100;
			// Common/Passive Mobs (Small)
			case BAT,ARMADILLO,BEE,CAT,FROG,
			FOX,DONKEY,LLAMA,MULE,OCELOT,PANDA,
			PARROT,SNIFFER,SQUID,TADPOLE,TURTLE,
			WOLF                               -> 10;
			// Cattle//Common Animals
			case CAMEL,COW,PIG,CHICKEN,COD,
			GLOW_SQUID,GOAT,HORSE,POLAR_BEAR,
			PUFFERFISH,RABBIT,SALMON,SHEEP,
			TRADER_LLAMA,TROPICAL_FISH         -> 15;
			// Uncommon Passive Mobs
			case SNOW_GOLEM,IRON_GOLEM,
			MOOSHROOM,DOLPHIN,AXOLOTL          -> 35;
			// Common Evil Mobs
			case CREEPER,ZOMBIE,SPIDER,
			SKELETON 						   -> 25;
			// Slime
			case SLIME                         -> 15;
			// Uncommon Evil Mobs
			case ENDERMAN,CAVE_SPIDER,GUARDIAN,
			STRAY,HUSK,BOGGED,VEX,GIANT,BREEZE,
			PHANTOM 						   -> 50;
			// Nether
			case BLAZE,MAGMA_CUBE,STRIDER,
			ZOGLIN,PIGLIN,PIGLIN_BRUTE,GHAST,
			WITHER_SKELETON				       -> 150;
			default                            -> 5;
		};
	}
	
	public static int getXp(Material material) 
	{
		return switch (material) 
		{
			// Overworld Variants
			case COAL_ORE,COPPER_ORE,
			LAPIS_ORE,REDSTONE_ORE     	-> 10;
			case IRON_ORE              	-> 15;
			case GOLD_ORE              	-> 20;
			case EMERALD_ORE           	-> 50;
			case DIAMOND_ORE          	-> 100;
			// Nether Variants
			case DEEPSLATE_COAL_ORE,
			DEEPSLATE_COPPER_ORE,
			DEEPSLATE_LAPIS_ORE,
			DEEPSLATE_REDSTONE_ORE     	-> 20;
			case GLOWSTONE 			   	-> 25;
			case DEEPSLATE_IRON_ORE    	-> 30;
			case NETHER_GOLD_ORE, 
			NETHER_QUARTZ_ORE 	       	-> 35;
			case DEEPSLATE_GOLD_ORE    	-> 40;
			case DEEPSLATE_EMERALD_ORE 	-> 75;
			case DEEPSLATE_DIAMOND_ORE 	-> 150;
			case ANCIENT_DEBRIS        	-> 200;
			// Woods
			case OAK_LOG               	-> 10;
			case BIRCH_LOG,DARK_OAK_LOG,
			ACACIA_LOG,SPRUCE_LOG      	-> 15;
			case MANGROVE_LOG          	-> 15;
			case JUNGLE_LOG            	-> 20;
			case CHERRY_LOG 		   	-> 25;
			default 				   	-> 0;
		};
	}
	
	public static int getXp(PotionType potionType) 
	{
		return switch (potionType) 
		{
			case MUNDANE -> 10;
			default -> 5;
		};
	}
}
