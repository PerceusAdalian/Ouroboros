package com.eol.materia.components;

import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaType;
import com.eol.materia.Materia;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.PrintUtils;

public class ElementCores 
{
	public static void load()
	{
		Materia.register(new Materia(PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f Core",
				"celestio_core",
				MateriaType.CELESTIO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia(PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f Core",
				"mortio_core",
				MateriaType.MORTIO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia(PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f Core",
				"inferno_core",
				MateriaType.INFERNO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia(PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f Core",
				"glacio_core",
				MateriaType.GLACIO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia(PrintUtils.color(ObsColors.AERO)+"&lAero&r&f Core",
				"aero_core",
				MateriaType.AERO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia(PrintUtils.color(ObsColors.GEO)+"&lGeo&r&f Core",
				"geo_core",
				MateriaType.GEO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized "+PrintUtils.color(ObsColors.GEO)+"&lGeo&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia(PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f Core",
				"cosmo_core",
				MateriaType.COSMO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia(PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f Core",
				"heresio_core",
				MateriaType.HERESIO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
	}
}
