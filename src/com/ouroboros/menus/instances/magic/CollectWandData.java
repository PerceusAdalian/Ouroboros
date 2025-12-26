package com.ouroboros.menus.instances.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lol.wand.Wand;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsMainMenu;
import com.ouroboros.utils.PrintUtils;

public class CollectWandData extends AbstractOBSGui
{

	public CollectWandData(Player player) 
	{
		super(player, "Wand Collection", 27, Set.of(10,13,16));
	}

	public static Map<UUID, Wand> wandCollector = new HashMap<>();
	public static Map<UUID, String> pageController = new HashMap<>();
	@Override
	protected void build() 
	{
		
		GuiButton.button(Material.GRAY_STAINED_GLASS_PANE)
		.setName("Place wand here")
		.setLore("Click on this panel with your wand.", 
				"If it's valid, you'll automatically advance to the next page.",
				"&c&lWARNING&r&f: If you quit during wand collection, data will be lost.",
				"Recover wands by executing &d&o/recoverwand&r&f.")
		.place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			ItemStack stack = p.getItemOnCursor();
			if (stack != null && Wand.isWand(stack))
			{
				wandCollector.put(p.getUniqueId(), new Wand(stack));
				p.setItemOnCursor(null);
				if (pageController.get(p.getUniqueId()).equals("spellselect"))
					GuiHandler.changeMenu(p, new SetSpellPage(p));
				else if (pageController.get(p.getUniqueId()).equals("upgrade"))
					GuiHandler.changeMenu(p, new WandUpgradePage(p));
				else if (pageController.get(p.getUniqueId()).equals("recharge"))
					GuiHandler.changeMenu(p, new WandRechargePage(p));
				return;
			}
			PrintUtils.OBSFormatError(p, "Invalid Object Detected... Cancelling operation, please try again.");
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 4, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 22, e->
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
