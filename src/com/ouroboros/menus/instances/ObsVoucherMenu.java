package com.ouroboros.menus.instances;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.enums.StatType;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;

public class ObsVoucherMenu extends AbstractOBSGui
{
	//Continue implementing for 1.0 release
	public ObsVoucherMenu(Player player) 
	{
		super(player, "Ouroboros Stat Voucher Menu", 54, Set.of(12,13,14,21,22,23,30,31,32,39,40,41,37,43));
	}

	public static Map<UUID, StatType> voucherConfirm = new HashMap<>();
	
	@Override
	protected void build() 
	{

		GuiButton.button(Material.OAK_SIGN).setName("&7{&e&li&r&7}").setLore(
				"&r&f&lUsage&r&f: &r&bΩBS &eStat Vouchers&r&f are one-time use items that let you pick a stat, ",
				"&r&fgiving enough &e&lXP&r&f to &b&olevel-up&r&f &lFive&r &7(5)&f times.",
				"&r&f&nYou’ll be asked to confirm before consumption&r&f. ",
				"&c&oVouchers are bound to you and cannot be traded&r&f.","",
				"&r&f&lNote&r&f: Backing out or closing the menu won’t consume the item.")
		.place(this, 4, e->
        {
	        Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_HANGING_SIGN_PLACE, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
        });
		
		GuiButton.button(Material.FILLED_MAP).setName("&b&lTravel")
		.place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_GRASS_STEP, SoundCategory.MASTER, 1, 1);

			voucherConfirm.put(p.getUniqueId(), StatType.TRAVEL);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.CRAFTING_TABLE).setName("&b&lCrafting")
		.place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CRAFTER_CRAFT, SoundCategory.MASTER, 1, 1);

			voucherConfirm.put(p.getUniqueId(), StatType.CRAFTING);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.BREWING_STAND).setName("&b&lAlchemy")
		.place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, SoundCategory.MASTER, 1, 1);

			voucherConfirm.put(p.getUniqueId(), StatType.ALCHEMY);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.WOODEN_AXE).setName("&b&lWoodcutting")
		.place(this, 21, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_WOOD_BREAK, SoundCategory.MASTER, 1, 1);
			
			voucherConfirm.put(p.getUniqueId(), StatType.WOODCUTTING);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.DIAMOND_PICKAXE).setName("&b&lMining")
		.place(this, 22, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_STONE_BREAK, SoundCategory.MASTER, 1, 1);

			voucherConfirm.put(p.getUniqueId(), StatType.MINING);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.FISHING_ROD).setName("&b&lFishing")
		.place(this, 23, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.MASTER, 1, 1);

			voucherConfirm.put(p.getUniqueId(), StatType.FISHING);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.IRON_HOE).setName("&b&lFarming")
		.place(this, 30, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_HOE_TILL, SoundCategory.MASTER, 1, 1);

			voucherConfirm.put(p.getUniqueId(), StatType.FARMING);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.ENCHANTED_BOOK).setName("&b&lEnchanting")
		.place(this, 31, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1, 1);

			voucherConfirm.put(p.getUniqueId(), StatType.ENCHANTING);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.BRUSH).setName("&b&lDiscovery")
		.place(this, 32, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_BRUSH_BRUSHING_GENERIC, SoundCategory.MASTER, 1, 1);

			voucherConfirm.put(p.getUniqueId(), StatType.DISCOVERY);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.NETHERITE_SWORD).setName("&c&lMelee")
		.place(this, 39, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER, 1, 1);

			voucherConfirm.put(p.getUniqueId(), StatType.MELEE);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.BOW).setName("&a&lRanged")
		.place(this, 40, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, SoundCategory.MASTER, 1, 1);

			voucherConfirm.put(p.getUniqueId(), StatType.RANGED);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.NETHER_STAR).setName("&d&lMagic")
		.place(this, 41, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_BOTTLE_FILL_DRAGONBREATH, SoundCategory.MASTER, 1, 1);

			voucherConfirm.put(p.getUniqueId(), StatType.MAGIC);
			GuiHandler.changeMenu(p, new VoucherConfirmPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
        
		paint();
	}

}
