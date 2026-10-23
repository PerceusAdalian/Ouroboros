package com.eol.echoes.instances.cosmo;

import java.util.List;

import com.eol.echoes.EchoData;
import com.eol.echoes.abilities.instances.special.ConvergenceProtocol;
import com.eol.echoes.instances.AbstractEOLWeapon;
import com.eol.echoes.records.EOLRecipe;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.eol.enums.EchoForm;
import com.eol.enums.ElementiumSlotType;
import com.eol.enums.MateriaType;
import com.eol.enums.ModifierCondition;
import com.eol.enums.PassiveEchoEffect;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.PrintUtils;

public class LanceOfNihility extends AbstractEOLWeapon
{

	public LanceOfNihility()
	{
		super("&r&e&lΣOL&r&f: &oArmament of Nihility "+PrintUtils.color(ObsColors.COSMO)+"✦", 
				"lance_of_nihility", true, 
				new EOLRecipe(MateriaType.NETHERITE, MateriaType.STRING, MateriaType.COSMO), 
				EchoForm.POLEARM, 
				ElementiumSlotType.COSMO, 
				ElementType.COSMO,
				buildModifiers(),
				new EchoData(0, 5, 0, 0, 4500, 4500),
				new ConvergenceProtocol().getInternalName(),
				null);
		}
		
		private static List<Modifier> buildModifiers()
	    {
	        return List.of(
	            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.EXPOSE, 1),
	            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.NULLIFYING, 1),
	            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.FATIGUING, 0.80),
	            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.POISONOUS, 0.70),
	            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.BURNING, 0.70),
	            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.INCREASED_MOVEMENT_SPEED, 1),
	            new PassiveModifier(ModifierCondition.PASSIVE, PassiveEchoEffect.PROTECTIVE_ABSOLUTE, 1));
	    }		
}