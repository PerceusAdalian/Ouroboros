package com.ouroboros.menus.instances.protocolecho;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.eol.enums.EchoForm;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ArmorForgeFormTargetPage extends ObsGui
{
	private ItemStack stack;
	
	public ArmorForgeFormTargetPage(Player player, ItemStack stack) 
	{
		super(player, "Armor "+Symbols.EOL+"cho Form Select", 27, Set.of(10,16));
		this.stack = stack;
	}

	@Override
	protected void build() 
	{
		
		GuiButton.button(Material.DARK_OAK_SIGN)
		.setName("&7[&ei&7] &bProtocol &e&lΣ&r&f - &oEchoic Catalyzation of Harmonic Objects &r&7(&e&lΣ&r&f.C.H.O&7)")
		.setLore("&r&d&oForm Calibration &r&f--",
				"&r&fBefore forging an &6Armor &b"+Symbols.EOL+"cho &fPiece, choose one of the",
				"&r&favailable &d&oForms&r&f below. You'll then proceed to the &e&lForge Page",
				"&r&fand may continue forging as normal.")
		.place(this, 4, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
		});
		
		GuiButton.button(Material.NETHERITE_HELMET)
		.setName("&r&d&lForm Calibration&r&f: &6Helmet")
		.setLore("&r&fClick to proceed with chosen &b&o"+Symbols.EOL+"cho Form&r&f.")
		.place(this, 11, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new ArmorForgePage(p, stack, EchoForm.HELMET));
		});
		
		GuiButton.button(Material.NETHERITE_CHESTPLATE)
		.setName("&r&d&lForm Calibration&r&f: &6Chestplate")
		.setLore("&r&fClick to proceed with chosen &b&o"+Symbols.EOL+"cho Form&r&f.")
		.place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new ArmorForgePage(p, stack, EchoForm.CHESTPLATE));
		});
		
		GuiButton.button(Material.NETHERITE_LEGGINGS)
		.setName("&r&d&lForm Calibration&r&f: &6Leggings")
		.setLore("&r&fClick to proceed with chosen &b&o"+Symbols.EOL+"cho Form&r&f.")
		.place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new ArmorForgePage(p, stack, EchoForm.LEGGINGS));
		});
		
		GuiButton.button(Material.NETHERITE_BOOTS)
		.setName("&r&d&lForm Calibration&r&f: &6Boots")
		.setLore("&r&fClick to proceed with chosen &b&o"+Symbols.EOL+"cho Form&r&f.")
		.place(this, 15, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new ArmorForgePage(p, stack, EchoForm.BOOTS));
		});

		//Exits
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
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
