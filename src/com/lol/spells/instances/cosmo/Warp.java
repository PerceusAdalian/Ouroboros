package com.lol.spells.instances.cosmo;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;

public class Warp extends Spell
{

	public Warp()
	{
		super("Warp", "warp", Material.NETHER_PORTAL, SpellType.UTILITY, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.FIVE, 100, 60, false,
				"&r&fWarp between 5 saved locations. &b&lMana&r&f is only deducted on &3Teleport&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
//		Player p = e.getPlayer();
		
		return 0;
	}

	@Override
	public int getTotalManaCost()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
