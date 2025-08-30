package com.ouroboros.objects;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.ouroboros.Ouroboros;
import com.ouroboros.utils.PrintUtils;

public abstract class AbstractObsObject 
{
	
	public static final NamespacedKey obsObject = new NamespacedKey(Ouroboros.instance, "obs_obj");
	private String name;
	protected String internalName;
	private String[] itemDescription;
	private Material material;
	private boolean isEnchanted = false;
	private boolean canDrop;
	
	public AbstractObsObject(String name, String internalName, Material material, boolean isEnchanted, boolean canDrop, String...itemDescription) 
	{
		this.name = name;
		this.internalName = internalName;
		this.material = material;
		this.isEnchanted = isEnchanted;
		this.itemDescription = itemDescription;
		this.canDrop = canDrop;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public String getInternalName() 
	{
		return internalName;
	}
	public void setInternalName(String internalName) 
	{
		this.internalName = internalName;
	}
	public String[] getItemDescription() 
	{
		return itemDescription;
	}
	public void setItemDescription(String[] itemDescription) 
	{
		this.itemDescription = itemDescription;
	}
	public Material getMaterial() 
	{
		return material;
	}
	public void setMaterial(Material material) 
	{
		this.material = material;
	}
	public boolean isEnchanted() 
	{
		return isEnchanted;
	}
	public boolean canDrop()
	{
		return canDrop;
	}
	public AbstractObsObject getInstance() 
	{
		return this;
	}
	public static String getInternalNameAsID(String internalName) 
	{
		int internalNameID = 0;
		for (char ch : internalName.toCharArray()) 
		{
			internalNameID += (int) ch;
		}
		return Integer.toHexString(internalNameID).toUpperCase();
	}
	
	public abstract boolean cast(PlayerInteractEvent e);
	
	public ItemStack toItemStack() 
	{
		ItemStack stack = new ItemStack(material, 1);
		ItemMeta meta = stack.getItemMeta();
		List<String> lore = new ArrayList<>();
			
		if (this.isEnchanted() == true) 
		{
			meta.setEnchantmentGlintOverride(true);
		}
		
		lore.add("\n");
		
		meta.setDisplayName(PrintUtils.ColorParser("&r&7&ko&r&f&l "+name+" &r&7&ko&r&f&l"));	
		for (String line : itemDescription) 
		{
			lore.add(PrintUtils.ColorParser("&r&f" + line) + "\n");
		}
		lore.add("\n");
		lore.add(PrintUtils.ColorParser("&r&7&oOBS Object ID: O_" + getInternalNameAsID(internalName)));			
		
		meta.setLore(lore);
		meta.getPersistentDataContainer().set(obsObject, PersistentDataType.STRING, internalName.toString());
		stack.setItemMeta(meta);
		
		return stack;
	}
}

