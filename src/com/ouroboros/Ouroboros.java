package com.ouroboros;

import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.accounts.ExpHandler;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.PrintUtils;

public class Ouroboros extends JavaPlugin
{
	public static Ouroboros instance; 
	public static boolean debug;
	
	@Override
	public void onEnable()
	{
		instance = this;
		debug = false;
		
		this.getCommand("obs").setExecutor(new ObsCommand());;
		
		GuiHandler.registerEvent(instance);
		GeneralEvents.register(instance);
		ExpHandler.register(instance);
		
		PlayerData.initializeDataFolder();
		
		PrintUtils.OBSConsolePrint("&fOuroboros -- &aOK");
	}
	
	@Override
	public void onDisable() 
	{
		PlayerData.saveAll();
		
		PrintUtils.OBSConsolePrint("&fOuroboros -- &b&oDisabling..");
	}
}

/*
 * Project Notes:
 * + WHAT TO DO NEXT:
 * - Add GUI framework for each stat and representation.
 *   > Make a reward system for leveling up stats every 10 levels. 
 *   > Implement prestige system.
 *   > Low Priority: Stat descriptions, a means to see next levelup rewards, abilities, etc.
 *  >> Begin drafting a means to model and implement abilities.
 * 		i.e. Through YAML, or TOML (Hard route), model abilities and implement access and event calls. 
 * 			 Abilities such as adding more xp to xp gain events. 
 * 			 Abilities such as seen in combat (Fire arrows, Flame strikes, or more damage to a certain entity type, etc.)
 */
