package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lol.spells.instances.Spell;
import com.lol.wand.Wand;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.PrintUtils;

public class WandViewPage extends AbstractOBSGui
{

	public WandViewPage(Player player) 
	{
		super(player, "Select Slot", 54, Set.of(11,12,13,14,15,20,21,22,23,24,37,43));
	}
	
	@Override
	protected void build() 
	{
		Wand wand = CollectWandData.wandCollector.get(player.getUniqueId());
		Spell spell = GuiButton.spellActivateConfirm.get(player.getUniqueId());
		
		renderWandSlots(player, wand, spell, this);
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE)
		.setName("<- &e&lGo Back")
		.setLore("&r&fClick to return to your Spellbook.", "&r&fDoing so will return your wand.")
		.place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			Wand storedWand = CollectWandData.wandCollector.get(p.getUniqueId());
			ItemStack returnedWand = storedWand.getAsItemStack();
			p.getInventory().addItem(returnedWand);
			CollectWandData.wandCollector.remove(p.getUniqueId());
            GuiButton.spellActivateConfirm.remove(p.getUniqueId());
            CollectWandData.pageController.remove(p.getUniqueId());
			GuiHandler.changeMenu(p, new SpellBookPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE)
		.setName("&c&lExit Menu")
		.setLore("&r&fClick to exit the menu.", "&r&fDoing so returns your wand.")
		.place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			Wand storedWand = CollectWandData.wandCollector.get(p.getUniqueId());
			ItemStack returnedWand = storedWand.getAsItemStack();
			p.getInventory().addItem(returnedWand);
			CollectWandData.wandCollector.remove(p.getUniqueId());
            GuiButton.spellActivateConfirm.remove(p.getUniqueId());
            CollectWandData.pageController.remove(p.getUniqueId());
			GuiHandler.close(p);
		});
		paint();
	}
	
	public void renderWandSlots(Player player, Wand wand, Spell spell, AbstractOBSGui gui)
	{
	    // Define the whitelisted GUI slots (up to 10 slots)
	    int[] validSlots = {11, 12, 13, 14, 15, 20, 21, 22, 23, 24};
	    int spellSlots = wand.getCurrentMaxSpellSlots();
	    
	    // Render slots based on wand capacity
	    for (int i = 0; i < validSlots.length; i++)
	    {
	        int guiSlot = validSlots[i];
	        
	        if (i < spellSlots)
	        {
	            // This slot is unlocked on the wand
	            final int selectedSlot = i;
	            Spell wandSpell = wand.getSpell(i);
	            
	            boolean isRemoving = CollectWandData.pageController.get(player.getUniqueId()).equals("removespell");

	            if (wandSpell == null)
	            {
	                // Empty slot - allow assignment
	            	char spellColor = PrintUtils.getElementTypeColor(spell.getElementType());
            		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE)
            		.setName("&r&fEmpty Slot #"+(i+1))
            		.setLore(!isRemoving ? "&r&fThis slot is empty. Click to assign &" + spellColor + spell.getName() + "&f to this slot" : "&r&fThis slot is empty.")
            		.place(gui, guiSlot, e ->
            		{
            			Player p = (Player) e.getWhoClicked();
            			if (!isRemoving)
            			{
            				EntityEffects.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.AMBIENT);
            				wand.setSpell(spell, selectedSlot);
            				ItemStack stack = wand.getAsItemStack();
            				p.getInventory().addItem(stack);	            				
            			}
            			else
            			{
            				e.setCancelled(true);
	            			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
            			}
            			CollectWandData.wandCollector.remove(p.getUniqueId());
            			GuiButton.spellActivateConfirm.remove(p.getUniqueId());
            			CollectWandData.pageController.remove(p.getUniqueId());
            			GuiHandler.close(p);
            		});
	            }
	            else
	            {
	            	char spellColor = PrintUtils.getElementTypeColor(wandSpell.getElementType());
	            	String slotName = "&r&fFull Slot #" + (i+1) + " [&" + spellColor + wandSpell.getName() + "&f]";
	            	String lore = isRemoving 
	            	    ? "&r&fThis slot is in use. Click to remove it?"
	            	    : "&r&fThis slot is in use. Click to replace &" + spellColor + wandSpell.getName() + 
	            	      " &fwith &" + PrintUtils.getElementTypeColor(spell.getElementType()) + spell.getName() + "&f?";

	            	GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE)
            	    .setName(slotName)
            	    .setLore(lore)
            	    .place(gui, guiSlot, e -> 
            	    {
            	        Player p = (Player) e.getWhoClicked();
            	        EntityEffects.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.AMBIENT);
            	        
            	        if (isRemoving) {
            	            wand.removeSpell(selectedSlot);
            	            wand.setSpellIndex(wand.getNextSpellSlot());
            	        } else {
            	            wand.setSpell(spell, selectedSlot);
            	        }
            	        
            	        p.getInventory().addItem(wand.getAsItemStack());
            	        CollectWandData.wandCollector.remove(p.getUniqueId());
            	        GuiButton.spellActivateConfirm.remove(p.getUniqueId());
            	        CollectWandData.pageController.remove(p.getUniqueId());
            	        GuiHandler.close(p);
            	    });
	            }
	        }
	        else
	        {
	            // This slot is locked (beyond wand's current capacity)
	            GuiButton.button(Material.BARRIER)
                .setName("&c&lLocked Slot")
                .setLore("This slot isn't currently unlocked via your tier of wand.", "Upgrade it to unlock more slots.")
                .place(gui, guiSlot, e ->
                {
                    Player p = (Player) e.getWhoClicked();
                    EntityEffects.playSound(p, Sound.BLOCK_CHEST_LOCKED, SoundCategory.AMBIENT);
                    e.setCancelled(true);
                });
	        }
	    }
	}

}
