package com.eol.echoes.instances.aero;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.KelligirAeroMastery;
import com.eol.echoes.instances.AbstractEOL;
import com.eol.echoes.records.ActiveModifier;
import com.eol.echoes.records.EOLRecipe;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.eol.enums.CombatStat;
import com.eol.enums.EchoForm;
import com.eol.enums.ElementiumSlotType;
import com.eol.enums.MateriaType;
import com.eol.enums.PassiveEchoEffect;
import com.eol.enums.WeaponModifierCondition;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.PrintUtils;

public class BowOfKelligir extends AbstractEOL
{

	public BowOfKelligir()
	{
		super("&r&e&lΣOL&r&f: &oBow of Kelligir "+PrintUtils.color(ObsColors.AERO)+"✦", 
				"bow_of_kelligir", true,
				new EOLRecipe(MateriaType.BOW, MateriaType.STRING, MateriaType.AERO), 
				EchoForm.BOW, 
				ElementiumSlotType.AERO, 
				buildModifiers(), 
				new EchoData(50, 4.5, .15, 5, 1000, 1000),
				new KelligirAeroMastery().getInternalName(), 
				null);
	}
	
	@SuppressWarnings("null")
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveModifier(WeaponModifierCondition.CLEAR_WEATHER, CombatStat.ATTACK, 0.80, true, false),
            new ActiveModifier(WeaponModifierCondition.DURING_DAY, CombatStat.CRIT_RATE, 0.60, true, false),
            new ActiveModifier(WeaponModifierCondition.DURING_DAY, CombatStat.CRIT_MODIFIER, 2.5, false, false),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.PROTECTIVE, 1),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.IGNORE_ARROW, 0.70));
    }
}
