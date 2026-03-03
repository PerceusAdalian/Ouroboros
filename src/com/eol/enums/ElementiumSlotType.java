package com.eol.enums;

public enum ElementiumSlotType
{
	NO_SLOT("No Slot"),
	MODULO("Modulo"),
	GLACIO("Glacio"),
	INFERNO("Inferno"),
	GEO("Geo"),
	AERO("Aero"),
	CELESTIO("Celestio"),
	MORTIO("Mortio"),
	COSMO("Cosmo");

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
