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
 * - Add a command to reset stats in playerdata. <DONE>
 * - Begin working on xp gain systems. <DONE>
 * - Add GUI framework for each stat and representation.
 * + WHAT TO DO NEXT:
 * - Make a means to manipulate account level; framework on that.
 * - Implement Combat XP gain.
 * - Implement Alchemy XP gain.
 */
