package com.ouroboros.mobs;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.ouroboros.mobs.events.MobGenerateEvent;
import com.ouroboros.mobs.events.MobGenerateEvent.SummonRequest;

public class MobSummoner
{
	public static LivingEntity build(Location location, EntityType type, int level, String customName)
	{
	    MobGenerateEvent.pendingSummons.put(location, new SummonRequest(level, customName));
	    return (LivingEntity) location.getWorld().spawnEntity(location, type);
	}
}
