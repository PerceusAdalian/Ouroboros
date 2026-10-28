package com.eol.echoes.instances.celestio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.DivineIntegrity;
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

public class IustusHammer extends AbstractEOLWeapon
{

	public IustusHammer()
	{
		super("&r&e&lΣOL&r&f: &oIustus' Hammer "+PrintUtils.color(ObsColors.CELESTIO)+"✦", 
				"iustus_hammer", true,
				new EOLRecipe(MateriaType.HAMMER, MateriaType.LEATHER, MateriaType.CELESTIO), 
				EchoForm.HAMMER, 
				ElementiumSlotType.CELESTIO, 
				ElementType.CELESTIO,
				buildModifiers(), 
				new EchoData(150, 2.25, .35, 1.5, 3500, 3500),
				new DivineIntegrity().getInternalName(), 
				null);
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.UNDEAD, CombatStat.ATTACK, 200, false, false),
            new ActiveEchoModifier(ModifierCondition.UNDEAD, CombatStat.CRIT_RATE, 0.30, true, false),
            new ActiveEchoModifier(ModifierCondition.UNDEAD, CombatStat.CRIT_MODIFIER, 2.5, false, false),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.EXPOSE, 0.20),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.HUMILITY, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.CELESTIO_ARMAMENT, 1));
    }

}
