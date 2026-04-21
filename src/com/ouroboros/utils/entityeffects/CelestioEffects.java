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
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.utils.MobNameplate;
import com.ouroboros.utils.EntityCategories;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.helpers.DivineFavorData;

public class CelestioEffects
{
	public static Set<UUID> hasHumility = new HashSet<>();
	public static void addHumility(LivingEntity target, int seconds)
	{
		if (EntityCategories.celestio_mobs.contains(target.getType())) return;
		if (hasHumility.contains(target.getUniqueId())) return;
		hasHumility.add(target.getUniqueId());
		ObsParticles.drawCelestioCastSigil(target);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> hasHumility.remove(target.getUniqueId()), seconds * 20);
	}
	
	/**
	 * @Description Celestio Signature Effect: Exposed will kill any undead mob if
	 *              it's not a boss.
	 *              If it's not an undead enemy, Exposed will BREAK any mob so long
	 *              as it's also not a boss.
	 *              Finally, Exposed also adds the "Glowing" effect.
	 */
	/*
		"&r&e&oExposed &r&fEffect: Reveals an entity's location and &6&oBreaks &r&fthem.",
		"&r&fIf those affected are &4&lMortio &r&faffiliated, they instantly die."
	 */
	public static void addExposed(LivingEntity target, int seconds) 
	{
		MobData data = MobData.getMob(target.getUniqueId());
		if (data != null) 
		{
			if (EntityCategories.mortio_mobs.contains(target.getType()) && !EntityCategories.calamity.contains(target.getType()))
				data.kill();

			else if (!EntityCategories.mortio_mobs.contains(target.getType()) && !EntityCategories.calamity.contains(target.getType())) 
			{
				if (!data.isBreak()) 
				{
					data.setBreak();
					MobNameplate.update(target);
				}
				EntityEffects.add(target, PotionEffectType.GLOWING, seconds * 20, 0, true);
			}
		}
	}

	public static void addSatiated(LivingEntity target)
	{
		EntityEffects.add(target, PotionEffectType.SATURATION, 60, 2, false);
	}
	
	public static Set<UUID> hasWard = new HashSet<>();
	/**
	 * @param target
	 * @param magnitude
	 * @param seconds
	 * @Description Ward Effect: grants Absorption, Fire Resistance, and Resistance&r&f equal to the magnitude of Ward.
	 * 
	 */
	public static void addWard(Player target, int magnitude, int seconds)
	{
		EntityEffects.add(target, PotionEffectType.ABSORPTION, seconds * 20, magnitude, false);
		EntityEffects.add(target, PotionEffectType.FIRE_RESISTANCE, seconds * 20, 0, false);
		EntityEffects.add(target, PotionEffectType.RESISTANCE, seconds * 20, magnitude, false);
		
		hasWard.add(target.getUniqueId());
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			EntityEffects.playSound(target, Sound.BLOCK_BEACON_DEACTIVATE, SoundCategory.AMBIENT);
			PrintUtils.PrintToActionBar(target, "&e&oWard&r&7&o wore off..");
			hasWard.remove(target.getUniqueId());
		}, seconds * 20);
	}
	
	public static Map<UUID, DivineFavorData> divineFavorRegistry = new HashMap<>();
	public static void addDivineFavor(Player target, int magnitude, int seconds)
	{
		EntityEffects.add(target, PotionEffectType.RESISTANCE, seconds * 20, magnitude, false);
		EntityEffects.add(target, PotionEffectType.ABSORPTION, seconds * 20, magnitude, false);
		
		divineFavorRegistry.put(target.getUniqueId(), new DivineFavorData(magnitude, seconds));
	}
	
	public static Set<UUID> hasEmpowered = new HashSet<>();
	public static void addEmpowered(Player target, int magnitude, int seconds)
	{
		EntityEffects.add(target, PotionEffectType.HASTE, seconds * 20, magnitude, false);
		EntityEffects.add(target, PotionEffectType.STRENGTH, seconds * 20, magnitude, false);
		EntityEffects.add(target, PotionEffectType.GLOWING, seconds * 20, 0, false);
		
		hasEmpowered.add(target.getUniqueId());
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			EntityEffects.playSound(target, Sound.BLOCK_BEACON_DEACTIVATE, SoundCategory.AMBIENT);
			PrintUtils.PrintToActionBar(target, "&e&oEmpowered&r&7&o wore off..");
			hasEmpowered.remove(target.getUniqueId());
		}, seconds * 20);
	}
}
