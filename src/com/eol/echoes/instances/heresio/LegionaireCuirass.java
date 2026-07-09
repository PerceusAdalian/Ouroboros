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

public class LegionaireCuirass extends AbstractEOLArmor
{

	public LegionaireCuirass()
	{
		super("&r&e&lΣOL&r&f: &oLegionaire Cuirass "+PrintUtils.color(ObsColors.HERESIO)+"✦", 
				"legionaire_cuirass", 
				EOLRecipe.of(MateriaType.NETHERITE, MateriaType.PELT), 
				new ArmorData(200, 0.25, 75, 0.20, 2500, 2500), 
				EchoForm.CHESTPLATE, 
				ElementType.HERESIO,
				buildModifiers(), 
				false, 
				null);
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
        		new ActiveArmorModifier(ModifierCondition.ELEMENTAL, ArmorStat.ARMOR_RATING, 75, false, false),
                new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.BLOCK_RATE, 0.10, true, false),
                new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.CRITICAL_ARMOR_RATING, 25, false, false),
                new ActiveArmorModifier(ModifierCondition.DURING_NIGHT, ArmorStat.CRITICAL_BLOCK_RATE, 0.05, true, false),
                new PassiveModifier(ModifierCondition.INCOMING_DAMAGE, PassiveEchoEffect.POISONOUS, .25));
    }
}
