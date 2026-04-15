package com.ouroboros;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BundleMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.eol.echoes.EchoForge;
import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaState;
import com.eol.enums.MateriaType;
import com.eol.materia.Materia;
import com.lol.enums.SpellType;
import com.lol.spells.SpellRegistry;
import com.lol.spells.instances.Spell;
import com.lol.wand.Wand;
import com.ouroboros.abilities.AbilityRegistry;
import com.ouroboros.abilities.instances.ObsAbility;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.accounts.PlayerHud;
import com.ouroboros.enums.StatType;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsMainMenu;
import com.ouroboros.menus.instances.magic.AdminSpellsPage;
import com.ouroboros.menus.instances.magic.CollectWandData;
import com.ouroboros.menus.instances.magic.SpellBookMainPage;
import com.ouroboros.menus.instances.magic.WandMainPage;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.MobSummoner;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.objects.ObjectRegistry;
import com.ouroboros.objects.instances.LuminiteCore;
import com.ouroboros.objects.instances.ObsStatVoucher;
import com.ouroboros.objects.instances.TearOfLumina;
import com.ouroboros.utils.EntityCategories;
import com.ouroboros.utils.InventoryUtils;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ObsCommand implements CommandExecutor, TabCompleter
{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) 
	{
		if (!(sender instanceof Player)) 
		{
			return false;
		}
		
		Player p = (Player) sender;
		UUID uuid = p.getUniqueId();
		if (args.length == 0) 
		{
			PrintUtils.OBSFormatError(p, "&7Invalid Argument(s)");
			return false;
		}
		
		//--------------------------------- GENERAL COMMANDS ---------------------------------//
		
		if (args[0].equals("welcomekit"))
		{
			PlayerData data = PlayerData.getPlayer(uuid);
			
			if (data.hasKitClaimed())
			{
				PrintUtils.OBSFormatPrint(p, "You have already claimed this kit!");
				return true;
			}
			
			AbstractObsObject voucherObj = new ObsStatVoucher();
			ItemStack voucherStack = voucherObj.toItemStack();
			
			ItemMeta meta = voucherStack.getItemMeta();
			meta.getPersistentDataContainer().set(ObsStatVoucher.voucherKey, PersistentDataType.STRING, p.getUniqueId().toString());
			voucherStack.setItemMeta(meta);
			
			AbstractObsObject luminiteCoreObj = new LuminiteCore();
			ItemStack luminiteCoreStack = luminiteCoreObj.toItemStack();

			AbstractObsObject luminiteTear = new TearOfLumina();
			ItemStack luminiteTearStack = luminiteTear.toItemStack();
			luminiteTearStack.setAmount(10);
			
			ItemStack bag = new ItemStack(Material.BUNDLE, 1);
			BundleMeta bagMeta = (BundleMeta) bag.getItemMeta();
			bagMeta.addItem(luminiteCoreStack);
			bagMeta.addItem(voucherStack);
			bagMeta.addItem(luminiteTearStack);
			bagMeta.addItem(Wand.get("wand_1").getAsItemStack());
			bagMeta.setDisplayName(PrintUtils.ColorParser("&bOurboros&f Welcome Kit"));
			bag.setItemMeta(bagMeta);
			
			Set<Integer> openSlots = InventoryUtils.getOpenSlots(p);
			Iterator<Integer> it = openSlots.iterator();
			
			if (openSlots.size() >= 1)
			{
				EntityEffects.playSound(p, Sound.ITEM_BUNDLE_DROP_CONTENTS, SoundCategory.AMBIENT);
				PrintUtils.OBSFormatPrint(p, "&fA kit has been added to your inventory!");
				p.getInventory().setItem(it.next(), bag);
			}
			else
			{
				PrintUtils.OBSFormatPrint(p, "&fInventory full. A kit has been dropped on the ground!");
				p.getWorld().dropItem(p.getLocation(), bag);
			}
			
			if (!p.isOp()) data.setKitClaimed(true);
			data.save();
			
			return true;
		}
		
		if (args[0].equals("recoverwand"))
		{
			if (!CollectWandData.wandCollector.containsKey(p.getUniqueId()))
			{
				PrintUtils.OBSFormatError(p, "You don't currently have a wand in need of recovery.");
				return true;
			}
			Wand wand = CollectWandData.wandCollector.get(p.getUniqueId());
			CollectWandData.wandCollector.remove(p.getUniqueId());
			ItemStack stack = wand.getAsItemStack();
			p.getInventory().addItem(stack);
			PrintUtils.OBSFormatDebug(p, "You wand was regenerated successfully. Check your inventory.");
			return true;
		}
		
		if (args[0].equals("menu")) 
		{
			if (Ouroboros.debug) PrintUtils.OBSConsoleDebug("&bPlayer&f: " + sender.getName().toString() + "&f opened the OBS Main Menu.");
			GuiHandler.open(p, new ObsMainMenu(p));
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			return true;
		}
		
		if (args[0].equals("spellbook"))
		{
			GuiHandler.open(p, new SpellBookMainPage(p));
			OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 3, 10, 0.5, Particle.CLOUD, null);
			OBSParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 10, 2, 0.5, Particle.ENCHANT, null);
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.AMBIENT);
			return true;
		}
		
		if (args[0].equals("wand"))
		{
			GuiHandler.open(p, new WandMainPage(p));
			OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 3, 10, 0.5, Particle.CLOUD, null);
			OBSParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 10, 2, 0.5, Particle.ENCHANT, null);
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.AMBIENT);
			return true;
		}
		
		//--------------------------------- OP ONLY COMMANDS ---------------------------------//
		
		if (args[0].equals("version")) 
		{
			if (affirmOP(p)) return true;
			
			// Player is confirmed OP at this point; print directly.
			PrintUtils.Print(p, "----------------------------",
					"                 &b&lOBS",
					" &f&l- &r&fSystem Time: "+LocalTime.now(),
					" &f&l- &r&a&lSystem Version&r&f: &7{&c&lALPHA&f&7}",
					" &f&l- &r&dAPI Version&r&f: "+Bukkit.getVersion().toString(),
					" &f&l- &r&fPlugins Loaded: &e&l" + Bukkit.getPluginManager().getPlugins().length,
					"----------------------------");
			return true;
		}
		
		if (args[0].equals("adminspells"))
		{
			if (affirmOP(p)) return true;
			SpellRegistry.spellRegistry.values().stream()
		    .filter(spell -> spell.getSpellType() == SpellType.DEBUG)
		    .forEach(spell -> 
		    {
		        PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		        if (data != null) data.getSpell(spell).setRegistered(true);
		    });
			
			GuiHandler.open(p, new AdminSpellsPage(p));
			return true;
		}
		
		if (args[0].equals("clearmobs"))
		{
			if (affirmOP(p)) return true;
			if (Ouroboros.debug == false) return true;
			for (World w : Bukkit.getWorlds())
			{
				for (Player player : w.getPlayers())
				{
					PrintUtils.OBSFormatDebug(player, "&c&lAttention&r&f: The server is about to reset it's current mob state."
							+ " If you have any important mobs you wish to keep, &c&nplease capture them&r&f."
							+ " We apologize for any inconveniences caused by this."
							+ " The server will wipe &c&lALL&r&f currently existing mobs in &b&oone (1) minute&r&f."
							+ " Please note &d&oessential mobs will be respawned&r&f as necessary. Thank you for your cooperation.");
				}
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{					
					for (Entity e : w.getEntities())
					{
						if (!(e instanceof LivingEntity) || e instanceof Player) continue;
						MobData data = MobData.getMob(e.getUniqueId());
						if (data != null) data.kill();
						else e.remove();
					}
					for (Player player : w.getPlayers())
					{
						PrintUtils.OBSFormatDebug(player, "&c&lAttention&r&f: Process complete. All mobs have been eliminated.");
					}
				}, 1200);
			}
		}
		
		if (args[0].equals("register"))
		{
			if (affirmOP(p)) return true;
			
			if (args[1].equals("spell") && SpellRegistry.spellRegistry.containsKey(args[2]) && args.length == 5)
			{
				Spell spell = SpellRegistry.spellRegistry.get(args[2]);
				Player target = Bukkit.getPlayer(args[3]);
				if (target == null) return false;
				boolean toggle = Boolean.parseBoolean(args[4]);
				PlayerData.getPlayer(target.getUniqueId()).getSpell(spell).setRegistered(toggle);
				PlayerData.getPlayer(target.getUniqueId()).save();
				String registered = toggle ? " registered for " : " removed from ";
				PrintUtils.OBSFormatDebug(p, "Spell: "+spell.getName()+registered+": "+target.getName());
				PrintUtils.OBSFormatPrint(p, "The spell: "+spell.getName()+" \nhas been"+registered+"your account. \nPlease contact the system admin if you think this was done in error.");
				return true;
			}
			
			if (args[1].equals("ability") && AbilityRegistry.abilityRegistry.containsKey(args[2]) && args.length == 5)
			{
				ObsAbility ability = AbilityRegistry.abilityRegistry.get(args[2]);
				Player target = Bukkit.getPlayer(args[3]);
				if (target == null) return false;
				boolean toggle = Boolean.parseBoolean(args[4]);
				PlayerData.getPlayer(target.getUniqueId()).getAbility(ability).setRegistered(toggle).setActive(false);
				PlayerData.getPlayer(target.getUniqueId()).save();
				String registered = toggle ? " registered for " : " removed from ";
				PrintUtils.OBSFormatDebug(p, "Ability: "+ability.getDisplayName()+registered+": "+target.getName());
				PrintUtils.OBSFormatPrint(p, "The ability: "+ability.getDisplayName()+" \nhas been"+registered+"your account. \nPlease contact the system admin if you think this was done in error.");
				return true;
			}
		}
		
		if (args[0].equals("registerAllMagic"))
		{
			if (affirmOP(p)) return true;
			
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null) return false;
			PlayerData data = PlayerData.getPlayer(target.getUniqueId());
			data.setMagicProficiency(PlayerData.MAXMAGIC);
			for (Spell spell : SpellRegistry.spellRegistry.values())
			{
				data.getSpell(spell).setRegistered(true);
			}
			data.save();
			PrintUtils.OBSFormatDebug(p, "All magic registered for: "+target.getName());
			PrintUtils.OBSFormatPrint(target, "All magic has been registered for your account. \nYour Gnosis has been elevated to 7 (max).");
			return true;
		}
		
		if (args[0].equals("registerAllAbilities"))
		{
			if (affirmOP(p)) return true;
			
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null) return false;
			PlayerData data = PlayerData.getPlayer(target.getUniqueId());
			for (ObsAbility ability : AbilityRegistry.abilityRegistry.values())
			{
				data.getAbility(ability).setRegistered(true);
			}
			data.save();
			PrintUtils.OBSFormatDebug(p, "All abilities registered for: "+target.getName());
			PrintUtils.OBSFormatPrint(target, "All abilities have been registered for your account.");
			return true;
		}
		
		if (args[0].equals("setLuminite"))
		{
			if (affirmOP(p)) return true;
			
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null) return false;
			PlayerData data = PlayerData.getPlayer(target.getUniqueId());
			data.setLuminite(PlayerData.maxLuminite);
			data.save();
			PlayerHud.updateHud(target);
			PrintUtils.OBSFormatDebug(p, "Maximum Luminite granted for: "+target.getName());
			PrintUtils.OBSFormatPrint(p, "You have been granted maximum Luminite. \nIf you believe this was done in error, contact the server admin.");
			return true;
		}
		
		if (args[0].equals("setScrap"))
		{
			if (affirmOP(p)) return true;
			
			Player target = Bukkit.getPlayer(args[1]);
			if (target == null) return false;
			PlayerData data = PlayerData.getPlayer(target.getUniqueId());
			data.setScrap(PlayerData.maxScrap);
			data.save();
			PrintUtils.OBSFormatDebug(p, "Maximum Scrap granted for: "+target.getName());
			PrintUtils.OBSFormatPrint(p, "You have been granted maximum Scrap. \nIf you believe this was done in error, contact the server admin.");
			return true;
		}
		if (args[0].equals("debug"))
		{
			if (affirmOP(p)) return true;
			
			if (Ouroboros.debug == false) 
			{
				Ouroboros.debug = true;
				PrintUtils.OBSFormatDebug(p, "&7Console logging has been turned &a&lON");
				PrintUtils.OBSConsoleDebug("&7Console logging has been turned &a&lON");
				return true;
			}
			Ouroboros.debug = false;
			PrintUtils.OBSFormatDebug(p, "&7Console logging has been turned &c&lOFF");
			PrintUtils.OBSConsoleDebug("&7Console logging has been turned &c&lOFF");
			return true;
		}
		
		if (args[0].equals("generate")) 
		{
			if (affirmOP(p)) return true;
			
			if (args[1].equals("spell") && SpellRegistry.spellRegistry.containsKey(args[2]))
			{
				Spell spell = SpellRegistry.spellRegistry.get(args[2]);
				ItemStack stack = spell.getAsItemStack(false);
				p.getInventory().addItem(stack);
				return true;
			}
			
			if (args[1].equals("wand") && Wand.wand_registry.containsKey(args[2]))
			{
				Wand wand = Wand.wand_registry.get(args[2]);
				ItemStack stack = wand.getAsItemStack();
				p.getInventory().addItem(stack);
				return true;
			}
			
			if (args[1].equals("object") && ObjectRegistry.itemRegistry.containsKey(args[2]))
			{				
				AbstractObsObject obj = ObjectRegistry.itemRegistry.get(args[2]);
				ItemStack stack = obj.toItemStack();
				p.getInventory().addItem(stack);
				return true;
			}
			
			if (args[1].equals("mob"))
			{
				EntityType type;
			    try
			    {
			        type = EntityType.valueOf(args[2].toUpperCase());
			    }
			    catch (IllegalArgumentException e)
			    {
			        return false;
			    }
			    
			    int level;
			    try
			    {
			    	level = Integer.valueOf(Integer.parseInt(args[3]));
			    }
			    catch (IllegalArgumentException e) 
			    {
			    	return false;
			    }
			    
			    if (level < 0 || level > 100)
			    {
			    	PrintUtils.OBSFormatError(p, "Invalid Level Input: Was expecting 0 < value < 100.");
			    	return true;
			    }
			    
			    String customName = args[4];
			    if (customName == null)
			    {
			    	PrintUtils.OBSFormatError(p, "Invalid Custom Name: Cannot be null.");
			    	return true;
			    }
			    
			    MobSummoner.build(p.getLocation(), type, level, customName);
			    return true;
			}
			
			if (args[1].equals("materia"))
			{
				// component sub-command: /obs generate materia component <refined|unrefined> <type/name>
				if (args[2].equals("component"))
				{
					if (args.length == 5 && args[3].equals("unrefined") && MateriaType.fromString(args[4]) != null)
					{
						MateriaType type = MateriaType.fromString(args[4]);
						ItemStack stack = Materia.generateUnrefinedMateria(type);
						p.getInventory().addItem(stack);
						return true;
					}

					if (args.length == 5 && args[3].equals("refined") && Materia.materia_registry.containsKey(args[4]))
					{
						Materia materia = Materia.materia_registry.get(args[4]);
						ItemStack stack = materia.getAsItemStack();
						p.getInventory().addItem(stack);
						return true;
					}
				}

				// catalyst: /obs generate materia catalyst <materiaName>
				if (args[2].equals("catalyst"))
				{
					if (Materia.materia_registry.containsKey(args[3]) && args.length == 4)
					{
						Materia catalyst = Materia.materia_registry.get(args[3]);
						ItemStack stack = catalyst.getAsItemStack(MateriaState.CATALYST);
						p.getInventory().addItem(stack);
						return true;
					}
				}

				// element_core: /obs generate materia element_core <core_name>
				if (args[2].equals("element_core"))
				{
					if (args.length == 4 && Materia.materia_registry.containsKey(args[3]))
					{
						Materia materia = Materia.materia_registry.get(args[3]);
						ItemStack stack = materia.getAsItemStack(MateriaState.ELEMENT_CORE);
						p.getInventory().addItem(stack);
						return true;
					}
				}
			}
			
			if (args[1].equals("echo") && args.length == 6)
			{
				Materia catalyst = null;
				Materia base = null;
				Materia binding = null;
				Materia element_core = null;
				
				if (Materia.get(args[2]) != null) catalyst = Materia.get(args[2]);
				if (Materia.get(args[3]) != null) base = Materia.get(args[3]);
				if (Materia.get(args[4]) != null) binding = Materia.get(args[4]);
				if (Materia.get(args[5]) != null) element_core = Materia.get(args[5]);
				
				
				if (catalyst.getMateriaComponent() != MateriaComponent.CATALYST)
					PrintUtils.OBSFormatError(p, "Catalyst Invalid: "+catalyst == null ? "null" : catalyst.getInternalName() + ", "+catalyst.getMateriaComponent().getLabel());

				if (base.getMateriaComponent() != MateriaComponent.BASE)
					PrintUtils.OBSFormatError(p, "Base Invalid: "+base == null ? "null" : base.getInternalName() + ", "+base.getMateriaComponent().getLabel());

				if (binding.getMateriaComponent() != MateriaComponent.BINDING)
					PrintUtils.OBSFormatError(p, "Binding Invalid: "+ binding == null ? "null" : binding.getInternalName() + ", "+binding.getMateriaComponent().getLabel());
				
				if (element_core != null && element_core.getMateriaComponent() != MateriaComponent.ELEMENT_CORE)
					PrintUtils.OBSFormatError(p, "Base Invalid: "+ element_core == null ? "null" : element_core.getInternalName() + ", "+element_core.getMateriaComponent().getLabel());
				
				ItemStack echo = EchoForge.forge(catalyst, base, binding, element_core);
				p.getInventory().addItem(echo);
				PrintUtils.OBSFormatDebug(p, "Echo Generated Successfully!", "Echo Generation");
			}
		}
		
		if (args[0].equals("money")) 
		{
			if (affirmOP(p)) return true;
			
			if (args.length < 3)
			{
				PrintUtils.OBSFormatError(p, "&7Usage: /obs money <sub-command> <player> [value]");
				return true;
			}
			
			Player target = Bukkit.getPlayer(args[2]);
			if (target == null) return false;
			
			if (args[1].equals("add") && args.length == 4) 
			{
				int value;
				try 
				{
					value = Integer.parseInt(args[3]);
				} 
				catch (NumberFormatException e)
				{
					e.printStackTrace();
					return false;
				}
				
				if (value <= 0 || value > PlayerData.fundsIntegerMax) 
				{
					PrintUtils.OBSFormatError(p, "&r&7&oExpecting a value between 0 and 99999999.");
					return false;
				}
				PlayerData.addMoney(target, value);
				PrintUtils.OBSFormatPrint(p, "&r&7&oSuccessfully added {&r&f&l"+value+"&r&e₪&r&7&o} to: &r&f&l"+target.getName()+"&r&7&o's account.");
				return true;
			}
			
			if (args[1].equals("subtract") && args.length == 4) 
			{
				if (affirmOP(p)) return true;
				
				int value;
				try 
				{
					value = Integer.parseInt(args[3]);
				} 
				catch (NumberFormatException e)
				{
					e.printStackTrace();
					return false;
				}
				
				if (value <= 0 || value > PlayerData.fundsIntegerMax) 
				{
					PrintUtils.OBSFormatError(p, "&r&7&oExpecting a value between 0 and 99999999.");
					return false;
				}
				PlayerData.subtractMoney(target, value);
				PrintUtils.OBSFormatPrint(p, "&r&7&oSuccessfully subtracted {&r&f&l"+value+"&r&e₪&r&7&o} from: &r&f&l"+target.getName()+"&r&7&o's account.");
				return true;
			}
			
			if (args[1].equals("setMaxMoney") && args.length == 3) 
			{
				if (affirmOP(p)) return true;
				
				PlayerData data = PlayerData.getPlayer(target.getUniqueId());
				data.setFunds(true, 0);
				data.setFunds(false, PlayerData.fundsIntegerMax);
				data.save();
				PlayerHud.updateHud(target);
				PrintUtils.OBSFormatPrint(p, "&r&7&oSuccessfully added max &r&e₪ &r&7&oto: &r&f&l"+target.getName()+"&r&7&o's account.");
				return true;
			}
			
			if (args[1].equals("setMaxDebt") && args.length == 3) 
			{
				if (affirmOP(p)) return true;
				
				PlayerData data = PlayerData.getPlayer(target.getUniqueId());
				data.setFunds(true, PlayerData.fundsIntegerMax);
				data.setFunds(false, 0);
				data.save();
				PlayerHud.updateHud(target);
				PrintUtils.OBSFormatPrint(p, "&r&7&oSuccessfully added max &r&cЖ &r&7&oto: &r&f&l"+target.getName()+"&r&7&o's account.");
				return true;
			}
			
			if (args[1].equals("resetMoney") && args.length == 3) 
			{
				if (affirmOP(p)) return true;
				
				PlayerData data = PlayerData.getPlayer(target.getUniqueId());
				data.setFunds(false, 0);
				data.setFunds(true, 0);
				data.save();
				PlayerHud.updateHud(target);
				PrintUtils.OBSFormatPrint(p, "&r&7&oSuccessfully reset { &r&e₪ &r&7&o& &r&cЖ &r&7&o} from: &r&f&l"+target.getName()+"&r&7&o's account.");
				return true;
			}
		}
		
		//--------------------------------- OBS STATS COMMANDS ---------------------------------//
		
		/**
		 * @Note: All sub commands of main command 'stats' requires OP. Players should always be able to view their stats in-game.
		 */
		if (args[0].equals("stats")) 
		{
			if (args.length == 1) 
			{		
				PlayerData data = PlayerData.getPlayer(uuid);
				PrintUtils.Print(p,
						"&b&l+&r&7-----------------------&f{&bΩ&f}&7-----------------------&b&l+",
						"                      &b&lOBS Statistical Inquiry&r&f",
						"                      &f&l- &r&fPlayer: &e&l" + p.getName(),
						"                          &f&lAccount Level&r&7: &a" + data.getAccountLevel(),
						"",
						"                             &7General Levels:", 
						"               &f&lTravel&r&7: &a" + data.getStat(StatType.TRAVEL, true) +
						"    &f&lCrafting&r&7: &a" + data.getStat(StatType.CRAFTING, true) +
						"    &f&lAlchemy&r&7: &a" + data.getStat(StatType.ALCHEMY, true),
						"             &f&lWoodcutting&r&7: &a" + data.getStat(StatType.WOODCUTTING, true) +
						"    &f&lMining&r&7: &a" + data.getStat(StatType.MINING, true) +
						"    &f&lFishing&r&7: &a" + data.getStat(StatType.FISHING, true),
						"                 &f&lFarming&r&7: &a" + data.getStat(StatType.FARMING, true) +
						"    &f&lEnchanting&r&7: &a" + data.getStat(StatType.ENCHANTING, true),
						"               &f&lDiscovery&r&7: &a" + data.getStat(StatType.DISCOVERY, true) + 
						"   &f&lRefinement&r&7: &a"+data.getStat(StatType.REFINEMENT, true),
						"",
						"                             &7Combat Levels:",
						"                 &f&lMelee&r&7: &c" + data.getStat(StatType.MELEE, true) +
						"    &f&lRanged&r&7: &c" + data.getStat(StatType.RANGED, true) +
						"    &f&lMagic&r&7: &c" + data.getStat(StatType.MAGIC, true),
						"",
						"                               &7Stat Points:",
						"             &f&lAbility Points: &6" + data.getAbilityPoints() + 
						"    &f&lPrestige Points: &6" + data.getPrestigePoints(),
						"",
						"   &7General XP:", 
						"   &f&lTravel&r&7: &b" + data.getStat(StatType.TRAVEL, false) + " &7/ " + PrintUtils.printNextLevelXP(uuid, StatType.TRAVEL),
						"   &f&lCrafting&r&7: &b" + data.getStat(StatType.CRAFTING, false) + " &7/ " + PrintUtils.printNextLevelXP(uuid, StatType.CRAFTING),
						"   &f&lAlchemy&r&7: &b" + data.getStat(StatType.ALCHEMY, false) + " &7/ " + PrintUtils.printNextLevelXP(uuid, StatType.ALCHEMY),
						"   &f&lWoodcutting&r&7: &b" + data.getStat(StatType.WOODCUTTING, false) + " &7/ " + PrintUtils.printNextLevelXP(uuid, StatType.WOODCUTTING),
						"   &f&lMining&r&7: &b" + data.getStat(StatType.MINING, false) + " &7/ " + PrintUtils.printNextLevelXP(uuid, StatType.MINING),
						"   &f&lFishing&r&7: &b" + data.getStat(StatType.FISHING, false) + " &7/ " + PrintUtils.printNextLevelXP(uuid, StatType.FISHING),
						"   &f&lFarming&r&7: &b" + data.getStat(StatType.FARMING, false) + " &7/ " + PrintUtils.printNextLevelXP(uuid, StatType.FARMING),
						"   &f&lEnchanting&r&7: &b" + data.getStat(StatType.ENCHANTING, false) + " &7/ " + PrintUtils.printNextLevelXP(uuid, StatType.ENCHANTING),
						"   &f&lDiscovery&r&7: &b" + data.getStat(StatType.DISCOVERY, false) + " &7/" + PrintUtils.printNextLevelXP(uuid, StatType.DISCOVERY),
						"   &f&lRefinement&r&7: &b" + data.getStat(StatType.REFINEMENT, false) + " &7/" + PrintUtils.printNextLevelXP(uuid, StatType.REFINEMENT),
						"",
						"   &7Combat XP:",
						"   &f&lMelee&r&7: &d" + data.getStat(StatType.MELEE, false) + " &7/ " + PrintUtils.printNextLevelXP(uuid, StatType.MELEE),
						"   &f&lRanged&r&7: &d" + data.getStat(StatType.RANGED, false) + " &7/ " + PrintUtils.printNextLevelXP(uuid, StatType.RANGED), 
						"   &f&lMagic&r&7: &d" + data.getStat(StatType.MAGIC, false) + " &7/ " + PrintUtils.printNextLevelXP(uuid, StatType.MAGIC),
						"&b&l+&r&7-----------------------&f{&bΩ&f}&7-----------------------&b&l+"
						);
				
				if (Ouroboros.debug) 
				{
					PrintUtils.OBSConsoleDebug("&bPlayer&f: " + sender.getName().toString() + "&f accessed their stats.");
				}
				
				return true;
			}
			
			if (args[1].equals("doLevelUpSound") && args.length == 3) 
			{
				Boolean bool = Boolean.parseBoolean(args[2]);
				if (bool) 
				{
					PlayerData.getPlayer(uuid).doLevelUpSound(true);
					PrintUtils.OBSFormatDebug(p, "XP & Levelup Audio Turned: &a&lON");
				}
				else 
				{
					PlayerData.getPlayer(uuid).doLevelUpSound(false);
					PrintUtils.OBSFormatDebug(p, "XP & Levelup Audio Turned: &c&lOFF");
				}
				return true;
			}
			
			if (args[1].equals("doXpNotifs") && args.length == 3) 
			{
				Boolean bool = Boolean.parseBoolean(args[2]);
				if (bool) 
				{
					PlayerData.getPlayer(uuid).doXpNotification(true);
					PrintUtils.OBSFormatDebug(p, "Xp & Level Notifications Turned: &a&lON");
				}
				else 
				{
					PlayerData.getPlayer(uuid).doXpNotification(false);
					PrintUtils.OBSFormatDebug(p, "Xp & Level Notifications Turned: &c&lOFF");
				}
				return true;
			}
			
			if (args[1].equals("reset") && args.length == 3)
			{
				if (affirmOP(p)) return true;
				
		        Player target = Bukkit.getPlayer(args[2]);
		        if (target == null) 
		        {
		            PrintUtils.OBSFormatError(p, "Player not found.");
		            return true;
		        }
		        
		        PlayerData.getPlayer(target.getUniqueId()).getFile().delete();
		        PlayerData.getPlayer(target.getUniqueId()).setDefaults();
		        PlayerData.getPlayer(target.getUniqueId()).save();
		        PlayerHud.updateHud(target);
		        PrintUtils.OBSFormatDebug(p, "Successfully Reset " + target.getName() + "'s Account");
		        return true;
			}
			
			if (args[1].equals("set") && args.length == 6) 
			{
				if (affirmOP(p)) return true;
				
		        Player target = Bukkit.getPlayer(args[2]);
		        if (target == null) 
		        {
		            PrintUtils.OBSFormatError(p, "Player not found.");
		            return true;
		        }

		        StatType statType = StatType.fromString(args[3]);
		        if (statType == null)
		        {
		            PrintUtils.OBSFormatError(p, "Invalid input StatType: "+args[3]);
		            return true;
		        }
		        
		        boolean setLevel = Boolean.parseBoolean(args[4]);
		        
				int value;
				try 
		        {
		            value = Integer.parseInt(args[5]);
		        } 
		        catch (NumberFormatException e) 
		        {
		            PrintUtils.OBSFormatError(p, "Value must be a number: \"" + args[5] + "\"");
		            return true;
		        }

		        if (value < 0 || value > 100) 
		        {
		            PrintUtils.OBSFormatError(p, "Value must be between 0 and 100.");
		            return true;
		        }
		        
		        PlayerData.getPlayer(target.getUniqueId()).setStat(statType, setLevel, value);
		        PlayerData.getPlayer(target.getUniqueId()).save();
		        PrintUtils.OBSFormatDebug(p, "Successfully updated stats of " + target.getName());
		        return true;
			}
			
			if (args[1].equals("addXp") && args.length == 5)
			{
				if (affirmOP(p)) return true;
				
				Player target = Bukkit.getPlayer(args[2]);
				if (target == null) 
		        {
		            PrintUtils.OBSFormatError(p, "Player not found.");
		            return true;
		        }
				
				StatType statType = StatType.fromString(args[3]);
				if (statType == null) 
		        {
		            PrintUtils.OBSFormatError(p, "Invalid input StatType: "+args[3]);
		            return true;
		        }
		        
		        int value;
		        try
		        {
		        	value = Integer.parseInt(args[4]);
		        }
		        catch (NumberFormatException e) 
		        {
		            PrintUtils.OBSFormatError(p, "Value must be a number: \"" + args[5] + "\"");
		            return true;
		        }
		        
		        if (value < 0 || value > Integer.MAX_VALUE)
		        {
		        	PrintUtils.OBSFormatError(p, "Value must be between 0 and "+Integer.MAX_VALUE+".");
		            return true;
		        }
		        PlayerData.getPlayer(target.getUniqueId()).doLevelUpSound(false);
		        EntityEffects.playSound(target, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
		        PlayerData.addXP(p, statType, value);
		        PrintUtils.OBSFormatDebug(p, "&a&oSuccessfully &r&fadded &l"+value+" &r&b"+statType.getFancyKey()+" &r&eXP&f to &o"+target.getName()+"&r&f's account.");
		        PlayerData.getPlayer(target.getUniqueId()).doLevelUpSound(true);
		        return true;
			}
			
			return false;
		}
		return false;
	
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) 
	{
		boolean isOp = (sender instanceof Player player) && player.isOp();
		
		return switch(args.length) 
		{
			case 0 -> List.of("obs");
			case 1 ->
			{
				// All players see general-use commands.
				List<String> cmds = new ArrayList<>(List.of("menu", "stats", "welcomekit", "spellbook", "recoverwand", "wand"));
				
				// OP-only commands are hidden from regular players.
				if (isOp) cmds.addAll(List.of("debug", "version", "generate", "money", "register","registerAllAbilities", "registerAllMagic", "adminspells", "clearmobs", "setLuminite", "setScrap"));
				yield cmds;
			}
			case 2 -> 
			{
				yield switch(args[0])
				{
					// OP-only root commands.
					case "registerAllAbilities", "registerAllMagic", "setLuminite", "setScrap" ->
						isOp ? Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()) : List.of();
					case "register" ->
						isOp ? List.of("spell", "ability") : List.of();
					case "generate" ->
						isOp ? List.of("object", "spell", "materia", "wand", "mob", "echo") : List.of();
					case "money" ->
						isOp ? List.of("add", "subtract", "setMaxMoney", "setMaxDebt", "resetMoney") : List.of();
					// Stats sub-commands: some are OP-only.
					case "stats" ->
					{
						List<String> statSubs = new ArrayList<>(List.of("doLevelUpSound", "doXpNotifs"));
						if (isOp) statSubs.addAll(List.of("set", "reset", "addXp"));
						yield statSubs;
					}
					// General-use commands with no further completions.
					case "spellbook", "recoverwand", "wand", "debug", "menu", "welcomekit" -> List.of();
					default -> List.of();
				};
			}
			case 3 ->
			{
				yield switch(args[1]) 
				{
					// OP-only sub-command completions.
					case "ability" -> isOp ? new ArrayList<>(AbilityRegistry.abilityRegistry.keySet()) : List.of();
					case "object" -> isOp ? new ArrayList<>(ObjectRegistry.itemRegistry.keySet()) : List.of();
					case "spell" -> isOp ? new ArrayList<>(SpellRegistry.spellRegistry.keySet()) : List.of();
					case "wand" -> isOp ? new ArrayList<>(Wand.wand_registry.keySet()) : List.of();
					case "materia" -> isOp ? List.of("catalyst", "element_core", "component") : List.of();
					case "mob" -> isOp ? EntityCategories.asList() : List.of();
					case "echo" -> isOp ? Materia.materia_registry.values().stream().filter(m -> m.getMateriaComponent() == MateriaComponent.CATALYST)
							.map(Materia::getInternalName)
							.collect(Collectors.toCollection(ArrayList::new)) : List.of();
					case "set", "reset", "addXp" ->
						isOp ? Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()) : List.of();
					case "add", "subtract", "setMaxMoney", "setMaxDebt", "resetMoney" ->
						isOp ? Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()) : List.of();
					// General-use sub-command completions.
					case "doLevelUpSound", "doXpNotifs" -> List.of("true", "false");
					default -> List.of();	
				};
			}
			case 4 ->
			{
				yield switch(args[1]) 
				{
					case "ability", "spell" ->
						isOp ? Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()) : List.of();
					case "mob" -> isOp ? List.of("<LEVEL: 1-100>") : List.of();
					case "materia" -> isOp ? switch(args[2])
					{
						case "catalyst" -> Materia.materia_registry.values().stream()
							.filter(m -> m.getMateriaComponent() == MateriaComponent.CATALYST)
							.map(Materia::getInternalName)
							.collect(Collectors.toCollection(ArrayList::new));
						case "component" -> List.of("refined", "unrefined");
						case "element_core" -> Materia.materia_registry.values().stream()
						.filter(m -> m.getMateriaComponent() == MateriaComponent.ELEMENT_CORE)
						.map(Materia::getInternalName)
						.collect(Collectors.toCollection(ArrayList::new));
						default -> List.of();
					} : List.of();
					case "echo" -> isOp ? Materia.materia_registry.values().stream().filter(m -> m.getMateriaComponent() == MateriaComponent.BASE)
							.map(Materia::getInternalName)
							.collect(Collectors.toCollection(ArrayList::new)) : List.of();
					case "set", "addXp" ->
						isOp ? Arrays.stream(StatType.values()).map(Enum::name).map(String::toUpperCase).collect(Collectors.toList()) : List.of();
					case "add", "subtract" ->
						isOp ? List.of("value <= 99999999") : List.of();
					default -> List.of();
				};
			}
			case 5 ->
			{
				yield switch(args[1]) 
				{
					case "materia" -> isOp ? switch(args[2]) 
					{
						case "component" -> switch(args[3]) 
						{
							case "unrefined" -> MateriaType.getAllKeys();
							case "refined" -> Materia.materia_registry.values().stream()
								.filter(m -> m.getMateriaComponent() == MateriaComponent.BASE || m.getMateriaComponent() == MateriaComponent.BINDING)
								.map(Materia::getInternalName)
								.collect(Collectors.toCollection(ArrayList::new));
							default -> List.of();
						};
						case "element_core" -> isOp ? Materia.materia_registry.values().stream()
							.filter(m -> m.getMateriaComponent() == MateriaComponent.ELEMENT_CORE)
							.map(Materia::getInternalName)
							.collect(Collectors.toCollection(ArrayList::new)) : List.of();
						default -> List.of();
					} : List.of();
					case "echo" -> isOp ? Materia.materia_registry.values().stream().filter(m -> m.getMateriaComponent() == MateriaComponent.BINDING)
							.map(Materia::getInternalName)
							.collect(Collectors.toCollection(ArrayList::new)) : List.of();
					case "mob" -> isOp ? List.of("<custom name>") : List.of();
					case "addXp" -> isOp ? List.of("<value>") : List.of();
					case "set" -> isOp ? List.of("true", "false") : List.of();
					case "spell", "ability" -> isOp ? List.of("true", "false") : List.of();
					default -> List.of();
				};
			}
			case 6 ->
			{
				yield switch(args[1]) 
				{
					case "set" -> isOp ? List.of("<value>") : List.of();
					case "echo" -> isOp ? Materia.materia_registry.values().stream().filter(m -> m.getMateriaComponent() == MateriaComponent.ELEMENT_CORE)
							.map(Materia::getInternalName)
							.collect(Collectors.toCollection(ArrayList::new)) : List.of();
					default -> List.of();
				};
			}
			default -> List.of();
		};
	}
	
	public static boolean affirmOP(Player p) 
	{
	    if(!p.isOp()) PrintUtils.OBSFormatError(p, "&r&f&oYou don't have permissions to access this command.");
	    return !p.isOp();
	}

}