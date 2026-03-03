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
		super(player, "Spellbook", 54, Set.of(13,20,21,22,23,24,31,37,39,41,43));
	}

	@Override
	protected void build() 
	{
		GuiButton.button(Material.FIRE_CHARGE).setName("&c&lInferno&r&e Spells").setLore("Click to navigate to all &c&lInferno&r&e Spells&f.").place(this, 20, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new InfernoSpellsPage(p));
		});

		GuiButton.button(Material.SNOWBALL).setName("&b&lGlacio&r&e Spells").setLore("Click to navigate to all &b&lGlacio&r&e Spells&f.").place(this, 21, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new GlacioSpellsPage(p));
		});

		GuiButton.button(Material.BRICK).setName("&6&lGeo&r&e Spells").setLore("Click to navigate to all &6&lGeo&r&e Spells&f.").place(this, 23, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new GeoSpellsPage(p));
		});

		GuiButton.button(Material.WIND_CHARGE).setName("&d&lAero&r&e Spells").setLore("Click to navigate to all &d&lAero&r&e Spells&f.").place(this, 24, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new AeroSpellsPage(p));
		});

		GuiButton.button(Material.NETHER_STAR).setName("&e&lCelestio&r&e Spells").setLore("Click to navigate to all &e&lCelestio&r&e Spells&f.").place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new CelestioSpellsPage(p));
		});

		GuiButton.button(Material.ECHO_SHARD).setName("&3&lCosmo&r&e Spells").setLore("Click to navigate to all &3&lCosmo&r&e Spells&f.").place(this, 22, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new CosmoSpellsPage(p));
		});

		GuiButton.button(Material.WITHER_ROSE).setName("&4&lMortio&r&e Spells").setLore("Click to navigate to all &4&lMortio&r&e Spells&f.").place(this, 31, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new MortioSpellsPage(p));
		});
		
		GuiButton.button(Material.ENDER_EYE).setName("&2&lHeresio&r&e Spells").setLore("Click to navigate to all &2&lHeresio&r&e spells&f.").place(this, 39, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new HeresioSpellsPage(p));
		});
		
		GuiButton.button(Material.END_CRYSTAL).setName("&f&b&oArcano&r&e Spells").setLore("Click to navigate to all &f&lArcano&r&e spells&f.").place(this, 41, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new ArcanoSpellsPage(p));
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to 'Magic Main Page'").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_BOOK_PUT, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new MagicMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_BOOK_PUT, SoundCategory.MASTER);
			GuiHandler.close(p);
		});
		
		paint();
	}

}
