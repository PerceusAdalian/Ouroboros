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

public class LegionaireHelm extends AbstractEOLArmor
{

	public LegionaireHelm()
	{
		super("&r&e&lΣOL&r&f: &oLegionaire Helm "+PrintUtils.color(ObsColors.HERESIO)+"✦", 
				"legionaire_helm", 
				EOLRecipe.of(MateriaType.NETHERITE, MateriaType.PELT), 
				new ArmorData(155, 0.175, 35, 0.10, 2500, 2500), 
				EchoForm.HELMET, 
				ElementType.HERESIO,
				buildModifiers(), 
				false, 
				null);
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveArmorModifier(ModifierCondition.ELEMENTAL, ArmorStat.ARMOR_RATING, 55, false, false),
            new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.BLOCK_RATE, 0.05, true, false),
            new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.CRITICAL_ARMOR_RATING, 25, false, false),
            new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.CRITICAL_BLOCK_RATE, 0.05, true, false),
            new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.NIGHTSIGHT, 1));
    }
}
