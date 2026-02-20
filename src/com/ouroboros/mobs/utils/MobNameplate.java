package com.ouroboros.mobs.utils;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityCategories;
import com.ouroboros.utils.PrintUtils;

import org.bukkit.entity.LivingEntity;

/**
 * @Documented newest itteration for mob health and identity display. Disregard ObsMobHealthbar.java
 * Further updates may delete the deprecated class; until then, please use this implementation.
 */
public class MobNameplate
{
	//Nameplate handler
    public static void build(LivingEntity mob)
    {
        MobData data = MobData.getMob(mob.getUniqueId());
        if (data == null) return;

        mob.setCustomNameVisible(true);
        mob.setCustomName(initializeNameplate(mob, data));

        if (Ouroboros.debug)
            PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oSpawnNameplate&r&f -- &aOK&f || &7Mob: "
                + PrintUtils.getFancyEntityName(mob.getType()));
    }

    public static void update(LivingEntity mob)
    {
        MobData data = MobData.getMob(mob.getUniqueId());
        if (data == null) return;

        mob.setCustomName(initializeNameplate(mob, data));

        if (Ouroboros.debug)
            PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oUpdateNameplate&r&f -- &aOK&f || &7Mob: "
                + PrintUtils.getFancyEntityName(mob.getType()));
    }

    public static void remove(LivingEntity mob)
    {
        mob.setCustomName(null);
        mob.setCustomNameVisible(false);
    }

    // Nameplate builder
    private static String initializeNameplate(LivingEntity mob, MobData data)
    {
        return PrintUtils.ColorParser(
        		buildIdentitySegment(mob, data) + " &7| &f{" + 
        		buildHPSegment(data) + "&f} &7| &f{" + 
        		buildArmorSegment(data) + "&f}");
    }

    // Segments
    private static String buildIdentitySegment(LivingEntity mob, MobData data)
    {
        String affinityTag = "&r&f⊰&" + PrintUtils.getElementTypeColor(EntityCategories.parseElementType(mob.getType())) + "۞&r&f⊱ ";
        String levelTag = " &eLv&f: &r&b&l" + data.getLevel();
//        int level = data.getLevel();
//        String entityRarityIcon = level >= 100 ? "&2♔"
//    							: level >= 80 ? "&c♕"
//    							: level >= 60 ? "&e⚜"
//    							: level >= 40 ? "&d✯"
//    							: level >= 20 ? "&b♢"
//    							: "&7●";
    							
        return affinityTag + PrintUtils.getFancyEntityName(mob.getType()) + levelTag;
    }

    private static String buildHPSegment(MobData data)
    {
        double current = data.getHp(false);
        double max = data.getHp(true);
        double ratio = (max <= 0) ? 0 : Math.min(1.0, current / max);

        String bar = buildHPBarString(ratio, 15);

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

}