package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.wand.Wand;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;

public class WandCraftNormalPage extends ObsGui
{

	public WandCraftNormalPage(Player player)
	{
		super(player, "Normal Wands", 54, Set.of());
	}

	@Override
	protected void build()
	{
		WandCraftPage.placeWandButton(player, Wand.get("wand_1"), 13, this);
		WandCraftPage.placeWandButton(player, Wand.get("wand_2"), 20, this);
		WandCraftPage.placeWandButton(player, Wand.get("wand_3"), 21, this);
		WandCraftPage.placeWandButton(player, Wand.get("wand_4"), 22, this);
		WandCraftPage.placeWandButton(player, Wand.get("wand_5"), 23, this);
		WandCraftPage.placeWandButton(player, Wand.get("wand_6"), 24, this);
		WandCraftPage.placeWandButton(player, Wand.get("wand_7"), 31, this);
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to the previous page.").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new WandCraftPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
	}

}
