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
import com.lol.spells.instances.celestio.Heal;
import com.lol.spells.instances.celestio.Lumina;
import com.lol.spells.instances.celestio.Lumos;
import com.lol.spells.instances.celestio.MagicMissile;
import com.lol.spells.instances.celestio.MinorBlessing;
import com.lol.spells.instances.celestio.Pneuma;
import com.lol.spells.instances.celestio.Protego;
import com.lol.spells.instances.celestio.Revelio;
import com.lol.spells.instances.celestio.Satiate;
import com.lol.spells.instances.celestio.SolarExpanse;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class CelestioSpellsPage extends ObsGui
{

	public CelestioSpellsPage(Player player) 
	{
		super(player, "Celestio Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));	}

	@Override
	protected void build() 
	{
		// 1
		GuiButton.placeCantripSpellButton(player, new Heal(), 10, this);
		GuiButton.placeSpellButton(player, new Lumina(), 11, this);
		GuiButton.placeSpellButton(player, new Diagnosis(), 12, this);
		GuiButton.placeSpellButton(player, new Protego(), 13, this);
		GuiButton.placeSpellButton(player, new Expell(), 14, this);
		
		// 2
		GuiButton.placeSpellButton(player, new Satiate(), 15, this);
		GuiButton.placeSpellButton(player, new MinorBlessing(), 16, this);
		GuiButton.placeSpellButton(player, new MagicMissile(), 19, this);
		// 3
		GuiButton.placeSpellButton(player, new Lumos(), 20, this);
		GuiButton.placeSpellButton(player, new Revelio(), 21, this);
		GuiButton.placeSpellButton(player, new Cure(), 22, this);
		
		// 4
		GuiButton.placeSpellButton(player, new AssertOrder(), 23, this);
		GuiButton.placeSpellButton(player, new SolarExpanse(), 24, this);
		
		// 5
		GuiButton.placeSpellButton(player, new Ascension(), 25, this);
		GuiButton.placeSpellButton(player, new Pneuma(), 28, this);
		
		// 6
		
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
