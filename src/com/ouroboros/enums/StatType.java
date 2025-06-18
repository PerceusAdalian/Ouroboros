package com.ouroboros.enums;

public enum StatType 
{
	GENERIC,
	CRAFTING,
	ALCHEMY,
	TRAVEL,
	WOODCUTTING,
	MINING,
	FISHING,
	MELEE,
	RANGED,
	MAGIC;
	
	public String getKey()
	{
		return this.name().toLowerCase();
	}
}
