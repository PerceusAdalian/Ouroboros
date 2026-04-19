package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.inferno.AspectOfAighil;
import com.lol.spells.instances.inferno.Bombarda;
import com.lol.spells.instances.inferno.Combustion;
import com.lol.spells.instances.inferno.Ignite;
import com.lol.spells.instances.inferno.Incendio;
import com.lol.spells.instances.inferno.Meteor;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class InfernoSpellsPage extends ObsGui
{

	public InfernoSpellsPage(Player player) 
	{
		super(player, "Inferno Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));
	}

	@Override
	protected void build() 
	{
		// 1
		GuiButton.placeCantripSpellButton(player, new Incendio(), 10, this);
		
		// 2
		GuiButton.placeSpellButton(player, new Combustion(), 11, this);
		
		// 3
		GuiButton.placeSpellButton(player, new Ignite(), 12, this);
		
		// 4
		GuiButton.placeSpellButton(player, new Bombarda(), 13, this);
		GuiButton.placeSpellButton(player, new Meteor(), 14, this);
		
		// 5
		
		// 6
		GuiButton.placeSpellButton(player, new AspectOfAighil(), 15, this);
		
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
