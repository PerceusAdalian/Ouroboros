package com.ouroboros.accounts;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

public class XpUtils 
{
	//Combat
	public static int getXp(LivingEntity entity) 
	{
		return switch(entity.getType()) 
		{
			case ENDER_DRAGON                  -> 1500;
			case WITHER                        -> 2250;
			case WARDEN 					   -> 3500;
			case ELDER_GUARDIAN                -> 2500;
			case ALLAY 						   -> 500;
			case VILLAGER,WANDERING_TRADER     -> 0;
			case BAT,ARMADILLO,BEE,CAT,FROG,
			FOX,DONKEY,LLAMA,MULE,OCELOT,PANDA,
			PARROT,SNIFFER,SQUID,TADPOLE,TURTLE,
			WOLF                               -> 10;
			case CAMEL,COW,PIG,CHICKEN,COD,
			GLOW_SQUID,GOAT,HORSE,POLAR_BEAR,
			PUFFERFISH,RABBIT,SALMON,SHEEP,
			TRADER_LLAMA,TROPICAL_FISH,SLIME   -> 15;
			case CREEPER,ZOMBIE,SPIDER,
			SKELETON 						   -> 25;
			case SNOW_GOLEM,IRON_GOLEM,
			MOOSHROOM,DOLPHIN,AXOLOTL          -> 35;
			case ENDERMAN,CAVE_SPIDER,GUARDIAN,
			STRAY,HUSK,BOGGED,VEX,GIANT,BREEZE,
			PHANTOM 						   -> 50;
			case EVOKER,ILLUSIONER,PILLAGER,
			VINDICATOR,WITCH,ZOMBIE_VILLAGER,
			DROWNED                            -> 100;
			case BLAZE,MAGMA_CUBE,STRIDER,
			ZOGLIN,PIGLIN,PIGLIN_BRUTE,GHAST,
			WITHER_SKELETON				       -> 150;
			
			default                            -> 5;
		};
	}
	
	//Mining/WoodCutting
	public static int getXp(Material material) 
	{
		return switch (material) 
		{
			case COAL_ORE,COPPER_ORE,
			LAPIS_ORE,REDSTONE_ORE     	-> 10;
			case IRON_ORE              	-> 15;
			case GOLD_ORE,DEEPSLATE_COAL_ORE,
			DEEPSLATE_COPPER_ORE,
			DEEPSLATE_LAPIS_ORE,
			DEEPSLATE_REDSTONE_ORE      -> 20;
			case EMERALD_ORE           	-> 50;
			case DIAMOND_ORE          	-> 100;
			case GLOWSTONE 			   	-> 25;
			case DEEPSLATE_IRON_ORE    	-> 30;
			case NETHER_GOLD_ORE, 
			NETHER_QUARTZ_ORE 	       	-> 35;
			case DEEPSLATE_GOLD_ORE    	-> 40;
			case DEEPSLATE_EMERALD_ORE 	-> 75;
			case DEEPSLATE_DIAMOND_ORE 	-> 150;
			case ANCIENT_DEBRIS        	-> 200;
			case OAK_LOG               	-> 10;
			case BIRCH_LOG,DARK_OAK_LOG,
			ACACIA_LOG,SPRUCE_LOG      	-> 15;
			case MANGROVE_LOG          	-> 15;
			case JUNGLE_LOG            	-> 20;
			case CHERRY_LOG 		   	-> 25;
			default 				   	-> 10;
		};
	}
	
	public static final Map<Biome, Integer> biomeXpMap = new HashMap<>();
	
	static
	{
		biomeXpMap.put(Biome.JUNGLE, 50);
		biomeXpMap.put(Biome.SPARSE_JUNGLE, 45);
		biomeXpMap.put(Biome.DESERT, 40);
		biomeXpMap.put(Biome.BADLANDS, 50);
		biomeXpMap.put(Biome.COLD_OCEAN, 65);
		biomeXpMap.put(Biome.WARM_OCEAN, 65);
		biomeXpMap.put(Biome.TAIGA, 45);
		biomeXpMap.put(Biome.SNOWY_TAIGA, 40);
		biomeXpMap.put(Biome.OLD_GROWTH_BIRCH_FOREST, 25);
		biomeXpMap.put(Biome.OLD_GROWTH_SPRUCE_TAIGA, 25);
	}
	
	public static int getXp(Biome b)
	{
		if (!biomeXpMap.containsKey(b)) return 10;
		return biomeXpMap.get(b);
	}
	
	// Crafting
	public static int getXp(ItemStack stack) 
	{
		return switch (stack.getType()) 
		{
			case LEATHER,PAPER,ARROW,BREAD,COOKIE,MUSHROOM_STEW,BEETROOT_SOUP,MELON,
			SHEARS,FLINT_AND_STEEL,WRITABLE_BOOK,RAIL,CHEST,TRAPPED_CHEST,
			REDSTONE_TORCH,LEVER,BARREL,FURNACE,REPEATER,COMPARATOR,OAK_BUTTON,
			STONE_BUTTON,OAK_PRESSURE_PLATE,STONE_PRESSURE_PLATE,
			LIGHT_WEIGHTED_PRESSURE_PLATE,HEAVY_WEIGHTED_PRESSURE_PLATE,
			TRIPWIRE_HOOK,OAK_DOOR,IRON_DOOR,OAK_FENCE_GATE,OAK_TRAPDOOR,
			IRON_TRAPDOOR,TNT,ARMOR_STAND									  			-> 10;
			case GLASS_BOTTLE,FIREWORK_STAR,BOOK,ENDER_EYE,BLAZE_POWDER,
			SPECTRAL_ARROW,CAKE,PUMPKIN_PIE,RABBIT_STEW,WOODEN_SWORD,BOW,
			WOODEN_SHOVEL,WOODEN_PICKAXE,WOODEN_AXE,WOODEN_HOE,LEATHER_HELMET,
			LEATHER_CHESTPLATE,LEATHER_LEGGINGS,LEATHER_BOOTS,FISHING_ROD     			-> 15;
			case BLACK_DYE,WHITE_DYE,GRAY_DYE,LIGHT_GRAY_DYE,BROWN_DYE,RED_DYE,
			ORANGE_DYE,YELLOW_DYE,LIME_DYE,GREEN_DYE,CYAN_DYE,BLUE_DYE,LIGHT_BLUE_DYE,
			MAGENTA_DYE,PURPLE_DYE,PINK_DYE,STONE_SWORD,STONE_SHOVEL,STONE_PICKAXE
			,STONE_AXE,STONE_HOE,SHIELD,CHAINMAIL_HELMET,CHAINMAIL_CHESTPLATE,
			CHAINMAIL_LEGGINGS,CHAINMAIL_BOOTS,TURTLE_HELMET 				  			-> 20;
			case LECTERN,CRAFTER,DISPENSER,DROPPER,DAYLIGHT_DETECTOR,HOPPER,
			LIGHTNING_ROD,CAULDRON,BELL,REDSTONE_LAMP,PISTON,JUKEBOX,OBSERVER 			-> 25;
			case IRON_SWORD,IRON_SHOVEL,IRON_PICKAXE,IRON_AXE,IRON_HOE,
			IRON_HELMET,IRON_CHESTPLATE,IRON_LEGGINGS,IRON_BOOTS,
			CROSSBOW,MINECART,STONECUTTER,CARTOGRAPHY_TABLE,NOTE_BLOCK,
			FLETCHING_TABLE,SMITHING_TABLE,GRINDSTONE,LOOM,SMOKER,BLAST_FURNACE,
			CAMPFIRE,COMPOSTER,BEEHIVE 										  			-> 30;
			case GOLDEN_SWORD,GOLDEN_SHOVEL,GOLDEN_PICKAXE,GOLDEN_AXE,
			GOLDEN_HOE,GOLDEN_HELMET,GOLDEN_CHESTPLATE,GOLDEN_LEGGINGS,
			GOLDEN_BOOTS,TIPPED_ARROW 										  			-> 35;
			case GLISTERING_MELON_SLICE,MAGMA_CREAM,GOLDEN_CARROT,
			GOLDEN_APPLE,BRUSH,LEAD,COMPASS,CLOCK,SPYGLASS,DIAMOND_SWORD,
			DIAMOND_SHOVEL,DIAMOND_PICKAXE,DIAMOND_AXE,DIAMOND_HOE,DIAMOND_HELMET,
			DIAMOND_CHESTPLATE,DIAMOND_LEGGINGS,DIAMOND_BOOTS,MACE,TRIDENT	  			-> 40;
			case ANVIL,CONDUIT,LODESTONE,ENCHANTING_TABLE,
			BREWING_STAND,RECOVERY_COMPASS       									    -> 50;
			case NETHERITE_UPGRADE_SMITHING_TEMPLATE,SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE,
			DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,
			COAST_ARMOR_TRIM_SMITHING_TEMPLATE,WILD_ARMOR_TRIM_SMITHING_TEMPLATE,
			WARD_ARMOR_TRIM_SMITHING_TEMPLATE,EYE_ARMOR_TRIM_SMITHING_TEMPLATE,
			VEX_ARMOR_TRIM_SMITHING_TEMPLATE,TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,
			SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE,RIB_ARMOR_TRIM_SMITHING_TEMPLATE,
			SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE,
			SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE,SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE,
			RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,HOST_ARMOR_TRIM_SMITHING_TEMPLATE,
			FLOW_ARMOR_TRIM_SMITHING_TEMPLATE,BOLT_ARMOR_TRIM_SMITHING_TEMPLATE,
			NETHERITE_SWORD,NETHERITE_SHOVEL,NETHERITE_PICKAXE,NETHERITE_AXE,NETHERITE_HOE,
			NETHERITE_HELMET,NETHERITE_CHESTPLATE,NETHERITE_LEGGINGS,NETHERITE_BOOTS-> 100;
			
			default -> 5;
		};
	}
	
	//Potions
	public static int getXp(PotionType potionType) 
	{
		return switch (potionType) 
		{
			case MUNDANE, AWKWARD,THICK 							    -> 10;
			case FIRE_RESISTANCE,INVISIBILITY,LEAPING,HEALING,LUCK,
			NIGHT_VISION,SLOW_FALLING,SWIFTNESS 						-> 50;
			case HARMING,POISON,INFESTED,OOZING,WEAKNESS,SLOWNESS       -> 65;
			case TURTLE_MASTER,REGENERATION,STRENGTH,WIND_CHARGED,
			WEAVING,WATER_BREATHING 									-> 75;
			case LONG_FIRE_RESISTANCE,LONG_INVISIBILITY,LONG_LEAPING,
			LONG_NIGHT_VISION,LONG_POISON,LONG_REGENERATION,
			LONG_SLOW_FALLING,LONG_SLOWNESS,LONG_STRENGTH,LONG_SWIFTNESS,
			LONG_TURTLE_MASTER,LONG_WATER_BREATHING,LONG_WEAKNESS 		-> 85;
			case STRONG_HARMING,STRONG_HEALING,STRONG_LEAPING,
			STRONG_POISON,STRONG_REGENERATION,STRONG_SLOWNESS,
			STRONG_STRENGTH,STRONG_SWIFTNESS,STRONG_TURTLE_MASTER 		-> 100;
			default -> 5;
		};
	}

	//Crops
	public static int getXp(Block block) 
	{
		return switch (block.getType()) 
		{
			case CACTUS,SUGAR_CANE,MELON_STEM,
			RED_MUSHROOM,BROWN_MUSHROOM,
			PITCHER_PLANT,TORCHFLOWER 				-> 5;
			case WHEAT,CARROTS,POTATOES,BEETROOTS   -> 10;
			case SWEET_BERRY_BUSH, CAVE_VINES, 
			CAVE_VINES_PLANT,TWISTING_VINES,
			WEEPING_VINES,GLOW_BERRIES,
			TORCHFLOWER_CROP,BAMBOO,PITCHER_CROP	->15;
			case CHORUS_FLOWER,CHORUS_PLANT,
			PUMPKIN,MELON 							-> 20;
			case NETHER_WART,COCOA 					-> 25;
			default -> 5;
		};
	}
	
	public static int getXp(Enchantment enchant) 
	{
		return switch (enchant.getTranslationKey().toLowerCase()) 
	    {
	        case "projectile_protection","soul_speed","sweeping_edge",
	        "knockback","bane_of_arthropods","smite","fire_protection",
	        "protection","punch","luck_of_the_sea","depth_strider",
	        "unbreaking"  											-> 10;
	        case "binding_curse","aqua_affinity","quick_charge",
	        "flame","fortune","vanishing_curse","blast_protection",
	        "lure","breach" 										-> 15;
	        case "channeling","density","efficiency","looting",
	        "riptide","loyalty","frost_walker","impaling" 			-> 20;
	        case "infinity","multishot","piercing","swift_sneak",
	        "thorns","feather_falling","fire_aspect" 				-> 25;
	        case "mending","silk_touch","wind_burst",
	        "respiration","power","sharpness" 						-> 50;
	        default -> 5;
	    };
	}

}
