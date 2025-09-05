package com.ouroboros.utils;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class OBSParticles 
{
	public static <T> void drawPoint(Location source, Particle p, double heightOffset, @Nullable T data) 
	{
		Vector v = source.toVector();
		source.getWorld().spawnParticle(p, v.getX(), v.getY() + heightOffset, v.getZ(), 0, 0, 0, 0, data);
	}
	
	public static <T> void drawLine(Location source, Location target, double interval, double heightOffset, Particle p, @Nullable T data) 
	{
		if (!source.getWorld().equals(target.getWorld())) 
		{
			return;
		}
		
		World w = source.getWorld();
		
		Vector v1 = source.toVector();
		Vector v2 = target.toVector();
		Vector vPrime = v2.subtract(v1).normalize().multiply(interval);
		
		double d1 = source.distance(target);
		double d2 = 0;
		
		for (;d2 < d1; v1.add(vPrime)) 
		{
			w.spawnParticle(p, v1.getX(), v1.getY() + heightOffset, v1.getZ(), 0, 0, 0, 0, data);
			d2 += interval;
		}
	}
	
	public static <T> void drawSinLine(Location loc1, Location loc2, double interval, Particle particle, T data)
    {
        // Check to see if both locations are in the same world
        if (!loc1.getWorld().equals(loc2.getWorld()))
            return;
        World world = loc1.getWorld();

        Vector vec1 = loc1.toVector();
        Vector vec2 = loc2.toVector(); 
        Vector vecf = vec2.subtract(vec1).normalize().multiply(interval);

        double distance = loc1.distance(loc2);
        double length = 0;

        for (; length < distance; vec1.add(vecf))
        {
            world.spawnParticle(particle, vec1.getX() + (Math.sin(length)), vec1.getY(), vec1.getZ(), 0, 0, 0, 0, data);
            length += interval;
        }
    }
	
	public static <T> void drawCosLine(Location loc1, Location loc2, double interval, Particle particle, T data)
    {
        // Check to see if both locations are in the same world
        if (!loc1.getWorld().equals(loc2.getWorld())) return;
        World world = loc1.getWorld();

        Vector vec1 = loc1.toVector();
        Vector vec2 = loc2.toVector(); 
        Vector vecf = vec2.subtract(vec1).normalize().multiply(interval);

        double distance = loc1.distance(loc2);
        double length = 0;

        for (; length < distance; vec1.add(vecf))
        {
            world.spawnParticle(particle, vec1.getX() + (Math.cos(length)), vec1.getY(), vec1.getZ(), 0, 0, 0, 0, data);
            length += interval;
        }
    }
	
	public static <T> void drawArcLine(Location loc1, Location loc2, double interval, double arcPeak, Particle particle, T data) 
	{
		if (!loc1.getWorld().equals(loc2.getWorld())) return;
		World world = loc1.getWorld();
		
		Vector vec1 = loc1.toVector();
		Vector vec2 = loc2.toVector();
		
		double distance = loc1.distance(loc2);
		Vector direction = vec2.subtract(vec1).normalize();
		
		double height = distance / arcPeak; //Controls arc height
		double length = 0;
		
		for (; length < distance; length += interval) 
		{
			Vector point = vec1.clone().add(direction.clone().multiply(length));
			double arcHeight = height * Math.sin(Math.PI * (length/distance));
			world.spawnParticle(particle, point.getX(), vec1.getY() + arcHeight, point.getZ(), 0, 0, 0, 0, data);
		}
	}
	
	//Same as drawArcLine() but adds an angle at which the arc line can follow.
	public static <T> void drawAngledArcLine(Location loc1, Location loc2, double interval, double arcPeak, double degTheta, double arcDeviation, Particle particle, T data) 
	{
		Random rand = new Random();

		if (!loc1.getWorld().equals(loc2.getWorld())) return;
		World world = loc1.getWorld();
		
		Vector vec1 = loc1.toVector();
		Vector vec2 = loc2.toVector();
		
		double distance = loc1.distance(loc2);
		Vector direction = vec2.subtract(vec1).normalize();
		
		double baseArcHeight = distance / arcPeak; //Controls arc height; the higher the value, the smaller the peak.
		
		double thetaRad = Math.toRadians(degTheta + (rand.nextDouble() * arcDeviation - arcDeviation / 2)); // Convert degrees-theta to theta-radians, and adds slight random variation to theta.
		
		Vector perp = new Vector(-direction.getZ(), 0, direction.getX()).normalize(); //Assigns a perpendicular vector for rotating 
		Vector rotatedPerp = perp.clone().rotateAroundAxis(direction, thetaRad); // Rotate around the direction perpendicular to thetaRad
		
		for (double length = 0; length < distance; length += interval) 
		{
			Vector point = vec1.clone().add(direction.clone().multiply(length)); //Creating points from initial location to final.
			double arcHeightDeviation = baseArcHeight * Math.sin(Math.PI * (length/distance)); //Controls the arc height as a sine wave.
			Vector displacement = rotatedPerp.clone().multiply((arcHeightDeviation += (rand.nextDouble() * arcDeviation - arcDeviation / 2)));
			world.spawnParticle(particle, 
					point.getX() + displacement.getX(), //Applying x, y, and z coordinates of the particle to their displacement. 
					point.getY() + displacement.getY(), 
					point.getZ() + displacement.getZ(), 
					0, 0, 0, 0, 
					data);
		}
	}
	
	public static <T> void drawCircle(Location location, double radius, double density, double heightOffset, Particle particle, T data)
    {
        World world = location.getWorld();

        double increment = (2 * Math.PI) / density;
        ArrayList<Location> locations = new ArrayList<Location>();

        for (int i = 0; i < density; i++)
        {
            double angle = i * increment;
            double x = location.getX() + (radius * Math.cos(angle));
            double z = location.getZ() + (radius * Math.sin(angle));

            locations.add(new Location(world, x, location.getY() + heightOffset, z));
        }

        locations.forEach(loc -> {
            world.spawnParticle(particle, loc.setDirection(new Vector(0, 0, 0)), 0, 0, 0, 0, data);
        });
    }
	
	public static <T> void drawAngledCircle(Location location, double radius, double density, double angledelta, double heightOffset, Particle particle, T data)
    {
        World world = location.getWorld();

        double increment = (2 * Math.PI) / density;
        ArrayList<Location> locations = new ArrayList<Location>();

        for (int i = 0; i < density; i++)
        {
            double angle = (i * increment) + angledelta;
            double x = location.getX() + (radius * Math.cos(angle));
            double z = location.getZ() + (radius * Math.sin(angle));

            locations.add(new Location(world, x, location.getY() + heightOffset, z));
        }

        locations.forEach(loc -> {
            world.spawnParticle(particle, loc.setDirection(new Vector(0, 0, 0)), 0, 0, 0, 0, data);
        });
    }
	
	public static <T> void drawCylinder(Location location, double radius, int stacks, int density, double distance, double heightOffset, Particle particle, T data)
    {
        Location buffer = location.clone();

        for (int i = 0; i < stacks; i++)
        {
            // Draw circle
            drawCircle(buffer, radius, density, heightOffset, particle, data);

            // Add height to buffer to create a cylinder
            buffer = buffer.add(0, distance, 0);
        }
    }
	
	public static <T> void drawDisc(Location location, double radius, int layers, int density, double heightOffset, Particle particle, T data)
    {
        for (int i = 0; i < layers; i++)
        {
            drawCircle(location, radius + (i/2), density + i, heightOffset, particle, data);
        }
    }
	
	/**
	 * 
	 * @param <T> @Nullable Generic data type for particle data.
	 * @param location Source location.
	 * @param radius X-coordinate declaration.
	 * @param layers Y-coordinate declaration.
	 * @param distanceBetweenLayers How many blocks apart from each layer.
	 * @param deltaangle How steep the vortext is.
	 * @param density How many particles to spawn per layer.
	 * @param heightOffset Determines how far away vertically from the source the vortex begins.
	 * @param particle Particle type.
	 * @param data Particle data.
	 */
	public static <T> void drawVerticalVortex(Location location, double radius, double layers, double distanceBetweenLayers, double deltaangle, int density, double heightOffset, Particle particle, T data)
    {
        for (double i = 0; i < layers; i += 1)
        {
            drawAngledCircle(location.add(0, (i * distanceBetweenLayers), 0), radius, density, deltaangle*i, heightOffset, particle, data);
        }
    }
	
	/**
	 * @param origin Location to place the spiral.
	 * @param tBound Controls width and length increment of the spiral. Standard formulation for "t" is 6*PI rads.
	 * @param spiralHeight Controls the height limit for the spiral.
	 * @param heightOffset Controls the initial height of the spiral. (i.e. 1.0 will start the spiral 1 block above the origin.)
	 * @param particle Particle type.
	 * @param data Particle data.
	 */
	public static <T> void drawSpiralVortex(Location origin, double tBound, double spiralHeight, double heightOffset, Particle particle, T data) 
	{
		if (tBound > 6.0 || tBound < 0.0) tBound = 6.0;
		double tLimit = tBound * Math.PI;
		double stepSize = tLimit / 100.0;
		
		for (double t = 0; t <= tLimit; t+=stepSize) 
		{
			double r = Math.exp(-0.1 * t);
			double x = r * Math.cos(t);
			double y = r * Math.sin(t);
			double z = spiralHeight * t;
			
			Location pLocation = origin.clone().add(x, z, y);
			origin.getWorld().spawnParticle(particle, pLocation, 0, 0, 0, 0, data);
		}
	}
	
	public static <T> void drawWisps(Location location, double domain, double range, int density, Particle particle, T data) 
	{
		/*
		 * Domain and range are constrained by a locations given x,y,z coordinates. 
		 * Density constrains how many particles to spawn.
		 */
		if (location == null || location.getWorld() == null) return;
		World w = location.getWorld();
		Random rand = new Random();
		
		for (int i = 0; i < density; i++) 
		{
			double x = location.getX() + (rand.nextDouble() * 2 - 1) * domain;
	        double y = location.getY() + (rand.nextDouble() * range);
	        double z = location.getZ() + (rand.nextDouble() * 2 - 1) * domain;
			w.spawnParticle(particle, new Location(w,x,y,z), 0, 0, 0, 0, data);
		}
	}
	
	public static void drawInfernoCastSigil(Player p) 
	{
		OBSParticles.drawDisc(p.getLocation(), p.getWidth()+1, 1, 8, 0.25, Particle.LAVA, null);
		OBSParticles.drawDisc(p.getLocation(), p.getWidth()+1.25, 2, 6, 0.35, Particle.SMOKE, null);
		OBSParticles.drawDisc(p.getLocation(), p.getWidth()+1.5, 2, 9, 0.45, Particle.ASH, null);
	}

	public static void drawMortioCastSigil(Player p)
	{
		OBSParticles.drawDisc(p.getLocation(), p.getWidth()+1, 1, 8, 0.25, Particle.ASH, null);
		OBSParticles.drawDisc(p.getLocation(), p.getWidth()+1.25, 2, 6, 0.35, Particle.SMOKE, null);
		OBSParticles.drawDisc(p.getLocation(), p.getWidth()+1.5, 2, 9, 0.45, Particle.CRIMSON_SPORE, null);
	}
}
