package com.eol.echoes.abilities.instances.combat;

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
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Flamelash extends EchoAbility
{

	public Flamelash() 
	{
		super("Flamelash", "flamelash", Material.BLAZE_ROD, StatType.MELEE, 5, 1, AbilityType.COMBAT, ElementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD,
				"&r&fSlash upwards to calcinate target mob dealing ",
				"&r&f&l5&r&c♥ &f&lSlash&r&f damage and apply &cBurn&f.",
				"&r&f&lRange: &b7 meters",
				"&r&f&lCooldown: &b5 seconds");
	}

	public static Set<UUID> cooldownTimer = new HashSet<>();
	
	@Override
	public int cast(PlayerInteractEvent e) 
	{
		if (e instanceof PlayerInteractEvent pie)
		{
			Player p = pie.getPlayer();
			if (cooldownTimer.contains(p.getUniqueId())) return -1;
			Entity target = RayCastUtils.getEntity(p, 7);
			if (target == null || !(target instanceof LivingEntity)) return -1;
			
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), p.getLocation().distance(target.getLocation()), 0.1, Particle.LAVA, null);
			EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.MASTER);
			ObsParticles.drawSpiralVortex(target.getLocation(), target.getWidth(), Math.max(Math.min(1, target.getHeight()), 2), 0, Particle.LAVA, null);
			
			MobData.damageUnnaturally(p, target, 10, true, true, ElementType.INFERNO);
			target.setFireTicks(200);
			
			cooldownTimer.add(p.getUniqueId());
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> cooldownTimer.remove(p.getUniqueId()), 100);
			
			return -1;
		}
		return -1;	
	}
}
