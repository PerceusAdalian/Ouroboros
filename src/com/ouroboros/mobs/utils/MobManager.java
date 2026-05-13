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

public class MobManager 
{
	public static final NamespacedKey MOB_DATA_KEY = new NamespacedKey(Ouroboros.instance, "mob_data");
	public static final NamespacedKey MOB_UUID_KEY = new NamespacedKey(Ouroboros.instance, "mob_uuid");
	
	public static void despawnAll()
	{
	    File file = new File(Ouroboros.instance.getDataFolder(), "mobs/serialized.yml");
	    YamlConfiguration config = new YamlConfiguration();
	    int saved = 0;

	    for (World w : Bukkit.getWorlds())
	    {
	        for (Entity e : w.getEntities())
	        {
	            if (!(e instanceof LivingEntity) || e instanceof Player) continue;
	            if (!e.getPersistentDataContainer().has(MOB_DATA_KEY, PersistentDataType.STRING)) continue;

	            MobData data = MobData.getMob(e.getUniqueId());
	            if (data == null) continue;

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
	        PrintUtils.OBSConsoleError("Failed to save serialized mob data:");
	        error.printStackTrace();
	    }

	    PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &o&bDespawnAll&r&f -- &aOK&f &7(OnDisable) &a" + saved + " mobs saved.");
	}

	public static void respawnAll()
	{
		MobGenerateEvent.suppressSpawnInit = true;
	    try
	    {
	    	
	    	File file = new File(Ouroboros.instance.getDataFolder(), "mobs/serialized.yml");
	    	if (!file.exists()) return;
	    	
	    	YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
	    	ConfigurationSection mobSection = config.getConfigurationSection("mobs");
	    	if (mobSection == null) return;
	    	
	    	int spawned = 0;
	    	int failed  = 0;
	    	
	    	for (String key : mobSection.getKeys(false))
	    	{
	    		String serialized = mobSection.getString(key);
	    		if (serialized == null) 
	    		{ 
	    			failed++; 
	    			continue; 
	    		}
	    		
	    		String[] segments = serialized.split("\\|");
	    		if (segments.length != 10) 
	    		{ 
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
	    				failed++; 
	    				continue; 
	    			}
	    			
	    			MobData data = MobData.rehydrate(le, serialized, Bukkit.getServer());
	    			if (data == null) 
	    			{ 
	    				le.remove(); 
	    				failed++; 
	    				continue; 
	    			}
	    			
	    			var att = ((Attributable) le).getAttribute(Attribute.MAX_HEALTH);
	    			att.setBaseValue(1023.9);
	    			((Damageable) le).setHealth(att.getBaseValue());
	    			
	    			LivingEntity leFinal = le;
	    			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	    			    MobNameplate.build(leFinal, true), 1L);
	    			
	    			spawned++;
	    		}
	    		catch (Exception ex)
	    		{
	    			PrintUtils.OBSConsoleError("Failed to respawn mob: " + key);
	    			ex.printStackTrace();
	    			failed++;
	    		}
	    	}
	    	
	    	file.delete();
	    	
	    	PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &o&bRespawnAll&r&f -- &aOK&f &7(OnEnable) " +
	    			"&a" + spawned + " spawned&7, " + (failed > 0 ? "&c" : "&a") + failed + " failed");
	    }
	    finally
	    {
	    	MobGenerateEvent.suppressSpawnInit = false;
	    }
	}

	public static void clearLegacyMobs()
	{
		for (World w : Bukkit.getWorlds())
		{
			for (Entity e : w.getEntities())
			{
				if (!(e instanceof LivingEntity) || e instanceof Player) continue;
				if (MobData.getMob(e.getUniqueId()) != null) MobData.getMob(e.getUniqueId()).kill();
				else e.remove();
			}
		}
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
	                    if (player.hasLineOfSight(mob)) continue;

	                    MobData.convertLegacyMob(mob);
	                }
	            }
	        }
	    }, 0L, 40L); // 40 ticks = 2 seconds, much cheaper
	}
	
}
