package com.ouroboros;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.eol.materia.MateriaCastHandler;
import com.eol.materia.instances.Materia;
import com.eol.materia.instances.components.Bases;
import com.eol.materia.instances.components.Bindings;
import com.eol.materia.instances.components.Catalysts;
import com.eol.materia.instances.components.ElementCores;
import com.lol.spells.SpellCastHandler;
import com.lol.spells.instances.SpellRegistry;
import com.lol.spells.instances.celestio.AssertOrder;
import com.lol.spells.instances.celestio.Pneuma;
import com.lol.spells.instances.cosmo.ArrestoMomentum;
import com.lol.wand.WandRegistry;
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
import com.ouroboros.mobs.utils.MobNameplate;
import com.ouroboros.objects.ObjectRegistry;
import com.ouroboros.objects.ObsObjectCastHandler;
import com.ouroboros.objects.TrainingWandRecipe;
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
		
		WandRegistry.load();
		
		AbilityCastHandler.register(instance);
		AbilityRegistry.abilityInit();		
		
		ShopItemContainer.init();

		TrainingWandRecipe.init();
		TrainingWandRecipe.register();
		
		ObjectRegistry.itemInit();
		ObsObjectCastHandler.register(instance);
		
		ArrestoMomentum.registerSpellHelper(instance);
		AssertOrder.registerSpellHelper(instance);
		Pneuma.registerSpellHelper(instance);
		
		MobManager.clearLegacyMobs();
		MobManager.respawnAll();
		MobNameplate.registerTaskHandler(instance);
		MobManager.convertLegacyMobsTask(instance);
		
		MateriaCastHandler.register(instance);
		Catalysts.load();
		Bases.load();
		Bindings.load();
		ElementCores.load();
		
		PrintUtils.OBSConsoleDebug("&fLoaded Abilities -- &e"+AbilityRegistry.abilityRegistry.size());
		PrintUtils.OBSConsolePrint("&fLoaded Spells -- &d"+SpellRegistry.spellRegistry.size());
		PrintUtils.OBSConsolePrint("&fLoaded Materia -- &e&l"+Materia.materia_registry.size());
		PrintUtils.OBSConsolePrint("&fΩuroboros -- &aOK");
	}
	
	@Override
	public void onDisable() 
	{
		enabled = false;
		
		HandlerList.unregisterAll(this);
		PlayerData.saveAll();
		MobData.saveAll();
		MobManager.despawnAll();
		PrintUtils.OBSConsolePrint("&fΩuroboros -- &b&oDisabling..");
	}
}

/* Ωuroboros // Echoes of Lumina // Legends of Luminus
 * Concept: Σ.C.H.O. (Engram Conversion to Harmonic Object) Protocol
 * Project Notes:
  * + WHAT TO DO NEXT:
 * - Make items drop in rarity respective to the mob's level.
 * - Add GUI framework for each stat and representation.
 *   > Make a reward system for leveling up stats every 10 levels. 
 *   > Implement prestige system.
 *   > Low Priority: Stat descriptions, a means to see next levelup rewards, abilities, etc.
 * - Make more abilities.
 *   > Low Priority: Make ability/spell upgrades
 * - Rework passives (perks)
 */
///kill @e[type=!minecraft:player]
///setblock ~ ~ ~ minecraft:chest{LootTable:"minecraft:chests/simple_dungeon"} replace
/*
 	Random index method:
	int randomIndex = new Random().nextInt(list.size()); <- Given some collection. 
 */