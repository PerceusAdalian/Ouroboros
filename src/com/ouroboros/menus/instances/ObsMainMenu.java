package com.ouroboros.menus.instances;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.ObsCommand;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.menus.instances.abilities.AbilitiesMainPage;
import com.ouroboros.menus.instances.magic.CollectWandData;
import com.ouroboros.menus.instances.magic.ElementalSpellBookPage;
import com.ouroboros.menus.instances.magic.SpecialSpellBookPage;
import com.ouroboros.menus.instances.magic.WandMainPage;
import com.ouroboros.menus.instances.protocolecho.EchoRepairPage;
import com.ouroboros.menus.instances.protocolecho.ProtocolEchoMainPage;
import com.ouroboros.menus.instances.protocolecho.RefinementPage;
import com.ouroboros.menus.instances.store.ObsShopGui;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ObsMainMenu extends ObsGui
{

	public ObsMainMenu(Player player) 
	{
		super(player, "Ouroboros Main Menu", 54, Set.of());
	}

	@Override
	protected void build() 
	{
		// Magic
		GuiButton.button(Material.STICK).setName("&b&lWand Menu").setLore("Click to view the wand menu. You can:",
				"   - &a&lCraft&r&f a new wand", 
				"   - &e&lUpgrade&r&7/&b&lRecharge&r&f an existing wand")
		.place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new WandMainPage(p));
		});

		GuiButton.button(Material.BOOK).setName("&e&lPrimary School &r&eSpells").setLore("Click to view all elemental &r&espells&f.").place(this, 21, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ElementalSpellBookPage(p));
		});
		
		GuiButton.button(Material.WRITTEN_BOOK).setName("&b&lSecondary School &r&eSpells").setLore("Click to view all unique &earcana&f.").place(this, 30, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new SpecialSpellBookPage(p));
		});

		
		GuiButton.button(Material.NETHER_STAR).setName("&d&lRecharge &r&fExisting Wand").setLore("Click to recharge an existing wand").place(this, 39, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			CollectWandData.pageController.put(p.getUniqueId(), "recharge");
			GuiHandler.changeMenu(p, new CollectWandData(p));
		});
		
		// Protocol Echo (Mid)
		GuiButton.button(Material.CRAFTER).setName("&bProtocol&f: &e&lΣ&r&f&l.C.H.O.").setLore("&r&fClick to view the &bProtocol&f: &e&lΣ&r&f&l.C.H.O. Menus").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ProtocolEchoMainPage(p));
		});
		
		GuiButton.button(Material.NETHER_STAR).setName("&c&lAbilities").setLore("&r&fClick to view available abilities").place(this, 23, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new AbilitiesMainPage(p));
		});
		
		GuiButton.button(Material.ANVIL).setName("&e&lProtocol &bδ &r&f- &b&oRepair Echoes&r&f").setLore("Click to &arepair&f a damaged &b&lEcho&r&f.").place(this, 32, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_SMITHING_TABLE_USE, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new EchoRepairPage(p));	
		});

		GuiButton.button(Material.BREWING_STAND).setName("&e&lProtocol &aα &r&f- &b&oRefinement").setLore("Click to &b&orefine&r&f raw materials into valid &bMateria").place(this, 41, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_WORK_MASON, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new RefinementPage(p));
		});
		
		// Msc Pages
		GuiButton.button(Material.SPYGLASS).setName("&b&l"+Symbols.OBS+"uroboros Statistical Inquiry").setLore("&r&fClick to run an inquiry on your stats.").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.AMBIENT);
			ObsCommand.printStats(p);
			GuiHandler.close(p);
		});
		
		GuiButton.button(Material.VILLAGER_SPAWN_EGG).setName("&e&lShop").setLore("&r&fClick to view and shop for &b&oServer-Side&r&f wares.").place(this, 25, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ObsShopGui(p));
		});
		
		GuiButton.button(Material.ENCHANTED_BOOK).setName("&b&lCodex").setLore("&r&fClick to view the official in-game codex").place(this, 34, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new CodexMainPage(p));
		});
		
		//Exits
		GuiButton.placeExit(37, this);
		GuiButton.placeExit(43, this);
		
		paint();
	}
}
