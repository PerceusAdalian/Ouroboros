package com.ouroboros.mobs;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;

public class MobDamageEvent implements Listener
{
	public static void register(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onHit(EntityDamageEvent e)
			{
				Entity target = e.getEntity();
				if (!(target instanceof LivingEntity)) return;
				if (!target.getPersistentDataContainer().has(AbstractObsMob.OBSMOB)||
						!(target.getPersistentDataContainer().has(MobGenerateEvent.mobKey))) return;				
				
				MobData data = MobData.getMob(target.getUniqueId());
				if (data == null) return; 
				double eventDamage = e.getFinalDamage();
				
				if (data.isBreak()) 
				{
					data.breakDamage(eventDamage, 10);
				}
				else
				{
					data.damage(eventDamage, true);
				}
				
				((Damageable) target).setHealth(((Attributable) target).getAttribute(Attribute.MAX_HEALTH).getValue());
				
				if (e instanceof EntityDamageByEntityEvent)
				{
					EntityDamageByEntityEvent dmgEvent = (EntityDamageByEntityEvent) e;
					if (dmgEvent.getDamager() instanceof Player)
					{
						ObsMobHealthbar.updateHPBar(target, true);
						Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
							ObsMobHealthbar.hideBossBar(target), 150);
					}
				}
				
				if (data.isDead())
				{
					((Damageable) target).setHealth(0);
					ObsMobHealthbar.removeBossBar(target);
					data.deleteFile(target.getUniqueId());
				}
			}
		}, plugin);
	}
}
