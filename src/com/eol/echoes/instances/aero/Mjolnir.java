package com.eol.echoes.instances.aero;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.AspectOfThor;
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

public class Mjolnir extends AbstractEOLWeapon
{

	public Mjolnir()
	{
		super("&r&e&lΣOL&r&f: &oMjolnir "+PrintUtils.color(ObsColors.AERO)+"✦", 
				"mjolnir", true,
				EOLRecipe.of(MateriaType.HAMMER, MateriaType.LEATHER, MateriaType.AERO), 
				EchoForm.HAMMER, 
				ElementiumSlotType.AERO, 
				ElementType.AERO,
				buildModifiers(), 
				new EchoData(125, 1.5, .45, 3, 4500, 4500),
				new AspectOfThor().getInternalName(),
				null);
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.STORMY_WEATHER, CombatStat.ATTACK, 125, false, false),
            new ActiveEchoModifier(ModifierCondition.STORMY_WEATHER, CombatStat.CRIT_RATE, 0.30, true, false),
            new ActiveEchoModifier(ModifierCondition.STORMY_WEATHER, CombatStat.CRIT_MODIFIER, 3, false, false),
            new PassiveModifier(ModifierCondition.STORMY_WEATHER, PassiveEchoEffect.PROTECTIVE, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.AERO_ARMAMENT, 1));
    }

}
