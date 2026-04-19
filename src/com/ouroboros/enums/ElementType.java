package com.ouroboros.enums;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.lol.enums.SpellementType;

public enum ElementType
{
	NONE(null),
	//Neutral Damage Type
	PURE("&r&f&lPure&r&f"),
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
	ARCANO("Arcano&r&f"),
	//Almighty/Pure Damage Types
	CRUSH("&r&f&o&lCrush&r&f"),
	SEVER("&r&f&o&lSever&r&f"),
	IMPALE("&r&f&o&lImpale&r&f"),
	//Almighty Elemental Damage Type
	//Special Almighty Explosive Damage Type
	BLAST("&r&e&o&lBlast&r&f"),
	//Elemental Damage Types
	CELESTIO("&lCelestio&r&f"),
	MORTIO("&lMortio&r&f"),
	INFERNO("&lInferno&r&f"),
	GLACIO("&lGlacio&r&f"),
	AERO("&lAero&r&f"),
	GEO("&lGeo&r&f"),
	COSMO("&lCosmo&r&f"),
	HERESIO("&lHeresio&r&f");
	
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
	
	public static ElementType fromString(String name)
	{
		try
		{
			return ElementType.valueOf(name.toUpperCase());
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
	}
	
	public static ElementType getFromSpellement(SpellementType sType)
	{
		ElementType eType = switch(sType)
		{
			case AERO 		-> ElementType.AERO;
			case ARCANO 	-> ElementType.ARCANO;
			case CELESTIO 	-> ElementType.CELESTIO;
			case COSMO 		-> ElementType.COSMO;
			case GEO 		-> ElementType.GEO;
			case GLACIO 	-> ElementType.GLACIO;
			case HERESIO 	-> ElementType.HERESIO;
			case INFERNO 	-> ElementType.INFERNO;
			case MORTIO 	-> ElementType.MORTIO;
			case NULL 		-> ElementType.NONE;
			default 		-> ElementType.NONE;	
			
		};
		
		return eType;
	}
	
	public static final EnumSet<ElementType> mobElements = EnumSet.of(
		    CELESTIO, MORTIO, INFERNO, GLACIO, AERO, GEO, COSMO, HERESIO, ARCANO);
	
	public static ElementType random()
	{
	    ElementType[] types = ElementType.mobElements.toArray(new ElementType[0]);
	    return types[ThreadLocalRandom.current().nextInt(types.length)];
	}
	
	public static Set<ElementType> elemental = Set.of(CELESTIO, MORTIO, INFERNO, GLACIO, GEO, AERO, COSMO, HERESIO, ARCANO);
	public static Set<ElementType> almighty = Set.of(CRUSH, SEVER, IMPALE, BLAST);
	public static Set<ElementType> natural = Set.of(BLUNT, PIERCE, SLASH, COMBUST, PUNCTURE, CORROSIVE);
}
