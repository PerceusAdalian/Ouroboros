package com.eol.echoes.instances.geo;

import java.util.List;

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

public class HammerOfNidus extends AbstractEOL
{

	public HammerOfNidus()
	{
		super("&r&e&lΣOL&r&f: &oHammer of Nidus "+PrintUtils.color(ObsColors.GEO)+"✦", 
				"hammer_of_nidus", true,
				new EOLRecipe(MateriaType.HAMMER, MateriaType.LEATHER, MateriaType.GEO), 
				EchoForm.HAMMER, 
				ElementiumSlotType.GEO, 
				buildModifiers(), 
				"nidus_preservation", 
				"Thou who wouldst beckon the earth itself -- listen well.",
				"My element, "+PrintUtils.color(ObsColors.GEO)+"&lGeo&r&7&o, is neither gift nor curse.",
				"It is the bones of this world, and yields to none.",
				"Wield me with purpose in &b&oEcho Form&r&7&o, as "+PrintUtils.color(ObsColors.CELESTIO)+"&oShe&r&7&o intended.");
	}
	
	@SuppressWarnings("null")
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveModifier(WeaponModifierCondition.DURING_DAY, CombatStat.ATTACK, 0.50, true, false),
            new ActiveModifier(WeaponModifierCondition.DURING_NIGHT, CombatStat.CRIT_RATE, 0.30, true, false),
            new ActiveModifier(WeaponModifierCondition.GROUNDED, CombatStat.CRIT_MODIFIER, 2, false, false),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.DECREASED_MOVEMENT_SPEED, 0),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.PROTECTIVE, 0));
    }

}
