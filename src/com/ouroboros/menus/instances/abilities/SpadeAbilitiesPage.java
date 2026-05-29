package com.ouroboros.menus.instances.abilities;

import java.util.Set;

import org.bukkit.entity.Player;

import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.ObsGui;

public class SpadeAbilitiesPage extends ObsGui
{

	public SpadeAbilitiesPage(Player player)
	{
		super(player, "Spade Abilities Page", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));
		
	}

	@Override
	protected void build()
	{
		
		//Exits
		GuiButton.placeGoBack(37, this, new AbilitiesMainPage(player));
		GuiButton.placeExit(43, this);
		
		paint();
	}

}
