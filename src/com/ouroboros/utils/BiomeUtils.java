package com.ouroboros.utils;

import org.bukkit.entity.Player;

public class BiomeUtils
{
    public static float getPlayerBiomeTemp(Player player)
    {
        return (float) player.getLocation().getBlock().getTemperature();
    }

    public enum BiomeTemperatureCategory
    {
        FROZEN, COLD, TEMPERATE, HOT
    }

    public static BiomeTemperatureCategory getTempCategory(Player player)
    {
        float temp = getPlayerBiomeTemp(player);

        if (temp < 0.15f)      return BiomeTemperatureCategory.FROZEN;
        else if (temp < 0.5f)  return BiomeTemperatureCategory.COLD;
        else if (temp < 0.95f) return BiomeTemperatureCategory.TEMPERATE;
        else                   return BiomeTemperatureCategory.HOT;
    }
}