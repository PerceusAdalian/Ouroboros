package com.ouroboros.menus.instances.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lol.wand.Wand;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.menus.instances.ObsMainMenu;

public class CollectWandData extends ObsGui
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
		.setLore("&r&fClick on this panel with your wand.", 
				"&r&fIf it's valid, you'll automatically advance to the next page.",
				"&c&lWARNING&r&f: If you quit during wand collection, data may be lost.",
				"&r&fRecover wands by executing &d&o/recoverwand&r&f.")
		.place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			ItemStack stack = p.getItemOnCursor();
			if (stack != null && Wand.isWand(stack))
			{
				wandCollector.put(p.getUniqueId(), new Wand(stack));
				p.setItemOnCursor(null);
				if (pageController.get(p.getUniqueId()).equals("spellselect"))
					GuiHandler.changeMenu(p, new WandViewPage(p));
				else if (pageController.get(p.getUniqueId()).equals("upgrade"))
					GuiHandler.changeMenu(p, new WandUpgradePage(p));
				else if (pageController.get(p.getUniqueId()).equals("recharge"))
					GuiHandler.changeMenu(p, new WandRechargePage(p));
				else if (pageController.get(p.getUniqueId()).equals("removespell"))
					GuiHandler.changeMenu(p, new WandViewPage(p));
				return;
			}
			else e.setCancelled(true);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 4, e->
		{
			e.setCancelled(true);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 12, e->
		{
			e.setCancelled(true);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 14, e->
		{
			e.setCancelled(true);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 22, e->
		{
			e.setCancelled(true);
		});
		
		//Exits
		GuiButton.placeGoBack(10, this, new ObsMainMenu(player));
		GuiButton.placeExit(16, this);
		
		paint();
	}

}
