package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.geo.Dracomorph;
import com.lol.spells.instances.geo.Dynamorph;
import com.lol.spells.instances.geo.Expelliarmus;
import com.lol.spells.instances.geo.Geomorph;
import com.lol.spells.instances.geo.Magemorph;
import com.lol.spells.instances.geo.Metalmorph;
import com.lol.spells.instances.geo.PetrificusTotalis;
import com.lol.spells.instances.geo.SandBlast;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class GeoSpellsPage extends ObsGui
{

	public GeoSpellsPage(Player player) 
	{
		super(player, "Geo Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));	}

	@Override
	protected void build() 
	{
		
		// 1
		GuiButton.placeCantripSpellButton(player, new Geomorph(), 10, this);
		GuiButton.placeSpellButton(player, new SandBlast(), 11, this);
		
		// 2
		GuiButton.placeSpellButton(player, new Metalmorph(), 12, this);
		GuiButton.placeSpellButton(player, new PetrificusTotalis(), 13, this);
		
		// 3
		GuiButton.placeSpellButton(player, new Magemorph(), 14, this);
		GuiButton.placeSpellButton(player, new Expelliarmus(), 15, this);
		
		// 4
		GuiButton.placeSpellButton(player, new Dynamorph(), 16, this);
		
		// 5
		GuiButton.placeSpellButton(player, new Dracomorph(), 19, this);
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to 'Spellbook Page'").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new ElementalSpellBookPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER);
			GuiHandler.close(p);
		});
		
		paint();	
	}

}
