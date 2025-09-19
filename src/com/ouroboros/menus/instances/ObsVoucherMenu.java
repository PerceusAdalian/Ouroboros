package com.ouroboros.menus.instances;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;

public class ObsVoucherMenu extends AbstractOBSGui
{
	//Continue implementing for 1.0 release
	public ObsVoucherMenu(Player player) 
	{
		super(player, "Ouroboros Stat Voucher Menu", 54, Set.of(12,13,14,21,22,23,30,31,32,39,40,41,37,43));
	}

	@Override
	protected void build() 
	{

		GuiButton.button(Material.OAK_SIGN).setName("&7{&e&li&r&7}").setLore(
				"&r&bΩuroboros&f Statistical Vouchers are one-time use items that let you pick a stat,",
				"&r&fgiving enough &e&lXP&r&f to '&b&olevel-up&r&f' five &7(5)&f times.",
				"&r&fYou’ll be asked to confirm before use. Backing out or closing the menu",
				"&r&fwon’t consume the item. Vouchers are bound to you and cannot be traded.")
		.place(this, 4, e->
        {
	        Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_HANGING_SIGN_PLACE, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
        });
	
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
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
