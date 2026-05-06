package com.eol.echoes.abilities.instances.special;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class LuminusRadiance extends EchoAbility
{

	public LuminusRadiance()
	{
		super("Luminus' Radiance", "radiance", Material.NETHER_STAR, StatType.MELEE, 0, 0, AbilityType.SPECIALABILITY, ElementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD,
				PrintUtils.assignDurabilityCost(50),
				"&r&fApplies &e&oExposed &r&fto all nearby &dMobs &7(25m, 10s)","",
				"&r&eExposed Effect&r&f: Reveals an entity's location and &6&oBreaks &r&fthem.",
				"&r&fIf those affected are "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f-based, they instantly die.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		EntityEffects.playSound(p, Sound.BLOCK_BELL_RESONATE, SoundCategory.AMBIENT);
		if (!RayCastUtils.getNearbyEntities(p, 25, (target)->
		{
			if (!(target instanceof LivingEntity) && (target instanceof Player)) return;
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 25, 0.75, 30, Particle.DUST_PILLAR, null);
			CelestioEffects.addExposed(target, 10);	
		})) return -1;
		return 50;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 50;
	}

}
