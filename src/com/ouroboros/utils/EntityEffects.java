package com.ouroboros.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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

public class EntityEffects 
{
	public static void add(Player player, PotionEffectType effectType, int duration, int intensity) 
	{
		player.addPotionEffect(new PotionEffect(effectType, duration, intensity));
	}
	
	public static void add(PlayerInteractEvent event, PotionEffectType effectType, int duration, int intensity) 
	{
		event.getPlayer().addPotionEffect(new PotionEffect(effectType, duration, intensity));
	}
	
	public static void add(LivingEntity entity, PotionEffectType effectType, int duration, int intensity, boolean setAmbient) 
	{
		entity.addPotionEffect(new PotionEffect(effectType, duration, intensity, setAmbient));
	}
	
	public static void playSound(Player source, Location loc, Sound sound, SoundCategory soundCategory, float magnitude, float magnitude2) 
	{
		source.getPlayer().playSound(loc, sound,  soundCategory,  magnitude, magnitude2);
	}
	
	public static void checkFromCombat(LivingEntity target, ElementType element)
	{
		MobData data = MobData.getMob(target.getUniqueId());
		if (data == null) return;
		
		//Puncture and Pierce remove an extra 20% of the mob's AR as long as they aren't broken.
		if ((element == ElementType.PUNCTURE || element == ElementType.PIERCE) && (!data.isBreak())) data.damageArmor(data.getArmor(true)*0.2d);
		//Slash damage will deal an additional 50% of the mob's overall health so long as they won't die and are broken.
		if (element == ElementType.SLASH && data.isBreak() && !data.isDead()) data.damage(data.getHp(false)*0.5, false);
		//Blunt damage temporarily slows mobs that aren't yet broken, making it easy to stack stun procs.
		if (element == ElementType.BLUNT && !data.isBreak()) EntityEffects.add(target, PotionEffectType.SLOWNESS, 200, 0, true);
		//Impale will force a BREAK so long as they aren't already broken. This is one of three PURE DAMAGE types.
		if (element == ElementType.IMPALE && !data.isBreak()) data.setBreak();
		//Sever will remove the remainder of the mob's HP after damage calculations so long as they're broken and not already defeated; the second PURE DAMAGE type.
		if (element == ElementType.SEVER && data.isBreak() && !data.isDead()) data.damage(data.getHp(false), false);
		//Crush will indefinitely stun a mob, regardless if they recover from the applied break status; the third and final PURE DAMAGE type.
		if (element == ElementType.CRUSH) 
		{
			EntityEffects.add(target, PotionEffectType.SLOWNESS, PotionEffect.INFINITE_DURATION, 99, true); 
			data.setBreak();
		}
		if (element == ElementType.GLACIO) addChill(target, 300);
		
		
	}
	
	/**
	 * @param target Entity target
	 * @param ticks How many ticks the mob should be frozen and slowed for
	 */
	public static void addChill(LivingEntity target, int ticks)
	{
		add(target, PotionEffectType.SLOWNESS, ticks, 2, true);
		target.setFreezeTicks(Math.min(target.getMaxFreezeTicks(), target.getFreezeTicks() + ticks));
	}
	
	public static void setBurn(LivingEntity target, int ticks)
	{
		add(target, PotionEffectType.HUNGER, ticks, 2, true);
		target.setFireTicks(ticks);
	}
	
	public static void addSanded(LivingEntity target, int ticks)
	{
		add(target, PotionEffectType.SLOWNESS, ticks, 1, false);
		add(target, PotionEffectType.BLINDNESS, ticks, 2, false);
	}
	
	private static Map<Entity, Boolean> duplicateEffectCheck = new HashMap<>();
	public static void addSuffocate(LivingEntity target, int ticks)
	{
		
		if (duplicateEffectCheck.containsKey(target)) return;
		duplicateEffectCheck.put(target, true);
		new BukkitRunnable() 
		{
		    int time = ticks;
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
	 * @Description Celestio Signature Effect: Exposed will kill any undead mob if it's not a boss.
	 * If it's not an undead enemy, Exposed will BREAK any mob so long as it's also not a boss.
	 * Finally, Exposed also adds the "Glowing" effect.
	 */
	public static void addExposed(LivingEntity target, int ticks)
	{
		MobData data = MobData.getMob(target.getUniqueId());
		if (data != null) 
		{
			if (EntityCategories.undead.contains(target.getType()) 
					&& !EntityCategories.calamity.contains(target.getType())) 
			{
				MobData.damageUnnaturally(null, target, data.getHp(false), false);
			}
			else if (!EntityCategories.undead.contains(target.getType()) 
						&& !EntityCategories.calamity.contains(target.getType()) 
							&& !data.isBreak())
			{
				data.setBreak();
			}
			
		}
		add(target, PotionEffectType.GLOWING, ticks, 0, true);
		
	}
}
