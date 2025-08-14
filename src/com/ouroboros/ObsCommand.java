package com.ouroboros;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.StatType;
import com.ouroboros.interfaces.ObsDisplayMain;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsMainMenu;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.objects.ObjectRegistry;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.PrintUtils;

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
		
		if (args[0].equals("version")) 
		{
			if (affirmOP(p)) 
			{				
				PrintUtils.Print(p, "----------------------------",
						"                 &b&lOBS",
						" &f&l- &r&fSystem Time: "+LocalTime.now(),
						" &f&l- &r&a&lSystem Version&r&f: &7{&c&lALPHA&f&7}",
						" &f&l- &r&dAPI Version&r&f: "+Bukkit.getVersion().toString(),
						" &f&l- &r&fPlugins Loaded: &e&l" + Bukkit.getPluginManager().getPlugins().length,
						"----------------------------");
				return true;
			}
			return false;
		}
		
		if (args[0].equals("debug"))
		{
			if (affirmOP(p)) 
			{				
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
			return false;
		}
		
		if (args[0].equals("menu")) 
		{
			if (Ouroboros.debug) 
			{
				PrintUtils.OBSConsoleDebug("&bPlayer&f: " + sender.getName().toString() + "&f opened the OBS Main Menu.");
			}
			GuiHandler.open(p, new ObsMainMenu(p));
			EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER, 1, 1);
			return true;
		}
		
		if (args[0].equals("generate") && ObjectRegistry.itemRegistry.containsKey(args[1])) 
		{
			AbstractObsObject obj = ObjectRegistry.itemRegistry.get(args[1]);
			ItemStack stack = obj.toItemStack();
			p.getInventory().addItem(stack);
			return true;
		}
		
		if (args[0].equals("money")) 
		{
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
				PlayerData data = PlayerData.getPlayer(target.getUniqueId());
				data.setFunds(true, 0);
				data.setFunds(false, PlayerData.fundsIntegerMax);
				data.save();
				ObsDisplayMain.updateHud(target);
				PrintUtils.OBSFormatPrint(p, "&r&7&oSuccessfully added max &r&e₪ &r&7&oto: &r&f&l"+target.getName()+"&r&7&o's account.");
				return true;
			}
			
			if (args[1].equals("setMaxDebt") && args.length == 3) 
			{
				PlayerData data = PlayerData.getPlayer(target.getUniqueId());
				data.setFunds(true, PlayerData.fundsIntegerMax);
				data.setFunds(false, 0);
				data.save();
				ObsDisplayMain.updateHud(target);
				PrintUtils.OBSFormatPrint(p, "&r&7&oSuccessfully added max &r&cЖ &r&7&oto: &r&f&l"+target.getName()+"&r&7&o's account.");
				return true;
			}
			
			if (args[1].equals("resetMoney") && args.length == 3) 
			{
				PlayerData data = PlayerData.getPlayer(target.getUniqueId());
				data.setFunds(false, 0);
				data.setFunds(true, 0);
				data.save();
				ObsDisplayMain.updateHud(target);
				PrintUtils.OBSFormatPrint(p, "&r&7&oSuccessfully reset { &r&e₪ &r&7&o& &r&cЖ &r&7&o} from: &r&f&l"+target.getName()+"&r&7&o's account.");
				return true;
			}
		}
		
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
						"        &f&lFarming&r&7: &a" + data.getStat(StatType.FARMING, true) +
						"    &f&lEnchanting&r&7: &a" + data.getStat(StatType.ENCHANTING, true)+
						"    &f&lDiscovery&r&7: &a" + data.getStat(StatType.DISCOVERY, true),
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
				if(!affirmOP(p))
				{
					return true;
				}
				
		        Player target = Bukkit.getPlayer(args[2]);
		        if (target == null) 
		        {
		            PrintUtils.OBSFormatError(p, "Player not found.");
		            return true;
		        }
		    
		        PlayerData.resetAccount(target.getUniqueId());
		        PlayerData.getPlayer(target.getUniqueId()).save();
		        PrintUtils.OBSFormatDebug(p, "Successfully Reset " + target.getName() + "'s Account");
		        return true;
			}
			
			if (args[1].equals("set") && args.length == 6) 
			{
				if(!affirmOP(p)) return true;
				
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
				if (!affirmOP(p)) return true;
				
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
		        EntityEffects.playSound(target, target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 1);
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
		return switch(args.length) 
		{
			case 0 -> List.of("obs");
			case 1 -> List.of("debug","menu","stats","generate","money","version");
			case 2 -> 
			{
				yield switch(args[0])
				{
					case "debug" -> List.of();
					case "menu" -> List.of();
					case "stats" -> List.of("set","reset","doLevelUpSound","doXpNotifs","addXp");
					case "generate" -> new ArrayList<>(ObjectRegistry.itemRegistry.keySet());
					case "money" -> List.of("add","subtract","setMaxMoney","setMaxDebt","resetMoney");
					default -> List.of();
				};
			}
			case 3 ->
			{
				yield switch(args[1]) 
				{
					case "set" -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
					case "reset" -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
					case "doLevelUpSound" -> List.of("true", "false");
					case "doXpNotifs" -> List.of("true", "false");
					case "addXp" -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
					case "add","subtract","setMaxMoney","setMaxDebt","resetMoney" -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
					default -> List.of();	
				};
				
			}
			case 4 ->
			{
				yield switch(args[1]) 
				{
					case "set" -> Arrays.stream(StatType.values()).map(Enum::name).map(String::toUpperCase).collect(Collectors.toList());
					case "addXp" -> Arrays.stream(StatType.values()).map(Enum::name).map(String::toUpperCase).collect(Collectors.toList());
					case "add","subtract" -> List.of("value <= 99999999");
					default -> List.of();
				};
			}
			case 5 ->
			{
				yield switch(args[1]) 
				{
					case "addXp" -> List.of("<value>");
					case "set" -> List.of("true","false");
					default -> List.of();
				};
			}
			case 6 ->
			{
				yield switch(args[1]) 
				{
					case "set" -> List.of("<value>");
					default -> List.of();
				};
			}
			default -> List.of();
		};
	}
	
	public static boolean affirmOP(Player p) 
	{
	    boolean isOp = p.isOp();
	    if(!isOp) PrintUtils.OBSFormatError(p, "&r&f&oYou don't have permissions to access this command.");
	    return isOp;
	}

}
