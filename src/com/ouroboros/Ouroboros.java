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
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.events.MobDamageEvent;
import com.ouroboros.mobs.events.MobDeathEvent;
import com.ouroboros.mobs.events.MobGenerateEvent;
import com.ouroboros.mobs.utils.MobManager;
import com.ouroboros.objects.ObjectRegistry;
import com.ouroboros.objects.ObsObjectCastHandler;
import com.ouroboros.objects.instances.EolBlazeArm;
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
		EolBlazeArm.registerHeldEvent(instance);
		
		ShopItemContainer.init();

		ObjectRegistry.itemInit();
		ObsObjectCastHandler.register(instance);
		
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

/* Ωuroboros
 * Project Notes:
 * + WHAT TO DO NEXT:
 * > HIGH PRIORITY: 
 * - Finish Hooking up damage types and affinity calcs; start with EntityEffects and implement the rest of the effects, next, go to ability cast handler and implement ability damage.
 * > Side High Priority: 
 * - Make items drop from discovery event depending on their level.
 * - Mob affinities and damage category damage (make cows resistant to CELESTIO damage, but weak to MORTIO damage, for example)
 * - Combat abilities do not display HP bar decreases; fix the update method.
 * - Add GUI framework for each stat and representation.
 *   > Make a reward system for leveling up stats every 10 levels. 
 *   > Implement prestige system.
 *   > Low Priority: Stat descriptions, a means to see next levelup rewards, abilities, etc.
 * - Make more abilities and implement them; now's the time to get creative!! <3
 *   > Low Priority: Make ability upgrades
 * - Implement Passive Perks! (DONE..Sort of)..
 */
///kill @e[type=!minecraft:player]
///setblock ~ ~ ~ minecraft:chest{LootTable:"minecraft:chests/simple_dungeon"} replace