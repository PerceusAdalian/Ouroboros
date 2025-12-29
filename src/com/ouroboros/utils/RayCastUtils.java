package com.ouroboros.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class RayCastUtils 
{
	public static Entity getNearestEntity(Player p, int range)
	{
	    RayTraceResult result = p.getLocation().getWorld().rayTraceEntities(p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(2)),p.getEyeLocation().getDirection(),range);
	    
	    if(result == null || result.getHitEntity() == null) return null;
	    return result.getHitEntity();
	}
	
	public static List<Block> getNearbyBlocks(Location origin, int radius) 
	{
		List<Block> locations = new ArrayList<>();
		for (int iy = -radius; iy <= radius; iy++)
		{
			for (int ix = -radius; ix <= radius; ix++)
			{
				for (int iz = -radius; iz <= radius; iz++)
				{
					Location newLoc = origin.clone().add(ix,iy,iz);
					locations.add(newLoc.getBlock());
				}
			}
		}
		return locations;
	}
	
	public static Block rayTraceBlock(Player p, double range) 
	{
		Location eye = p.getEyeLocation();
		RayTraceResult result = p.getWorld().rayTraceBlocks(eye, eye.getDirection(), range);

	    if(result == null || result.getHitBlock() == null) return null;
	    return result.getHitBlock();
	}
	
	/**
	 * @param source via player's location
	 * @param radius to check for mobs
	 * @param action to accept for each entity (LivingEntity by default)
	 */
	public static boolean getNearbyEntities(Player source, int radius, Consumer<LivingEntity> action) 
	{
	    List<Entity> nearbyEntities = source.getNearbyEntities(radius, radius, radius);
	    
	    if (nearbyEntities.isEmpty()) 
	    {
	        PrintUtils.OBSFormatError(source, "No targets found.");
	        return false;
	    }

	    for (Entity target : nearbyEntities) 
	    {
	        if (target instanceof LivingEntity livingTarget) 
	        {
	            action.accept(livingTarget);
	        }
	    }
	    return true;
	}
	
	/**
	 * @param source via specified entity location
	 * @param radius to check for mobs
	 * @param action to accept for each entity (LivingEntity by default)
	 */
	public static boolean getNearbyEntities(Entity source, int radius, Consumer<LivingEntity> action) 
	{
		List<Entity> nearbyEntities = source.getNearbyEntities(radius, radius, radius);
		
		if (nearbyEntities.isEmpty()) 
	    {
	        return false;
	    }

	    for (Entity target : nearbyEntities) 
	    {
	        if (target instanceof LivingEntity livingTarget) 
	        {
	        	if (target instanceof Player) continue;
	            action.accept(livingTarget);
	        }
	    }
	    return true;
	}
}
