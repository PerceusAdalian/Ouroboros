package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lol.wand.Wand;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.PrintUtils;

public class WandRechargePage extends AbstractOBSGui
{

	public WandRechargePage(Player player) 
	{
		super(player, "Wand Recharge Interface", 27, Set.of(13,10,16));
	}

	@Override
	protected void build() 
	{
		Wand wand = CollectWandData.wandCollector.get(player.getUniqueId());
		
		int rechargeCost = wand.getRechargeCost();
		int currentLuminite = PlayerData.getPlayer(player.getUniqueId()).getLuminite();
		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE)
		.setName("Confirm Wand Recharge")
		.setLore("&r&fClick to add &9Mana&f to your wand.","",
				"&r&fYour wand's current &9Mana&f: "+wand.getCurrentMana()+" &7/ "+wand.getMaxMana(),"",
				"&e&lRecharge Cost&r&f: "+rechargeCost+"&b۞",
				"&e&lYour Luminite&r&f: "+currentLuminite+"&b۞")
		.place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (currentLuminite < rechargeCost)
			{
				PrintUtils.OBSFormatError(p, "Insufficient Luminite to Recharge Wand. Please try again..");
				EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER);
				ItemStack returnedWand = wand.getAsItemStack();
				p.getInventory().addItem(returnedWand);
				CollectWandData.wandCollector.remove(p.getUniqueId());
	            CollectWandData.pageController.remove(p.getUniqueId());
	            GuiHandler.close(p);
	            return;
			}
			
			PrintUtils.OBSFormatPrint(p, "Wand Recharged!");
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_ACTIVATE, SoundCategory.AMBIENT);
			PlayerData.subtractLuminite(p, rechargeCost);
			wand.setMaxMana();
			ItemStack returnedWand = wand.getAsItemStack();
			p.getInventory().addItem(returnedWand);
			CollectWandData.wandCollector.remove(p.getUniqueId());
            CollectWandData.pageController.remove(p.getUniqueId());
            GuiHandler.close(p);
            return;
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to 'Wand Main Page'. Doing so will return your wand.").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			Wand storedWand = CollectWandData.wandCollector.get(p.getUniqueId());
			ItemStack returnedWand = storedWand.getAsItemStack();
			p.getInventory().addItem(returnedWand);
			CollectWandData.wandCollector.remove(p.getUniqueId());
            CollectWandData.pageController.remove(p.getUniqueId());
			GuiHandler.changeMenu(p, new WandMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to exit this menu. Doing so will return your wand.").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER);
			Wand storedWand = CollectWandData.wandCollector.get(p.getUniqueId());
			ItemStack returnedWand = storedWand.getAsItemStack();
			p.getInventory().addItem(returnedWand);
			CollectWandData.wandCollector.remove(p.getUniqueId());
			CollectWandData.pageController.remove(p.getUniqueId());
			GuiHandler.close(p);
		});
		
		paint();
	}

}
