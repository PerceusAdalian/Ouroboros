package com.ouroboros.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.instances.abilities.AbilityConfirmationPage;
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
    
    public static GuiButton button(Material material) 
    {
        return new GuiButton(material);
    }
    
    public GuiButton setName(String name) 
    {
        meta.setDisplayName(PrintUtils.ColorParser(name));
        return this;
    }

    public GuiButton setLore(String...lines) 
    {
        for (String line : lines) 
        {
            lore.add(PrintUtils.ColorParser("&r&f" + line));
        }
        return this;
    }
    
    public GuiButton setLore(List<String> lines) 
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
    

	public static Map<Player, AbstractOBSAbility> abilityConfirmMap = new HashMap<>();
	public static Map<Player, Boolean> confirmRegister = new HashMap<>();
	
    public static void placeAbilityButton(Player player, AbstractOBSAbility ability, int slot, AbstractOBSGui gui) 
	{
	    ItemStack stack = ability.toIcon(player);

	    GuiButton.button(stack.getType())
	        .setName(stack.getItemMeta().getDisplayName())
	        .setLore(stack.getItemMeta().getLore())
	        .place(gui, slot, e -> 
	        {
	            Player p = (Player) e.getWhoClicked();
	            if (PlayerData.canRegister(p.getUniqueId(), ability)) 
	    		{
	    	        abilityConfirmMap.put(p, ability.getInstance());
	    	        confirmRegister.put(p, true);
	    	        GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
	    	    }
	    	    else if (PlayerData.getPlayer(p.getUniqueId()).getAbility(ability).isRegistered()) 
	    	    {
	    	        abilityConfirmMap.put(p, ability.getInstance());
	    	        confirmRegister.put(p, false);
	    	        GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
	    	    }
	    	    else 
	    	    {
	    	        e.setCancelled(true);
	    	        p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
	    	    }
	        });
	}
    
}
