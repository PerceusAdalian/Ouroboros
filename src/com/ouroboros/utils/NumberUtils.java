package com.ouroboros.utils;

public class NumberUtils
{
	public static double lerp(double minVal, double maxVal, int clampValue, int minLevel, int maxLevel)
	{
	    double t = Math.min(1.0, (double)(clampValue - minLevel) / (maxLevel - minLevel));
	    return minVal + t * (maxVal - minVal);
	}
}
