package com.ouroboros.utils.entityeffects.helpers;

import org.bukkit.scheduler.BukkitTask;

public class ChillEffectData
{
    public final int magnitude;
    public final long expiresAt;
    private final BukkitTask expiryTask;

    public ChillEffectData(int magnitude, long expiresAt, BukkitTask expiryTask) 
    {
        this.magnitude = magnitude;
        this.expiresAt = expiresAt;
        this.expiryTask = expiryTask;
    }

    public void cancel() 
    {
        expiryTask.cancel();
    }

    public long getRemainingTicks() 
    {
        long remaining = expiresAt - System.currentTimeMillis();
        return Math.max(0, remaining / 50);
    }
}