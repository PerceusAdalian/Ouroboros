package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsMainMenu;

public class WandMainPage extends AbstractOBSGui
{

	public WandMainPage(Player player) 
	{
		super(player, "Wand Interface", 27, Set.of(12,13,14,10,16));
	}

	@Override
	protected void build() 
	{
		
		GuiButton.button(Material.NETHER_STAR).setName("&f&lCraft &r&fNew Wand").setLore("Click to craft a new wand").place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new CraftableWandsView(p));
		});
		
		GuiButton.button(Material.STICK).setName("&e&lUpgrade &r&fExisting Wand").setLore("Click to upgrade an existing wand").place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		
		GuiButton.button(Material.NETHER_STAR).setName("&d&lRecharge &r&fExisting Wand").setLore("Click to recharge an existing wand").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Obs Main Page").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ObsMainMenu(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
	}
	
}
