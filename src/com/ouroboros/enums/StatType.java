package com.ouroboros.enums;

public enum StatType 
{
	//Stat types
	CRAFTING,
	ALCHEMY,
	ENCHANTING,
	DISCOVERY,
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
	ABILITYPOINTS,
	
	;
	
	public String getKey()
	{
		return this.name().toLowerCase();
	}
	
	public String getFancyKey() 
	{
		String name = this.name().toLowerCase();
		String s = "";
		char[] chars = name.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return s += new String(chars);
	}
	
	public static StatType fromString(String input) 
	{
        try 
        {
            return StatType.valueOf(input.toUpperCase());
        } 
        catch (IllegalArgumentException e) 
        {
            return null;
        }
    }
}
