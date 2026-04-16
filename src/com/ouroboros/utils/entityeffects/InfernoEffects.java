package com.ouroboros.utils.entityeffects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.OBSParticles;

public class InfernoEffects
{
	public static void addBurn(LivingEntity target, int seconds) 
	{
		target.setFireTicks(seconds * 20);
	}
	
	public static Set<UUID> hasCharred = new HashSet<>();
	/*
	 * "&r&cCharred &eEffect&f: Causes &b&oHunger&r&f, &b&oFatigue&r&f, and &b&oSlowness&r&f, while afflicted take &b&o25%&r&f more&r&f",
				PrintUtils.color(ObsColors.INFERNO)+"&lInferno Damage&r&f, and may cause &cBurn&f upon hit removing the effect."
	 */
	/**
	 * @Effect Charred Effect: Causes Hunger, Fatigue, and Slowness, while afflicted take 25% more
				Inferno Damage, and may cause Burn upon hit removing the effect."
	 * @param target
	 * @param seconds
	 */
	public static void addCharred(LivingEntity target, int seconds)
	{
		target.setFireTicks(seconds * 20);
		EntityEffects.add(target, PotionEffectType.HUNGER, seconds*20, 0, false);
		EntityEffects.add(target, PotionEffectType.MINING_FATIGUE, seconds*20, 0, false);
		EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds*20, 0, false);
		hasCharred.add(target.getUniqueId());
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasCharred.remove(target.getUniqueId()), seconds*20);
	}
	
	public static void setMelt(LivingEntity target)
	{
		MobData data = MobData.getMob(target.getUniqueId());
		if (data != null) data.setBreak();
		OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 7, Particle.DRIPPING_WATER, null);
		OBSParticles.drawInfernoCastSigil(target);
	}
}
