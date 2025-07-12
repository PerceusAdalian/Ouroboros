package com.ouroboros;

import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.abilities.AbilityCastHandler;
import com.ouroboros.abilities.AbilityRegistry;
import com.ouroboros.ability.instances.combat.ImbueFire;
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
		
		PlayerData.initializeDataFolder();
		
		GeneralEvents.register(instance);
		GuiHandler.registerEvent(instance);
		ExpHandler.register(instance);
		AbilityCastHandler.register(instance);
		AbilityRegistry.abilityInit();		
		ImbueFire.registerCleanupHandler(instance);		

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
 * > HIGH PRIORITY: Only allow 1 combat ability to be active at a time. 
 * - Add GUI framework for each stat and representation.
 *   > Make a reward system for leveling up stats every 10 levels. 
 *   > Implement prestige system.
 *   > Low Priority: Stat descriptions, a means to see next levelup rewards, abilities, etc.
 * - Make more abilities and implement them; now's the time to get creative!! <3
 *   > Low Priority: Make ability upgrades
 * - Implement Passive Perks!
 */
