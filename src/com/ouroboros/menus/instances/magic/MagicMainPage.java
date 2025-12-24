package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsMainMenu;
import com.ouroboros.utils.EntityEffects;

public class MagicMainPage extends AbstractOBSGui
{

	public MagicMainPage(Player player) 
	{
		super(player, "Magic Main Page", 27, Set.of(12,13,14,10,16));
	}

	@Override
	protected void build() 
	{
		GuiButton.button(Material.STICK).setName("&b&lWand Menu").setLore("Click to view the wand menu. You can:","   - Craft a new wand", "   - Upgrade an existing wand", "   - Or recharge an existing wand")
		.place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new WandMainPage(p));
		});
		
		GuiButton.button(Material.OAK_SIGN).setName("&7{&e&li&r&7}").setLore("This is the main page for magic.", 
				"Click on the 'Wand Menu' option for all wand-based operations.",
				"Alternatively, click on the 'Spell Menu' option to view/set/upgrade all currently registered spells.",
				"You can also view your &b&oLuminite Tears &r&7(&r&b۞&7)&f on your &d&oHud&r&f.", 
				"Please note that &b&oLuminite Tears &r&7(&r&b۞&7)&f are a necessary currency for all operations.",
				"More can be earned by &ckilling mobs&f, or earning them from leveling &d&lMagic&r&f.").place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			e.setCancelled(true);
		});

		GuiButton.button(Material.ENCHANTED_BOOK).setName("&d&lSpellbook").setLore("Click to view, set, and upgrade spells to your wand").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new SpellBookPage(p));
		});
		
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Obs Main Page").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_INSERT_ENCHANTED, SoundCategory.MASTER, 1, 1);
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
