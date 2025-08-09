package com.ouroboros.abilities.instances;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.ouroboros.Ouroboros;
import com.ouroboros.abilities.AbilityRegistry;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.AbilityDamageType;
import com.ouroboros.enums.ObsAbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.PrintUtils;

public abstract class AbstractOBSAbility 
{
	private final File file;
	private final YamlConfiguration config;
	private final String displayName;
	private final String internalName;
	private final Material icon;
	private final String[] description;
	private final ObsAbilityType aType;
	private final StatType statRequirement;
	private final AbilityDamageType dType;
	private final int levelRequirement;
	private final int APCOST;
	public static final NamespacedKey OBSABILITY = new NamespacedKey(Ouroboros.instance, "obsability");
	
	public AbstractOBSAbility(String displayName, String internalName, Material icon, StatType statRequirement, int levelRequirement, int APCOST, 
			ObsAbilityType aType, AbilityDamageType dType, String...description) 
	{
		this.displayName = displayName;
		this.internalName = internalName;
		this.APCOST = APCOST;
		this.icon = icon;
		this.statRequirement = statRequirement;
		this.levelRequirement = levelRequirement;
		this.aType = aType;
		this.dType = dType;
		this.description = description;
		this.file = new File(getDataFolder(), "abilities/"+internalName+".yml");
		this.config = YamlConfiguration.loadConfiguration(file);
		
		if (!file.exists()) 
		{
			setInfo();
			save();
		}
	}
	
	public AbstractOBSAbility(String displayName, String internalName, Material icon, StatType statRequirement, int levelRequirement, int APCOST,
			ObsAbilityType aType, String...description)
	{
		this.displayName = displayName;
		this.internalName = internalName;
		this.APCOST = APCOST;
		this.icon = icon;
		this.statRequirement = statRequirement;
		this.levelRequirement = levelRequirement;
		this.aType = aType;
		this.dType = null;
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
		config.set("ap_cost", APCOST);
		config.set("stat_requirement", statRequirement.getKey());
		config.set("level_requirement", levelRequirement);
		config.set("description", description);
		config.set("ability_type", aType.getKey());
		if (dType != null) config.set("damage_type", dType.getType());
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
		return Material.getMaterial(config.getString("icon_material"));
	}
	
	public String[] getDescription() 
	{
		return description;
	}
	
	public StatType getStatRequirement() 
	{
		
		return StatType.fromString(config.getString("stat_requirement"));
	}
	
	public int getLevelRequirement() 
	{
		return config.getInt("level_requirement");
	}
	
	public int getAPCost() 
	{
		return config.getInt("ap_cost");
	}
	
	public AbstractOBSAbility getInstance() 
	{
		return this;
	}
	
	public NamespacedKey getKey() 
	{
		return OBSABILITY;
	}
	
	public ObsAbilityType getAbilityType() 
	{
		return aType;
	}
	
	public AbilityDamageType getDamageType() 
	{
		return dType;
	}
	
	public static AbstractOBSAbility fromInternalName(String string)
	{
		if (AbilityRegistry.abilityRegistry.containsKey(string))
		{
			AbstractOBSAbility ability = AbilityRegistry.abilityRegistry.get(string);
			return ability;
		}
		return null;
	}
	
	public abstract boolean cast(Event e);
	
	// For gui display
	public ItemStack toIcon(Player p) 
	{
		ItemStack stack = new ItemStack(icon, 1);
		ItemMeta meta = stack.getItemMeta();
		List<String> lore = new ArrayList<>();
		
		meta.setDisplayName(PrintUtils.ColorParser("&r&f"+displayName));
		lore.add("\n");
		
		if (!PlayerData.getPlayer(p.getUniqueId()).getAbility(getInstance()).isRegistered()) 
		{
			stack.setType(Material.NETHER_STAR);
			meta.setDisplayName(PrintUtils.ColorParser("&r&7LOCKED: &f"+displayName));
			
			char color = switch(statRequirement) 
			{
				case WOODCUTTING,TRAVEL,CRAFTING,ALCHEMY,MINING,FISHING,FARMING,ENCHANTING -> color = 'a';
				case MELEE,RANGED,MAGIC -> color = 'c';
				default -> color = '7';
			};
			
			lore.add(PrintUtils.assignAbilityType(aType));
			for (String line : description) lore.add(PrintUtils.ColorParser("&r&f"+line) + "\n");	
			lore.add("");
			if (PlayerData.getPlayer(p.getUniqueId()).getStat(statRequirement, true) < levelRequirement) 
			{
				lore.add(PrintUtils.ColorParser("&r&c&lStat Required&r&f: &b&l"+statRequirement.getFancyKey()+" &r&7@ &flvl&"+color+levelRequirement));
				lore.add(PrintUtils.ColorParser("&r&fYour &b&l"+statRequirement.getFancyKey()+"&r&f lvl: &"+color+PlayerData.getPlayer(p.getUniqueId()).getStat(statRequirement, true)));
			}
			else if (PlayerData.getPlayer(p.getUniqueId()).getStat(statRequirement, true) >= levelRequirement) 
			{
				lore.add(PrintUtils.ColorParser("&r&fAP Cost to Register: &6"+(APCOST == 0 ? "FREE" : String.valueOf(APCOST))+"&7/"+PlayerData.getPlayer(p.getUniqueId()).getAbilityPoints()));				
			}
		}
		else 
		{
			lore.add(PrintUtils.assignAbilityType(aType));
			if (dType != null) lore.add(PrintUtils.assignAbilityDamageType(dType));
			lore.add("");
			boolean activatedAbility = PlayerData.getPlayer(p.getUniqueId()).getAbility(getInstance()).isActive();
			lore.add(PrintUtils.ColorParser("&b> &nActivated&r&f: &l" + (activatedAbility ? "True" : "false")));
			for (String line : description) lore.add(PrintUtils.ColorParser("&r&f"+line) + "\n");
		}
		
		meta.setLore(lore);
		meta.getPersistentDataContainer().set(OBSABILITY, PersistentDataType.STRING, internalName.toString());
		stack.setItemMeta(meta);
		
		return stack;
	}
	
}
