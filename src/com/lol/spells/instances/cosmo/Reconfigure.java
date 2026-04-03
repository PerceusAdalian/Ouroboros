package com.lol.spells.instances.cosmo;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Reconfigure extends Spell
{

	public Reconfigure() 
	{
		super("Reconfigure", "reconfigure", Material.CRAFTER, SpellType.UTILITY, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 5, 1, false,
				"&r&fOpen a crafting simulation to reconfigure held items.");
	}

	@SuppressWarnings("deprecation")
	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		p.openWorkbench(null, true);
		EntityEffects.playSound(p, Sound.BLOCK_CRAFTER_CRAFT, SoundCategory.AMBIENT);
		return 5;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 5;
	}

}
