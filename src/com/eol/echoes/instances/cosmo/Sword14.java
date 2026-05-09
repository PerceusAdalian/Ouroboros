package com.eol.echoes.instances.cosmo;

import java.util.List;

import com.eol.echoes.abilities.instances.special.SpacialRend;
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

public class Sword14 extends AbstractEOL
{
	/**
	 * @TODO Test Echo, build, and catalyst.
	 */
	
	public Sword14()
	{
		super("&r&e&lΣOL&r&f: &oSword No. 14 "+PrintUtils.color(ObsColors.CELESTIO)+"✦", 
			"sword_14", false, 
			new EOLRecipe(MateriaType.NETHERITE, MateriaType.STRING, MateriaType.COSMO), 
			EchoForm.SWORD, 
			ElementiumSlotType.COSMO, 
			buildModifiers(),
			new SpacialRend().getInternalName(),
			"[ System Log 14 ]",
			"..Construction records nominal. Waveform stability: unresolved..",
			"Server load anomalies persist — origin traced to generative overflow..",
			"Memory bleed detected across several active instances..",
			"..one instance, designation: "+PrintUtils.color(ObsColors.CELESTIO)+"&oLumina&r&7&o, drawing excess ether..",
			"..source unclear.. flagged for review. Cache flush scheduled prior to",
			"next cycle: Subject Echoic Dissonance events increasing in frequency..",
			"We'll address it after the reset.. [ Log End 1616:4:30-17:26:34]");
	}
	
	@SuppressWarnings("null")
	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveModifier(WeaponModifierCondition.OVERWORLD, CombatStat.ATTACK, 0.30, true, false),
    		new ActiveModifier(WeaponModifierCondition.NETHER, CombatStat.CRIT_MODIFIER, 1.5, false, false),
            new ActiveModifier(WeaponModifierCondition.END, CombatStat.CRIT_RATE, 0.25, true, false),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.FATIGUING, 10),
            new PassiveModifier(WeaponModifierCondition.LIVING, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 0));
    }

}
