package com.ouroboros.objects.instances;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.PrintUtils;

public class ManaGem extends AbstractObsObject
{

	public ManaGem()
	{
		super("Mana Gem", "mana_gem", Material.LAPIS_LAZULI, true, false, 
				PrintUtils.assignRarity(Rarity.THREE),"",
				"&r&fA refined manifestation of "+PrintUtils.color(ObsColors.ARCANO)+"&lArcano&r&f energy.","",
				"&r&f&lUsage&r&f:",
				"&r&fKeep in &ooff-hand&r&f to &a&orestore &r&f500 &9&lMana&r&f in a pinch.","",
				"&r&fThis item is &d&ostackable&r&f and &c&l&odestroyed&r&f upon use.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e)
	{
		return true;
	}

}
