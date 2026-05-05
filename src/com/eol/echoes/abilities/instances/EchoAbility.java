package com.eol.echoes.abilities.instances;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.eol.echoes.abilities.AbilityRegistry;
import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.PrintUtils;

public abstract class EchoAbility 
{
	private final File file;
	private final YamlConfiguration config;
	private final String displayName;
	private final String internalName;
	private final Material icon;
	private final String[] description;
	private final AbilityType aType;
	private final StatType statRequirement;
	private final ElementType eType;
	private final CastConditions condition;
	private final EchoForm echoForm;
	private final int levelRequirement;
	private final int APCost;
	
	public static final NamespacedKey OBSABILITY = new NamespacedKey(Ouroboros.instance, "obsability");
	
	public EchoAbility(String displayName, String internalName, Material icon, StatType statRequirement, int levelRequirement, int APCost, 
			AbilityType aType, ElementType eType, CastConditions condition, EchoForm echoForm, String...description) 
	{
		this.displayName = displayName;
		this.internalName = internalName;
		this.APCost = APCost;
		this.icon = icon;
		this.statRequirement = statRequirement;
		this.levelRequirement = levelRequirement;
		this.aType = aType;
		this.eType = eType;
		this.condition = condition;
		this.echoForm = echoForm;
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
		config.set("ap_cost", APCost);
		config.set("stat_requirement", statRequirement.getKey());
		config.set("level_requirement", levelRequirement);
		config.set("description", description);
		config.set("ability_type", aType.getKey());
		if (eType != null) config.set("damage_type", eType.getType());
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
	
	public EchoAbility getInstance() 
	{
		return this;
	}
	
	public NamespacedKey getKey() 
	{
		return OBSABILITY;
	}
	
	public AbilityType getAbilityType() 
	{
		return aType;
	}
	
	public ElementType getElementType() 
	{
		return eType;
	}
	
	public CastConditions getCastCondition() 
	{
		return condition;
	}
	public EchoForm getAbilityCategory() 
	{
		return echoForm;
	}
	public static EchoAbility fromInternalName(String string)
	{
		if (AbilityRegistry.abilityRegistry.containsKey(string))
		{
			EchoAbility ability = AbilityRegistry.abilityRegistry.get(string);
			return ability;
		}
		return null;
	}
	
	public abstract int cast(PlayerInteractEvent e);
	
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
				case WOODCUTTING,TRAVEL,CRAFTING,ALCHEMY,
					 MINING,FISHING,FARMING,ENCHANTING -> color = 'a';
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
				lore.add(PrintUtils.ColorParser("&r&fAP Cost to Register: &6"+(APCost == 0 ? "FREE" : String.valueOf(APCost))+"&7/"+PlayerData.getPlayer(p.getUniqueId()).getAbilityPoints()));				
			}
		}
		else 
		{
			lore.add(PrintUtils.assignAbilityType(aType));
			if (eType != null) lore.add(PrintUtils.assignElementType(eType));
			if (condition != CastConditions.PASSIVE) lore.add(PrintUtils.assignCastCondition(condition));
			lore.add(PrintUtils.assignEchoForm(echoForm));
			lore.add("");
			boolean activatedAbility = PlayerData.getPlayer(p.getUniqueId()).getAbility(getInstance()).isActive();
			lore.add(PrintUtils.ColorParser("&b> &nActivated&r&f: &l" + (activatedAbility ? "True" : "False")));
			for (String line : description) lore.add(PrintUtils.ColorParser("&r&f"+line) + "\n");
		}
		
		meta.setLore(lore);
		meta.getPersistentDataContainer().set(OBSABILITY, PersistentDataType.STRING, internalName.toString());
		stack.setItemMeta(meta);
		
		return stack;
	}
	
}
