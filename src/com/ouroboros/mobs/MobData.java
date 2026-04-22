package com.ouroboros.mobs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.utils.LevelTable;
import com.ouroboros.mobs.utils.MobManager;
import com.ouroboros.mobs.utils.MobNameplate;
import com.ouroboros.utils.EntityCategories;
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

public class MobData 
{
	protected final UUID uuid;
	private final File file;
	private final YamlConfiguration config;

	private static final Map<UUID, MobData> dataMap = new HashMap<>();

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
	
	public void initialize(LivingEntity entity)
	{
	    if (!(entity instanceof LivingEntity livingEntity)) return;
	    String name = PrintUtils.getFancyEntityName(entity.getType());
	    double baseHp = livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
	    int level = LevelTable.getLevel(entity.getLocation().getBlock().getBiome());
	    double hp = baseHp * ((level * 0.15d) + 1.0);
	    int armor = (int) (hp * 0.3);
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
	    entity.getPersistentDataContainer().set(MobManager.MOB_UUID_KEY, PersistentDataType.STRING, uuid.toString()); // add this
	    save();
	}
	
	public void initializeSummoned(LivingEntity entity, int level, String customName)
	{
		double baseHp = entity.getAttribute(Attribute.MAX_HEALTH).getValue();
		double hp = baseHp * ((level * 0.15d) + 1.0);
		int armor = (int) (hp * 0.3);
		
		setUUID(uuid);
		setName(customName);          // stored directly, nameplate reads this back
		setEntityType(entity.getType());
		setLevel(level);
		setHp(Math.ceil(hp), true);
		setHp(Math.ceil(hp), false);
		setArmor(armor, true);
		setArmor(armor, false);
		setBreak(false);
		setLocation(entity.getLocation());
		entity.getPersistentDataContainer().set(MobManager.MOB_DATA_KEY, PersistentDataType.STRING, serialize());
		entity.getPersistentDataContainer().set(MobNameplate.customMob, PersistentDataType.STRING, customName);
		save();
	}
	
	public static void convertLegacyMob(LivingEntity entity)
	{
	    UUID uuid = entity.getUniqueId();
	    dataMap.remove(uuid);

	    File staleFile = new File(getDataFolder(), "mobs/data/" + uuid + ".yml");
	    if (staleFile.exists()) staleFile.delete();
	    MobData data = MobData.loadMobData(entity);
	    if (data == null) return;

	    MobNameplate.build(entity, true);

	    var att = ((Attributable) entity).getAttribute(Attribute.MAX_HEALTH);
	    att.setBaseValue(1023.9);
	    ((Damageable) entity).setHealth(att.getBaseValue());
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
		//Parse segments
		String[] segments = data.split("\\|");
		if (segments.length != 10) return null;
		
		//Data IDs
		UUID uuid = UUID.fromString(segments[0]);
		String name = segments[1];
		EntityType eType = EntityType.valueOf(segments[2].trim().toUpperCase().replace(" ", "_"));
		int level = Integer.parseInt(segments[3]);
		double baseHp = Double.parseDouble(segments[4]);
		double currentHp = Double.parseDouble(segments[5]);
		int baseArmor = Integer.parseInt(segments[6]);
		int currentArmor = Integer.parseInt(segments[7]);
		boolean isBroken = Boolean.parseBoolean(segments[8]);
		
		//Loc IDs
		String[] locSegments = segments[9].split(",");
		World world = server.getWorld(locSegments[0]);
		double x = Double.parseDouble(locSegments[1]);
        double y = Double.parseDouble(locSegments[2]);
        double z = Double.parseDouble(locSegments[3]);
        Location loc = new Location(world,x,y,z);
        
        //Generate new MobData file and reconstruct the config
        File mobFile = new File(getDataFolder(), "mobs/data/"+uuid+".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(mobFile);
        MobData mobData = new MobData(uuid, mobFile, config);
        
        //Setting the data in their config from the deserialization
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
        
        //Load the mob into the data map, and save their file
        dataMap.put(uuid, mobData);
        mobData.save();
        
        return mobData;
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
	
	public ElementType getAffinity(boolean getWeakness)
	{
		
		return getWeakness ? ElementType.valueOf(config.getString("mob.affinity")) : ElementType.valueOf(config.getString("mob.affinity.weakness"));
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
	
	/**
	 * @param value
	 * @param damageArmor
	 * @param element
	 */
	public void damage(double value, boolean damageArmor, @Nullable ElementType element)
	{
		MobData data = MobData.getMob(uuid);
		Entity entity = Bukkit.getEntity(uuid);
		
		if (element == null) element = ElementType.PURE;
		
		if (!CosmoEffects.isVoidedRegistry.containsKey(uuid))
		{
			if (EntityCategories.parseUniversalImmunity(entity, element)) value = 0;
			else if (EntityCategories.parseUniversalResistance(entity, element)) value *= 0.5;
			else if (EntityCategories.parseUniversalWeakness(entity, element)) value *= 1.5;
		}
		
		//Damage Bonuses From Effects
		if (CelestioEffects.hasHumility.contains(uuid) && element == ElementType.CELESTIO) value *= 1.15;
		if (InfernoEffects.hasCharred.contains(uuid) && element == ElementType.INFERNO) value *= 1.25;
		if (AeroEffects.hasShock.contains(uuid) && element == ElementType.AERO) value *= 1.25;
		if (HeresioEffects.isIntimidated.contains(uuid) && element == ElementType.HERESIO) value *= 1.20;
		if (ArcanoEffects.hasEtherOverload.contains(uuid) && ElementType.elemental.contains(element)) value *= 1.5;
		if (GeoEffects.hasVulnerable.contains(uuid) && ElementType.physical.contains(element)) value *= 1.5;
		
		double currentHP = data.getHp(false);
		double newHP = currentHP - value;
		data.setHp(newHP, false);

		if (damageArmor) damageArmor(value);
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
		if (isBreak()) 
			setBreak(false);
		setArmor(setMax ? getArmor(true) : (getArmor(false) + (int) value), false);
		Entity entity = Bukkit.getEntity(uuid);
		MobNameplate.update((LivingEntity) entity);
		ObsParticles.drawWisps(entity.getLocation(), entity.getWidth(), entity.getHeight(), 5, Particle.WAX_ON, null);
	}
	
	public void damageArmor(double value)
	{
		
		if (isBreak()) return;
		int currentArmor = getArmor(false);
		int newArmor = (int) (currentArmor - (value * 0.5));
		setArmor(newArmor, false);
		if (getArmor(false) <= 0)
		{
			setBreak(true);
			Entity entity = Bukkit.getEntity(uuid);
			ObsParticles.drawWisps(entity.getLocation(), entity.getWidth(), entity.getHeight(), 10, Particle.END_ROD, null);
			setArmor(0, false);
			MobNameplate.update((LivingEntity) entity);
			Bukkit.getScheduler().runTaskLaterAsynchronously(Ouroboros.instance, ()->
			{
				setArmor(getArmor(true), false);
				setBreak(false);
				MobNameplate.update((LivingEntity) entity);
			}, 300);
		}
	}
	
	public static double damageUnnaturally(@Nullable Player player, Entity target, double value, boolean doHurtAnimation, boolean damageArmor, @Nullable ElementType element)
	{
		MobData data = MobData.getMob(target.getUniqueId());
		
		if (data == null)
		{
			((Damageable) target).damage(value);
		}
		else 
		{
			if (element == null) element = ElementType.PURE;
			EntityEffects.checkFromCombat((LivingEntity) target, element); // apply effects first

			if (data.isBreak())
			    data.breakDamage(value, 10);
			else
			    data.damage(value, damageArmor, element);
			
			data.save();

			//Update their HP bar
			MobNameplate.update((LivingEntity) target);
			
			if (data.isDead())
			{
				LivingEntity le = (LivingEntity) target;
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> le.setHealth(0), 5L);
				MobNameplate.remove(le);
				data.deleteFile();
			}
		}

		((LivingEntity) target).playHurtAnimation(0);
		if (doHurtAnimation && player != null) 
		{
			Vector direction = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(0.4);
			target.setVelocity(direction.setY(0.4));	
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
					"\n                          &b&o- Weakness Damage&r&f: "+(EntityCategories.parseUniversalWeakness(target, element)?"&aTRUE&f ":"&cFALSE&f ")+
					"\n                          &b&o- Resistance Damage&r&f: "+(EntityCategories.parseUniversalResistance(target, element)?"&aTRUE&f ":"&cFALSE&f ")+
					"\n                          &b&o- Immunity Damage&r&f: "+(EntityCategories.parseUniversalImmunity(target, element)?"&aTRUE&f ":"&cFALSE&f ")+"|| &o&7END");
		}
		
		return value;
	}
	
	public void kill()
	{
		damage(getHp(false), false, ElementType.PURE);
		Entity entity = Bukkit.getEntity(uuid);
		LivingEntity le = (LivingEntity) entity;
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> le.setHealth(0), 5L);
		MobNameplate.remove(le);
		deleteFile();
	}
	
	
	public void breakDamage(double value, double percent)
	{
		MobData data = MobData.getMob(uuid);
		double baseHP = data.getHp(true);
		double damage = ((percent/100.0)*baseHP)+value;
		damage(damage, false, ElementType.PURE);
	}
	
	public boolean isBreak() 
	{
		return config.getBoolean("mob.broken_status");
	}
	
	/**
	 * @param bool Sets the status break in the config of the mob.
	 */
	public void setBreak(boolean bool) 
	{
		config.set("mob.broken_status", bool);
	}
	
	/**
	 * @return Sets the mob's break status and depletes the mob's AR, directly causing a break. 
	 */
	public void setBreak()
	{
	    setArmor(0, false);
	    setBreak(true);
	    Entity entity = Bukkit.getEntity(uuid);
	    ObsParticles.drawWisps(entity.getLocation(), entity.getWidth(), entity.getHeight(), 10, Particle.END_ROD, null);
	    MobNameplate.update((LivingEntity) entity);
	    EntityEffects.add((LivingEntity) entity, PotionEffectType.SLOWNESS, 300, 99, false);
	    ((Mob) entity).setAI(false);
	    ((Mob) entity).setTarget(null);
	    Bukkit.getScheduler().runTaskLaterAsynchronously(Ouroboros.instance, () ->
	    {
	    	((Mob) entity).setAI(true);
	        setArmor(getArmor(true), false);
	        setBreak(false);
	        MobNameplate.update((LivingEntity) entity);
	    }, 300);
	}
	
	public boolean isDead()
	{
		return config.getDouble("mob.hp.current") <= 0;
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
	}
	
	public static File getDataFolder() 
	{
		return Ouroboros.instance.getDataFolder();
	}
}
