package com.ouroboros.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.utils.PrintUtils;

public class ObsDisplayMain
{
		
    public static void createHud(Player p) 
    {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective nexusHud = board.registerNewObjective("obs_hud", Criteria.DUMMY, 
        		PrintUtils.ColorParser("&r&bΩ &7| "+"&r&fOBS Hud&r&f"+" &r&7|&f &r&bΩ"));
        		nexusHud.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        nexusHud.getScore(PrintUtils.ColorParser("&r&f&lUser&r&f: " + p.getDisplayName())).setScore(0);
        nexusHud.getScore(PrintUtils.ColorParser("&r&7⋖&e₪&r&7⋗&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getFunds(false));
        nexusHud.getScore(PrintUtils.ColorParser("&r&7⋖&cЖ&r&7⋗&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getFunds(true));
       
        p.setScoreboard(board);
    }

    public static void updateHud(Player p) 
    {
    	Scoreboard board = p.getScoreboard();
   	 	Objective obj = board.getObjective(DisplaySlot.SIDEBAR);
   	 	obj.getScore(PrintUtils.ColorParser("&r&f&lUser&r&f: " + p.getDisplayName())).setScore(0);
   	 	obj.getScore(PrintUtils.ColorParser("&r&7⋖&e₪&r&7⋗&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getFunds(false));
   	 	obj.getScore(PrintUtils.ColorParser("&r&7⋖&cЖ&r&7⋗&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getFunds(true));
    }
}
