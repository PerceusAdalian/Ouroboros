package com.eol.materia.components;

import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaType;
import com.eol.materia.Materia;
import com.ouroboros.enums.Rarity;

public class ElementCores 
{
	public static void load()
	{
		Materia.register(new Materia("&e&lCelestio&r&f Core",
				"celestio_core",
				MateriaType.CELESTIO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized &e&lCelestio&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia("&4&lMortio&r&f Core",
				"mortio_core",
				MateriaType.MORTIO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized &8&lMortio&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia("&c&lInferno&r&f Core",
				"inferno_core",
				MateriaType.INFERNO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized &c&lInferno&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia("&b&lGlacio&r&f Core",
				"glacio_core",
				MateriaType.GLACIO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized &b&lGlacio&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia("&d&lAero&r&f Core",
				"aero_core",
				MateriaType.AERO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized &d&lAero&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia("&6&lGeo&r&f Core",
				"geo_core",
				MateriaType.GEO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized &6&lGeo&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia("&3&lCosmo&r&f Core",
				"cosmo_core",
				MateriaType.COSMO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized &3&lCosmo&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
		
		Materia.register(new Materia("&2&lHeresio&r&f Core",
				"heresio_core",
				MateriaType.HERESIO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized &2&lHeresio&r&f ether.",
				"&r&fUse in &bProtocol&f: &eΣ&f.C.H.O. or otherwise as a &3&oMateria&r&f."));
	}
}
