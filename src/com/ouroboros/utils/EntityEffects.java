package com.ouroboros.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
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
	
	public static void heal(Player p, double value) 
	{
	    if (p == null || !p.isOnline() || value <= 0) return;
	
	    double maxHealth = p.getAttribute(Attribute.MAX_HEALTH).getValue();
	    
	    p.setHealth(Math.min(p.getHealth() + value, maxHealth));
	}

	public static void playSound(Player source, Location loc, Sound sound, SoundCategory soundCategory, float magnitude, float magnitude2) 
	{
		source.getPlayer().playSound(loc, sound, soundCategory, magnitude, magnitude2);
	}

	public static void playSound(Player source, Sound sound, SoundCategory soundCategory) 
	{
		source.getPlayer().playSound(source.getLocation(), sound, soundCategory, 1, 1);
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
			addBurn(target, 300);
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

	public static void addBurn(LivingEntity target, int seconds) 
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
	 * @Description Aero signature effect. Static causes mobs to emit shockwaves in a 10m radius.  
										   Affected mobs receive 1.25x Aero damage, and Aero-based mobs are healed instead.
				"&r&dStatic &eEffect&f: causes mobs to emit shockwaves in a 10m radius.",
				"&r&fAffected mobs receive &b&o1.25x &r&d&lAero&r&f damage.",
				"&d&lAero&r&f-based mobs are &e&oimmune&r&f, and &a&ohealed&r&f instead."
	 * @param target
	 * @param seconds
	 */
	public static void addStatic(LivingEntity target, Player caster, int seconds)
	{
		hasStatic.put(target.getUniqueId(), true);
		OBStandardTimer.runWithCancel(Ouroboros.instance, (r)->
		{
			if (target.isDead() || (target instanceof Player p && !p.isOnline())) return;
			
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 4, Particle.ELECTRIC_SPARK, null);
			
			RayCastUtils.getNearbyEntities(target, 10, (C)->
			{
				if (!(C instanceof LivingEntity) || C.equals(caster)) return;
				
				OBSParticles.drawLine(target.getLocation(), C.getLocation(), target.getLocation().distance(target.getLocation())%2+1, 0.5, Particle.END_ROD, null);
				
				MobData data = MobData.getMob(C.getUniqueId());
				if (EntityCategories.aero_mobs.contains(C.getType()))
				{
					if (data == null) return;
					data.heal(1, false, false, false);
				}
				
				else Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
						target.getWorld().strikeLightning(C.getLocation()), 20);
			});
		}, 20, seconds*20);
		//Remove after expiry
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasStatic.remove(target.getUniqueId()), seconds*20);
	}
	
	public static void addShock(LivingEntity target, int seconds)
	{
		add(target, PotionEffectType.SLOWNESS, seconds * 20, 99, true);
		add(target, PotionEffectType.GLOWING, seconds * 20, 0, true);
		OBStandardTimer.runWithCancel(Ouroboros.instance, (r)->
		{
			if (target.isDead()) return;
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.ELECTRIC_SPARK, null);
		}, 10, seconds*20);
	}

	public static void addCharged(LivingEntity target, int magnitude, int seconds)
	{
		add(target, PotionEffectType.SPEED, seconds * 20, magnitude, true);
		add(target, PotionEffectType.HASTE, seconds * 20, magnitude, true);
		OBStandardTimer.runWithCancel(Ouroboros.instance, (r)->
		{
			if (target.isDead()) return;
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.DRAGON_BREATH, 1.0f);
		}, 10, seconds*20);
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
			if (EntityCategories.mortio_mobs.contains(target.getType()) && !EntityCategories.calamity.contains(target.getType()))
				data.kill();

			else if (!EntityCategories.mortio_mobs.contains(target.getType()) && !EntityCategories.calamity.contains(target.getType())) 
			{
				if (!data.isBreak()) data.setBreak();
				add(target, PotionEffectType.GLOWING, seconds * 20, 0, true);
			}
		}
	}

	public static Set<UUID> hasDread = new HashSet<>();
	/*
	 * "&r&4Dread &eEffect&f: Applies a debilitation that causes &b&ohunger&r&f and &b&oblindness&r&f",
					"&r&fto those afflicted. Dread is &e&ocurable&r&f and does not stack, however",
					"&r&fsubsequent applications will cause &4Doom&f after a second application."
	 */
	public static void addDread(LivingEntity target, int seconds)
	{
		if (hasDread.contains(target.getUniqueId())) 
		{
			hasDread.remove(target.getUniqueId());
			addDoom(target, 0, seconds * 20);
			return;
		}
		
		hasDread.add(target.getUniqueId());
		add(target, PotionEffectType.HUNGER, seconds * 20, 0, false);
		add(target, PotionEffectType.BLINDNESS, seconds * 20, 0, false);
	}
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
		
		add(target, PotionEffectType.WEAKNESS, seconds * 20, magnitude, false);
		add(target, PotionEffectType.MINING_FATIGUE, seconds * 20, magnitude, false);
		add(target, PotionEffectType.SLOWNESS, seconds * 20, magnitude, false);
		
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
	
	
	public static Map<UUID, Boolean> hasDoom = new HashMap<>();
	/**
	 * @Description Mortio Signature Effect: 
	 * Doom applies a DOT effect equal to it's magnitude. 
	 * Afflicted take 1.25x Mortio damage, and reapplying instantly kills them (NONPVP).
	 * Mortio-based mobs are otherwise unaffected, and healed instead.
	 
	 * @param target    (LivingEntity)
	 * @param magnitude The stacks of doom that equate to the level of Wither (i.e.
	 *                  0 = Wither 1)
	 * @param seconds
	 */
	/*
	 * "&r&4Doom &eEffect&f: Doom applies a &dDOT&f effect equal to it's &b&omagnitude&r&f.",
				"&r&fAfflicted take &b&o1.25x &r&4&lMortio&r&f damage, and reapplying instantly kills them &7(NONPVP)",
				"&r&4&lMortio&r&f-based mobs are otherwise unaffected, and &a&ohealed&r&f instead."
	 */
	public static void addDoom(LivingEntity target, int magnitude, int seconds) 
	{
	    MobData data = MobData.getMob(target.getUniqueId());
	    if (data == null) 
	    {
	        add(target, PotionEffectType.WITHER, seconds * 20, magnitude, false);
	        return;
	    }
	    
	    boolean isMortio = EntityCategories.mortio_mobs.contains(target.getType());
	    UUID uuid = target.getUniqueId();
	    
	    if (hasDoom.containsKey(uuid)) 
	    {
	        hasDoom.remove(uuid);
	        if (!isMortio) data.kill();
	        return;
	    }
	    
	    hasDoom.put(uuid, true);
	    PotionEffectType effectType = isMortio ? PotionEffectType.HUNGER : PotionEffectType.WITHER;
	    int effectMagnitude = isMortio ? 0 : magnitude;
	    add(target, effectType, seconds * 20, effectMagnitude, true);
	    
	    int duration = seconds * 20;
	    OBStandardTimer.runWithCancel(Ouroboros.instance, (r) -> 
	    {
	        if (!target.hasPotionEffect(effectType) || target.isDead()) 
	        {
	            r.cancel();
	            return;
	        }
	        if (isMortio) data.heal(1, false, true, false);
	        OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.SCULK_SOUL, null);
	    }, 20, duration);
	    
	    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> hasDoom.remove(uuid), duration);
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
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 4, Particle.SCULK_SOUL,null);
		}, 20, seconds * 20);
	}
	
	public static void addWard(LivingEntity target, int magnitude, int seconds)
	{
		add(target, PotionEffectType.ABSORPTION, seconds * 20, magnitude, false);
		add(target, PotionEffectType.FIRE_RESISTANCE, seconds * 20, 0, false);
		add(target, PotionEffectType.RESISTANCE, seconds * 20, magnitude, false);
		
	}
	
	/**
	 * @Description Heresio Signature Effect: Wildcard applies a random debuff on the target.
	 */
	public static class WildcardData 
	{
	    public final PotionEffectType effect;
	    public final int magnitude;
	    
	    public WildcardData(PotionEffectType effect, int magnitude) 
	    {
	        this.effect = effect;
	        this.magnitude = magnitude;
	    }
	}
	
	public static Map<UUID, WildcardData> isHexed = new HashMap<>();
	
	public static void addWildcard(LivingEntity target, int magnitude, int seconds)
	{
		PotionEffectType debuff = debuffs.iterator().next();
		target.addPotionEffect(new PotionEffect(debuff, seconds * 20, magnitude, true, true, true));
		isHexed.put(target.getUniqueId(), new WildcardData(debuff, magnitude));
	}
	
	public static void addWildcard(LivingEntity target, int magnitude)
	{
		PotionEffectType debuff = debuffs.iterator().next();
		target.addPotionEffect(new PotionEffect(debuff, PotionEffect.INFINITE_DURATION, magnitude, true, true, true));
		isHexed.put(target.getUniqueId(), new WildcardData(debuff, magnitude));
	}
	
	public static final Set<PotionEffectType> debuffs = Set.of(PotionEffectType.BLINDNESS, PotionEffectType.DARKNESS, PotionEffectType.HUNGER,
			PotionEffectType.INFESTED, PotionEffectType.NAUSEA, PotionEffectType.OOZING, PotionEffectType.POISON, PotionEffectType.RAID_OMEN,
			PotionEffectType.SLOWNESS, PotionEffectType.MINING_FATIGUE, PotionEffectType.TRIAL_OMEN, PotionEffectType.UNLUCK, 
			PotionEffectType.WEAKNESS, PotionEffectType.WITHER, PotionEffectType.WEAVING);
}
