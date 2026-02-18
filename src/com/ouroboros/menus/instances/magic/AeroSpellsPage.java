package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.aero.Charge;
import com.lol.spells.instances.aero.Diffindo;
import com.lol.spells.instances.aero.GalvanicNeedle;
import com.lol.spells.instances.aero.Levioso;
import com.lol.spells.instances.aero.Smite;
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
		GuiButton.placeSpellButton(player, new Charge(), 10, this);
		GuiButton.placeSpellButton(player, new Smite(), 11, this);
		GuiButton.placeSpellButton(player, new Levioso(), 12, this);
		// Two Rarity
		GuiButton.placeSpellButton(player, new Thunderbolt(), 13, this);
		// Three Rarity
		// Four Rarity
		GuiButton.placeSpellButton(player, new GalvanicNeedle(), 14, this);
		// Five Rarity
		GuiButton.placeSpellButton(player, new Diffindo(), 15, this);
		GuiButton.placeSpellButton(player, new Thunderstorm(), 16, this);
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to 'Magic Main Page'").place(this, 37, e->
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
