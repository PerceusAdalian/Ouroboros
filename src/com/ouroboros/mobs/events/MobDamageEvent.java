package com.ouroboros.mobs.events;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Trident;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.utils.AffinityRegistry;
import com.ouroboros.mobs.utils.MobAffinity;
import com.ouroboros.mobs.utils.MobManager;
import com.ouroboros.mobs.utils.ObsMobHealthbar;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.ResolveCombatElement;

public class MobDamageEvent implements Listener
{
	public static HashMap<UUID, Boolean> hpBarMap = new HashMap<>();
	
	public static void register(JavaPlugin plugin)
	{
		/**
		 * @TODO: Hookup damage event to compare against mob 
		 * 		  affinities and calculate damage that way instead of vanilla.
		 */
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
				
				//In the event the damage source is as shown below, execute this block
				if (e instanceof EntityDamageByEntityEvent dmgEvent)
				{
					ElementType element = ElementType.NEUTRAL;
					if (dmgEvent.getDamager() instanceof Player p)
					{
						ItemStack held = p.getPlayer().getInventory().getItem(EquipmentSlot.HAND);
						Material m = held != null ? held.getType() : Material.AIR;
						element = ResolveCombatElement.getFromMaterial(m);
					}
					else if (dmgEvent.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player)
					{
						element = ElementType.PUNCTURE;
					}
					else if (dmgEvent.getDamager() instanceof Trident trident && trident.getShooter() instanceof Player)
					{
						element = ElementType.IMPALE;
					}
					else if (dmgEvent.getDamager() instanceof ThrownPotion potion && potion.getShooter() instanceof Player)
					{
						element = ElementType.ARCANO;
					}
					
					//Find element type of the damage source arbitrarily through ElementType and compare to MobAffinity
					MobAffinity affinity = AffinityRegistry.getAffinity(target.getType());
					
					//Run damage calculations and check the mob's affinity
					double dmg = dmgEvent.getFinalDamage();
					if (!EntityEffects.isVoidedRegistry.containsKey(target))
					{
						if (affinity.immuneTo(element)) dmg = 0;
						else if (affinity.resists(element)) dmg *= 0.5;
					}
					else if (affinity.weakTo(element)) dmg *= 1.5;
					
					if (data.isBreak()) 
						data.breakDamage(dmg, 10);
					else 
						data.damage(dmg, true);

					//Apply relevent effects based on the combat element used.
					EntityEffects.checkFromCombat((LivingEntity)target, element);
					
					//Update their HP bar
					BossBar bar = ObsMobHealthbar.bossBars.get(target.getUniqueId());
					if (bar == null) ObsMobHealthbar.initializeHPBar(target, true);
					else ObsMobHealthbar.updateHPBar(target, true);
					
					//And mark for removal later
					if (!hpBarMap.containsKey(target.getUniqueId())) 
					{							
						hpBarMap.put(target.getUniqueId(), true);
						Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
						{
							hpBarMap.remove(target.getUniqueId());
							ObsMobHealthbar.hideBossBar(target);
						}, 150);
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
				else //Run normal damage calculations if the damage event is a passive trigger (i.e. fall damage)
				{
					double dmg = e.getFinalDamage();

					if (data.isBreak()) 
						data.breakDamage(dmg, 10);
					else
						data.damage(dmg, true);
					
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
				
				data.save(); //Save the mob's data profile
				
				if (data.isDead()) //Check for death status and potential removal
				{
					((Damageable) target).setHealth(0);
					ObsMobHealthbar.removeBossBar(target);
					data.deleteFile();
				}
				else //Or heal them to full
				{
					((Damageable) target).setHealth(((Attributable) target).getAttribute(Attribute.MAX_HEALTH).getValue());
				}
				
			}
		}, plugin);
	}
}
