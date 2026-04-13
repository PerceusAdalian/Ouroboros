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

public class WandCraftAlternativePage  extends ObsGui
{

	public WandCraftAlternativePage(Player player)
	{
		super(player, "Alternative Wands", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,30,32,39,41,37,43));
	}

	@Override
	protected void build()
	{
		
		// 5
		WandCraftPage.placeWandButton(player, Wand.get("celestio_wand"), 10, this);
		WandCraftPage.placeWandButton(player, Wand.get("mortio_wand"), 11, this);
		WandCraftPage.placeWandButton(player, Wand.get("inferno_wand"), 12, this);
		WandCraftPage.placeWandButton(player, Wand.get("glacio_wand"), 13, this);
		WandCraftPage.placeWandButton(player, Wand.get("aero_wand"), 14, this);
		WandCraftPage.placeWandButton(player, Wand.get("geo_wand"), 15, this);
		WandCraftPage.placeWandButton(player, Wand.get("cosmo_wand"), 16, this);
		WandCraftPage.placeWandButton(player, Wand.get("heresio_wand"), 19, this);
		
		// 6
		WandCraftPage.placeWandButton(player, Wand.get("baton_of_the_holy_magistrate"), 20, this);
		WandCraftPage.placeWandButton(player, Wand.get("sithis_armament"), 21, this);
		WandCraftPage.placeWandButton(player, Wand.get("aighils_talon"), 22, this);
		WandCraftPage.placeWandButton(player, Wand.get("bjorn_artifact"), 23, this);
		WandCraftPage.placeWandButton(player, Wand.get("nidus_cane"), 24, this);
		WandCraftPage.placeWandButton(player, Wand.get("seth_caduceus"), 25, this);
		WandCraftPage.placeWandButton(player, Wand.get("antenna_of_end"), 30, this);
		WandCraftPage.placeWandButton(player, Wand.get("stave_of_general_falric"), 32, this);
		
		// 7
		WandCraftPage.placeWandButton(player, Wand.get("luminas_wand"), 39, this);
		WandCraftPage.placeWandButton(player, Wand.get("twilight_catalyst"), 41, this);
		
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
