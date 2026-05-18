package com.eol.echoes.abilities.instances.special;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.EntityCategories;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
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
				"&r&fIf at least 10 &dMobs&f died, grant &eWard &bV &fto &6self &7(30s)","",
				"&r&eExposed Effect&r&f: Reveals an entity's location and &6&oBreaks &r&fthem.",
				"&r&fIf those affected are "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f-based, they instantly die.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		List<Entity> targets = RayCastUtils.getEntitiesInFOV(p, 30.0, 30);
		if (targets == null || targets.isEmpty()) return -1;

		EntityEffects.playSound(p, Sound.BLOCK_BELL_USE, SoundCategory.AMBIENT);
		ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 10, 0.85, 25, 0.2, Particle.DUST_PLUME, null);
		
		AtomicInteger count = new AtomicInteger(0);
		for (int i = 0; i < targets.size(); i++)
		{
		    if (!(targets.get(i) instanceof LivingEntity le) || le instanceof Player) continue;
		    Location to = le.getLocation();
		    
		    final int taskIndex = i;
		    final int lastIndex = targets.size() - 1;
		    
		    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
		    {
		        ObsParticles.drawLine(p.getLocation(), to, 0.9, 0.4, Particle.CLOUD, null);
		        ObsParticles.drawCelestioCastSigil(le);

		        CelestioEffects.addExposed(le, 20);

		        if (EntityCategories.mortio_mobs.contains(le.getType()))
		        {
		            ObsParticles.drawLine(to, p.getLocation(), 0.8, 0.3, Particle.DUST_PILLAR, Material.GLOWSTONE.createBlockData());
		            count.getAndIncrement();
		        }

		        if (taskIndex == lastIndex && count.get() >= 10)
		        {
		            Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
		            {
		                EntityEffects.playSound(p, Sound.BLOCK_BELL_RESONATE, SoundCategory.AMBIENT);
		                ObsParticles.drawCylinder(p.getLocation(), p.getWidth() + 2, 3, 8, 0.5, 0.1, Particle.ENCHANT, null);
		                ObsParticles.drawCelestioCastSigil(p);
		                CelestioEffects.addWard(p, 4, 600);
		            }, 30);
		        }
		    }, 1 + i);
		}
		
		return 50;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 50;
	}

}
