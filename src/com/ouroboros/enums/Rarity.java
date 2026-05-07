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
		if (mobLevel >= 100)     return 7; 
		else if (mobLevel >= 80) return 6; 
	    else if (mobLevel >= 50) return 5; 
	    else if (mobLevel >= 40) return 4; 
	    else if (mobLevel >= 30) return 3; 
	    else if (mobLevel >= 20) return 2; 
	    else                     return 1;
	}
}
