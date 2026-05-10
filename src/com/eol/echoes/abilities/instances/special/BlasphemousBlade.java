package com.eol.echoes.abilities.instances.special;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;

public class BlasphemousBlade extends EchoAbility
{
	// TODO
	public BlasphemousBlade() 
	{
		super("Blasphemous Blade", "blasphemous_blade", Material.NETHER_STAR, StatType.MELEE, 0, 0, 100, AbilityType.SPECIALABILITY, ElementType.HERESIO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD, 
				"&r&f");
	}

	@Override
	public int cast(PlayerInteractEvent e) 
	{
		return 0;
	}

	@Override
	public int getFinalDurabilityCost() 
	{
		return 0;
	}

}
