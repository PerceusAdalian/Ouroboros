package com.ouroboros.mobs.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.utils.MobManager;
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
				if (!e.getEntity().getPersistentDataContainer().has(MobManager.MOB_DATA_KEY)) return;
				if (Ouroboros.debug == true) 
				{
					String string = e.getEntity().getCustomName().toString();
					PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oDamageEvent&r&f -- &aOK&7 Mob: " + string + " || &c&oDied Successfully");
				}
				return;
			}
		},plugin);
	}
}
