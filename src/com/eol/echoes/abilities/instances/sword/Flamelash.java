package com.eol.echoes.abilities.instances.sword;

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
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class Flamelash extends EchoAbility
{

	public Flamelash() 
	{
		super("Flamelash", "flamelash", Material.BLAZE_ROD, StatType.MELEE, 3, 1, 10, AbilityType.COMBAT, ElementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD,
				"&r&fDeal &l5&r&c♥ &e&lSlash&r&f damage to &6target &dMob&f, inflicting &cBurn &7(7m, 10s)");
	}

	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 7, target ->
		{
			if (!(target instanceof LivingEntity) || target instanceof Player) return;
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), p.getLocation().distance(target.getLocation()), 0.1, Particle.LAVA, null);
			EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.MASTER);
			ObsParticles.drawSpiralVortex(target.getLocation(), target.getWidth(), Math.max(Math.min(1, target.getHeight()), 2), 0, Particle.LAVA, null);
			
			MobData.damageUnnaturally(p, target, 5, true, true, ElementType.SLASH);
			InfernoEffects.addBurn((LivingEntity) target, 10);
			
		})) return -1;
		return 10;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 10;
	}
}
