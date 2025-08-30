package com.ouroboros.objects;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.objects.instances.EolBlazeArm;

public class ObjectDropHandler 
{
	public static void register(JavaPlugin plugin) 
    {
        Bukkit.getPluginManager().registerEvents(new Listener() 
        {
			public static HashMap<AbstractObsObject, Boolean> hasBeenRecentlyDropped = new HashMap<>();
			
			@EventHandler
			public void drop(EntityDeathEvent e) 
			{
				if (!(e.getEntity() instanceof LivingEntity)) 
				{
					return;
				}
				
				Random r = new Random();
				final int maxDrops = 2;
				int currentDrops = 0;
				final double dropChance = 0.1599d;
				
				for (AbstractObsObject item : ObjectRegistry.itemRegistry.values()) 
				{
					if (!item.canDrop()) continue;
					if (currentDrops >= maxDrops) break;
				    if (r.nextDouble() >= dropChance) continue;
				    if (hasBeenRecentlyDropped.getOrDefault(item, false)) continue;
			    	e.getDrops().add(item.toItemStack());
			    	hasBeenRecentlyDropped.put(item, true);
			    	currentDrops++;
				}
				
				if (e.getEntityType().equals(EntityType.BLAZE))
				{
					if (r.nextDouble() >= 0.8559d) return;
					AbstractObsObject item = new EolBlazeArm();
					e.getDrops().add(item.toItemStack());
					return;
				}
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> hasBeenRecentlyDropped.clear(), 600L);
			}
        },plugin);
    }
}
