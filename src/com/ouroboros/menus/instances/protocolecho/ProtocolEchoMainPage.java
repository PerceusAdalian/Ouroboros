package com.ouroboros.menus.instances.protocolecho;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsMainMenu;

public class ProtocolEchoMainPage extends AbstractOBSGui
{

	public ProtocolEchoMainPage(Player player) 
	{
		super(player, "Protocol: Σ.C.H.O.", 27, Set.of(10,11,12,13,14,15,16));
	}

	@Override
	protected void build() 
	{
		
		GuiButton.button(Material.WRITABLE_BOOK).setName("&e&lEssence&r&f Reservoirs").setLore("Click to view your current &e&lEssence&r&f Reservoirs.").place(this, 15, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new EssenceReservoirsPage(p));
		});
		
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to the Main Page").place(this, 10, e->
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
