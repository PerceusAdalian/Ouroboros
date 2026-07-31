package com.eol.echoes.instances.aero;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.Boomburst;
import com.eol.echoes.instances.AbstractEOLWeapon;
import com.eol.echoes.records.ActiveEchoModifier;
import com.eol.echoes.records.EOLRecipe;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.eol.enums.CombatStat;
import com.eol.enums.EchoForm;
import com.eol.enums.ElementiumSlotType;
import com.eol.enums.MateriaType;
import com.eol.enums.ModifierCondition;
import com.eol.enums.PassiveEchoEffect;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.PrintUtils;

public class SethSpear extends AbstractEOLWeapon
{

	public SethSpear()
	{
		super("&r&e&lΣOL&r&f: &oSeth's Talon "+PrintUtils.color(ObsColors.AERO)+"✦", 
				"seth_spear", true,
				EOLRecipe.of(MateriaType.IRON, MateriaType.LEATHER, MateriaType.AERO), 
				EchoForm.POLEARM, 
				ElementiumSlotType.AERO, 
				ElementType.AERO,
				buildModifiers(), 
				new EchoData(75, 4.0, .35, 2.5, 1000, 1000),
				new Boomburst().getInternalName(), 
				"Thou who would claim my talon, heed this warning:",
				"If it's the storm itself you desire, look no further--",
				"This is the very essence of "+PrintUtils.color(ObsColors.AERO)+"&lStorm&r&7&o,",
				"made tangible, just as "+PrintUtils.color(ObsColors.CELESTIO)+"&oShe&r&7&o intended.",
				"But take it not with greed in your heart,",
				"for the storms bend to none but me.");
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.STORMY_WEATHER, CombatStat.ATTACK, 100, false, false),
            new ActiveEchoModifier(ModifierCondition.STORMY_WEATHER, CombatStat.CRIT_RATE, 0.45, true, false),
            new ActiveEchoModifier(ModifierCondition.STORMY_WEATHER, CombatStat.CRIT_MODIFIER, 2.5, false, false),
            new PassiveModifier(ModifierCondition.STORMY_WEATHER, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 1),
            new PassiveModifier(ModifierCondition.STORMY_WEATHER, PassiveEchoEffect.AERO_ARMAMENT, 1));
    }
}
