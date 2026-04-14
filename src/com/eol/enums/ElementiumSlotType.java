package com.eol.enums;

import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.PrintUtils;

public enum ElementiumSlotType
{
	NO_SLOT("No Slot"),
	MODULO("&r&f&oModulo&r&f"),
	INFERNO(PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f"),
	GLACIO(PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f"),
	GEO(PrintUtils.color(ObsColors.GEO)+"&lGeo&r&f"),
	AERO(PrintUtils.color(ObsColors.AERO)+"&lAero&r&f"),
	CELESTIO(PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f"),
	MORTIO(PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f"),
	COSMO(PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f"),
	HERESIO(PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f");
	
	private final String element;	
	
	ElementiumSlotType(String element) 
	{
		this.element = element;
	}

	public String getElement() 
	{
		return element;
	}
}
