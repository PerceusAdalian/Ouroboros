package com.eol.echoes.instances.celestio;

import java.util.List;

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

public class LuminusBroadsword extends AbstractEOL
{

	public LuminusBroadsword()
	{
		super("&r&e&lΣOL&r&f: &oLuminus' Broadsword &r&e&l✦", 
				"luminus_broadsword", true,
				new EOLRecipe(MateriaType.GOLD, MateriaType.PELT, MateriaType.CELESTIO), 
				EchoForm.SWORD, 
				ElementiumSlotType.CELESTIO, 
				buildModifiers(), 
				"radiance", 
				"&r&fMy &e&oBeloved Sister&r&f-- Always one to make me smile.",
				"&r&eShe&f gave me this sword so that I may repel both the darkness of &2Twilight&f",
				"&r&fand the darkness I hold within myself. I bear arms in honor of her,",
				"&r&fand wish to be by her side in due time. &o'&r&e&oGlory to Her Majesty&r&f&o'...");
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveModifier(WeaponModifierCondition.UNDEAD,  CombatStat.ATTACK,        0.25, true),
            new ActiveModifier(WeaponModifierCondition.UNDEAD, CombatStat.CRIT_RATE, 0.30, true),
            new ActiveModifier(WeaponModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 1.0, false),
            new PassiveModifier(WeaponModifierCondition.PASSIVE, PassiveEchoEffect.EXPOSE, 0.25),
            new PassiveModifier(WeaponModifierCondition.STORMY_WEATHER, PassiveEchoEffect.MOVEMENT_SPEED, 0));
    }

}
