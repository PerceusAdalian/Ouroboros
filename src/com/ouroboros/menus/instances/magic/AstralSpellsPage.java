package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.entity.Player;

import com.lol.spells.instances.astral.Attenuate;
import com.lol.spells.instances.astral.CollectiveUnconscious;
import com.lol.spells.instances.astral.Starfall;
import com.lol.spells.instances.astral.TemporalDistortion;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.ObsGui;

public class AstralSpellsPage extends ObsGui
{
	public AstralSpellsPage(Player player) 
	{
		super(player, "Astral Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));
	}

	@Override
	protected void build() 
	{
		// 1
		GuiButton.placeSpellButton(player, new Starfall(), 10, this);
		
		// 2
		GuiButton.placeSpellButton(player, new Attenuate(), 11, this);
		
		// 3

		// 4
		GuiButton.placeSpellButton(player, new CollectiveUnconscious(), 12, this);
		
		// 5

		// 6
		
		// 7
		GuiButton.placeSpellButton(player, new TemporalDistortion(), 13, this);
		
		//Exits
		GuiButton.placeGoBack(37, this, new SpecialSpellBookPage(player));
		GuiButton.placeExit(43, this);
		
		paint();
		
	}
}
