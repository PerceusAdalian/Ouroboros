package com.ouroboros.utils;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
	
	public static void add(Entity entity, PotionEffectType effectType, int duration, int intensity) 
	{
		if (entity instanceof LivingEntity) 
		{
			((LivingEntity) entity).addPotionEffect(new PotionEffect(effectType, duration, intensity));
		}
	}
	
	public static void playSound(Player source, Location loc, Sound sound, SoundCategory soundCategory, float magnitude, float magnitude2) 
	{
		source.getPlayer().playSound(loc, sound,  soundCategory,  magnitude, magnitude2);
	}
}
