package com.ouroboros;

import java.io.File;
import java.time.LocalTime;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

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
			affirmOP(p);
			PrintUtils.Print(p, "----------------------------",
								"               &b&lOBS          ",
								" &f&l- &r&fSystem Time: "+LocalTime.now(),
								" &f&l- &r&a&lSystem Version&r&f: &7{&c&lALPHA&f&7}",
								" &f&l- &r&dAPI Version&r&f: "+Bukkit.getVersion().toString(),
								" &f&l- &r&fPlugins Loaded: &e&l" + Bukkit.getPluginManager().getPlugins().toString().length(),
								"----------------------------");
			return true;
		}
		
		if (args[0].equals("debug"))
		{
			affirmOP(p);
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
			File file = new File(Ouroboros.instance.getDataFolder() + "/playerdata", p.getUniqueId() + ".yml");

	        if (!file.exists()) 
	        {
	            PrintUtils.OBSFormatError(p, "&r&fNo data found. Try re-logging to initialize.");
	            return true;
	        }

	        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
	        
	        PrintUtils.Print(p,
	        	    "&b&l+&r&7-----------------&f{&bΩ&f}&7-----------------&b&l+",
	        	    "            &b&lOBS Statistical Inquiry",
	        	    "         &f&l- &r&fPlayer: &e&l" + p.getName(),
	        	    "",
	        	    "&b&l+&r&7------------------------------------&b&l+",
	        	    "                   &b&lLevel Profile",
	        	    "",
	        	    " &7General Levels:",
	        	    "  &f&lGeneric&r&7: &a" + config.getInt("stats.generic") + 
	        	    "    &f&lTravel&r&7: &a" + config.getInt("stats.travel"),
	        	    "  &f&lCrafting&r&7: &a" + config.getInt("stats.crafting")+
	        	    "    &f&lAlchemy&r&7: &a" + config.getInt("stats.alchemy"),
	        	    "  &f&lWoodcutting&r&7: &a" + config.getInt("stats.woodcutting") +
	        	    "    &f&lMining&r&7: &a" + config.getInt("stats.mining") +
	        	    "    &f&lFishing&r&7: &a" + config.getInt("stats.fishing"),
	        	    "",
	        	    " &7Combat Levels:",
	        	    "  &f&lMelee&r&7: &c" + config.getInt("stats.combat.melee") +
	        	    "    &f&lRanged&r&7: &c" + config.getInt("stats.combat.ranged") +
	        	    "    &f&lMagic&r&7: &c" + config.getInt("stats.combat.magic"),
	        	    "",
	        	    " &7General Experience:",
	        	    "  &f&lGeneric&r&7: &b" + config.getInt("experience.generic") + 
	        	    "    &f&lTravel&r&7: &b" + config.getInt("experience.travel"),
	        	    "  &f&lCrafting&r&7: &b" + config.getInt("experience.crafting") + 
	        	    "    &f&lAlchemy&r&7: &b" + config.getInt("experience.alchemy"),
	        	    "  &f&lWoodcutting&r&7: &b" + config.getInt("experience.woodcutting") +
	        	    "    &f&lMining&r&7: &b" + config.getInt("experience.mining") +
	        	    "    &f&lFishing&r&7: &b" + config.getInt("stats.experience.fishing"),
	        	    "",
	        	    " &7Combat XP:",
	        	    "  &f&lMelee&r&7: &d" + config.getInt("experience.combat.melee") +
	        	    "    &f&lRanged&r&7: &d" + config.getInt("experience.combat.ranged") +
	        	    "    &f&lMagic&r&7: &d" + config.getInt("experience.combat.magic"),
	        	    "",
	        	    "&b&l+&r&7-----------------&f{&bΩ&f}&7-----------------&b&l+"
	        	);

	        if (Ouroboros.debug) 
	        {
	        	PrintUtils.OBSConsoleDebug("&bPlayer&f: " + sender.getName().toString() + "&f accessed their stats.");
	        }

	        return true;
			
		}
		
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) 
	{
		return switch(args.length) 
		{
			case 0 -> List.of("obs");
			case 1 -> List.of("debug","menu");
			case 2 -> 
			{
				yield switch(args[0])
				{
					case "debug" -> List.of();
					case "menu" -> List.of();
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
