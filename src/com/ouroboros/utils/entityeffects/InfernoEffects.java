package com.ouroboros.utils.entityeffects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.ObsTimer;
import com.ouroboros.utils.PrintUtils;

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
		EntityEffects.add(target, PotionEffectType.HUNGER, seconds*20, 0, false);
		EntityEffects.add(target, PotionEffectType.MINING_FATIGUE, seconds*20, 0, false);
		EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds*20, 0, false);
		hasCharred.add(target.getUniqueId());
		ObsTimer.runWithCancel(Ouroboros.instance, e ->
		{
			if (target.isDead() || !hasCharred.contains(target.getUniqueId())) return;
			ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.SMOKE, null);
			ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.FLAME, null);
		}, 20, seconds * 20);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasCharred.remove(target.getUniqueId()), seconds*20);
	}
	
	public static void setMelt(LivingEntity target)
	{
		MobData data = MobData.getMob(target.getUniqueId());
		if (data != null) data.setBreak();
		ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 7, Particle.DRIPPING_WATER, null);
		ObsParticles.drawInfernoCastSigil(target);
	}
	
	public static Set<UUID> hasInfernalBody = new HashSet<>();
	public static boolean addInfernalBody(Player p, int seconds)
	{
		if (hasInfernalBody.contains(p.getUniqueId())) return false;
		
		hasInfernalBody.add(p.getUniqueId());
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> hasInfernalBody.remove(p.getUniqueId()), seconds*20);
		
		return true;
	}
	
	public static Map<UUID, Boolean> isImbuedPlayer = new HashMap<>();
	public static boolean addImbueFire(Player p, int seconds)
	{
		if (isImbuedPlayer.containsKey(p.getUniqueId()))
		{
			PrintUtils.PrintToActionBar(p, "&cAlready Imbued!");
			return false;
		}
		
		EntityEffects.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER);
		EntityEffects.playSound(p, Sound.ENTITY_BLAZE_BURN, SoundCategory.MASTER);
		isImbuedPlayer.put(p.getUniqueId(), true);
		
		Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(Ouroboros.class), () -> 
		{
			isImbuedPlayer.remove(p.getUniqueId());
			EntityEffects.playSound(p, Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.MASTER);
			ObsParticles.drawInfernoCastSigil(p);
		}, 600);
		
		return true;
	}
	
	public static Set<UUID> primed = new HashSet<>();
	public static boolean addPrimer(LivingEntity le, int seconds)
	{
		if (primed.contains(le.getUniqueId())) return false;
		primed.add(le.getUniqueId());
		
		ObsParticles.drawWisps(le.getLocation(), le.getWidth(), le.getHeight(), 5, Particle.LAVA, null);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> primed.remove(le.getUniqueId()), seconds * 20);
		
		return true;
	}
}
