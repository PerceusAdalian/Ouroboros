package com.eol.materia.instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaState;
import com.eol.enums.MateriaType;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.Rarity;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.Nullable;
import com.ouroboros.utils.PrintUtils;

public class Materia 
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
	
	/**
	 * @param name
	 * @param internalName
	 * @param materiaType
	 * @param materiaComponent
	 * @param rarity
	 * @param shouldDrop
	 * @param description
	 */
	public Materia(String name, String internalName, MateriaType materiaType, MateriaComponent materiaComponent, 
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
	
	public Rarity getRarity()           
	{ 
		return rarity; 
	}
	
	public MateriaComponent getMateriaComponent() 
	{ 
		return materiaComponent; 
	}
	
	public MateriaType getMateriaType() 
	{ 
		return materiaType; 
	}
	
	public boolean shouldDrop()         
	{ 
		return shouldDrop; 
	}

	public String getInternalNameAsID()
	{
		int hash = internalName.hashCode() & 0xFFFF;
		return Integer.toHexString(hash).toUpperCase();
	}

	public List<String> getLore()
	{
		List<String> lore = new ArrayList<>();
		for (String line : description)
			lore.add(PrintUtils.ColorParser(line));
		return lore;
	}

	public static final Map<String, Materia> materia_registry = new HashMap<>();
	
	public static void register(Materia materia)
	{
		materia_registry.put(materia.getInternalName(), materia);
	}
	
	public static Materia get(String internalName)
	{
		return materia_registry.get(internalName);
	}
	
	public ItemStack getAsItemStack()
	{
		return getAsItemStack(MateriaState.NORMAL);
	}
	
	public ItemStack getAsItemStack(MateriaState state) 
	{
		ItemStack stack = new ItemStack(materiaType.getMaterial(), 1);
		ItemMeta meta = stack.getItemMeta();

		meta.setDisplayName(PrintUtils.ColorParser(name + (state != MateriaState.ELEMENT_CORE ? (" " + PrintUtils.getRarityAsNumeralValue(rarity)) : "")));
		meta.setEnchantmentGlintOverride(state == MateriaState.NORMAL);
		meta.setLore(buildLore(materiaType, state, materiaComponent));
		applyPersistentData(meta, state);
		stack.setItemMeta(meta);

		return stack;
	}
	
	/** Produces a generic unrefined item for a given type. Stackable. */
	public static ItemStack generateUnrefinedMateria(MateriaType type)
	{
		ItemStack stack = new ItemStack(type.getMaterial(), 1);
		ItemMeta meta = stack.getItemMeta();
		
		meta.setDisplayName(PrintUtils.ColorParser(type.getLabel()));
		meta.setEnchantmentGlintOverride(false);
		meta.setLore(List.of(
				PrintUtils.ColorParser("&7&oUnrefined"),"",
				PrintUtils.ColorParser("&r&fAn object with &3&ounstable waveform&r&f."),
				PrintUtils.ColorParser("&bRefinement&r&f is required to use in &bProtocol&f: &eΣ&f.C.H.O."),"",
				PrintUtils.assignMateriaType(type) + PrintUtils.ColorParser("&r&7 | ") + PrintUtils.assignMateriaState(MateriaState.UNREFINED)));

		var pdc = meta.getPersistentDataContainer();
		pdc.set(materiaTypeKey,  PersistentDataType.STRING, type.getKey());
		pdc.set(materiaStateKey, PersistentDataType.STRING, MateriaState.UNREFINED.getKey());
		
		stack.setItemMeta(meta);
		return stack;
	}

	private List<String> buildLore(MateriaType materiaType, MateriaState state, MateriaComponent materiaComponent) 
	{
	    List<String> lore = new ArrayList<>();
	    
		lore.add(state == MateriaState.UNREFINED
	        ? PrintUtils.assignObfuscatedRarity()
	        : PrintUtils.assignRarity(rarity));
	    lore.add("");
	    lore.addAll(getStateLore(state));
	    
	    boolean isCatalyst = materiaComponent == MateriaComponent.CATALYST;
	    boolean isElementCore = materiaComponent == MateriaComponent.ELEMENT_CORE;
	    if (state != MateriaState.UNREFINED)
	    {	    	
	    	if (description != null) 
	    	{
	    		lore.add("");
	    		for (String line : description) 
	    		{
	    			lore.add(PrintUtils.ColorParser("&r&f" + line));
	    		}
	    	}
	    	lore.add("");
	    	lore.add(""+(isCatalyst ? "" : PrintUtils.assignMateriaType(materiaType) + PrintUtils.ColorParser("&r&7 | ")) +
	    			(isCatalyst || isElementCore ? "" : PrintUtils.assignMateriaState(state) + PrintUtils.ColorParser("&r&7 | ")) +
	    			PrintUtils.assignMateriaComponent(materiaComponent));
	    	lore.add(PrintUtils.ColorParser("&r&7Materia ID: Σ_"+getInternalNameAsID()));
	    }
	    
	    return lore;
	}
	
	private List<String> getStateLore(MateriaState state) 
	{
		return switch (state)
		{
			case CATALYST -> List.of(
					PrintUtils.ColorParser("&r&fA catalyst of infinite possibilities&r&f."),
					PrintUtils.ColorParser("&d&oRight-Click&r&f to initialize &bProtocol&f: &eΣ&f.C.H.O."));
			case NORMAL -> List.of(
					PrintUtils.ColorParser("&r&fA coherent, harmonic object. It hums with potential."),
					PrintUtils.ColorParser("&r&fCan be used as a valid &bcomponent&f in &bProtocol&f: &eΣ&f.C.H.O."));
			case ELEMENT_CORE-> List.of();
			default -> List.of();
		};
	}
	
	private void applyPersistentData(ItemMeta meta, MateriaState state) 
	{
		var pdc = meta.getPersistentDataContainer();
		pdc.set(materiaKey,      PersistentDataType.STRING, internalName);
		pdc.set(componentKey,    PersistentDataType.STRING, materiaComponent.getKey());
		pdc.set(materiaTypeKey,  PersistentDataType.STRING, materiaType.getKey());
		pdc.set(materiaStateKey, PersistentDataType.STRING, state.getKey());
	}
	
	public static boolean isUnrefined(ItemStack stack)
	{
		if (stack == null || stack.getType().isAir()) return false;
		ItemMeta meta = stack.getItemMeta();
		var pdc = meta.getPersistentDataContainer();
		if (!pdc.has(materiaStateKey, PersistentDataType.STRING)) return false;
		return MateriaState.fromString(pdc.get(materiaStateKey, PersistentDataType.STRING)) == MateriaState.UNREFINED;
	}
    
	public static Materia refine(MateriaType type, @Nullable MateriaComponent component, Rarity rarity)
	{
	    List<Materia> candidates = materia_registry.values().stream()
	            .filter(m -> m.materiaType == type
	                    && (component == null || m.materiaComponent == component)
	                    && m.rarity == rarity)
	            .collect(Collectors.toList());

	    if (candidates.isEmpty()) return null;

	    return candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
	}

	/* Converts relevant items from MateriaType respective to each item in inventory to the unrefined counterpart */
	public static void convertItemsTask(Plugin plugin)
	{
		Bukkit.getScheduler().runTaskTimer(plugin, () ->
		{
		    for (Player player : Bukkit.getOnlinePlayers())
		    {
		        ItemStack[] contents = player.getInventory().getContents();
		        boolean changed = false;

		        for (int i = 0; i < contents.length; i++)
		        {
		            ItemStack stack = contents[i];
		            if (stack == null || stack.getType().isAir()) continue;
		            if (stack.getItemMeta().getPersistentDataContainer().has(materiaTypeKey, PersistentDataType.STRING)) continue;
		            if (stack.getItemMeta().getPersistentDataContainer().has(AbstractObsObject.obsObject, PersistentDataType.STRING)) continue;
		            
		            MateriaType type = MateriaType.fromMaterial(stack.getType());
		            if (type == null) continue;
		            
		            ItemStack unrefined = generateUnrefinedMateria(type);
		            unrefined.setAmount(stack.getAmount());
		            contents[i] = unrefined;
		            changed = true;
		        }

		        if (changed) player.getInventory().setContents(contents);
		    }
		}, 0L, 20L);
	}
	
}
