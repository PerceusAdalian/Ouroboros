package com.lol.spells.instances.cosmo;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.PrintUtils;

public class Collapse extends Spell
{
	// TODO
	public Collapse()
	{
		super("Collapse", "collapse", Material.ENDER_PEARL, SpellType.OFFENSIVE, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 150, 2.5, false,
				"&r&fPull nearby targets inward toward the target mob &7(30m)&f",
				"&r&fDeals "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f damage equal to &6&&otarget mob's &r&b&olevel&f.",
				"&r&fDeals &e&lCrush&r&f damage equal to the sum of pulled targets' levels ÷ 4");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
//		Player p = e.getPlayer();
		return 150;
	}

	@Override
	public int getTotalManaCost()
	{
		return 150;
	}

}
