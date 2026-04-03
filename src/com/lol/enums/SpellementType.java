package com.lol.enums;

import java.util.Set;

public enum SpellementType 
{
	CELESTIO("&lCelestio&r&f"),
	MORTIO("&lMortio&r&f"),
	INFERNO("&lInferno&r&f"),
	GLACIO("&lGlacio&r&f"),
	AERO("&lAero&r&f"),
	GEO("&lGeo&r&f"),
	COSMO("&lCosmo&r&f"),
	HERESIO("&lHeresio&r&f"),
	ARCANO("&lArcano&r&f"),
	ASTRAL("&lAstral&r&f"),
	
	//Administrator SpellementType
	NULL("&f&lNone&r&f");
	
	private String name;
	
	SpellementType(String name)
	{
		this.name = name;
	}
	
	public String getKey()
	{
		return this.name();
	}
	
	public String getType()
	{
		return name;
	}
	
	public static SpellementType fromString(String name)
	{
		try
		{
			return SpellementType.valueOf(name.toUpperCase());
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}
	
	public static final Set<SpellementType> elemental = Set.of(CELESTIO, MORTIO, INFERNO, GLACIO, AERO, GEO, COSMO, HERESIO);
}
