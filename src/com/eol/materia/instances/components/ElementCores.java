package com.eol.materia.instances.components;

import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaType;
import com.eol.materia.instances.Materia;
import com.ouroboros.enums.Rarity;

public class ElementCores 
{
	public static void load()
	{
		Materia.register(new Materia("&c&lInferno&r&f Core",
				"inferno_core",
				MateriaType.INFERNO,
				MateriaComponent.ELEMENT_CORE,
				Rarity.FIVE,
				false,
				"&r&fA refined manifestation of pure, stabilized &c&lInferno&r&f ether.",
				"&r&fYou can use this to recharge special wands, or for &bProtocol&f: &eΣ&f.C.H.O."));
		
		
	}
}
