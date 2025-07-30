package com.ouroboros.mobs;

import java.io.File;
import java.io.IOException;

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
			
			e.getPersistentDataContainer().set(MOB_DATA_KEY, PersistentDataType.STRING, serialized);
			e.getPersistentDataContainer().set(MobGenerateEvent.mobKey, PersistentDataType.STRING, data.getUUID().toString());
			
			MobData.loadMobData(e);
			MobData.setMobVisuals(e, data);
		}
		
		file.delete();
	}

	public static void despawnAll() 
	{
		File file = new File(Ouroboros.instance.getDataFolder(), "mobs/serialized.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		for (World w : Bukkit.getWorlds()) 
		{
		    for (Entity e : w.getEntities()) 
		    {
		        if (!(e instanceof LivingEntity) || e instanceof Player) continue;        
		        if (!e.getPersistentDataContainer().has(MOB_DATA_KEY, PersistentDataType.STRING)) continue;

		        MobData data = MobData.getMob(e.getUniqueId());
		        if (data == null) continue;
		        
		        data.setLocation(e.getLocation());
		        e.getPersistentDataContainer().set(MOB_DATA_KEY, PersistentDataType.STRING, data.serialize());
		        
		        String serialized = e.getPersistentDataContainer().get(MOB_DATA_KEY, PersistentDataType.STRING);
		        if (serialized == null) continue;
		        
		        config.set("mobs." + e.getUniqueId(), serialized);

		        data.deleteFile();

		        ObsMobHealthbar.removeBossBar(e);
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
	}
}
