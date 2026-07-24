package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.EchoManager;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.echoes.records.EchoManifest;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.TimeUtils;
import com.ouroboros.utils.TimeUtils.Timeframe;
import com.ouroboros.utils.entityeffects.CosmoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Supernova extends EchoAbility
{

	public Supernova()
	{
		super("Supernova", "supernova", Material.NETHER_STAR, StatType.MELEE, 0, 0, 0, AbilityType.ULTIMATE, ElementType.COSMO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD, 
				PrintUtils.assignAstralVariant("Supernova", true) + " &r&e&oQuazar Explosion&r&f -- &cRemove &f50 &bDurability",
				"&r&fInflict &b&o450%&r&f of &oBase Attack&r&f as "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f damage,",
				"&r&fapply &6&oBreak&r&f and "+PrintUtils.color(ObsColors.COSMO)+"&oVoided&r&f to all &6"+Symbols.TARGET+" &fin &d"+Symbols.AOE+" &7(30m, 20s)","",
				PrintUtils.assignAstralVariant("Supernova", false) + " &r&9&oAntimatter Cascade&r&f -- &cRemove &f35 &bDurability",
				"&r&fApply "+PrintUtils.color(ObsColors.COSMO)+"Radiated &bV&f, to all &6"+Symbols.TARGET+" &fin &d"+Symbols.CONAL+" &7(45m, 20s)","",
				PrintUtils.color(ObsColors.COSMO)+"Voided &eEffect&f: Neutralizes elemental affinity.",
				PrintUtils.color(ObsColors.COSMO)+"Radiated &eEffect&f: applies a &2&oCurse&r&f and inflicts a "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo &r&fDOT",
				"&r&fthat scales in intensity with the &b&omagnitude&r&f of "+PrintUtils.color(ObsColors.COSMO)+"Radiated&r&f.","",
				"&b&oEchoic Resonance&r&f: If in "+PrintUtils.color(ObsColors.COSMO)+"&lThe End &r&e->&r&f All effects doubled.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		boolean isInEnd = p.getWorld().getEnvironment().equals(World.Environment.THE_END);
		
		if (TimeUtils.checkTime(Bukkit.getWorld("world"), Timeframe.DAY))
		{
			EchoManifest codec = EchoManager.getCodec(e.getItem());
			double damage = isInEnd ? codec.baseStats().getAttack() * 9 : 4.5;
			
			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER);
			EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.AMBIENT);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.MASTER);	
				ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 30, 0.80, 45, 0.5, Particle.DRAGON_BREATH, 1.0f);
				ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 30, 0.80, 40, 0.2, Particle.BLOCK_CRUMBLE, Material.END_STONE.createBlockData());
				ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 30, 0.75, 30, 0.2, Particle.END_ROD, null);
			}, 14);
			if (!RayCastUtils.getNearbyEntities(p, 30, target ->
			{
				if (target == null || !(target instanceof LivingEntity le)) return;
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{	
					le.getWorld().createExplosion(le.getLocation().getX(), le.getLocation().getY(), le.getLocation().getZ(), 0, !isInEnd, !isInEnd, p);
					MobData.damageUnnaturally(p, le, damage, true, true, ElementType.COSMO, codec);
					CosmoEffects.addVoided(le, 20);
					if (le instanceof Player pTarget) PlayerData.getPlayer(pTarget.getUniqueId()).setBreak();
					else MobData.getMob(le.getUniqueId()).setBreak();
					ObsParticles.drawCosmoCastSigil(le);
				}, 14);
			})) return -1;
			return 50;
		}
		
		if (TimeUtils.checkTime(Bukkit.getWorld("world"), Timeframe.NIGHT))
		{
			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER);
			EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.AMBIENT);
			if (!RayCastUtils.getEntitiesInFov(p, 45, 160, target ->
			{
				if (target == null || !(target instanceof LivingEntity le)) return;
				
				double theta = ObsParticles.deriveDegreeTheta(p.getLocation(), le.getLocation());
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.4, 9, theta, 1, Particle.DRAGON_BREATH, 1.0f);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 8, theta, 0.5, Particle.END_ROD, null);
				ObsParticles.drawCosLine(p.getLocation(), le.getLocation(), 0.7, Particle.BLOCK_CRUMBLE, Material.END_STONE.createBlockData());
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{					
					ObsParticles.drawCosmoCastSigil(le);
					CosmoEffects.addRadiated(le, isInEnd ? 10 : 5, 20);
				}, 12);
			})) return -1;
			return 35;
		}
		
		return -1;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 0;
	}

}
