package com.ouroboros.mobs.events;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.lol.spells.EntityCategoryToSpellement;
import com.lol.spells.instances.Spell;
import com.lol.spells.instances.SpellRegistry;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.EntityCategory;
import com.ouroboros.mobs.utils.MobManager;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.objects.ObjectRegistry;
import com.ouroboros.objects.instances.TearOfLumina;
import com.ouroboros.utils.PrintUtils;

public class MobDeathEvent implements Listener
{
	public static void register(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener()
		{
			public static HashMap<AbstractObsObject, Boolean> objectDropsRegistry = new HashMap<>();
			public static HashMap<Spell, Boolean> spellDropsRegistry = new HashMap<>();

			@EventHandler
			public void onDeath(EntityDeathEvent e) 
			{
			    LivingEntity le = e.getEntity();
			    if (!le.getPersistentDataContainer().has(MobManager.MOB_DATA_KEY)) return;
			    
			    le.getAttribute(Attribute.MAX_HEALTH).setBaseValue(1);
			    
			    // Handle drop table
			    Random r = new Random();
			    final int maxDrops = 2;
			    int currentDrops = 0;
			    final double dropChance = 0.1599d;
			    final double spellDropChance = 0.0999d;
			    
			    if (r.nextDouble() < 0.4499d)
			    {
			        ItemStack tearStack = new TearOfLumina().toItemStack();
			        e.getDrops().add(tearStack);
			    }
			    
			    // Item drops
			    for (AbstractObsObject item : ObjectRegistry.itemRegistry.values()) 
			    {
			        if (!item.canDrop()) continue;
			        if (currentDrops >= maxDrops) break;
			        if (r.nextDouble() >= dropChance) continue;
			        if (objectDropsRegistry.getOrDefault(item, false)) continue;
			        
			        e.getDrops().add(item.toItemStack());
			        objectDropsRegistry.put(item, true);
			        currentDrops++;
			    }
			    
			    // Spell drops
			    EntityCategory mobCategory = EntityCategoryToSpellement.getMobCategory(e.getEntityType());
			    if (mobCategory != null) // Moved null check outside loop
			    {
			        for (Spell spell : SpellRegistry.spellRegistry.values())
			        {
			            if (currentDrops >= maxDrops) break;
			            if (!EntityCategoryToSpellement.isElementMatch(spell.getElementType(), mobCategory)) continue;
			            if (r.nextDouble() >= spellDropChance) continue;
			            if (spellDropsRegistry.getOrDefault(spell, false)) continue;
			            
			            e.getDrops().add(spell.getAsItemStack(false));
			            spellDropsRegistry.put(spell, true);
			            currentDrops++;
			        }
			    }
			    
			    // Clear recently dropped maps after 30 seconds
			    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> {
			        objectDropsRegistry.clear();
			        spellDropsRegistry.clear();
			    }, 600L);
			    
			    if (Ouroboros.debug) 
			    {
			        String mobName = le.getCustomName() != null ? le.getCustomName() : le.getType().name();
			        PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oDamageEvent&r&f -- &aOK&7 Mob: " + mobName + " || &c&oDied Successfully");
			    }
			}
		},plugin);
	}
}
