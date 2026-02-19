package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.mortio.Haunt;
import com.lol.spells.instances.mortio.Reap;
import com.lol.spells.instances.mortio.Sew;
import com.lol.spells.instances.mortio.SkullOfSithis;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;

public class MortioSpellsPage extends AbstractOBSGui
{

	public MortioSpellsPage(Player player) 
	{
		super(player, "Mortio Spells", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,38,39,40,41,42,37,43));	}

	@Override
	protected void build() 
	{
		// 1
		GuiButton.placeSpellButton(player, new Sew(), 10, this);
		
		// 2
		GuiButton.placeSpellButton(player, new Reap(), 11, this);
		// 3
		
		// 4
		GuiButton.placeSpellButton(player, new SkullOfSithis(), 12, this);
		
		// 5
		GuiButton.placeSpellButton(player, new Haunt(), 13, this);
		
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
