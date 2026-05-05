package com.eol.enums;

public enum EchoForm 
{
	SWORD("&r&c⚔︎&f"),
	HATCHET("&r&c🪓&f"),
	POLEARM("&r&c𐃆&f"),
	PICKAXE("&r&6⛏&f"),
	SPADE("&r&6♠&f"),
	SCYTHE("&r&6☭&f"),
	
	HAMMER("&r&b🔨&f"),
	ARMAMENT("&r&b🔱&f"),
	BOW("&r&d🏹&f"),
	CROSSBOW("&r&d🏹&f")
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
