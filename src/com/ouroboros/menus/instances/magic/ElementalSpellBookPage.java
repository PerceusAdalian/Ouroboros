package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.enums.ObsColors;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ElementalSpellBookPage extends ObsGui
{

	public ElementalSpellBookPage(Player player) 
	{
		super(player, "Elemental Spellbook", 54, Set.of(13,20,21,22,23,24,31,37,40,43));
	}

	@Override
	protected void build() 
	{
		GuiButton.button(Material.FIRE_CHARGE).setName(PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&e Spells")
		.setLore("Click to navigate to all "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&e Spells&f.").place(this, 20, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new InfernoSpellsPage(p));
		});

		GuiButton.button(Material.SNOWBALL).setName(PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&e Spells")
		.setLore("Click to navigate to all "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&e Spells&f.").place(this, 21, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new GlacioSpellsPage(p));
		});

		GuiButton.button(Material.BRICK).setName(PrintUtils.color(ObsColors.GEO)+"&lGeo&r&e Spells")
		.setLore("Click to navigate to all "+PrintUtils.color(ObsColors.GEO)+"&lGeo&r&e Spells&f.").place(this, 23, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new GeoSpellsPage(p));
		});

		GuiButton.button(Material.WIND_CHARGE).setName(PrintUtils.color(ObsColors.AERO)+"&lAero&r&e Spells")
		.setLore("Click to navigate to all "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&e Spells&f.").place(this, 24, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new AeroSpellsPage(p));
		});

		GuiButton.button(Material.NETHER_STAR).setName(PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&e Spells")
		.setLore("Click to navigate to all "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&e Spells&f.").place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new CelestioSpellsPage(p));
		});

		GuiButton.button(Material.ECHO_SHARD).setName(PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&e Spells")
		.setLore("Click to navigate to all "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&e Spells&f.").place(this, 22, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new CosmoSpellsPage(p));
		});

		GuiButton.button(Material.WITHER_ROSE).setName(PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&e Spells")
		.setLore("Click to navigate to all "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&e Spells&f.").place(this, 31, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new MortioSpellsPage(p));
		});
		
		GuiButton.button(Material.ENDER_EYE).setName(PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&e Spells")
		.setLore("Click to navigate to all "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&e spells&f.").place(this, 40, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new HeresioSpellsPage(p));
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to 'Spellbook Main Page'").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_BOOK_PUT, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new SpellBookMainPage(p));
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
