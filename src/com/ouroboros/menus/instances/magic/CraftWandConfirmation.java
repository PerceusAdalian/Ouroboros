package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Particle;
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
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;

public class CraftWandConfirmation extends AbstractOBSGui
{

	public CraftWandConfirmation(Player player) 
	{
		super(player, "Confirm Craft", 27, Set.of(10,13,16));
	}

	@Override
	protected void build() 
	{
		Wand wand = CraftableWandsView.wandConfirmationMap.get(player.getUniqueId());
		ItemStack stack = wand.getAsItemStack();
		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE).setName("&a&lConfirm Craft&r&f: &n"+stack.getItemMeta().getDisplayName()+"&r&f?").setLore("&e&lCraft Cost&r&f: 10&b۞", 
				"&e&lYour Luminite&r&f: "+PlayerData.getPlayer(player.getUniqueId()).getLuminite()+"&b۞").place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
			if (data.getLuminite() >= 10)
			{
				PlayerData.subtractLuminite(p, 10);
				OBSParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 10, 2, 0.5, Particle.ENCHANT, null);
				EntityEffects.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.AMBIENT);
				PrintUtils.OBSFormatDebug(p, stack.getItemMeta().getDisplayName()+ " Crafted Successfully!");
				p.getWorld().dropItemNaturally(p.getLocation(), stack);
				GuiHandler.close(p);
			}
			PrintUtils.OBSFormatError(p, "Insufficient Luminite!");
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to 'Craftable Wands'").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new CraftableWandsView(p));
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
