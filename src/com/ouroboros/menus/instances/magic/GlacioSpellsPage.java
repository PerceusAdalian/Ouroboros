package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.entity.Player;

import com.lol.spells.instances.glacio.Chill;
import com.lol.spells.instances.glacio.Contaminate;
import com.lol.spells.instances.glacio.Freeze;
import com.lol.spells.instances.glacio.GlacialFlood;
import com.lol.spells.instances.glacio.Glacius;
import com.lol.spells.instances.glacio.IcyWind;
import com.lol.spells.instances.glacio.Riptide;
import com.lol.spells.instances.glacio.Scald;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.ObsGui;

public class GlacioSpellsPage extends ObsGui
{

	public GlacioSpellsPage(Player player) 
	{
		super(player, "Glacio Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));
	}

	@Override
	protected void build() 
	{
		// 1
		GuiButton.placeCantripSpellButton(player, new Chill(), 10, this);
		GuiButton.placeSpellButton(player, new Riptide(), 11, this);
		
		// 2
		GuiButton.placeSpellButton(player, new Glacius(), 12, this);
		GuiButton.placeSpellButton(player, new Freeze(), 13, this);
		GuiButton.placeSpellButton(player, new Scald(), 14, this);
		
		// 3
		GuiButton.placeSpellButton(player, new Contaminate(), 15, this);
		GuiButton.placeSpellButton(player, new IcyWind(), 16, this);
		
		// 4
		GuiButton.placeSpellButton(player, new GlacialFlood(), 19, this);
		
		// 5
		
		//Exits
		GuiButton.placeGoBack(37, this, new ElementalSpellBookPage(player));
		GuiButton.placeExit(43, this);
		
		paint();
	}

}
