package com.eol.materia.instances;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaState;
import com.eol.enums.MateriaType;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.Nullable;
import com.ouroboros.utils.PrintUtils;

public abstract class Materia 
{
	public static final NamespacedKey materiaKey = new NamespacedKey(Ouroboros.instance, "materia_key");
	public static final NamespacedKey materiaStateKey = new NamespacedKey(Ouroboros.instance, "materia_state");
	public static final NamespacedKey componentKey = new NamespacedKey(Ouroboros.instance, "materia_component");
	public static final NamespacedKey materiaTypeKey = new NamespacedKey(Ouroboros.instance, "materia_type");
	
	private String name;
	private String internalName;
	private String[] description;
	private MateriaType materiaType;
	private MateriaComponent materiaComponent;
	private Rarity rarity;
	private boolean shouldDrop;
	
	public Materia(String name, String internalName, MateriaType materiaType, MateriaState materiaState, MateriaComponent materiaComponent, 
			Rarity rarity, boolean shouldDrop, @Nullable String...description)
	{
		this.name = name;
		this.internalName = internalName;
		this.materiaType = materiaType;
		this.materiaComponent = materiaComponent;
		this.rarity = rarity;
		this.shouldDrop = shouldDrop;
		this.description = description;
	}

	public String getName() 
	{
		return name;
	}

	public String getInternalName() 
	{
		return internalName;
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
	
	public List<String> getLore() 
	{
		List<String> lore = new ArrayList<>();
		for (String line : description) 
		{
			lore.add(PrintUtils.ColorParser(line));
		}
		return lore;
	}

	public Rarity getRarity() 
	{
		return rarity;
	}

	public boolean shouldDrop() 
	{
		return shouldDrop;
	}
	
	public ItemStack getAsItemStack(MateriaState state)
	{
		Material material = switch (materiaType)
		{
			case CATALYST -> material = Material.NETHER_STAR;
			
			case WOOD -> material = Material.OAK_BUTTON;
			case STONE -> material = Material.STONE_BUTTON;
			case IRON -> material = Material.IRON_NUGGET;
			case COPPER -> material = Material.COPPER_NUGGET;
			case DIAMOND -> material = Material.DIAMOND;
			case GOLD -> material = Material.GOLD_NUGGET;
			case NETHERITE -> material = Material.NETHERITE_SCRAP;
			
			case STRING -> material = Material.STRING;
			case LEATHER -> material = Material.LEATHER;
			
			case CELESTIO -> material = Material.GHAST_TEAR;
			case MORTIO -> material = Material.BONE;
			case INFERNO -> material = Material.BLAZE_POWDER;
			case GLACIO -> material = Material.PRISMARINE_CRYSTALS;
			case GEO -> material = Material.BRICK;
			case AERO -> material = Material.AMETHYST_SHARD;
			case COSMO -> material = Material.ECHO_SHARD;
			case HERESIO -> material = Material.ENDER_EYE;
		};
		
		ItemStack stack = new ItemStack(material, 1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(PrintUtils.ColorParser(name));
		if (state.equals(MateriaState.NORMAL)) meta.setEnchantmentGlintOverride(true);
		
		List<String> lore = new ArrayList<>();
		if (state.equals(MateriaState.UNREFINED))
			lore.add(PrintUtils.assignObfuscatedRarity(rarity));
		else lore.add(PrintUtils.assignRarity(rarity)+"\n");
		lore.add("\n");
		lore.add(PrintUtils.ColorParser("&r&f&nUsage&r&f:"));
		if (state.equals(MateriaState.CATALYST)) 
		{
			lore.add(PrintUtils.ColorParser("&r&fAn &e&oecho&r&f of infinite possibilities&r&f."));
			lore.add("");
			lore.add(PrintUtils.ColorParser("&d&oRight-Click&r&f to initialize &bProtocol&f: &eΣ&f.C.H.O."));
		}
		else if (state.equals(MateriaState.UNREFINED)) 
		{
			lore.add(PrintUtils.ColorParser("&r&fAn object with &3&ounstable waveform&r&f."));
			lore.add("");
			lore.add(PrintUtils.ColorParser("&b&oRefinement&r&f is required to use in &bProtocol&f: &eΣ&f.C.H.O."));
		}
		else if (state.equals(MateriaState.NORMAL)) 
		{
			lore.add(PrintUtils.ColorParser("&r&fA simple harmonic object."));
			lore.add("");
			lore.add(PrintUtils.ColorParser("Can be used as a valid material in &bProtocol&f: &eΣ&f.C.H.O."));
		}
		
		if (description != null)
		{
			lore.add("\n");
			for (String line : description)
			{
				lore.add(PrintUtils.ColorParser("&r&f"+line) + "\n");
			}			
		}
		lore.add("\n");
		lore.add(PrintUtils.ColorParser("&r&7&oΣ.C.H.O. Materia ID: Σ_" + getInternalNameAsID()));			
		
		meta.setLore(lore);
		meta.getPersistentDataContainer().set(materiaKey, PersistentDataType.STRING, internalName);
		meta.getPersistentDataContainer().set(componentKey, PersistentDataType.STRING, materiaComponent.getMateriaComponentType());
		meta.getPersistentDataContainer().set(materiaTypeKey, PersistentDataType.STRING, materiaType.getKey());
		meta.getPersistentDataContainer().set(materiaStateKey, PersistentDataType.STRING, state.getKey());
		meta.setUnbreakable(true);
		
		stack.setItemMeta(meta);
		
		return stack;
	}
}
