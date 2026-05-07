package com.eol.echoes.abilities.instances.special;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class LuminusRadiance extends EchoAbility
{

	public LuminusRadiance()
	{
		super("Luminus' Radiance", "radiance", Material.NETHER_STAR, StatType.MELEE, 0, 0, 50, AbilityType.SPECIALABILITY, ElementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD,
				"&r&fApplies &e&oExposed &r&fto all nearby &dMobs&f in FOV &7(30m, 20s, &6"+Symbols.TARGET+" Limit&7: &630&7)",
				"&r&fIf at least 10 &dMobs&f died, grant &eWard &bV &fto &6self &7(30s)");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		List<Entity> targets = RayCastUtils.getEntitiesInFOV(p, 30.0, 30);
		if (targets == null || targets.isEmpty()) return -1;
		
		EntityEffects.playSound(p, Sound.BLOCK_BELL_USE, SoundCategory.AMBIENT);
		ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 25, 0.75, 30, 0.2, Particle.DUST_PILLAR, null);
		
		AtomicInteger count = new AtomicInteger(0);
		for (int i = 0; i < targets.size(); i++)
		{
			if (!(targets.get(i) instanceof LivingEntity le) || le instanceof Player) continue;
			Location from = i == 0 ? p.getLocation() : ((LivingEntity) targets.get(i - 1)).getLocation();
	        Location to = le.getLocation();
	        
	        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
	        {	        	
	        	ObsParticles.drawLine(from, to, 0.6, 0.5, Particle.END_ROD, null);
	        	ObsParticles.drawLine(from, to, 0.7, 0.4, Particle.DUST, new DustOptions(Color.WHITE, 1f));
	        	ObsParticles.drawCelestioCastSigil(le);
	        	
	        	CelestioEffects.addExposed(le, 20);
	        	
	        	MobData data = MobData.getMob(le.getUniqueId());
	        	if (data != null && data.getAffinity(false) == ElementType.MORTIO) 
	        	{
	        		ObsParticles.drawLine(to, from, 0.6, 0.4, Particle.CRIMSON_SPORE, null);
	        		count.getAndIncrement();
	        	}
	        }, 1+i);
		}
		
		if (count.get() >= 10)
		{
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{				
				EntityEffects.playSound(p, Sound.BLOCK_BELL_RESONATE, SoundCategory.AMBIENT);
				ObsParticles.drawCylinder(p.getLocation(), p.getWidth() + 2, 3, 8, 0.5, 0.1, Particle.ENCHANT, null);
				ObsParticles.drawDisc(p.getLocation(), p.getWidth(), 1, 7, 0.4, Particle.WAX_ON, null);
				CelestioEffects.addWard(p, 4, 600);
			}, 30);
		}
		
		return 50;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 50;
	}

}
