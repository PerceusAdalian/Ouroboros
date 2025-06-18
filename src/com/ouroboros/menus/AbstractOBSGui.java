package com.ouroboros.menus;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class AbstractOBSGui 
{
	protected Map<Integer, Consumer<InventoryClickEvent>> clickActions = new HashMap<>();

	protected final String guiTitle;
	protected final int size;
	protected final Set<Integer> whitelistedSlots;
	protected final Inventory inv;
	protected Player player;
	
	public AbstractOBSGui(Player player, String guiTitle, int size, Set<Integer> whitelistedSlots) 
	{
		this.player = player;
        this.guiTitle = guiTitle;
        this.size = size;
        this.whitelistedSlots = whitelistedSlots;
        this.inv = Bukkit.createInventory(null, size, guiTitle);
	}
	
	public void open() 
	{
		build();
		player.openInventory(inv);
	}
	
	public String getGuiTitle() 
	{
		return guiTitle;
	}
	
	public Inventory getInventory() 
	{
		return inv;
	}
	
	public void handleClick(InventoryClickEvent e) 
	{
	    Consumer<InventoryClickEvent> action = clickActions.get(e.getRawSlot());
	    if (action != null) 
	    {
	        action.accept(e);
	    } 
	    else if(!whitelistedSlots.contains(e.getRawSlot())) 
	    {
	        e.setCancelled(true);
	    }
	}
	
	public void handleClose(InventoryCloseEvent e) 
	{
		return;
	}
	
	public void paint() 
	{
		for (int i = 0; i < size; i++) 
		{
			if (!whitelistedSlots.contains(i) && inv.getItem(i) == null) 
			{
				ItemStack stack = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
				ItemMeta meta = stack.getItemMeta();
				meta.setDisplayName(" ");
				stack.setItemMeta(meta);
				inv.setItem(i, stack);
			}
		}
	}
	
	protected abstract void build();
}
