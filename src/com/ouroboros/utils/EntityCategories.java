package com.ouroboros.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.EntityCategory;

public class EntityCategories 
{
	public static final Set<EntityType> none = EnumSet.of(EntityType.PLAYER);
	
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
	
	public static final Set<EntityType> calamity = EnumSet.of(EntityType.ELDER_GUARDIAN,EntityType.ENDER_DRAGON,EntityType.WARDEN,EntityType.WITHER);
	
	public static final Set<EntityType> arcano_mobs = EnumSet.of(EntityType.WITCH,EntityType.CREAKING,EntityType.VEX, EntityType.ILLUSIONER, EntityType.ELDER_GUARDIAN,EntityType.GUARDIAN);
	
	public static final Set<EntityType> celestio_mobs = EnumSet.of(EntityType.ALLAY,EntityType.CAT,EntityType.COW,EntityType.GLOW_SQUID,
			EntityType.MOOSHROOM,EntityType.MULE,EntityType.PANDA,EntityType.FOX,EntityType.RABBIT,EntityType.SHEEP,
			EntityType.TRADER_LLAMA,EntityType.VILLAGER,EntityType.WOLF);
	
	public static final Set<EntityType> mortio_mobs = EnumSet.of(EntityType.DROWNED,EntityType.ZOGLIN,EntityType.WITHER_SKELETON,EntityType.GIANT,EntityType.SKELETON,
			EntityType.SKELETON_HORSE,EntityType.STRAY,EntityType.ZOMBIE,EntityType.ZOMBIE_HORSE,EntityType.ZOMBIFIED_PIGLIN,EntityType.HUSK,EntityType.ZOMBIE_NAUTILUS);
	
	public static final Set<EntityType> inferno_mobs = EnumSet.of(EntityType.BLAZE,EntityType.MAGMA_CUBE,
			EntityType.PIGLIN, EntityType.PIGLIN_BRUTE,EntityType.HOGLIN,EntityType.STRIDER);
	
	public static final Set<EntityType> glacio_mobs	 = EnumSet.of(EntityType.AXOLOTL,EntityType.PUFFERFISH,EntityType.COD,EntityType.DOLPHIN
			,EntityType.GLOW_SQUID,EntityType.SALMON,EntityType.SNOW_GOLEM,EntityType.SQUID,EntityType.TADPOLE,EntityType.BOGGED,
			EntityType.TROPICAL_FISH,EntityType.TURTLE,EntityType.POLAR_BEAR, EntityType.NAUTILUS);
	
	public static final Set<EntityType> aero_mobs = EnumSet.of(EntityType.BAT,EntityType.GHAST,EntityType.BEE,EntityType.BREEZE,
			EntityType.CHICKEN,EntityType.HAPPY_GHAST,EntityType.PARROT);
	
	public static final Set<EntityType> geo_mobs = EnumSet.of(EntityType.ARMADILLO,EntityType.DONKEY,EntityType.IRON_GOLEM,
			EntityType.CAMEL,EntityType.CAVE_SPIDER,EntityType.SPIDER,EntityType.GOAT,EntityType.PIG,EntityType.HORSE,
			EntityType.LLAMA,EntityType.OCELOT,EntityType.SLIME,EntityType.SNIFFER,EntityType.SILVERFISH,EntityType.FROG);
	
	public static final Set<EntityType> cosmo_mobs = EnumSet.of(EntityType.ENDERMITE,EntityType.ENDERMAN,EntityType.SHULKER,EntityType.ENDER_DRAGON,EntityType.WARDEN,EntityType.PHANTOM);
	
	public static final Set<EntityType> heresio_mobs = EnumSet.of(EntityType.CREEPER,EntityType.WITHER,EntityType.VINDICATOR,EntityType.EVOKER,
			EntityType.PILLAGER,EntityType.RAVAGER, EntityType.ZOMBIE_VILLAGER);

	public static final Set<EntityType> bugs = EnumSet.of(EntityType.BEE, EntityType.SILVERFISH, EntityType.SPIDER, EntityType.CAVE_SPIDER);
	
	public static final Set<EntityType> elemental = EnumSet.of(EntityType.BLAZE,EntityType.MAGMA_CUBE,
			EntityType.PIGLIN, EntityType.PIGLIN_BRUTE,EntityType.HOGLIN,EntityType.STRIDER,
			EntityType.AXOLOTL,EntityType.PUFFERFISH,EntityType.COD,EntityType.DOLPHIN,
			EntityType.GLOW_SQUID,EntityType.SALMON,EntityType.SNOW_GOLEM,EntityType.SQUID,EntityType.TADPOLE,EntityType.BOGGED,
			EntityType.TROPICAL_FISH,EntityType.TURTLE,EntityType.POLAR_BEAR, EntityType.NAUTILUS,
			EntityType.BAT,EntityType.GHAST,EntityType.BEE,EntityType.BREEZE,
			EntityType.CHICKEN,EntityType.HAPPY_GHAST,EntityType.PARROT,
			EntityType.ARMADILLO,EntityType.DONKEY,EntityType.IRON_GOLEM,
			EntityType.CAMEL,EntityType.CAVE_SPIDER,EntityType.SPIDER,EntityType.GOAT,EntityType.PIG,EntityType.HORSE,
			EntityType.LLAMA,EntityType.OCELOT,EntityType.SLIME,EntityType.SNIFFER,EntityType.SILVERFISH,EntityType.FROG);

	public static boolean canAccept(Entity entity, EntityCategory category)
	{
		return EntityCategory.get(category).contains(entity.getType()) ? true : false;
	}
	
	
	private static final Map<EntityCategory, Set<ElementType>> WEAKNESSES = Map.of(
	    EntityCategory.CELESTIO_MOBS,  EnumSet.of(ElementType.MORTIO, ElementType.COSMO, ElementType.HERESIO),
	    EntityCategory.MORTIO_MOBS,    EnumSet.of(ElementType.CELESTIO, ElementType.COSMO, ElementType.HERESIO),
	    
	    EntityCategory.INFERNO_MOBS,   EnumSet.of(ElementType.GLACIO, ElementType.GEO),
	    EntityCategory.GLACIO_MOBS,    EnumSet.of(ElementType.INFERNO, ElementType.BLUNT, ElementType.CORROSIVE, ElementType.COMBUST),
	    EntityCategory.AERO_MOBS,      EnumSet.of(ElementType.GEO, ElementType.GLACIO, ElementType.COMBUST),
	    EntityCategory.GEO_MOBS,       EnumSet.of(ElementType.AERO, ElementType.CRUSH, ElementType.BLAST),
	    
	    EntityCategory.COSMO_MOBS,     EnumSet.of(ElementType.COSMO, ElementType.HERESIO, ElementType.ARCANO),
	    EntityCategory.HERESIO_MOBS,   EnumSet.of(ElementType.COSMO, ElementType.ARCANO));

	private static final Map<EntityCategory, Set<ElementType>> RESISTANCES = Map.of(
	    EntityCategory.CELESTIO_MOBS,  EnumSet.of(ElementType.INFERNO, ElementType.SLASH),
	    EntityCategory.MORTIO_MOBS,    EnumSet.of(ElementType.INFERNO, ElementType.CORROSIVE),
	    
	    EntityCategory.INFERNO_MOBS,   EnumSet.of(ElementType.AERO, ElementType.PIERCE, ElementType.PUNCTURE),
	    EntityCategory.GLACIO_MOBS,    EnumSet.of(ElementType.GEO, ElementType.SLASH, ElementType.PIERCE, ElementType.PUNCTURE),
	    EntityCategory.AERO_MOBS,      EnumSet.of(ElementType.MORTIO, ElementType.PIERCE, ElementType.PUNCTURE),
	    EntityCategory.GEO_MOBS,       EnumSet.of(ElementType.SLASH, ElementType.BLUNT, ElementType.PUNCTURE, ElementType.PIERCE),
	    
	    EntityCategory.COSMO_MOBS,     EnumSet.of(ElementType.BLUNT, ElementType.SLASH, ElementType.PUNCTURE, ElementType.PIERCE, ElementType.CORROSIVE, ElementType.COMBUST),
	    EntityCategory.HERESIO_MOBS,   EnumSet.of(ElementType.BLUNT, ElementType.SLASH, ElementType.PUNCTURE, ElementType.PIERCE, ElementType.CORROSIVE, ElementType.COMBUST));

	private static final Map<EntityCategory, Set<ElementType>> IMMUNITIES = Map.of(
	    EntityCategory.CELESTIO_MOBS,  EnumSet.of(ElementType.CELESTIO),
	    EntityCategory.MORTIO_MOBS,    EnumSet.of(ElementType.MORTIO),
	    
	    EntityCategory.INFERNO_MOBS,   EnumSet.of(ElementType.INFERNO, ElementType.COMBUST, ElementType.BLAST),
	    EntityCategory.GLACIO_MOBS,    EnumSet.of(ElementType.GLACIO),
	    EntityCategory.AERO_MOBS,      EnumSet.of(ElementType.AERO),
	    EntityCategory.GEO_MOBS,       EnumSet.of(ElementType.CORROSIVE, ElementType.COMBUST),
	    
	    EntityCategory.COSMO_MOBS,     EnumSet.of(ElementType.INFERNO, ElementType.GLACIO, ElementType.AERO, ElementType.GEO),
	    EntityCategory.HERESIO_MOBS,   EnumSet.of(ElementType.CELESTIO, ElementType.MORTIO, ElementType.HERESIO));

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
	    if (cosmo_mobs.contains(entityType)) return ElementType.COSMO;
	    if (heresio_mobs.contains(entityType))  return ElementType.HERESIO;
	    if (arcano_mobs.contains(entityType)) return ElementType.ARCANO;
	    if (none.contains(entityType)) return ElementType.NONE;
	    return ElementType.NONE;
	}
	
	public static List<String> asList() 
	{
		List<String> entityNames = new ArrayList<>();
		for (EntityType type : EntityType.values())
		{
		  entityNames.add(type.name());
		}
		return entityNames;	
	}
}
