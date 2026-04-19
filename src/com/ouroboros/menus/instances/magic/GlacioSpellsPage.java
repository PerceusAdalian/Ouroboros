package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.glacio.Chill;
import com.lol.spells.instances.glacio.Freeze;
import com.lol.spells.instances.glacio.GlacialFlood;
import com.lol.spells.instances.glacio.Glacius;
import com.lol.spells.instances.glacio.IcyWind;
import com.lol.spells.instances.glacio.Riptide;
import com.lol.spells.instances.glacio.Scald;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.entityeffects.EntityEffects;

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
		GuiButton.placeSpellButton(player, new IcyWind(), 15, this);
		
		// 4
		GuiButton.placeSpellButton(player, new GlacialFlood(), 16, this);
		
		// 5
		
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
