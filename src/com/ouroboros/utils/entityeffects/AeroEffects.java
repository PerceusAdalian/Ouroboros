package com.ouroboros.utils.entityeffects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityCategories;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.OBStandardTimer;
import com.ouroboros.utils.RayCastUtils;

public class AeroEffects
{
	public static Map<UUID, Boolean> hasStatic = new HashMap<>();
	/**
	 * @Description Aero signature effect. Static causes mobs to emit shockwaves in a 10m radius.  
										   Affected mobs receive 1.25x Aero damage, and Aero-based mobs are healed instead.
				"&r&dStatic &eEffect&f: causes mobs to emit shockwaves in a 10m radius.",
				"&r&fAffected mobs receive &b&o1.25x &r&d&lAero&r&f damage.",
				"&d&lAero&r&f-based mobs are &e&oimmune&r&f, and &a&ohealed&r&f instead."
	 * @param target
	 * @param seconds
	 */
	public static void addStatic(LivingEntity target, Player caster, int seconds)
	{
		hasStatic.put(target.getUniqueId(), true);
		OBStandardTimer.runWithCancel(Ouroboros.instance, (r)->
		{
			if (target.isDead() || (target instanceof Player p && !p.isOnline())) return;
			
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 4, Particle.ELECTRIC_SPARK, null);
			
			RayCastUtils.getNearbyEntities(target, 10, (C)->
			{
				if (!(C instanceof LivingEntity) || C.equals(caster)) return;
				
				OBSParticles.drawLine(target.getLocation(), C.getLocation(), target.getLocation().distance(target.getLocation())%2+1, 0.5, Particle.END_ROD, null);
				
				MobData data = MobData.getMob(C.getUniqueId());
				if (EntityCategories.aero_mobs.contains(C.getType()))
				{
					if (data == null) return;
					data.heal(1, false, false, false);
				}
				
				else Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
						target.getWorld().strikeLightning(C.getLocation()), 20);
			});
		}, 20, seconds*20);
		//Remove after expiry
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasStatic.remove(target.getUniqueId()), seconds*20);
	}
	
	public static void addShock(LivingEntity target, int seconds)
	{
		EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds * 20, 99, true);
		EntityEffects.add(target, PotionEffectType.GLOWING, seconds * 20, 0, true);
		OBStandardTimer.runWithCancel(Ouroboros.instance, (r)->
		{
			if (target.isDead()) return;
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.ELECTRIC_SPARK, null);
		}, 10, seconds*20);
	}

	public static void addCharged(LivingEntity target, int magnitude, int seconds)
	{
		EntityEffects.add(target, PotionEffectType.SPEED, seconds * 20, magnitude, true);
		EntityEffects.add(target, PotionEffectType.HASTE, seconds * 20, magnitude, true);
		OBStandardTimer.runWithCancel(Ouroboros.instance, (r)->
		{
			if (target.isDead()) return;
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.DRAGON_BREATH, 1.0f);
		}, 10, seconds*20);
	}
}
