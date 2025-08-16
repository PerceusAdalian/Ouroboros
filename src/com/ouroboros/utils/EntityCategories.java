package com.ouroboros.utils;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.ouroboros.enums.EntityCategory;

public class EntityCategories 
{
	public static final Set<EntityType>	any = EnumSet.of(
			EntityType.ALLAY,EntityType.ARMADILLO,EntityType.AXOLOTL,EntityType.BAT,EntityType.BEE,
			EntityType.BLAZE,EntityType.BOGGED,EntityType.BREEZE,EntityType.CAMEL,EntityType.CAT,EntityType.CAVE_SPIDER,
			EntityType.CHICKEN,EntityType.COD,EntityType.COW,EntityType.CREAKING,EntityType.CREEPER, EntityType.DOLPHIN,
			EntityType.DONKEY,EntityType.DROWNED,EntityType.ELDER_GUARDIAN,EntityType.ENDER_DRAGON,
			EntityType.ENDERMAN,EntityType.ENDERMITE,EntityType.EVOKER,EntityType.FOX,EntityType.FROG,EntityType.GHAST,
			EntityType.GIANT,EntityType.GLOW_SQUID,EntityType.GOAT,EntityType.GUARDIAN,EntityType.HAPPY_GHAST,
			EntityType.HOGLIN,EntityType.HORSE,EntityType.HUSK,EntityType.ILLUSIONER,EntityType.IRON_GOLEM,
			EntityType.LLAMA,EntityType.MAGMA_CUBE,EntityType.MOOSHROOM,EntityType.MULE,EntityType.OCELOT,
			EntityType.PANDA,EntityType.PARROT,EntityType.PHANTOM,EntityType.PIG,EntityType.PIGLIN,EntityType.PIGLIN_BRUTE,
			EntityType.PILLAGER,EntityType.POLAR_BEAR,EntityType.PUFFERFISH,EntityType.RABBIT,EntityType.RAVAGER,
			EntityType.SALMON,EntityType.SHEEP,EntityType.SHULKER,EntityType.SILVERFISH,
			EntityType.SKELETON,EntityType.SKELETON_HORSE,EntityType.SLIME,EntityType.SNIFFER,EntityType.SNOW_GOLEM,
			EntityType.SPIDER,EntityType.SQUID,EntityType.STRAY,EntityType.STRIDER,EntityType.TADPOLE,EntityType.TRADER_LLAMA,
			EntityType.TROPICAL_FISH,EntityType.TURTLE,EntityType.VEX,EntityType.VILLAGER,EntityType.VINDICATOR,
			EntityType.WANDERING_TRADER,EntityType.WARDEN,EntityType.WITCH,EntityType.WITHER,EntityType.WITHER_SKELETON,
			EntityType.WOLF,EntityType.ZOGLIN,EntityType.ZOMBIE,EntityType.ZOMBIE_HORSE,EntityType.ZOMBIE_VILLAGER,
			EntityType.ZOMBIFIED_PIGLIN);
	
	public static final Set<EntityType> living = EnumSet.of(
			EntityType.ALLAY,EntityType.ARMADILLO,EntityType.AXOLOTL,EntityType.BAT,EntityType.BEE,
			EntityType.CAMEL,EntityType.CAT,EntityType.CAVE_SPIDER,EntityType.CHICKEN,EntityType.COD,EntityType.COW,EntityType.CREAKING,
			EntityType.CREEPER,EntityType.DOLPHIN,EntityType.DONKEY,EntityType.ELDER_GUARDIAN,EntityType.EVOKER,EntityType.FOX,EntityType.FROG,
			EntityType.GLOW_ITEM_FRAME,EntityType.GOAT,EntityType.GUARDIAN,EntityType.HORSE,EntityType.ILLUSIONER,EntityType.LLAMA,EntityType.MOOSHROOM,
			EntityType.MULE,EntityType.OCELOT,EntityType.PANDA,EntityType.PARROT,EntityType.PIG,EntityType.PILLAGER,EntityType.POLAR_BEAR,EntityType.PUFFERFISH,
			EntityType.RABBIT,EntityType.RAVAGER,EntityType.SALMON,EntityType.SHEEP,EntityType.SILVERFISH,EntityType.SLIME,EntityType.SNIFFER,EntityType.SPIDER,EntityType.SQUID,
			EntityType.TADPOLE,EntityType.TRADER_LLAMA,EntityType.TROPICAL_FISH,EntityType.TURTLE,EntityType.VILLAGER,EntityType.VINDICATOR,EntityType.WANDERING_TRADER,EntityType.WITCH,
			EntityType.WOLF);
	
	public static final Set<EntityType> undead = EnumSet.of(EntityType.DROWNED,EntityType.GHAST,EntityType.GIANT,EntityType.HAPPY_GHAST,EntityType.PHANTOM,EntityType.SKELETON,
			EntityType.SKELETON_HORSE,EntityType.STRAY,EntityType.VEX,EntityType.WITHER,EntityType.WITHER_SKELETON,EntityType.ZOGLIN,EntityType.ZOMBIE,EntityType.ZOMBIE_HORSE,
			EntityType.ZOMBIE_VILLAGER,EntityType.ZOMBIFIED_PIGLIN,EntityType.HUSK);

	public static final Set<EntityType> aquatic = EnumSet.of(EntityType.BOGGED,EntityType.COD,EntityType.CREAKING,EntityType.DOLPHIN,EntityType.DROWNED,
			EntityType.ELDER_GUARDIAN,EntityType.GUARDIAN,EntityType.GLOW_SQUID,EntityType.SALMON,EntityType.SNOW_GOLEM,EntityType.SQUID,EntityType.TADPOLE,EntityType.FROG,
			EntityType.TROPICAL_FISH,EntityType.TURTLE);
	
	public static final Set<EntityType> flying = EnumSet.of(EntityType.ALLAY,EntityType.BAT,EntityType.BEE,EntityType.BLAZE,EntityType.BREEZE,EntityType.CHICKEN,
			EntityType.ENDER_DRAGON,EntityType.GHAST,EntityType.HAPPY_GHAST,EntityType.PARROT,EntityType.PHANTOM,EntityType.VEX,EntityType.WITHER);
	
	public static final Set<EntityType> infernal = EnumSet.of(EntityType.BLAZE,EntityType.MAGMA_CUBE,EntityType.HOGLIN,EntityType.PIGLIN,EntityType.PIGLIN_BRUTE,
			EntityType.STRIDER,EntityType.GHAST,EntityType.WITHER,EntityType.WITHER_SKELETON,EntityType.ZOGLIN,EntityType.ZOMBIFIED_PIGLIN);
	
	public static final Set<EntityType> ethereal = EnumSet.of(EntityType.ALLAY,EntityType.BREEZE,EntityType.ENDER_DRAGON,EntityType.ENDERMAN,EntityType.ENDERMITE,EntityType.WITCH,
			EntityType.EVOKER,EntityType.VINDICATOR,EntityType.VEX,EntityType.SHULKER,EntityType.PHANTOM,EntityType.WARDEN);
	
	public static final Set<EntityType> occultic = EnumSet.of(EntityType.EVOKER,EntityType.ILLUSIONER,EntityType.PILLAGER,EntityType.RAVAGER,EntityType.VEX,EntityType.WITCH,
			EntityType.WITHER,EntityType.WITHER_SKELETON,EntityType.VINDICATOR);
	
	public static final Set<EntityType> calamity = EnumSet.of(EntityType.ALLAY,EntityType.ELDER_GUARDIAN,EntityType.ENDER_DRAGON,EntityType.WARDEN,EntityType.WITHER);
	
	public static final Set<EntityType> celestio_mobs = EnumSet.of(EntityType.ALLAY,EntityType.ARMADILLO,EntityType.AXOLOTL,EntityType.BAT,EntityType.BEE,EntityType.CAMEL,
			EntityType.CAT,EntityType.CHICKEN,EntityType.COD,EntityType.COW,EntityType.DOLPHIN,EntityType.DONKEY,EntityType.FOX,EntityType.FROG,EntityType.GHAST,EntityType.GLOW_SQUID,
			EntityType.GOAT,EntityType.GUARDIAN,EntityType.ELDER_GUARDIAN,EntityType.HAPPY_GHAST,EntityType.HORSE,EntityType.IRON_GOLEM,EntityType.LLAMA,EntityType.MOOSHROOM,EntityType.MULE,
			EntityType.OCELOT,EntityType.PANDA,EntityType.PARROT,EntityType.PIG,EntityType.PUFFERFISH,EntityType.POLAR_BEAR,EntityType.RABBIT,EntityType.SALMON,EntityType.SHEEP,EntityType.SLIME,EntityType.SNOW_GOLEM,
			EntityType.SQUID,EntityType.TADPOLE,EntityType.TRADER_LLAMA,EntityType.TROPICAL_FISH,EntityType.TURTLE,EntityType.VILLAGER,EntityType.WOLF);
	
	public static final Set<EntityType> mortio_mobs = EnumSet.of(EntityType.DROWNED,EntityType.CREEPER,EntityType.GHAST,EntityType.GIANT,EntityType.SKELETON,
			EntityType.SKELETON_HORSE,EntityType.STRAY,EntityType.VEX,EntityType.WITHER,EntityType.WITHER_SKELETON,EntityType.ZOGLIN,EntityType.ZOMBIE,EntityType.ZOMBIE_HORSE,
			EntityType.ZOMBIE_VILLAGER,EntityType.ZOMBIFIED_PIGLIN,EntityType.HUSK,EntityType.BAT);
	
	public static final Set<EntityType> inferno_mobs = EnumSet.of(EntityType.BLAZE,EntityType.MAGMA_CUBE,EntityType.PIGLIN,EntityType.ZOGLIN,EntityType.HOGLIN,EntityType.STRIDER);
	
	public static final Set<EntityType> glacio_mobs	 = EnumSet.of(EntityType.BOGGED,EntityType.COD,EntityType.CREAKING,EntityType.DOLPHIN,EntityType.DROWNED,
			EntityType.ELDER_GUARDIAN,EntityType.GUARDIAN,EntityType.GLOW_SQUID,EntityType.SALMON,EntityType.SNOW_GOLEM,EntityType.SQUID,EntityType.TADPOLE,EntityType.FROG,
			EntityType.TROPICAL_FISH,EntityType.TURTLE,EntityType.STRAY,EntityType.POLAR_BEAR,EntityType.FOX,EntityType.PANDA);
	
	public static final Set<EntityType> aero_mobs = EnumSet.of(EntityType.BAT,EntityType.BEE,EntityType.BREEZE,EntityType.CHICKEN,
			EntityType.GHAST,EntityType.HAPPY_GHAST,EntityType.PARROT,EntityType.VEX);
	
	public static final Set<EntityType> geo_mobs = EnumSet.of(EntityType.ARMADILLO,EntityType.BOGGED,EntityType.CAMEL,EntityType.CAVE_SPIDER,EntityType.SPIDER,
			EntityType.DONKEY,EntityType.GOAT,EntityType.GLOW_SQUID,EntityType.IRON_GOLEM,EntityType.HUSK,EntityType.HORSE,EntityType.LLAMA,EntityType.OCELOT,EntityType.MULE,
			EntityType.PIG,EntityType.SLIME,EntityType.SNIFFER,EntityType.SNOW_GOLEM,EntityType.TADPOLE,EntityType.FROG,EntityType.RABBIT,EntityType.SILVERFISH,EntityType.TURTLE,
			EntityType.WOLF);
	
	public static final Set<EntityType> cosmo_mobs = EnumSet.of(EntityType.ENDERMITE,EntityType.ENDERMAN,EntityType.SHULKER,EntityType.ENDER_DRAGON,EntityType.WARDEN,EntityType.PHANTOM,
			EntityType.VEX,EntityType.ILLUSIONER,EntityType.VINDICATOR,EntityType.ALLAY);
	
	/**
	 * Method no longer requires use, but substituted by "any".
	 */
	@Deprecated
	public static final Set<Set<EntityType>> categories = Set.of(any, living, undead, aquatic, flying, infernal, ethereal, occultic, calamity, celestio_mobs,
			mortio_mobs, inferno_mobs, glacio_mobs, aero_mobs, geo_mobs, cosmo_mobs);
	
	public static boolean canAccept(Entity entity, EntityCategory category)
	{
		return EntityCategory.get(category).contains(entity.getType()) ? true : false;
	}
}
