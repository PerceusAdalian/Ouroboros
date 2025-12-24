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
	private static int maxSpellSlots;
	private static int INTERNAL_MAX_SPELL_SLOTS = 10;
	private int spellIndex = 0;
	private Rarity rarity;
	private Spell[] spellSlots = new Spell[maxSpellSlots];
	private static NamespacedKey[] slotKeys = new NamespacedKey[maxSpellSlots];
	private boolean deserialized = false;

	public static final NamespacedKey wandKey = new NamespacedKey(Ouroboros.instance, "magicwand");
	public static final NamespacedKey maxWandManaKey = new NamespacedKey(Ouroboros.instance, "maxWandMana");
	public static final NamespacedKey currentWandManaKey = new NamespacedKey(Ouroboros.instance, "currentWandMana");
	public static final NamespacedKey currentMaxSpellSlotsKey = new NamespacedKey(Ouroboros.instance, "currentMaxSpellSlots");
	public static final NamespacedKey absoluteMaxSpellSlotsKey = new NamespacedKey(Ouroboros.instance, "absoluteMaxSpellSlots");
	
	static
	{
		for (int i = 0; i < slotKeys.length; i++)
		{
			slotKeys[i] = new NamespacedKey(Ouroboros.instance, "slot_" + i);
		}
	}
	
	public Wand(String name, Rarity rarity, int maxSlots, int maxMana)
	{
		this.name = name;
		this.rarity = rarity;
		maxSpellSlots = maxSlots;
		this.maxMana = maxMana;
		this.currentMana = maxMana;
	}
	
	public Wand(ItemStack wand)
	{
		if (!isWand(wand)) return;
		
		ItemMeta meta = wand.getItemMeta();
		PersistentDataContainer pdc = meta.getPersistentDataContainer();
		spellIndex = pdc.get(wandKey, PersistentDataType.INTEGER);
		name = meta.getDisplayName();
		
		for (int i = 0; i < maxSpellSlots; i++)
		{
			if (!pdc.has(slotKeys[i], PersistentDataType.STRING)) continue;
			spellSlots[i] = SpellRegistry.spellRegistry.get(pdc.get(slotKeys[i], PersistentDataType.STRING));
		}
		
		currentMana = pdc.getOrDefault(currentWandManaKey, PersistentDataType.INTEGER, 100);
		maxMana = pdc.getOrDefault(maxWandManaKey, PersistentDataType.INTEGER, 100);
		
		deserialized = true;
	}

	public static boolean isWand(ItemStack stack)
	{
		if (stack == null || stack.getType().isAir()) return false;
		
		ItemMeta meta = stack.getItemMeta();
		PersistentDataContainer pdc = meta.getPersistentDataContainer();
		
		return pdc.has(wandKey, PersistentDataType.INTEGER);
	}
	
	public boolean hasSpell(int slot)
	{
		return spellSlots[slot] != null;
	}
	
	public void setSpell(Spell spell, int slot)
	{
		spellSlots[slot] = spell;
	}
	
	public Spell getSpell(int slot)
	{
		return spellSlots[slot];
	}
	
	public int getSpellIndex()
	{
		return spellIndex;
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
		
		return getAsItemStack().getItemMeta().getPersistentDataContainer().get(currentMaxSpellSlotsKey, PersistentDataType.INTEGER);
	}
	
	public void setNewMaxSpellSlots(int value)
	{
		ItemStack stack = getAsItemStack();
		ItemMeta meta = stack.getItemMeta();
		PersistentDataContainer pdc = meta.getPersistentDataContainer();
		
		if (value > pdc.get(absoluteMaxSpellSlotsKey, PersistentDataType.INTEGER)) return;
		pdc.set(currentMaxSpellSlotsKey, PersistentDataType.INTEGER, value);
		stack.setItemMeta(meta);
		setMaxMana();
	}
	
	public int getAbsoluteMaxSpellSlots()
	{
		return INTERNAL_MAX_SPELL_SLOTS;
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
	
	public ItemStack getAsItemStack()
	{
		ItemStack stack = new ItemStack(Material.STICK, 1);
		ItemMeta meta = stack.getItemMeta();
		PersistentDataContainer pdc = meta.getPersistentDataContainer();
		
		meta.setDisplayName(name);
		meta.setEnchantmentGlintOverride(true);
		
		pdc.set(wandKey, PersistentDataType.INTEGER, spellIndex);
		pdc.set(currentWandManaKey, PersistentDataType.INTEGER, currentMana);
		pdc.set(maxWandManaKey, PersistentDataType.INTEGER, maxMana);
		pdc.set(currentMaxSpellSlotsKey, PersistentDataType.INTEGER, maxSpellSlots);
		pdc.set(absoluteMaxSpellSlotsKey, PersistentDataType.INTEGER, INTERNAL_MAX_SPELL_SLOTS);
		
		if (!deserialized) return stack;
		
		List<String> existingLore = meta.getLore();
		if (existingLore == null) existingLore = new ArrayList<>();
		existingLore.add("");
		existingLore.add(PrintUtils.assignRarity(rarity));
		existingLore.add(PrintUtils.ColorParser("&r&9Mana Capacity&f: " )+ currentMana + PrintUtils.ColorParser("&r&f/")+ maxMana);
		existingLore.add("");
		String spellSlotIcons = "";
		for (int i = 0; i < maxSpellSlots; i++)
		{
			char ch = '7';
			for (Spell spell : spellSlots) ch = PrintUtils.getElementTypeColor(spell.getElementType());
			spellSlotIcons += ("&"+ch+(getSpell(i)==null?"●":"○"));
		}		
		existingLore.add("Spell Slots: " + PrintUtils.ColorParser(spellSlotIcons));
		existingLore.add("");
		
		for (int i = 0; i < maxSpellSlots; i++)
		{
			if (spellSlots[i] == null) continue;
			
			Spell spell = spellSlots[i];
			
			pdc.set(slotKeys[i], PersistentDataType.STRING, spell.getInternalName());
			
			if (spellIndex == i) meta.setLore(spell.getLore());
		}
		
		meta.setLore(existingLore);
		stack.setItemMeta(meta);
		
		return stack;
	}
	
}
