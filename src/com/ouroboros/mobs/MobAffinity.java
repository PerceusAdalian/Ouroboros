package com.ouroboros.mobs;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Entity;

import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.EntityCategory;

public class MobAffinity
{
	private static final Map<EntityCategory, Set<ElementType>> WEAKNESSES = Map.of(
		    EntityCategory.CELESTIO_MOBS,  EnumSet.of(ElementType.MORTIO, ElementType.COSMO, ElementType.HERESIO),
		    EntityCategory.MORTIO_MOBS,    EnumSet.of(ElementType.CELESTIO, ElementType.COSMO, ElementType.HERESIO),
		    
		    EntityCategory.INFERNO_MOBS,   EnumSet.of(ElementType.GLACIO, ElementType.GEO),
		    EntityCategory.GLACIO_MOBS,    EnumSet.of(ElementType.INFERNO, ElementType.BLUNT, ElementType.CORROSIVE, ElementType.COMBUST),
		    EntityCategory.AERO_MOBS,      EnumSet.of(ElementType.GEO, ElementType.GLACIO, ElementType.COMBUST),
		    EntityCategory.GEO_MOBS,       EnumSet.of(ElementType.AERO, ElementType.BLAST),
		    
		    EntityCategory.COSMO_MOBS,     EnumSet.of(ElementType.COSMO, ElementType.HERESIO, ElementType.ARCANO),
		    EntityCategory.HERESIO_MOBS,   EnumSet.of(ElementType.COSMO, ElementType.ARCANO));

		private static final Map<EntityCategory, Set<ElementType>> RESISTANCES = Map.of(
		    EntityCategory.CELESTIO_MOBS,  EnumSet.of(ElementType.INFERNO),
		    EntityCategory.MORTIO_MOBS,    EnumSet.of(ElementType.INFERNO, ElementType.CORROSIVE),
		    
		    EntityCategory.INFERNO_MOBS,   EnumSet.of(ElementType.AERO, ElementType.PIERCE, ElementType.PUNCTURE),
		    EntityCategory.GLACIO_MOBS,    EnumSet.of(ElementType.GEO, ElementType.SLASH),
		    EntityCategory.AERO_MOBS,      EnumSet.of(ElementType.MORTIO, ElementType.PIERCE, ElementType.PUNCTURE, ElementType.SEVER, ElementType.IMPALE, ElementType.CRUSH),
		    EntityCategory.GEO_MOBS,       EnumSet.of(ElementType.SLASH, ElementType.BLUNT, ElementType.PUNCTURE, ElementType.PIERCE, ElementType.SEVER, ElementType.IMPALE),
		    
		    EntityCategory.COSMO_MOBS,     EnumSet.of(ElementType.BLUNT, ElementType.SLASH, ElementType.PUNCTURE, ElementType.PIERCE, ElementType.CORROSIVE, ElementType.COMBUST),
		    EntityCategory.HERESIO_MOBS,   EnumSet.of(ElementType.BLUNT, ElementType.SLASH, ElementType.PUNCTURE, ElementType.PIERCE, ElementType.CORROSIVE, ElementType.COMBUST));

		private static final Map<EntityCategory, Set<ElementType>> IMMUNITIES = Map.of(
		    EntityCategory.CELESTIO_MOBS,  EnumSet.of(ElementType.CELESTIO),
		    EntityCategory.MORTIO_MOBS,    EnumSet.of(ElementType.MORTIO),
		    
		    EntityCategory.INFERNO_MOBS,   EnumSet.of(ElementType.INFERNO, ElementType.COMBUST, ElementType.BLAST),
		    EntityCategory.GLACIO_MOBS,    EnumSet.of(ElementType.GLACIO),
		    EntityCategory.AERO_MOBS,      EnumSet.of(ElementType.AERO),
		    EntityCategory.GEO_MOBS,       EnumSet.of(ElementType.CORROSIVE, ElementType.COMBUST, ElementType.CRUSH),
		    
		    EntityCategory.COSMO_MOBS,     EnumSet.of(ElementType.INFERNO, ElementType.GLACIO, ElementType.AERO, ElementType.GEO),
		    EntityCategory.HERESIO_MOBS,   EnumSet.of(ElementType.CELESTIO, ElementType.MORTIO, ElementType.HERESIO));

		private static boolean matchesCategory(Entity entity, Map<EntityCategory, Set<ElementType>> table, ElementType eType) 
		{
		    return table.entrySet().stream().anyMatch(e -> EntityCategories.canAccept(entity, e.getKey()) && e.getValue().contains(eType));
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

}
