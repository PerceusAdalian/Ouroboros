package com.ouroboros.mobs;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataType;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.Nullable;
import com.ouroboros.utils.PrintUtils;

public abstract class AbstractObsMob 
{
	public static final NamespacedKey OBSMOB = new NamespacedKey(Ouroboros.instance, "obs_mob");
	
	private EntityType eType;
	private String name;
	private int level,armor;
	private double health, HPMOD = (getLevel()*0.15d)+1.0;
	
	public AbstractObsMob(EntityType eType, @Nullable String name, int level, double health, int armor)
	{
		this.eType = eType;
		this.name = name;
		this.health = health;
		this.armor = armor;
	}
	
	public EntityType getEntityType() 
	{
		return eType;
	}

	public void setEntityType(EntityType eType) 
	{
		this.eType = eType;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public int getLevel() 
	{
		return level;
	}

	public void setLevel(int level) 
	{
		this.level = level;
	}

	public double getHealth() 
	{
		return health;
	}

	public void setHealth(double health) 
	{
		this.health = health;
	}

	public int getArmor() 
	{
		return armor;
	}

	public void setArmor(int armor) 
	{
		this.armor = armor;
	}

	public NamespacedKey getKey() 
	{
		return OBSMOB;
	}
	
	public AbstractObsMob getInstance() 
	{
		return this;
	}

	public Entity generate(Location loc) 
	{
		Entity obsMob = loc.getWorld().spawnEntity(loc, getEntityType());
		MobData mobData = new MobData(obsMob);
		
		mobData.setName(getName());
		mobData.setEntityType(getEntityType());
		mobData.setHp(getHealth(),true);
		mobData.setHp(getHealth(), false);
		mobData.setArmor(getArmor(),true);
		mobData.setArmor(getArmor(), false);
		mobData.setLevel(getLevel());
		mobData.setBreak(false);
		
		if (getName() == null)
		{
			String inherentName = PrintUtils.getFancyEntityName(eType);
			setName(inherentName);
			mobData.setName(getName());
		}
		
		if (getHealth() == 0)
		{
			setHealth(((Attributable) obsMob).getAttribute(Attribute.MAX_HEALTH).getValue()*HPMOD);
			mobData.setHp(getHealth(),true);
			mobData.setHp(getHealth(), false);
		}
		
		if (getArmor() == 0)
		{
			setArmor((int) (getHealth()*HPMOD*0.5));
			mobData.setArmor(getArmor(),true);
			mobData.setArmor(getArmor(), false);
		}
		
		if (getLevel() == 0)
		{
			setLevel(LevelTable.getLevel(obsMob.getLocation().getBlock().getBiome()));
			mobData.setLevel(getLevel());
		}
		
		obsMob.setCustomName(PrintUtils.ColorParser("&e{&f&lLvl&r&f: &l" + getLevel() + "&r&e} &f" + getName()));
		obsMob.setCustomNameVisible(true);
		obsMob.getPersistentDataContainer().set(OBSMOB, PersistentDataType.STRING, getName());
		
		if (Ouroboros.debug == true) 
		{
			String str = obsMob.getCustomName().toString();
			PrintUtils.OBSConsoleDebug("From AbstractObsMob: &f"+str+" || &aGenerated Successfully");
		}
		
		mobData.save();
		return obsMob;
	}
}
