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
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GeoEffects;

public class Dynamorph extends Spell
{

	public Dynamorph() 
	{
		super("Dynamorph", "dynamorph", Material.DIAMOND, SpellType.DEFENSIVE, SpellementType.GEO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 200, 20, false, false,
				"&r&fApplies &6Guarded &bVII&f, &6Barbed &bV&f, and minor &b&oSlowness&r&f to &6self&7 (1min)","",
				"&r&6Guarded &eEffect&f: Halves incoming damage for 7 instances.",
				"&r&6Barbed &eEffect&f: Incoming damage is redirected as "+PrintUtils.color(ObsColors.GEO)+"&lGeo&r&f damage",
				"&r&fequal to the &b&omagnitude&r&f of &6Barbed&f pre &6Guarded&f procs.");
		
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (!GeoEffects.addGuarded(p, 7, 60)) return -1;
		GeoEffects.addBarbed(p, 5, 60);
		EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.AMBIENT);
		return 300;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 300;
	}

}
