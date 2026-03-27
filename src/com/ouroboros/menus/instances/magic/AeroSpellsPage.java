package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.aero.Charge;
import com.lol.spells.instances.aero.Diffindo;
import com.lol.spells.instances.aero.Galeforce;
import com.lol.spells.instances.aero.GalvanicNeedle;
import com.lol.spells.instances.aero.Gust;
import com.lol.spells.instances.aero.Levioso;
import com.lol.spells.instances.aero.Smite;
import com.lol.spells.instances.aero.Tailwind;
import com.lol.spells.instances.aero.Thunderbolt;
import com.lol.spells.instances.aero.Thunderstorm;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;

public class AeroSpellsPage extends AbstractOBSGui
{

	public AeroSpellsPage(Player player) 
	{
		super(player, "Aero Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));
	}

	@Override
	protected void build() 
	{
		// One Rarity
		GuiButton.placeSpellButton(player, new Gust(), 10, this);
		GuiButton.placeSpellButton(player, new Tailwind(), 11, this);
		GuiButton.placeSpellButton(player, new Charge(), 12, this);
		GuiButton.placeSpellButton(player, new Smite(), 13, this);
		GuiButton.placeSpellButton(player, new Levioso(), 14, this);
		
		// Two Rarity
		GuiButton.placeSpellButton(player, new Galeforce(), 15, this);
		GuiButton.placeSpellButton(player, new Thunderbolt(), 16, this);
		
		// Three Rarity
		
		// Four Rarity
		GuiButton.placeSpellButton(player, new GalvanicNeedle(), 19, this);
		
		// Five Rarity
		GuiButton.placeSpellButton(player, new Diffindo(), 20, this);
		GuiButton.placeSpellButton(player, new Thunderstorm(), 21, this);
		
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
