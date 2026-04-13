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
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class WandCraftPage extends ObsGui
{

	public WandCraftPage(Player player) 
	{
		super(player, "Craftable Wands", 27, Set.of(10,12,14,16));
	}

	public static Map<UUID, Wand> wandConfirmationMap = new HashMap<>();
	
	@Override
	protected void build() 
	{
		
		GuiButton.button(Material.STICK).setName("&fMain Progression Wands").setLore("Click to view all craftable ordinary wands.").place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new WandCraftNormalPage(p));
		});
		
		GuiButton.button(Material.STICK).setName("&e&oAlternative &r&fWands").setLore("Click to view all craftable &e&oalternative&r&f wands.").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new WandCraftAlternativePage(p));
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

	public static void placeWandButton(Player player, Wand wand, int slot, ObsGui gui)
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
		if (wand.getElementType() != null)
		{
			ElementType eType = wand.getElementType();
			lore.add("&e&lCraft Cost&r&f: "+craftCost+"&b۞&f, "+craftCost/2+PrintUtils.getElementTypeColor(eType)+"⚛&r&f, "+craftCost/3+"&6♻");
			lore.add("&fYour &eLuminite&f: "+PlayerData.getPlayer(player.getUniqueId()).getLuminite());
			lore.add("&fYour "+PrintUtils.getElementTypeColor(eType)+eType.getType()+"&r&e Essence&f: "+PrintUtils.getElementTypeColor(eType)+data.getEssence(eType));
			lore.add("&fYour &6Scrap&f: "+data.getScrap()+"&6♻");
		}
		else
		{			
			lore.add("&e&lCraft Cost&r&f: "+craftCost+"&b۞&f");
			lore.add("&e&lYour Luminite&r&f: "+PlayerData.getPlayer(player.getUniqueId()).getLuminite());
		}
		
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
			if (wand.getElementType() != null)
			{
				if (data.getLuminite() >= craftCost && data.getScrap() >= craftCost / 3 && data.getEssence(wand.getElementType()) >= craftCost / 2)
				{
					PlayerData.subtractLuminite(p, craftCost);
					PlayerData.subtractScrap(p, craftCost / 3);
					PlayerData.subtractEssence(p, wand.getElementType(), craftCost / 2);
					
					OBSParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 10, 2, 0.5, Particle.ENCHANT, null);
					EntityEffects.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.AMBIENT);
					PrintUtils.OBSFormatDebug(p, wand.getName()+ " Crafted Successfully!");
					p.getInventory().addItem(stack);
					GuiHandler.changeMenu(p, new WandMainPage(p));
					return;
				}
				else
				{
					PrintUtils.OBSFormatError(p, "Insufficient Materials!");
					p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
					GuiHandler.close(p);
				}
			}
			else if (wand.getElementType() == null && data.getLuminite() >= craftCost)
			{
				PlayerData.subtractLuminite(p, craftCost);
				OBSParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 10, 2, 0.5, Particle.ENCHANT, null);
				EntityEffects.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.AMBIENT);
				PrintUtils.OBSFormatDebug(p, wand.getName()+ " Crafted Successfully!");
				p.getInventory().addItem(stack);
				GuiHandler.changeMenu(p, new WandMainPage(p));
				return;
			}
			PrintUtils.OBSFormatError(p, "Insufficient Luminite!");
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
	}
}
