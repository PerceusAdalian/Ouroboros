package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.entity.Player;

import com.lol.spells.instances.admin.RemoveEntity;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.ObsGui;

public class AdminSpellsPage extends ObsGui
{

	public AdminSpellsPage(Player player) 
	{
		super(player, "Admin Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));
	}

	@Override
	protected void build() 
	{
		// Admin Spells (All Rarity One)
		GuiButton.placeSpellButton(player, new RemoveEntity(), 10, this);
		
		//Exits
		GuiButton.placeExit(37, this);
		GuiButton.placeExit(43, this);
		
		paint();
	}

}
