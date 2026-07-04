package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.entity.Player;

import com.lol.spells.instances.ardentio.Empathy;
import com.lol.spells.instances.ardentio.Passion;
import com.lol.spells.instances.ardentio.Peace;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.ObsGui;

public class ArdentioSpellsPage extends ObsGui
{
	public ArdentioSpellsPage(Player player) 
	{
		super(player, "Ardentio Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));
	}

	@Override
	protected void build() 
	{
		// 1
		GuiButton.placeSpellButton(player, new Passion(), 10, this);
		
		// 2
		
		// 3
		GuiButton.placeSpellButton(player, new Empathy(), 11, this);
		
		// 4
		GuiButton.placeSpellButton(player, new Peace(), 12, this);
		
		// 5

		// 6
		
		// 7
		
		//Exits
		GuiButton.placeGoBack(37, this, new SpecialSpellBookPage(player));
		GuiButton.placeExit(43, this);
		
		paint();
		
	}
}
