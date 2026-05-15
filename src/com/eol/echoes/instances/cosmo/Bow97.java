package com.eol.echoes.instances.cosmo;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.SpatialDistortion;
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

public class Bow97 extends AbstractEOL
{

	public Bow97()
	{
		super("&r&e&lΣOL&r&f: &oBow No. 97 "+PrintUtils.color(ObsColors.COSMO)+"✦", 
				"bow_97", true, 
				new EOLRecipe(MateriaType.BOW, MateriaType.STRING, MateriaType.COSMO), 
				EchoForm.BOW, 
				ElementiumSlotType.COSMO, 
				buildModifiers(),
				new EchoData(100, 5.0, .45, 3.0, 2000, 2000),
				new SpatialDistortion().getInternalName(),
				"&r&7[ System Log 97 ]",
				"..Critical system errors causing spatial distortion anomalies..",
				"..Instance designation: "+PrintUtils.color(ObsColors.CELESTIO)+"&oLumina&r&7&o, bypassed integrity firewalls..",
				"Crash suspected to occur at any moment.. enumeration values corrupted..",
				"..Warn: Unnable to keep up, is the server online? '..thought we patched this!'",
				"Attempting final emergency reset in 3.. 2.. 1.. 'Hope this works!'",
				"&r&7[ Log End 1632:7:22-12:12:01]");
	}

	@SuppressWarnings("null")
	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveModifier(WeaponModifierCondition.OVERWORLD, CombatStat.ATTACK, 0.80, true, false),
    		new ActiveModifier(WeaponModifierCondition.NETHER, CombatStat.CRIT_RATE, 0.25, true, false),
    		new ActiveModifier(WeaponModifierCondition.END, CombatStat.CRIT_MODIFIER, 4.0, false, false),
    		new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 1),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.INFINITY, 1),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.COSMO_ARMAMENT, 1));
    }
	
}
