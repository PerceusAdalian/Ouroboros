package com.ouroboros.enums;

public enum ObsAbilityType 
{
	PERK("&r&f&oPerk"),
	COMBAT("&r&cCombat"),
	SPECIALABILITY("&r&c&lSpecial"),
	UTILITY("&r&b&oUtility");
	
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
