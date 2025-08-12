package com.ouroboros;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
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
import com.ouroboros.mobs.MobManager;
import com.ouroboros.objects.ObjectDropHandler;
import com.ouroboros.objects.ObjectRegistry;
import com.ouroboros.objects.ObsObjectCastHandler;
import com.ouroboros.utils.PrintUtils;

public class Ouroboros extends JavaPlugin
{
	public static Ouroboros instance; 
	public static boolean debug;
	private static boolean enabled = false;
	
	public static boolean isActive() 
	{ 
		return enabled;
	}
	
	@Override
	public void onEnable()
	{
		instance = this;
		debug = false;
		enabled = true;
		
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
		
		MobManager.respawnAll();
		
		PrintUtils.OBSConsolePrint("&fOuroboros -- &aOK");
	}
	
	@Override
	public void onDisable() 
	{
		enabled = false;
		
		HandlerList.unregisterAll(this);
		PlayerData.saveAll();
		MobData.saveAll();
		MobManager.despawnAll();
		for (World w : Bukkit.getWorlds()) 
		{
			for (Entity e : w.getEntities())
			{
				Set<EntityType> invalidMobs = Set.of(EntityType.PLAYER, EntityType.ENDER_DRAGON, EntityType.WITHER,EntityType.WARDEN,EntityType.ALLAY,EntityType.VILLAGER,EntityType.IRON_GOLEM);
				if (!(e instanceof LivingEntity le) || invalidMobs.contains(le.getType())) continue;
				le.remove();
			}
		}
		PrintUtils.OBSConsolePrint("&fOuroboros -- &b&oDisabling..");
	}
}

//Fix combat ability not changing/displaying pages properly; it won't override the previous combat ability, and will just add more to the file. 

/* Ωuroboros
 * Project Notes:
 * + WHAT TO DO NEXT:
 * > HIGH PRIORITY: 
 * - MobData persistence is broken across restarts. Find some way to load/unload the data, as right now, it's not *truly* persistent. 
 * > Side High Priority: 
 * - Only allow 1 combat ability to be active at a time. 
 * - Add GUI framework for each stat and representation.
 *   > Make a reward system for leveling up stats every 10 levels. 
 *   > Implement prestige system.
 *   > Low Priority: Stat descriptions, a means to see next levelup rewards, abilities, etc.
 * - Make more abilities and implement them; now's the time to get creative!! <3
 *   > Low Priority: Make ability upgrades
 * - Implement Passive Perks!
 */