package com.ouroboros.utils.entityeffects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.ObsTimer;

public class CosmoEffects
{
	/**
	 * @Description Cosmo Signature Effect: neutralizes affected entity's elemental affinity.",
	 * @param target The target to set as voided.
	 * @param ticks  The duration to add the entity target into the isVoided
	 *               registry.
	 */
	public static Map<UUID, Boolean> isVoidedRegistry = new HashMap<>();

	public static void addVoided(LivingEntity target, int seconds) 
	{
		if (isVoidedRegistry.containsKey(target.getUniqueId()))return;
		isVoidedRegistry.put(target.getUniqueId(), true);
		
		OBSParticles.drawCosmoCastSigil(target);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> isVoidedRegistry.remove(target.getUniqueId()), seconds * 20);
		ObsTimer.runWithCancel(Ouroboros.instance, (r) -> 
		{
			if (target.isDead()) return;
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 4, Particle.SCULK_SOUL,null);
		}, 20, seconds * 20);
	}
}
