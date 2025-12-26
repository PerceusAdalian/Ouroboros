package com.lol.enums;

public enum SpellType 
{
	OFFENSIVE("&r&c&lOffensive&r&f"),
	DEFENSIVE("&r&6&lDefensive&r&f"),
	SUPPORT("&r&a&oSupport&r&f"),
	UTILITY("&r&b&oUtility&r&f"),
	ULTIMATE("&r&e&lUltimate&r&f");

	private final String spellType; 
	
	SpellType(String string) 
	{
		this.spellType = string;
	}

	public String getSpellType() 
	{
		return spellType;
	}
	
	public static SpellType fromString(String name)
	{
		try
		{
			return SpellType.valueOf(name.toUpperCase());
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}
}
