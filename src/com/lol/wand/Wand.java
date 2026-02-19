package com.lol.wand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.lol.spells.instances.Spell;
import com.lol.spells.instances.SpellRegistry;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.PrintUtils;

public class Wand 
{
	private String name;
    private int currentMana;
    private int maxMana;
    private static final int INTERNAL_MAX_WAND_MANA = 5000;
    private int maxSpellSlots;
    private static final int INTERNAL_MAX_SPELL_SLOTS = 10;
    private int spellIndex = 0;
    private Rarity rarity;
    private Spell[] spellSlots;
    private NamespacedKey[] slotKeys;
    private boolean deserialized = false;

    public static final NamespacedKey wandKey = new NamespacedKey(Ouroboros.instance, "magicwand");
    public NamespacedKey maxWandManaKey = new NamespacedKey(Ouroboros.instance, "maxWandMana");
    public NamespacedKey currentWandManaKey = new NamespacedKey(Ouroboros.instance, "currentWandMana");
    public NamespacedKey currentMaxSpellSlotsKey = new NamespacedKey(Ouroboros.instance, "currentMaxSpellSlots");
    public static final NamespacedKey absoluteMaxSpellSlotsKey = new NamespacedKey(Ouroboros.instance, "absoluteMaxSpellSlots");
    public static final NamespacedKey rarityKey = new NamespacedKey(Ouroboros.instance, "wandRarity"); // ADD THIS
    
    public Wand(String name, Rarity rarity, int maxSlots, int maxMana)
    {
        this.name = name;
        this.rarity = rarity;
        this.maxSpellSlots = maxSlots;
        this.maxMana = maxMana;
        this.currentMana = maxMana;
        this.spellSlots = new Spell[maxSlots];
        this.slotKeys = new NamespacedKey[maxSlots];
        this.deserialized = true;
        
        // Initialize slot keys
        for (int i = 0; i < maxSlots; i++)
        {
            slotKeys[i] = new NamespacedKey(Ouroboros.instance, "slot_" + i);
        }
    }
	
    public Wand(ItemStack wand)
    {
        if (!isWand(wand)) return;
        
        ItemMeta meta = wand.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        
        spellIndex = pdc.get(wandKey, PersistentDataType.INTEGER);
        name = meta.getDisplayName();
        currentMana = pdc.getOrDefault(currentWandManaKey, PersistentDataType.INTEGER, 100);
        maxMana = pdc.getOrDefault(maxWandManaKey, PersistentDataType.INTEGER, 100);
        maxSpellSlots = pdc.getOrDefault(currentMaxSpellSlotsKey, PersistentDataType.INTEGER, 1);
        
        String rarityName = pdc.get(rarityKey, PersistentDataType.STRING);
        this.rarity = rarityName != null ? Rarity.valueOf(rarityName) : Rarity.ONE;
        
        // Initialize arrays
        this.spellSlots = new Spell[maxSpellSlots];
        this.slotKeys = new NamespacedKey[maxSpellSlots];
        
        // Initialize slot keys
        for (int i = 0; i < maxSpellSlots; i++)
        {
            slotKeys[i] = new NamespacedKey(Ouroboros.instance, "slot_" + i);
        }
        
        // Load spells
        for (int i = 0; i < maxSpellSlots; i++)
        {
            if (!pdc.has(slotKeys[i], PersistentDataType.STRING)) continue;
            spellSlots[i] = SpellRegistry.spellRegistry.get(pdc.get(slotKeys[i], PersistentDataType.STRING));
        }
        
        deserialized = true;
    }

	public static boolean isWand(ItemStack stack)
	{
		if (stack == null || stack.getType().isAir()) return false;
		
		ItemMeta meta = stack.getItemMeta();
		PersistentDataContainer pdc = meta.getPersistentDataContainer();
		
		return pdc.has(wandKey, PersistentDataType.INTEGER);
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean hasSpell(int slot)
	{
		return spellSlots[slot] != null;
	}
	
	public void setSpell(Spell spell, int slot)
	{
		spellSlots[slot] = spell;
		spellIndex = slot;
	}
	
	public Spell getSpell(int slot)
	{
		return spellSlots[slot];
	}
	
	public int getSpellIndex()
	{
		return spellIndex;
	}
	
	public void setSpellIndex(int slot)
	{
		spellIndex = slot;
	}
	
	public void removeSpell(int slot)
	{
		spellSlots[slot] = null;
	}
	
	public void clearWand()
	{
		spellSlots = null;
	}
	
	public void subtractMana(int value)
	{
		currentMana -= value;
		if (currentMana < 0) currentMana = 0;
	}
	
	public void addMana(int value)
	{
		currentMana += value;
		if (currentMana > maxMana || currentMana < 0) currentMana = maxMana;
	}
	
	public int getMaxMana()
	{
		return maxMana;
	}
	
	public int getCurrentMana()
	{
		return currentMana;
	}
	
	public void setNewMaxMana(int value)
	{
		maxMana = value;
	}
	
	public void setMaxMana()
	{
		currentMana = maxMana;
	}
	
	public Rarity getRarity()
	{
		return rarity;
	}
	
	public int getCurrentMaxSpellSlots()
	{
	    return maxSpellSlots;
	}
	
	public void setNewMaxSpellSlots(int value)
	{
	    this.maxSpellSlots = value;
	    
	    // Resize the arrays if needed
	    if (value > spellSlots.length)
	    {
	        Spell[] newSpellSlots = new Spell[value];
	        NamespacedKey[] newSlotKeys = new NamespacedKey[value];
	        
	        // Copy existing spells
	        System.arraycopy(spellSlots, 0, newSpellSlots, 0, spellSlots.length);
	        System.arraycopy(slotKeys, 0, newSlotKeys, 0, slotKeys.length);
	        
	        // Initialize new slot keys
	        for (int i = slotKeys.length; i < value; i++)
	        {
	            newSlotKeys[i] = new NamespacedKey(Ouroboros.instance, "slot_" + i);
	        }
	        
	        this.spellSlots = newSpellSlots;
	        this.slotKeys = newSlotKeys;
	    }
	}
	
	public int getAbsoluteMaxSpellSlots()
	{
		return INTERNAL_MAX_SPELL_SLOTS;
	}
	
	public int getAbsoluteMaxMana()
	{
		return INTERNAL_MAX_WAND_MANA;
	}
	
	public Spell getNextSpell()
	{
		int nextSlot = spellIndex;
		int count = 0;
		
		while (count < getCurrentMaxSpellSlots())
		{
			nextSlot++;
			count++;
			
			if(nextSlot > getCurrentMaxSpellSlots() - 1) nextSlot = 0;
			
			Spell spell = spellSlots[nextSlot];
			
			if(spell == null) continue;
			
			return spell;
		}
		return null;
	}
	
	public int getNextSpellSlot()
	{
		int nextSlot = spellIndex;
		int count = 0;
		while (count < getCurrentMaxSpellSlots())
		{
			nextSlot++;
			count++;
			
			if (nextSlot > getCurrentMaxSpellSlots() - 1) nextSlot = 0;
			
			Spell spell = spellSlots[nextSlot];
			
			if (spell == null) continue;
			
			return nextSlot;
		}
		return 0;
	}
	
	public void rotateSpells()
	{
		int nextSlot = spellIndex;
		int count = 0;
		
		while (count < getCurrentMaxSpellSlots())
		{
			nextSlot++;
			count++;
			
			if (nextSlot > getCurrentMaxSpellSlots() - 1) nextSlot = 0;

			Spell spell = spellSlots[nextSlot];
			
			if (spell == null) continue;
			
			spellIndex = nextSlot;
			
			return;
		}
		return;
	}
	
	public int getRechargeCost() 
	{
	    int missingMana = maxMana - currentMana;
	    if (missingMana == 0) return 0;
	    
	    double percentMissing = (double) missingMana / maxMana;
	    int rarityTier = rarity.getRarity(); // 1-7
	    int maxCostForTier = rarityTier * 5; // 5, 10, 15, 20, 25, 30, 35
	    
	    // Scale cost based on percentage missing, up to the tier's max
	    int cost = (int) Math.ceil(percentMissing * maxCostForTier);
	    
	    return Math.max(1, cost); // Minimum 1 luminite for any recharge
	}
	
	public ItemStack getAsItemStack()
	{
	    ItemStack stack = new ItemStack(Material.STICK, 1);
	    ItemMeta meta = stack.getItemMeta();
	    PersistentDataContainer pdc = meta.getPersistentDataContainer();
	    
	    meta.setDisplayName(PrintUtils.ColorParser(name));
	    meta.setEnchantmentGlintOverride(true);
	    
	    pdc.set(wandKey, PersistentDataType.INTEGER, spellIndex);
	    pdc.set(currentWandManaKey, PersistentDataType.INTEGER, currentMana);
	    pdc.set(maxWandManaKey, PersistentDataType.INTEGER, maxMana);
	    pdc.set(currentMaxSpellSlotsKey, PersistentDataType.INTEGER, maxSpellSlots);
	    pdc.set(absoluteMaxSpellSlotsKey, PersistentDataType.INTEGER, INTERNAL_MAX_SPELL_SLOTS);
	    pdc.set(rarityKey, PersistentDataType.STRING, rarity.name()); // Don't forget to save rarity!
	    
	    
	    if (!deserialized) 
	    {
	    	stack.setItemMeta(meta);
	    	return stack;
	    }
	    
	    // Build the wand's main lore
	    List<String> existingLore = new ArrayList<>();
	    existingLore.add("");
	    existingLore.add(PrintUtils.assignRarity(rarity));
	    existingLore.add("");
	    
	    // Build spell slot icons with proper null checks
	    String spellSlotIcons = "";
	    for (int i = 0; i < maxSpellSlots; i++)
	    {
	        Spell spell = getSpell(i);
	        if (spell == null) 
	        {
	            spellSlotIcons += "&7○"; // Empty grey circle for empty slot
	        } 
	        else 
	        {
	            char color = PrintUtils.getElementTypeColor(spell.getElementType());
	            spellSlotIcons += "&" + color + "●"; // Color coded full circle for filled slot
	        }
	    }
	    existingLore.add(PrintUtils.ColorParser("&r&e&lSpell Slots&r&f: " + spellSlotIcons));
	    existingLore.add("");
	    
	    // Save spell data to NBT
	    for (int i = 0; i < maxSpellSlots; i++)
	    {
	        if (spellSlots[i] == null) continue;
	        Spell spell = spellSlots[i];
	        pdc.set(slotKeys[i], PersistentDataType.STRING, spell.getInternalName());
	    }
	    
	    // Add the current spell's lore if there is one
	    if (spellSlots[spellIndex] != null) 
	    {
	        Spell currentSpell = spellSlots[spellIndex];
	        existingLore.add(PrintUtils.ColorParser("&b&lEquipped Spell&r&f: "+currentSpell.getName()+
	        		"&r&7 {"+PrintUtils.rarityToString(currentSpell.getRarity())+"&r&7}"));
	        existingLore.add(PrintUtils.ColorParser("&f&nDescription&r&f:"));
	        existingLore.addAll(currentSpell.getLore()); // Add all spell lore lines
	        existingLore.add("");
	        existingLore.add(PrintUtils.ColorParser("&r&fOther Details:"));
	        if (currentSpell.isPvpCombatible()) existingLore.add(PrintUtils.assignPVPCompatible());
	        existingLore.add(PrintUtils.assignElementType(currentSpell.getElementType()));
	        existingLore.add(PrintUtils.assignSpellType(currentSpell.getSpellType()));
	        existingLore.add(PrintUtils.assignCastCondition(currentSpell.getCastCondition()));
	        existingLore.add(PrintUtils.ColorParser("&r&b&lMana Cost&r&f: "+currentSpell.getManacost()+" &7| &r&f&lCooldown&r&f: "+currentSpell.getCooldown()+" second(s)"));
	    }
	    
	    existingLore.add("");
	    existingLore.add(PrintUtils.ColorParser("&r&9Mana Capacity&f: ") + currentMana + PrintUtils.ColorParser("&r&f/") + maxMana);
	    existingLore.add(PrintUtils.ColorParser("&r&d&oLeft-Click&r&f to cycle spells &7| &d&oShift_Left-Click&r&f to remove spells"));
	    // Finally, set the combined lore at the end
	    meta.setLore(existingLore);
	    stack.setItemMeta(meta);
	    
	    return stack;
	}
	
}
