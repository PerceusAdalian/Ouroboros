package com.ouroboros;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.interfaces.ObsDisplayMain;

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
        			//PH
        		}
        		
        		if (!p.hasPlayedBefore()) 
        		{
        			//PH
        		}
        		PlayerData.loadPlayer(p.getUniqueId());
        		ObsDisplayMain.createHud(p);
        	}
        	
        	@EventHandler
        	public void onQuit(PlayerQuitEvent e) 
        	{
        		Player p = e.getPlayer();
        		
        		PlayerData.unloadPlayer(p.getUniqueId());
        	}
        	
        }, plugin);
    }
}
