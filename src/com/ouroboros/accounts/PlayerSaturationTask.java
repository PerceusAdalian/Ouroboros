package com.ouroboros.accounts;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;

public class PlayerSaturationTask
{
	private static final int REGEN_INTERVAL = 100;

    // Saturation thresholds → regen % of max HP per tick
    private static final float WELLFED  = 15f;
    private static final float HUNGRY    = 8f;
    private static final float STARVING   = 3f;

    private static final double WELLFED_REGEN  = 0.04; // 4%
    private static final double HUNGRY_REGEN   = 0.02; // 2%
    private static final double STARVING_REGEN = 0.005; // 0.5%
    
    @SuppressWarnings("null")
	public static void start(JavaPlugin plugin)
    {
        Bukkit.getScheduler().runTaskTimer(plugin, () ->
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                PlayerData data = PlayerData.getPlayer(p.getUniqueId());
                if (data == null) continue;
                if (data.getHP() >= data.getDefaultHP()) continue;

                float sat = p.getSaturation();
                double regenPercent = sat >= WELLFED  ? WELLFED_REGEN
                                    : sat >= HUNGRY   ? HUNGRY_REGEN
                                    : sat >= STARVING ? STARVING_REGEN
                                    : 0;

                if (regenPercent == 0) continue;

                double amount = data.getDefaultHP() * regenPercent;
                PlayerData.heal(p, amount, false);
                
                if (Ouroboros.debug)
                    PrintUtils.OBSConsoleDebug("&e&lRegen&r&f: &b&o" + p.getName()
                        + "&r&f -- &aOK&7 || &fSat: &e" + sat
                        + " &7|| &fRegen: &a+" + String.format("%.1f", amount)
                        + " &7|| &fHP: &f" + data.getHP() + "&7/&f" + data.getDefaultHP());
            }
        }, 0L, REGEN_INTERVAL);
    }
}
