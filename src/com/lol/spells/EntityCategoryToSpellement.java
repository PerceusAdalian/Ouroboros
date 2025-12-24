package com.lol.spells;

import org.bukkit.entity.EntityType;

import com.lol.enums.SpellementType;
import com.ouroboros.enums.EntityCategory;
import com.ouroboros.utils.EntityCategories;

public class EntityCategoryToSpellement 
{
	/**
	 * Determines which EntityCategory a mob belongs to based on element types
	 * Returns the most specific elemental category, or null if only in generic categories
	 */
	public static EntityCategory getMobCategory(EntityType type)
	{
	    // Check elemental categories first (most specific)
	    if (EntityCategories.celestio_mobs.contains(type)) return EntityCategory.CELESTIO_MOBS;
	    if (EntityCategories.mortio_mobs.contains(type)) return EntityCategory.MORTIO_MOBS;
	    if (EntityCategories.inferno_mobs.contains(type)) return EntityCategory.INFERNO_MOBS;
	    if (EntityCategories.glacio_mobs.contains(type)) return EntityCategory.GLACIO_MOBS;
	    if (EntityCategories.aero_mobs.contains(type)) return EntityCategory.AERO_MOBS;
	    if (EntityCategories.geo_mobs.contains(type)) return EntityCategory.GEO_MOBS;
	    if (EntityCategories.cosmo_mobs.contains(type)) return EntityCategory.COSMO_MOBS;
	    if (EntityCategories.heresio_mobs.contains(type)) return EntityCategory.HERESIO_MOBS;
	    
	    // If not in any elemental category, return null (won't drop elemental spells)
	    return null;
	}

	// Checks if a SpellementType matches an EntityCategory
	public static boolean isElementMatch(SpellementType spellType, EntityCategory mobCategory)
	{
	    return switch (spellType)
	    {
	        case CELESTIO -> mobCategory == EntityCategory.CELESTIO_MOBS;
	        case MORTIO -> mobCategory == EntityCategory.MORTIO_MOBS;
	        case INFERNO -> mobCategory == EntityCategory.INFERNO_MOBS;
	        case GLACIO -> mobCategory == EntityCategory.GLACIO_MOBS;
	        case AERO -> mobCategory == EntityCategory.AERO_MOBS;
	        case GEO -> mobCategory == EntityCategory.GEO_MOBS;
	        case COSMO -> mobCategory == EntityCategory.COSMO_MOBS;
	        case HERESIO -> mobCategory == EntityCategory.HERESIO_MOBS;
	    };
	}
}
