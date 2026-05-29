package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.entity.Player;

import com.lol.spells.instances.arcano.ArcaneBolt;
import com.lol.spells.instances.arcano.AspectOfLordran;
import com.lol.spells.instances.arcano.ExtractEther;
import com.lol.spells.instances.arcano.Fortune;
import com.lol.spells.instances.arcano.Freecast;
import com.lol.spells.instances.arcano.Mute;
import com.lol.spells.instances.arcano.OuroborosPrime;
import com.lol.spells.instances.arcano.Prisma;
import com.lol.spells.instances.arcano.Reparo;
import com.lol.spells.instances.arcano.Sigil;
import com.lol.spells.instances.arcano.Surveil;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.ObsGui;

public class ArcanoSpellsPage extends ObsGui
{

	public ArcanoSpellsPage(Player player) 
	{
		super(player, "Arcano Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));
	}

	@Override
	protected void build() 
	{
		// 1
		GuiButton.placeCantripSpellButton(player, new Surveil(), 10, this);
		GuiButton.placeCantripSpellButton(player, new Sigil(), 11, this);
		
		// 2
		GuiButton.placeSpellButton(player, new ArcaneBolt(), 12, this);
		GuiButton.placeSpellButton(player, new Prisma(), 13, this);
		
		// 3
		GuiButton.placeSpellButton(player, new Reparo(), 14, this);
		GuiButton.placeSpellButton(player, new ExtractEther(), 15, this);
		
		// 4
		GuiButton.placeSpellButton(player, new Mute(), 16, this);
		
		// 5
		GuiButton.placeSpellButton(player, new Freecast(), 19, this);
		GuiButton.placeSpellButton(player, new Fortune(), 20, this);
		
		// 6
		
		// 7
		GuiButton.placeSpellButton(player, new AspectOfLordran(), 21, this);
		GuiButton.placeSpellButton(player, new OuroborosPrime(), 22, this);

		//Exits
		GuiButton.placeGoBack(37, this, new SpecialSpellBookPage(player));
		GuiButton.placeExit(43, this);
		
		paint();
		
	}

}
