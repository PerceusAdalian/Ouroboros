package com.ouroboros.utils.entityeffects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.ObsTimer;
import com.ouroboros.utils.PrintUtils;

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
		
		ObsParticles.drawCosmoCastSigil(target);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> isVoidedRegistry.remove(target.getUniqueId()), seconds * 20);
		ObsTimer.runWithCancel(Ouroboros.instance, (r) -> 
		{
			if (target.isDead()) 
			{
				r.cancel();
				return;
			}
			ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 4, Particle.SCULK_SOUL,null);
		}, 20, seconds * 20);
	}
	
	public static Set<UUID> hasNegate = new HashSet<>();
	public static boolean addNegate(Player p, int seconds)
	{
		UUID uuid = p.getUniqueId();
		if (hasNegate.contains(uuid)) return false;
		hasNegate.add(uuid);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
		{
			if (p.isOnline() && !p.isDead() && hasNegate.contains(uuid))
			{
				PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.COSMO)+"&o'Negate' &r&7&oEffect End");
				EntityEffects.playSound(p, Sound.ITEM_SHIELD_BREAK, SoundCategory.AMBIENT);
			}
			hasNegate.remove(uuid);
		}, seconds * 20);
		
		ObsTimer.runWithCancel(Ouroboros.instance, (r) -> 
		{
			if (!hasNegate.contains(uuid) || p.isDead() || !p.isOnline()) 
			{
				r.cancel();
				return;
			}
			ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 6, Particle.DRAGON_BREATH, 0.5f);
		}, 20, seconds * 20);
		
		return true;
	}
}
