package com.ouroboros.mobs;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
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

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;

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
				if (!(target instanceof LivingEntity)) return;
				if (!target.getPersistentDataContainer().has(MobManager.MOB_DATA_KEY, PersistentDataType.STRING)) return;
				
				MobData data = MobData.getMob(target.getUniqueId());
				if (data == null) return; 
				
//				e.setCancelled(true);
				
				double dmg = e.getFinalDamage();
				if (data.isBreak()) 
				{
					data.breakDamage(dmg, 10);
				}
				else
				{
					data.damage(dmg, true);
				}
				
				if (e instanceof EntityDamageByEntityEvent dmgEvent && dmgEvent.getDamager() instanceof Player)
				{
					BossBar bar = ObsMobHealthbar.bossBars.get(target.getUniqueId());
					if (bar == null) ObsMobHealthbar.initializeHPBar(target, true);
					else ObsMobHealthbar.updateHPBar(target, true);
					
					if (!hpBarMap.containsKey(target.getUniqueId())) 
					{							
						hpBarMap.put(target.getUniqueId(), true);
						Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
						{
							hpBarMap.remove(target.getUniqueId());
							ObsMobHealthbar.hideBossBar(target);
						}, 150);
					}
				}
				
				data.save();
				
				if (data.isDead())
				{
					((Damageable) target).setHealth(0);
					ObsMobHealthbar.removeBossBar(target);
					data.deleteFile();
				}
				else
				{
					((Damageable) target).setHealth(((Attributable) target).getAttribute(Attribute.MAX_HEALTH).getValue());
				}
				
				if (Ouroboros.debug) 
				{
					String name = target.getCustomName();
					PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oDamageEvent&r&f -- &aOK&7 || &fMob: "+
							(name!=null?name:PrintUtils.getFancyEntityName(data.getEntityType()))+"&7 || &fDamage Dealt: &c"+
							dmg+"&7 || &aHP: &f"+data.getHp(false)+"&7/&f"+data.getHp(true)+
							(data.isBreak()?" &7|| &6Break&f: &cTRUE&f":(" &7|| &6Break&f: &aFALSE&7 || &6AR&f: "+
							data.getArmor(false)+"&7/&f"+data.getArmor(true))+" || &o&7END"));
				}
			}
		}, plugin);
	}
}
