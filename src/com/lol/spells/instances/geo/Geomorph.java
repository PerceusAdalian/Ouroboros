package com.lol.spells.instances.geo;

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
import com.ouroboros.utils.entityeffects.GeoEffects;

public class Geomorph extends Spell
{
	
	public Geomorph()
	{
		super("Geomorph", "geomorph", Material.BRICK, SpellType.CANTRIP, SpellementType.GEO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 25, 1, true,
				"&r&fApplies &6Guarded&f to &6self&7 (30s)","",
				"&r&6Guarded &eEffect&f: Halves incoming damage for 3 instances.");
	}
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		GeoEffects.addGuarded(p, 30);
		return 50;
	}

	@Override
	public int getTotalManaCost()
	{
		return 50;
	}
	
}
