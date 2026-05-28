package com.eol.echoes.instances.celestio;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.LuminusRadiance;
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

public class LuminusBroadsword extends AbstractEOLWeapon
{

	public LuminusBroadsword()
	{
		super("&r&e&lΣOL&r&f: &oLuminus' Broadsword "+PrintUtils.color(ObsColors.CELESTIO)+"✦", 
				"luminus_broadsword", true,
				new EOLRecipe(MateriaType.GOLD, MateriaType.PELT, MateriaType.CELESTIO), 
				EchoForm.SWORD, 
				ElementiumSlotType.CELESTIO, 
				buildModifiers(), 
				new EchoData(35, 2.5, .50, 3, 1000, 1000),
				null,
				new LuminusRadiance().getInternalName(), 
				"My beloved sister—she who never failed to make me smile.",
				"this blade she entrusted to me, that I might repel the darkness of "+PrintUtils.color(ObsColors.HERESIO)+"Twilight&7",
				"and the darkness that festers within my own soul. I carry it in her name,",
				"and await the day I may stand beside her again.",
				"&o'&r"+PrintUtils.color(ObsColors.CELESTIO)+"&oGlory to Her Majesty&r&7&o'...");
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.UNDEAD, CombatStat.ATTACK, 0.25, true, false),
            new ActiveEchoModifier(ModifierCondition.UNDEAD, CombatStat.CRIT_RATE, 0.30, true, false),
            new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 1.5, false, false),
            new ActiveEchoModifier(ModifierCondition.LIVING, CombatStat.ATTACK, -0.90, true, true),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.EXPOSE, .15),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.PROTECTIVE, 1));
    }
	
}