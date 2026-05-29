package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.menus.instances.ObsMainMenu;

public class SpellBookMainPage extends ObsGui
{

	public SpellBookMainPage(Player player)
	{
		super(player, "Spellbook", 27, Set.of());
	}

	@Override
	protected void build()
	{
		GuiButton.button(Material.BOOK).setName("&e&lPrimary School &r&eSpells").setLore("Click to view all elemental &r&espells&f.").place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ElementalSpellBookPage(p));
		});
		
		GuiButton.button(Material.WRITTEN_BOOK).setName("&b&lSecondary School &r&eSpells").setLore("Click to view all unique &earcana&f.").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new SpecialSpellBookPage(p));
		});
		
		GuiButton.placeGoBack(10, this, new ObsMainMenu(player));
		GuiButton.placeExit(16, this);
		
		paint();
	}

}