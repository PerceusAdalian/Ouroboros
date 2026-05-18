package com.ouroboros.accounts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;

public class PlayerHud
{
    // Sidebar sort order — higher = closer to top
    private static final int SLOT_USER    =  8;
    private static final int SLOT_SEP_TOP =  7;
    private static final int SLOT_HP      =  6;
    private static final int SLOT_ARMOR   =  5;
    private static final int SLOT_SEP_BOT =  4;
    private static final int SLOT_FUNDS   =  3;
    private static final int SLOT_DEBT    =  2;
    private static final int SLOT_LUM     =  1;
    private static final int SLOT_SCRAP   =  0;

    private static final String SEP_A = PrintUtils.ColorParser("&7--------{ &bVitals&7 }--------");
    private static final String SEP_B = PrintUtils.ColorParser("&7-------{ &bAccount&7 }-------");

    public static void create(Player p)
    {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective hud = board.registerNewObjective("obs_hud", Criteria.DUMMY,
                PrintUtils.ColorParser("&7|    &bΩBS &fPlayer HUD    &7|"));
        hud.setDisplaySlot(DisplaySlot.SIDEBAR);

        PlayerData data = PlayerData.getPlayer(p.getUniqueId());
        
        hud.getScore(PrintUtils.ColorParser("&r&f&lUser&r&f: " + PrintUtils.getFavoriteColor(data)+"&l"+p.getDisplayName())).setScore(SLOT_USER);
        hud.getScore(SEP_A).setScore(SLOT_SEP_TOP);
        hud.getScore(buildArmorEntry(data)).setScore(SLOT_ARMOR);
        hud.getScore(buildHPEntry(data)).setScore(SLOT_HP);
        hud.getScore(SEP_B).setScore(SLOT_SEP_BOT);
        hud.getScore(buildFundsEntry(data)).setScore(SLOT_FUNDS);
        hud.getScore(buildDebtEntry(data)).setScore(SLOT_DEBT);
        hud.getScore(buildLuminiteEntry(data)).setScore(SLOT_LUM);
        hud.getScore(buildScrapEntry(data)).setScore(SLOT_SCRAP);

        p.setScoreboard(board);
    }

    public static void update(Player p)
    {
        Scoreboard board = p.getScoreboard();
        Objective obj = board.getObjective("obs_hud");
        
        if (obj == null) 
        { 
        	create(p); 
        	return; 
        }

        PlayerData data = PlayerData.getPlayer(p.getUniqueId());

        refreshEntry(board, obj, PrintUtils.ColorParser("&r&f&lUser&r&f: " + PrintUtils.getFavoriteColor(data)+"&l"+p.getDisplayName()), SLOT_USER);
        refreshEntry(board, obj, buildArmorEntry(data),    SLOT_ARMOR);
        refreshEntry(board, obj, buildHPEntry(data),       SLOT_HP);
        refreshEntry(board, obj, buildFundsEntry(data),    SLOT_FUNDS);
        refreshEntry(board, obj, buildDebtEntry(data),     SLOT_DEBT);
        refreshEntry(board, obj, buildLuminiteEntry(data), SLOT_LUM);
        refreshEntry(board, obj, buildScrapEntry(data),    SLOT_SCRAP);

        if (Ouroboros.debug)
            PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oUpdateHud&r&f -- &aOK&7 (Player: " + p.getName() + ")");
    }

    // -- Entry builders --

    private static String buildHPEntry(PlayerData data)
    {
        double hp = data.getHP(), max = data.getDefaultHP();
        String color = hp > max * 0.5 ? "&a" : hp > max * 0.25 ? "&e" : "&c";
        return PrintUtils.ColorParser("&r&7⟨&c"+Symbols.HP+"&7 " + color + String.format("%.2f", hp) + "&7/" + max + "⟩");
    }

    private static String buildArmorEntry(PlayerData data)
    {
        int armor = data.getArmor(), max = data.getDefaultArmor();
        if (data.isBreak())
            return PrintUtils.ColorParser("&r&7⟨&6"+Symbols.ARMOR+"&7⟩ [&c&lBREAK&7] "+"⟩");
        String color = "&6";
        return PrintUtils.ColorParser("&r&7⟨&6"+Symbols.ARMOR+"&7 " + color + armor + "&7/" + max+"⟩");
    }

    private static String buildFundsEntry(PlayerData data)
    {
    	int funds = data.getFunds(false);
    	String color = funds > 0 ? "&e" : "&7";
        return PrintUtils.ColorParser("&r&7⋖&e"+Symbols.MONEY+"&7: " + color + funds + "&7⋗");
    }

    private static String buildDebtEntry(PlayerData data)
    {
        int debt = data.getFunds(true);
        String color = debt > 0 ? "&c" : "&7";
        return PrintUtils.ColorParser("&r&7⋖&c"+Symbols.DEBT+"&7: " + color + debt + "&7⋗");
    }

    private static String buildLuminiteEntry(PlayerData data)
    {
        return PrintUtils.ColorParser("&r&7⋞&b"+Symbols.LUMINITE+"&7: &f" + data.getLuminite()+"&7⋟");
    }

    private static String buildScrapEntry(PlayerData data)
    {
        return PrintUtils.ColorParser("&r&7⋖&6"+Symbols.SCRAP+"&7: &f" + data.getScrap()+"&7⋗");
    }

    private static void refreshEntry(Scoreboard board, Objective obj, String newEntry, int slot)
    {
        board.getEntries().stream()
             .filter(e -> obj.getScore(e).getScore() == slot)
             .forEach(board::resetScores);
        obj.getScore(newEntry).setScore(slot);
    }
}