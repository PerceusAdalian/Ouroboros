package com.ouroboros.accounts;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class RestoreArmorTask
{
    // Call RestoreArmorTask.markHit(player) from PlayerDamageEvent after any damage lands
    public static final Map<UUID, Long> lastHitTime = new ConcurrentHashMap<>();

    private static final long COOLDOWN_MS = 60000L; // 1 minute

    public static void markHit(Player p)
    {
        lastHitTime.put(p.getUniqueId(), System.currentTimeMillis());
    }

    @SuppressWarnings("null")
	public static void start(Plugin plugin)
    {
        Bukkit.getScheduler().runTaskTimer(plugin, () ->
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                PlayerData data = PlayerData.getPlayer(p.getUniqueId());
                if (data == null) continue;
                if (data.isBreak()) continue;
                if (data.getArmor() >= data.getDefaultArmor()) continue;

                long lastHit = lastHitTime.getOrDefault(p.getUniqueId(), 0L);
                if (System.currentTimeMillis() - lastHit < COOLDOWN_MS) continue;

                data.setArmor(data.getDefaultArmor());
                data.save();
        		
                ObsParticles.drawWisps(p.getLocation(), 3, 3, 5, Particle.WAX_ON, null);
                EntityEffects.playSound(p, Sound.ITEM_BONE_MEAL_USE, SoundCategory.MASTER);
                PrintUtils.PrintToActionBar(p, "&6"+Symbols.ARMOR+" &a&oRestored");
                PlayerHud.update(p);

                if (Ouroboros.debug)
                    PrintUtils.OBSConsoleDebug("&e&lTask&r&f: &b&oRestoreArmor&r&f -- &aOK&7 (Player: "
                        + p.getName() + " | AR restored to " + data.getDefaultArmor() + ")");
            }
        }, 0L, 20L);
    }
}