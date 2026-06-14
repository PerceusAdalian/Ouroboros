package com.eol.echoes.instances.inferno;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.LanceFlammes;
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

public class BowOfAgni extends AbstractEOLWeapon
{

	public BowOfAgni() 
	{
		super("&r&e&lΣOL&r&f: &oBow of Agni "+PrintUtils.color(ObsColors.INFERNO)+"✦", 
				"bow_of_agni", true, 
				new EOLRecipe(MateriaType.BOW, MateriaType.STRING, MateriaType.INFERNO), 
				EchoForm.BOW, 
				ElementiumSlotType.INFERNO, 
				buildModifiers(),
				new EchoData(50, 5, .30, 2.5, 1250, 1250),
				new LanceFlammes().getInternalName(),
				"&r&b&oVoyageuse&r&7&o, Puisque tu as pris mon arc, je t'enjoins... de m'utiliser",
				"&r&7&ovéritablement, tout comme "+PrintUtils.color(ObsColors.CELESTIO)+"&oelle &r&7&ol'avait voulu.");
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveEchoModifier(ModifierCondition.HOTBIOMES, CombatStat.ATTACK, .90, true, false),
    		new ActiveEchoModifier(ModifierCondition.HOTBIOMES, CombatStat.CRIT_RATE, 0.45, true, false),
    		new ActiveEchoModifier(ModifierCondition.HOTBIOMES, CombatStat.CRIT_MODIFIER, 1.5, false, false),
    		new ActiveEchoModifier(ModifierCondition.COLDBIOMES, CombatStat.ATTACK, -.75, true, true),
            new PassiveModifier(ModifierCondition.DURING_DAY, PassiveEchoEffect.BURNING, 0.35),
            new PassiveModifier(ModifierCondition.HOTBIOMES, PassiveEchoEffect.INFINITY, 1));
    }

}
