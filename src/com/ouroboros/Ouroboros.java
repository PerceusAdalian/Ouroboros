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
 * <DONE ITEMS>
 * - Add a command to reset stats in playerdata. <DONE>
 * - Begin working on xp gain systems. <DONE>
 * - Implement Combat XP gain. <DONE>
 * + WHAT TO DO NEXT:
 * - Implement Alchemy XP gain. 
 * - Stop crafting event from over giving xp: e.g. max stack size on cursor, not enough to merge on cursor, giving infinite xp gain. 
 *      Try: cloning the itemstack, removing result from matrix, and moving it to main inventory, or dropping if full.
 * - Add GUI framework for each stat and representation.
 * - Make a means to manipulate account level; framework on that.
 *      i.e. Every 10 levels of any stat, add 1 account level. 
 *      	 OR Every skill point used, +1 account level. 
 *      	 OR Every 1 prestige (This is a stretch), add an account level.
 * - Begin drafting a means to model and implement abilities.
 * 		i.e. Through YAML, or TOML (Hard route), model abilities and implement access and event calls. 
 * 			 Abilities such as adding more xp to xp gain events. 
 * 			 Abilities such as seen in combat (Fire arrows, Flame strikes, or more damage to a certain entity type, etc.)
 */
