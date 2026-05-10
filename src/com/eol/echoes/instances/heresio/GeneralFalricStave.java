package com.eol.echoes.instances.heresio;

import java.util.List;

import com.eol.echoes.abilities.instances.special.BlasphemousBlade;
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

public class GeneralFalricStave extends AbstractEOL
{

	public GeneralFalricStave() 
	{
		super("&r&e&lΣOL&r&f: &oGeneral Falric's Stave "+PrintUtils.color(ObsColors.HERESIO)+"✦", 
				"general_falric_sword", false, 
				new EOLRecipe(MateriaType.NETHERITE, MateriaType.STRING, MateriaType.HERESIO), 
				EchoForm.SWORD, 
				ElementiumSlotType.COSMO, 
				buildModifiers(),
				new BlasphemousBlade().getInternalName(),
				"Some dead guy:",
				"\"Excuse me, but did that knight just use his own"+PrintUtils.color(ObsColors.HERESIO)+"&oblood&r&7&o to attack?",
				"Oh gods, it's "+PrintUtils.color(ObsColors.HERESIO)+"&l&oHim&r&7&o.. You-- make haste and return to the capital to warn",
				PrintUtils.color(ObsColors.CELESTIO)+"&oIntegrity Knight Luminus&r&7&o that the "+PrintUtils.color(ObsColors.HERESIO)+"&oAnti-Light Legion&r&7&o",
				"has summoned reinforcements! "+PrintUtils.color(ObsColors.HERESIO)+"&oGeneral Falric &r&7&ohas arriv--!\"");
	}

	@SuppressWarnings("null")
	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveModifier(WeaponModifierCondition.DURING_NIGHT, CombatStat.ATTACK, 0.70, true, false),
    		new ActiveModifier(WeaponModifierCondition.DURING_NIGHT, CombatStat.CRIT_RATE, 0.25, true, false),
    		new ActiveModifier(WeaponModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 1.5, false, false),
            new PassiveModifier(WeaponModifierCondition.DURING_NIGHT, PassiveEchoEffect.NIGHTSIGHT, 0),
            new PassiveModifier(WeaponModifierCondition.DURING_NIGHT, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 0));
    }
	
}
