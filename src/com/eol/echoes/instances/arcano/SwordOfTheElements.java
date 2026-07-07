package com.eol.echoes.instances.arcano;

import java.util.List;

import com.eol.echoes.EchoData;
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
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.PrintUtils;

public class SwordOfTheElements extends AbstractEOLWeapon
{

	public SwordOfTheElements()
	{
		super("&r&e&lΣOL&r&f: &oSword of the Elements "+PrintUtils.color(ObsColors.ARCANO)+"✦", 
				"sword_of_the_elements", true,
				EOLRecipe.of(MateriaType.IRON, MateriaType.PELT, null), 
				EchoForm.SWORD, 
				ElementiumSlotType.MODULO, 
				buildModifiers(), 
				new EchoData(50, 4.0, .25, 2.5, 750, 750),
				null,
				null);
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.ELEMENTAL, CombatStat.ATTACK, 0.75, true, false),
            new ActiveEchoModifier(ModifierCondition.ELEMENTAL, CombatStat.CRIT_RATE, 0.50, true, false),
            new ActiveEchoModifier(ModifierCondition.ELEMENTAL, CombatStat.CRIT_MODIFIER, 1.5, false, false),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.PROTECTIVE, 1),
            new PassiveModifier(ModifierCondition.PVE, PassiveEchoEffect.ARCANO_ARMAMENT, 1));
    }
}
