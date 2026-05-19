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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.MobNameplate;
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
		    data.damageArmor(5, ElementType.PURE);
		    MobNameplate.update(target);
		    data.save();

		}, 20, seconds * 20);
		
	}
	
	public static void playSound(Player source, Location loc, Sound sound, SoundCategory soundCategory, float magnitude, float magnitude2) 
	{
		source.getPlayer().playSound(loc, sound, soundCategory, magnitude, magnitude2);
	}

	public static void playSound(Player source, Sound sound, SoundCategory soundCategory) 
	{
		source.getPlayer().playSound(source.getLocation(), sound, soundCategory, 1, 1);
	}
	
	public static void rushEntity(Player source, LivingEntity target, double magnitude)
	{
		Vector v1 = target.getLocation().toVector();
		Vector v2 = source.getLocation().toVector();
		source.setVelocity(v1.subtract(v2).normalize().multiply(magnitude));
	}
	
	public static double resolveEffectModifiedDamage(Player p, ElementType element, double dmg, DamageCause cause)
	{
	    double modifiedDamage = dmg;

	    // Fire Resistance: nullify all fire-typed damage
	    if (element == ElementType.INFERNO && p.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE))
	        return -1;

	    // Resistance: 20% reduction per level (level 5 = full immunity)
	    if (p.hasPotionEffect(PotionEffectType.RESISTANCE))
	    {
	        int level = p.getPotionEffect(PotionEffectType.RESISTANCE).getAmplifier() + 1;
	        modifiedDamage *= Math.max(0, 1.0 - (0.2 * level));
	        if (modifiedDamage <= 0) return -1;
	    }

	    // Slow Falling: negate all fall damage
	    if (cause == DamageCause.FALL && p.hasPotionEffect(PotionEffectType.SLOW_FALLING))
	        return -1;

	    // Conduit Power: negate drowning damage
	    if (cause == DamageCause.DROWNING && p.hasPotionEffect(PotionEffectType.CONDUIT_POWER))
	        return -1;

	    // --- Absorption: consumed as a buffer before armor/HP ---
	    if (p.getAbsorptionAmount() > 0)
	    {
	        double absorb = p.getAbsorptionAmount();
	        if (absorb >= modifiedDamage)
	        {
	            p.setAbsorptionAmount(absorb - modifiedDamage);
	            return -1;
	        }
	        else
	        {
	            modifiedDamage -= absorb;
	            p.setAbsorptionAmount(0);
	        }
	    }

	    return modifiedDamage;
	}
}
