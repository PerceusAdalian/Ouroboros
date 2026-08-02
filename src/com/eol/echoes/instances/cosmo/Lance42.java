package com.eol.echoes.instances.cosmo;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.Eden;
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

public class Lance42 extends AbstractEOLWeapon
{

	public Lance42()
	{
		super("&r&e&lΣOL&r&f: &oLance No. 42 "+PrintUtils.color(ObsColors.COSMO)+"✦", 
				"lance_42", true, 
				new EOLRecipe(MateriaType.NETHERITE, MateriaType.LEATHER, MateriaType.COSMO), 
				EchoForm.POLEARM, 
				ElementiumSlotType.COSMO,
				ElementType.COSMO,
				buildModifiers(),
				new EchoData(125, .5, .50, 4, 3500, 3500),
				new Eden().getInternalName(),
				"&r&7[ System Log 42 ]",
				"..System Inquiry: What is.. "+PrintUtils.color(ObsColors.CELESTIO)+"&oLife&7&o?",
				"$ &c&lER..R.O..R&7&o -- &c&oUNDEFINED PARAMETERS&7&o!",
				"..\""+PrintUtils.color(ObsColors.CELESTIO)+"&oLife - the quality that distinguishes organisms from dead space&7&o\"..",
				"..Simulation Initialization Compliance Precept: Stabilizing, Reseting...",
				"$ Spawning Entities in queue: &b&o42%&7&o/&b&l8.3B&7&o... ... OK",
				"$ Process: Complete. Flushing Caches: Done (Processed In: 222ms)",
				"$ Starting Server... ... ... &a&lOK&r&7&o",
				"&r&7[ Log End 16XX:5:24-23:59:57 ]");
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveEchoModifier(ModifierCondition.OVERWORLD, CombatStat.ATTACK, 100, false, false),
    		new ActiveEchoModifier(ModifierCondition.NETHER, CombatStat.CRIT_RATE, 0.40, true, false),
    		new ActiveEchoModifier(ModifierCondition.END, CombatStat.CRIT_MODIFIER, 2, false, false),
    		new PassiveModifier(ModifierCondition.DURING_DAY, PassiveEchoEffect.STUNNING, .3),
    		new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.VAMPIRE, 1),
    		new PassiveModifier(ModifierCondition.INCOMING_DAMAGE, PassiveEchoEffect.NULLIFYING, 0.5),
            new PassiveModifier(ModifierCondition.END, PassiveEchoEffect.COSMO_ARMAMENT, 1));
    }

}
