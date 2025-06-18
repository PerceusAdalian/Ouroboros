package com.ouroboros.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ouroboros.utils.PrintUtils;

public class GuiButton 
{
	private final ItemStack item;
    private final ItemMeta meta;
    private final List<String> lore = new ArrayList<>();
    
    public GuiButton(Material material) 
    {
        item = new ItemStack(material);
        meta = item.getItemMeta();
    }
    
    public GuiButton(ItemStack obj) 
    {
		this.item = new ItemStack(obj);
		this.meta = obj.getItemMeta();
    }
    
    public static GuiButton button(Material material) 
    {
        return new GuiButton(material);
    }
    
    public static GuiButton button(ItemStack stack) 
    {
    	return new GuiButton(stack);
    }
    
    public GuiButton setName(String name) 
    {
        meta.setDisplayName(PrintUtils.ColorParser(name));
        return this;
    }

    public GuiButton setLore(String... lines) 
    {
        for (String line : lines) 
        {
            lore.add(PrintUtils.ColorParser("&r&f" + line));
        }
        return this;
    }
    
    public void place(AbstractOBSGui gui, int slot, Consumer<InventoryClickEvent> action) 
    {
        meta.setLore(lore);
        item.setItemMeta(meta);
        gui.getInventory().setItem(slot, item);
        gui.clickActions.put(slot, action);
    }
    
}
