package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.entity.Player;

import com.lol.spells.instances.cosmo.Antimatter;
import com.lol.spells.instances.cosmo.ArrestoMomentum;
import com.lol.spells.instances.cosmo.Collapse;
import com.lol.spells.instances.cosmo.Disintegrate;
import com.lol.spells.instances.cosmo.ElementalConfinement;
import com.lol.spells.instances.cosmo.Gate;
import com.lol.spells.instances.cosmo.Negate;
import com.lol.spells.instances.cosmo.Nullify;
import com.lol.spells.instances.cosmo.Reconfigure;
import com.lol.spells.instances.cosmo.Teleport;
import com.lol.spells.instances.cosmo.VoidForm;
import com.lol.spells.instances.cosmo.Warp;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.ObsGui;

public class CosmoSpellsPage extends ObsGui
{

	public CosmoSpellsPage(Player player) 
	{
		super(player, "Cosmo Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));	}

	@Override
	protected void build() 
	{
		
		// 1
		GuiButton.placeCantripSpellButton(player, new Reconfigure(), 10, this);
		GuiButton.placeSpellButton(player, new Nullify(), 11, this);
		
		// 2
		GuiButton.placeSpellButton(player, new Negate(), 12, this);
		GuiButton.placeSpellButton(player, new Antimatter(), 13, this);
		GuiButton.placeSpellButton(player, new ArrestoMomentum(), 14, this);
		
		// 3
		GuiButton.placeSpellButton(player, new Disintegrate(), 15, this);
		GuiButton.placeSpellButton(player, new ElementalConfinement(), 16, this);
		GuiButton.placeSpellButton(player, new Collapse(), 19, this);
		GuiButton.placeSpellButton(player, new Teleport(), 20, this);
		
		// 4
		GuiButton.placeSpellButton(player, new VoidForm(), 21, this);
		GuiButton.placeSpellButton(player, new Gate(), 22, this);
		
		// 5
		GuiButton.placeSpellButton(player, new Warp(), 23, this);
		
		
		//Exits
		GuiButton.placeGoBack(37, this, new ElementalSpellBookPage(player));
		GuiButton.placeExit(43, this);
		
		paint();	
	}

}
