package com.eol.echoes.instances.cosmo;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.Annihilate;
import com.eol.echoes.instances.AbstractEOL;
import com.eol.echoes.records.ActiveModifier;
import com.eol.echoes.records.EOLRecipe;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.eol.enums.CombatStat;
import com.eol.enums.EchoForm;
import com.eol.enums.ElementiumSlotType;
import com.eol.enums.MateriaType;
import com.eol.enums.PassiveEchoEffect;
import com.eol.enums.WeaponModifierCondition;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.PrintUtils;

public class Axe84 extends AbstractEOL
{

	public Axe84()
	{
		super("&r&e&lΣOL&r&f: &oHatchet No. 84 "+PrintUtils.color(ObsColors.COSMO)+"✦", 
				"axe_84", true, 
				new EOLRecipe(MateriaType.NETHERITE, MateriaType.STRING, MateriaType.COSMO), 
				EchoForm.HATCHET, 
				ElementiumSlotType.COSMO, 
				buildModifiers(),
				new EchoData(100, 4, .45, 3, 4500, 4500),
				new Annihilate().getInternalName(),
				"&r&7[ System Log 84 ]",
				"..Errors flooding the system, unnable to restore from backups..",
				"Critical system overload imminent: Chronic, excess",
				"resource draw detected in main branch..",
				"..Instance designation: "+PrintUtils.color(ObsColors.CELESTIO)+"&oLumina&r&7&o, bypassed test protocols..",
				"..exceeding theoretical limit.. Override Authorized:",
				"Attempting emergency reset in 3.. 2.. ",
				"&r&7[ Log End 1619:5:15-18:43:43]");
	}
	
	@SuppressWarnings("null")
	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveModifier(WeaponModifierCondition.OVERWORLD, CombatStat.ATTACK, 0.40, true, false),
    		new ActiveModifier(WeaponModifierCondition.END, CombatStat.CRIT_RATE, 0.25, true, false),
    		new ActiveModifier(WeaponModifierCondition.COSMIC, CombatStat.CRIT_MODIFIER, 2.0, false, false),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.PROTECTIVE, 0),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 0),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.POISONOUS, 1),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.COSMO_ARMAMENT, 1));
    }

}
