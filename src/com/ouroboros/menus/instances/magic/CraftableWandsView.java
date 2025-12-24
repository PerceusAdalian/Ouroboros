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
import com.lol.wand.instances.BasicWand;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;

public class CraftableWandsView extends AbstractOBSGui
{

	public CraftableWandsView(Player player) 
	{
		super(player, "Craftable Wands", 27, Set.of(10,16));
	}

	public static Map<UUID, Wand> wandConfirmationMap = new HashMap<>();
	
	@Override
	protected void build() 
	{
		Wand wand = new BasicWand();
		ItemStack stack = wand.getAsItemStack();
		GuiButton.button(stack.getType()).setName("&7[&ei&7] &f&lWand Preview&r&f: "+stack.getItemMeta().getDisplayName()).setLore(stack.getItemMeta().getLore()).place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			wandConfirmationMap.put(p.getUniqueId(), wand);
			GuiHandler.changeMenu(p, new CraftWandConfirmation(p));
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to 'Wand Main Page'").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new WandMainPage(p));
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
