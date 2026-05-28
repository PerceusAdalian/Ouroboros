package com.eol.echoes.instances.heresio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.BloodFolliedBlade;
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

public class GeneralFalricStave extends AbstractEOLWeapon
{

	public GeneralFalricStave() 
	{
		super("&r&e&lΣOL&r&f: &oGeneral Falric's Stave "+PrintUtils.color(ObsColors.HERESIO)+"✦", 
				"general_falric_sword", false, 
				new EOLRecipe(MateriaType.NETHERITE, MateriaType.STRING, MateriaType.HERESIO), 
				EchoForm.SWORD, 
				ElementiumSlotType.HERESIO, 
				buildModifiers(),
				new EchoData(95, 4, .35, 3.5, 3500, 3500),
				null,
				new BloodFolliedBlade().getInternalName(),
				"Some dead guy:",
				"\"Excuse me, but did that knight just use his own "+PrintUtils.color(ObsColors.HERESIO)+"&oblood&r&7&o to attack?",
				"Oh gods, it's "+PrintUtils.color(ObsColors.HERESIO)+"&l&oHim&r&7&o.. You-- make haste and return to the capital to warn",
				PrintUtils.color(ObsColors.CELESTIO)+"&oIntegrity Knight Luminus&r&7&o that the "+PrintUtils.color(ObsColors.HERESIO)+"&oAnti-Light Legion&r&7&o",
				"has summoned reinforcements! "+PrintUtils.color(ObsColors.HERESIO)+"&oGeneral Falric &r&7&ohas arriv--!\"");
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.ATTACK, 0.70, true, false),
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_RATE, 0.25, true, false),
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 1.5, false, false),
    		new ActiveEchoModifier(ModifierCondition.OCCULTIC, CombatStat.ATTACK, -0.90, true, true),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.VAMPIRE, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.HERESIO_ARMAMENT, 1),
            new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 1));
    }
	
}
