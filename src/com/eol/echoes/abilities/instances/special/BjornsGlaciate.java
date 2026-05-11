package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.EchoData;
import com.eol.echoes.EchoManager;
import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.BiomeUtils;
import com.ouroboros.utils.BiomeUtils.BiomeTemperatureCategory;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GlacioEffects;

public class BjornsGlaciate extends EchoAbility
{

	public BjornsGlaciate() 
	{
		super("Bjorn's Glaciate", "bjorn_glaciate", Material.NETHER_STAR, StatType.MELEE, 0, 0, 50, AbilityType.SPECIALABILITY, ElementType.GLACIO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.HATCHET, 
				"&r&fIn &d&oWarm Biomes&r&f, reduce &b&oDurability Cost&r&f to &l10&r&f.",
				"&r&fInflict &b&o120%&r&f of &oBase Attack&r&f as "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f damage",
				"&r&fas a &d&oRadial AOE&r&f inflicting &bChill II&f to all hit &7(25m, 15s)","",
				"&r&bChill &eEffect&f: &b&oSlows&r&f while inflicting a "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f DOT effect.",
				"&r&fReapplying &bChill&f increases the &b&omagnitude&r&f, while keeping initial duration.");
	}

	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		EchoData stats = EchoManager.getEchoData(e.getItem());
		if (stats == null) return -1;
		
		BiomeTemperatureCategory temp = BiomeUtils.getTempCategory(p);
		int finalDurabilityCost = 50;
		if (temp == BiomeUtils.BiomeTemperatureCategory.HOT || temp == BiomeUtils.BiomeTemperatureCategory.TEMPERATE) 
			finalDurabilityCost = 10;
		
		double damage = stats.getAttack() * 1.2;
		
		if (!RayCastUtils.getNearbyEntities(p, 25, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;

			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER);
			ObsParticles.drawDisc(p.getLocation(), 10, 3, 10, 0.3, Particle.SWEEP_ATTACK, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{				
				EntityEffects.playSound(p, Sound.BLOCK_GLASS_BREAK, SoundCategory.AMBIENT);
				EntityEffects.playSound(p, Sound.BLOCK_POWDER_SNOW_PLACE, SoundCategory.MASTER);
				ObsParticles.drawGlacioCastSigil(le);
				ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 25, 0.75, 30, 0.2, Particle.SNOWFLAKE, null);
				MobData.damageUnnaturally(p, le, damage, true, true, ElementType.GLACIO);
				GlacioEffects.addChill(p, le, 2, 15);
			}, 10);
		})) return -1;
		
		
		return finalDurabilityCost;
	}

	@Override
	public int getFinalDurabilityCost() 
	{
		
		return 50;
	}

}
