package com.eol.echoes.instances.glacio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.BjornsGlaciate;
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

public class AxeOfBjorn extends AbstractEOL
{

	public AxeOfBjorn() 
	{
		super("&r&e&lΣOL&r&f: &oAxe of Bjorn "+PrintUtils.color(ObsColors.GLACIO)+"✦",
				"axe_of_bjorn", true, 
				new EOLRecipe(MateriaType.DIAMOND, MateriaType.LEATHER, MateriaType.GLACIO), 
				EchoForm.HATCHET, 
				ElementiumSlotType.GLACIO, 
				buildModifiers(),
				new EchoData(75, 2.5, .40, 2.5, 3000, 3000), 
				new BjornsGlaciate().getInternalName(), 
				"Thou who takes up my axe -- hear me, and be not afraid.",
				"My element, "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&7&o, is not the Cold of Death.",
				"It is stillness.. Preservation.. Warmth-- The moment before the world moves.",
				"Wield me with intention in &b&oEcho Form&7&o, as "+PrintUtils.color(ObsColors.CELESTIO)+"&oShe&r&7&o intended.",
				"And do not confuse my mercy for fragility..",
				"I protect the creatures of the Forest.");
	}

	@SuppressWarnings("null")
	private static List<Modifier> buildModifiers()
    {
        return List.of(
        	new ActiveModifier(WeaponModifierCondition.HOTBIOMES, CombatStat.ATTACK, 0.45, true, false),
            new ActiveModifier(WeaponModifierCondition.INFERNAL, CombatStat.CRIT_MODIFIER, 3, false, false),
            new ActiveModifier(WeaponModifierCondition.INFERNAL, CombatStat.CRIT_RATE, 0.20, true, false),
            new ActiveModifier(WeaponModifierCondition.GLACIAL, CombatStat.ATTACK, -0.90, true, true),
            new PassiveModifier(WeaponModifierCondition.COLDBIOMES, PassiveEchoEffect.PROTECTIVE, 0));
    }
}
