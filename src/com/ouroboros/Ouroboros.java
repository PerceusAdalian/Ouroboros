package com.ouroboros;

import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.abilities.AbilityCastHandler;
import com.ouroboros.abilities.AbilityRegistry;
import com.ouroboros.ability.instances.combat.ImbueFire;
import com.ouroboros.accounts.ExpHandler;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.store.ShopItemContainer;
import com.ouroboros.mobs.MobDamageEvent;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.MobDeathEvent;
import com.ouroboros.mobs.MobGenerateEvent;
import com.ouroboros.objects.ObjectDropHandler;
import com.ouroboros.objects.ObjectRegistry;
import com.ouroboros.objects.ObsObjectCastHandler;
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
		MobData.initializeDataFolder();
		
		GeneralEvents.register(instance);
		MobGenerateEvent.register(instance);
		MobDeathEvent.register(instance);
		MobDamageEvent.register(instance);
		
		GuiHandler.registerEvent(instance);
		ExpHandler.register(instance);
		
		AbilityCastHandler.register(instance);
		AbilityRegistry.abilityInit();		
		ImbueFire.registerCleanupHandler(instance);		
		
		ShopItemContainer.init();

		ObjectRegistry.itemInit();
		ObsObjectCastHandler.register(instance);
		ObjectDropHandler.register(instance);
		
		PrintUtils.OBSConsolePrint("&fOuroboros -- &aOK");
	}
	
	@Override
	public void onDisable() 
	{
		PlayerData.saveAll();
		MobData.saveAll();
		PrintUtils.OBSConsolePrint("&fOuroboros -- &b&oDisabling..");
	}
}

/* Ωuroboros
 * Project Notes:
 * + WHAT TO DO NEXT:
 * > HIGH PRIORITY: Only allow 1 combat ability to be active at a time. 
 * > Side High Priority: 
 *     1. Merge Mob Generation from EOL into Ouroboros for full integration. >>>>> ALMOST DONE!! Just write the damage event and compile + test
 * - Add GUI framework for each stat and representation.
 *   > Make a reward system for leveling up stats every 10 levels. 
 *   > Implement prestige system.
 *   > Low Priority: Stat descriptions, a means to see next levelup rewards, abilities, etc.
 * - Make more abilities and implement them; now's the time to get creative!! <3
 *   > Low Priority: Make ability upgrades
 * - Implement Passive Perks!
 */