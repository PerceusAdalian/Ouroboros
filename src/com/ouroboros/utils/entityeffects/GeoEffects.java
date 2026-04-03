package com.ouroboros.utils.entityeffects;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ouroboros.Ouroboros;

public class GeoEffects
{
	public static void addSanded(LivingEntity target, int seconds)
	{
		EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds*20, 4, false);
		EntityEffects.add(target, PotionEffectType.BLINDNESS, seconds*20, 4, false);
	}
	
	private static Map<Entity, Boolean> duplicateEffectCheck = new HashMap<>();
	public static void addSuffocate(LivingEntity target, int seconds) 
	{

		if (duplicateEffectCheck.containsKey(target))
			return;
		duplicateEffectCheck.put(target, true);
		new BukkitRunnable() 
		{
			int time = seconds * 20;

			@Override
			public void run() 
			{

				if (time-- <= 0 || target.isDead()) 
				{
					duplicateEffectCheck.remove(target);
					cancel();
					return;
				}
				target.setRemainingAir(0); // forces suffocation tick
			}
		}.runTaskTimer(Ouroboros.instance, 0L, 20L); // every second
	}
}
