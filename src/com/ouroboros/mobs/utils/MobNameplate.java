package com.ouroboros.mobs.utils;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityCategories;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

/**
 * @Documented newest itteration for mob health and identity display. Disregard ObsMobHealthbar.java
 * Further updates may delete the deprecated class; until then, please use this implementation.
 */
public class MobNameplate
{
	public static final NamespacedKey customMob = new NamespacedKey(Ouroboros.instance, "customMobName");
	
	//Nameplate handler
    public static void build(LivingEntity mob, boolean buildCustom)
    {
        MobData data = MobData.getMob(mob.getUniqueId());
        if (data == null) return;

        mob.setCustomNameVisible(true);
        if (mob.getPersistentDataContainer().has(customMob) && buildCustom)
        {
        	mob.setCustomName(initialize(mob, mob.getPersistentDataContainer().get(customMob, PersistentDataType.STRING), data));
        }
        else mob.setCustomName(initialize(mob, data));

        if (Ouroboros.debug)
            PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oSpawnNameplate&r&f -- &aOK&f || &7Mob: "
                + data.getName());
    }

    public static void update(LivingEntity mob)
    {
        MobData data = MobData.getMob(mob.getUniqueId());
        if (data == null) return;
        
        mob.setCustomName(initialize(mob, data));

        if (Ouroboros.debug)
            PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oUpdateNameplate&r&f -- &aOK&f || &7Mob: "
                + PrintUtils.getFancyEntityName(mob.getType()));
    }

    public static void remove(LivingEntity mob)
    {
        mob.setCustomName(null);
        mob.setCustomNameVisible(false);
    }
    
    public static void hide(LivingEntity mob)
    {
    	mob.setCustomNameVisible(false);
    }
    
    public static void show(LivingEntity mob)
    {
    	mob.setCustomNameVisible(true);
    }

    // Nameplate builder
    private static String initialize(LivingEntity mob, MobData data)
    {
        return PrintUtils.ColorParser(
        		buildIdentitySegment(mob, data) + " &7| &f{" + 
        		buildHPSegment(data) + "&f} &7| &f{" + 
        		buildArmorSegment(data) + "&f}");
    }
    
    /**
     * @description: Initializes a custom mob nameplate with a chosen name.
     * @param mob
     * @param name : Chosen Name
     * @param data
     * @return
     */
    private static String initialize(LivingEntity mob, String name, MobData data)
    {
    	mob.getPersistentDataContainer().set(customMob, PersistentDataType.STRING, data.setName(name));
    	data.setName(PrintUtils.ColorParser(name));
    	return PrintUtils.ColorParser(buildIdentitySegment(mob, data.getName(), data) + " &7| &f{" + 
        		buildHPSegment(data) + "&f} &7| &f{" + 
        		buildArmorSegment(data) + "&f}");
    	
    }

    // Segments
    private static String buildIdentitySegment(LivingEntity mob, MobData data)
    {
    	String levelTag = " &eLv&f: &r&b&l" + data.getLevel();
        String affinityTag = buildAffinityTag(mob, data);
        return affinityTag + PrintUtils.getFancyEntityName(mob.getType()) + levelTag;
    }
    
    /**
     * 
     * @param mob
     * @param name takes in an optional name segment for custom mob building.
     * @param data
     * @return
     */
    private static String buildIdentitySegment(LivingEntity mob, String name, MobData data)
    {
    	String levelTag = " &eLv&f: &r&b&l" + data.getLevel();
        String affinityTag = buildAffinityTag(mob, data);
        return affinityTag + PrintUtils.ColorParser(name) + levelTag;
    }
    
    private static String buildHPSegment(MobData data)
    {
        double current = data.getHp(false);
        double max = data.getHp(true);
        double ratio = (max <= 0) ? 0 : Math.min(1.0, current / max);

        String bar = buildHPBarString(ratio, 10);

        return "&c❤&r&f: " + bar;
    }


    private static String buildArmorSegment(MobData data)
    {
        if (data.isBreak()) return "&4&k&lo &r&c&lBreak&r&f! &4&k&lo";
        double current = data.getArmor(false);
        double max = data.getArmor(true);
        double ratio = (max <= 0) ? 0 : Math.min(1.0, current / max);
        
        String bar = buildArmorBarString(ratio, 5);
        
        return "&6⛨&r&f: " + bar;
    }
    
    private static String buildHPBarString(double ratio, int segments)
    {
    	int filled = Math.min(segments, Math.max(0, (int) Math.round(ratio * segments)));
    	int empty = segments - filled;
    	
    	String barColor  = ratio >= 0.6 ? "&a" : ratio >= 0.35 ? "&e" : "&c";
    	String emptyColor = ratio >= 0.6 ? "&2" : ratio >= 0.35 ? "&6" : "&4";
    	
    	return barColor + "|".repeat(filled) + emptyColor + "|".repeat(empty);
    }
    
    private static String buildArmorBarString(double ratio, int segments)
    {
    	int filled = Math.min(segments, Math.max(0, (int) Math.round(ratio * segments)));
    	int empty = segments - filled;
    	
    	return "&e" + "|".repeat(filled) + "&6" + "|".repeat(empty);
    }
    
    private static String buildAffinityTag(LivingEntity mob, MobData data)
    {
    	int level = data.getLevel();
        String entityTierIcon = level >= 100 ? "۞"
							  : level >= 80  ? "❂"
							  : level >= 60  ? "⚜"
							  : level >= 40  ? "✯"
							  : level >= 20  ? "♢"
							  : "●";
		String affinityTag = "&r&f⊰&" + PrintUtils.getElementTypeColor(EntityCategories.parseElementType(mob.getType())) + entityTierIcon + "&r&f⊱ ";
    	return affinityTag;
    }
    
    public static void registerTaskHandler(Plugin plugin)
    {
    	Bukkit.getScheduler().runTaskTimer(plugin, ()->
    	{
    		for (World w : Bukkit.getWorlds())
    		{
    			for (LivingEntity mob : w.getLivingEntities())
    			{
    				if (mob instanceof Player) continue;
    				if (mob.getCustomName() == null) continue;
    				
    				for (Player p : w.getPlayers())
    				{
    					if (MobData.getMob(mob.getUniqueId()) == null) continue;
    					mob.setCustomNameVisible(RayCastUtils.isMobVisible(p, mob, 35, 90));
    				}
    			}
    		}
    	}, 0L, 5L);
    }

}