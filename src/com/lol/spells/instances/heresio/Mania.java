package com.lol.spells.instances.heresio;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Mania extends Spell
{

	public Mania() 
	{
		super("Mania", "mania", Material.ENDER_EYE, SpellType.CONTROL, SpellementType.HERESIO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 200, 10, false,
				"&r&fCauses other &d&oMobs&r&f around you to attack each other &7(30m)");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		List<LivingEntity> nearby = RayCastUtils.getNearbyEntities(p, 30).stream()
			    .filter(target -> target instanceof Mob)
			    .map(target -> (LivingEntity) target)
			    .toList();

		if (nearby.isEmpty()) return -1;
		
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{			
			EntityEffects.playSound(p, Sound.ENTITY_ENDERMAN_SCREAM, SoundCategory.AMBIENT);
			OBSParticles.drawWave(Ouroboros.instance, p.getLocation(), 30, 0.3, 8, Particle.WITCH, null);
			OBSParticles.drawWave(Ouroboros.instance, p.getLocation(), 30, 0.4, 8, Particle.TRIAL_OMEN, null);
			OBSParticles.drawWave(Ouroboros.instance, p.getLocation(), 30, 0.25, 8, Particle.GLOW_SQUID_INK, null);
			
			for (LivingEntity entity : nearby) 
			{
				OBSParticles.drawHeresioCastSigil(entity);
				
				nearby.stream()
				.filter(other -> !other.getUniqueId().equals(entity.getUniqueId()))
				.findFirst()
				.ifPresent(target -> ((Mob) entity).setTarget(target));
			}
		}, 20);
		
		
		return 200;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 200;
	}

}
