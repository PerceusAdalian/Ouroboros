package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.heresio.AvadaKedavra;
import com.lol.spells.instances.heresio.Axiom;
import com.lol.spells.instances.heresio.Beguile;
import com.lol.spells.instances.heresio.Corollary;
import com.lol.spells.instances.heresio.Hex;
import com.lol.spells.instances.heresio.Hypothesis;
import com.lol.spells.instances.heresio.Lemma;
import com.lol.spells.instances.heresio.Mania;
import com.lol.spells.instances.heresio.Penance;
import com.lol.spells.instances.heresio.Postulate;
import com.lol.spells.instances.heresio.Theorem;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class HeresioSpellsPage extends ObsGui
{

	public HeresioSpellsPage(Player player) 
	{
		super(player, "Heresio Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));	}

	@Override
	protected void build() 
	{
		// 1
		GuiButton.placeCantripSpellButton(player, new Axiom(), 10, this);
		
		
		// 2
		GuiButton.placeSpellButton(player, new Postulate(), 11, this);
		GuiButton.placeSpellButton(player, new Beguile(), 12, this);
		
		// 3
		GuiButton.placeSpellButton(player, new Hypothesis(), 13, this);
		GuiButton.placeSpellButton(player, new Penance(), 14, this);
		
		// 4
		GuiButton.placeSpellButton(player, new Lemma(), 15, this);
		GuiButton.placeSpellButton(player, new Mania(), 16, this);
		
		// 5
		GuiButton.placeSpellButton(player, new Theorem(), 19, this);
		
		// 6
		GuiButton.placeSpellButton(player, new Corollary(), 20, this);
		
		// 7
		GuiButton.placeSpellButton(player, new Hex(), 21, this);
		GuiButton.placeSpellButton(player, new AvadaKedavra(), 22, this);
		
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
