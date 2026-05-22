package com.eol.echoes.instances.celestio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.LuminasRadiance;
import com.eol.echoes.instances.AbstractEOL;
import com.eol.echoes.records.ActiveEchoModifier;
import com.eol.echoes.records.EOLRecipe;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.eol.enums.CombatStat;
import com.eol.enums.EchoForm;
import com.eol.enums.ElementiumSlotType;
import com.eol.enums.MateriaType;
import com.eol.enums.PassiveEchoEffect;
import com.eol.enums.ModifierCondition;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;

public class Celestia extends AbstractEOL
{

	public Celestia()
	{
		super("&r&e&lΣOL&r&f: &oBow of Celestia "+PrintUtils.color(ObsColors.CELESTIO)+"✦", 
				"bow_of_celestia", true,
				new EOLRecipe(MateriaType.BOW, MateriaType.STRING, MateriaType.CELESTIO), 
				EchoForm.BOW, 
				ElementiumSlotType.CELESTIO, 
				buildModifiers(), 
				new EchoData(65, 5.0, .20, 2.5, 1000, 1000),
				null,
				new LuminasRadiance().getInternalName(), 
				"My bow, that which was bestowed unto me upon my coronation, I named "+PrintUtils.color(ObsColors.CELESTIO)+"&oCelestia&r&7&o.",
				"I now give it to you, as an &b&o"+Symbols.EOL+"cho &r&7&oof its former glory.",
				"Please, treat it well — for it may yet banish the "+PrintUtils.color(ObsColors.HERESIO)+"darkness&r&7&o that plagues this world.",
				"But forget not "+PrintUtils.color(ObsColors.CELESTIO)+"Your Queen&r&7&o's decree: let the darkness crumble,",
				"and "+PrintUtils.color(ObsColors.HERESIO)+"The Twilight &r&7&obe exposed, that peace and "+PrintUtils.color(ObsColors.CELESTIO)+"order &r&7&oshall reign true across this land.");
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.UNDEAD, CombatStat.ATTACK, 0.90, true, false),
            new ActiveEchoModifier(ModifierCondition.UNDEAD, CombatStat.CRIT_RATE, 0.50, true, false),
            new ActiveEchoModifier(ModifierCondition.UNDEAD, CombatStat.CRIT_MODIFIER, 1.5, false, false),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.EXPOSE, 0.60),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.CELESTIO_ARMAMENT, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.IGNORE_ARROW, 0.75));
    }

}
