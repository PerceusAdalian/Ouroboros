package com.ouroboros.utils;

import java.util.Random;

public class Chance 
{
	/**
	 * @param percentValue
	 */
	public static boolean of(double percentValue)
	{
		Random random = new Random();
		return random.nextDouble() < Math.max(0, Math.min(percentValue, 100)) / 100;
	}
}
