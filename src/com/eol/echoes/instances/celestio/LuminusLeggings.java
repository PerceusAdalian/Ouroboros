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
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.PrintUtils;

public class LuminusLeggings extends AbstractEOLArmor
{

	public LuminusLeggings()
	{
		super("&r&e&lΣOL&r&f: &oLeggings of Luminus "+PrintUtils.color(ObsColors.CELESTIO)+"✦", 
				"luminus_leggings", 
				EOLRecipe.of(MateriaType.GOLD, MateriaType.PELT), 
				new ArmorData(25, 0.07, 25, 0.1, 1000, 1000), 
				EchoForm.LEGGINGS, 
				ElementType.CELESTIO,
				buildModifiers(), 
				true, 
				null);
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveArmorModifier(ModifierCondition.UNDEAD, ArmorStat.ARMOR_RATING, 25, false, false),
            new ActiveArmorModifier(ModifierCondition.UNDEAD, ArmorStat.BLOCK_RATE, 0.1, true, false),
            new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.CRITICAL_ARMOR_RATING, 20, false, false),
            new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.CRITICAL_BLOCK_RATE, 0.10, true, false),
            new PassiveModifier(ModifierCondition.DURING_DAY, PassiveEchoEffect.DECREASED_MOVEMENT_SPEED, 1),
            new PassiveModifier(ModifierCondition.INCOMING_DAMAGE, PassiveEchoEffect.EXPOSE, 0.1));
    }
}
