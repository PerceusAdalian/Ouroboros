package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.cosmo.Antimatter;
import com.lol.spells.instances.cosmo.ArrestoMomentum;
import com.lol.spells.instances.cosmo.ElementalConfinement;
import com.lol.spells.instances.cosmo.Gate;
import com.lol.spells.instances.cosmo.Nullify;
import com.lol.spells.instances.cosmo.Reconfigure;
import com.lol.spells.instances.cosmo.VoidForm;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;

public class CosmoSpellsPage extends AbstractOBSGui
{

	public CosmoSpellsPage(Player player) 
	{
		super(player, "Cosmo Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));	}

	@Override
	protected void build() 
	{
		
		// 1
		GuiButton.placeSpellButton(player, new Reconfigure(), 10, this);
		GuiButton.placeSpellButton(player, new Nullify(), 11, this);
		
		// 2
		GuiButton.placeSpellButton(player, new Antimatter(), 12, this);
		GuiButton.placeSpellButton(player, new ArrestoMomentum(), 13, this);
		
		// 3
		GuiButton.placeSpellButton(player, new ElementalConfinement(), 14, this);
		
		// 4
		GuiButton.placeSpellButton(player, new VoidForm(), 15, this);
		
		// 5
		GuiButton.placeSpellButton(player, new Gate(), 16, this);
		
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
