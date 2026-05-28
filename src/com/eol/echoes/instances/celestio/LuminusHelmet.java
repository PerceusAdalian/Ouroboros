package com.eol.echoes.instances.celestio;

import java.util.List;

import com.eol.echoes.ArmorData;
import com.eol.echoes.instances.AbstractEOLArmor;
import com.eol.echoes.records.ActiveArmorModifier;
import com.eol.echoes.records.EOLRecipe;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.eol.enums.ArmorStat;
import com.eol.enums.EchoForm;
import com.eol.enums.MateriaType;
import com.eol.enums.ModifierCondition;
import com.eol.enums.PassiveEchoEffect;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.PrintUtils;

public class LuminusHelmet extends AbstractEOLArmor
{

	public LuminusHelmet()
	{
		super("&r&e&lΣOL&r&f: &oHelm of Luminus "+PrintUtils.color(ObsColors.CELESTIO)+"✦", 
				"luminus_helmet", 
				EOLRecipe.of(MateriaType.GOLD, MateriaType.PELT), 
				new ArmorData(20, 0.05, 15, 0.1, 1000, 1000), 
				EchoForm.HELMET, 
				buildModifiers(), 
				true, 
				null);
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveArmorModifier(ModifierCondition.UNDEAD, ArmorStat.ARMOR_RATING, 25, false, false),
            new ActiveArmorModifier(ModifierCondition.UNDEAD, ArmorStat.BLOCK_RATE, 0.1, true, false),
            new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.CRITICAL_ARMOR_RATING, 15, false, false),
            new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.CRITICAL_BLOCK_RATE, 0.10, true, false),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.NIGHTSIGHT, 1),
            new PassiveModifier(ModifierCondition.INCOMING_DAMAGE, PassiveEchoEffect.EXPOSE, 0.1));
    }
}
