package com.ouroboros.utils;

public class NumberUtils
{
	public static double lerp(double minVal, double maxVal, int clampValue, int minLevel, int maxLevel)
	{
	    double t = Math.min(1.0, (double)(clampValue - minLevel) / (maxLevel - minLevel));
	    return minVal + t * (maxVal - minVal);
	}
	
	public static double lerp(double floor, double ceiling, double t)
    {
        return floor + (ceiling - floor) * t;
    }
	
	public static int clamp(int val, int min, int max)
	{
		return Math.max(min, Math.min(val, max));
	}
	
	public static double clamp(double val, double min, double max)
	{
		return Math.max(min, Math.min(val, max));
	}
	
	public static int percent(double percent, int val)
	{
		return (int) (percent / 100.0) * val;
	}
	
	public static double percent(double percent, double val)
	{
		return (percent / 100.0) * val;
	}
	
	public static long percent(double percent, long val)
	{
		return (long) ((percent / 100.0) * val);
	}
}
