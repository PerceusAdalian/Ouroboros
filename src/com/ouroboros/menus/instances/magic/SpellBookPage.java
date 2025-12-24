package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;

public class SpellBookPage extends AbstractOBSGui
{

	public SpellBookPage(Player player) 
	{
		super(player, "Spellbook", 54, Set.of(20,12,22,21,14,23,32,24,30,37,43));
	}

	@Override
	protected void build() 
	{
		GuiButton.button(Material.FIRE_CHARGE).setName("&c&lInferno&r&e Spells").setLore("Click to navigate to all &c&lInferno&r&e Spells.").place(this, 20, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new InfernoSpellsPage(p));
		});

		GuiButton.button(Material.SNOWBALL).setName("&b&lGlacio&r&e Spells").setLore("Click to navigate to all &b&lGlacio&r&e Spells.").place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			
		});

		GuiButton.button(Material.BRICK).setName("&6&lGeo&r&e Spells").setLore("Click to navigate to all &6&lGeo&r&e Spells.").place(this, 21, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			
		});

		GuiButton.button(Material.WIND_CHARGE).setName("&d&lAero&r&e Spells").setLore("Click to navigate to all &d&lAero&r&e Spells.").place(this, 30, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			
		});

		GuiButton.button(Material.NETHER_STAR).setName("&e&lCelestio&r&e Spells").setLore("Click to navigate to all &e&lCelestio&r&e Spells.").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			
		});

		GuiButton.button(Material.ECHO_SHARD).setName("&3&lCosmo&r&e Spells").setLore("Click to navigate to all &3&lCosmo&r&e Spells.").place(this, 23, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			
		});

		GuiButton.button(Material.WITHER_ROSE).setName("&4&lMortio&r&e Spells").setLore("Click to navigate to all &4&lMortio&r&e Spells.").place(this, 32, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			
		});
		
		GuiButton.button(Material.WRITABLE_BOOK).setName("&fPerks &r&fand &bUtility &aSpells").setLore("Click to navigate to all &fPerks and &bUtility &eSpells.").place(this, 24, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			
		});
		
		GuiButton.button(Material.FLOWER_BANNER_PATTERN).setName("&eSpecial Spells").setLore("Click to navigate to all &eSpecial Spells").place(this, 22, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to 'Magic Main Page'").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new MagicMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.close(p);
		});
		
		paint();
	}

}
