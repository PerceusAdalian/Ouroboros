package com.ouroboros.mobs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;

public class MobData 
{
	protected final UUID uuid;
	private final File file;
	private final YamlConfiguration config;

	private static final Map<UUID, MobData> dataMap = new HashMap<>();

	public MobData(Entity entity) 
	{
		this.uuid = entity.getUniqueId();
		this.file = new File(getDataFolder(), "mobs/"+uuid.toString()+".yml");
		this.config = YamlConfiguration.loadConfiguration(file);
		
		if (!file.exists()) 
		{
			int level = LevelTable.getLevel(entity.getLocation().getBlock().getBiome());
			initialize(entity, level);
			save();
		}
	}
	
	public static MobData getMob(UUID uuid) 
	{
		return dataMap.get(uuid);
	}
	
	public void initialize(Entity entity, int level)
	{
		if (!file.exists())
		{
			if (!(entity instanceof LivingEntity livingEntity)) return;
			String name = PrintUtils.getFancyEntityName(entity.getType());
			double baseHp = livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
			double hp = baseHp*((level*0.15d)+1.0);
			int armor = (int) (hp*0.3);
			setUUID(uuid);
			setName(name);
			setEntityType(livingEntity.getType());
			setLevel(level);
			setHp(hp, true);
			setHp(hp, false);
			setArmor(armor, true);
			setArmor(armor, false);
			setBreak(false);
			entity.getPersistentDataContainer().set(MobGenerateEvent.mobKey, PersistentDataType.STRING, uuid.toString());
			entity.getPersistentDataContainer().set(AbstractObsMob.OBSMOB, PersistentDataType.STRING, getName());
			save();			
		}
		return;
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
	
	public void setName(String str) 
	{
		config.set("mob.name", str);
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
	
	public void damage(double value, boolean damageArmor)
	{
		MobData data = MobData.getMob(uuid);
		
		double currentHP = data.getHp(false);
		double newHP = currentHP - value;
		data.setHp(newHP, false);

		if (damageArmor)
		{
			if (data.isBreak()) return;
			int currentArmor = data.getArmor(false);
			int newArmor = (int) (currentArmor - (value * 0.5));
			data.setArmor(newArmor, false);
			if (data.getArmor(false) <= 0)
			{
				data.setBreak(true);
				Entity entity = Bukkit.getEntity(uuid);
				OBSParticles.drawWisps(entity.getLocation(), entity.getWidth(), entity.getHeight(), 10, Particle.END_ROD, null);
				data.setArmor(0, false);
				Bukkit.getScheduler().runTaskLaterAsynchronously(Ouroboros.instance, ()->
				{
					data.setArmor(data.getArmor(true), false);
					return;
				}, 600);
			}
		}
	}
	
	public void breakDamage(double value, double percent)
	{
		MobData data = MobData.getMob(uuid);
		double baseHP = data.getHp(true);
		double damage = ((percent/100.0)*baseHP)+value;
		damage(damage, false);
	}
	
	public boolean isBreak() 
	{
		return config.getBoolean("mob.broken_status");
	}
	
	public void setBreak(boolean bool) 
	{
		config.set("mob.broken_status", bool);
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
	
	public static void loadMobData(Entity entity) 
	{
		UUID uuid = entity.getUniqueId();
		if (!dataMap.containsKey(uuid)) dataMap.put(uuid, new MobData(entity));
	}
	
	public static void loadAll()
	{
		for (World w : Bukkit.getWorlds())
		{
			for (Entity e : w.getEntities())
			{
				if (!(e instanceof LivingEntity)) continue;
				processEntity(e);
			}
		}
	}
	
	public static void unloadAll()
	{
		for (World w : Bukkit.getWorlds())
		{
			for (Entity e : w.getEntities())
			{
				if (!(e instanceof LivingEntity)) continue;
				if (!e.getPersistentDataContainer().has(MobGenerateEvent.mobKey)) continue;
				
				MobData.unloadMobData(e);
				ObsMobHealthbar.removeBossBar(e);
			}
		}
	}
	
	public static void unloadMobData(Entity entity)
	{
		MobData data = dataMap.remove(entity.getUniqueId());
		if (data != null) data.save();
	}
	
	public static UUID parseUUID(Entity entity) 
	{ 
		String uuidString = entity.getPersistentDataContainer().get(MobGenerateEvent.mobKey, PersistentDataType.STRING);
		if (uuidString == null) return null;

		UUID uuid;
		try 
		{
			uuid = UUID.fromString(uuidString);
		} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
			return null;
		}
		return uuid;
	}

	public static void processEntity(Entity entity)
	{
		PersistentDataContainer container = entity.getPersistentDataContainer();

		if (!container.has(MobGenerateEvent.mobKey, PersistentDataType.STRING)) 
		{
			registerNewEntity(entity);
		} 
		else 
		{
			loadExistingEntity(entity);
		}
	}
	
	public static void registerNewEntity(Entity entity)
	{
		UUID uuid = entity.getUniqueId();
		entity.getPersistentDataContainer().set(MobGenerateEvent.mobKey, PersistentDataType.STRING, uuid.toString());
		int level = LevelTable.getLevel(entity.getLocation().getBlock().getBiome());
		
		MobData.loadMobData(entity);
		MobData data = MobData.getMob(uuid);
		
		if (data == null) 
		{
			if (Ouroboros.debug) PrintUtils.OBSConsoleError("No MobData found for UUID: "+uuid);
			return;
		}

		data.initialize(entity, level);
		setMobVisuals(entity, data);
	}
	
	public static void loadExistingEntity(Entity entity)
	{
		MobData data = MobData.getMob(parseUUID(entity));
		if (data == null) 
		{
			if (Ouroboros.debug) PrintUtils.OBSConsoleError("No MobData found for UUID: "+parseUUID(entity));
			return;
		}
		
		int level = data.getLevel();
		
		data.initialize(entity, level);
		setMobVisuals(entity, data);
	}
	
	public static void setMobVisuals(Entity entity, MobData data) 
	{
		int level = data.getLevel();
		entity.setCustomName(PrintUtils.ColorParser("&e{&f&lLvl&r&f: &l" + level + "&r&e} &f" + data.getName()));
		entity.setCustomNameVisible(true);
		ObsMobHealthbar.initializeHPBar(entity, false);
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
