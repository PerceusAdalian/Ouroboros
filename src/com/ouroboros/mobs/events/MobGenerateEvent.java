package com.ouroboros.mobs.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.PrintUtils;

public class MobGenerateEvent implements Listener
{
	
	public static void register(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener()
		{
			@EventHandler
			public void onSpawn(EntitySpawnEvent e) 
			{
				if(!Ouroboros.isActive()) 
				{
					e.setCancelled(true);
					return;
				}
				
				Entity entity = e.getEntity();
				if (!(entity instanceof LivingEntity)) return;
				
				MobData.loadMobData(entity);
				MobData data = MobData.getMob(entity.getUniqueId());
				if (data == null) return;
				
				data.initialize(entity);
				MobData.setMobVisuals(entity, data);
				
				if (Ouroboros.debug == true) 
				{
					String string = entity.getCustomName().toString();
					PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oMobGenerateEvent&r&f -- &aOK &7Mob: " + string + " || &a&oGenerated Successfully");
				}
			}
		}, plugin);
	}
}
