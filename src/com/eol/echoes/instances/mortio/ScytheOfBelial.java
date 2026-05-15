package com.eol.echoes.instances.mortio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.MarkedForDeath;
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

public class ScytheOfBelial extends AbstractEOL
{

	public ScytheOfBelial()
	{
		super("&r&e&lΣOL&r&f: &oScythe of Belial "+PrintUtils.color(ObsColors.MORTIO)+"✦", 
				"scythe_of_belial", false,
				new EOLRecipe(MateriaType.NETHERITE, MateriaType.PELT, MateriaType.MORTIO), 
				EchoForm.SCYTHE, 
				ElementiumSlotType.MORTIO, 
				buildModifiers(), 
				new EchoData(85, 5, .40, 2.5, 2000, 2000),
				new MarkedForDeath().getInternalName(), 
				null);
	}

	@SuppressWarnings("null")
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveModifier(WeaponModifierCondition.DURING_NIGHT, CombatStat.ATTACK, 0.80, true, false),
            new ActiveModifier(WeaponModifierCondition.DURING_NIGHT, CombatStat.CRIT_RATE, 0.25, true, false),
            new ActiveModifier(WeaponModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 4, false, false),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.FATIGUING, 0.50),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.SLOWING, 0.50),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.VAMPIRE, 1),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.MORTIO_ARMAMENT, 1));
    }
	
}
