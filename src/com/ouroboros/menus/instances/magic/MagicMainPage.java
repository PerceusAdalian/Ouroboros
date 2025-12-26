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
		GuiButton.button(Material.STICK).setName("&b&lWand Menu").setLore("Click to view the wand menu. You can:",
				"   - &a&lCraft&r&f a new wand", 
				"   - &e&lUpgrade&r&7/&b&lRecharge&r&f an existing wand")
		.place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new WandMainPage(p));
		});
		
		GuiButton.button(Material.OAK_SIGN).setName("&7{&e&li&r&7}").setLore("This is the main Magic menu.",
				"Select &dWand Menu&f to manage all wand-related actions.",
				"Select &dSpell Menu&f to view, equip, or upgrade registered spells.",
				"",
				"Your &b&oLuminite Tears &r&7(&r&b۞&7)&f are shown on your &d&oHUD&r&f.",
				"&b&oLuminite Tears &r&7(&r&b۞&7)&f are required for all magic operations.",
				"Earn more by &ckilling mobs&f or leveling up &d&lMagic&r&f."
).place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			e.setCancelled(true);
		});

		GuiButton.button(Material.ENCHANTED_BOOK).setName("&d&lSpellbook").setLore("Click to view your spellbook. You can:",
				"   - &d&lView&r&7/&e&lUpgrade&r&f registered spells",
				"   - &a&lAdd&r&7/&c&lRemove&r&f spells to/from your wand").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new SpellBookPage(p));
		});
		
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Obs Main Page").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER, 1, 1);
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
