package com.ouroboros.utils.entityeffects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.ObsTimer;
import com.ouroboros.utils.entityeffects.helpers.ChillEffectData;

public class GlacioEffects
{
	public static final Map<UUID, ChillEffectData> chillEffects = new HashMap<>();
	/*
	 * Chill Effect: those affected are inflicted with a Glacio DOT effect. Reapplying Chill increases the magnitude, keeping initial duration.
	 */
	/**
	 * 
	 * @param target
	 * @param magnitude
	 * @param seconds
	 */
	public static void addChill(Player p, LivingEntity target, int magnitude, int seconds) 
	{
	    UUID id = target.getUniqueId();
	    ChillEffectData existingEffect = chillEffects.get(id);

	    // Stack magnitude, preserve original expiry time if re-applying
	    int newMagnitude = Math.min(20, (existingEffect != null ? existingEffect.magnitude : 0) + magnitude == 0 ? 1 : magnitude);
	    long expiresAt = existingEffect != null ? existingEffect.expiresAt : System.currentTimeMillis() + (seconds * 1000L);

	    long remainingTicks = existingEffect != null ? existingEffect.getRemainingTicks() : seconds * 20L;

	    // Cancel both old tasks cleanly
	    if (existingEffect != null) existingEffect.cancel();

	    // Slowness — re-apply with remaining duration so it matches DOT expiry
	    target.removePotionEffect(PotionEffectType.SLOWNESS);
	    EntityEffects.add(target, PotionEffectType.SLOWNESS, (int) remainingTicks, newMagnitude, true);

	    // DOT every second for remaining duration
	    final int finalMagnitude = newMagnitude;
	    BukkitTask[] dotTask = new BukkitTask[1]; // forward reference trick
	    dotTask[0] = Bukkit.getScheduler().runTaskTimer(Ouroboros.instance, () -> 
	    {
	        if (target == null || target.isDead() || !target.isValid()) 
	        {
	            chillEffects.remove(id);
	            dotTask[0].cancel();
	            return;
	        }
	        OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 8, Particle.SNOWFLAKE, null);
	        MobData.damageUnnaturally(p, target, finalMagnitude == 0 ? 1 : finalMagnitude, false, true, ElementType.GLACIO);
	    }, 20L, 20L);
	    
	    // Expiry cleanup
	    BukkitTask expiryTask = Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
	    {
	        chillEffects.remove(id);
	        dotTask[0].cancel();
	        target.removePotionEffect(PotionEffectType.SLOWNESS);
	    }, remainingTicks);

	    chillEffects.put(id, new ChillEffectData(newMagnitude, expiresAt, expiryTask));
	}
	
	public static Set<UUID> hasFrosted = new HashSet<>();
	public static void addFrosted(LivingEntity target, int magnitude, int seconds)
	{
		if (hasFrosted.contains(target.getUniqueId())) return;
		hasFrosted.add(target.getUniqueId());
		EntityEffects.add(target, PotionEffectType.WEAKNESS, seconds * 20, magnitude, false);
		EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds * 20, magnitude, false);
		ObsTimer.runWithCancel(Ouroboros.instance, e ->
		{
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.SNOWFLAKE, null);
		}, 20, seconds * 20);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasFrosted.remove(target.getUniqueId()), seconds * 20);
	}
	
	public static Set<UUID> hasFrozen = new HashSet<>();
	public static void addFrozen(LivingEntity target)
	{
		if (chillEffects.containsKey(target.getUniqueId()))
		{
			int magnitude = chillEffects.get(target.getUniqueId()).magnitude;
			chillEffects.remove(target.getUniqueId());
			chillEffects.get(target.getUniqueId()).cancel();
			EntityEffects.add(target, PotionEffectType.SLOWNESS, PotionEffect.INFINITE_DURATION, 99, true);
			EntityEffects.add(target, PotionEffectType.WEAKNESS, PotionEffect.INFINITE_DURATION, magnitude, true);
			if (!(target instanceof Player))
			{
				((Mob) target).setTarget(null);
				target.setAI(false);
			}
		}
	}
}
