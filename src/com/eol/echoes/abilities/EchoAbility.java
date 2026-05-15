package com.eol.echoes.abilities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.PrintUtils;

public abstract class EchoAbility 
{
	private final String displayName;
	private final String internalName;
	private final Material icon;
	private final String[] description;
	private final AbilityType aType;
	private final StatType statRequirement;
	private final ElementType eType;
	private final CastConditions castCondition;
	private final EchoForm echoForm;
	private final int levelRequirement;
	private final int APCost;
	private final int durabilityCost;
	
	public static final NamespacedKey OBSABILITY = new NamespacedKey(Ouroboros.instance, "obsability");
	
	public EchoAbility(String displayName, String internalName, Material icon, StatType statRequirement, int levelRequirement, int APCost, int durabilityCost, 
			AbilityType aType, ElementType eType, CastConditions castCondition, EchoForm echoForm, String...description) 
	{
		this.displayName = displayName;
		this.internalName = internalName;
		this.APCost = APCost;
		this.durabilityCost = durabilityCost;
		this.icon = icon;
		this.statRequirement = statRequirement;
		this.levelRequirement = levelRequirement;
		this.aType = aType;
		this.eType = eType;
		this.castCondition = castCondition;
		this.echoForm = echoForm;
		this.description = description;
	}
	
	public String getDisplayName() 
	{
		return displayName;
	}
	
	public String getInternalName() 
	{
		return internalName;
	}
	
	public Material getIcon() 
	{
		return icon;
	}
	
	public String[] getDescription() 
	{
		return description;
	}
	
	public StatType getStatRequirement() 
	{
		
		return statRequirement;
	}
	
	public int getLevelRequirement() 
	{
		return levelRequirement;
	}
	
	public int getAPCost() 
	{
		return APCost;
	}
	
	public int getDurabilityCost()
	{
		return durabilityCost;
	}

	public EchoAbility getInstance() 
	{
		return this;
	}
	
	public static EchoAbility get(String internalName)
	{
		return AbilityRegistry.abilityRegistry.getOrDefault(internalName, null);
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
		return castCondition;
	}
	public EchoForm getEchoForm() 
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
	public abstract int getFinalDurabilityCost();
	
	public ItemStack toIcon(Player p) 
	{
		ItemStack stack = new ItemStack(icon, 1);
		ItemMeta meta = stack.getItemMeta();
		List<String> lore = new ArrayList<>();
		
		meta.setDisplayName(PrintUtils.ColorParser("&r&f"+displayName));
		if (!PlayerData.getPlayer(p.getUniqueId()).getAbility(getInstance()).isRegistered()) 
		{
			stack.setType(Material.NETHER_STAR);
		    meta = stack.getItemMeta();
			meta.setDisplayName(PrintUtils.ColorParser("&r&7LOCKED: &f"+displayName));
			
			char color = switch(statRequirement) 
			{
				case WOODCUTTING,TRAVEL,CRAFTING,ALCHEMY,
					 MINING,FISHING,FARMING,ENCHANTING -> color = 'a';
				case MELEE,RANGED,MAGIC -> color = 'c';
				default -> color = '7';
			};
			
			lore.add(PrintUtils.assignAbilityType(aType));
			lore.add(PrintUtils.ColorParser("&r&f- &oDescription&r&f:"));
			if (durabilityCost > 0) lore.add(PrintUtils.assignDurabilityCost(durabilityCost));
			for (String line : description)
			{
				lore.add(PrintUtils.ColorParser(line));	
			}
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
			lore.add(PrintUtils.assignEchoForm(echoForm));
			lore.add("");
			lore.add(PrintUtils.ColorParser("&r&f- &oDescription&r&f:"));
			if (durabilityCost > 0) lore.add(PrintUtils.assignDurabilityCost(durabilityCost));
			for (String line : description) 
			{
				lore.add(PrintUtils.ColorParser(line));
			}
		}
		
		meta.setLore(lore);
		meta.getPersistentDataContainer().set(OBSABILITY, PersistentDataType.STRING, internalName.toString());
		stack.setItemMeta(meta);
		
		return stack;
	}
	
}
