package com.ouroboros.utils.entityeffects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;

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

		if (duplicateEffectCheck.containsKey(target))return;
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
	
	
	public static Map<UUID, Integer> guarded_registry = new HashMap<>();
	/**
	 * Halves incoming damage for 3 instances.
	 */
	public static boolean addGuarded(Player p, int magnitude, int seconds)
	{
		if (guarded_registry.containsKey(p.getUniqueId())) return false;
		guarded_registry.put(p.getUniqueId(), magnitude);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
		{
			guarded_registry.remove(p.getUniqueId());
			EntityEffects.playSound(p, Sound.ITEM_SHIELD_BREAK, SoundCategory.AMBIENT);
			PrintUtils.PrintToActionBar(p, "&6Guarded&7&o expired..");
		}, seconds * 20);
		return true;
	}
	
	public static void subGuarded(Player p)
	{
		if (!guarded_registry.containsKey(p.getUniqueId())) return;
		
		int magnitude = guarded_registry.get(p.getUniqueId());
		
		if (--magnitude <= 0)
	    {
			EntityEffects.playSound(p, Sound.ITEM_SHIELD_BREAK, SoundCategory.AMBIENT);
			PrintUtils.PrintToActionBar(p, "&6Guarded&7&o expired..");
	        guarded_registry.remove(p.getUniqueId());
	        return;
	    }
		
		EntityEffects.playSound(p, Sound.ITEM_SHIELD_BLOCK, SoundCategory.AMBIENT);
	    guarded_registry.put(p.getUniqueId(), magnitude);
	}
	
	public static Set<UUID> hasVulnerable = new HashSet<>();
	public static void addVulnerable(LivingEntity target, int seconds)
	{
		if (hasVulnerable.contains(target.getUniqueId())) return;
		hasVulnerable.add(target.getUniqueId());
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> hasVulnerable.remove(target.getUniqueId()), seconds * 20);
	}
	
	public static Map<UUID, Integer> isBarbed = new HashMap<>();
	public static boolean addBarbed(Player p, int magnitude, int seconds)
	{
		if (isBarbed.containsKey(p.getUniqueId())) return false;
		isBarbed.put(p.getUniqueId(), magnitude);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->isBarbed.remove(p.getUniqueId()), seconds * 20);
		return true;
	}
}
