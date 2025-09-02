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
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.utils.AffinityRegistry;
import com.ouroboros.mobs.utils.LevelTable;
import com.ouroboros.mobs.utils.MobAffinity;
import com.ouroboros.mobs.utils.MobManager;
import com.ouroboros.mobs.utils.ObsMobHealthbar;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.Nullable;
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
	
	public void initialize(Entity entity)
	{
		if (!file.exists())
		{
			if (!(entity instanceof LivingEntity livingEntity)) return;
			String name = PrintUtils.getFancyEntityName(entity.getType());
			double baseHp = livingEntity.getAttribute(Attribute.MAX_HEALTH).getValue();
			int level = LevelTable.getLevel(entity.getLocation().getBlock().getBiome());
			double hp = baseHp*((level*0.15d)+1.0);
			int armor = (int) (hp*0.3);
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
			save();			
		}
		return;
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
	
	public void setAffinity(boolean setWeakness)
	{
		
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
	
	public void damage(double value, boolean damageArmor, @Nullable ElementType element)
	{
		MobData data = MobData.getMob(uuid);
		
		if (element == null) element = ElementType.NEUTRAL;
		
		MobAffinity affinity = AffinityRegistry.getAffinity(getEntityType());
		if (!EntityEffects.isVoidedRegistry.containsKey(uuid))
		{
			if (affinity.immuneTo(element)) value = 0;
			else if (affinity.resists(element)) value *= 0.5;
		}
		else if (affinity.weakTo(element)) value *= 1.5;
		
		double currentHP = data.getHp(false);
		double newHP = currentHP - value;
		data.setHp(newHP, false);

		if (damageArmor)
		{
			damageArmor(value);
		}
	}
	
	public void damageArmor(double value)
	{
		MobData data = MobData.getMob(uuid);
		
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
	
	public static void damageUnnaturally(@Nullable Player player, Entity target, double value, boolean damageArmor, @Nullable ElementType element)
	{
		MobData data = MobData.getMob(target.getUniqueId());
		
		boolean nullData = data == null ? true : false;
		if (nullData)
		{
			((Damageable) target).damage(value);
		}
		else 
		{
			if (element == null) element = ElementType.NEUTRAL;
			
			MobAffinity affinity = AffinityRegistry.getAffinity(target.getType());
			if (!EntityEffects.isVoidedRegistry.containsKey(target.getUniqueId()))
			{
				if (affinity.immuneTo(element)) 
				{
					EntityEffects.playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.MASTER, 1, 1);
					value = 0;
				}
				else if (affinity.resists(element)) 
				{
					EntityEffects.playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, SoundCategory.MASTER, 1, 1);
					value *= 0.5;
				}
			}
			else if (affinity.weakTo(element)) 
			{
				EntityEffects.playSound(player, player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.MASTER, 1, 1);
				value *= 1.5;
			}
			
			data.damage(value, damageArmor, element);
			data.save();

			ObsMobHealthbar.updateHPBar(target, true);
			
			if (data.isDead())
			{
				((Damageable) target).setHealth(0);
				ObsMobHealthbar.removeBossBar(target);
				data.deleteFile();
			}
		}

		((LivingEntity) target).playHurtAnimation(0);
		
		if (player!=null) 
		{
			Vector direction = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(0.4);
			target.setVelocity(direction.setY(0.4));	
		}
		
		if (Ouroboros.debug) 
		{
			MobAffinity affinity = AffinityRegistry.getAffinity(target.getType());
			
			String name = target.getCustomName();
			PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oDamageEvent&r&f -- &aOK&7 || &fMob: "+
					(name!=null?name:PrintUtils.getFancyEntityName(data.getEntityType()))+
					"\n                          &7&f- Damage Dealt: &c"+
					value+"&7 || &aHP: &f"+data.getHp(false)+"&7/&f"+data.getHp(true)+
					(data.isBreak()?" &7|| &6Break&f: &aTRUE&f":("&7 || &6Break&f: &cFALSE&7 || &6AR&f: "+
					data.getArmor(false)+"&7/&f"+data.getArmor(true))+
					"\n                          &b&o> DamageType&r&f: "+element.getKey()+
					"\n                          &b&o- Weakness Damage&r&f: "+(affinity.getWeaknesses().contains(element)?"&aTRUE&f ":"&cFALSE&f ")+
					"\n                          &b&o- Resistance Damage&r&f: "+(affinity.getResistances().contains(element)?"&aTRUE&f ":"&cFALSE&f ")+
					"\n                          &b&o- Immunity Damage&r&f: "+(affinity.getImmunities().contains(element)?"&aTRUE&f ":"&cFALSE&f ")+"|| &o&7END"));
		}
	}
	
	public void breakDamage(double value, double percent)
	{
		MobData data = MobData.getMob(uuid);
		double baseHP = data.getHp(true);
		double damage = ((percent/100.0)*baseHP)+value;
		damage(damage, false, null);
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
		MobData data = MobData.getMob(uuid);
		data.damageArmor(data.getArmor(false));
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

	public static MobData loadMobData(Entity entity) 
	{
		UUID uuid = entity.getUniqueId();
		if (!dataMap.containsKey(uuid)) dataMap.put(uuid, new MobData(entity));
		return dataMap.get(uuid);
	}
	
	@Deprecated
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
	
	@Deprecated
	public static void unloadAll()
	{
		for (World w : Bukkit.getWorlds())
		{
			for (Entity e : w.getEntities())
			{
				if (!(e instanceof LivingEntity)) continue;
				if (!e.getPersistentDataContainer().has(MobManager.MOB_DATA_KEY)) continue;
				
				MobData.unloadMobData(e);
				ObsMobHealthbar.removeBossBar(e);
			}
		}
	}
	
	@Deprecated
	public static void unloadMobData(Entity entity)
	{
		MobData data = dataMap.remove(entity.getUniqueId());
		if (data != null) data.save();
	}
	
	public static UUID parseUUID(Entity entity) 
	{ 
		String uuidString = entity.getPersistentDataContainer().get(MobManager.MOB_DATA_KEY, PersistentDataType.STRING);
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

	@Deprecated
	public static void processEntity(Entity entity)
	{
		PersistentDataContainer container = entity.getPersistentDataContainer();

		if (!container.has(MobManager.MOB_DATA_KEY, PersistentDataType.STRING)) 
		{
			registerNewEntity(entity);
		} 
		else 
		{
			loadExistingEntity(entity);
		}
	}

	@Deprecated
	public static void registerNewEntity(Entity entity)
	{
		UUID uuid = entity.getUniqueId();
		entity.getPersistentDataContainer().set(MobManager.MOB_DATA_KEY, PersistentDataType.STRING, uuid.toString());
		
		MobData.loadMobData(entity);
		MobData data = MobData.getMob(uuid);
		
		if (data == null) 
		{
			if (Ouroboros.debug) PrintUtils.OBSConsoleError("No MobData found for UUID: "+uuid);
			return;
		}

		data.initialize(entity);
		setMobVisuals(entity, data);
	}

	@Deprecated
	public static void loadExistingEntity(Entity entity)
	{
		MobData data = MobData.getMob(parseUUID(entity));
		if (data == null) 
		{
			if (Ouroboros.debug) PrintUtils.OBSConsoleError("No MobData found for UUID: "+parseUUID(entity));
			return;
		}
		
		data.initialize(entity);
		setMobVisuals(entity, data);
	}
	
	public static void setMobVisuals(Entity entity, MobData data) 
	{
		int level = data.getLevel();
		entity.setCustomName(PrintUtils.ColorParser("&e{&f&lLvl&r&f: &l" + level + "&r&e} &f" + PrintUtils.getFancyEntityName(data.getEntityType())));
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
