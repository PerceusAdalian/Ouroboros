package com.ouroboros.utils.entityeffects.helpers;

import org.bukkit.scheduler.BukkitTask;

public class BulwarkData
{
	public int magnitude;
	public int seconds;
	private BukkitTask expiryTask;
	
	public BulwarkData(int magnitude, int seconds, BukkitTask expiryTask)
	{
		this.magnitude = magnitude;
		this.seconds = seconds;
		this.expiryTask = expiryTask;
	}
	
	public void cancel() 
    {
        expiryTask.cancel();
    }
}
