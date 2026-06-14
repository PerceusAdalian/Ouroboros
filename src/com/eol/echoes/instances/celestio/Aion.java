package com.eol.echoes.instances.celestio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.PhotonCannon;
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

public class Aion extends AbstractEOLWeapon
{

	public Aion()
	{
		super("&r&e&lΣOL&r&f: &oA.I.O.N. "+PrintUtils.color(ObsColors.CELESTIO)+"✦", 
				"aion", true,
				new EOLRecipe(MateriaType.GOLD, MateriaType.LEATHER, MateriaType.CELESTIO), 
				EchoForm.POLEARM, 
				ElementiumSlotType.CELESTIO, 
				buildModifiers(), 
				new EchoData(100, 2.5, .05, 4.5, 3000, 3000),
				new PhotonCannon().getInternalName(), null);
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.DURING_DAY, CombatStat.CRIT_RATE, 0.65, true, false),
            new ActiveEchoModifier(ModifierCondition.DURING_DAY, CombatStat.CRIT_MODIFIER, 5.5, false, false),
            new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.ATTACK, -1, true, true),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.PROTECTIVE, 1),
            new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.EXPOSE, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.CELESTIO_ARMAMENT, 1));
    }

}
