package com.ouroboros.utils.entityeffects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.utils.MobNameplate;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.ObsTimer;

public class EntityEffects 
{
	public static final Set<PotionEffectType> debuffs = Set.of(PotionEffectType.BLINDNESS, PotionEffectType.DARKNESS, PotionEffectType.HUNGER,
			PotionEffectType.INFESTED, PotionEffectType.NAUSEA, PotionEffectType.OOZING, PotionEffectType.POISON, PotionEffectType.RAID_OMEN,
			PotionEffectType.SLOWNESS, PotionEffectType.MINING_FATIGUE, PotionEffectType.TRIAL_OMEN, PotionEffectType.UNLUCK, 
			PotionEffectType.WEAKNESS, PotionEffectType.WITHER, PotionEffectType.WEAVING);
	
	/**
	 * @param player
	 * @param effectType
	 * @param duration
	 * @param intensity
	 */
	public static void add(Player player, PotionEffectType effectType, int duration, int intensity) 
	{
		player.addPotionEffect(new PotionEffect(effectType, duration, intensity));
	}

	/**
	 * @param event
	 * @param effectType
	 * @param duration
	 * @param intensity
	 */
	public static void add(PlayerInteractEvent event, PotionEffectType effectType, int duration, int intensity) 
	{
		event.getPlayer().addPotionEffect(new PotionEffect(effectType, duration, intensity));
	}

	/**
	 * @param entity
	 * @param effectType
	 * @param duration
	 * @param magnitude
	 * @param setAmbient
	 */
	public static void add(LivingEntity entity, PotionEffectType effectType, int duration, int magnitude, boolean setAmbient) 
	{
		entity.addPotionEffect(new PotionEffect(effectType, duration, magnitude, setAmbient));
	}
	
	public static Set<UUID> hasErosion = new HashSet<>();
	public static void addErosion(LivingEntity entity, int seconds)
	{
		if (hasErosion.contains(entity.getUniqueId())) return;
		hasErosion.add(entity.getUniqueId());
		
		
		ObsTimer.runWithCancel(Ouroboros.instance, (r) ->
		{
		    LivingEntity target = (LivingEntity) Bukkit.getEntity(entity.getUniqueId());
		    MobData data = MobData.getMob(entity.getUniqueId());
		    
		    if (target == null || data == null || target.isDead() || data.isBreak())
		    {
		    	hasErosion.remove(entity.getUniqueId());
		        r.cancel();
		        return;
		    }

		    ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.BLOCK_CRUMBLE, Material.EMERALD_BLOCK.createBlockData());
		    data.damageArmor(5, ElementType.CORROSIVE);
		    MobNameplate.update(target);
		    data.save();

		}, 20, seconds * 20);
		
	}
	
	public static void heal(Player p, double value) 
	{
	    if (p == null || !p.isOnline() || value <= 0) return;
	
	    double maxHealth = p.getAttribute(Attribute.MAX_HEALTH).getValue();
	    
	    p.setHealth(Math.min(p.getHealth() + value, maxHealth));
	}
	
	public static void heal(Player p)
	{
		if (p == null || !p.isOnline()) return;
		p.setHealth(p.getAttribute(Attribute.MAX_HEALTH).getValue());	
	}
	
	public static void playSound(Player source, Location loc, Sound sound, SoundCategory soundCategory, float magnitude, float magnitude2) 
	{
		source.getPlayer().playSound(loc, sound, soundCategory, magnitude, magnitude2);
	}

	public static void playSound(Player source, Sound sound, SoundCategory soundCategory) 
	{
		source.getPlayer().playSound(source.getLocation(), sound, soundCategory, 1, 1);
	}
}
