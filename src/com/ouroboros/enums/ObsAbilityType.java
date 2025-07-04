package com.ouroboros.enums;

public enum ObsAbilityType 
{
	UTILITY("&r&b&oUtility"),
	OFFENSIVE("&r&c&lOffensive"),
	DEFENSIVE("&r&6&lDefensive"),
	SUPPORT("&r&e&oSupport");

	private final String abilityType; 
	
	ObsAbilityType(String string) 
	{
		this.abilityType = string;
	}

	public String getAbilityType() 
	{
		return this.abilityType;
	}
	
	public String getKey() 
	{
		return this.name().toLowerCase();
	}
}
