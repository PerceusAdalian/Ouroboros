package com.ouroboros.enums;

import java.util.Set;

import org.bukkit.entity.EntityType;

import com.ouroboros.utils.EntityCategories;

public enum EntityCategory 
{
	NONE,
	ANY,
	CALAMITY,
	ARCANO_MOBS,
	CELESTIO_MOBS,
	MORTIO_MOBS,
	INFERNO_MOBS,
	GLACIO_MOBS,
	AERO_MOBS,
	GEO_MOBS,
	HERESIO_MOBS,
	COSMO_MOBS,
	BUGS,
	ELEMENTAL;
	
	public String getKey()
	{
		String key = "";
		String str = this.name().toLowerCase();
		if (str.contains("_")) 
		{
			String[] splitName = str.split("_");
			for (String s : splitName) 
			{
				char[] chars = s.toCharArray();
				chars[0] = Character.toUpperCase(chars[0]);
				str += new String(chars);
				str += " ";
			}

			key = key.substring(0,key.length()-1);
		}
		else
		{
			char[] chars = str.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			key += new String(chars);
			
			key = key.substring(0,key.length());
		}
		return key;
	}
	
	public static Set<EntityType> get(EntityCategory category)
	{
		return switch (category)
		{
			case NONE -> EntityCategories.none;
			case ANY -> EntityCategories.any;
			case CALAMITY -> EntityCategories.calamity;
			case CELESTIO_MOBS -> EntityCategories.celestio_mobs;
			case MORTIO_MOBS -> EntityCategories.mortio_mobs;
			case INFERNO_MOBS -> EntityCategories.inferno_mobs;
			case GLACIO_MOBS -> EntityCategories.glacio_mobs;
			case AERO_MOBS -> EntityCategories.aero_mobs;
			case GEO_MOBS -> EntityCategories.geo_mobs;
			case COSMO_MOBS -> EntityCategories.cosmo_mobs;
			case HERESIO_MOBS -> EntityCategories.heresio_mobs;
			case ARCANO_MOBS -> EntityCategories.arcano_mobs;
			case BUGS -> EntityCategories.bugs;
			case ELEMENTAL -> EntityCategories.elemental;
		};
	}
	
	public static Set<EntityType> parseByElementType(ElementType type)
	{
		return switch (type)
		{
			case ARCANO-> EntityCategories.arcano_mobs;
			case CELESTIO -> EntityCategories.celestio_mobs;
			case MORTIO -> EntityCategories.mortio_mobs;
			case INFERNO -> EntityCategories.inferno_mobs;
			case GLACIO -> EntityCategories.glacio_mobs;
			case AERO -> EntityCategories.aero_mobs;
			case GEO -> EntityCategories.geo_mobs;
			case COSMO -> EntityCategories.cosmo_mobs;
			case HERESIO -> EntityCategories.heresio_mobs;
			default -> EntityCategories.any;	
		};
	}
}
