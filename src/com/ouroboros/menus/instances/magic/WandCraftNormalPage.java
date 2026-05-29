package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.entity.Player;

import com.lol.wand.Wand;
import com.ouroboros.menus.GuiButton;
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
		GuiButton.placeGoBack(37, this, new WandCraftPage(player));
		GuiButton.placeExit(43, this);
		paint();
	}

}
