package com.ouroboros.ability.instances.combat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.Ouroboros;
import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.enums.AbilityCategory;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsAbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class Flamelash extends AbstractOBSAbility
{

	public Flamelash() 
	{
		super("Flamelash", "flamelash", Material.BLAZE_ROD, StatType.MELEE, 5, 1, ObsAbilityType.COMBAT, ElementType.SLASH, CastConditions.RIGHT_CLICK_AIR, AbilityCategory.SWORDS,
				"&r&fSlash upwards to calcinate target mob dealing ",
				"&r&f&l5&r&c♥ &f&lSlash&r&f damage and apply &cBurn&f.",
				"&r&f&lRange: &b7 meters",
				"&r&f&lCooldown: &b5 seconds");
	}

	public static Set<UUID> cooldownTimer = new HashSet<>();
	
	@Override
	public boolean cast(Event e) 
	{
		if (e instanceof PlayerInteractEvent pie)
		{
			Player p = pie.getPlayer();
			if (cooldownTimer.contains(p.getUniqueId())) return false;
			Entity target = RayCastUtils.getNearestEntity(p, 7);
			if (target == null || !(target instanceof LivingEntity)) return false;
			
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), p.getLocation().distance(target.getLocation()), 0.1, Particle.LAVA, null);
			EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, SoundCategory.MASTER, 1, 1);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				OBSParticles.drawSpiralVortex(target.getLocation(), target.getWidth(), Math.max(Math.min(1, target.getHeight()), 2), 0, Particle.LAVA, null), 10);
			
			MobData.damageUnnaturally(p, target, 10, true);
			target.setFireTicks(200);
			
			cooldownTimer.add(p.getUniqueId());
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> cooldownTimer.remove(p.getUniqueId()), 100);
			
			return true;
		}
		return false;	
	}
}
