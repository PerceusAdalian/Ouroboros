package com.ouroboros.menus.instances.crafting;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsMainMenu;

public class CraftingMainPage extends AbstractOBSGui
{

	public CraftingMainPage(Player player) 
	{
		super(player, "Crafting Main Menu", 27, Set.of(10,11,12,13,14,15,16));
	}

	@Override
	protected void build() 
	{
		
		
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Obs Main Page").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ObsMainMenu(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
	}

}
