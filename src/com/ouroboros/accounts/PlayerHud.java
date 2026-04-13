package com.ouroboros.accounts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;

public class PlayerHud
{
		
    public static void createHud(Player p) 
    {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective hud = board.registerNewObjective("obs_hud", Criteria.DUMMY, 
        		PrintUtils.ColorParser("&7|    &bΩBS &fHud    &7|"));
        		hud.setDisplaySlot(DisplaySlot.SIDEBAR);
        
        hud.getScore(PrintUtils.ColorParser("&r&f&lUser&r&f: " + p.getDisplayName()));
        hud.getScore(PrintUtils.ColorParser("&r&7⋖&e₪&r&7⋗&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getFunds(false));
        hud.getScore(PrintUtils.ColorParser("&r&7⋖&cЖ&r&7⋗&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getFunds(true));
        hud.getScore(PrintUtils.ColorParser("&r&7⋞&b۞&r&7⋟&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getLuminite());
        hud.getScore(PrintUtils.ColorParser("&r&7⋖&6♻&7⋗&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getScrap());
        p.setScoreboard(board);
    }

    public static void updateHud(Player p) 
    {
    	Scoreboard board = p.getScoreboard();
   	 	Objective obj = board.getObjective(DisplaySlot.SIDEBAR);
   	 	obj.getScore(PrintUtils.ColorParser("&r&f&lUser&r&f: " + p.getDisplayName()));
   	 	obj.getScore(PrintUtils.ColorParser("&r&7⋖&e₪&r&7⋗&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getFunds(false));
   	 	obj.getScore(PrintUtils.ColorParser("&r&7⋖&cЖ&r&7⋗&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getFunds(true));
        obj.getScore(PrintUtils.ColorParser("&r&7⋞&b۞&r&7⋟&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getLuminite());
        obj.getScore(PrintUtils.ColorParser("&r&7⋖&6♻&7⋗&r&f")).setScore(PlayerData.getPlayer(p.getUniqueId()).getScrap());
        
   	 	if (Ouroboros.debug) PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oUpdateHud&r&f -- &aOK&7 (Player: "+p.getName()+")");
    }
}

// ⋖⋗