package com.ouroboros.mobs;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;

public class MobGenerateEvent implements Listener
{
	public static final NamespacedKey mobKey = new NamespacedKey(Ouroboros.instance, "mob_key");
	
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
				entity.getPersistentDataContainer().set(MobGenerateEvent.mobKey, PersistentDataType.STRING, data.getUUID().toString());
				entity.getPersistentDataContainer().set(AbstractObsMob.OBSMOB, PersistentDataType.STRING, data.getName());
				MobData.setMobVisuals(entity, data);
				
				if (Ouroboros.debug == true) 
				{
					String string = entity.getCustomName().toString();
					PrintUtils.OBSConsoleDebug("&fMobGenerateEvent: &f" + string + " || &aGenerated Successfully");
				}
			}
		}, plugin);
	}
}
