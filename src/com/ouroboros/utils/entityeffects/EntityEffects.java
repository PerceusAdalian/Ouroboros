package com.ouroboros.utils.entityeffects;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.utils.MobNameplate;

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

}
