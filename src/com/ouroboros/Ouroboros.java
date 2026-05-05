package com.ouroboros;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.eol.echoes.EchoHeldEvent;
import com.eol.echoes.EchoManager;
import com.eol.echoes.abilities.AbilityCastHandler;
import com.eol.echoes.abilities.AbilityRegistry;
import com.eol.echoes.config.EchoConfig;
import com.eol.echoes.instances.EOLRegistry;
import com.eol.materia.Materia;
import com.eol.materia.MateriaCastHandler;
import com.eol.materia.components.Bases;
import com.eol.materia.components.Bindings;
import com.eol.materia.components.Catalysts;
import com.eol.materia.components.ElementCores;
import com.lol.spells.SpellCastHandler;
import com.lol.spells.SpellRegistry;
import com.lol.spells.instances.aero.Fly;
import com.lol.spells.instances.aero.Tailwind;
import com.lol.spells.instances.arcano.Sigil;
import com.lol.spells.instances.celestio.AssertOrder;
import com.lol.spells.instances.celestio.Pneuma;
import com.lol.spells.instances.cosmo.ArrestoMomentum;
import com.lol.spells.instances.cosmo.Teleport;
import com.lol.spells.instances.cosmo.Warp;
import com.lol.wand.WandRegistry;
import com.ouroboros.accounts.ExpHandler;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.store.ShopItemContainer;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.Outbreak;
import com.ouroboros.mobs.events.MobDamageEvent;
import com.ouroboros.mobs.events.MobDeathEvent;
import com.ouroboros.mobs.events.MobGenerateEvent;
import com.ouroboros.mobs.utils.MobManager;
import com.ouroboros.mobs.utils.MobNameplate;
import com.ouroboros.objects.BowLimbRecipe;
import com.ouroboros.objects.ManaGemRecipe;
import com.ouroboros.objects.ObjectRegistry;
import com.ouroboros.objects.ObsObjectCastHandler;
import com.ouroboros.objects.TrainingWandRecipe;
import com.ouroboros.utils.PrintUtils;

public class Ouroboros extends JavaPlugin
{
	public static Ouroboros instance; 
	public static boolean debug;
	public static boolean debugSpells;
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
		debugSpells = false;
		enabled = true;
		
		this.getCommand("obs").setExecutor(new ObsCommand());;
		
		EchoConfig.load();
		
		PlayerData.initializeDataFolder();
		MobData.initializeDataFolder();	
		GeneralEvents.register(instance);
		EchoManager.registerEventHelpers(instance);
		MobGenerateEvent.register(instance);
		MobDeathEvent.register(instance);
		MobDamageEvent.register(instance);
		
		MobManager.clearLegacyMobs();
		MobManager.respawnAll();
		MobNameplate.registerTaskHandler(instance);
		MobManager.convertLegacyMobsTask(instance);
		
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
		ManaGemRecipe.init();
		ManaGemRecipe.register();
		BowLimbRecipe.init();
		BowLimbRecipe.register();
		ObjectRegistry.itemInit();
		ObsObjectCastHandler.register(instance);
		
		ArrestoMomentum.registerSpellHelper(instance);
		AssertOrder.registerSpellHelper(instance);
		Pneuma.registerSpellHelper(instance);
		Tailwind.registerSpellHelper(instance);
		Teleport.registerSpellHelper(instance);
		Warp.registerSpellHelper(instance);
		Fly.registerSpellHelper(instance);
		Sigil.registerSpellHelper(instance);
		
		MateriaCastHandler.register(instance);
		Catalysts.load();
		Bases.load();
		Bindings.load();
		ElementCores.load();
		Materia.convertItemsTask(instance);
		Materia.registerRefinementHelper(instance);
		EOLRegistry.itemInit();
		
		Outbreak.registerRandomOutbreakTask(instance);
		
		EchoHeldEvent.register(instance);
		EchoHeldEvent.queueBuffTask(instance);
		
		PrintUtils.OBSConsoleDebug("&fLoaded Abilities -- &e"+AbilityRegistry.abilityRegistry.size());
		PrintUtils.OBSConsolePrint("&fLoaded Spells -- &d"+SpellRegistry.spellRegistry.size());
		PrintUtils.OBSConsolePrint("&fLoaded Materia -- &e&l"+Materia.materia_registry.size());
		PrintUtils.OBSConsolePrint("&fLoaded &eEOLs&f -- &e&l"+EOLRegistry.registry.size());
		
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
 * High Priority: Fix force breaking
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