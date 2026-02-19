package com.lol.spells.instances;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import com.ouroboros.utils.PrintUtils;

public abstract class Spell 
{
	private final File file;
	private final YamlConfiguration config;
	
	private String name;
	private String internalName;
	private Material icon;
	private SpellType sType;
	private SpellementType eType;
	private CastConditions castCondition;
	private int manacost;
	private double cooldown;
	private Rarity spellTier;
	private boolean pvpCompatible;
	private String[] spellDescription;
	
	public Spell(String name, String internalName, Material icon, SpellType sType, SpellementType eType, CastConditions castCondition, Rarity spellTier, int manacost, double cooldown, 
			boolean pvpCompatible, String...spellDescription)
	{
		this.name = name;
		this.internalName = internalName;
		this.icon = icon;
		this.sType = sType;
		this.eType = eType;
		this.castCondition = castCondition;
		this.spellTier = spellTier;
		this.manacost = manacost;
		this.cooldown = cooldown;
		this.spellDescription = spellDescription;
		this.pvpCompatible = pvpCompatible;
		
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
		config.set("spell_type", sType.toString());
		config.set("element_type", eType.name());
		config.set("description", spellDescription);
		config.set("manacost", manacost);
		config.set("cooldown", cooldown);
		config.set("pvpCompatible", pvpCompatible);
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
		return SpellementType.fromString(config.getString("element_type"));
	}
	
	public SpellType getSpellType()
	{
		return SpellType.fromString(config.getString("spell_type"));
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
	
	public Rarity getRarity()
	{
		return spellTier;
	}

	public boolean isPvpCombatible() {
		return pvpCompatible;
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
		List<String> lore = new ArrayList<>();
		
		meta.setDisplayName(PrintUtils.ColorParser(toIcon?"&r&f"+name:"&b&lOld Spellbook&r&f: "+name));

		lore.add(PrintUtils.assignRarity(spellTier));
		lore.add("");
		lore.add(PrintUtils.assignElementType(eType));		
		
		if (toIcon)
		{       
			if (pvpCompatible) PrintUtils.assignPVPCompatible();
			lore.add(PrintUtils.assignSpellType(sType));
		    lore.add(PrintUtils.assignCastCondition(castCondition));
		    lore.add("");        
		    lore.add(PrintUtils.ColorParser("&r&f&oDescription: \n"));
		    
		    List<String> parsedDescriptionLines = new ArrayList<>();
		    for (String line : spellDescription) 
		    {
		        parsedDescriptionLines.add(PrintUtils.ColorParser(line));
		    }
		    lore.addAll(parsedDescriptionLines);
		    
		    lore.add("");
		    lore.add(PrintUtils.ColorParser("&r&b&lMana Cost&r&f: "+manacost+" &7| &r&f&lCooldown&r&f: "+cooldown));
		    meta.getPersistentDataContainer().set(LOLSPELL, PersistentDataType.STRING, internalName.toString());
		}
		else
		{
		    lore.add("");
		    lore.add(PrintUtils.ColorParser("&f&nUsage&r&f: &d&oShift_Right-Click&r&f to register this spell to your account."));
		    lore.add("");
		    lore.add(PrintUtils.ColorParser("&7&oLegends of Lumina: ID_"+getInternalNameAsID()));
		    
		    BookMeta bookMeta = (BookMeta) meta;
		    bookMeta.setAuthor(PrintUtils.assignAuthor(getElementType()));
		    bookMeta.setGeneration(Generation.TATTERED);
		    bookMeta.setTitle(PrintUtils.ColorParser("Legends of Lumina: "+name));
		    
		    // PAGE 1: Title Page
		    StringBuilder page1 = new StringBuilder();
		    page1.append("§l§nLegends of Lumina§r\n\n");
		    page1.append("§0Spell: §0§l").append(name).append("\n\n");
		    page1.append("§8§o").append(PrintUtils.assignRarity(spellTier, true)).append("§r");
		    bookMeta.addPage(page1.toString());
		    
		    // PAGE 2+: Description (with automatic page splitting for larger descriptions)
		    StringBuilder currentPage = new StringBuilder();
		    currentPage.append("§l§nDescription§r\n\n");
		    int pageCharCount = currentPage.length();
		    final int MAX_CHARS_PER_PAGE = 240; // Leave some buffer below the 256 limit

		    for (String line : spellDescription) 
		    {
		        String cleanLine = "§0" + line.replaceAll("§[0-9a-fk-or]", "").replaceAll("&[0-9a-fk-or]", "") + "\n";
		        
		        // Check if adding this line would exceed the page limit
		        if (pageCharCount + cleanLine.length() > MAX_CHARS_PER_PAGE) 
		        {
		            // Add current page and start a new one
		            bookMeta.addPage(currentPage.toString());
		            currentPage = new StringBuilder();
		            currentPage.append("§0"); // Start new page with black text
		            pageCharCount = 0;
		        }
		        
		        currentPage.append(cleanLine);
		        pageCharCount += cleanLine.length();
		    }

		    // Add the final page if it has content
		    if (currentPage.length() > 0) 
		    {
		        bookMeta.addPage(currentPage.toString());
		    }
		    
		    // PAGE 3: Stats
		    StringBuilder page3 = new StringBuilder();
		    page3.append("§l§nSpell Details§r\n\n");
		    String elementType = PrintUtils.assignElementType(eType).replace("&", "§").replace("§f", "§0");
		    page3.append(elementType).append("\n\n");
		    page3.append(PrintUtils.assignSpellType(sType).replace("&", "§").replace("§f", "§0")).append("\n");		    
		    page3.append(PrintUtils.assignCastCondition(castCondition).replace("&", "§").replace("§f", "§0")).append("\n\n");
		    page3.append("§9§lMana Cost§0: ").append(manacost).append("\n");
		    page3.append("§0§lCooldown§0: ").append(cooldown).append(" second(s)");
		    bookMeta.addPage(page3.toString());
		    bookMeta.getPersistentDataContainer().set(LOLSPELLBOOK, PersistentDataType.STRING, internalName.toString());
		    bookMeta.setLore(lore);
		    
		    stack.setItemMeta(bookMeta);
		    
		    return stack;
		}

		meta.setLore(lore);
		stack.setItemMeta(meta);

		return stack;
	}
}
