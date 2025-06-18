package com.ouroboros;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.PrintUtils;

public class Ouroboros extends JavaPlugin
{
	public static Ouroboros instance; 
	public static boolean debug;
	
	@Override
	public void onEnable()
	{
		instance = this;
		debug = false;
		
		this.getCommand("obs").setExecutor(new ObsCommand());;
		
		GuiHandler.registerEvent(instance);
		GeneralEvents.register(instance);
		
		File playerDataFolder = new File(getDataFolder(), "playerdata");
	    if (!playerDataFolder.exists()) playerDataFolder.mkdirs();
		
		PrintUtils.OBSConsolePrint("&fOuroboros -- &aOK");
	}
	
	@Override
	public void onDisable() 
	{
		for (Player p : Bukkit.getOnlinePlayers()) 
		{
			if (p.isOnline()) 
			{
				PlayerData.save(p);
			}
		}
		
		PrintUtils.OBSConsolePrint("&fOuroboros -- &b&oDisabling..");
	}
}
