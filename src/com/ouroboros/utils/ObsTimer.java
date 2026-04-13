package com.ouroboros.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.ouroboros.utils.entityeffects.EntityEffects;

public class ObsTimer 
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
	
	private static Map<UUID, Integer> timer = new HashMap<>();
	private static Map<UUID, BukkitTask> taskHolders = new HashMap<>();
	public static void startCountdown(Player player, int seconds, Plugin plugin, Consumer<Player> consumer)
	{
	    UUID uuid = player.getUniqueId();
	    timer.put(uuid, seconds);

	    int beepThreshold = seconds * 20 / 100;
	    int[][] thresholds = 
	    {
	        {seconds * 20/100, '6'},
	        {seconds * 40/100, 'e'},
	        {seconds * 60/100, '2'},
	        {seconds * 80/100, 'a'}
	    };

	    BukkitTask[] taskHolder = new BukkitTask[1];
	    taskHolder[0] = Bukkit.getScheduler().runTaskTimer(plugin, () ->
	    {
	        int secondsRemaining = timer.get(uuid);

	        char color = 'c';
	        for (int[] t : thresholds) if (secondsRemaining <= t[0]) { color = (char) t[1]; break; }

	        if (secondsRemaining <= beepThreshold)
	            EntityEffects.playSound(player, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT);

	        PrintUtils.PrintToActionBar(player, "&" + color + secondsRemaining + "&7s remaining..");

	        if (secondsRemaining <= 0)
	        {
	            taskHolder[0].cancel();
	            taskHolders.remove(uuid);
	            timer.remove(uuid);
	            consumer.accept(player);
	            return;
	        }

	        timer.put(uuid, secondsRemaining - 1);

	    }, 0, 20);
	    
	    taskHolders.put(uuid, taskHolder[0]);
	}
	
	public static void cancelCountdown(Player player)
	{
		UUID uuid = player.getUniqueId();
		if (taskHolders.containsKey(uuid))
		{
			BukkitTask task = taskHolders.remove(uuid);
			task.cancel();
			timer.remove(uuid);
		}
	}
	
	public static boolean hasActiveCountdown(Player player)
	{
	    return taskHolders.containsKey(player.getUniqueId());
	}
	
}