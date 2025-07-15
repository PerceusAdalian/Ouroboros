package com.ouroboros.menus.instances;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;

public class ObsMainMenu extends AbstractOBSGui
{

	public ObsMainMenu(Player player) 
	{
		super(player, "Ouroboros Main Menu", 27, Set.of(10, 13, 16));
	}

	@Override
	protected void build() 
	{
		GuiButton.button(Material.BUNDLE).setName("&e&lShop&r&f").setLore("&r&fClick to view the official OBS Store.").place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ObsShopGui(p));
		});
		GuiButton.button(Material.NETHER_STAR).setName("&c&lAbilities&r&f").setLore("&r&fClick to view available abilities").place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new AbilityMainPage(p));
		});
		
		//Exits
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to exit").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to exit").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
	}
}
