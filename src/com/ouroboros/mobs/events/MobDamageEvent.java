package com.ouroboros.mobs.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Trident;
import org.bukkit.entity.WindCharge;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.abilities.instances.combat.ImbueFire;
import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.utils.MobManager;
import com.ouroboros.mobs.utils.MobNameplate;
import com.ouroboros.utils.EntityCategories;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.ResolveCombatElement;

public class MobDamageEvent implements Listener
{
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
				
				ElementType element = ElementType.NEUTRAL;
				double dmg;
				
				//In the event the damage source is as shown below, execute this block
				if (e instanceof EntityDamageByEntityEvent dmgEvent && dmgEvent.getDamager() instanceof Player p)
				{
					if (dmgEvent.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player)
						element = ElementType.PUNCTURE;
					else if (dmgEvent.getDamager() instanceof Trident trident && trident.getShooter() instanceof Player)
						element = ElementType.IMPALE;
					else if (dmgEvent.getDamager() instanceof ThrownPotion potion && potion.getShooter() instanceof Player)
						element = ElementType.ARCANO;
					else if (dmgEvent.getDamager() instanceof Fireball fb && fb.getShooter() instanceof Player)
						element = ElementType.INFERNO;
					else if (dmgEvent.getDamager() instanceof WindCharge wc && wc.getShooter() instanceof Player)
						element = ElementType.AERO;
					else if (dmgEvent.getDamager() instanceof WitherSkull ws && ws.getShooter() instanceof Player)
						element = ElementType.MORTIO;
					else 
					{
						ItemStack held = p.getPlayer().getInventory().getItem(EquipmentSlot.HAND);
						Material m = held != null ? held.getType() : Material.AIR;
						element = ResolveCombatElement.getFromMaterial(m);
					}
					
					dmg = dmgEvent.getFinalDamage();
					
					if (ImbueFire.isImbuedPlayer.containsKey(p.getUniqueId()))
					{
						element = ElementType.INFERNO;
						dmg *= 1.1;
						target.setFireTicks(100);
					}
					
					if (data.isBreak()) 
						data.breakDamage(dmg, 10);
					else 
						data.damage(dmg, true, element);
					
					//Apply relevent effects based on the combat element used.
					EntityEffects.checkFromCombat((LivingEntity) target, element);
					
					//Update their HP bar
					if (target.getCustomName() == null) 
					{
						MobNameplate.build((LivingEntity) target);
						MobNameplate.update((LivingEntity) target);
					}
					else 
					{
						MobNameplate.update((LivingEntity) target);
					}
					
				}
				else //Run normal damage calculations if the damage event is a passive trigger (i.e. fall damage)
				{
					DamageCause cause = e.getCause();
					
					if (cause == DamageCause.ENTITY_EXPLOSION || cause == DamageCause.BLOCK_EXPLOSION)
						element = ElementType.BLAST;
					else if (cause == DamageCause.CAMPFIRE || cause == DamageCause.FIRE || cause == DamageCause.LAVA
							|| cause == DamageCause.FIRE_TICK)
						element = ElementType.INFERNO;
					else if (cause == DamageCause.FALL || cause == DamageCause.FALLING_BLOCK)
						element = ElementType.GEO;
					else if (cause == DamageCause.CONTACT || cause == DamageCause.PROJECTILE || cause == DamageCause.THORNS)
						element = ElementType.PIERCE;
					else if (cause == DamageCause.DRAGON_BREATH || cause == DamageCause.CRAMMING
							|| cause == DamageCause.WORLD_BORDER || cause == DamageCause.VOID
							|| cause == DamageCause.SONIC_BOOM)
						element = ElementType.COSMO;
					else if (cause == DamageCause.DROWNING || cause == DamageCause.SUFFOCATION
							|| cause == DamageCause.WITHER || cause == DamageCause.DRYOUT
							|| cause == DamageCause.STARVATION)
						element = ElementType.MORTIO;
					else if (cause == DamageCause.FALLING_BLOCK)
						element = ElementType.CRUSH;
					else if (cause == DamageCause.FREEZE)
						element = ElementType.GLACIO;
					else if (cause == DamageCause.FLY_INTO_WALL || cause == DamageCause.LIGHTNING)
						element = ElementType.AERO;
					else if (cause == DamageCause.POISON)
						element = ElementType.CORROSIVE;
					else if (cause == DamageCause.MAGIC)
						element = ElementType.ARCANO;
					
					dmg = e.getFinalDamage();
					
					if (data.isBreak()) 
						data.breakDamage(dmg, 10);
					else
						data.damage(dmg, true, element);
					
					MobNameplate.update((LivingEntity) target);
					
					if (Ouroboros.debug) 
					{
						String name = target.getCustomName();
						PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oNormalDamageEvent&r&f -- &aOK&7 || &fMob: "+
								(name!=null?name:PrintUtils.getFancyEntityName(data.getEntityType()))+"&7 || &fDamage Dealt: &c"+
								dmg+"&7 || &aHP: &f"+data.getHp(false)+"&7/&f"+data.getHp(true)+
								(data.isBreak()?" &7|| &6Break&f: &cTRUE&f":(" &7|| &6Break&f: &aFALSE&7 || &6AR&f: "+
								data.getArmor(false)+"&7/&f"+data.getArmor(true))+" || &o&7END"));
					}
				}
				
				data.save(); //Save the mob's data profile
				
				if (data.isDead()) //Check for death status and potential removal
				{
					LivingEntity le = (LivingEntity) target;
					Bukkit.getScheduler().runTaskLater(plugin, () -> le.setHealth(0), 5L);
					MobNameplate.remove(le);
					data.deleteFile();
					return;
				}
				
				if (Ouroboros.debug) 
				{
					String name = target.getCustomName();
					PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oDamageEvent&r&f -- &aOK&7 || &fMob: "+
							(name!=null?name:PrintUtils.getFancyEntityName(data.getEntityType()))+
							"\n                          &7&f- Damage Dealt: &c"+
							dmg+"&7 || &aHP: &f"+data.getHp(false)+"&7/&f"+data.getHp(true)+
							(data.isBreak()?" &7|| &6Break&f: &aTRUE&f":("&7 || &6Break&f: &cFALSE&7 || &6AR&f: "+
							data.getArmor(false)+"&7/&f"+data.getArmor(true)))+
							"\n                          &b&o> DamageType&r&f: "+element.getKey()+
							"\n                          &b&o- Weakness Damage&r&f: "+(EntityCategories.parseUniversalWeakness(target, element)?"&aTRUE&f ":"&cFALSE&f ")+
							"\n                          &b&o- Resistance Damage&r&f: "+(EntityCategories.parseUniversalResistance(target, element)?"&aTRUE&f ":"&cFALSE&f ")+
							"\n                          &b&o- Immunity Damage&r&f: "+(EntityCategories.parseUniversalImmunity(target, element)?"&aTRUE&f ":"&cFALSE&f ")+"|| &o&7END");
				}
			}
		}, plugin);
	}
}
