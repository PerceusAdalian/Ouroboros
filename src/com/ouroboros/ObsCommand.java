package com.ouroboros;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.StatType;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsMainMenu;
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
		
		if (args[0].equals("stats")) 
		{
			if (args.length == 1) 
			{				
				PrintUtils.Print(p,
						"&b&l+&r&7-----------------------&f{&bΩ&f}&7-----------------------&b&l+",
						"                      &b&lOBS Statistical Inquiry&r&f",
						"                      &f&l- &r&fPlayer: &e&l" + p.getName(),
						"                          &f&lAccount Level&r&7: &a" + PlayerData.getPlayer(p.getUniqueId()).getAccountLevel(),
						"",
						"                             &7General Levels:", 
						"               &f&lTravel&r&7: &a" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.TRAVEL, true) +
						"    &f&lCrafting&r&7: &a" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.CRAFTING, true) +
						"    &f&lAlchemy&r&7: &a" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.ALCHEMY, true),
						"             &f&lWoodcutting&r&7: &a" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.WOODCUTTING, true) +
						"    &f&lMining&r&7: &a" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.MINING, true) +
						"    &f&lFishing&r&7: &a" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.FISHING, true),
						"",
						"                             &7Combat Levels:",
						"                 &f&lMelee&r&7: &c" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.MELEE, true) +
						"    &f&lRanged&r&7: &c" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.RANGED, true) +
						"    &f&lMagic&r&7: &c" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.MAGIC, true),
						"",
						"                               &7Stat Points:",
						"             &f&lAbility Points: &6" + PlayerData.getPlayer(p.getUniqueId()).getAbilityPoints() + 
						"    &f&lPrestige Points: &6" + PlayerData.getPlayer(p.getUniqueId()).getPrestigePoints(),
						"",
						"   &7General XP:", 
						"   &f&lTravel&r&7: &b" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.TRAVEL, false),
						"   &f&lCrafting&r&7: &b" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.CRAFTING, false),
						"   &f&lAlchemy&r&7: &b" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.ALCHEMY, false),
						"   &f&lWoodcutting&r&7: &b" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.WOODCUTTING, false),
						"   &f&lMining&r&7: &b" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.MINING, false),
						"   &f&lFishing&r&7: &b" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.FISHING, false),
						"",
						"   &7Combat XP:",
						"   &f&lMelee&r&7: &d" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.MELEE, false),
						"   &f&lMagic&r&7: &d" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.MAGIC, false),
						"   &f&lRanged&r&7: &d" + PlayerData.getPlayer(p.getUniqueId()).getStat(StatType.RANGED, false), 
						"&b&l+&r&7-----------------------&f{&bΩ&f}&7-----------------------&b&l+"
						);
				
				if (Ouroboros.debug) 
				{
					PrintUtils.OBSConsoleDebug("&bPlayer&f: " + sender.getName().toString() + "&f accessed their stats.");
				}
				
				return true;
			}
			
			if (args[1].equals("set") && args.length == 6) 
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

		        Optional<StatType> optType = StatType.fromString(args[3]);
		        if (optType.isEmpty()) 
		        {
		            PrintUtils.OBSFormatError(p, "Invalid input StatType: "+args[3]);
		            return true;
		        }
		        StatType statType = optType.get();
		        
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
			case 1 -> List.of("debug","menu","stats");
			case 2 -> 
			{
				yield switch(args[0])
				{
					case "debug" -> List.of();
					case "menu" -> List.of();
					case "stats" -> List.of("set");
					default -> List.of();
				};
			}
			case 3 ->
			{
				yield switch(args[1]) 
				{
					case "set" -> Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
					default -> List.of();
				};
			}
			case 4 ->
			{
				yield switch(args[1]) 
				{
					case "set" -> Arrays.stream(StatType.values()).map(Enum::name).map(String::toUpperCase).collect(Collectors.toList());
					default -> List.of();
				};
			}
			case 5 ->
			{
				yield switch(args[1]) 
				{
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
