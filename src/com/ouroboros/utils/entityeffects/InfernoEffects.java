package com.ouroboros.utils.entityeffects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;

public class InfernoEffects
{
	public static void addBurn(LivingEntity target, int seconds) 
	{
		target.setFireTicks(seconds * 20);
	}
	
	public static Set<UUID> hasCharred = new HashSet<>();
	/**
	 * @Effect Causes those affected to have Hunger, Fatigue, and Slowness, while taking 25% more Inferno Damage.
	 * 			20% chance to cause burn while charred.
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
}
