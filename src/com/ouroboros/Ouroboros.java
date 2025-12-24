package com.ouroboros;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.lol.spells.SpellCastHandler;
import com.lol.spells.instances.SpellRegistry;
import com.ouroboros.abilities.AbilityCastHandler;
import com.ouroboros.abilities.AbilityRegistry;
import com.ouroboros.accounts.ExpHandler;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.store.ShopItemContainer;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.events.MobDamageEvent;
import com.ouroboros.mobs.events.MobDeathEvent;
import com.ouroboros.mobs.events.MobGenerateEvent;
import com.ouroboros.mobs.utils.MobManager;
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
		
		SpellCastHandler.register(instance);
		SpellRegistry.init();
		
		AbilityCastHandler.register(instance);
		AbilityRegistry.abilityInit();		
		
		ShopItemContainer.init();

		ObjectRegistry.itemInit();
		ObsObjectCastHandler.register(instance);
		
		MobManager.respawnAll();
		
		PrintUtils.OBSConsoleDebug("&fLoaded Abilities -- &e"+AbilityRegistry.abilityRegistry.size());
		PrintUtils.OBSConsolePrint("&fLoaded Spells -- &d"+SpellRegistry.spellRegistry.size());
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
 * >> IDEA: Make a portable bag interface that connects with the player shop. Allow for buttons and item slots to insert items into that is tied to the player file, or is a serialized string alongside
 			the item's PDC. You can "clear" the bag which would give you item value/a different kind of stat alongside OBS's stat system: Scrap. Where depending on the item, will give you money,
 			effectively "selling" all of the items and adding currency. You can transfer the bag, the item(s) are simply stored on a pdc or, finding the data from one player, checking to see if it's
			picked up by another player, getting their player data and then transfering the serialized string to their player file. A player can only have 1 bag attached to their account (linked).
   			Maybe mess around with item stacks to add a method to add infinite of some x item and then apply a means to fetch the item(s) at that index/cell.
	  		Item Name: <PH> Bag of Holding ... maybe this could be an EOL in and of itself as a relic item???
 * + WHAT TO DO NEXT:
 * > HIGH PRIORITY: 
 * - CHECK REAP AND SEW; TEST IMBUE FIRE DAMAGE EVENT.
 * > Side High Priority: 
 * - Make items drop from discovery event depending on their level.
 * - Combat abilities do not display HP bar decreases; fix the update method. (FIXED????)
 * - Add GUI framework for each stat and representation. (UNDERWAY)
 *   > Make a reward system for leveling up stats every 10 levels. 
 *   > Implement prestige system.
 *   > Low Priority: Stat descriptions, a means to see next levelup rewards, abilities, etc.
 * - Make more abilities and implement them; now's the time to get creative!! <3
 *   > Low Priority: Make ability upgrades
 * - Implement Passive Perks! (DONE..Sort of)..
 */
///kill @e[type=!minecraft:player]
///setblock ~ ~ ~ minecraft:chest{LootTable:"minecraft:chests/simple_dungeon"} replace
/*
 	Random index method:
	int randomIndex = new Random().nextInt(list.size()); <- Given some collection. 
 */