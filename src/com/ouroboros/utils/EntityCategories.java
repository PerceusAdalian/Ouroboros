package com.ouroboros.utils;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.ouroboros.enums.ElementType;
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
			EntityType.ZOMBIFIED_PIGLIN, EntityType.NAUTILUS, EntityType.ZOMBIE_NAUTILUS);
	
	public static final Set<EntityType> calamity = EnumSet.of(EntityType.ALLAY,EntityType.ELDER_GUARDIAN,EntityType.ENDER_DRAGON,EntityType.WARDEN,EntityType.WITHER);
	
	public static final Set<EntityType> celestio_mobs = EnumSet.of(EntityType.ALLAY,
			EntityType.CAT,EntityType.COW,EntityType.FOX,EntityType.FROG,EntityType.GHAST,EntityType.GLOW_SQUID,
			EntityType.MOOSHROOM,EntityType.MULE,EntityType.PANDA,EntityType.PIG,
			EntityType.RABBIT,EntityType.SHEEP,EntityType.TRADER_LLAMA,EntityType.VILLAGER,EntityType.WOLF);
	
	public static final Set<EntityType> mortio_mobs = EnumSet.of(EntityType.DROWNED,EntityType.GIANT,EntityType.SKELETON,
			EntityType.SKELETON_HORSE,EntityType.STRAY,EntityType.WITHER,EntityType.WITHER_SKELETON,EntityType.ZOMBIE,EntityType.ZOMBIE_HORSE,
			EntityType.ZOMBIE_VILLAGER,EntityType.ZOMBIFIED_PIGLIN,EntityType.VEX,EntityType.HUSK,EntityType.ZOMBIE_NAUTILUS);
	
	public static final Set<EntityType> inferno_mobs = EnumSet.of(EntityType.BLAZE,EntityType.GHAST,EntityType.MAGMA_CUBE,EntityType.PIGLIN,EntityType.ZOGLIN,EntityType.HOGLIN,EntityType.STRIDER);
	
	public static final Set<EntityType> glacio_mobs	 = EnumSet.of(EntityType.AXOLOTL,EntityType.PUFFERFISH,EntityType.COD,EntityType.CREAKING,EntityType.DOLPHIN,
			EntityType.ELDER_GUARDIAN,EntityType.GUARDIAN,EntityType.GLOW_SQUID,EntityType.SALMON,EntityType.SNOW_GOLEM,EntityType.SQUID,EntityType.TADPOLE,EntityType.FROG,
			EntityType.TROPICAL_FISH,EntityType.TURTLE,EntityType.POLAR_BEAR, EntityType.NAUTILUS);
	
	public static final Set<EntityType> aero_mobs = EnumSet.of(EntityType.BAT,EntityType.BEE,EntityType.BREEZE,EntityType.CHICKEN,EntityType.HAPPY_GHAST,EntityType.PARROT);
	
	public static final Set<EntityType> geo_mobs = EnumSet.of(EntityType.ARMADILLO,EntityType.DONKEY,EntityType.IRON_GOLEM,
			EntityType.BOGGED,EntityType.CAMEL,EntityType.CAVE_SPIDER,EntityType.SPIDER,EntityType.GOAT,EntityType.HORSE,
			EntityType.LLAMA,EntityType.OCELOT,EntityType.SLIME,EntityType.SNIFFER,EntityType.SILVERFISH);
	
	public static final Set<EntityType> cosmo_mobs = EnumSet.of(EntityType.ENDERMITE,EntityType.ENDERMAN,EntityType.SHULKER,EntityType.ENDER_DRAGON,EntityType.WARDEN,EntityType.PHANTOM);
	
	public static final Set<EntityType> heresio_mobs = EnumSet.of(EntityType.EVOKER,EntityType.CREEPER,EntityType.ILLUSIONER,
			EntityType.PILLAGER,EntityType.RAVAGER,EntityType.WITCH,EntityType.VINDICATOR);

	public static boolean canAccept(Entity entity, EntityCategory category)
	{
		return EntityCategory.get(category).contains(entity.getType()) ? true : false;
	}
	
	private static final Map<EntityCategory, Set<ElementType>> WEAKNESSES = Map.of(
	    EntityCategory.CELESTIO_MOBS,  EnumSet.of(ElementType.MORTIO, ElementType.COSMO, ElementType.HERESIO, ElementType.SLASH),
	    EntityCategory.MORTIO_MOBS,    EnumSet.of(ElementType.CELESTIO, ElementType.COSMO, ElementType.HERESIO, ElementType.PIERCE),
	    EntityCategory.INFERNO_MOBS,   EnumSet.of(ElementType.GLACIO, ElementType.GEO),
	    EntityCategory.GLACIO_MOBS,    EnumSet.of(ElementType.INFERNO, ElementType.AERO, ElementType.BLUNT),
	    EntityCategory.GEO_MOBS,       EnumSet.of(ElementType.AERO, ElementType.INFERNO, ElementType.CORROSIVE),
	    EntityCategory.AERO_MOBS,      EnumSet.of(ElementType.GEO, ElementType.GLACIO, ElementType.COMBUST),
	    EntityCategory.COSMO_MOBS,     EnumSet.of(ElementType.HERESIO, ElementType.ARCANO),
	    EntityCategory.HERESIO_MOBS,   EnumSet.of(ElementType.COSMO, ElementType.ARCANO));

	private static final Map<EntityCategory, Set<ElementType>> RESISTANCES = Map.of(
	    EntityCategory.CELESTIO_MOBS,  EnumSet.of(ElementType.INFERNO, ElementType.SLASH),
	    EntityCategory.MORTIO_MOBS,    EnumSet.of(ElementType.INFERNO, ElementType.PIERCE),
	    EntityCategory.INFERNO_MOBS,   EnumSet.of(ElementType.AERO, ElementType.PUNCTURE, ElementType.PIERCE),
	    EntityCategory.GLACIO_MOBS,    EnumSet.of(ElementType.GEO),
	    EntityCategory.GEO_MOBS,       EnumSet.of(ElementType.CELESTIO, ElementType.BLUNT),
	    EntityCategory.AERO_MOBS,      EnumSet.of(ElementType.MORTIO, ElementType.PIERCE, ElementType.PUNCTURE));

	private static final Map<EntityCategory, Set<ElementType>> IMMUNITIES = Map.of(
	    EntityCategory.CELESTIO_MOBS,  EnumSet.of(ElementType.CELESTIO),
	    EntityCategory.MORTIO_MOBS,    EnumSet.of(ElementType.MORTIO),
	    EntityCategory.INFERNO_MOBS,   EnumSet.of(ElementType.INFERNO, ElementType.COMBUST, ElementType.BLAST),
	    EntityCategory.GLACIO_MOBS,    EnumSet.of(ElementType.GLACIO),
	    EntityCategory.AERO_MOBS,      EnumSet.of(ElementType.AERO),
	    EntityCategory.COSMO_MOBS,     EnumSet.of(ElementType.INFERNO, ElementType.GLACIO, ElementType.AERO, ElementType.GEO),
	    EntityCategory.HERESIO_MOBS,   EnumSet.of(ElementType.CELESTIO, ElementType.MORTIO));

	private static boolean matchesCategory(Entity entity, Map<EntityCategory, Set<ElementType>> table, ElementType eType) 
	{
	    return table.entrySet().stream().anyMatch(e -> canAccept(entity, e.getKey()) && e.getValue().contains(eType));
	}

	public static boolean parseUniversalWeakness(Entity entity, ElementType eType) 
	{
	    return matchesCategory(entity, WEAKNESSES, eType);
	}

	public static boolean parseUniversalResistance(Entity entity, ElementType eType) 
	{
	    return matchesCategory(entity, RESISTANCES, eType);
	}

	public static boolean parseUniversalImmunity(Entity entity, ElementType eType) 
	{
	    return matchesCategory(entity, IMMUNITIES, eType);
	}
	
	public static ElementType parseElementType(EntityType entityType)
	{
	    if (celestio_mobs.contains(entityType)) return ElementType.CELESTIO;
	    if (mortio_mobs.contains(entityType))   return ElementType.MORTIO;
	    if (inferno_mobs.contains(entityType))  return ElementType.INFERNO;
	    if (aero_mobs.contains(entityType))     return ElementType.AERO;
	    if (geo_mobs.contains(entityType))      return ElementType.GEO;
	    if (glacio_mobs.contains(entityType))   return ElementType.GLACIO;
	    if (heresio_mobs.contains(entityType))  return ElementType.HERESIO;

	    return ElementType.NONE;
	}
}
