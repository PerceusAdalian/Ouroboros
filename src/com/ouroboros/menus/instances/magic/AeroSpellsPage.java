package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.aero.ChainLightning;
import com.lol.spells.instances.aero.Charge;
import com.lol.spells.instances.aero.Diffindo;
import com.lol.spells.instances.aero.Fly;
import com.lol.spells.instances.aero.Galeforce;
import com.lol.spells.instances.aero.GalvanicNeedle;
import com.lol.spells.instances.aero.Gust;
import com.lol.spells.instances.aero.HealingCurrent;
import com.lol.spells.instances.aero.Levioso;
import com.lol.spells.instances.aero.Smite;
import com.lol.spells.instances.aero.Tailwind;
import com.lol.spells.instances.aero.Thunderbolt;
import com.lol.spells.instances.aero.Thunderstorm;
import com.lol.spells.instances.aero.Vaporize;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class AeroSpellsPage extends ObsGui
{

	public AeroSpellsPage(Player player) 
	{
		super(player, "Aero Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));
	}

	@Override
	protected void build() 
	{
		// One Rarity
		GuiButton.placeCantripSpellButton(player, new Smite(), 10, this);
		GuiButton.placeSpellButton(player, new Gust(), 11, this);
		GuiButton.placeSpellButton(player, new Tailwind(), 12, this);
		GuiButton.placeSpellButton(player, new Charge(), 13, this);
		GuiButton.placeSpellButton(player, new Levioso(), 14, this);
		
		// Two Rarity
		GuiButton.placeSpellButton(player, new Galeforce(), 15, this);
		GuiButton.placeSpellButton(player, new Thunderbolt(), 16, this);
		
		// Three Rarity
		GuiButton.placeSpellButton(player, new ChainLightning(), 19, this);
		GuiButton.placeSpellButton(player, new HealingCurrent(), 20, this);
		GuiButton.placeSpellButton(player, new Fly(), 21, this);
		
		// Four Rarity
		GuiButton.placeSpellButton(player, new GalvanicNeedle(), 22, this);
		// Five Rarity
		GuiButton.placeSpellButton(player, new Diffindo(), 23, this);
		GuiButton.placeSpellButton(player, new Thunderstorm(), 24, this);
		GuiButton.placeSpellButton(player, new Vaporize(), 25, this);
		
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
