package com.ouroboros.accounts;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.util.Vector;

import com.eol.echoes.abilities.AbilityHandler;
import com.eol.echoes.abilities.AbilityRegistry;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.echoes.records.EchoManifest;
import com.lol.spells.Spell;
import com.lol.spells.SpellDataHandler;
import com.lol.spells.SpellRegistry;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.GateCodes;
import com.ouroboros.enums.LegacyColor;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.Nullable;
import com.ouroboros.utils.NumberUtils;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.CosmoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class PlayerData 
{
	protected final UUID uuid;
	protected File file;
	private final YamlConfiguration config;
	public static final int baseXP = 225, fundsIntegerMax = 99999999, MAXMAGIC = 7,
			maxLuminite = 9999, maxEssence = 9999, maxScrap = 9999, maxShards = 999;
	private static final double ExpMultiplier = 1.18;
	public static final double playerHpMax = 10000, playerHpMin = 250;
	public static final int playerArmorMax = 1000, playerArmorMin = 50;
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
	
	public File getFile() 
	{
		return file;
	}

	public void setFile(File file) 
	{
		this.file = file;
	}

	public static PlayerData getPlayer(UUID uuid) 
	{
	    return dataMap.get(uuid); 
	}
	
	public UUID getUUID(PlayerData data)
	{
		return data.uuid;
	}
	
	public void setDefaults() 
	{
	    if (!file.exists()) 
	    {
	    	setAccountLevel(0);
	    	initializeAbilities();
	    	
	    	// Stats
	    	setDefaultHP(playerHpMin);
	    	setDefaultArmor(playerArmorMin);
	    	setHP(getDefaultHP());
	    	setArmor(getDefaultArmor());
	    	setBreak(false);
	    	
	    	//Currencies
	    	setFunds(true, 0);
	    	setFunds(false, 0);
	    	setLuminite(0);
	    	setScrap(0);
	    	for (ElementType eType : ElementType.values())
	    	{
	    		if (!ElementType.elemental.contains(eType)) continue;
	    		setEssence(eType, 0);
	    	}
	    	
	        // General Levels
	    	setStat(StatType.CRAFTING, true, 0);
	    	setStat(StatType.ALCHEMY, true, 0);
	    	setStat(StatType.ENCHANTING, true, 0);
	    	setStat(StatType.TRAVEL, true, 0);
	    	setStat(StatType.WOODCUTTING, true, 0);
	    	setStat(StatType.MINING, true, 0);
	    	setStat(StatType.FISHING, true, 0);
	    	setStat(StatType.FARMING, true, 0);
	    	setStat(StatType.DISCOVERY, true, 0);
	    	setStat(StatType.REFINEMENT, true, 0);
	    	
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
	    	setStat(StatType.DISCOVERY, false, 0);
	    	setStat(StatType.REFINEMENT, false, 0);
	    	
	        // Combat Stat Experience
	    	setStat(StatType.MELEE, false, 0);
	    	setStat(StatType.RANGED, false, 0);
	    	setStat(StatType.MAGIC, false, 0);
	    	
	    	setGate(GateCodes.OVERWORLD, Bukkit.getWorld("world").getSpawnLocation());
	    	setGate(GateCodes.NETHER, null);
	    	setGate(GateCodes.END, null);
	    	
	    	doLevelUpSound(true);
	    	doXpNotification(true);
	    	doAutoCollectLootDrops(true);
	    	setAbilityPoints(0);
	    	setPrestigePoints(0);
	    	initializeWarpLocations();
	    	
	    	for (Spell spell : SpellRegistry.spellRegistry.values())
	    	{
	    		getSpell(spell).setRegistered(false).setShards(0);
	    	}
	    	
	    	for (EchoAbility ability : AbilityRegistry.abilityRegistry.values())
	    	{
	    		getAbility(ability).setRegistered(false);
	    	}
	    	
	    	setMagicProficiency(0);
	    	setKitClaimed(false);
	    	setFavoriteColor(LegacyColor.WHITE);
	    }
	    return;
	}
	
	public void setHP(double value)
	{
	    config.set("hp.current", Math.max(0, Math.min(value, getDefaultHP())));
	}
	
	public double getHP()
	{
		return config.getDouble("hp.current");
	}
	
	public void setDefaultHP(double value)
	{
		config.set("hp.default", NumberUtils.clamp(value, playerHpMin, playerHpMax));
	}
	
	public double getDefaultHP()
	{
		return config.getDouble("hp.default");
	}
	
	public static boolean heal(Player player, double value, boolean restoreArmor)
	{
		PlayerData data = PlayerData.getPlayer(player.getUniqueId());
		if (data.getHP() == data.getDefaultHP()) return false;
		if (data.getHP() + value > data.getDefaultHP()) data.setHP(data.getDefaultHP());
		else data.setHP(data.getHP() + value);
		ObsParticles.drawWisps(player.getLocation(), 3, 3, 5, Particle.HAPPY_VILLAGER, null);
		if(restoreArmor) 
		{
			data.setArmor(data.getDefaultArmor());
			ObsParticles.drawWisps(player.getLocation(), 3, 3, 5, Particle.WAX_ON, null);
		}
		PlayerData.syncVanillaHealth(player);
		data.save();
		PlayerHud.update(player);
		return true;
	}
	
	public static void fullRestore(Player player)
	{
		PlayerData data = PlayerData.getPlayer(player.getUniqueId());
		data.setHP(data.getDefaultHP());
		data.setArmor(data.getDefaultArmor());
		if (data.isBreak())
		{
			data.setBreak(false);
			player.removePotionEffect(PotionEffectType.SLOWNESS);
			player.removePotionEffect(PotionEffectType.MINING_FATIGUE);
		}
		ObsParticles.drawWisps(player.getLocation(), 3, 3, 5, Particle.WAX_ON, null);
		ObsParticles.drawWisps(player.getLocation(), 3, 3, 5, Particle.HAPPY_VILLAGER, null);
		PlayerData.syncVanillaHealth(player);
		data.save();
		PlayerHud.update(player);
	}
	
	public void setArmor(int value)
	{
		config.set("armor.current", value);
	}
	
	public int getArmor()
	{
		return config.getInt("armor.current");
	}
	
	public void setDefaultArmor(int value)
	{
		config.set("armor.default", NumberUtils.clamp(value, playerArmorMin, playerArmorMax));
	}
	
	public int getDefaultArmor()
	{
		return config.getInt("armor.default");
	}
	
	public static void restoreArmor(Player player, int value)
	{
		PlayerData data = PlayerData.getPlayer(player.getUniqueId());
		if (data.getArmor() + value > data.getDefaultArmor()) data.setArmor(data.getDefaultArmor());
		else data.setArmor(data.getArmor() + value);
		ObsParticles.drawWisps(player.getLocation(), 3, 3, 5, Particle.WAX_ON, null);
		data.save();
	}
	
	public boolean isBreak()
	{
		return config.getBoolean("playerbroken");
	}
	
	public void setBreak(boolean var)
	{
		config.set("playerbroken", var);
	}
	
	public void setBreak()
	{
		if (isBreak()) return;
		
		Player p = Bukkit.getPlayer(uuid);
		setArmor(0);
		setBreak(true);
		EntityEffects.playSound(p, Sound.ITEM_SHIELD_BREAK, SoundCategory.MASTER);
		ObsParticles.drawWisps(p.getLocation(), 4, 4, 7, Particle.END_ROD, null);
		PrintUtils.PrintToActionBar(p, "&6"+Symbols.ARMOR+" &c&l&oBreak&r&f!");
		EntityEffects.add(p, PotionEffectType.SLOWNESS, 300, 2, false);
		EntityEffects.add(p, PotionEffectType.MINING_FATIGUE, 300, 2, false);
		PlayerHud.update(p);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			Player playerRef = Bukkit.getPlayer(uuid);
		    if (playerRef == null || !playerRef.isOnline() || playerRef.isDead()) return;
		    
			setBreak(false);
			setArmor(getDefaultArmor());
			EntityEffects.playSound(playerRef, Sound.ITEM_BONE_MEAL_USE, SoundCategory.MASTER);
			ObsParticles.drawWisps(playerRef.getLocation(), 3, 3, 5, Particle.WAX_ON, null);
			PrintUtils.PrintToActionBar(playerRef, "&6"+Symbols.ARMOR+" &a&oRestored&r&f!");
			PlayerHud.update(playerRef);
			save();
		}, 300);
		save();
	}
	
	public void damage(double value, ElementType element, boolean routeArmorDamage)
	{
	    Player player = Bukkit.getPlayer(uuid);
	    
	    double mx = 1.0;
	    
	    if (!CosmoEffects.isVoidedRegistry.containsKey(player.getUniqueId()))
	    {
	    	mx = switch (element)
			{
			case MORTIO 			-> 1.5;
			case INFERNO, COMBUST, 
			COSMO, HERESIO 			-> 1.25;
			case CELESTIO,SEVER, 
			IMPALE, CRUSH, BLAST    -> 0.5;
			default                 -> 1.0;
			};
	    }

	    double remaining = value * mx;
	    
	    if (!isBreak() && routeArmorDamage)
	    {
	        double armorMX = switch (element)
	        {
	            case CORROSIVE        -> 2.0;
	            case PIERCE, PUNCTURE -> 1.5;
	            default               -> 0.5;
	        };

	        int newArmor = (int) (getArmor() - remaining * armorMX);
	        
	        if (newArmor < 0)
	        {
	            double overflow = (-newArmor) / armorMX;
	            overflow = Math.min(overflow, getHP());
	            setArmor(0);
	            setBreak();

	            double breakMX = switch (element)
	            {
		            case INFERNO, COMBUST, MORTIO,
		            COSMO, HERESIO,SEVER, 
		            IMPALE, CRUSH, BLAST -> 1.5;
	                case SLASH           -> 1.25;
	                case CELESTIO 		 -> 0.5;
	                default              -> 1.0;
	            };
	            remaining = overflow * breakMX;
	        }
	        else
	        {
	            setArmor(newArmor);
	            remaining = 0;
	        }
	    }
	    
	    setHP(getHP() - remaining);

	    if (getHP() <= 0)
	    {
	        setHP(0);
	        setBreak(false);
	        save();
	        
	        if (player != null)
	        {
	            PlayerHud.update(player);
	            Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	            {
	                if (player.isOnline() && !player.isDead())
                	Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
                	{
                	    if (player.isOnline() && !player.isDead())
                	    {
                	        player.setMetadata("ouroboros_death", new FixedMetadataValue(Ouroboros.instance, true));
                	        player.setHealth(0);
                	    }
                	}, 1L);
	            }, 1L);
	        }
	        return;
	    }

	    syncVanillaHealth(player);
	    PlayerHud.update(player);
	    save();
	}
	
	public static void damageUnnaturally(@Nullable Player attacker, Player target, double value, boolean doHurtAnimation, boolean routeArmorDamage, @Nullable ElementType element, @Nullable EchoManifest codec)
	{
		PlayerData data = PlayerData.getPlayer(target.getUniqueId());
		
		if (element == null) element = ElementType.PURE;
		
		data.damage(value, element, routeArmorDamage);
		
		
		if (doHurtAnimation && attacker != null)
		{
			target.playHurtAnimation(0);
			Vector kb = target.getLocation().toVector().subtract(attacker.getLocation().toVector());
			if (kb.lengthSquared() > 0.001)
			{
				Vector direction = kb.normalize().multiply(0.4);
				target.setVelocity(direction.setY(0.4));
			}
		}
		
		if (Ouroboros.debug)
		PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oPlayerDamageUnnaturally&r&f -- &aOK&7 || &fTarget: &f" + target.getName()
			+ (attacker != null ? " &7|| &fAttacker: &f" + attacker.getName() : "")
			+ " &7|| &fDamage: &c" + value
			+ " &7|| &6Element&f: " + element.getKey()
			+ " &7|| &aHP&f: " + getPlayer(target.getUniqueId()).getHP()
			+ "&7/&f" + getPlayer(target.getUniqueId()).getDefaultHP()
			+ (getPlayer(target.getUniqueId()).isBreak()
			? " &7|| &6Break&f: &cTRUE"
			: " &7|| &6AR&f: " + getPlayer(target.getUniqueId()).getArmor()
			+ "&7/&f" + getPlayer(target.getUniqueId()).getDefaultArmor())
			+ " &7|| &o&7END");
	}
	
	public static void syncVanillaHealth(Player player)
	{
	    PlayerData data = PlayerData.getPlayer(player.getUniqueId());
	    if (data == null) return;
	    if (data.getHP() <= 0)
	    {
	        player.setHealth(0);
	        return;
	    }
	    double ratio = data.getHP() / data.getDefaultHP();
	    double scaled = NumberUtils.clamp(ratio * 20.0, 0.1, 20.0);
	    player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(20.0);
	    player.setHealth(scaled);
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
				EntityEffects.playSound(p, Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER);
				ObsParticles.drawDisc(p.getLocation(), p.getWidth(), 2, 15, 0.5, Particle.CLOUD, null);
				ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.WAX_ON, null);	
			}
			if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
				PrintUtils.Print(p, "","&f|&bΩ&f|&b&l "+PrintUtils.printStatType(sType)+"&r&f Leveled Up! | &7Lvl "+preLevel+" &r&7-> "+ "&f&lLvl &r&b&l" + level,
					"&e&l+&f1&6AP&r&f | &nCurrent Ability Points&r&f: &6" + abilityPoints);			
			
			if (level % 10 == 0) 
			{
				accountLevel++;
				abilityPoints+=5;
				if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
					EntityEffects.playSound(p, Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER);
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
	
	public static void incrementLevel(Player p, StatType sType, int level)
	{
		for (int i = 0; i < level; i++)
		{
			int xpToAdd = getNextLevelXP(p.getUniqueId(), sType);
			addXP(p, sType, xpToAdd);
		}
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
	
	public void initializeAbilities() 
	{
		for (EchoAbility a : AbilityRegistry.abilityRegistry.values()) 
    	{
			String registered = ".registered";
			String active = ".active";
			String pathAlpha = switch(a.getAbilityType()) 
			{
				case COMBAT -> pathAlpha = "abilities.combat_ability."+a.getInternalName();
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
	
	public void subtractAbilityPoints(int value)
	{
		if (getAbilityPoints() - value < 0) setAbilityPoints(0);
		setAbilityPoints(getAbilityPoints() - value);
	}
	
	public void setAbilityPoints(int value) 
	{
		config.set("points.ability", value);
	}
	
	public AbilityHandler getAbility(EchoAbility ability) 
	{
		return new AbilityHandler(ability, config);
	}
	
	public SpellDataHandler getSpell(Spell spell)
	{
		return new SpellDataHandler(spell, config);
	}
	
	public int getStatLevel(StatType sType)
	{
		return getStat(sType, true);
	}
	
	public static boolean canRegisterEchoAbility(UUID uuid, EchoAbility ability) 
	{
		PlayerData data = PlayerData.getPlayer(uuid);
		EchoAbility pAbility = ability.getInstance();
		
		boolean statRequirement = data.getStatLevel(pAbility.getStatRequirement()) >= pAbility.getLevelRequirement();
		boolean notRegistered = (!data.getAbility(pAbility).isRegistered());
		boolean hasEnoughAP = data.getAbilityPoints() >= pAbility.getAPCost();
		return statRequirement && hasEnoughAP && notRegistered;
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
		PlayerHud.update(p);
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
		PlayerHud.update(p);
	}

	public int getLuminite()
	{
		return config.getInt("luminite");
	}
	
	public void setLuminite(int value)
	{
		config.set("luminite", value);
	}
	
	public static void addLuminite(Player p, int value)
	{
		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		data.setLuminite(data.getLuminite()+value);
		if (data.getLuminite() > maxLuminite) data.setLuminite(maxLuminite);
		data.save();
		PlayerHud.update(p);
	}
	
	public static void subtractLuminite(Player p, int value)
	{
		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		data.setLuminite(data.getLuminite()-value);
		if (data.getLuminite() < 0) data.setLuminite(0);
		data.save();
		PlayerHud.update(p);
	}
	
	public int getEssence(ElementType elementType)
	{
		if (!ElementType.elemental.contains(elementType)) 
		{
			PrintUtils.OBSConsoleError("Unexpected ElementType: " + elementType.getType());
			return 0;
		}
		
		String pathAlpha = "."+elementType.getKey().toLowerCase();
		return config.getInt("essence"+pathAlpha);
	}
	
	public void setEssence(ElementType elementType, int value)
	{
		if (!ElementType.elemental.contains(elementType)) 
		{
			PrintUtils.OBSConsoleError("Unexpected ElementType: " + elementType.getType());
			return;
		}
		
		String pathAlpha = "."+elementType.getKey().toLowerCase();
		config.set("essence"+pathAlpha, value);
	}
	
	public static void addEssence(Player p, ElementType elementType, int value)
	{
		if (!ElementType.elemental.contains(elementType)) 
		{
			PrintUtils.OBSConsoleError("Unexpected ElementType: " + elementType.getType());
			return;
		}
		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		data.setEssence(elementType, data.getEssence(elementType)+value);
		if (data.getEssence(elementType) > maxEssence) data.setEssence(elementType, maxEssence);
		data.save();
	}
	
	public static void subtractEssence(Player p, ElementType elementType, int value)
	{
		if (!ElementType.elemental.contains(elementType)) 
		{
			PrintUtils.OBSConsoleError("Unexpected ElementType: " + elementType.getType());
			return;
		}
		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		data.setEssence(elementType, data.getEssence(elementType)-value);
		if (data.getEssence(elementType) < 0) data.setEssence(elementType, 0);
		data.save();
	}
	
	public int getScrap()
	{
		return config.getInt("scrap");
	}
	
	public void setScrap(int value)
	{
		config.set("scrap", value);
	}
	
	public static void addScrap(Player p, int value)
	{
		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		data.setScrap(data.getScrap()+value);
		if (data.getScrap() > maxScrap) data.setScrap(maxScrap);
		data.save();
		PlayerHud.update(p);
	}
	
	public static void subtractScrap(Player p, int value)
	{
		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		data.setScrap(data.getScrap()-value);
		if (data.getScrap() < 0) data.setScrap(0);
		data.save();
		PlayerHud.update(p);
	}
	
	public void setMagicProficiency(int value)
	{
		config.set("magic_proficiency", value);
	}
	
	public int getMagicProficiency()
	{
		return config.getInt("magic_proficiency");
	}
	
	public void setGate(GateCodes code, Location loc)
	{
		String pathMod = switch(code)
		{
			case OVERWORLD -> pathMod = "_overworld";
			case NETHER -> pathMod = "_nether";
			case END -> pathMod = "_end";
		};
		config.set("gate"+pathMod, loc);
	}
	
	public Location getGate(GateCodes code)
	{
		String pathMod = switch(code)
		{
			case OVERWORLD -> pathMod = "_overworld";
			case NETHER -> pathMod = "_nether";
			case END -> pathMod = "_end";
		};
		return (Location) config.get("gate"+pathMod);
	}
	
	public WarpData getWarpData(int index)
	{
	    if (index < 1 || index > 5) return null;
	    Location loc = config.getLocation("warp_location_" + index);
	    if (loc == null) return null;
	    String nickname = config.getString("warp_nickname_" + index, null);
	    return new WarpData(index, loc, nickname);
	}

	public void setWarpData(int index, Location location, String nickname)
	{
	    if (index < 1 || index > 5) return;
	    config.set("warp_location_" + index, location);
	    config.set("warp_nickname_" + index, nickname);
	}

	public void clearWarpData(int index)
	{
	    if (index < 1 || index > 5) return;
	    config.set("warp_location_" + index, null);
	    config.set("warp_nickname_" + index, null);
	}

	public void initializeWarpLocations()
	{
	    for (int i = 1; i <= 5; i++)
	    {
	        config.set("warp_location_" + i, null);
	        config.set("warp_nickname_" + i, "n/a");
	    }
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
		save();
	}
	
	public boolean doLevelUpSound() 
	{
		return config.getBoolean("dolevelupsound");
	}
	
	public void doXpNotification(boolean bool) 
	{
		config.set("doxpnotifs", bool);
		save();
	}
	
	public boolean doXpNotification() 
	{
		return config.getBoolean("doxpnotifs");
	}
	
	public void doAutoCollectLootDrops(boolean bool)
	{
		config.set("auto_collect_loot_drops", bool);
		save();
	}
	
	public boolean doAutoCollectLootDrops()
	{
		return config.getBoolean("auto_collect_loot_drops");
	}
	
	public boolean hasKitClaimed()
	{
		return config.getBoolean("kitclaimed");
	}
	
	public void setKitClaimed(boolean bool)
	{
		config.set("kitclaimed", bool);
	}
	
	public LegacyColor getFavoriteColor()
	{
		return LegacyColor.valueOf(config.getString("favoritecolor"));
	}
	
	public void setFavoriteColor(LegacyColor color)
	{
		config.set("favoritecolor", color.name());
	}
	
	public static ItemStack getPlayerHead(Player p)
	{
		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		if (meta != null)
		{
			meta.setOwnerProfile(p.getPlayerProfile());
			head.setItemMeta(meta);
		}
		return head;
	}
	
	public static ItemStack getOfflineHead(UUID uuid, String name, String base64Texture)
	{
	    ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
	    SkullMeta meta = (SkullMeta) head.getItemMeta();
	    if (meta != null)
	    {
	        PlayerProfile profile = Bukkit.createPlayerProfile(uuid, name);
	        PlayerTextures textures = profile.getTextures();
	        try
	        {
	            byte[] decoded = Base64.getDecoder().decode(base64Texture);
	            String json = new String(decoded);
	            String url = json.substring(json.indexOf("\"url\":\"") + 7, json.indexOf("\"", json.indexOf("\"url\":\"") + 7));
	            textures.setSkin(URI.create(url).toURL());
	            profile.setTextures(textures);
	        }
	        catch (Exception ex)
	        {
	            ex.printStackTrace();
	        }
	        meta.setOwnerProfile(profile);
	        head.setItemMeta(meta);
	    }
	    return head;
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