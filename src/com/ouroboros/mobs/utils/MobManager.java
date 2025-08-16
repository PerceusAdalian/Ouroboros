package com.ouroboros.mobs.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.PrintUtils;

public class MobManager 
{
	public static final NamespacedKey MOB_DATA_KEY = new NamespacedKey(Ouroboros.instance, "mob_data");
	
	public static void respawnAll() 
	{
		File file = new File(Ouroboros.instance.getDataFolder(), "mobs/serialized.yml");
		if (!file.exists()) return;
		
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection mobSection = config.getConfigurationSection("mobs");
		if (mobSection == null) return;
		
		for (String key : mobSection.getKeys(false)) 
		{
			String serialized = mobSection.getString(key);
			if (serialized == null) continue;
			
			MobData data = MobData.deserialize(serialized, Bukkit.getServer());
			if (data == null) 
			{
			    PrintUtils.OBSConsoleError("Failed to deserialize mob: " + key);
			    continue;
			}
			
			Location loc = data.getLocation();
			World w = loc.getWorld();
			if (w == null) continue;
			
			EntityType type = data.getEntityType();
			Entity e = w.spawnEntity(loc, type);

			MobData.setMobVisuals(e, data);
		}
		
		file.delete();
		
		PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &o&bRespawnAll&r&f -- &aOK&f &7(OnEnable)");
	}

	public static void despawnAll() 
	{
		File file = new File(Ouroboros.instance.getDataFolder(), "mobs/serialized.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		Set<String> activeMobUUIDs = new HashSet<>();
		
		for (World w : Bukkit.getWorlds()) 
		{
		    for (Entity e : w.getEntities()) 
		    {
		        if (!(e instanceof LivingEntity) || e instanceof Player) continue;        
		        if (!e.getPersistentDataContainer().has(MOB_DATA_KEY, PersistentDataType.STRING)) continue;

		        activeMobUUIDs.add(e.getUniqueId().toString());
		        
		        MobData mobData = MobData.getMob(e.getUniqueId());
		        if (mobData == null) continue;
		        
		        if (e.getLocation() != mobData.getLocation())
		        {
		        	mobData.setLocation(e.getLocation());
		        	e.getPersistentDataContainer().set(MOB_DATA_KEY, PersistentDataType.STRING, mobData.serialize());		        	
		        }
		        
		        String serialized = e.getPersistentDataContainer().get(MOB_DATA_KEY, PersistentDataType.STRING);
		        if (serialized == null) continue;
		        
		        config.set("mobs." + e.getUniqueId(), serialized);
		        
		        e.remove();
		    }
		}
		
		try 
		{
		    config.save(file);
		} 
		catch (IOException e) 
		{
		    PrintUtils.OBSConsoleError("Failed to save serialized mob data:");
		    e.printStackTrace();
		}
		
		File mobFolder = new File(Ouroboros.instance.getDataFolder(),"mobs/data");
        if (mobFolder.exists() && mobFolder.isDirectory())
        {
        	File[] files = mobFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        	if (files != null)
        	{
        		for (File f : files)
        			f.delete();
        	}
        }
		
		PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &o&bDespawnAll&r&f -- &aOK&f &7(OnDisable)");
	}
	
	@Deprecated
	public static void registerLegacyMobs()
	{
		for (World w : Bukkit.getWorlds())
		{
			for (Entity e : w.getEntities())
			{
				if (!(e instanceof LivingEntity) || e instanceof Player) continue;
				if (e.getPersistentDataContainer().has(MOB_DATA_KEY, PersistentDataType.STRING)) continue;
				
				Location loc = e.getLocation();
				EntityType eType = e.getType();				
				Entity newEntity = w.spawnEntity(loc, eType);
				e.remove();
				MobData mobData = MobData.loadMobData(newEntity);
				mobData.initialize(newEntity);
				MobData.setMobVisuals(newEntity, mobData);
			}
		}
	}
}
