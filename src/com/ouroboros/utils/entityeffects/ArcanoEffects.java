package com.ouroboros.utils.entityeffects;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.OBStandardTimer;

public class ArcanoEffects
{
	public static Set<UUID> hasEtherOverload = new HashSet<>();
	public static void addEtherOverload(LivingEntity target, int seconds)
	{
		if (hasEtherOverload.contains(target.getUniqueId())) return;
		OBStandardTimer.runWithCancel(Ouroboros.instance, e->
		{
			if (target.isDead()) return;
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 3, Particle.GLOW_SQUID_INK, null);
		}, 20, seconds*20);
		hasEtherOverload.add(target.getUniqueId());
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasEtherOverload.remove(target.getUniqueId()), seconds * 20);
	}
	
	public static Set<UUID> hasEtherDisruption = new HashSet<>();
	public static void addEtherDisruption(Player target, int seconds)
	{
		if (hasEtherDisruption.contains(target.getUniqueId())) return;
		OBStandardTimer.runWithCancel(Ouroboros.instance, e->
		{
			if (target.isDead()) return;
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 3, Particle.GLOW_SQUID_INK, null);
			OBSParticles.drawDisc(target.getLocation(), target.getWidth(), 3, 4, 0.1, Particle.BUBBLE_COLUMN_UP, null);
		}, 20, seconds*20);
		hasEtherDisruption.add(target.getUniqueId());
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->hasEtherDisruption.remove(target.getUniqueId()), seconds * 20);
	}
	
	public static void addChroma(LivingEntity target, int seconds)
	{
		// TODO
	}
	
	
}
