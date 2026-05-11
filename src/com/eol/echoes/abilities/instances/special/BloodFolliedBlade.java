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
import com.eol.echoes.EchoManager.DurabilityOperation;
import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
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
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.HeresioEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;

public class BloodFolliedBlade extends EchoAbility
{
	public BloodFolliedBlade() 
	{
		super("Blood-Follied Blade", "blood_follied_blade", Material.NETHER_STAR, StatType.MELEE, 0, 0, 0, AbilityType.SPECIALABILITY, ElementType.HERESIO,
				CastConditions.MIXED, EchoForm.SWORD, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&2Blood-Follied Blade&f: &2&oFoul Play&r&f -- Deal 2&c"+Symbols.HP+" &fto &6Self&f",
				"&r&fRush towards &6target &dMob&f dealing &b&o200% &r&f&oBase Attack&r&f as "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f damage,",
				"&r&finflicting &2Intimidate&f, and on kill, grants a stack of "+PrintUtils.color(ObsColors.MORTIO)+"Jinx&f to &6self &7(15m, 10s)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&2Blood-Follied Blade&f: &2&oRegal Penance&r&f --",
				"&r&aRestore &cHP&f and &b&oDurability&r&f equal to stacks of "+PrintUtils.color(ObsColors.MORTIO)+"Jinx&f.");
	}

	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			int jinxStacks = MortioEffects.jinxRegistry.get(p.getUniqueId()).magnitude;
			EntityEffects.playSound(p, Sound.ENTITY_CREAKING_ACTIVATE, SoundCategory.AMBIENT);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.AMBIENT);
				ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 4, 0.7, 20, 0.3, Particle.WARPED_SPORE, null);
				ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 3, 0.7, 17, 0.4, Particle.BLOCK_CRUMBLE, Material.WARPED_WART_BLOCK);
				
				EntityEffects.heal(p, jinxStacks);
				EchoManager.modifyDurability(p, e.getItem(), DurabilityOperation.ADD, jinxStacks, false);
				MortioEffects.jinxRegistry.remove(p.getUniqueId());
			}, 12);
			return 0;
		}

		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{			
			EchoData stats = EchoManager.getEchoData(e.getItem());
			if (stats == null) return -1;
			
			double damage = stats.getAttack() * 2.0;
			
			if (!RayCastUtils.getEntity(p, 15, target ->
			{
				if (!(target instanceof LivingEntity le) || target instanceof Player) return;
				
				EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.AMBIENT);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.6, 0.4, Particle.SWEEP_ATTACK, null);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.6, 0.4, Particle.WARPED_SPORE, null);
				EntityEffects.rushEntity(p, le, 2.0);
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS);
					ObsParticles.drawLandingWave(le);
					
					MobData data = MobData.getMob(le.getUniqueId());
					boolean fatal = data != null && (data.getHp(false) - damage <= 0);
					
					MobData.damageUnnaturally(p, le, damage, true, true, ElementType.HERESIO);
					HeresioEffects.addIntimidate(le, 0, 10);
					MobData.damageUnnaturally(p, p, 2, false, false, null);
					if (fatal)
					{
						MortioEffects.addJinxStacks(p, 1);
					}
					
				}, 10);
				
			})) return -1;
			
			return 0;
		}
		
		return -1;
	}

	@Override
	public int getFinalDurabilityCost() 
	{
		return 0;
	}

}
