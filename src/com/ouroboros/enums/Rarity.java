package com.ouroboros.enums;

public enum Rarity
{
	NONE(0),
	ONE(1),
	TWO(2),
	THREE(3),
	FOUR(4),
	FIVE(5),
	SIX(6),
	SEVEN(7);
	
	private final int rarity;
	
	private Rarity(int rarity) 
	{
		this.rarity = rarity;
	}
	
	public int getRarity() 
	{
		return this.rarity;
	}
	
	public static int getRarityForMobLevel(int mobLevel)
	{
		if (mobLevel >= 100)     return 7; // Calamity
	    else if (mobLevel >= 80) return 5; // Royal
	    else if (mobLevel >= 60) return 4; // Elite
	    else if (mobLevel >= 40) return 3; // Uncommon
	    else if (mobLevel >= 20) return 2; // Common
	    else                     return 1; // Fodder
	}
}
