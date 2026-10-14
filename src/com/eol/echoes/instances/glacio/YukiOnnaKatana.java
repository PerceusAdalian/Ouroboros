package com.eol.echoes.instances.glacio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.AbsoluteZero;
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

public class YukiOnnaKatana extends AbstractEOLWeapon
{

	public YukiOnnaKatana()
	{
		super("&r&e&lΣOL&r&f: &oKatana of a Frost Maiden "+PrintUtils.color(ObsColors.GLACIO)+"✦",
				"yuki_onna_katana", false, 
				new EOLRecipe(MateriaType.DIAMOND, MateriaType.PELT, MateriaType.GLACIO), 
				EchoForm.SWORD, 
				ElementiumSlotType.GLACIO,
				ElementType.GLACIO,
				buildModifiers(),
				new EchoData(125, 4.0, .35, 2.5, 1500, 1500), 
				new AbsoluteZero().getInternalName(), 
				null);
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
        	new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.ATTACK, 0.80, true, false),
            new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 2.5, false, false),
            new ActiveEchoModifier(ModifierCondition.COLDBIOMES, CombatStat.CRIT_RATE, 0.45, true, false),
            new PassiveModifier(ModifierCondition.COLDBIOMES, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.GLACIO_ARMAMENT, 1));
    }

}
