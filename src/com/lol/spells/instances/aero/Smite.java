package com.lol.spells.instances.aero;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class Smite extends Spell
{

	public Smite() 
	{
		super("Smite", "smite", Material.AMETHYST_SHARD, SpellType.OFFENSIVE, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 25, 1, false,
				"&r&fSmite your target within &b&o25 meters&r&f");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 25);
		if (!(target instanceof LivingEntity) || target == null) return false;
		EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.AMBIENT);
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.ELECTRIC_SPARK, null);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			target.getWorld().strikeLightning(target.getLocation()), 15);
		return true;
	}

}
