package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.celestio.Ascension;
import com.lol.spells.instances.celestio.AssertOrder;
import com.lol.spells.instances.celestio.Cure;
import com.lol.spells.instances.celestio.Diagnosis;
import com.lol.spells.instances.celestio.Expell;
import com.lol.spells.instances.celestio.Lumos;
import com.lol.spells.instances.celestio.Pneuma;
import com.lol.spells.instances.celestio.Protego;
import com.lol.spells.instances.celestio.Revelio;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;

public class CelestioSpellsPage extends AbstractOBSGui
{

	public CelestioSpellsPage(Player player) 
	{
		super(player, "Celestio Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));	}

	@Override
	protected void build() 
	{
		// 1
		GuiButton.placeSpellButton(player, new Diagnosis(), 10, this);
		GuiButton.placeSpellButton(player, new Protego(), 11, this);
		GuiButton.placeSpellButton(player, new Expell(), 12, this);
		
		// 2
		
		// 3
		GuiButton.placeSpellButton(player, new Lumos(), 13, this);
		GuiButton.placeSpellButton(player, new Revelio(), 14, this);
		GuiButton.placeSpellButton(player, new Cure(), 15, this);
		
		// 4
		GuiButton.placeSpellButton(player, new AssertOrder(), 16, this);
		
		// 5
		GuiButton.placeSpellButton(player, new Ascension(), 19, this);
		
		// 6
		GuiButton.placeSpellButton(player, new Pneuma(), 20, this);
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to 'Spellbook Page'").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new SpellBookPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.close(p);
		});
		
		paint();	
	}

}
