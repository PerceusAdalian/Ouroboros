package com.eol.echoes.instances.mortio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.BloodrotBlade;
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

public class ButchersGreatsword extends AbstractEOLWeapon
{

	public ButchersGreatsword()
	{
		super("&r&e&lΣOL&r&f: &oButcher's Greatsword "+PrintUtils.color(ObsColors.MORTIO)+"✦", 
				"butchers_greatsword", false,
				new EOLRecipe(MateriaType.NETHERITE, MateriaType.LEATHER, MateriaType.MORTIO), 
				EchoForm.SWORD, 
				ElementiumSlotType.MORTIO,
				ElementType.MORTIO,
				buildModifiers(), 
				new EchoData(250, 1.5, .10, 3, 150, 150),
				new BloodrotBlade().getInternalName(), 
				null);
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.PVP, CombatStat.ATTACK, 150, false, false),
            new ActiveEchoModifier(ModifierCondition.PVP, CombatStat.CRIT_RATE, 0.45, true, false),
            new ActiveEchoModifier(ModifierCondition.PVP, CombatStat.CRIT_MODIFIER, 1.5, false, false),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.POISONOUS, 0.45),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.FATIGUING, 0.85),
            new PassiveModifier(ModifierCondition.PVP, PassiveEchoEffect.MORTIO_ARMAMENT, 1));
    }
	
}
