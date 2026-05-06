package com.eol.enums;

public enum EchoForm 
{
	SWORD("&r&cSwords&f"),
	HATCHET("&r&cHatchets&f"),
	POLEARM("&r&cPolearms&f"),
	PICKAXE("&r&6Pickaxes&f"),
	SPADE("&r&6Spades&f"),
	SCYTHE("&r&6Scythes&f"),
	
	HAMMER("&r&bHammer&f"),
	ARMAMENT("&r&bArmament&f"),
	BOW("&r&dBow&f"),
	CROSSBOW("&r&dCrossbow&f")
	;
	
	private final String symbol;
	EchoForm(String string)
	{
		this.symbol = string;
	}
	
	public String getLabel()
	{
		return symbol;
	}
	
}
