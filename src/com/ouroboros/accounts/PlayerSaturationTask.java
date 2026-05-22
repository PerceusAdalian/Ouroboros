package com.ouroboros.accounts;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;

public class PlayerSaturationTask
{
	private static final int REGEN_INTERVAL = 100;

    // Saturation thresholds → regen % of max HP per tick
	private static final int WELLFED  = 16;
    private static final int HUNGRY   = 8;
    private static final int STARVING = 3;

    private static final double WELLFED_REGEN  = 0.04; // 4%
    private static final double HUNGRY_REGEN   = 0.02; // 2%
    private static final double STARVING_REGEN = 0.005; // 0.5%
    
    public static void start(JavaPlugin plugin)
    {
        Bukkit.getScheduler().runTaskTimer(plugin, () ->
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
            	if (p.isDead()) continue;
                if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) continue;
                
                PlayerData data = PlayerData.getPlayer(p.getUniqueId());
                if (data == null) continue;
                if (data.getHP() >= data.getDefaultHP()) continue;

                int food = p.getFoodLevel();
                double regenPercent = food >= WELLFED  ? WELLFED_REGEN
                                    : food >= HUNGRY   ? HUNGRY_REGEN
                                    : food >= STARVING ? STARVING_REGEN
                                    : 0;
                if (regenPercent == 0) continue;

                double amount = data.getDefaultHP() * regenPercent;
                PlayerData.heal(p, amount, false);
                
                if (Ouroboros.debug)
                    PrintUtils.OBSConsoleDebug("&e&lRegen&r&f: &b&o" + p.getName()
                        + "&r&f -- &aOK&7 || &fSat: &e" + food
                        + " &7|| &fRegen: &a+" + String.format("%.1f", amount)
                        + " &7|| &fHP: &f" + data.getHP() + "&7/&f" + data.getDefaultHP());
            }
        }, 0L, REGEN_INTERVAL);
    }
}
