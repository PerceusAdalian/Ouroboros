package com.eol.enums;

public enum MateriaState 
{
	NORMAL("&bRefined&r&f"),
	UNREFINED("&fUnrefined&r&f"),
	ELEMENT_CORE("&d&oElement Core&r&f"),
	CATALYST("&e&lCatalyst&r&f");
	
	private final String materiaType;
	
	MateriaState(String materiaType)
	{
		this.materiaType = materiaType;
	}
	
	public String getState()
	{
		return materiaType;
	}
	
	public String getKey()
	{
		return this.name();
	}
	
	public static MateriaState fromString(String name)
	{
		try
		{
			return MateriaState.valueOf(name.toUpperCase());
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}
}
