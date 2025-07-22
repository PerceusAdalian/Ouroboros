package com.ouroboros.mobs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
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
			String name = PrintUtils.getFancyEntityName(entity.getType());
			if (!(entity instanceof LivingEntity livingEntity)) return;
			double baseHp = livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
			double hp = baseHp*((level*0.15d)+1.0);
			int armor = (int) (hp*0.3);
			setName(name);
			setEntityType(livingEntity.getType());
			setLevel(level);
			setHp(hp, true);
			setHp(hp, false);
			setArmor(armor, true);
			setArmor(armor, false);
			setBreak(false);
			entity.getPersistentDataContainer().set(AbstractObsMob.OBSMOB, PersistentDataType.STRING, name);
			save();			
		}
		return;
	}
	
	public EntityType getEntityType() 
	{
		return (EntityType) config.get("mob.entity_type");
	}
	
	public void setEntityType(EntityType eType) 
	{
		config.set("mob.entity_type", eType);
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
	
	public static void unloadMobData(UUID uuid)
	{
		MobData data = dataMap.remove(uuid);
		if (data != null) data.save();
	}
	
	public void deleteFile(UUID uuid)
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
