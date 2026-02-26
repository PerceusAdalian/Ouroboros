package com.ouroboros.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

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
	
    public static boolean isEntityInFOV(Player player, Entity mob, double foxDegrees)
    {
    	Vector lookDirection = player.getLocation().getDirection().normalize();
    	
    	Location eyeLoc = player.getEyeLocation();
    	Location mobCenter = mob.getLocation().add(0, mob.getHeight() / 2, 0);
    	Vector mobHitbox = mobCenter.toVector().subtract(eyeLoc.toVector()).normalize();
    	
    	double dot = lookDirection.dot(mobHitbox);
    	double FovToRad = Math.toRadians(foxDegrees / 2);
    	
    	return dot >= Math.cos(FovToRad);
    }
    
    public static boolean isMobVisible(Player player, LivingEntity mob, double maxDistance, double fovInDegrees)
    {
    	if (player.getLocation().distanceSquared(mob.getLocation()) > maxDistance * maxDistance)
	    	return false;
    	
    	if(!isEntityInFOV(player, mob, fovInDegrees))
    		return false;
    	
    	if (!player.hasLineOfSight(mob))
    		return false;
    	
    	return true;
    }
	
	public static boolean createHitBox(Player p, double width, double height, double length, Consumer<Entity> consumer)
	{
		Location origin = p.getEyeLocation();
		World w = p.getWorld();
		
		Vector forward = origin.getDirection().normalize();
		Vector upRelative = new Vector(0, 1, 0);
		
		Vector right;
		if (Math.abs(forward.dot(upRelative)) > 0.999) right = new Vector(1, 0, 0);
		else right = forward.clone().crossProduct(upRelative).normalize();
		
		Vector up = right.clone().crossProduct(forward).normalize();
		
		double halfWidthBound = width / 2.0;
		double halfHeightBound = height / 2.0;
		
		Vector nearCenter = toVector(origin);
		Vector farCenter = nearCenter.clone().add(forward.clone().multiply(length));
		
		List<Vector> corners = new ArrayList<>();
		for (double widthBound : new double[] {-halfWidthBound, halfWidthBound})
		{
			for (double heightBound : new double[] {-halfHeightBound, halfHeightBound})
			{
				Vector offset = right.clone().multiply(widthBound).add(up.clone().multiply(heightBound));
				corners.add(nearCenter.clone().add(offset));
				corners.add(farCenter.clone().add(offset));
			}
		}
		
		double minX = corners.stream().mapToDouble(Vector::getX).min().orElse(0);
        double minY = corners.stream().mapToDouble(Vector::getY).min().orElse(0);
        double minZ = corners.stream().mapToDouble(Vector::getZ).min().orElse(0);
        double maxX = corners.stream().mapToDouble(Vector::getX).max().orElse(0);
        double maxY = corners.stream().mapToDouble(Vector::getY).max().orElse(0);
        double maxZ = corners.stream().mapToDouble(Vector::getZ).max().orElse(0);

        BoundingBox hitbox = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        Collection<Entity> hitboxQuery = w.getNearbyEntities(hitbox);
        List<Entity> hitEntities = new ArrayList<>();
        
        for (Entity e : hitboxQuery)
        {
        	if (e.equals(p)) continue;
        	
        	Vector entityHitBox = toVector(e.getLocation()).add(new Vector(0, e.getHeight() / 2.0, 0));
        	Vector relativeEntityPosition = entityHitBox.clone().subtract(nearCenter);
        	
        	double localForward = relativeEntityPosition.dot(forward);
        	double localRight = relativeEntityPosition.dot(right);
        	double localUp = relativeEntityPosition.dot(up);
        	
        	if (localForward >= 0 && localForward <= length &&
        			localRight >= -halfWidthBound && localRight <= halfWidthBound &&
    					localUp >= -halfHeightBound && localUp <= halfHeightBound) 
        	{
        		hitEntities.add(e);
        	}
        }
        
        if (hitEntities.isEmpty()) return false;
        
        hitEntities.forEach(consumer);
        
        return true;
	}
	
	private static Vector toVector(Location loc) 
	{
		return new Vector(loc.getX(), loc.getY(), loc.getZ());
	}
	
}
