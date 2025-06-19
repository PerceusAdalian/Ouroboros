package com.ouroboros.accounts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.StatType;

public class PlayerData 
{
	protected final UUID uuid;
	private final File file;
	private final YamlConfiguration config;
	
	private static final Map<UUID, PlayerData> dataMap = new HashMap<>();
	
	public PlayerData(UUID uuid) 
	{
		this.uuid = uuid;
		this.file = new File(getDataFolder(), "playerdata/"+uuid+".yml");
		this.config = YamlConfiguration.loadConfiguration(file);
		
		if (!file.exists()) 
		{
			setDefaults();
			save();
		}
	}
	
	public static PlayerData getPlayer(UUID uuid) 
	{
	    return dataMap.get(uuid); 
	}
	
	public void setDefaults() 
	{
	    if (!file.exists()) 
	    {
	    	setAccountLevel(0);
	    	
	        // General Levels
	    	setStat(StatType.CRAFTING, true, 0);
	    	setStat(StatType.ALCHEMY, true, 0);
	    	setStat(StatType.TRAVEL, true, 0);
	    	setStat(StatType.WOODCUTTING, true, 0);
	    	setStat(StatType.MINING, true, 0);
	    	setStat(StatType.FISHING, true, 0);
	    	
	        // Combat Levels
	    	setStat(StatType.MELEE, true, 0);
	    	setStat(StatType.RANGED, true, 0);
	    	setStat(StatType.MAGIC, true, 0);
	    	
	        // General Stat Experience
	    	setStat(StatType.CRAFTING, false, 0);
	    	setStat(StatType.ALCHEMY, false, 0);
	    	setStat(StatType.TRAVEL, false, 0);
	    	setStat(StatType.WOODCUTTING, false, 0);
	    	setStat(StatType.MINING, false, 0);
	    	setStat(StatType.FISHING, false, 0);
	    	
	        // Combat Stat Experience
	    	setStat(StatType.MELEE, false, 0);
	    	setStat(StatType.RANGED, false, 0);
	    	setStat(StatType.MAGIC, false, 0);
	    	
	    	setAbilityPoints(0);
	    	setPrestigePoints(0);
	    }
	    return;
	}
	
	/**
	 * @param sType The type of stat to get. See StatType.java
	 * @param getLevel Toggle whether getStat returns the level (true) or experience (false).
	 */
	public int getStat(StatType sType, boolean getLevel) 
	{
		if (getLevel) return config.getInt("stats."+sType.getKey());
		else return config.getInt("experience."+sType.getKey());
	}
	
	/**
	 * @param sType The type of stat to set. See StatType.java
	 * @param setLevel Toggle whether to set stat level (true) or experience (false).
	 * @param value Must be 0 < value < 100. Sets to existing value if not in range.
	 */
	public void setStat(StatType sType, boolean setLevel, int value) 
	{
		String path = setLevel ? "stats." + sType.getKey() : "experience." + sType.getKey();
	    config.set(path, Math.max(0, Math.min(100, value))); // clamped 0–100
	}
	
	public int getAccountLevel() 
	{
		return config.getInt("stats.accountlevel");
	}
	
	public void setAccountLevel(int value) 
	{
		config.set("stats.accountlevel", value);
	}
	
	public int getAbilityPoints() 
	{
		return config.getInt("points.ability");
	}
	
	public void setAbilityPoints(int value) 
	{
		config.set("points.ability", value);
	}
	
	public int getPrestigePoints() 
	{
		return config.getInt("points.pretige");
	}
	
	public void setPrestigePoints(int value) 
	{
		config.set("points.prestige", value);
	}
	
	public void save() 
	{
		try 
		{
			config.save(file);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void saveAll() 
	{
	    for (PlayerData data : dataMap.values()) 
	    {
	        data.save();
	    }
	}

	
	public void reload() throws FileNotFoundException, IOException, InvalidConfigurationException
	{
		try 
		{
			config.load(file);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (InvalidConfigurationException e) 
		{
			e.printStackTrace();
		}
	}
	
	//Loads a player into a static data cache to finalize data later.
	public static void loadPlayer(UUID uuid) 
	{
	    if (!dataMap.containsKey(uuid)) 
	    {
	        dataMap.put(uuid, new PlayerData(uuid));
	    }
	}
	
	//Unloads the player from the data cache, and properly saves to file.
	public static void unloadPlayer(UUID uuid) 
	{
	    PlayerData data = dataMap.remove(uuid);
	    if (data != null) 
	    {
	        data.save();
	    }
	}
	
	public static void initializeDataFolder() 
	{
		File playerDataFolder = new File(getDataFolder(), "playerdata");
	    if (!playerDataFolder.exists()) playerDataFolder.mkdirs();
	}
	
	public static File getDataFolder() 
	{
		return Ouroboros.instance.getDataFolder();
	}
}