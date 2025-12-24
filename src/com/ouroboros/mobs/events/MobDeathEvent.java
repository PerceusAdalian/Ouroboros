package com.ouroboros.mobs.events;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.lol.spells.EntityCategoryToSpellement;
import com.lol.spells.instances.Spell;
import com.lol.spells.instances.SpellRegistry;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.EntityCategory;
import com.ouroboros.mobs.utils.MobManager;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.objects.ObjectRegistry;
import com.ouroboros.utils.PrintUtils;

public class MobDeathEvent implements Listener
{
	public static void register(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener()
		{
			public static HashMap<AbstractObsObject, Boolean> hasBeenRecentlyDropped = new HashMap<>();
			public static HashMap<Spell, Boolean> hasBeenRecentlyDropped2 = new HashMap<>();
			@EventHandler
			public void onDeath(EntityDeathEvent e) 
			{
				if (!(e instanceof LivingEntity le)) return;
				if (!e.getEntity().getPersistentDataContainer().has(MobManager.MOB_DATA_KEY)) return;
				
				le.getAttribute(Attribute.MAX_HEALTH).setBaseValue(1);
				
				//Handle drop table
				Random r = new Random();
				final int maxDrops = 2;
				int currentDrops = 0;
				final double dropChance = 0.1599d;
				final double spellDropChance = 0.0999d;
				
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
				
				for (Spell spell : SpellRegistry.spellRegistry.values())
				{
				    if (currentDrops >= maxDrops) break;
				    EntityCategory mobCategory = EntityCategoryToSpellement.getMobCategory(e.getEntityType());
				    if (mobCategory == null) continue;
				    if (!EntityCategoryToSpellement.isElementMatch(spell.getElementType(), mobCategory)) continue;
				    if (r.nextDouble() >= spellDropChance) continue;
				    if (hasBeenRecentlyDropped2.getOrDefault(spell, false)) continue;
				    e.getDrops().add(spell.getAsItemStack(false));
				    hasBeenRecentlyDropped2.put(spell, true);
				    currentDrops++;
				}
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> hasBeenRecentlyDropped.clear(), 600L);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> hasBeenRecentlyDropped2.clear(), 600L);
				
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
