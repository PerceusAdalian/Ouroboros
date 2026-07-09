package com.eol.echoes.instances.heresio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.VexingMalice;
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

public class LegionaireBattleaxe extends AbstractEOLWeapon
{

	public LegionaireBattleaxe() 
	{
		super("&r&e&lΣOL&r&f: &oLegionaire Battleaxe "+PrintUtils.color(ObsColors.HERESIO)+"✦", 
				"legionaire_battleaxe", false, 
				new EOLRecipe(MateriaType.NETHERITE, MateriaType.STRING, MateriaType.HERESIO), 
				EchoForm.HATCHET,
				ElementiumSlotType.HERESIO,
				ElementType.HERESIO,
				buildModifiers(),
				new EchoData(95, 4.5, .30, 1.5, 2500, 2500),
				new VexingMalice().getInternalName(),
				null);
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.ATTACK, 1.25, true, false),
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_RATE, 0.55, true, false),
    		new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 3.0, false, false),
    		new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.POISONOUS, .65),
            new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.HERESIO_ARMAMENT, 1));
    }

}
