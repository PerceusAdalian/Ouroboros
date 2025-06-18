package com.ouroboros.accounts;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.ouroboros.Ouroboros;

public class PlayerData 
{
	public static void register(Player p) 
	{
		File file = new File(Ouroboros.instance.getDataFolder() + "/playerdata", p.getUniqueId() + ".yml");
	    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

	    if (!file.exists()) 
	    {
	        config.set("uuid", p.getUniqueId().toString());
	        config.set("name", p.getName());

	        // General Levels
	        config.set("stats.generic", 0);
	        config.set("stats.crafting", 0);
	        config.set("stats.alchemy", 0);
	        config.set("stats.travel", 0);
	        config.set("stats.woodcutting", 0);
	        config.set("stats.mining", 0);
	        config.set("stats.fishing", 0);

	        // Combat Levels
	        config.set("stats.combat.melee", 0);
	        config.set("stats.combat.ranged", 0);
	        config.set("stats.combat.magic", 0);
	        
	        // General Stat Experience
	        config.set("experience.generic",0);
	        config.set("experience.crafting",0);
	        config.set("experience.alchemy",0);
	        config.set("experience.travel",0);
	        config.set("experience.woodcutting",0);
	        config.set("experience.mining",0);
	        config.set("experience.fishing",0);
	        
	        // Combat Stat Experience
	        config.set("experience.combat.melee",0);
	        config.set("experience.combat.ranged",0);
	        config.set("experience.combat.magic",0);

	        try 
	        {
	            config.save(file);
	        } 
	        catch (IOException err) 
	        {
	            err.printStackTrace();
	        }
	    }
	    return;
	}
	
	public static void save(Player p) 
	{
		File file = new File(Ouroboros.instance.getDataFolder() + "/playerdata", p.getUniqueId() + ".yml");

	    if (!file.exists()) return;

	    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

	    try 
	    {
	        config.save(file);
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	
}

//	        config.set("stats.vitality", 0);
//	        config.set("stats.strength", 0);
//	        config.set("stats.intellect", 0);
//	        config.set("stats.dexterity", 0);
//	        config.set("stats.agility", 0);