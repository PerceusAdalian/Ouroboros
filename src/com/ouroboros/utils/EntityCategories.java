package com.ouroboros.utils;

import java.util.EnumSet;
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
	
	public static boolean parseUniversalWeakness(Entity entity, ElementType eType)
	{
		if (canAccept(entity, EntityCategory.CELESTIO_MOBS) && eType.equals(ElementType.MORTIO)) return true;  // Darkness overcomes Light
		if (canAccept(entity, EntityCategory.CELESTIO_MOBS) && eType.equals(ElementType.COSMO)) return true;   // Reality can distort order
		if (canAccept(entity, EntityCategory.CELESTIO_MOBS) && eType.equals(ElementType.HERESIO)) return true; // Knowledge shapes Order
		if (canAccept(entity, EntityCategory.CELESTIO_MOBS) && eType.equals(ElementType.SLASH)) return true;
		
		if (canAccept(entity, EntityCategory.MORTIO_MOBS) && eType.equals(ElementType.CELESTIO)) return true;  // Light overcomes Darkness
		if (canAccept(entity, EntityCategory.MORTIO_MOBS) && eType.equals(ElementType.COSMO)) return true;     // Reality can distort desire
		if (canAccept(entity, EntityCategory.MORTIO_MOBS) && eType.equals(ElementType.HERESIO)) return true;   // Knowledge trumps Dispair
		if (canAccept(entity, EntityCategory.MORTIO_MOBS) && eType.equals(ElementType.PIERCE)) return true;
		
		if (canAccept(entity, EntityCategory.INFERNO_MOBS) && eType.equals(ElementType.GLACIO)) return true;   // Water quenches Fire
		if (canAccept(entity, EntityCategory.INFERNO_MOBS) && eType.equals(ElementType.GEO)) return true;      // Sand can put out a Fire
		
		if (canAccept(entity, EntityCategory.GLACIO_MOBS) && eType.equals(ElementType.INFERNO)) return true;   // Fire melts Ice
		if (canAccept(entity, EntityCategory.GLACIO_MOBS) && eType.equals(ElementType.AERO)) return true;      // Strong winds can displace Water
		if (canAccept(entity, EntityCategory.GLACIO_MOBS) && eType.equals(ElementType.BLUNT)) return true;     // A Hammar smashes Ice
		
		if (canAccept(entity, EntityCategory.GEO_MOBS) && eType.equals(ElementType.AERO)) return true;         // String winds cause Erosion
		if (canAccept(entity, EntityCategory.GEO_MOBS) && eType.equals(ElementType.INFERNO)) return true;      // High Temperature can cause Geological changes (Metamorphism)
		if (canAccept(entity, EntityCategory.GEO_MOBS) && eType.equals(ElementType.CORROSIVE)) return true;
		
		if (canAccept(entity, EntityCategory.AERO_MOBS) && eType.equals(ElementType.GEO)) return true;         // Sand is an Insulator of Electricity
		if (canAccept(entity, EntityCategory.AERO_MOBS) && eType.equals(ElementType.GLACIO)) return true;      // Low Temperatures can cause stifled Airflow
		if (canAccept(entity, EntityCategory.AERO_MOBS) && eType.equals(ElementType.COMBUST)) return true;
		
		if (canAccept(entity, EntityCategory.COSMO_MOBS) && eType.equals(ElementType.HERESIO)) return true;    // Knowledge distorts Reality
		if (canAccept(entity, EntityCategory.COSMO_MOBS) && eType.equals(ElementType.ARCANO)) return true;
		
		if (canAccept(entity, EntityCategory.HERESIO_MOBS) && eType.equals(ElementType.COSMO)) return true;    // Reality invents Knowledge
		if (canAccept(entity, EntityCategory.HERESIO_MOBS) && eType.equals(ElementType.ARCANO)) return true;
		
		return false;
	}
	
	public static boolean parseUniversalResistance(Entity entity, ElementType eType)
	{
		if (canAccept(entity, EntityCategory.CELESTIO_MOBS) && eType.equals(ElementType.INFERNO)) return true; // You cannot Burn Light
		if (canAccept(entity, EntityCategory.CELESTIO_MOBS) && eType.equals(ElementType.SLASH)) return true;
		
		if (canAccept(entity, EntityCategory.MORTIO_MOBS) && eType.equals(ElementType.INFERNO)) return true;   // You cannot Burn Darkness
		if (canAccept(entity, EntityCategory.MORTIO_MOBS) && eType.equals(ElementType.PIERCE)) return true;
		
		if (canAccept(entity, EntityCategory.INFERNO_MOBS) && eType.equals(ElementType.AERO)) return true; 	   // Oxygen fans the flames of Fire
		if (canAccept(entity, EntityCategory.INFERNO_MOBS) && eType.equals(ElementType.PUNCTURE)) return true;
		if (canAccept(entity, EntityCategory.INFERNO_MOBS) && eType.equals(ElementType.PIERCE)) return true;
		
		if (canAccept(entity, EntityCategory.GLACIO_MOBS) && eType.equals(ElementType.GEO)) return true;       // A Rock can't hurt Water, it simply sinks, or floats
		if (canAccept(entity, EntityCategory.GEO_MOBS) && eType.equals(ElementType.CELESTIO)) return true;     // Light doesn't (usually) pass through Solid Matter
		if (canAccept(entity, EntityCategory.GEO_MOBS) && eType.equals(ElementType.BLUNT)) return true;
		
		if (canAccept(entity, EntityCategory.AERO_MOBS) && eType.equals(ElementType.MORTIO)) return true;      // Wind cannot be held by Despair
		if (canAccept(entity, EntityCategory.AERO_MOBS) && eType.equals(ElementType.PIERCE)) return true;
		if (canAccept(entity, EntityCategory.AERO_MOBS) && eType.equals(ElementType.PUNCTURE)) return true;
		
		return false;
	}
	
	public static boolean parseUniversalImmunity(Entity entity, ElementType eType)
	{
		if (canAccept(entity, EntityCategory.CELESTIO_MOBS) && eType.equals(ElementType.CELESTIO)) return true; 
		if (canAccept(entity, EntityCategory.MORTIO_MOBS) && eType.equals(ElementType.MORTIO)) return true;   
		
		if (canAccept(entity, EntityCategory.INFERNO_MOBS) && eType.equals(ElementType.INFERNO)) return true; 	   
		if (canAccept(entity, EntityCategory.INFERNO_MOBS) && eType.equals(ElementType.COMBUST)) return true; 	   
		if (canAccept(entity, EntityCategory.INFERNO_MOBS) && eType.equals(ElementType.BLAST)) return true; 	   
		
		if (canAccept(entity, EntityCategory.GLACIO_MOBS) && eType.equals(ElementType.GLACIO)) return true;       
		
		if (canAccept(entity, EntityCategory.AERO_MOBS) && eType.equals(ElementType.AERO)) return true;     
		
		if (canAccept(entity, EntityCategory.COSMO_MOBS) && eType.equals(ElementType.INFERNO)) return true;    // Reality is immune to the Elements
		if (canAccept(entity, EntityCategory.COSMO_MOBS) && eType.equals(ElementType.GLACIO)) return true;     // Reality is immune to the Elements    
		if (canAccept(entity, EntityCategory.COSMO_MOBS) && eType.equals(ElementType.AERO)) return true;       // Reality is immune to the Elements   
		if (canAccept(entity, EntityCategory.COSMO_MOBS) && eType.equals(ElementType.GEO)) return true;        // Reality is immune to the Elements  
		
		if (canAccept(entity, EntityCategory.HERESIO_MOBS) && eType.equals(ElementType.CELESTIO)) return true; // Higher Knowledge is keen to Religious Obligation
		if (canAccept(entity, EntityCategory.HERESIO_MOBS) && eType.equals(ElementType.MORTIO)) return true;   // Higher Knowledge overcomes Mental Struggles and aware of Life Impermanence
		
		return false;
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
