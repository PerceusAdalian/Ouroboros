package com.ouroboros.accounts;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.ouroboros.Ouroboros;
import com.ouroboros.abilities.AbilityHandler;
import com.ouroboros.abilities.AbilityRegistry;
import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.enums.StatType;
import com.ouroboros.interfaces.ObsDisplayMain;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;

public class PlayerData 
{
	protected final UUID uuid;
	private final File file;
	private final YamlConfiguration config;
	public static final int baseXP = 100, fundsIntegerMax = 99999999;
	private static final double ExpMultiplier = 1.15;
	private static final Map<UUID, PlayerData> dataMap = new HashMap<>();
	
	public PlayerData(UUID uuid) 
	{
		this.uuid = uuid;
		this.file = new File(getDataFolder(), "playerdata/"+Bukkit.getPlayer(uuid).getName()+"/"+uuid+".yml");
		this.config = YamlConfiguration.loadConfiguration(file);
		
		if (!file.exists()) 
		{
			setDefaults();
			save();
		}
	}
	
	public static PlayerData getPlayer(UUID uuid) 
	{
	    return dataMap.get(uuid); 
	}
	
	public void setDefaults() 
	{
	    if (!file.exists()) 
	    {
	    	setAccountLevel(0);
	    	initializeAbilities();
	    	setFunds(true, 0);
	    	setFunds(false, 0);
	        // General Levels
	    	setStat(StatType.CRAFTING, true, 0);
	    	setStat(StatType.ALCHEMY, true, 0);
	    	setStat(StatType.ENCHANTING, true, 0);
	    	setStat(StatType.TRAVEL, true, 0);
	    	setStat(StatType.WOODCUTTING, true, 0);
	    	setStat(StatType.MINING, true, 0);
	    	setStat(StatType.FISHING, true, 0);
	    	setStat(StatType.FARMING, true, 0);
	    	
	        // Combat Levels
	    	setStat(StatType.MELEE, true, 0);
	    	setStat(StatType.RANGED, true, 0);
	    	setStat(StatType.MAGIC, true, 0);
	    	
	        // General Stat Experience
	    	setStat(StatType.CRAFTING, false, 0);
	    	setStat(StatType.ALCHEMY, false, 0);
	    	setStat(StatType.ENCHANTING, false, 0);
	    	setStat(StatType.TRAVEL, false, 0);
	    	setStat(StatType.WOODCUTTING, false, 0);
	    	setStat(StatType.MINING, false, 0);
	    	setStat(StatType.FISHING, false, 0);
	    	setStat(StatType.FARMING, false, 0);
	    	
	        // Combat Stat Experience
	    	setStat(StatType.MELEE, false, 0);
	    	setStat(StatType.RANGED, false, 0);
	    	setStat(StatType.MAGIC, false, 0);
	    	
	    	doLevelUpSound(true);
	    	doXpNotification(true);
	    	setAbilityPoints(0);
	    	setPrestigePoints(0);
	    }
	    return;
	}
	
	/**
	 * @param sType The type of stat to get. See StatType.java
	 * @param getLevel Returns the level (true) or experience (false).
	 */
	public int getStat(StatType sType, boolean getLevel) 
	{
		String path = getLevel ? "stats." + sType.getKey() : "experience." + sType.getKey();
		return config.getInt(path);
	}
	
	/**
	 * @param sType The type of stat to set. See StatType.java
	 * @param setLevel Set stat level (true) or experience (false).
	 * @param value Must be 0 < value < 100. Sets to existing value if not in range.
	 */
	public void setStat(StatType sType, boolean setLevel, int value) 
	{
		String path = setLevel ? "stats." + sType.getKey() : "experience." + sType.getKey();
		if (setLevel) value = Math.max(0, Math.min(100, value));
		config.set(path, value);
	}

	public static void addXP(Player p, StatType sType, int value) 
	{
		UUID uuid = p.getUniqueId();
		PlayerData data = PlayerData.getPlayer(uuid);
		
		int level = data.getStat(sType, true);
		int accountLevel = data.getAccountLevel();
		int xp = data.getStat(sType, false);
		int abilityPoints = data.getAbilityPoints();
		
		xp += value;
		
		while (level < 100 && xp >= PlayerData.getNextLevelXP(uuid, sType)) 
		{
			xp -= PlayerData.getNextLevelXP(uuid, sType);
			int preLevel = level;
			int preAccountLevel = accountLevel;
			level++;
			abilityPoints++;
			
			if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
			{
				EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1);
				OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 2, 15, 0.5, Particle.CLOUD, null);
				OBSParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.WAX_ON, null);	
			}
			if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
				PrintUtils.Print(p, "","&f|&bΩ&f|&b&l "+PrintUtils.printStatType(sType)+"&r&f Leveled Up! | &7Lvl "+preLevel+" &r&7-> "+ "&f&lLvl &r&b&l" + level,
					"&e&l+&f1&6AP&r&f | &nCurrent Ability Points&r&f: &6" + abilityPoints);			
			
			if (level % 10 == 0) 
			{
				accountLevel++;
				abilityPoints+=5;
				if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
					EntityEffects.playSound(p, p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1, 1);
				if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
					PrintUtils.Print(p, "","&f|&bΩ&f|&b&l Account Level Up&r&f! | &7Lvl "+preAccountLevel+" &r&7-> "+ "&f&lLvl &r&b&l" + accountLevel,
						"&e&l+&f5&6AP&r&f | &nCurrent Ability Points&r&f: &6" + abilityPoints);			
			}
		}
		
		data.setStat(sType, true, level);
		data.setStat(sType, false, xp);
		data.setAccountLevel(accountLevel);
		data.setAbilityPoints(abilityPoints);
		data.save();
	}

	public static void resetXP(UUID uuid, StatType sType) 
	{
		PlayerData.getPlayer(uuid).setStat(sType, false, 0);
	}

	public static void resetAllXP(UUID uuid) 
	{
		for (StatType type : StatType.values()) 
		{
			PlayerData.getPlayer(uuid).setStat(type, false, 0);
		}
	}
	
	public static void resetAllLevels(UUID uuid) 
	{
		for (StatType type : StatType.values()) 
		{
			PlayerData.getPlayer(uuid).setStat(type, true, 0);
		}
	}
	
	public static void resetAccount(UUID uuid) 
	{
		PlayerData data = PlayerData.getPlayer(uuid);
		for (StatType type : StatType.values()) 
		{
			data.setStat(type, false, 0);
			data.setStat(type, true, 0);
		}
		data.setAccountLevel(0);
		data.setAbilityPoints(0);
		data.setPrestigePoints(0);
		for (AbstractOBSAbility a : AbilityRegistry.abilityRegistry.values()) 
			data.getAbility(a).setRegistered(false).setActive(false);
	}
	
	public void initializeAbilities() 
	{
		for (AbstractOBSAbility a : AbilityRegistry.abilityRegistry.values()) 
    	{
			String registered = ".registered";
			String active = ".active";
			String pathAlpha = switch(a.getAbilityType()) 
			{
				case COMBAT -> pathAlpha = "abilities.combat_ability."+a.getInternalName();
				case PERK -> pathAlpha = "abilities.perk."+a.getInternalName();
				case SPECIALABILITY -> pathAlpha = "abilities.special_ability."+a.getInternalName();
				case UTILITY -> pathAlpha = "abilities.utility."+a.getInternalName();
			};
			if (!config.contains(pathAlpha + registered))
				config.set(pathAlpha + registered, false);
			if (!config.contains(pathAlpha + active))
				config.set(pathAlpha + active, false);
    	}
	}
	
	public static boolean checkLevelUpReady(UUID uuid, StatType sType) 
	{
		return PlayerData.getPlayer(uuid).getStat(sType, false) >= PlayerData.getNextLevelXP(uuid, sType);
	}
	
	public int getAccountLevel() 
	{
		return config.getInt("stats.accountlevel");
	}
	
	public void setAccountLevel(int value) 
	{
		config.set("stats.accountlevel", value);
	}
	
	public int getAbilityPoints() 
	{
		return config.getInt("points.ability");
	}
	
	public void setAbilityPoints(int value) 
	{
		config.set("points.ability", value);
	}
	
	public AbilityHandler getAbility(AbstractOBSAbility ability) 
	{
		return new AbilityHandler(ability, config);
	}
	
	public static boolean canRegister(UUID uuid, AbstractOBSAbility ability) 
	{
		
		return !PlayerData.getPlayer(uuid).getAbility(ability).isRegistered() && PlayerData.getPlayer(uuid).getAbilityPoints() >= ability.getAPCost();
	}
	
	public int getPrestigePoints() 
	{
		return config.getInt("points.pretige");
	}
	
	public void setPrestigePoints(int value) 
	{
		config.set("points.prestige", value);
	}
	
	public int getFunds(boolean getDebt) 
	{
		return config.getInt(getDebt?"funds.debt":"funds.money");
	}
	
	public void setFunds(boolean getDebt, int value) 
	{
		config.set(getDebt?"funds.debt":"funds.money", value);
	}
	
	public static void addMoney(Player p, int value) 
	{
		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		int currentMoney = data.getFunds(false);
		int currentDebt = data.getFunds(true);

		int amountToDebt = Math.min(value, currentDebt);
		currentDebt -= amountToDebt;

		int amountToMoney = value - amountToDebt;
		currentMoney = Math.min(currentMoney + amountToMoney, fundsIntegerMax);

		data.setFunds(false, currentMoney);
		data.setFunds(true, currentDebt);
		data.save();
		ObsDisplayMain.updateHud(p);
	}

	
	public static void subtractMoney(Player p, int value) 
	{
		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		int currentMoney = data.getFunds(false);
		int currentDebt = data.getFunds(true);

		int amountFromMoney = Math.min(value, currentMoney);
		currentMoney -= amountFromMoney;

		int remaining = value - amountFromMoney;
		currentDebt = Math.min(currentDebt + remaining, fundsIntegerMax);

		data.setFunds(false, currentMoney);
		data.setFunds(true, currentDebt);
		data.save();
		ObsDisplayMain.updateHud(p);
	}

	
	public void save() 
	{
		try 
		{
			config.save(file);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void saveAll() 
	{
	    for (PlayerData data : dataMap.values()) 
	    {
	        data.save();
	    }
	}
	
	//Loads a player into a static data cache to finalize data later.
	public static void loadPlayer(UUID uuid) 
	{
	    if (!dataMap.containsKey(uuid)) 
	    {
	        dataMap.put(uuid, new PlayerData(uuid));
	    }
	}
	
	//Unloads the player from the data cache, and properly saves to file.
	public static void unloadPlayer(UUID uuid) 
	{
	    PlayerData data = dataMap.remove(uuid);
	    if (data != null) 
	    {
	        data.save();
	    }
	}
	
	public static int getNextLevelXP(UUID uuid, StatType statType) 
	{
		return (int) (baseXP * Math.pow(ExpMultiplier, getPlayer(uuid).getStat(statType, true)));
	}
	
	public void doLevelUpSound(boolean bool) 
	{
		config.set("dolevelupsound", bool);
	}
	
	public boolean doLevelUpSound() 
	{
		return config.getBoolean("dolevelupsound");
	}
	
	public void doXpNotification(boolean bool) 
	{
		config.set("doxpnotifs", bool);
	}
	
	public boolean doXpNotification() 
	{
		return config.getBoolean("doxpnotifs");
	}
	
	public static void initializeDataFolder() 
	{
		File playerDataFolder = new File(getDataFolder(), "playerdata");
	    if (!playerDataFolder.exists()) playerDataFolder.mkdirs();
	}
	
	public static File getDataFolder() 
	{
		return Ouroboros.instance.getDataFolder();
	}
}