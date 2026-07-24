package com.eol.echoes.instances.geo;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.sword.Bolster;
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

public class KnightsBroadsword extends AbstractEOLWeapon
{

	public KnightsBroadsword()
	{
		super("&r&e&lΣOL&r&f: &oKnight's Broadsword "+PrintUtils.color(ObsColors.GEO)+"✦", 
				"knights_broadsword", true,
				new EOLRecipe(MateriaType.IRON, MateriaType.PELT, MateriaType.GEO), 
				EchoForm.SWORD, 
				ElementiumSlotType.GEO,
				ElementType.GEO,
				buildModifiers(), 
				new EchoData(75, 2.5, .20, 2.0, 750, 750),
				new Bolster().getInternalName(),
				null);
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.OVERWORLD, CombatStat.ATTACK, 0.50, true, false),
            new ActiveEchoModifier(ModifierCondition.OVERWORLD, CombatStat.CRIT_RATE, 0.15, true, false),
            new ActiveEchoModifier(ModifierCondition.GROUNDED, CombatStat.CRIT_MODIFIER, 1.5, false, false),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.PROTECTIVE, 0));
    }

}
