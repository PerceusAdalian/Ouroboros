package com.lol.enums;

public enum SpellementType 
{
	CELESTIO("&r&f&lCelestio&r&f"),
	MORTIO("&r&4&lMortio&r&f"),
	INFERNO("&r&c&lInferno&r&f"),
	GLACIO("&r&b&lGlacio&r&f"),
	AERO("&r&d&lAero&r&f"),
	GEO("&r&6&lGeo&r&f"),
	COSMO("&r&3&lCosmo&r&f"),
	HERESIO("&r&2&lHeresio&r&f");
	
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
}
