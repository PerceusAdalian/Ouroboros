package com.ouroboros.abilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;

public abstract class AbstractOBSAbility 
{
	private final File file;
	private final YamlConfiguration config;
	private final String displayName;
	private final String internalName;
	private final Material icon;
	private final String[] description;
	protected boolean isRegistered;
	
	public static final NamespacedKey OBSABILITY = new NamespacedKey(Ouroboros.instance, "obsability");
	
	public AbstractOBSAbility(String displayName, String internalName, Material icon, String...description) 
	{
		this.displayName = displayName;
		this.internalName = internalName;
		this.icon = icon;
		this.description = description;
		this.file = new File(getDataFolder(), "abilities/"+internalName+".yml");
		this.config = YamlConfiguration.loadConfiguration(file);
		
		if (!file.exists()) 
		{
			setInfo();
			save();
		}
	}
	
	public void setInfo() 
	{
		config.set("ability_name", displayName);
		config.set("internal_name", internalName);
		config.set("icon_material", icon.toString());
		config.set("description", description);
		config.set("namespace", OBSABILITY.toString());
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
	
	public static File getDataFolder() 
	{
		return Ouroboros.instance.getDataFolder();
	}
	
	public String getDisplayName() 
	{
		return config.getString("ability_name");
	}
	
	public String getInternalName() 
	{
		return config.getString("internal_name");
	}
	
	public Material getIcon() 
	{
		return Material.getMaterial(config.get("icon_material").toString());
	}
	
	public String[] getDescription() 
	{
		return description;
	}
	
	public AbstractOBSAbility getInstance() 
	{
		return this;
	}
	
	public NamespacedKey getKey() 
	{
		return OBSABILITY;
	}
	
	public abstract boolean cast(PlayerInteractEvent p);
	
	public ItemStack toIcon() 
	{
		ItemStack stack = new ItemStack(icon, 1);
		ItemMeta meta = stack.getItemMeta();
		List<String> lore = new ArrayList<>();
		
		meta.setDisplayName(PrintUtils.ColorParser("&r&f"+displayName));
		lore.add("\n");
		for (String line : description) 
			lore.add(PrintUtils.ColorParser("&r&f"+line) + "\n");
		lore.add("\n");
		meta.setLore(lore);
		meta.getPersistentDataContainer().set(OBSABILITY, PersistentDataType.STRING, internalName.toString());
		stack.setItemMeta(meta);
		
		return stack;
	}
	
}
