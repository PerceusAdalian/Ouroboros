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
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GeoEffects;

public class Magemorph extends Spell
{

	public Magemorph() 
	{
		super("Magemorph", "magemorph", Material.GOLD_INGOT, SpellType.DEFENSIVE, SpellementType.GEO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 150, 10, false,
				"&r&fApplies &6Guarded &bII and &eWard &b III to &6self&7 (30s, 45s)","",
				"&r&6Guarded &eEffect&f: Halves incoming damage for 2 instances.",
				"&r&eWard Effect&f: grants &b&oAbsorption&r&f, &b&oFire Resistance&r&f, and &b&oResistance&r&f equal to the magnitude of &eWard&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (!GeoEffects.addGuarded(p, 2, 30)) return -1;
		CelestioEffects.addWard(p, 2, 45);
		EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GOLD, SoundCategory.AMBIENT);
		return 150;
	}

	@Override
	public int getTotalManaCost()
	{
		return 150;
	}

}
