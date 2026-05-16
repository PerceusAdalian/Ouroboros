package com.eol.echoes.abilities.instances.special;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.PrintUtils;

public class ArcaneOrder extends EchoAbility
{

	public ArcaneOrder()
	{
		super("Arcane Order", "arcane_order", Material.NETHER_STAR, StatType.MELEE, 0, 0, 0, AbilityType.SPECIALABILITY, ElementType.ARCANO,
				CastConditions.MIXED, EchoForm.POLEARM, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Arcane Order&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oArbanian Stance&r&f --",
				"&r&7&l┏--&r&7{&e✧ &oArbanian Stance&f: "+PrintUtils.color(ObsColors.ARCANO)+"Regal Offence &7<-+-> "+PrintUtils.color(ObsColors.ARCANO)+"Crowned Defense",
				"&r&7&l┗┳- Regal Offense: Base Attack Becomes 125",
				
				"",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Arcane Order&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oThe King's Return&r&f --",
				"");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
