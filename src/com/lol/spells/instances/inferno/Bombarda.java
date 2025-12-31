package com.lol.spells.instances.inferno;

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

public class Bombarda extends Spell
{

	public Bombarda() 
	{
		super("Bombarda", "bombarda", Material.BLAZE_POWDER, SpellType.OFFENSIVE, SpellementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 200, 3, 
				"&r&fExpell a short-range burst of &c&lInferno&r&f energy",
				"&r&fcausing a concentrated explosion of ether about your target,",
				"&r&finflicting &e&lBlast&r&f damage and causing &cBurn &7(20s).",
				"&r&f&lRange&r&f: 25 &b&ometers &r&7| &f&lYield&r&f: 3","",
				"&r&cBurn&f: DOT effect constantly dealing &c&lInferno&r&f damage.","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&c&oCinder Plume&r&7&o', however, colloquially known as '&r&c&oBombarda&r&7&o'.");
		
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 25);
		if (!(target instanceof LivingEntity) || target == null) return false;
		OBSParticles.drawInfernoCastSigil(p);
		OBSParticles.drawAngledArcLine(p.getLocation(), target.getLocation(), 0.5, 3, 90, 30, Particle.FLAME, null);
		EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			EntityEffects.addBurn((LivingEntity) target, 10);
			OBSParticles.drawSpiralVortex(target.getLocation(), target.getWidth(), 3, 0.1, Particle.LAVA, null);
			OBSParticles.drawVerticalVortex(target.getLocation(), target.getWidth()+1, target.getHeight(), 0.5, 30, 10, 0.5, Particle.SMOKE, null);
		}, 15);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->target.getWorld().createExplosion(target.getLocation(), 3, false, false), 20);
		return true;
	}

}
