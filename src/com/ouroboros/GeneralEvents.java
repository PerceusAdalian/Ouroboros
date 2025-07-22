package com.ouroboros;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.interfaces.ObsDisplayMain;
import com.ouroboros.mobs.ObsMobHealthbar;
import com.ouroboros.utils.PrintUtils;

public class GeneralEvents implements Listener
{
	public static void register(JavaPlugin plugin) 
    {
        Bukkit.getPluginManager().registerEvents(new Listener() 
        {
        	@EventHandler 
        	public void onJoin(PlayerJoinEvent e) 
        	{
        		Player p = e.getPlayer();
        		
        		if (p.isOp()) 
        		{
        			p.setGameMode(GameMode.CREATIVE);
        		}
        		
        		if (!p.hasPlayedBefore()) 
        		{
        			PrintUtils.OBSFormatPrint(p, "Welcome to The Viel, "+p.getName()+"!");
        		}
        		else 
        		{        			
        			PrintUtils.OBSFormatPrint(p, "Welcome Back "+p.getName());
        		}
        		
        		PlayerData.loadPlayer(p.getUniqueId());
        		ObsDisplayMain.createHud(p);
        		ObsMobHealthbar.addPlayer(e.getPlayer());
        	}
        	
        	@EventHandler
        	public void onQuit(PlayerQuitEvent e) 
        	{
        		Player p = e.getPlayer();
        		ObsMobHealthbar.removePlayer(p);
        		PlayerData.unloadPlayer(p.getUniqueId());
        	}
        	
        }, plugin);
    }
}
