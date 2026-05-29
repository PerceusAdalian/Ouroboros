package com.eol.echoes.instances.cosmo;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.SpatialRend;
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
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.PrintUtils;

public class Sword14 extends AbstractEOLWeapon
{
	
	public Sword14()
	{
		super("&r&e&lΣOL&r&f: &oSword No. 14 "+PrintUtils.color(ObsColors.COSMO)+"✦", 
			"sword_14", false, 
			new EOLRecipe(MateriaType.NETHERITE, MateriaType.STRING, MateriaType.COSMO), 
			EchoForm.SWORD, 
			ElementiumSlotType.COSMO, 
			buildModifiers(),
			new EchoData(100, 4.5, .50, 2.5, 2000, 2000),
			new SpatialRend().getInternalName(),
			"[ System Log 14 ]",
			"..Construction records nominal. Waveform stability: unresolved..",
			"Server load anomalies persist: Memory bleed detected across several instances..",
			"..one instance, designation: "+PrintUtils.color(ObsColors.CELESTIO)+"&oLumina&r&7&o, drawing excess ether..",
			"Anomalous events increasing in frequency. We'll address it after the reset.. ",
			"[ Log End 1616:4:30-17:26:34]");
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveEchoModifier(ModifierCondition.OVERWORLD, CombatStat.ATTACK, 0.30, true, false),
    		new ActiveEchoModifier(ModifierCondition.NETHER, CombatStat.CRIT_MODIFIER, 1.5, false, false),
            new ActiveEchoModifier(ModifierCondition.END, CombatStat.CRIT_RATE, 0.25, true, false),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.FATIGUING, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 0));
    }

}
