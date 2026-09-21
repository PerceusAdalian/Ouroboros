package com.eol.echoes.instances.arcano;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.Chroma;
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

public class PainterLance extends AbstractEOLWeapon
{

	public PainterLance()
	{
		super("&r&e&lΣOL&r&f: &oA Painter's Applier "+PrintUtils.color(ObsColors.ARCANO)+"✦", 
				"painter_lance", true, 
				new EOLRecipe(MateriaType.GOLD, MateriaType.STRING, null), 
				EchoForm.POLEARM, 
				ElementiumSlotType.MODULO, 
				ElementType.ARCANO,
				buildModifiers(),
				new EchoData(0, 2.5, .75, 5, 1000, 1000),
				new Chroma().getInternalName(),
				"O' to paint with passion! Let's try a shade of, you, next.");
	}

	private static List<Modifier> buildModifiers()
    {
        return List.of(
    		new ActiveEchoModifier(ModifierCondition.PVE, CombatStat.ATTACK, 100, false, false),
    		new ActiveEchoModifier(ModifierCondition.ELEMENTAL, CombatStat.CRIT_RATE, 0.15, true, false),
    		new ActiveEchoModifier(ModifierCondition.OCCULTIC, CombatStat.CRIT_MODIFIER, 7.5, false, false),
    		new PassiveModifier(ModifierCondition.DURING_DAY, PassiveEchoEffect.EXPOSE, .65),
    		new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.NULLIFYING, .80),
    		new PassiveModifier(ModifierCondition.OVERWORLD, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.ARCANO_ARMAMENT, 1));
    }

}
