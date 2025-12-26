package com.ouroboros.menus.instances.magic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lol.wand.Wand;
import com.lol.wand.instances.BasicWand;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;

public class CraftableWandsView extends AbstractOBSGui
{

	public CraftableWandsView(Player player) 
	{
		super(player, "Craftable Wands", 27, Set.of(13,10,16));
	}

	public static Map<UUID, Wand> wandConfirmationMap = new HashMap<>();
	
	@Override
	protected void build() 
	{
		
		placeWandButton(player, new BasicWand(), 13, this);
		
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

	
	public static void placeWandButton(Player player, Wand wand, int slot, AbstractOBSGui gui)
	{
		PlayerData data = PlayerData.getPlayer(player.getUniqueId());
		
		wand.setSpell(null, 0);
		
		ItemStack stack = wand.getAsItemStack();
		ItemMeta meta = stack.getItemMeta();
		
		List<String> lore = new ArrayList<>();
		if (meta.getLore() != null) 
		{
			for (String line : meta.getLore())
			{
				lore.add(line);
			}
		}
		lore.add("");
		lore.add("&r&fClick to confirm craft.");
		lore.add("&e&lCraft Cost&r&f: 10&b۞");
		lore.add("&e&lYour Luminite&r&f: "+PlayerData.getPlayer(player.getUniqueId()));
		
		GuiButton.button(stack.getType()).setName("&7[&ei&7] &f&lWand Preview&r&f: "+stack.getItemMeta().getDisplayName()).setLore(lore).place(gui, slot, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			if (data.getLuminite() >= 10)
			{
				PlayerData.subtractLuminite(p, 10);
				OBSParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 10, 2, 0.5, Particle.ENCHANT, null);
				EntityEffects.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.AMBIENT);
				PrintUtils.OBSFormatDebug(p, stack.getItemMeta().getDisplayName()+ " Crafted Successfully!");
				p.getInventory().addItem(stack);
				GuiHandler.close(p);
				return;
			}
			PrintUtils.OBSFormatError(p, "Insufficient Luminite!");
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
	}
}
