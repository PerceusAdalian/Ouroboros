package com.ouroboros.enums;

import java.util.Set;

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
	//Almighty Elemental Damage Type
	//Special Almighty Explosive Damage Type
	BLAST("&r&c&o&lBlast&r&f"),
	//Elemental Damage Types
	CELESTIO("&r&e&lCelestio&r&f"),
	MORTIO("&r&4&lMortio&r&f"),
	INFERNO("&r&c&lInferno&r&f"),
	GLACIO("&r&b&lGlacio&r&f"),
	AERO("&r&d&lAero&r&f"),
	GEO("&r&6&lGeo&r&f"),
	COSMO("&r&3&lCosmo&r&f"),
	HERESIO("&r&2&lHeresio&r&f");
	
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
	
	public static Set<ElementType> elemental = Set.of(CELESTIO, MORTIO, INFERNO, GLACIO, GEO, AERO, COSMO, HERESIO);
	public static Set<ElementType> almighty = Set.of(CRUSH, SEVER, IMPALE, BLAST);
	public static Set<ElementType> natural = Set.of(BLUNT, PIERCE, SLASH, COMBUST, PUNCTURE, CORROSIVE);
}
