package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

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
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class LuminasRadiance extends EchoAbility
{

	public LuminasRadiance()
	{
		super("Lumina's Radiance", "luminas_radiance", Material.NETHER_STAR, StatType.RANGED, 0, 0, 0, AbilityType.ULTIMATE, ElementType.CELESTIO,
				CastConditions.MIXED, EchoForm.BOW, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.LEFT_CLICK_AIR),
				"&r&eLumina's Radiance&f: &e&oHoly&r&f -- &cRemove &f15 &b&oDurability",
				"&r&fDeal 100&c"+Symbols.HP+PrintUtils.color(ObsColors.CELESTIO)+" &lCelestio&r&f damage as a &d&oLinear AOE",
				"&r&fto any hit &6target &dMob &fapplying &e&oHumility &r&7(30m, 15s)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&eLumina's Radiance&f: &e&oPrayer&r&f -- &cRemove &f5 &b&oDurability",
				"&r&fGrant &eWard &bIII &fand &b&oSwift Footed &r&fto &6self &7(30s)","",
				"&r&eHumility Effect&r&f: Affected take &b&o15%&r&f more "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f damage.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (!CelestioEffects.addWard(p, 2, 30)) return -1;
			EntityEffects.add(p, PotionEffectType.SPEED, 600, 0);
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 6, .85, 20, 0.3, Particle.CLOUD, null);
			EntityEffects.playSound(p, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.AMBIENT);
			return 5;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.LEFT_CLICK_AIR))
		{	
			
			Block bTarget = RayCastUtils.rayTraceBlock(p, 30);
			Location targetLoc = bTarget != null ? bTarget.getLocation() : p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(30));
			
			EntityEffects.playSound(p, Sound.ENTITY_BREEZE_INHALE, SoundCategory.AMBIENT);
			ObsParticles.drawAngledArcLine(targetLoc, p.getLocation(), 0.6, 3, -35, 1.5, Particle.CLOUD, null);
			ObsParticles.drawAngledArcLine(targetLoc, p.getLocation(), 0.6, 3, 215, 1.5, Particle.CLOUD, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				RayCastUtils.createHitBox(p, 5, 3, 30, target ->
				{
					if (!(target instanceof LivingEntity le) || target instanceof Player) return;
					
					MobData.damageUnnaturally(p, le, 100, true, true, ElementType.CELESTIO, null);
					CelestioEffects.addHumility(le, 15);
				});
				
				EntityEffects.playSound(p, Sound.ENTITY_SHULKER_SHOOT, SoundCategory.AMBIENT);
				ObsParticles.drawLine(p.getEyeLocation(), targetLoc, 1,   -.5, Particle.CRIT,      null);
				ObsParticles.drawLine(p.getEyeLocation(), targetLoc, 2,   -.6, Particle.WAX_ON,    null);
				ObsParticles.drawLine(p.getEyeLocation(), targetLoc, 0.5, -.5, Particle.END_ROD,   null);
			}, 10);
			
			return 15;
		}
		
		return -1;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 5;
	}

}
