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
		super("Geomorph", "geomorph", Material.BRICK, SpellType.CANTRIP, SpellementType.GEO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 25, 1, true, false,
				"&r&fApplies &6Guarded &bIII&f to &6self&7 (30s)","",
				"&r&6Guarded &eEffect&f: Halves incoming damage for 3 instances.");
	}
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (!GeoEffects.addGuarded(p, 3, 30)) return -1;
		EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
		return 25;
	}

	@Override
	public int getTotalManaCost()
	{
		return 25;
	}
	
}
