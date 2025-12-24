package com.ouroboros.mobs.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.PrintUtils;

public class ObsMobHealthbar 
{
	public static final NamespacedKey HPBARTITLE = new NamespacedKey(Ouroboros.instance, "hp_bar_title");
	public static final NamespacedKey HPBARPROGRESS = new NamespacedKey(Ouroboros.instance, "hp_bar_progress");
	public static final Map<UUID, BossBar> bossBars = new HashMap<>();
	public static BarStyle barStyle;
	
	public static void initializeHPBar(Entity entity, Boolean bool)
	{
		if (!entity.getPersistentDataContainer().has(MobManager.MOB_DATA_KEY, PersistentDataType.STRING)) return;
		
		String hpBarTitle;
		MobData data = MobData.getMob(entity.getUniqueId());
		if (data.isBreak())
			hpBarTitle = PrintUtils.ColorParser("&f" + PrintUtils.getFancyEntityName(entity.getType()) + " &6AR&f: " + "&f&k0&r&e{&c&lBreak!&r&e}&f&k0");
		else
			hpBarTitle = PrintUtils.ColorParser("&f"+PrintUtils.getFancyEntityName(entity.getType())+" &6AR&f: &7(&f"+data.getArmor(false)+"&7/&f"+data.getArmor(true)+"&r&7)");
		
		BossBar bar = Bukkit.createBossBar(hpBarTitle, BarColor.GREEN, getBarStyle(data.getLevel()));
		double baseHP = data.getHp(true);
		double currentHP = data.getHp(false);
		double progress = (baseHP <= 0) ? 0.0 : Math.min(1.0, Math.max(0.0, currentHP / baseHP));
		
		bar.setProgress(progress);
		setVisible(entity, bool);
		
		entity.getPersistentDataContainer().set(HPBARTITLE, PersistentDataType.STRING, hpBarTitle);
		entity.getPersistentDataContainer().set(HPBARPROGRESS, PersistentDataType.DOUBLE, progress);
		
		for (Player p : entity.getWorld().getPlayers()) 
			addPlayer(p);
		
		bossBars.put(entity.getUniqueId(), bar);
		if (Ouroboros.debug) 
			PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oInitializeHPBar&r&f -- &aOK&f || &7Mob: "+
				(entity.getCustomName()!=null?entity.getCustomName():PrintUtils.getFancyEntityName(entity.getType())));
	}
	
	public static void updateHPBar(Entity entity, Boolean bool)
	{
		String hpBarTitle;
		
		BossBar bar = bossBars.get(entity.getUniqueId());
		if (bar == null) return;
	
		MobData data = MobData.getMob(entity.getUniqueId());
		if (data.isBreak())
			hpBarTitle = PrintUtils.ColorParser("&f" +PrintUtils.getFancyEntityName(entity.getType())+ " &6AR&f: " + "&f&k0&r&e{&c&lBreak!&r&e}&f&k0");
		else
			hpBarTitle = PrintUtils.ColorParser("&f"+PrintUtils.getFancyEntityName(entity.getType())+" &6AR&f: &7(&f"+data.getArmor(false)+"&7/&f"+data.getArmor(true)+"&r&7)");
		
		double baseHP = data.getHp(true);
		double currentHP = data.getHp(false);
		double progress = (baseHP <= 0) ? 0.0 : Math.min(1.0, Math.max(0.0, currentHP / baseHP));
		
		bar.setColor(getBarColor(currentHP, baseHP));
		bar.setProgress(progress);
		bar.setTitle(hpBarTitle);
		setVisible(entity, bool);
		entity.getPersistentDataContainer().set(HPBARPROGRESS, PersistentDataType.DOUBLE, progress);
		if (Ouroboros.debug) PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oUpdateHPBar&r&f -- &aOK&f || &7Mob: "+
				(entity.getCustomName()!=null?entity.getCustomName():PrintUtils.getFancyEntityName(entity.getType())));
		return;
	
	}
	
	public static void setVisible(Entity entity, boolean bool)
	{
		BossBar bar = bossBars.get(entity.getUniqueId());
		if (bar != null) bar.setVisible(bool);
	}
	
	public static void addPlayer(Player p)
	{
		for (Map.Entry<UUID, BossBar> entry : bossBars.entrySet())
		{
			BossBar bar = entry.getValue();
			bar.addPlayer(p);
		}
	}
	
	public static void removePlayer(Player p)
	{
		for (Map.Entry<UUID, BossBar> entry : bossBars.entrySet()) 
		{
            BossBar bar = entry.getValue();
            bar.removePlayer(p.getPlayer());
        }
	}
	
	public static void removeBossBar(Entity entity) 
	{
        BossBar bar = bossBars.remove(entity.getUniqueId());

        if (bar != null) 
        {
            bar.setVisible(false);
            bar.removeAll();
        }
    }
	
	public static void removeBossBars() 
	{
		ObsMobHealthbar.bossBars.forEach((key, bar) -> bar.removeAll());
		ObsMobHealthbar.bossBars.clear();
	}
	
	public static BarStyle getBarStyle(int level) 
	{
	    if (level <= 30) return BarStyle.SOLID;
	    if (level <= 45) return BarStyle.SEGMENTED_6;
	    if (level <= 65) return BarStyle.SEGMENTED_10;
	    if (level <= 85) return BarStyle.SEGMENTED_12;
	    return BarStyle.SEGMENTED_20;
	}
	
	public static BarColor getBarColor(double hp, double baseHp)
	{
		if (hp >= baseHp*0.8d) return BarColor.GREEN;
		if (hp >= baseHp*0.6d) return BarColor.YELLOW;
		if (hp >= baseHp*0.4d) return BarColor.PINK;
		return BarColor.RED;
	}
	
}
