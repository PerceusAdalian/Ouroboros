package com.eol.echoes.instances.arcano;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.SpatialDistortion;
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

public class LanceOfLordran extends AbstractEOLWeapon
{

	public LanceOfLordran()
	{
		super("&r&e&lΣOL&r&f: &oLance of Lordran "+PrintUtils.color(ObsColors.ARCANO)+"✦", 
				"bow_97", true, 
				new EOLRecipe(MateriaType.IRON, MateriaType.PELT, null), 
				EchoForm.POLEARM, 
				ElementiumSlotType.COSMO, 
				buildModifiers(),
				new EchoData(0, 4.5, .40, 1.5, 1500, 1500),
				null,
				new SpatialDistortion().getInternalName(),
				null);
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveEchoModifier(ModifierCondition.ELEMENTAL, CombatStat.ATTACK, 0.80, true, false),
    		new ActiveEchoModifier(ModifierCondition.ELEMENTAL, CombatStat.CRIT_RATE, 0.35, true, false),
    		new ActiveEchoModifier(ModifierCondition.ELEMENTAL, CombatStat.CRIT_MODIFIER, 3.5, false, false),
    		new PassiveModifier(ModifierCondition.DURING_DAY, PassiveEchoEffect.BURNING, 0.75),
    		new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.EXPOSE, 0.75),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.ARCANO_ARMAMENT, 1));
    }
	
}
