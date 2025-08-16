package com.ouroboros.enums;

import java.util.Set;

import org.bukkit.entity.EntityType;

import com.ouroboros.utils.EntityCategories;

public enum EntityCategory 
{
	ANY,
	LIVING,
	UNDEAD,
	AQUATIC,
	FLYING,
	INFERNAL,
	ETHEREAL,
	OCCULTIC,
	CALAMITY,
	CELESTIO_MOBS,
	MORTIO_MOBS,
	INFERNO_MOBS,
	GLACIO_MOBS,
	AERO_MOBS,
	GEO_MOBS,
	COSMO_MOBS;
	
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
			case ANY -> EntityCategories.any;
			case LIVING -> EntityCategories.living;
			case UNDEAD -> EntityCategories.undead;
			case AQUATIC -> EntityCategories.aquatic;
			case FLYING -> EntityCategories.flying;
			case INFERNAL -> EntityCategories.infernal;
			case ETHEREAL -> EntityCategories.ethereal;
			case OCCULTIC -> EntityCategories.occultic;
			case CALAMITY -> EntityCategories.calamity;
			case CELESTIO_MOBS -> EntityCategories.celestio_mobs;
			case MORTIO_MOBS -> EntityCategories.mortio_mobs;
			case INFERNO_MOBS -> EntityCategories.inferno_mobs;
			case GLACIO_MOBS -> EntityCategories.glacio_mobs;
			case AERO_MOBS -> EntityCategories.aero_mobs;
			case GEO_MOBS -> EntityCategories.geo_mobs;
			case COSMO_MOBS -> EntityCategories.cosmo_mobs;
		};
	}
}
