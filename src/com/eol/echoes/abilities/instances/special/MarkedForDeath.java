package com.eol.echoes.abilities.instances.special;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.eol.echoes.EchoData;
import com.eol.echoes.EchoManager;
import com.eol.echoes.EchoManager.DurabilityOperation;
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
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;
import com.ouroboros.utils.entityeffects.helpers.JinxData;

public class MarkedForDeath extends EchoAbility
{

	public MarkedForDeath()
	{
		super("Marked for Death", "marked_for_death", Material.NETHER_STAR, StatType.MELEE, 0, 0, 0, AbilityType.ULTIMATE, ElementType.MORTIO,
				CastConditions.MIXED, EchoForm.SCYTHE, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.MORTIO)+"Marked for Death&f: "+PrintUtils.color(ObsColors.MORTIO)+"&oVex&r&f --",
				"&r&fMark &6target &dMob&f and apply &4Haze &7(45m, 30s)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.MORTIO)+"Marked for Death&f: "+PrintUtils.color(ObsColors.MORTIO)+"&oHarvest&r&f -- &cRemove &f15 &b&oDurability",
				"&r&3Teleport&f to a marked target, dealing "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio &r&fdamage equal to",
				"&b&o300%&r&f of &oBase Attack&r&f x &oCritical Multiplier&r&f and gain &4Jynx &bI&f.",
				"&r&fWhile there are no marked targets, restore &b&oDurability&r&f",
				"&r&fequal to current stacks of &4Jynx&f x 10.","",
				"&4Haze &eEffect&f: afflicted receive an additional &b&o25%&r "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio &r&fdamage.");
	}

	private static Map<UUID, UUID> markedTarget = new HashMap<>();
	
	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (!markedTarget.containsKey(p.getUniqueId()))
			{
				JinxData jd = MortioEffects.jinxRegistry.get(p.getUniqueId());
				if (jd == null) return -1;
				
				EntityEffects.playSound(p, Sound.ENTITY_WARDEN_HEARTBEAT, SoundCategory.MASTER);
				EntityEffects.playSound(p, Sound.ENTITY_CREAKING_AMBIENT, SoundCategory.AMBIENT);
				ObsParticles.drawCylinder(p.getLocation(), 3, 3, 10, 0.5, 0.1, Particle.ENCHANT, null);
				ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 8, Particle.CRIMSON_SPORE, null);
				ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 8, Particle.SMOKE, null);
				EchoManager.modifyDurability(p, e.getItem(), DurabilityOperation.ADD, jd.magnitude*10, false);
				
				return 0;
			}
			else
			{
				UUID targetUUID = markedTarget.get(p.getUniqueId());
				if (targetUUID == null) return -1;
				
				LivingEntity target = (LivingEntity) Bukkit.getEntity(targetUUID);
				if (target == null || target.isDead()) return -1;
				
				Location loc = (Location) target.getLocation().clone();
				if (loc == null) return -1;
				
				MobData data = MobData.getMob(targetUUID);
				if (data == null) return -1;
				
				EchoData echoData = EchoManager.getEchoData(e.getItem());
				if (echoData == null) return -1;
				
				double damage = (echoData.getAttack() * 3) * echoData.getCritModifier();
				EntityEffects.playSound(p, Sound.ENTITY_WITHER_AMBIENT, SoundCategory.AMBIENT);
				ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 4, 0.8, 20, 0.1, Particle.LARGE_SMOKE, null);
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					if (!p.isOnline() || p.isDead() || !markedTarget.containsKey(p.getUniqueId())) return;

					p.teleport(loc);
					p.getLocation().setDirection(loc.getDirection());

					PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.MORTIO)+"&oVex Dissipates..");
					ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 4, 0.8, 20, 0.1, Particle.LARGE_SMOKE, null);
					EntityEffects.playSound(p, Sound.ENTITY_PLAYER_TELEPORT, SoundCategory.MASTER);
					EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.AMBIENT);
		            markedTarget.remove(p.getUniqueId());
					
		            MortioEffects.addJinxStacks(p, 1);
		            if (!MobData.isDying(targetUUID)) MobData.damageUnnaturally(p, target, damage, true, true, ElementType.MORTIO, EchoManager.getCodec(e.getItem()));
				}, 15);
				
				return 15;
			}
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{			
		    if (!RayCastUtils.getEntity(p, 45, target->
		    {
		        if (!(target instanceof LivingEntity le) || target instanceof Player) return;
		        if (markedTarget.containsValue(le.getUniqueId())) return;
		        
		        markedTarget.remove(p.getUniqueId());
		        markedTarget.put(p.getUniqueId(), le.getUniqueId());
		        
		        double theta = ObsParticles.deriveDegreeTheta(p.getLocation(), le.getLocation());
		        EntityEffects.playSound(p, Sound.ENTITY_WITHER_SHOOT, SoundCategory.AMBIENT);
		        ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 10, theta, 0.3, Particle.LARGE_SMOKE, null);
		        ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.8, 9.5, theta, 0.7, Particle.ASH, null);
		        ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.4, 9, theta, 0.5, Particle.CRIMSON_SPORE, null);
		        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		        {
		            MortioEffects.addHaze(le, 30);
		            ObsParticles.drawLandingWave(le);
		            ObsParticles.drawMortioCastSigil(le);
		        }, 10);
		        
		        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		        {
		            if (!p.isOnline() || p.isDead() || !markedTarget.containsKey(p.getUniqueId())) return;
		            PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.MORTIO)+"&oVex Dissipates..");
		            EntityEffects.playSound(p, Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT);
		            markedTarget.remove(p.getUniqueId());
		        }, 600);
		    
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

	public static void registerAbilityHelper(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void onQuit(PlayerQuitEvent e)
			{
				Player p = e.getPlayer();
				if (markedTarget.containsKey(p.getUniqueId())) markedTarget.remove(p.getUniqueId());
			}
			
			@EventHandler
			public void onMobDeath(EntityDeathEvent e)
			{
				Entity entity = e.getEntity();
				if (!(entity instanceof LivingEntity le)) return;
				if (!(le.getKiller() instanceof Player p)) return;
				if (!markedTarget.containsKey(p.getUniqueId())) return;
				if (markedTarget.get(p.getUniqueId()).equals(le.getUniqueId())) 
				{
					PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.MORTIO)+"&oVex Dissipates..");
					EntityEffects.playSound(p, Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT);
					markedTarget.remove(p.getUniqueId());
				}
			}
		}, plugin);
	}
	
}
