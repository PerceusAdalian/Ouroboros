package com.ouroboros.enums;

import java.util.Optional;

public enum StatType 
{
	//Stat types
	CRAFTING,
	ALCHEMY,
	ENCHANTING,
	TRAVEL,
	WOODCUTTING,
	MINING,
	FISHING,
	FARMING,
	MELEE,
	RANGED,
	MAGIC,
	
	//Stat points
	PRESTIGUEPOINTS,
	ABILITYPOINTS;
	
	public String getKey()
	{
		return this.name().toLowerCase();
	}
	
	public static Optional<StatType> fromString(String input) 
	{
        try 
        {
            return Optional.of(StatType.valueOf(input.toUpperCase()));
        } 
        catch (IllegalArgumentException e) 
        {
            return Optional.empty();
        }
    }
}
