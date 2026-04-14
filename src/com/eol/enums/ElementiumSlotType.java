package com.eol.enums;

public enum ElementiumSlotType
{
	NO_SLOT("No Slot"),
	MODULO("&lModulo&r&f"),
	GLACIO("&lGlacio&r&f"),
	INFERNO("&lInferno&r&f"),
	GEO("&lGeo&r&f"),
	AERO("&lAero&r&f"),
	CELESTIO("&lCelestio&r&f"),
	MORTIO("&lMortio&r&f"),
	COSMO("&lCosmo&r&f"),
	HERESIO("&lHeresio&r&f");
	
	private final String element;	
	
	ElementiumSlotType(String element) 
	{
		this.element = element;
	}

	public String getElement() 
	{
		return element;
	}
}
