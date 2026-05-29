package com.eol.echoes.instances.mortio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.PlaguesPrimer;
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

public class Plague extends AbstractEOLWeapon
{

	public Plague()
	{
		super("&r&e&lΣOL&r&f: &oPlague "+PrintUtils.color(ObsColors.MORTIO)+"✦", 
				"plague", false,
				new EOLRecipe(MateriaType.CROSSBOW, MateriaType.STRING, MateriaType.MORTIO), 
				EchoForm.CROSSBOW, 
				ElementiumSlotType.MORTIO, 
				buildModifiers(), 
				new EchoData(100, 4.0, .25, 3, 1500, 1500),
				new PlaguesPrimer().getInternalName(), 
				null);
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.ATTACK, 0.50, true, false),
            new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_RATE, 0.30, true, false),
            new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 2.5, false, false),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.POISONOUS, 0.40),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.RECYCLE_ARROWS, 1));
    }
	
}
