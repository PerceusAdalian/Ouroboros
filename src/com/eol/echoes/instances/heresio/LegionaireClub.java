package com.eol.echoes.instances.heresio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.SpiritBreak;
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

public class LegionaireClub extends AbstractEOLWeapon
{

	public LegionaireClub() 
	{
		super("&r&e&lΣOL&r&f: &oLegionaire Warhammer "+PrintUtils.color(ObsColors.HERESIO)+"✦", 
				"legionaire_club", false, 
				new EOLRecipe(MateriaType.HAMMER, MateriaType.LEATHER, MateriaType.HERESIO), 
				EchoForm.HAMMER,
				ElementiumSlotType.HERESIO,
				ElementType.HERESIO,
				buildModifiers(),
				new EchoData(150, 1.5, .25, 3.5, 3500, 3500),
				new SpiritBreak().getInternalName(),
				null);
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.ATTACK, 0.35, true, false),
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_RATE, 0.15, true, false),
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 1.5, false, false),
    		new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.POISONOUS, .45),
            new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.HERESIO_ARMAMENT, 1));
    }

}
