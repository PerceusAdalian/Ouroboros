package com.eol.echoes.instances.mortio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.MarkedForDeath;
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

public class ScytheOfBelial extends AbstractEOLWeapon
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
				null,
				new MarkedForDeath().getInternalName(), 
				null);
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.ATTACK, 0.80, true, false),
            new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_RATE, 0.25, true, false),
            new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 4, false, false),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.FATIGUING, 0.50),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.SLOWING, 0.50),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.VAMPIRE, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.MORTIO_ARMAMENT, 1));
    }
	
}
