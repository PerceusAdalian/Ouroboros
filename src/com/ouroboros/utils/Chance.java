package com.ouroboros.utils;

import java.util.Random;

public class Chance 
{
	public static boolean of(double value)
	{
		Random random = new Random();
		return random.nextDouble() < Math.max(0, Math.min(value, 100)) / 100;
	}
}
