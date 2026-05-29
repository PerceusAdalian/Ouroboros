package com.ouroboros.menus.instances.protocolecho;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.menus.instances.ObsMainMenu;
import com.ouroboros.utils.Symbols;

public class ProtocolEchoMainPage extends ObsGui
{

	public ProtocolEchoMainPage(Player player) 
	{
		super(player, "Protocol: Σ.C.H.O.", 27, Set.of(10,11,12,13,14,15,16));
	}

	@Override
	protected void build() 
	{
		
		GuiButton.button(Material.PHANTOM_MEMBRANE).setName("&e&lProtocol &6Φ &r&f- &b&oScrap").setLore("Click to &6&oScrap&r&f "+Symbols.EOL+"choic objects.").place(this, 11, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_SMITHING_TABLE_USE, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ScrapPage(p));
		});
		
		GuiButton.button(Material.ANVIL).setName("&e&lProtocol &bδ &r&f- &b&oRepair Echoes&r&f").setLore("Click to &arepair&f a damaged &b&lEcho&r&f.").place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_SMITHING_TABLE_USE, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new EchoRepairPage(p));	
		});

		GuiButton.button(Material.BREWING_STAND).setName("&e&lProtocol &aα &r&f- &b&oRefinement").setLore("Click to &b&orefine&r&f raw materials into valid &bMateria").place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_WORK_MASON, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new RefinementPage(p));
		});
		
		GuiButton.button(Material.LECTERN).setName("&e&lProtocol &dγ &r&f- &b&oCore Crafting").setLore("Click to refine &e&lEssence&r&f into respective &e&lCores&r&f.").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CRAFTER_CRAFT, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new CoreCraftingPage(p));
		});
		
		GuiButton.button(Material.WRITABLE_BOOK).setName("&e&lEssence&r&f Reservoirs").setLore("Click to view your current &e&lEssence&r&f Reservoirs.").place(this, 15, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new EssenceReservoirsPage(p));
		});

		//Exits
		GuiButton.placeGoBack(10, this, new ObsMainMenu(player));
		GuiButton.placeExit(16, this);
		
		paint();
	}

}
