package com.ouroboros.mobs.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.events.MobGenerateEvent;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class MobManager 
{
	public static final NamespacedKey MOB_DATA_KEY = new NamespacedKey(Ouroboros.instance, "mob_data");
	public static final NamespacedKey MOB_UUID_KEY = new NamespacedKey(Ouroboros.instance, "mob_uuid");
	
	/**
	 * Serializes all active Ouroboros mobs to mobs/serialized.yml and removes
	 * them from the world. Called from onDisable.
	 *
	 * Iteration uses the PDC key as the gate, not the in-memory dataMap, so
	 * this survives scenarios where dataMap lost sync (e.g. the previous boot
	 * crashed mid-load). A mob that has MOB_DATA_KEY stamped on it but is no
	 * longer in dataMap is removed from the world without serialization rather
	 * than being left as a ghost.
	 */
	public static void despawnAll()
	{
	    File file = new File(Ouroboros.instance.getDataFolder(), "mobs/serialized.yml");
	    YamlConfiguration config = new YamlConfiguration();
	    int saved  = 0;
	    int orphan = 0;

	    for (World w : Bukkit.getWorlds())
	    {
	        for (Entity e : w.getEntities())
	        {
	            if (!(e instanceof LivingEntity) || e instanceof Player) continue;
	            if (!e.getPersistentDataContainer().has(MOB_DATA_KEY, PersistentDataType.STRING)) continue;

	            MobData data = MobData.getMob(e.getUniqueId());
	            if (data == null)
	            {
	            	PrintUtils.OBSConsoleError("despawnAll: orphaned mob " + e.getUniqueId() +
	            		" has PDC key but no dataMap entry — removing without saving.");
	            	e.remove();
	            	orphan++;
	            	continue;
	            }

	            data.setLocation(e.getLocation());
	            config.set("mobs." + e.getUniqueId(), data.serialize());
	            e.remove();
	            saved++;
	        }
	    }

	    File mobFolder = new File(Ouroboros.instance.getDataFolder(), "mobs/data");
	    if (mobFolder.exists())
	    {
	        File[] files = mobFolder.listFiles((dir, name) -> name.endsWith(".yml"));
	        if (files != null) 
	        	for (File f : files) 
	        		f.delete();
	    }

	    try 
	    { 
	    	config.save(file); 
	    }
	    catch (IOException error)
	    {
	        PrintUtils.OBSConsoleError("despawnAll: CRITICAL — failed to save serialized mob data!");
	        error.printStackTrace();
	    }

	    PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &o&bDespawnAll&r&f -- &aOK&f &7(OnDisable) " +
	    		"&a" + saved + " mobs saved" +
	    		(orphan > 0 ? "&7, &c" + orphan + " orphans removed" : "") + "&f.");
	}

	/**
	 * Reads mobs/serialized.yml and respawns every mob onto the world using
	 * rehydrate(). suppressSpawnInit prevents MobGenerateEvent from running
	 * its own initialization on these spawns.
	 *
	 * KEY INVARIANT: spawnEntity() gives each mob a brand-new UUID. rehydrate()
	 * maps the snapshot data onto that new UUID. Do NOT use the old UUID from
	 * the serialized string for any dataMap operation after this call.
	 */
	public static void respawnAll()
	{
		MobGenerateEvent.suppressSpawnInit = true;
	    try
	    {
	    	File file = new File(Ouroboros.instance.getDataFolder(), "mobs/serialized.yml");
	    	if (!file.exists())
	    	{
	    		PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &o&bRespawnAll&r&f -- &7No serialized file found, skipping.");
	    		return;
	    	}
	    	
	    	YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
	    	ConfigurationSection mobSection = config.getConfigurationSection("mobs");
	    	if (mobSection == null)
	    	{
	    		PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &o&bRespawnAll&r&f -- &7Serialized file is empty, skipping.");
	    		file.delete();
	    		return;
	    	}
	    	
	    	int spawned = 0;
	    	int failed  = 0;
	    	
	    	for (String key : mobSection.getKeys(false))
	    	{
	    		String serialized = mobSection.getString(key);
	    		if (serialized == null) 
	    		{ 
	    			PrintUtils.OBSConsoleError("respawnAll: null serialized string for key: " + key);
	    			failed++; 
	    			continue; 
	    		}
	    		
	    		String[] segments = serialized.split("\\|");
	    		if (segments.length != 10) 
	    		{ 
	    			PrintUtils.OBSConsoleError("respawnAll: bad segment count for key " + key + ": " + serialized);
	    			failed++; 
	    			continue; 
	    		}
	    		
	    		try
	    		{
	    			EntityType type = EntityType.valueOf(segments[1].trim().toUpperCase().replace(" ", "_"));
	    			
	    			String[] locParts = segments[9].split(",");
	    			World world = Bukkit.getWorld(locParts[0]);
	    			if (world == null) 
	    			{ 
	    				PrintUtils.OBSConsoleError("respawnAll: world not found '" + locParts[0] + "' for mob key: " + key);
	    				failed++; 
	    				continue; 
	    			}
	    			
	    			double x = Double.parseDouble(locParts[1]);
	    			double y = Double.parseDouble(locParts[2]);
	    			double z = Double.parseDouble(locParts[3]);
	    			Location loc = new Location(world, x, y, z);
	    			
	    			Entity e = world.spawnEntity(loc, type);
	    			if (!(e instanceof LivingEntity le)) 
	    			{ 
	    				e.remove(); 
	    				PrintUtils.OBSConsoleError("respawnAll: spawned entity is not LivingEntity for key: " + key);
	    				failed++; 
	    				continue; 
	    			}
	    			
	    			MobData data = MobData.rehydrate(le, serialized, Bukkit.getServer());
	    			if (data == null) 
	    			{ 
	    				le.remove(); 
	    				PrintUtils.OBSConsoleError("respawnAll: rehydrate returned null for key: " + key);
	    				failed++; 
	    				continue; 
	    			}
	    			
	    			// Set Bukkit max health to our sentinel value
	    			var att = ((Attributable) le).getAttribute(Attribute.MAX_HEALTH);
	    			att.setBaseValue(1023.9);
	    			((Damageable) le).setHealth(att.getBaseValue());
	    			
	    			// Build the nameplate using the now-registered data
	    			MobNameplate.build(le, true);
	    			spawned++;
	    		}
	    		catch (Exception ex)
	    		{
	    			PrintUtils.OBSConsoleError("respawnAll: exception while processing key: " + key);
	    			ex.printStackTrace();
	    			failed++;
	    		}
	    	}
	    	
	    	file.delete();
	    	
	    	PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &o&bRespawnAll&r&f -- &aOK&f &7(OnEnable) " +
	    			"&a" + spawned + " spawned&7, " + (failed > 0 ? "&c" : "&a") + failed + " failed&f.");
	    }
	    finally
	    {
	    	// Always release the suppression flag, even if an exception escaped
	    	MobGenerateEvent.suppressSpawnInit = false;
	    }
	}

	public static void clearLegacyMobs()
	{
		int removed = 0;
		int skipped = 0;

		for (World w : Bukkit.getWorlds())
		{
			for (Entity e : w.getEntities())
			{
				if (!(e instanceof LivingEntity) || e instanceof Player) continue;

				if (!e.getPersistentDataContainer().has(MOB_DATA_KEY, PersistentDataType.STRING))
				{
					skipped++;
					continue;
				}

				MobData data = MobData.getMob(e.getUniqueId());
				if (data != null) data.kill();
				else e.remove();
				removed++;
			}
		}

		PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &o&bClearLegacyMobs&r&f -- &aOK&f" +
				" &c" + removed + " removed&7, &7" + skipped + " vanilla mobs untouched&f.");
	}
	
	public static void convertLegacyMobsTask(Plugin plugin)
	{
	    Bukkit.getScheduler().runTaskTimer(plugin, () ->
	    {
	        for (World w : Bukkit.getWorlds())
	        {
	            for (Player player : w.getPlayers())
	            {
	                for (Entity e : player.getNearbyEntities(100, 100, 100))
	                {
	                    if (!(e instanceof LivingEntity mob) || e instanceof Player) continue;
	                    if (MobData.getMob(mob.getUniqueId()) != null) continue;
	                    if (MobData.isDying(mob.getUniqueId())) continue;
	                    
	                    if (mob.getPersistentDataContainer().has(MOB_DATA_KEY, PersistentDataType.STRING))
	                    {
	                        MobData.loadMobData(mob);
	                        MobNameplate.build(mob, true);
	                        continue;
	                    }

	                    if (RayCastUtils.isEntityInFOV(player, mob, 90)) continue;

	                    MobData.convertLegacyMob(mob);
	                }
	            }
	        }
	    }, 0L, 20L);
	}
}