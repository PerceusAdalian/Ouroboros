package com.ouroboros.enums;

import java.util.Set;

import org.bukkit.Material;

import com.ouroboros.utils.AbilityObjectCategory;
import com.ouroboros.utils.PrintUtils;

public enum AbilityCategory 
{
	SWORDS,
	AXES,
	PICKAXES,
	SHOVELS,
	SCYTHES,
	BOWS,
	FISHING,
	SHEARS,
	TRIDENT,
	MACE,
	BRUSH,
	ANY
	;
	
	public String getKey()
	{
		String key = "";
		String str = this.name().toLowerCase();
		char[] chars = str.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		key += new String(chars);
		key = key.substring(0,key.length()-1);
		return PrintUtils.ColorParser("&r&b&o"+key+"&r&f");
	}
	
	public static Set<Material> get(AbilityCategory ac) 
	{
		return switch(ac)
		{
			case SWORDS -> AbilityObjectCategory.swords;
			case AXES -> AbilityObjectCategory.axes;
			case PICKAXES -> AbilityObjectCategory.pickaxes;
			case SHOVELS -> AbilityObjectCategory.shovels;
			case SCYTHES -> AbilityObjectCategory.scythes;
			case BOWS -> AbilityObjectCategory.bows;
			case FISHING -> Set.of(AbilityObjectCategory.fishing);
			case SHEARS -> Set.of(AbilityObjectCategory.shears);
			case TRIDENT -> Set.of(AbilityObjectCategory.trident);
			case MACE -> Set.of(AbilityObjectCategory.mace);
			case BRUSH -> Set.of(AbilityObjectCategory.brush);
			case ANY -> Set.of(Material.values());
		};
	}
	
}
