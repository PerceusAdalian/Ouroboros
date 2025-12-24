package com.lol.spells.instances;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.Nullable;
import com.ouroboros.utils.PrintUtils;

public abstract class Spell 
{
	private final File file;
	private final YamlConfiguration config;
	
	private String name;
	private String internalName;
	private Material icon;
	private SpellType sType1;
	private SpellType sType2;
	private SpellementType eType;
	private CastConditions castCondition;
	private int manacost;
	private double cooldown;
	private Rarity spellTier;
	private String[] spellDescription;
	
	public Spell(String name, String internalName, Material icon, SpellType sType1, @Nullable SpellType sType2, SpellementType eType, CastConditions castCondition, Rarity spellTier, int manacost, double cooldown, String...spellDescription)
	{
		this.name = name;
		this.internalName = internalName;
		this.icon = icon;
		this.sType1 = sType1;
		this.eType = eType;
		this.castCondition = castCondition;
		this.spellTier = spellTier;
		this.manacost = manacost;
		this.cooldown = cooldown;
		this.spellDescription = spellDescription;
		
		this.file = new File(getDataFolder(), "spells/"+internalName+".yml");
		this.config = YamlConfiguration.loadConfiguration(file);
		
		if (!file.exists())
		{
			setInfo();
			save();
		}
	}
	
	public void setInfo()
	{
		config.set("spell_name", name);
		config.set("internal_name", internalName);
		config.set("icon_material", icon.toString());
		if (sType2 == null) 
			config.set("spell_type", sType1);
		else
		{
			config.set("spell_type_1", sType1);
			config.set("spell_type_2", sType2);
		}
		config.set("element_type", eType.getType());
		config.set("description", spellDescription);
		config.set("manacost", manacost);
		config.set("cooldown", cooldown);
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
	
	public String getName() 
	{
		return config.getString("spell_name");
	}
	
	public String getInternalName()
	{
		return config.getString("internal_name");
	}

	public Material getIcon() 
	{
		return Material.getMaterial(config.getString("icon_material"));
	}

	public String getInternalNameAsID() 
	{
		int internalNameID = 0;
		for (char ch : internalName.toCharArray()) 
		{
			internalNameID += (int) ch;
		}
		return Integer.toHexString(internalNameID).toUpperCase();
	}
	
	public SpellementType getElementType() 
	{
		return eType;
	}

	public CastConditions getCastCondition() 
	{
		return castCondition;
	}

	public int getManacost() 
	{
		return config.getInt("manacost");
	}

	public double getCooldown() 
	{
		return config.getDouble("cooldown");
	}

	public List<String> getLore() 
	{
		List<String> lore = new ArrayList<>();
		for (String line : spellDescription) 
		{
			lore.add(PrintUtils.ColorParser(line));
		}
		return lore;
	}
	
	public abstract boolean Cast(PlayerInteractEvent e);
	
	public static final NamespacedKey LOLSPELL = new NamespacedKey(Ouroboros.instance, "lolspell");
	public static final NamespacedKey LOLSPELLBOOK = new NamespacedKey(Ouroboros.instance, "lolspellbook");
	
	public ItemStack getAsItemStack(boolean toIcon)
	{
		ItemStack stack = new ItemStack(toIcon?icon:Material.WRITTEN_BOOK, 1);
		ItemMeta meta = stack.getItemMeta();
		
		meta.setDisplayName(PrintUtils.ColorParser(toIcon?"&r&f"+name:"&b&lOld Spellbook&r&f: "+name));
		List<String> lore = new ArrayList<>();
		lore.add(PrintUtils.assignRarity(spellTier));
		lore.add("");
		lore.add(PrintUtils.assignElementType(eType));		
		
		if (toIcon)
		{			
			if (sType2 == null) lore.add(PrintUtils.assignSpellType(sType1)); 
			else lore.add(PrintUtils.assignSpellType(sType1, sType2));
			lore.add(PrintUtils.assignCastCondition(castCondition));
			lore.add("");		
			lore.add(PrintUtils.ColorParser("&r&f&oDescription: \n"));
			for (String line : lore) lore.add(PrintUtils.ColorParser(line));
			lore.add("");
			lore.add(PrintUtils.ColorParser("&r&b&lMana Cost&r&f: "+manacost+" &7| &r&f&lCooldown&r&f: "+cooldown));
			meta.getPersistentDataContainer().set(LOLSPELL, PersistentDataType.STRING, internalName.toString());
		}
		else
		{
			lore.add("");
			lore.add("&f&nUsage&r&f: &d&oShift_Right-Click&r&f to register this spell to your account.");
			lore.add("");
			lore.add(PrintUtils.ColorParser("&7&oLegends of Lumina: ID_"+getInternalNameAsID()));			
			BookMeta bookMeta = (BookMeta) stack.getItemMeta();
			
			bookMeta.setAuthor(PrintUtils.assignAuthor(getElementType()));
			bookMeta.setGeneration(Generation.TATTERED);
			
			StringBuilder sb = new StringBuilder();
			Arrays.asList(spellDescription).forEach(line ->
			{
				sb.append(line.replace("&f", "&0") + '\n');
			});
			bookMeta.addPage(PrintUtils.ColorParser(sb.toString()));
			meta.getPersistentDataContainer().set(LOLSPELLBOOK, PersistentDataType.STRING, internalName.toString());
			stack.setItemMeta(bookMeta);
		}
		
		meta.setLore(lore);
		stack.setItemMeta(meta);
		
		return stack;
	}
}
