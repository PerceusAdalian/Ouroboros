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
import com.lol.wand.instances.Wand_3;
import com.lol.wand.instances.Wand_2;
import com.lol.wand.instances.Wand_1;
import com.lol.wand.instances.ArchmageStaff;
import com.lol.wand.instances.LuminaCatalyst;
import com.lol.wand.instances.Wand_4;
import com.lol.wand.instances.TwilightCatalyst;
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
		super(player, "Craftable Wands", 54, Set.of(11,12,13,14,15,30,32,37,43));
	}

	public static Map<UUID, Wand> wandConfirmationMap = new HashMap<>();
	
	@Override
	protected void build() 
	{
		
		placeWandButton(player, new Wand_1(), 11, this);
		placeWandButton(player, new Wand_2(), 12, this);
		placeWandButton(player, new Wand_3(), 13, this);
		placeWandButton(player, new Wand_4(), 14, this);
		placeWandButton(player, new ArchmageStaff(), 15, this);
		
		placeWandButton(player, new LuminaCatalyst(), 30, this);
		placeWandButton(player, new TwilightCatalyst(), 32, this);
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to 'Wand Main Page'").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new WandMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
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
		int craftCost = wand.getRarity().getRarity() * 10;
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
		lore.add("&e&lCraft Cost&r&f: "+craftCost+"&b۞");
		lore.add("&e&lYour Luminite&r&f: "+PlayerData.getPlayer(player.getUniqueId()).getLuminite());
		
		List<String> loreAlt = new ArrayList<>();
		loreAlt.add("&r&fThis wand is current locked due to lacking &d&oGnosis&r&f.");
		loreAlt.add("&r&fYour current &d&oGnosis Level&r&f: &d&l" + data.getMagicProficiency()+"&7/"+wand.getRarity().getRarity());
		loreAlt.add("&r&fIncrease &d&oGnosis &r&fby &e&nupgrading your current wands&r&f to unlock crafting.");
		
		boolean hasMagicRating = data.getMagicProficiency() >= wand.getRarity().getRarity();
		
		GuiButton.button(hasMagicRating ? stack.getType() : Material.BARRIER)
		.setName(hasMagicRating ? "&7[&ei&7] &f&lWand Preview&r&f: "+wand.getName() : "&r&fLocked Wand: "+wand.getName())
		.setLore(hasMagicRating ? lore : loreAlt)
		.place(gui, slot, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			if (data.getLuminite() >= craftCost)
			{
				PlayerData.subtractLuminite(p, craftCost);
				OBSParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 10, 2, 0.5, Particle.ENCHANT, null);
				EntityEffects.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.AMBIENT);
				PrintUtils.OBSFormatDebug(p, wand.getName()+ " Crafted Successfully!");
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
