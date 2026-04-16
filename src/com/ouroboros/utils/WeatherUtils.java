package com.ouroboros.utils;

import org.bukkit.World;

public class WeatherUtils
{
	public enum WeatherType
	{
		CLEAR,
		RAINY,
		STORMING
	}
	
	public static boolean checkWeather(World world, WeatherType weatherType)
    {
        return switch (weatherType)
        {
        	case CLEAR    -> !world.hasStorm() && !world.isThundering();
        	case RAINY    ->  world.hasStorm() && !world.isThundering();
        	case STORMING ->  world.isThundering();
        };
    }
}
