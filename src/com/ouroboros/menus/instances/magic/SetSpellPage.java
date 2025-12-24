package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.Spell;
import com.lol.wand.Wand;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;

public class SetSpellPage extends AbstractOBSGui
{

	public SetSpellPage(Player player) 
	{
		super(player, "Select Slot", 54, Set.of(10,11,12,13,14,15,20,21,22,23,24,37,43));
	}
	
	@Override
	protected void build() 
	{
		Wand wand = CollectWandData.wandCollector.get(player.getUniqueId());
		Spell spell = GuiButton.spellActivateConfirm.get(player.getUniqueId());
		
		//hookup spell slot selection, placement, and then move on to the command, test, and you should be done. 
		//Also, add error handling for when the player exists. 
		//Hookup commands for /spellbook, and /recoverspell
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to your Spellbook.",
				"Doing so will return your wand.").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new SpellBookPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to exit this page. Doing so returns your wand.").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
	}

}
