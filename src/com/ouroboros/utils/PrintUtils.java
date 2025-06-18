package com.ouroboros.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PrintUtils
{
	public static String ColorParser(String msg) 
	{
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public static void Print(String msg) 
	{
		Bukkit.getServer().getConsoleSender().sendMessage(ColorParser(msg));
	}
	
	public static void Print(Player player, String msg) 
	{
		player.getPlayer().sendMessage(ColorParser(msg));	
	}
	
	public static void Print(Player player, String...msg) 
	{
		for (String line : msg) 
		{			
			player.getPlayer().sendMessage(ColorParser(line));
		}
	}
	
	public static void OBSFormatPrint(Player player, String msg) 
	{
		Print(player, "&f|&eΩ&r&f| "+msg+" &r&f/&e$&f//");
	}
	
	public static void OBSFormatError(Player player, String msg) 
	{
		Print(player, "&f|&cΩ&r&f| "+msg+" &r&f/&c!&f//");
	}
	
	public static void OBSFormatDebug(Player player, String msg) 
	{
		Print(player.getPlayer(), "&f|&bΩ&r&f| "+msg+ " &r&f/&b?&f//");
	}
	
	public static void OBSConsolePrint(String msg) 
	{
		Print("&f|&eΩ&r&f| "+msg+" &r&f/&e$&f//");
	}
	
	public static void OBSConsoleError(String msg) 
	{
		Print("&f|&cΩ&r&f| "+msg+" &r&f/&c!&f//");
	}
	
	public static void OBSConsoleDebug(String msg) 
	{
		Print("&f|&bΩ&r&f| "+msg+ " &r&f/&b?&f//");
	}
}
