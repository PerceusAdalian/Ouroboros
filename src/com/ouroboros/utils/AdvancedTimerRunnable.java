package com.ouroboros.utils;

import java.util.Objects;

import org.bukkit.scheduler.BukkitRunnable;

import com.ouroboros.Ouroboros;

public class AdvancedTimerRunnable 
{
	private BukkitRunnable timerRunnable; // Timer that can be reset
    private final Runnable targetTask;    // Code to run
    private final long ticks;             // Ticks to wait to run after

    /**
    * @param runnable Code to run, written as "() -> { /* Your code here *\/ }"
    * @param ticks Ticks to run code after having elapsed
    *
    * @throws IllegalArgumentException Thrown if ticks < 0
    */
    public AdvancedTimerRunnable(Runnable runnable, long ticks) 
    {
        this.targetTask = Objects.requireNonNull(runnable); // Make sure that the task is not null

        if (ticks < 0) // Validate that ticks are positive
        {
            throw new IllegalArgumentException("Ticks must be at least 1");
        }
        this.ticks = ticks;
    }

    /**
    * Resets the timer.
    */
    public void reset()
    {
        // Check if a timer exists, if it does, cancel and reset it
        if (this.timerRunnable != null)
        {
            this.timerRunnable.cancel(); // Cancel the current timer
            this.timerRunnable = null; // Delete the current timer
        }

        this.timerRunnable = timerTask(); // New timer task instance
        this.timerRunnable.runTaskLater(Ouroboros.instance, this.ticks); 
    }

    /**
    * Creates a new task to run the given code.
    *
    * @returns A new BukkitRunnable
    */
    private BukkitRunnable timerTask()
    {
        // This is called an "anonymous" class
        return new BukkitRunnable() 
        {
            @Override
            public void run() 
            {
                if (!this.isCancelled())
                {
                    targetTask.run();
                }
            }
        };
    }
}
