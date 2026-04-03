package com.ouroboros.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.utils.MobNameplate;

public class EntityEffects 
{
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
		MobData data = MobData.getMob(target.getUniqueId());
		if (data == null) return;

		// Puncture and Pierce remove an extra 20% of the mob's AR as long as they
		// aren't broken.
		if ((element == ElementType.PUNCTURE || element == ElementType.PIERCE) && !data.isBreak())
			data.damageArmor(data.getArmor(false) * 0.2d);
		
		// Slash damage will deal an additional 50% of the mob's overall health so long
		// as they won't die and are broken.
		if (element == ElementType.SLASH && data.isBreak())
			data.damage(data.getHp(false) * 0.5, false, ElementType.PURE);
		
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
		if (element == ElementType.SEVER && data.isBreak())
			data.damage(data.getHp(false), false, ElementType.PURE);
		
		// Crush will indefinitely stun a mob, regardless if they recover from the
		// applied break status; the third and final PURE DAMAGE type.
		if (element == ElementType.CRUSH) 
		{
			EntityEffects.add(target, PotionEffectType.SLOWNESS, PotionEffect.INFINITE_DURATION, 99, true);
			data.setBreak(true);
			data.setArmor(0, false);
		}
		
		// Corrosive will erode target's armor by 30% of its current value.
		if (element == ElementType.CORROSIVE && !data.isBreak())
			data.damageArmor(data.getArmor(false) * 0.3d);
		
		MobNameplate.update(target);
	}

	public static Set<UUID> hasEtherOverload = new HashSet<>();
	public static void addEtherOverload(LivingEntity target, int seconds)
	{
		if (hasEtherOverload.contains(target.getUniqueId())) return;
		OBStandardTimer.runWithCancel(Ouroboros.instance, e->
		{
			if (target.isDead()) return;
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 3, Particle.GLOW_SQUID_INK, null);
		}, 20, seconds*20);
		hasEtherOverload.add(target.getUniqueId());
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasEtherOverload.remove(target.getUniqueId()), seconds * 20);
	}
	
	public static Set<UUID> hasEtherDisruption = new HashSet<>();
	public static void addEtherDisruption(Player target, int seconds)
	{
		if (hasEtherDisruption.contains(target.getUniqueId())) return;
		OBStandardTimer.runWithCancel(Ouroboros.instance, e->
		{
			if (target.isDead()) return;
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 3, Particle.GLOW_SQUID_INK, null);
			OBSParticles.drawDisc(target.getLocation(), target.getWidth(), 3, 4, 0.1, Particle.BUBBLE_COLUMN_UP, null);
		}, 20, seconds*20);
		hasEtherDisruption.add(target.getUniqueId());
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasEtherDisruption.remove(target.getUniqueId()), seconds * 20);
	}
	
	public static void addChroma(LivingEntity target, int seconds)
	{
		// TODO
	}
	
	public static Set<UUID> hasHumility = new HashSet<>();
	public static void addHumility(LivingEntity target, int seconds)
	{
		if (EntityCategories.celestio_mobs.contains(target.getType())) return;
		if (hasHumility.contains(target.getUniqueId())) return;
		hasHumility.add(target.getUniqueId());
		OBSParticles.drawCelestioCastSigil(target);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> hasHumility.remove(target.getUniqueId()), seconds * 20);
	}
	
	private static Map<UUID, BukkitTask> chillTimers = new HashMap<>();
	public static Map<UUID, Integer> hasChill = new HashMap<>();
	/*
	 * Chill Effect: those affected are inflicted with a Glacio DOT effect. Reapplying Chill increases the magnitude, keeping initial duration.
	 */
	/**
	 * 
	 * @param target
	 * @param magnitude
	 * @param seconds
	 */
	public static void addChill(LivingEntity target, int magnitude, int seconds) 
	{
	    UUID id = target.getUniqueId();
	    int newMagnitude = hasChill.getOrDefault(id, 0) + magnitude;
	    if (newMagnitude > 20) newMagnitude = 20;
	    int ticks = seconds * 20;

	    // Cancel existing expiry task if re-applying
	    BukkitTask existing = chillTimers.get(id);
	    if (existing != null) existing.cancel();

	    // Apply or re-apply effects
	    target.removePotionEffect(PotionEffectType.SLOWNESS);
	    add(target, PotionEffectType.SLOWNESS, ticks, newMagnitude, true);
	    target.setFreezeTicks(Math.min(target.getMaxFreezeTicks(), target.getFreezeTicks() + ticks));
	    hasChill.put(id, newMagnitude);

	    // Schedule cleanup
	    BukkitTask task = Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
	    {
	        chillTimers.remove(id);
	        hasChill.remove(id);
	    }, ticks);

	    chillTimers.put(id, task);
	}
	
	public static Set<UUID> hasFrosted = new HashSet<>();
	public static void addFrosted(LivingEntity target, int magnitude, int seconds)
	{
		if (hasFrosted.contains(target.getUniqueId())) return;
		hasFrosted.add(target.getUniqueId());
		add(target, PotionEffectType.WEAKNESS, seconds * 20, magnitude, false);
		add(target, PotionEffectType.SLOWNESS, seconds * 20, magnitude, false);
		OBStandardTimer.runWithCancel(Ouroboros.instance, e ->
		{
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.SNOWFLAKE, null);
		}, 20, seconds * 20);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasFrosted.remove(target.getUniqueId()), seconds * 20);
	}
	
	public static Set<UUID> hasFrozen = new HashSet<>();
	public static void addFrozen(LivingEntity target)
	{
		if (hasChill.containsKey(target.getUniqueId()))
		{
			int magnitude = hasChill.get(target.getUniqueId());
			hasChill.remove(target.getUniqueId());
			chillTimers.get(target.getUniqueId()).cancel();
			add(target, PotionEffectType.SLOWNESS, PotionEffect.INFINITE_DURATION, 99, true);
			add(target, PotionEffectType.WEAKNESS, PotionEffect.INFINITE_DURATION, magnitude, true);
			if (!(target instanceof Player))
			{
				((Mob) target).setTarget(null);
				target.setAI(false);
			}
		}
	}

	public static void addBurn(LivingEntity target, int seconds) 
	{
		target.setFireTicks(seconds * 20);
	}
	
	public static Set<UUID> hasCharred = new HashSet<>();
	/**
	 * @Effect Causes those affected to have Hunger, Fatigue, and Slowness, while taking 25% more Inferno Damage.
	 * 			20% chance to cause burn while charred.
	 * @param target
	 * @param seconds
	 */
	public static void addCharred(LivingEntity target, int seconds)
	{
		target.setFireTicks(seconds * 20);
		add(target, PotionEffectType.HUNGER, seconds*20, 0, false);
		add(target, PotionEffectType.MINING_FATIGUE, seconds*20, 0, false);
		add(target, PotionEffectType.SLOWNESS, seconds*20, 0, false);
		hasCharred.add(target.getUniqueId());
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasCharred.remove(target.getUniqueId()), seconds*20);
	}
	
	public static void addSanded(LivingEntity target, int seconds)
	{
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
				add(target, PotionEffectType.GLOWING, seconds * 20, 0, true);
			}
		}
	}

	public static void addSatiated(LivingEntity target)
	{
		EntityEffects.add(target, PotionEffectType.SATURATION, 60, 2, false);
	}
	
	public static Set<UUID> hasDread = new HashSet<>();
	/*
	 * "&r&4Dread &eEffect&f: Applies a debilitation that causes &b&ohunger&r&f and &b&oblindness&r&f",
					"&r&fto those afflicted. Dread is &e&ocurable&r&f and does not stack, however",
					"&r&fsubsequent applications will cause &4Doom&f after a second application.",
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
	
	public static Map<UUID, Integer> nightShifted = new HashMap<>();
	/*
	  "&r&4Night-Shift &eEffect&f: Increased &b&oSpeed&r&f and &b&oStrength&r&f",
		"&r&fper level of &4Night-Shift&f, plus &b&oNight Vision&r&f.",
		"&r&fReduces &b&oFall Damage &r&fby &b&o10% &r&fper level.",
		"&r&fIf it's nighttime, Night-Shift's &b&omagnitude&r&f is doubled."
	 */
	/**
	 * @param target
	 * @param magnitude values of >=1 accepted.
	 * @param seconds
	 */
	public static void addNightShift(Player target, int magnitude, int seconds)
	{
		if (nightShifted.containsKey(target.getUniqueId())) return;
		
		nightShifted.put(target.getUniqueId(), magnitude);
		
		OBStandardTimer.runWithCancel(Ouroboros.instance, (e)->
		{
			if (!((Player) target).isOnline()) e.cancel();
			
			long time = target.getWorld().getTime();
			boolean isNight = time >= 13000 && time <= 23000;
			int amp = isNight ? (magnitude * 2) - 1 : magnitude - 1;

			add(target, PotionEffectType.SPEED, 40, amp, false);
			add(target, PotionEffectType.STRENGTH, 40, amp, false);
			add(target, PotionEffectType.NIGHT_VISION, 300, 0, false);

			nightShifted.put(target.getUniqueId(), amp);
		}, 20, seconds * 20);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
		{
			if (target.isOnline()) 
			{	
				PrintUtils.PrintToActionBar(target, "&4&oNight-Shift ends..");
				EntityEffects.playSound(target, Sound.BLOCK_CHAIN_BREAK, SoundCategory.AMBIENT);
			}
			nightShifted.remove(target.getUniqueId());
		}, seconds * 20);
	}
	
	public static void addShroud(Player target, int magnitude, int seconds)
	{
		add(target, PotionEffectType.SPEED, 20 * seconds, magnitude);
		add(target, PotionEffectType.JUMP_BOOST, 20 * seconds, magnitude);
		add(target, PotionEffectType.INVISIBILITY, 20 * seconds, magnitude);
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
	 * @Description Cosmo Signature Effect: Voided strips the afflicted entity of all",
				"Resistances and Immunities for the duration."
	 * @param target The target to set as voided.
	 * @param ticks  The duration to add the entity target into the isVoided
	 *               registry.
	 */
	public static Map<UUID, Boolean> isVoidedRegistry = new HashMap<>();

	public static void addVoided(LivingEntity target, int seconds) 
	{
		if (isVoidedRegistry.containsKey(target.getUniqueId()))return;
		isVoidedRegistry.put(target.getUniqueId(), true);
		
		OBSParticles.drawCosmoCastSigil(target);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> isVoidedRegistry.remove(target.getUniqueId()), seconds * 20);
		OBStandardTimer.runWithCancel(Ouroboros.instance, (r) -> 
		{
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 4, Particle.SCULK_SOUL,null);
		}, 20, seconds * 20);
	}
	
	/**
	 * @param target
	 * @param magnitude
	 * @param seconds
	 * @Description Ward Effect: grants Absorption, Fire Resistance, and Resistance&r&f equal to the magnitude of Ward.
	 * 
	 */
	public static void addWard(LivingEntity target, int magnitude, int seconds)
	{
		add(target, PotionEffectType.ABSORPTION, seconds * 20, magnitude, false);
		add(target, PotionEffectType.FIRE_RESISTANCE, seconds * 20, 0, false);
		add(target, PotionEffectType.RESISTANCE, seconds * 20, magnitude, false);
	}
	
	public static Map<UUID, DivineFavorData> divineFavorRegistry = new HashMap<>();
	public static void addDivineFavor(Player target, int magnitude, int seconds)
	{
		add(target, PotionEffectType.RESISTANCE, seconds * 20, magnitude, false);
		add(target, PotionEffectType.ABSORPTION, seconds * 20, magnitude, false);
		
		divineFavorRegistry.put(target.getUniqueId(), new DivineFavorData(magnitude, seconds));
	}
	
	public static class DivineFavorData
	{
		public int magnitude;
		public int seconds;
		
		public DivineFavorData(int magnitude, int seconds)
		{
			this.magnitude = magnitude;
			this.seconds = seconds;
		}
	}
	
	public static Map<UUID, JinxData> jinxRegistry = new HashMap<>();
	public static void addJinxStacks(Player target, int magnitude)
	{
		int registerMagnitude = magnitude;
		if (jinxRegistry.containsKey(target.getUniqueId()))
		{
			registerMagnitude = jinxRegistry.get(target.getUniqueId()).magnitude + magnitude;
		}
		
		OBSParticles.drawMortioCastSigil(target);
		jinxRegistry.put(target.getUniqueId(), new JinxData(registerMagnitude));
	}
	
	public static class JinxData
	{
		public int magnitude;
		
		public JinxData(int magnitude)
		{
			this.magnitude = magnitude;
		}
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
	
	/**
	 * @param target
	 * @param magnitude
	 * @param seconds
	 */
	public static void addWildcard(LivingEntity target, int magnitude, int seconds)
	{
		PotionEffectType debuff = debuffs.iterator().next();
		target.addPotionEffect(new PotionEffect(debuff, seconds * 20, magnitude, true, true, true));
		isHexed.put(target.getUniqueId(), new WildcardData(debuff, magnitude));
	}
	
	/**
	 * @param target
	 * @param magnitude
	 * @Documented assumed duration is Infinite
	 */
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
