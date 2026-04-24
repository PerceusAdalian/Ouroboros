package com.lol.wand;

import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.PrintUtils;

public class WandRegistry 
{
	public static void load()
	{
		// Main Progression
		Wand.register(new Wand("&r&fMagic Wand", "wand_1", Rarity.ONE, null, 1, 250));
		Wand.register(new Wand("&r&fMagic Wand", "wand_2", Rarity.TWO, null, 2, 500));
		Wand.register(new Wand("&r&fMagic Wand", "wand_3", Rarity.THREE, null, 4, 750));
		Wand.register(new Wand("&r&fMagic Wand", "wand_4", Rarity.FOUR, null, 5, 1000));
		Wand.register(new Wand("&bArchmage's Scepter&r&f", "wand_5", Rarity.FIVE, null, 5, 2000));
		Wand.register(new Wand("&eRoyal Scepter&r&f", "wand_6", Rarity.SIX, null, 5, 3500));
		Wand.register(new Wand("&dForgotten King's Stave&r&f", "wand_7", Rarity.SEVEN, ElementType.ARCANO, 5, 5000));
		
		// Elemental
		
		// Five
		Wand.register(new Wand("&r&fWand of the "+PrintUtils.color(ObsColors.CELESTIO)+"&lHeavens&r&f", "celestio_wand", Rarity.FIVE, ElementType.CELESTIO, 5, 1500));
		Wand.register(new Wand("&r&fWand of the "+PrintUtils.color(ObsColors.MORTIO)+"&lDead&r&f", "mortio_wand", Rarity.FIVE, ElementType.MORTIO, 5, 1500));
		Wand.register(new Wand("&r&fWand of "+PrintUtils.color(ObsColors.INFERNO)+"&lFire&r&f", "inferno_wand", Rarity.FIVE, ElementType.INFERNO, 5, 1500));
		Wand.register(new Wand("&r&fWand of "+PrintUtils.color(ObsColors.GLACIO)+"&lIce&r&f", "glacio_wand", Rarity.FIVE, ElementType.GLACIO, 5, 1500));
		Wand.register(new Wand("&r&fWand of the "+PrintUtils.color(ObsColors.AERO)+"&lSky&r&f", "aero_wand", Rarity.FIVE, ElementType.AERO, 5, 1500));
		Wand.register(new Wand("&r&fWand of the "+PrintUtils.color(ObsColors.GEO)+"&lEarth&r&f", "geo_wand", Rarity.FIVE, ElementType.GEO, 5, 1500));
		Wand.register(new Wand("&r&fWand of the "+PrintUtils.color(ObsColors.COSMO)+"&lCosmos&r&f", "cosmo_wand", Rarity.FIVE, ElementType.COSMO, 5, 1500));
		Wand.register(new Wand("&r&fWand of the "+PrintUtils.color(ObsColors.HERESIO)+"&lHeretics&r&f", "heresio_wand", Rarity.FIVE, ElementType.HERESIO, 5, 1500));
		
		// Six
		Wand.register(new Wand("&eΣOL&f: Baton of the "+PrintUtils.color(ObsColors.CELESTIO)+"&lHoly Magistrate&r&f", "baton_of_the_holy_magistrate", Rarity.SIX, ElementType.CELESTIO, 5, 3000));
		Wand.register(new Wand("&eΣOL&f: Armament of "+PrintUtils.color(ObsColors.MORTIO)+"&lSithis&r&f", "sithis_armament", Rarity.SIX, ElementType.MORTIO, 5, 3000));
		Wand.register(new Wand("&eΣOL&f: "+PrintUtils.color(ObsColors.INFERNO)+"&lAighil's Talon&r&f", "aighils_talon", Rarity.SIX, ElementType.INFERNO, 5, 3000));
		Wand.register(new Wand("&eΣOL&f: Artifact of "+PrintUtils.color(ObsColors.GLACIO)+"&lBjorn&r&f", "bjorn_artifact", Rarity.SIX, ElementType.GLACIO, 5, 3000));
		Wand.register(new Wand("&eΣOL&f: Cane of "+PrintUtils.color(ObsColors.GEO)+"&lNidus&r&f", "nidus_cane", Rarity.SIX, ElementType.GEO, 5, 3000));
		Wand.register(new Wand("&eΣOL&f: "+PrintUtils.color(ObsColors.AERO)+"&lSeth&r&f's Caduceus&r&f", "seth_caduceus", Rarity.SIX, ElementType.AERO, 5, 3000));
		Wand.register(new Wand("&eΣOL&f: &r&fAntenna of "+PrintUtils.color(ObsColors.COSMO)+"&lEnd&r&f", "antenna_of_end", Rarity.SIX, ElementType.COSMO, 5, 3000));
		Wand.register(new Wand("&eΣOL&f: Stave of "+PrintUtils.color(ObsColors.HERESIO)+"&lGeneral Falric&r&f", "stave_of_general_falric", Rarity.SIX, ElementType.HERESIO, 5, 3000));
		
		// Seven
		Wand.register(new Wand("&eΣOL&f: "+PrintUtils.color(ObsColors.CELESTIO)+"&lLumina's Scepter&r&f", "luminas_wand", Rarity.SEVEN, ElementType.CELESTIO, 5, 5000));
		Wand.register(new Wand("&eΣOL&f: &2&lTwilight Catalyst&r&f", "twilight_catalyst", Rarity.SEVEN, ElementType.HERESIO, 5, 5000));
	}
}
