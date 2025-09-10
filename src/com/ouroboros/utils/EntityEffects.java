package com.ouroboros.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.MobData;

public class EntityEffects {
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
	 * @param intensity
	 * @param setAmbient
	 */
	public static void add(LivingEntity entity, PotionEffectType effectType, int duration, int intensity, boolean setAmbient) 
	{
		entity.addPotionEffect(new PotionEffect(effectType, duration, intensity, setAmbient));
	}

	public static void playSound(Player source, Location loc, Sound sound, SoundCategory soundCategory, float magnitude, float magnitude2) 
	{
		source.getPlayer().playSound(loc, sound, soundCategory, magnitude, magnitude2);
	}

	public static void checkFromCombat(LivingEntity target, ElementType element) 
	{

		Random r = new Random();

		MobData data = MobData.getMob(target.getUniqueId());
		if (data == null) return;

		// Puncture and Pierce remove an extra 20% of the mob's AR as long as they
		// aren't broken.
		if ((element == ElementType.PUNCTURE || element == ElementType.PIERCE) && (!data.isBreak()))
			data.damageArmor(data.getArmor(false) * 0.2d);
		// Slash damage will deal an additional 50% of the mob's overall health so long
		// as they won't die and are broken.
		if (element == ElementType.SLASH && data.isBreak() && !data.isDead())
			data.damage(data.getHp(false) * 0.5, false, ElementType.NEUTRAL);
		// Blunt damage temporarily slows mobs that aren't yet broken, making it easy to
		// stack stun procs.
		if (element == ElementType.BLUNT && !data.isBreak())
			EntityEffects.add(target, PotionEffectType.SLOWNESS, 200, 0, true);
		// Impale will force a BREAK so long as they aren't already broken. This is one
		// of three PURE DAMAGE types.
		if (element == ElementType.IMPALE && !data.isBreak())
			data.setBreak();
		// Sever will remove the remainder of the mob's HP after damage calculations so
		// long as they're broken and not already defeated; the second PURE DAMAGE type.
		if (element == ElementType.SEVER && data.isBreak() && !data.isDead())
			data.damage(data.getHp(false), false, ElementType.NEUTRAL);
		// Crush will indefinitely stun a mob, regardless if they recover from the
		// applied break status; the third and final PURE DAMAGE type.
		if (element == ElementType.CRUSH) 
		{
			EntityEffects.add(target, PotionEffectType.SLOWNESS, PotionEffect.INFINITE_DURATION, 99, true);
			data.setBreak();
		}
		// Corrosive will erode target's armor by 30% of its current value.
		if (element == ElementType.CORROSIVE && !data.isBreak())
			data.damageArmor(data.getArmor(false) * 0.3d);
		if (element == ElementType.GLACIO) 
		{
			if (r.nextDouble() >= 0.2155)
				return;
			addChill(target, 300);
		}
		if (element == ElementType.INFERNO) 
		{
			if (r.nextDouble() >= 0.1399)
				return;
			setBurn(target, 300);
		}

	}

	/**
	 * @param target Entity target
	 * @param ticks  How many ticks the mob should be frozen and slowed for
	 */
	public static void addChill(LivingEntity target, int seconds) 
	{
		add(target, PotionEffectType.SLOWNESS, seconds*20, 2, true);
		target.setFreezeTicks(Math.min(target.getMaxFreezeTicks(), target.getFreezeTicks() + seconds * 20));
	}

	public static void setBurn(LivingEntity target, int seconds) 
	{
		add(target, PotionEffectType.HUNGER, seconds*20, 2, true);
		target.setFireTicks(seconds * 20);
	}

	public static void addSanded(LivingEntity target, int seconds)
	{
		if (EntityCategories.geo_mobs.contains(target.getType()))
		{
			add(target, PotionEffectType.SPEED, seconds*20, 2, true);
			MobData data = MobData.getMob(target.getUniqueId());
			if (data == null)
			{
				try
				{
					target.setHealth(target.getHealth()+seconds);
				} 
				catch (Error e)
				{
					target.setHealth(((Attributable) target).getAttribute(Attribute.MAX_HEALTH).getValue());
				}
			}
			else
			{
				data.heal(seconds%10, false, true, false);
			}
		}
		add(target, PotionEffectType.SLOWNESS, seconds*20, 4, false);
		add(target, PotionEffectType.BLINDNESS, seconds*20, 4, false);
	}

	
	public static Map<UUID, Boolean> hasStatic = new HashMap<>();
	/**
	 * @Description Aero signature effect. Static causes mobs (except Aero, which are healed) to emit shockwaves in a 10x10 radius.  
										   Affected mobs take 1.25x Aero damage (see damage event).

	 * @param target
	 * @param seconds
	 */
	public static void addStatic(LivingEntity target, int seconds)
	{
		hasStatic.put(target.getUniqueId(), true);
		OBStandardTimer.runWithCancel(Ouroboros.instance, (r)->
		{
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 4, Particle.END_ROD, null);
			if(target.getNearbyEntities(10, 10, 10).isEmpty()) return;
			for (Entity eTarget : target.getNearbyEntities(10, 10, 10))
			{
				if (!(eTarget instanceof LivingEntity)) continue;
				
				OBSParticles.drawLine(target.getLocation(), eTarget.getLocation(), target.getLocation().distance(target.getLocation())%2+1, 0.5, Particle.END_ROD, null);
				
				MobData data = MobData.getMob(eTarget.getUniqueId());
				if (EntityCategories.aero_mobs.contains(eTarget.getType()))
				{
					if (data == null) continue;
					data.heal(1, false, true, false);
				}
				
				else Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->target.getWorld().strikeLightning(eTarget.getLocation()), 20);
			}
		}, 20, seconds*20);
		//Remove after expiry
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasStatic.remove(target.getUniqueId()), seconds*20);
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

	/**
	 * @Description Celestio Signature Effect: Exposed will kill any undead mob if
	 *              it's not a boss.
	 *              If it's not an undead enemy, Exposed will BREAK any mob so long
	 *              as it's also not a boss.
	 *              Finally, Exposed also adds the "Glowing" effect.
	 */
	public static void addExposed(LivingEntity target, int seconds) 
	{
		MobData data = MobData.getMob(target.getUniqueId());
		if (data != null) 
		{
			if (EntityCategories.undead.contains(target.getType()) && !EntityCategories.calamity.contains(target.getType()))
				data.kill();

			else if (!EntityCategories.undead.contains(target.getType())
					&& !EntityCategories.calamity.contains(target.getType())
						&& !data.isBreak()) 
			{
				data.setBreak();
				add(target, PotionEffectType.GLOWING, seconds * 20, 0, true);
			}
		}
	}

	public static Map<UUID, Boolean> hasDoom = new HashMap<>();
	/**
	 * @Description Mortio Signature Effect: Doom regenerates mortio-based mobs (undead, etc.) and marks celestio-based mobs (non-boss).  
											 Marked mobs take 1.25x mortio damage, and reapplying Doom instantly kills them.
											 Otherwise, Doom inflicts wither equal to its magnitude. Mortio mobs are healed instead.
	 * @param target    (LivingEntity)
	 * @param magnitude The stacks of doom that equate to the level of Wither (i.e.
	 *                  0 = Wither 1)
	 * @param seconds
	 */
	public static void addDoom(LivingEntity target, int magnitude, int seconds) 
	{
		MobData data = MobData.getMob(target.getUniqueId());
		if (data != null) 
		{
			if (EntityCategories.mortio_mobs.contains(target.getType())) 
			{
				add(target, PotionEffectType.HUNGER, seconds * 20, 0, true);
				OBStandardTimer.runWithCancel(Ouroboros.instance, (r) -> 
				{
					data.heal(1, false, true, false);
				}, 20, seconds * 20);
				hasDoom.put(target.getUniqueId(), true);
				OBStandardTimer.runWithCancel(Ouroboros.instance, (r) -> 
				{
					OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5,Particle.SCULK_SOUL, null);
				}, 20, seconds * 20);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance,() -> hasDoom.remove(target.getUniqueId()), seconds * 20);
			} 
			else if (EntityCategories.celestio_mobs.contains(target.getType()) && 
						!EntityCategories.calamity.contains(target.getType()) &&
							!hasDoom.containsKey(target.getUniqueId())) 
			{
				hasDoom.put(target.getUniqueId(), true);
				add(target, PotionEffectType.WITHER, seconds * 20, magnitude, true);
				OBStandardTimer.runWithCancel(Ouroboros.instance, (r) -> 
				{
					OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5,Particle.SCULK_SOUL, null);
				}, 20, seconds * 20);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance,() -> hasDoom.remove(target.getUniqueId()), seconds * 20);
			} 
			else if (hasDoom.containsKey(target.getUniqueId()) && !EntityCategories.mortio_mobs.contains(target.getType())) 
			{
				hasDoom.remove(target.getUniqueId());
				data.kill();
			} 
		} 
		else add(target, PotionEffectType.WITHER, seconds * 20, magnitude, false); // Not an OBS mob (legacy case) OR Is not a Mortio/Celestio mob.
	}

	/**
	 * @Description Cosmo Signature Effect: Voided will remove any mob's immunities
	 *              and resistances, and make all damage neutral within the set
	 *              duration.
	 * @param target The target to set as voided.
	 * @param ticks  The duration to add the entity target into the isVoided
	 *               registry.
	 */
	public static Map<UUID, Boolean> isVoidedRegistry = new HashMap<>();

	public static void addVoided(LivingEntity target, int seconds) 
	{
		if (isVoidedRegistry.containsKey(target.getUniqueId()))
			return;
		isVoidedRegistry.put(target.getUniqueId(), true);
		add(target, PotionEffectType.GLOWING, seconds * 20, 0, true);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> isVoidedRegistry.remove(target.getUniqueId()), seconds * 20);
		OBStandardTimer.runWithCancel(Ouroboros.instance, (r) -> 
		{
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 4, Particle.END_ROD,null);
		}, 20, seconds * 20);
	}
}
