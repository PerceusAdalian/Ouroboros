package com.lol.spells.instances.cosmo;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Collapse extends Spell
{
	public Collapse()
	{
		super("Collapse", "collapse", Material.ENDER_PEARL, SpellType.OFFENSIVE, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 150, 2.5, false, false,
				"&r&fCollapses &b&onearby Mobs&r&f into &6target&r&d Mob &7(Range: &640m&7, Radius: &b20m&7)",
				"&r&fPulled &d&oMobs&r&f take "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo &r&fdamage equal to the &6target&f's &eLevel&r&f.",
				"&r&6Target&f takes &e&lCrush &r&fdamage equal to &b&o# Pulled ÷ 4&r&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getEntity(p, 40);
		if (target == null || !(target instanceof LivingEntity initialTarget) || target instanceof Player) return -1;
		MobData data = MobData.getMob(initialTarget.getUniqueId());
		EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.AMBIENT);
		ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.9, 0.4, Particle.END_ROD, null);
		ObsParticles.drawLine(p.getLocation(), target.getLocation(), 5, 0.5, Particle.SONIC_BOOM, null);
		
		List<Entity> nearby = RayCastUtils.getNearbyEntities(target, 20);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{		
			ObsParticles.drawX(target.getLocation(), p.getEyeLocation().getDirection(), 8, 0.4, 0.1, false, Particle.END_ROD, null);
			ObsParticles.drawX(target.getLocation(), p.getEyeLocation().getDirection(), 8, 0.4, 0.1, false, Particle.GLOW_SQUID_INK, null);
			
			if (nearby.isEmpty()) // No damage possible; there aren't any mobs to deal damage/crush inward towards the mob, so it just "fails"
			{
				EntityEffects.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.AMBIENT);
				ObsParticles.drawCosmoCastSigil((LivingEntity) target);
			}
			else
			{
				double damageToNearby = data != null ? data.getLevel() : 1;
				double count = 0;
				for (Entity nTarget : nearby)
				{
					if (!(nTarget instanceof LivingEntity)) continue;
					double deltaAngle = ObsParticles.deriveDegreeTheta(target.getLocation(), nTarget.getLocation());
					ObsParticles.drawAngledArcLine(target.getLocation(), nTarget.getLocation(), 0.6, 10, deltaAngle, 0.5, Particle.GLOW_SQUID_INK, null);
					
					Vector pull = target.getLocation().toVector().subtract(nTarget.getLocation().toVector());
					MobData.damageUnnaturally(p, nTarget, damageToNearby, true, true, ElementType.COSMO);
					if (pull.lengthSquared() < 0.001) continue;
					pull.normalize().multiply(4);
					nTarget.setVelocity(pull);
					count++;
				}
				MobData.damageUnnaturally(p, target, count / 4.0, true, true, ElementType.CRUSH);
			}
		}, 15);
		
		return 150;
	}

	@Override
	public int getTotalManaCost()
	{
		return 150;
	}

}
