package com.lol.spells.instances.geo;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GeoEffects;

public class Metalmorph extends Spell
{

	public Metalmorph() 
	{
		super("Metalmorph", "metalmorph", Material.IRON_INGOT, SpellType.DEFENSIVE, SpellementType.GEO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 50, 1, false, false,
				"&r&fApplies &6Guarded &bV&f and minor &b&oSlowness&r&f to &6self&7 (45s)","",
				"&r&6Guarded &eEffect&f: Halves incoming damage for 5 instances.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (!GeoEffects.addGuarded(p, 5, 45)) return -1;
		EntityEffects.add(p, PotionEffectType.SLOWNESS, 45*20, 2);
		EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_IRON, SoundCategory.AMBIENT);
		return 50;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 50;
	}

}
