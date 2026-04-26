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
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class Bombarda extends Spell
{

	public Bombarda() 
	{
		super("Bombarda", "bombarda", Material.BLAZE_POWDER, SpellType.OFFENSIVE, SpellementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 200, 3, false,
				"&r&fExpell a short-range burst of &c&lInferno&r&f energy",
				"&r&fcausing a concentrated explosion of ether about your target,",
				"&r&finflicting &e&lBlast&r&f damage and causing &cBurn &7(20s).",
				"&r&f&lRange&r&f: 25 &b&ometers &r&7| &f&lYield&r&f: 3","",
				"&r&cBurn&f: DOT effect constantly dealing "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f damage.","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&c&oCinder Plume&r&7&o', however, colloquially known as '&r&c&oBombarda&r&7&o'.");
		
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		return playSpellEffect(p, 0, 25) ? 200 : -1;
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

	public static boolean playSpellEffect(Player p, double damage, int range)
	{
		Entity target = RayCastUtils.getEntity(p, range);
		if (!(target instanceof LivingEntity) || target instanceof Player || target == null) return false;
		
		ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.FLAME, null);
		EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			InfernoEffects.addBurn((LivingEntity) target, 10);
			ObsParticles.drawSpiralVortex(target.getLocation(), target.getWidth(), 3, 0.1, Particle.LAVA, null);
			ObsParticles.drawWave(Ouroboros.instance, target.getLocation(), 6, 0.5, 6, Particle.FLAME, null);
		}, 9);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			if (damage != 0 && damage > 0)
			{
				ObsParticles.drawDisc(target.getLocation(), target.getWidth(), 1, 4, 0.1, Particle.DUST_PLUME, null);
				EntityEffects.playSound(p, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT);
				MobData.damageUnnaturally(p, target, damage, true, true, ElementType.INFERNO);
			}
			else target.getWorld().createExplosion(target.getLocation(), 3, false, false);
		}, 14);
		
		return true;
	}
	
}
