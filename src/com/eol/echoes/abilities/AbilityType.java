package com.eol.echoes.abilities;

public enum AbilityType 
{
	OFFENSIVE("&r&c&lOffensive&r&f"),
	DEFENSIVE("&r&6&lDefensive&r&f"),
	SUPPORT("&r&a&oSupport&r&f"),
	UTILITY("&r&b&oUtility&r&f"),
	CONTROL("&r&d&oControl&r&f"),
	BUFF("&r&6&oBuff&r&f"),
	DEBUFF("&r&6&oDebuff&r&f"),
	ULTIMATE("&r&e&lUltimate&r&f"),
	SIGNATURE("&r&e&oSignature&r&f");
	
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
