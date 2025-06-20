package com.ouroboros.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.StatType;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

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
	
	public static void PrintToActionBar(Player player, String msg) 
	{
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorParser(msg)));
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
	
	public static String printNextLevelXP(UUID uuid, StatType sType) 
	{
		int level = PlayerData.getPlayer(uuid).getStat(sType, true);
		if (level == 100) 
		{
			return ColorParser("&r&7<&b&oPRESTIGE READY&r&7>");
		}
		return ColorParser(""+PlayerData.getNextLevelXP(uuid, sType));
	}
}
