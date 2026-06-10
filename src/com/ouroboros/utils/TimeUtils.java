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
    
    public static void setTime(World world, Timeframe timeframe)
    {
    	switch (timeframe)
    	{
	    	case DAY -> world.setTime(6000);
	    	case NIGHT -> world.setTime(18000);
    	}
    }
}