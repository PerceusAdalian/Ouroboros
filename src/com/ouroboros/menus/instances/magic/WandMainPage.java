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

public class WandMainPage extends ObsGui
{

	public WandMainPage(Player player) 
	{
		super(player, "Wand Main Menu", 27, Set.of(12,13,14,10,16));
	}

	@Override
	protected void build() 
	{
		
		GuiButton.button(Material.CRAFTER).setName("&f&lCraft &r&fNew Wand").setLore("Click to craft a new wand").place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new WandCraftPage(p));
		});
		
		GuiButton.button(Material.STICK).setName("&e&lUpgrade &r&fExisting Wand").setLore("Click to upgrade an existing wand").place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			CollectWandData.pageController.put(p.getUniqueId(), "upgrade");
			GuiHandler.changeMenu(p, new CollectWandData(p));
		});
		
		GuiButton.button(Material.NETHER_STAR).setName("&d&lRecharge &r&fExisting Wand").setLore("Click to recharge an existing wand").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			CollectWandData.pageController.put(p.getUniqueId(), "recharge");
			GuiHandler.changeMenu(p, new CollectWandData(p));
		});
		
		//Exits
		GuiButton.placeGoBack(10, this, new ObsMainMenu(player));
		GuiButton.placeExit(16, this);
		paint();
	}
	
}
