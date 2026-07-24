package com.eol.echoes.instances.astral;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.Supernova;
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

public class StarsweptGreatsword extends AbstractEOLWeapon
{

	public StarsweptGreatsword()
	{
		super("&r&e&lΣOL&r&f: &oStarswept Greatsword "+PrintUtils.color(ObsColors.ASTRAL)+"✦", 
				"starswept_greatsword", true,
				new EOLRecipe(MateriaType.NETHERITE, MateriaType.LEATHER, MateriaType.COSMO), 
				EchoForm.SWORD, 
				ElementiumSlotType.ASTRAL, 
				ElementType.COSMO,
				buildModifiers(), 
				new EchoData(150, 1.0, .20, 1.5, 4500, 4500),
				new Supernova().getInternalName(), 
				null);
	}
	
	private static List<Modifier> buildModifiers()
    {
        return List.of(
            new ActiveEchoModifier(ModifierCondition.DURING_DAY, CombatStat.ATTACK, 0.85, true, false),
            new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_RATE, 0.55, true, false),
            new ActiveEchoModifier(ModifierCondition.DURING_NIGHT, CombatStat.CRIT_MODIFIER, 3.5, false, false),
            new PassiveModifier(ModifierCondition.DURING_DAY, PassiveEchoEffect.EXPOSE, 0.10),
            new PassiveModifier(ModifierCondition.DURING_DAY, PassiveEchoEffect.CELESTIO_ARMAMENT, 1),
            new PassiveModifier(ModifierCondition.DURING_NIGHT, PassiveEchoEffect.COSMO_ARMAMENT, 1),
            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.PROTECTIVE, 1));
    }

}
