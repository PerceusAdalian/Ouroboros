package com.ouroboros.mobs;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.eol.echoes.ResolveEchoInteract;
import com.eol.echoes.records.EchoManifest;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.utils.Nullable;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.AeroEffects;
import com.ouroboros.utils.entityeffects.ArcanoEffects;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.CosmoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GeoEffects;
import com.ouroboros.utils.entityeffects.HeresioEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;

public class MobData 
{
	protected final UUID uuid;
	private final File file;
	private final YamlConfiguration config;

	private static final Map<UUID, MobData> dataMap = new HashMap<>();
	public static final Set<UUID> dyingRegistry = Collections.synchronizedSet(new HashSet<>());

	public MobData(LivingEntity entity) 
	{
		this.uuid = entity.getUniqueId();
		this.file = new File(getDataFolder(), "mobs/data/"+uuid.toString()+".yml");
		this.config = YamlConfiguration.loadConfiguration(file);
		
		if (!file.exists()) 
		{
			initialize(entity);
			save();
		}
	}
	
	private MobData(UUID uuid, File file, YamlConfiguration config)
	{
		this.uuid = uuid;
		this.file = file;
		this.config = config;
	}
	
	public static MobData getMob(UUID uuid) 
	{
		return dataMap.get(uuid);
	}

	public static boolean isDying(UUID uuid)
	{
		return dyingRegistry.contains(uuid);
	}
	
	public void initialize(LivingEntity entity)
	{
	    if (!(entity instanceof LivingEntity livingEntity)) return;
	    String name = PrintUtils.getFancyEntityName(entity.getType());
	    double baseHp = livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
	    int level = LevelTable.getLevel(entity.getLocation().getBlock().getBiome());
	    double t = (double)(level - 1) / 99.0;
	    double multiplier = 1.0 + (t * t) * 99.0;
	    double hp = Math.ceil(Math.min(baseHp * multiplier, 200000));
	    int armor = Math.min(1000, Math.max((int)(hp * 0.3), 10));
	    
	    setUUID(uuid);
	    setName(name);
	    setEntityType(livingEntity.getType());
	    setLevel(level);
	    setHp(Math.ceil(hp), true);
	    setHp(Math.ceil(hp), false);
	    setArmor(armor, true);
	    setArmor(armor, false);
	    setBreak(false);
	    setLocation(entity.getLocation());
	    entity.getPersistentDataContainer().set(MobManager.MOB_DATA_KEY, PersistentDataType.STRING, serialize());
	    entity.getPersistentDataContainer().set(MobManager.MOB_UUID_KEY, PersistentDataType.STRING, uuid.toString());
	    save();
	}
	
	public void initializeSummoned(LivingEntity entity, int level, String customName)
	{
		double baseHp = entity.getAttribute(Attribute.MAX_HEALTH).getValue();
		double t = (double)(level - 1) / 99.0;
	    double multiplier = 1.0 + (t * t) * 99.0;
	    double hp = Math.ceil(Math.min(baseHp * multiplier, 200000));
		int armor = Math.min(1000, Math.max((int)(hp * 0.3), 10));
	    
		setUUID(uuid);
		setName(customName);
		setEntityType(entity.getType());
		setLevel(level);
		setHp(Math.ceil(hp), true);
		setHp(Math.ceil(hp), false);
		setArmor(armor, true);
		setArmor(armor, false);
		setBreak(false);
		setLocation(entity.getLocation());
		entity.getPersistentDataContainer().set(MobManager.MOB_DATA_KEY, PersistentDataType.STRING, serialize());
		entity.getPersistentDataContainer().set(MobManager.MOB_UUID_KEY, PersistentDataType.STRING, uuid.toString());
		entity.getPersistentDataContainer().set(MobNameplate.customMob, PersistentDataType.STRING, customName);
		save();
	}
	
	public static void convertLegacyMob(LivingEntity entity)
	{
	    UUID uuid = entity.getUniqueId();
	    if (isDying(uuid)) return;
	    if (entity.getPersistentDataContainer().has(MobManager.MOB_DATA_KEY, PersistentDataType.STRING)) return;
	    if (dataMap.containsKey(uuid)) return;

	    File staleFile = new File(getDataFolder(), "mobs/data/" + uuid + ".yml");
	    if (staleFile.exists()) staleFile.delete();

	    MobData data = loadMobData(entity);
	    if (data == null) return;

	    var att = ((Attributable) entity).getAttribute(Attribute.MAX_HEALTH);
	    att.setBaseValue(1023.9);
	    ((Damageable) entity).setHealth(att.getBaseValue());

	    MobNameplate.build(entity, true);
	    
	    if (Ouroboros.debug)
	    {
	        PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oConvertLegacyMob&r&f -- &aOK&7 || &fMob: " +
	            PrintUtils.getFancyEntityName(entity.getType()) +
	            " &7|| &fLevel: &b" + data.getLevel() +
	            " &7|| &aHP: &f" + data.getHp(false) + "&7/&f" + data.getHp(true));
	    }
	}
	
	public String serialize()
	{
		Location loc = getLocation();
		return getUUID().toString() + "|" +
			   getEntityType()+"|"+
			   getName()+ "|" +
			   getLevel() + "|" +
			   getHp(true) + "|" +
		       getHp(false) + "|" +
		       getArmor(true) + "|" +
			   getArmor(false) + "|" +
			   isBreak() + "|" +
			   loc.getWorld().getName() + "," +
			   loc.getX() + "," + 
			   loc.getY() + "," +
			   loc.getZ();
	}
	
	public static MobData deserialize(String data, Server server)
	{
		String[] segments = data.split("\\|");
		if (segments.length != 10) return null;
		
		UUID uuid = UUID.fromString(segments[0]);
		EntityType eType = EntityType.valueOf(segments[1].trim().toUpperCase().replace(" ", "_"));
		String name = segments[2];
		int level = Integer.parseInt(segments[3]);
		double baseHp = Double.parseDouble(segments[4]);
		double currentHp = Double.parseDouble(segments[5]);
		int baseArmor = Integer.parseInt(segments[6]);
		int currentArmor = Integer.parseInt(segments[7]);
		boolean isBroken = Boolean.parseBoolean(segments[8]);
		
		String[] locSegments = segments[9].split(",");
		World world = server.getWorld(locSegments[0]);
		double x = Double.parseDouble(locSegments[1]);
        double y = Double.parseDouble(locSegments[2]);
        double z = Double.parseDouble(locSegments[3]);
        Location loc = new Location(world,x,y,z);
        
        File mobFile = new File(getDataFolder(), "mobs/data/"+uuid+".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mobFile);
        MobData mobData = new MobData(uuid, mobFile, config);
        
        mobData.setUUID(uuid);
        mobData.setName(name);
        mobData.setEntityType(eType);
        mobData.setLevel(level);
        mobData.setHp(baseHp, true);
        mobData.setHp(currentHp, false);
        mobData.setArmor(baseArmor, true);
        mobData.setArmor(currentArmor, false);
        mobData.setBreak(isBroken);
        mobData.setLocation(loc);
        
        dataMap.put(uuid, mobData);
        mobData.save();
        
        return mobData;
	}
	
	public static MobData rehydrate(LivingEntity entity, String serialized, Server server)
	{
		String[] segments = serialized.split("\\|");
		if (segments.length != 10)
		{
			PrintUtils.OBSConsoleError("rehydrate: bad segment count (" + segments.length + ") for: " + serialized);
			return null;
		}

		try
		{
			EntityType eType   = EntityType.valueOf(segments[1].trim().toUpperCase().replace(" ", "_"));
			String     name    = segments[2];
			int        level   = Integer.parseInt(segments[3]);
			double     baseHp  = Double.parseDouble(segments[4]);
			double     currHp  = Double.parseDouble(segments[5]);
			int        baseAr  = Integer.parseInt(segments[6]);
			int        currAr  = Integer.parseInt(segments[7]);
			boolean    broken  = Boolean.parseBoolean(segments[8]);

			UUID newUUID = entity.getUniqueId();

			File              newFile   = new File(getDataFolder(), "mobs/data/" + newUUID + ".yml");
			YamlConfiguration newConfig = new YamlConfiguration();
			MobData           data      = new MobData(newUUID, newFile, newConfig);

			data.setUUID(newUUID);
			data.setName(name);
			data.setEntityType(eType);
			data.setLevel(level);
			data.setHp(baseHp, true);
			data.setHp(currHp,  false);
			data.setArmor(baseAr, true);
			data.setArmor(currAr, false);
			data.setBreak(broken);
			data.setLocation(entity.getLocation());

			String correctSerialized = data.serialize();
			entity.getPersistentDataContainer().set(MobManager.MOB_DATA_KEY, PersistentDataType.STRING, correctSerialized);
			entity.getPersistentDataContainer().set(MobManager.MOB_UUID_KEY, PersistentDataType.STRING, newUUID.toString());

			dataMap.put(newUUID, data);
			data.save();

			if (Ouroboros.debug)
				PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oRehydrate&r&f -- &aOK&f" +
					" &7|| UUID: &b" + newUUID +
					" &7|| &fLv: &b" + level +
					" &7|| HP: &a" + currHp + "&7/&f" + baseHp);

			return data;
		}
		catch (Exception ex)
		{
			PrintUtils.OBSConsoleError("rehydrate: failed to parse serialized data: " + serialized);
			ex.printStackTrace();
			return null;
		}
	}
	
	public Location getLocation()
	{
		World world = Bukkit.getWorld(config.getString("mob.location.world"));
		double x = config.getDouble("mob.location.x");
		double y = config.getDouble("mob.location.y");
		double z = config.getDouble("mob.location.z");
		return new Location(world,x,y,z);
	}
	
	public void setLocation(Location loc)
	{
		config.set("mob.location.world", loc.getWorld().getName());
		config.set("mob.location.x", loc.getX());
		config.set("mob.location.y", loc.getY());
		config.set("mob.location.z", loc.getZ());
	}
	
	public UUID getUUID()
	{
		return UUID.fromString(config.getString("mob.uuid"));
	}
	
	public void setUUID(UUID uuid)
	{
		config.set("mob.uuid", uuid.toString());
	}
	
	public EntityType getEntityType() 
	{
		return EntityType.valueOf(config.getString("mob.entity_type"));
	}
	
	public void setEntityType(EntityType eType) 
	{
		config.set("mob.entity_type", eType.name());
	}
	
	public int getLevel() 
	{
		return config.getInt("mob.level");
	}
	
	public void setLevel(int value) 
	{
		config.set("mob.level", value);
	}
	
	public String getName() 
	{
		return config.getString("mob.name");
	}
	
	public String setName(String str) 
	{
		config.set("mob.name", str);
		return config.getString("mob.name");
	}
	
	public double getHp(Boolean getBase) 
	{
		String path = getBase?"mob.hp.base":"mob.hp.current";
		return config.getDouble(path);
	}
	
	public void setHp(double value, Boolean setBase) 
	{
		String path = setBase?"mob.hp.base":"mob.hp.current";
		config.set(path, value);
	}
	
	public int getArmor(Boolean getBase) 
	{
		String path = getBase?"mob.armor.base":"mob.armor.current";
		return config.getInt(path);
	}
	
	public void setArmor(int value, Boolean setBase) 
	{
		String path = setBase?"mob.armor.base":"mob.armor.current";
		config.set(path, value);
	}
	
	public void damage(double value, boolean damageArmor, @Nullable ElementType element)
	{
		MobData data = MobData.getMob(uuid);
		Entity entity = Bukkit.getEntity(uuid);
		
		if (element == null) element = ElementType.PURE;
		
		if (CelestioEffects.hasHumility.contains(uuid) && element == ElementType.CELESTIO) value *= 1.15;
		if (InfernoEffects.hasCharred.contains(uuid) && element == ElementType.INFERNO) value *= 1.25;
		if (AeroEffects.hasShock.contains(uuid) && element == ElementType.AERO) value *= 1.25;
		if (HeresioEffects.isIntimidated.contains(uuid) && element == ElementType.HERESIO) value *= 1.20;
		if (MortioEffects.hasHaze.contains(uuid) && element == ElementType.MORTIO) value *= 1.25;
		if (ArcanoEffects.hasEtherOverload.contains(uuid) && ElementType.elemental.contains(element)) value *= 1.5;
		if (GeoEffects.hasVulnerable.contains(uuid) && ElementType.physical.contains(element)) value *= 1.5;
		
		if (!CosmoEffects.isVoidedRegistry.containsKey(uuid))
		{
			if (MobAffinity.parseUniversalImmunity(entity, element)) value = 0;
			else if (MobAffinity.parseUniversalResistance(entity, element)) value *= 0.5;
			else if (MobAffinity.parseUniversalWeakness(entity, element)) value *= 1.5;
		}

		if (element == ElementType.SLASH && data.isBreak()) value *= 1.5;
		if (element == ElementType.SEVER && data.isBreak()) value *= 2;
		
		double currentHP = data.getHp(false);
		double newHP = currentHP - (data.isBreak() ? value : value * 0.7);
		data.setHp(newHP, false);

		if (damageArmor) damageArmor(value, element);
	}
	
	public void breakDamage(double value, ElementType element)
	{
		double baseHP = getHp(true);
	    damage((baseHP * 0.1) + value, false, element);
	}
	
	public void heal(double value, boolean setMaxHp, boolean healArmor, boolean setMaxArmor)
	{
		setHp(setMaxHp ? getHp(true) : (getHp(false)+value), false);
		Entity entity = Bukkit.getEntity(uuid);
		MobNameplate.update((LivingEntity) entity);
		if (healArmor)
			healArmor(value, setMaxArmor);
		ObsParticles.drawWisps(entity.getLocation(), entity.getWidth(), entity.getHeight(), 5, Particle.HAPPY_VILLAGER, null);
	}
	
	public void healArmor(double value, boolean setMax)
	{
		if (isBreak()) setBreak(false);
		setArmor(setMax ? getArmor(true) : (getArmor(false) + (int) value), false);
		Entity entity = Bukkit.getEntity(uuid);
		MobNameplate.update((LivingEntity) entity);
		ObsParticles.drawWisps(entity.getLocation(), entity.getWidth(), entity.getHeight(), 5, Particle.WAX_ON, null);
	}
	
	public void damageArmor(double value, ElementType element)
	{
	    double armorDamageCoeff = switch (element)
	    {
	        case CORROSIVE        -> 1.5;
	        case PUNCTURE, PIERCE -> 1.25;
	        default               -> 0.5;
	    };

	    if (isBreak()) return;
	    int currentArmor = getArmor(false);
	    int newArmor = (int)(currentArmor - (value * armorDamageCoeff));
	    setArmor(newArmor, false);

	    if (getArmor(false) <= 0 || element == ElementType.IMPALE || element == ElementType.CRUSH)
	    	setBreak();

	    save();
	}
	
	public static double damageUnnaturally(@Nullable Player player, Entity target, double value, boolean doHurtAnimation, boolean damageArmor, @Nullable ElementType element, @Nullable EchoManifest codec)
	{
		if (target instanceof Player targetPlayer)
		{
		    PlayerData.damageUnnaturally(player, targetPlayer, value, doHurtAnimation, damageArmor, element, codec);
		    return value;
		}
		
		MobData data = MobData.getMob(target.getUniqueId());
		
		if (data == null)
		{
			((Damageable) target).damage(value);
		}
		else 
		{
			if (element == null) element = ElementType.PURE;
			
			if (data.isBreak()) 
				data.breakDamage(value, element);
			else 
				data.damage(value, true, element);
			
			if (codec != null && player != null)
				ResolveEchoInteract.resolvePassiveEffect(codec, player, (LivingEntity) target);

			data.save();
		}

		if (doHurtAnimation && player != null) 
		{
			((LivingEntity) target).playHurtAnimation(0);
			Vector kb = target.getLocation().toVector().subtract(player.getLocation().toVector());
		    if (kb.lengthSquared() > 0.001)
		    {
		        Vector direction = kb.normalize().multiply(0.4);
		        target.setVelocity(direction.setY(0.4));
		    }
		}

		if (Ouroboros.debug) 
		{
			String name = target.getCustomName();
			PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oDamageEvent&r&f -- &aOK&7 || &fMob: "+
					(name!=null?name:PrintUtils.getFancyEntityName(data.getEntityType()))+
					"\n                          &7&f- Damage Dealt: &c"+
					value+"&7 || &aHP: &f"+data.getHp(false)+"&7/&f"+data.getHp(true)+
					(data.isBreak()?" &7|| &6Break&f: &aTRUE&f":("&7 || &6Break&f: &cFALSE&7 || &6AR&f: "+
					data.getArmor(false)+"&7/&f"+data.getArmor(true)))+
					"\n                          &b&o> DamageType&r&f: "+element.getKey()+
					"\n                          &b&o- Weakness Damage&r&f: "+(MobAffinity.parseUniversalWeakness(target, element)?"&aTRUE&f ":"&cFALSE&f ")+
					"\n                          &b&o- Resistance Damage&r&f: "+(MobAffinity.parseUniversalResistance(target, element)?"&aTRUE&f ":"&cFALSE&f ")+
					"\n                          &b&o- Immunity Damage&r&f: "+(MobAffinity.parseUniversalImmunity(target, element)?"&aTRUE&f ":"&cFALSE&f ")+"|| &o&7END");
		}

		if (data.isDead()) 
		{
			LivingEntity le = (LivingEntity) target;
			MobNameplate.remove(le);
			data.save();
			
			dyingRegistry.add(le.getUniqueId());
			
			le.setMetadata("ouroboros_dying", new FixedMetadataValue(Ouroboros.instance, true));
			if (player != null) le.setMetadata("ouroboros_killer", new FixedMetadataValue(Ouroboros.instance, player.getUniqueId().toString()));
			le.setHealth(0);
			return value;
		}

		if (target.getCustomName() == null && !MobData.isDying(target.getUniqueId()))
		{
			MobNameplate.build((LivingEntity) target, true);
			MobNameplate.update((LivingEntity) target);
		}
		else MobNameplate.update((LivingEntity) target);
		
		return value;
	}
	
	public void kill()
	{
		dyingRegistry.add(uuid);
		damage(getHp(false), false, ElementType.PURE);
		Entity entity = Bukkit.getEntity(uuid);
		LivingEntity le = (LivingEntity) entity;
		le.setHealth(0);
		MobNameplate.remove(le);
		deleteFile();
	}
	
	public boolean isBreak() 
	{
		return config.getBoolean("mob.broken_status");
	}
	
	public void setBreak(boolean bool) 
	{
		config.set("mob.broken_status", bool);
	}
	
	public void setBreak()
	{
		if (getMob(uuid) == null) return;
		
		setArmor(0, false);
	    setBreak(true);
	    
	    Entity entity = Bukkit.getEntity(uuid);
	    ((Mob) entity).setAI(false);
	    ((Mob) entity).setTarget(null);
	    
	    MobNameplate.update((LivingEntity) entity);
	    EntityEffects.add((LivingEntity) entity, PotionEffectType.SLOWNESS, 300, 99, false);
	    ObsParticles.drawDisc(entity.getLocation(), entity.getWidth(), 1, 7, 0.1, Particle.CRIMSON_SPORE, null);
		ObsParticles.drawWisps(entity.getLocation(), entity.getWidth(), entity.getHeight(), 8, Particle.LARGE_SMOKE, null);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			MobData data = MobData.getMob(uuid);
			if (data == null || data.isDead() || entity.isDead()) return;
			((Mob) entity).setAI(true);
		    setArmor(getArmor(true), false);
			setBreak(false);
			MobNameplate.update((LivingEntity) entity);
			ObsParticles.drawWisps(entity.getLocation(), entity.getWidth(), entity.getHeight(), 5, Particle.WAX_ON, null);
			save();
		}, 400);
		save();
	}
	
	public boolean isDead()
	{
		return config.getDouble("mob.hp.current") <= 0;
	}
	
	public static boolean checkFatalDamage(MobData data, double damage)
	{
		if (data == null) return false;
		double hp = data.getHp(false);
		if (hp - damage < 0) return true;
		return false;
	}
	
	public static double getOverflowFatalDamage(MobData data, double damage)
	{
		if (data == null || !checkFatalDamage(data, damage)) return 0;
		return Math.abs(data.getHp(false) - damage);
	}
	
	public void save() 
	{
		try {config.save(file);}
		catch (IOException e) {e.printStackTrace();}
	}
	
	public static void saveAll() 
	{
	    for (MobData data : dataMap.values()) 
	    {
	        data.save();
	    }
	}
	
	public MobData getInstance() 
	{
		return this;
	}

	public static MobData loadMobData(LivingEntity entity) 
	{
		UUID uuid = entity.getUniqueId();
		if (isDying(uuid)) return null;
		if (!dataMap.containsKey(uuid)) dataMap.put(uuid, new MobData(entity));
		return dataMap.get(uuid);
	}
	
	public static UUID parseUUID(Entity entity)
	{
	    String uuidString = entity.getPersistentDataContainer().get(MobManager.MOB_UUID_KEY, PersistentDataType.STRING);
	    if (uuidString == null) return null;
	    try
	    {
	        return UUID.fromString(uuidString);
	    }
	    catch (IllegalArgumentException e)
	    {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public void deleteFile()
	{
		dataMap.remove(uuid);
		file.delete();
	}
	
	public static void initializeDataFolder()	
	{
		File mobDataFolder = new File(getDataFolder(), "mobs");
		if (!mobDataFolder.exists()) mobDataFolder.mkdirs();
		File mobDataSubFolder = new File(getDataFolder(), "mobs/data");
		if (!mobDataSubFolder.exists()) mobDataSubFolder.mkdirs();
	}
	
	public static File getDataFolder() 
	{
		return Ouroboros.instance.getDataFolder();
	}

	public String debugDump()
	{
		return "&7[MobData Dump]&f" +
			"\n  UUID:    &b" + uuid +
			"\n  Name:    &f" + getName() +
			"\n  Type:    &f" + getEntityType() +
			"\n  Level:   &b" + getLevel() +
			"\n  HP:      &a" + getHp(false) + "&7/&f" + getHp(true) +
			"\n  Armor:   &e" + getArmor(false) + "&7/&f" + getArmor(true) +
			"\n  Break:   " + (isBreak() ? "&cTRUE" : "&aFALSE") +
			"\n  Dying:   " + (dyingRegistry.contains(uuid) ? "&cTRUE" : "&aFALSE") +
			"\n  In Map:  " + (dataMap.containsKey(uuid) ? "&aYES" : "&cNO") +
			"\n  File:    " + (file.exists() ? "&aEXISTS" : "&cMISSING");
	}
}