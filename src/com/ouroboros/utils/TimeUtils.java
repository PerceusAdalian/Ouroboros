package com.ouroboros.utils;

import org.bukkit.World;

public class TimeUtils
{
	public enum Timeframe
	{
		DAY,
		NIGHT
	}

    public static boolean checkTime(World world, Timeframe timeframe)
    {
        long time = world.getTime();
        return switch (timeframe)
        {
            case Timeframe.DAY   -> time < 13000;
            case Timeframe.NIGHT -> time >= 13000;
        };
    }
}