package com.eol.echoes.instances.heresio;

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

public class LegionaireSabatons extends AbstractEOLArmor
{

	public LegionaireSabatons()
	{
		super("&r&e&lΣOL&r&f: &oLegionaire Sabatons "+PrintUtils.color(ObsColors.HERESIO)+"✦", 
				"legionaire_sabatons", 
				EOLRecipe.of(MateriaType.NETHERITE, MateriaType.PELT), 
				new ArmorData(125, 0.075, 40, 0.10, 2500, 2500), 
				EchoForm.BOOTS, 
				ElementType.CELESTIO,
				buildModifiers(), 
				false, 
				null);
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
        		new ActiveArmorModifier(ModifierCondition.ELEMENTAL, ArmorStat.ARMOR_RATING, 45, false, false),
                new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.BLOCK_RATE, 0.05, true, false),
                new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.CRITICAL_ARMOR_RATING, 25, false, false),
                new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.CRITICAL_BLOCK_RATE, 0.05, true, false),
                new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 1));
    }
}
