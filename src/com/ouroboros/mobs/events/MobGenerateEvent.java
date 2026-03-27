package com.ouroboros.mobs.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.utils.MobNameplate;
import com.ouroboros.utils.PrintUtils;

public class MobGenerateEvent implements Listener
{
    public record SummonRequest(int level, String customName) {}
	public static final Map<Location, SummonRequest> pendingSummons = new HashMap<>();
	
    public static void register(JavaPlugin plugin)
    {
        Bukkit.getPluginManager().registerEvents(new Listener()
        {
            @EventHandler
            public void onSpawn(EntitySpawnEvent e)
            {
                if (!Ouroboros.isActive())
                {
                    e.setCancelled(true);
                    return;
                }

                Entity entity = e.getEntity();
                if (!(entity instanceof LivingEntity living) || entity instanceof ArmorStand) return;

                SummonRequest request = pendingSummons.remove(e.getLocation());
                
                
                if (request != null)
                    initializeSummoned(living, request.level(), request.customName());
                else
                    initializeNatural(living);
                
                if (Ouroboros.debug == true) 
				{
					String string = entity.getCustomName().toString();
					PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oMobGenerateEvent&r&f -- &aOK &7Mob: " + string + " || &a&oGenerated Successfully");
				}
            }
        }, plugin);
    }

    private static void initializeNatural(LivingEntity entity)
    {
        MobData.loadMobData(entity);
        MobData data = MobData.getMob(entity.getUniqueId());
        if (data == null) return;

        data.initialize(entity);
        MobNameplate.build(entity, false);
        setMaxHealth(entity);
    }

    private static void initializeSummoned(LivingEntity entity, int level, String customName)
    {
        MobData.loadMobData(entity);
        MobData data = MobData.getMob(entity.getUniqueId());
        if (data == null) return;

        data.initializeSummoned(entity, level, customName);
        MobNameplate.build(entity, true);
        setMaxHealth(entity);
    }

    private static void setMaxHealth(LivingEntity entity)
    {
        var att = ((Attributable) entity).getAttribute(Attribute.MAX_HEALTH);
        att.setBaseValue(1023.9);
        ((Damageable) entity).setHealth(att.getBaseValue());
    }
}