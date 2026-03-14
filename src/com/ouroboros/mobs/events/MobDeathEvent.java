package com.ouroboros.mobs.events;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.lol.spells.instances.Spell;
import com.lol.spells.instances.SpellRegistry;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.EntityCategory;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.utils.MobManager;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.objects.ObjectRegistry;
import com.ouroboros.objects.instances.AeroEssence;
import com.ouroboros.objects.instances.CelestioEssence;
import com.ouroboros.objects.instances.CosmoEssence;
import com.ouroboros.objects.instances.GeoEssence;
import com.ouroboros.objects.instances.GlacioEssence;
import com.ouroboros.objects.instances.HeresioEssence;
import com.ouroboros.objects.instances.InfernoEssence;
import com.ouroboros.objects.instances.MortioEssence;
import com.ouroboros.objects.instances.TearOfLumina;
import com.ouroboros.utils.Chance;
import com.ouroboros.utils.EntityCategoryToSpellement;
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
			    final int maxDrops = 3;
			    final int maxSpellDrops = 1;
			    final int maxEssenceDrops = 4;
			    int currentDrops = 0;
			    int currentSpellDrops = 0;
			    
			    if (Chance.of(50))
			    {
			        ItemStack tearStack = new TearOfLumina().toItemStack();
			        e.getDrops().add(tearStack);
			    }
			    
			    // Item drops
			    for (AbstractObsObject item : ObjectRegistry.itemRegistry.values()) 
			    {
			        if (!item.canDrop()) continue;
			        if (currentDrops >= maxDrops) break;
			        if (!Chance.of(65)) continue;
			        if (objectDropsRegistry.getOrDefault(item, false)) continue;
			        
			        e.getDrops().add(item.toItemStack());
			        objectDropsRegistry.put(item, true);
			        currentDrops++;
			    }
			    
			    // Spell & Essence drops
			    EntityCategory mobCategory = EntityCategoryToSpellement.getMobCategory(e.getEntityType());
			    MobData data = MobData.getMob(le.getUniqueId());
			    if (mobCategory != null) // Moved null check outside loop
			    {
			        for (Spell spell : SpellRegistry.spellRegistry.values())
			        {
			        	if (currentSpellDrops >= maxSpellDrops) break;
			        	if (!EntityCategoryToSpellement.isElementMatch(spell.getElementType(), mobCategory)) continue;
			        	if (!Chance.of(50)) continue;
			            if (spell.getRarity().getRarity() > (data == null ? 0 : Rarity.getRarityForMobLevel(data.getLevel()))) continue;
			            if (spellDropsRegistry.getOrDefault(spell, false)) continue;
			            
			            e.getDrops().add(spell.getAsItemStack(false));
			            spellDropsRegistry.put(spell, true);
			            currentSpellDrops++;
			        }
			        
			        for (int i = 0; i <= maxEssenceDrops; i++) 
			        {
			            if (Chance.of(6.5)) 
			            {
			                AbstractObsObject essence = switch (mobCategory) 
			                {
			                    case CELESTIO_MOBS -> new CelestioEssence();
			                    case MORTIO_MOBS   -> new MortioEssence();
			                    case INFERNO_MOBS  -> new InfernoEssence();
			                    case GLACIO_MOBS   -> new GlacioEssence();
			                    case AERO_MOBS     -> new AeroEssence();
			                    case GEO_MOBS      -> new GeoEssence();
			                    case COSMO_MOBS    -> new CosmoEssence();
			                    case HERESIO_MOBS  -> new HeresioEssence();
			                    default            -> null;
			                };
			                if (essence != null) e.getDrops().add(essence.toItemStack());
			            }
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
			    
			    if(data != null) data.deleteFile();
			}
		},plugin);
	}
}
