package com.ouroboros.accounts;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
    public static final Map<UUID, Long> lastHitTick = new ConcurrentHashMap<>();
    private static long currentTick = 0L;

    private static final long COOLDOWN_TICKS = 1200L;

    public static void markHit(Player p)
    {
    	if (lastHitTick.containsKey(p.getUniqueId())) lastHitTick.remove(p.getUniqueId());
        lastHitTick.put(p.getUniqueId(), currentTick);
    }

    public static void start(Plugin plugin)
    {
    	Bukkit.getScheduler().runTaskTimer(plugin, () ->
        {
            currentTick++;

            for (Player p : Bukkit.getOnlinePlayers())
            {
            	if (p == null) continue;

            	if (p.isDead()) continue;
                if (p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR) continue;
                
            	PlayerData data = PlayerData.getPlayer(p.getUniqueId());
            	if (data == null) continue;
            	if (data.getArmor() >= data.getDefaultArmor()) continue;
            	if (data.isBreak()) continue;

                long lastHit = lastHitTick.getOrDefault(p.getUniqueId(), 0L);
                if (currentTick - lastHit < COOLDOWN_TICKS) continue;

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
        }, 0L, 1L); 
    }
}