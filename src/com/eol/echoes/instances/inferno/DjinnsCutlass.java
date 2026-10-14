package com.eol.echoes.instances.inferno;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.GeniesWrath;
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

public class DjinnsCutlass extends AbstractEOLWeapon
{

	public DjinnsCutlass()
	{
		super("&r&e&lΣOL&r&f: &oDjinn's Khopesh "+PrintUtils.color(ObsColors.INFERNO)+"✦", 
				"djinn_khopesh", false, 
				new EOLRecipe(MateriaType.GOLD, MateriaType.PELT, MateriaType.INFERNO), 
				EchoForm.SWORD, 
				ElementiumSlotType.INFERNO,
				ElementType.INFERNO,
				buildModifiers(),
				new EchoData(150, 3.5, .25, 2.0, 1250, 1250),
				new GeniesWrath().getInternalName(),
				null);
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveEchoModifier(ModifierCondition.HOTBIOMES, CombatStat.ATTACK, 1.45, true, false),
    		new ActiveEchoModifier(ModifierCondition.HOTBIOMES, CombatStat.CRIT_RATE, 0.60, true, false),
    		new ActiveEchoModifier(ModifierCondition.HOTBIOMES, CombatStat.CRIT_MODIFIER, 1.5, false, false),
            new PassiveModifier(ModifierCondition.DURING_DAY, PassiveEchoEffect.BURNING, 0.45),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.INFERNO_ARMAMENT, 1));
    }

}
