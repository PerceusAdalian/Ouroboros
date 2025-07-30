package com.ouroboros.mobs;

import java.util.HashMap;
import java.util.UUID;

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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.ouroboros.Ouroboros;

public class MobDamageEvent implements Listener
{
	public static HashMap<UUID, Boolean> hpBarMap = new HashMap<>();
	
	public static void register(JavaPlugin plugin)
	{
		
		Bukkit.getPluginManager().registerEvents(new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onHit(EntityDamageEvent e)
			{
				Entity target = e.getEntity();
				if (!(target instanceof LivingEntity le)) return;
				if (!le.getPersistentDataContainer().has(AbstractObsMob.OBSMOB, PersistentDataType.STRING)) return;
				
				MobData data = MobData.getMob(le.getUniqueId());
				if (data == null) return; 
				
				e.setCancelled(true);
				
				double dmg = e.getFinalDamage();
				if (data.isBreak()) 
				{
					data.breakDamage(dmg, 10);
				}
				else
				{
					data.damage(dmg, true);
				}
				
				if (e instanceof EntityDamageByEntityEvent dmgEvent && dmgEvent.getDamager() instanceof Player p)
				{
					le.playHurtAnimation(0);
					Vector direction = target.getLocation().toVector().subtract(p.getLocation().toVector()).normalize().multiply(0.4);
					target.setVelocity(direction.setY(0.4));	
					
					ObsMobHealthbar.updateHPBar(le, true);
					if (!hpBarMap.containsKey(le.getUniqueId())) 
					{							
						hpBarMap.put(le.getUniqueId(), true);
						Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
						{
							hpBarMap.remove(le.getUniqueId());
							ObsMobHealthbar.hideBossBar(le);
						}, 150);
					}
				}
				
				data.save();
				
				if (data.isDead())
				{
					((Damageable) le).setHealth(0);
					ObsMobHealthbar.removeBossBar(le);
					data.deleteFile();
				}
				else
				{
					((Damageable) le).setHealth(((Attributable) le).getAttribute(Attribute.MAX_HEALTH).getValue());
				}
			}
		}, plugin);
	}
}
