package com.ouroboros.menus.store;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsShopGui;
import com.ouroboros.utils.PrintUtils;

public class ShopGuiItemConfirm extends AbstractOBSGui
{

	public ShopGuiItemConfirm(Player player) 
	{
		super(player, "Confirm Purchase", 27, Set.of(4,10,12,13,14,16));
	}

	@Override
	protected void build() 
	{
		String itemName = ObsShopGui.confirmBuyer.get(player.getUniqueId()).toString().toLowerCase();
		String[] splitName = itemName.split("_");
		itemName = "";
		for (String s : splitName) 
		{
			char[] chars = s.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			itemName += new String(chars);
			itemName += " ";
		}
		itemName = itemName.substring(0, itemName.length() - 1);
		
		GuiButton.button(Material.OAK_SIGN)
		.setName("&r&f[&e&li&r&f]&o Purchase Terms and Conditions")
		.setLore("&r&a&oConfirm&r&f your &e&opurchase&r&f below by choosing the &oammount&r&f you'd wish to buy.",
				"&r&fThe cost &7(&e₪&7)&r&f associated is listed in the &b&oitem's description&r&f.",
				"&r&f&l&oNecessary &r&e&o₪ &r&f&l&omust be present to complete the purchase.",
				"&r&fYou may continue to &omake multiple purchases of the same item&r&f.",
				"To close or return the current or previous screen,",
				"Click either &c&o'Exit'&r&f or &a&o'Go Back'&r&f buttons.",
				"&r&f&nThank you for choosing to shop with us&r&f.")
		.place(this, 4, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1,1);
		});
		
		GuiButton.button(ObsShopGui.confirmBuyer.get(player.getUniqueId()))
		.setName("&r&f" + itemName + " 1x")
		.setLore(PrintUtils.setCost(ShopItemContainer.itemTable.get(ObsShopGui.confirmBuyer.get(player.getUniqueId()))))
		.place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			ItemStack stack = new ItemStack(ObsShopGui.confirmBuyer.get(player.getUniqueId()), 1);
			int cost = ShopItemContainer.itemTable.get(ObsShopGui.confirmBuyer.get(player.getUniqueId()));
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.MASTER, 1,1);
			PlayerData.subtractMoney(p, cost);
			p.getInventory().addItem(stack);
		});
		
		GuiButton.button(ObsShopGui.confirmBuyer.get(player.getUniqueId()))
		.setName("&r&f" + itemName + " 16x")
		.setLore(PrintUtils.setCost(ShopItemContainer.itemTable.get(ObsShopGui.confirmBuyer.get(player.getUniqueId()))*16))
		.place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			ItemStack stack = new ItemStack(ObsShopGui.confirmBuyer.get(player.getUniqueId()), 16);
			int cost = ShopItemContainer.itemTable.get(ObsShopGui.confirmBuyer.get(player.getUniqueId()))*16;			
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.MASTER, 1,1);
			PlayerData.subtractMoney(p, cost);
			p.getInventory().addItem(stack);
		});
		
		GuiButton.button(ObsShopGui.confirmBuyer.get(player.getUniqueId()))
		.setName("&r&f" + itemName + " 32x")
		.setLore(PrintUtils.setCost(ShopItemContainer.itemTable.get(ObsShopGui.confirmBuyer.get(player.getUniqueId()))*32))
		.place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			ItemStack stack = new ItemStack(ObsShopGui.confirmBuyer.get(player.getUniqueId()), 32);
			int cost = ShopItemContainer.itemTable.get(ObsShopGui.confirmBuyer.get(player.getUniqueId()))*32;
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.MASTER, 1,1);
			PlayerData.subtractMoney(p, cost);
			p.getInventory().addItem(stack);
			
		});
		
		//Exits
		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to previous screen.").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			ObsShopGui.confirmBuyer.remove(player.getUniqueId());
			GuiHandler.changeMenu(p, new ObsShopGui(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to exit.").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			ObsShopGui.confirmBuyer.remove(player.getUniqueId());
			GuiHandler.close(p);
		});
		paint();
	}

}
