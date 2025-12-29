package com.ouroboros.enums;

public enum ElementType
{
	NONE(null),
	//Neutral Damage Type
	NEUTRAL("&r&fNeutral&r&f"),
	//Normal Melee Damage Types
	BLUNT("&r&fBlunt&r&f"), 
	PIERCE("&r&fPierce&r&f"),
	SLASH("&r&fSlash&r&f"),
	//Special Explosive Damage Type
	COMBUST("&r&cCombust&r&f"),
	//Ranged Damage Types
	PUNCTURE("&r&fPuncture&r&f"),
	CORROSIVE("&r&aCorrosive&r&f"),
	//Magic Damage Type
	ARCANO("&r&b&oArcano&r&f"),
	//Almighty/Pure Damage Types
	CRUSH("&r&f&o&lCrush&r&f"),
	SEVER("&r&f&o&lSever&r&f"),
	IMPALE("&r&f&o&lImpale&r&f"),
	//Almighty/Pure Elemental Damage Type
	HERESIO("&r&2&lHeresio&r&f"),
	//Special Almighty Explosive Damage Type
	BLAST("&r&c&o&lBlast&r&f"),
	//Elemental Damage Types
	GLACIO("&r&b&lGlacio&r&f"),
	INFERNO("&r&c&lInferno&r&f"),
	GEO("&r&6&lGeo&r&f"),
	AERO("&r&d&lAero&r&f"),
	CELESTIO("&r&e&lCelestio&r&f"),
	MORTIO("&r&4&lMortio&r&f"),
	COSMO("&r&3&lCosmo&r&f");
	
	private final String element;	
	
	ElementType(String element) 
	{
		this.element = element;
	}
	
	public String getKey()
	{
		return this.name();
	}

	public String getType() 
	{
		return element;
	}
}
