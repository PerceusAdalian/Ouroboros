package com.ouroboros.mobs;

import java.util.Comparator;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class MobNameplate
{
	public static final NamespacedKey customMob = new NamespacedKey(Ouroboros.instance, "customMobName");
	private static final double NAMEPLATE_RANGE = 64.0;

	public static void build(LivingEntity mob, boolean buildCustom)
    {
		if (isDeadOrDying(mob)) return;
        MobData data = MobData.getMob(mob.getUniqueId());
        if (data == null)
        {
        	if (Ouroboros.debug)
        		PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oSpawnNameplate&r&f -- &cSKIPPED&f" +
        			" &7(data null for UUID " + mob.getUniqueId() + ")");
        	return;
        }

        mob.setCustomNameVisible(true);
        if (mob.getPersistentDataContainer().has(customMob) && buildCustom)
        {
        	String cName = initialize(mob, mob.getPersistentDataContainer().get(customMob, PersistentDataType.STRING), data);
        	if (cName == null) return;
        	mob.setCustomName(cName);
        }
        else 
        {
        	String name = initialize(mob, data);
        	if (name == null) return;
        	mob.setCustomName(name);
        }

        if (Ouroboros.debug)
            PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oSpawnNameplate&r&f -- &aOK&f || &7Mob: "
                + data.getName());
    }

    public static void update(LivingEntity mob)
    {
    	if (isDeadOrDying(mob)) return;
        MobData data = MobData.getMob(mob.getUniqueId());
        if (data == null) return;
        
        String name = initialize(mob, data);
        if (name == null) return;
        mob.setCustomName(name);

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

    private static String initialize(LivingEntity mob, MobData data)
    {
    	if (isDeadOrDying(mob)) return null;
        return PrintUtils.ColorParser(
        		buildIdentitySegment(mob, data) + " &7| &f{" + 
        		buildHPSegment(data) + "&f} &7| &f{" + 
        		buildArmorSegment(data) + "&f}");
    }
    
    private static String initialize(LivingEntity mob, String name, MobData data)
    {
    	if (isDeadOrDying(mob)) return null;
    	mob.getPersistentDataContainer().set(customMob, PersistentDataType.STRING, data.setName(name));
    	data.setName(PrintUtils.ColorParser(name));
    	return PrintUtils.ColorParser(buildIdentitySegment(mob, data.getName(), data) + " &7| &f{" + 
        		buildHPSegment(data) + "&f} &7| &f{" + 
        		buildArmorSegment(data) + "&f}");
    }

    private static String buildIdentitySegment(LivingEntity mob, MobData data)
    {
    	String levelTag = " &eLv&f: &r&b&l" + data.getLevel();
        return buildAffinityTag(mob, data) + PrintUtils.getFancyEntityName(mob.getType()) + levelTag;
    }
    
    private static String buildIdentitySegment(LivingEntity mob, String name, MobData data)
    {
    	String levelTag = " &eLv&f: &r&b&l" + data.getLevel();
        return buildAffinityTag(mob, data) + PrintUtils.ColorParser(name) + levelTag;
    }
    
    private static String buildHPSegment(MobData data)
    {
        double current = data.getHp(false);
        double max = data.getHp(true);
        double ratio = (max <= 0) ? 0 : Math.min(1.0, current / max);
        return "&c❤&r&f: " + buildHPBarString(ratio, 10);
    }

    private static String buildArmorSegment(MobData data)
    {
        if (data.isBreak()) return "&4&k&lo &r&c&lBreak&r&f! &4&k&lo";
        double current = data.getArmor(false);
        double max = data.getArmor(true);
        double ratio = (max <= 0) ? 0 : Math.min(1.0, current / max);
        return "&6⛨&r&f: " + buildArmorBarString(ratio, 5);
    }
    
    private static String buildHPBarString(double ratio, int segments)
    {
    	int filled = Math.min(segments, Math.max(0, (int) Math.round(ratio * segments)));
    	int empty = segments - filled;
    	String barColor   = ratio >= 0.6 ? "&a" : ratio >= 0.35 ? "&e" : "&c";
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
		return "&r&f⊰" + PrintUtils.getElementTypeColor(EntityCategories.parseElementType(mob.getType())) + entityTierIcon + "&r&f⊱ ";
    }

    private static boolean isDeadOrDying(LivingEntity mob)
	{
		return mob.isDead() || MobData.isDying(mob.getUniqueId());
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
    			    if (isDeadOrDying(mob)) continue;
    			    if (MobData.getMob(mob.getUniqueId()) == null) continue;
    			    
    			    Player nearest = w.getPlayers().stream()
    			        .filter(p -> p.getWorld().equals(mob.getWorld()))
    			        .filter(p -> p.getLocation().distanceSquared(mob.getLocation()) <= NAMEPLATE_RANGE * NAMEPLATE_RANGE)
    			        .min(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(mob.getLocation())))
    			        .orElse(null);
    			    
    			    if (nearest == null) { mob.setCustomNameVisible(false); continue; }
    			    mob.setCustomNameVisible(RayCastUtils.isMobVisible(nearest, mob, NAMEPLATE_RANGE, 90));
    			}
    		}
    	}, 0L, 5L);
    }
}