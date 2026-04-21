package com.ouroboros.utils.entityeffects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.helpers.WildcardData;

public class HeresioEffects
{
	public static Set<UUID> isCursed = new HashSet<>();
	/**
	 * @param target
	 * @param magnitude
	 * @param seconds
	 */
	/*
	 * "&r&4Curse &eEffect&f: Applies a debilitation that &b&oslows&r&f, &b&oweakens&r&f,",
				"&r&fand &b&ofatigues&r&f those afflicted. Curses are &e&ocurable&r&f and cannot stack.",
				"&r&fThose afflicted by a curse also receive 20% more &4&lMortio&r&f damage."
	 */
	public static boolean addCurse(LivingEntity target, int magnitude, int seconds)
	{
		if (isCursed.contains(target.getUniqueId())) return false;
		isCursed.add(target.getUniqueId());
		
		if (target instanceof Player p)
		{
			PrintUtils.PrintToActionBar(p, "&c&oYou've been cursed!");
			EntityEffects.playSound(p, Sound.ENTITY_WITHER_SPAWN, SoundCategory.AMBIENT);
			OBSParticles.drawMortioCastSigil(p);
		}
		
		EntityEffects.add(target, PotionEffectType.WEAKNESS, seconds * 20, magnitude, false);
		EntityEffects.add(target, PotionEffectType.MINING_FATIGUE, seconds * 20, magnitude, false);
		EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds * 20, magnitude, false);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			if (!isCursed.contains(target.getUniqueId())) return;
			
			if (target instanceof Player p)
			{
				PrintUtils.PrintToActionBar(p, "&e&oThe curse subsides..");
				EntityEffects.playSound(p, Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT);
			}
			
			isCursed.remove(target.getUniqueId());
		}, seconds * 20);
		
		return true;
	}
	
	public static Map<UUID, WildcardData> isHexed = new HashMap<>();
	
	/**
	 * @param target
	 * @param magnitude
	 * @param seconds
	 */
	public static void addWildcard(LivingEntity target, int magnitude, int seconds)
	{
		PotionEffectType debuff = EntityEffects.debuffs.iterator().next();
		target.addPotionEffect(new PotionEffect(debuff, seconds * 20, magnitude, true, true, true));
		isHexed.put(target.getUniqueId(), new WildcardData(debuff, magnitude));
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(Ouroboros.instance, ()->
		{
			if (target instanceof Player p) PrintUtils.PrintToActionBar(p, "&2Wildcard&7&o hex resolved!");
			target.removePotionEffect(debuff);
			isHexed.remove(target.getUniqueId());
		}, seconds*20);
	}
	
	/**
	 * @param target
	 * @param magnitude
	 * @Documented assumed duration is Infinite
	 */
	public static void addWildcard(LivingEntity target, int magnitude)
	{
		PotionEffectType debuff = EntityEffects.debuffs.iterator().next();
		target.addPotionEffect(new PotionEffect(debuff, PotionEffect.INFINITE_DURATION, magnitude, true, true, true));
		isHexed.put(target.getUniqueId(), new WildcardData(debuff, magnitude));
	}
	
	public static Set<UUID> isIntimidated = new HashSet<>();
	public static void addIntimidate(LivingEntity target, int magnitude, int seconds)
	{
		if (isIntimidated.contains(target.getUniqueId())) return;
		isIntimidated.add(target.getUniqueId());
		EntityEffects.add(target, PotionEffectType.MINING_FATIGUE, seconds, magnitude, true);
		EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds, magnitude, true);
		EntityEffects.add(target, PotionEffectType.WEAKNESS, seconds, magnitude, true);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->isIntimidated.remove(target.getUniqueId()), seconds * 20);
	}
	
}
