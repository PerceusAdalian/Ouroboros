package com.ouroboros.utils;

import java.util.function.Consumer;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class OBStandardTimer 
{	
	/**@param plugin A given plugin instance.
	 * @param taskAction Task Action initialized with the standard () -> {} function.
	 * @param intervalTicks Ticks at which to compare to cancelTicks, and for every taskAction to execute code.
	 * @param cancelTicks Ticks at which to cancel the runnable.
	 */
	public static void runWithCancel(Plugin plugin, Consumer<BukkitRunnable> taskAction, int intervalTicks, int cancelTicks) 
	{
        new BukkitRunnable() 
        {
            private int elapsedTicks = 0; // Track elapsed time

            @Override
            public void run() 
            {
                if (elapsedTicks >= cancelTicks) 
                { 
                    this.cancel(); // Cancel after the defined time
                    return;
                }

                taskAction.accept(this); // Execute the provided action

                elapsedTicks += intervalTicks; // Increment time
            }
        }.runTaskTimer(plugin, 0L, intervalTicks);
    }
}