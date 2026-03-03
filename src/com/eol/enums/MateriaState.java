package com.eol.enums;

public enum MateriaState 
{
	NORMAL("&b&oRefined&r&f"),
	UNREFINED("&f&oUnrefined&r&f"),
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
}
