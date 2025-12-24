package com.lol.enums;

public enum SpellType 
{
	OFFENSIVE("&r&c&lOffensive&r&f"),
	DEFENSIVE("&r&6&lDefensive&r&f"),
	SUPPORT("&r&a&oSupport&r&f"),
	UTILITY("&r&b&oUtility&r&f"),
	ULTIMATE("&r&e&lUltimate&r&f");

	private final String abilityType; 
	
	SpellType(String string) 
	{
		this.abilityType = string;
	}

	public String getSpellType() 
	{
		return this.abilityType;
	}
}
