package com.ouroboros.menus.instances;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.store.ShopGuiItemConfirm;
import com.ouroboros.menus.store.ShopItemContainer;
import com.ouroboros.utils.PrintUtils;

public class ObsShopGui extends AbstractOBSGui
{
	public static Map<UUID, Material> confirmBuyer = new HashMap<>();

	public ObsShopGui(Player player) 
	{
		super(player, "Nexus Shop", 54, Set.of(21,22,23,37,43));
	}

	@Override
	protected void build() 
	{
		
		GuiButton.button(Material.DIRT).setLore(PrintUtils.setCost(ShopItemContainer.itemTable.get(Material.DIRT))).place(this, 21, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.MASTER, 1, 1);
			confirmBuyer.put(p.getUniqueId(), Material.DIRT);
			GuiHandler.changeMenu(p, new ShopGuiItemConfirm(p));
		});
		
		GuiButton.button(Material.OAK_WOOD).setLore(PrintUtils.setCost(ShopItemContainer.itemTable.get(Material.OAK_WOOD))).place(this, 22, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.MASTER, 1, 1);
			confirmBuyer.put(p.getUniqueId(), Material.OAK_WOOD);
			GuiHandler.changeMenu(p, new ShopGuiItemConfirm(p));
		});
		
		GuiButton.button(Material.BREAD).setLore(PrintUtils.setCost(ShopItemContainer.itemTable.get(Material.BREAD))).place(this, 23, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.MASTER, 1, 1);
			confirmBuyer.put(p.getUniqueId(), Material.BREAD);
			GuiHandler.changeMenu(p, new ShopGuiItemConfirm(p));
		});
		
		//Exits
		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to the previous screen.").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ObsMainMenu(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to exit.").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
	}

}
