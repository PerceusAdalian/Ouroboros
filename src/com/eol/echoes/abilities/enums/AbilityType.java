package com.eol.echoes.abilities.enums;

public enum AbilityType 
{
	COMBAT("&r&cCombat&r&f"),
	SPECIALABILITY("&r&e&lSpecial&r&f"),
	UTILITY("&r&b&oUtility&r&f");
	
	private final String abilityType; 
	
	AbilityType(String string) 
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
