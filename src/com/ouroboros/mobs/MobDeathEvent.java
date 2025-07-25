package com.ouroboros.mobs;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;

public class MobDeathEvent implements Listener
{
	public static void register(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener()
		{
			@EventHandler
			public void onDeath(EntityDeathEvent e) 
			{
				if (!e.getEntity().getPersistentDataContainer().has(AbstractObsMob.OBSMOB)) return;
				if (Ouroboros.debug == true) 
				{
					String string = e.getEntity().getCustomName().toString();
					PrintUtils.OBSConsoleDebug("&fObsMobDeathEvent: &r&f" + string + " || &cDied Successfully");
				}
				return;
			}
		},plugin);
	}
}
