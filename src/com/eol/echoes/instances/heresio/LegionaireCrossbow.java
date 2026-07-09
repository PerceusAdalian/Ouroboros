package com.eol.echoes.instances.heresio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.crossbow.QuickLoad;
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

public class LegionaireCrossbow extends AbstractEOLWeapon
{

	public LegionaireCrossbow() 
	{
		super("&r&e&lΣOL&r&f: &oLegionaire Crossbow "+PrintUtils.color(ObsColors.HERESIO)+"✦", 
				"legionaire_crossbow", false, 
				new EOLRecipe(MateriaType.CROSSBOW, MateriaType.STRING, MateriaType.HERESIO), 
				EchoForm.CROSSBOW,
				ElementiumSlotType.HERESIO,
				ElementType.HERESIO,
				buildModifiers(),
				new EchoData(150, 3.0, .45, 3.5, 2500, 2500),
				new QuickLoad().getInternalName(),
				null);
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.ATTACK, 0.65, true, false),
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_RATE, 0.25, true, false),
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 1.5, false, false),
    		new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.RECYCLE_ARROWS, 1),
            new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.HERESIO_ARMAMENT, 1));
    }

}
