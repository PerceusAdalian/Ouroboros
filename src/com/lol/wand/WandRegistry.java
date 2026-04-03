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
		
		Wand.register(new Wand("Magic Wand", "wand_1", Rarity.ONE, null, 1, 100));
		Wand.register(new Wand("Magic Wand", "wand_2", Rarity.TWO, null, 2, 200));
		Wand.register(new Wand("Magic Wand", "wand_3", Rarity.THREE, null, 4, 400));
		Wand.register(new Wand("Magic Wand", "wand_4", Rarity.FOUR, null, 5, 1000));
		Wand.register(new Wand("&b&oArchmage's Staff&r&f", "wand_5", Rarity.FIVE, null, 5, 1500));
		Wand.register(new Wand("&eΣOL&f: &eLumina's Scepter&r&f", "luminas_wand", Rarity.SIX, ElementType.CELESTIO, 8, 2000));
		Wand.register(new Wand("&eΣOL&f: &2&lTwilight Catalyst&r&f", "twilight_catalyst", Rarity.SEVEN, ElementType.HERESIO, 10, 3000));
	
		
		// Elemental
		Wand.register(new Wand("&r&fArmament of "+PrintUtils.color(ObsColors.MORTIO)+"&lSithis&r&f", "sithis_armament", Rarity.FIVE, ElementType.MORTIO, 5, 1500));
		Wand.register(new Wand("&r&fStaff of "+PrintUtils.color(ObsColors.INFERNO)+"&lAgni&r&f", "agni_staff", Rarity.FIVE, ElementType.INFERNO, 5, 1500));
		Wand.register(new Wand("&r&fArtifact of "+PrintUtils.color(ObsColors.GLACIO)+"&lBjorn&r&f", "bjorn_artifact", Rarity.FIVE, ElementType.GLACIO, 5, 1500));
		Wand.register(new Wand("&r&fCane of "+PrintUtils.color(ObsColors.GEO)+"&lNidus&r&f", "nidus_cane", Rarity.FIVE, ElementType.GEO, 5, 1500));
		Wand.register(new Wand(PrintUtils.color(ObsColors.AERO)+"&lSeth&r&f's Caduceus&r&f", "seth_caduceus", Rarity.FIVE, ElementType.AERO, 5, 1500));
		Wand.register(new Wand("&r&fAntenna of "+PrintUtils.color(ObsColors.COSMO)+"&lEnd&r&f", "antenna_of_end", Rarity.FIVE, ElementType.COSMO, 5, 1500));
	}
}
