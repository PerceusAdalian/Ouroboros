package com.eol.echoes.instances.heresio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.FoulPlay;
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

public class LegionairePike extends AbstractEOLWeapon
{

	public LegionairePike() 
	{
		super("&r&e&lΣOL&r&f: &oLegionaire Pike "+PrintUtils.color(ObsColors.HERESIO)+"✦", 
				"legionaire_pike", false, 
				new EOLRecipe(MateriaType.NETHERITE, MateriaType.LEATHER, MateriaType.HERESIO), 
				EchoForm.POLEARM,
				ElementiumSlotType.HERESIO,
				ElementType.HERESIO,
				buildModifiers(),
				new EchoData(95, 2.0, .40, 3.5, 2500, 2500),
				new FoulPlay().getInternalName(),
				null);
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.ATTACK, 0.80, true, false),
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_RATE, 0.30, true, false),
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 2.5, false, false),
    		new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.HERESIO_ARMAMENT, 1));
    }

}
