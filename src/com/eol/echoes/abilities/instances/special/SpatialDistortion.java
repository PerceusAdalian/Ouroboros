package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.EchoData;
import com.eol.echoes.EchoManager;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.CosmoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class SpatialDistortion extends EchoAbility
{

	public SpatialDistortion()
	{
		super("Spatial Distortion", "spatial_distortion", Material.NETHER_STAR, StatType.RANGED, 0, 0, 0, AbilityType.SPECIALABILITY, ElementType.COSMO,
				CastConditions.MIXED, EchoForm.BOW, 
				"&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.LEFT_CLICK_AIR),
				"&r&3Spatial Distortion&f: &3&oWarp Arrow&r&f -- &cRemove &f5 &b&oDurability",
				"&r&fLaunch an arrow that &3Teleports&f you to its landing location.","",
				"&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_LEFT_CLICK_AIR),
				"&r&3Spatial Distortion&f: &3&oCataclysmic Shot&r&f -- &cRemove &f15 &b&oDurability",
				"&r&fCreate a &3Cataclysm&f at &6target &dMob &fdealing "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f damage",
				"&r&fequal to &f&oBase Attack &r&fx &oCrit Modifier&r&f + &oCrit Rating inflicting &3Voided",
				"&r&7(&6"+Symbols.TARGET+"&7:&650m&7, &dRadius&7: &d25m&7, &bEffect Duration&7: &b15s&7)","",
				"&r&3Voided &eEffect&f: Neutralizes elemental affinity.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_LEFT_CLICK_AIR))
		{
			Entity target = RayCastUtils.getEntity(p, 50);
			if (target == null || !(target instanceof LivingEntity) || target instanceof Player) return -1;
			
			EchoData data = EchoManager.getEchoData(e.getItem());
			double damage = (1 + data.getCritRate()) * (data.getAttack() * data.getCritModifier());
			
			EntityEffects.playSound(p, Sound.ENTITY_SHULKER_SHOOT, SoundCategory.AMBIENT);
			double theta = ObsParticles.deriveDegreeTheta(p.getLocation(), target.getLocation());
			ObsParticles.drawAngledArcLine(p.getLocation(), target.getLocation(), 0.7, 8, theta, 0.3, Particle.END_ROD, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), target.getLocation(), 0.9, 9, theta, 0.5, Particle.CLOUD, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.AMBIENT);
				ObsParticles.drawLandingWave((LivingEntity) target);
				ObsParticles.drawCosmoCastSigil((LivingEntity) target);
				ObsParticles.drawWave(Ouroboros.instance, target.getLocation(), 20, .95, 50, 0.3, Particle.END_ROD, null);
				ObsParticles.drawWave(Ouroboros.instance, target.getLocation(), 20, .95, 50, 0.3, Particle.DRAGON_BREATH, 1.0f);
				ObsParticles.drawWave(Ouroboros.instance, target.getLocation(), 25, .85, 50, 0.3, Particle.CLOUD, null);
				MobData.damageUnnaturally(p, target, damage, true, true, ElementType.COSMO, null);
				CosmoEffects.addVoided((LivingEntity) target, 15);
			}, 7);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT);
				RayCastUtils.getNearbyEntities(target, 25, nearby ->
				{
					if (nearby == null || !(nearby instanceof LivingEntity le) || nearby instanceof Player) return;
					ObsParticles.drawWisps(le.getLocation(), le.getWidth(), le.getHeight(), 1, Particle.EXPLOSION_EMITTER, null);
					MobData.damageUnnaturally(p, le, damage, true, true, ElementType.COSMO, null);
					CosmoEffects.addVoided(le, 15);
				});
				
			}, 14);
			
			return 15;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.LEFT_CLICK_AIR))
		{			
			EnderPearl ep = (EnderPearl) p.getWorld().spawnEntity(p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(1.5)), EntityType.ENDER_PEARL);
			ep.setVelocity(p.getEyeLocation().getDirection().normalize().multiply(4));
			ep.setShooter(p);
			EntityEffects.playSound(p, Sound.ENTITY_SHULKER_SHOOT, SoundCategory.AMBIENT);
			return 5;
		}
		
		return -1;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 5;
	}

}
