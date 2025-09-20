package com.ouroboros.menus.instances;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.StatType;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.objects.instances.ObsStatVoucher;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.PrintUtils;

public class VoucherConfirmPage extends AbstractOBSGui
{

	public VoucherConfirmPage(Player player) 
	{
		super(player, "Voucher Confirm", 27, Set.of(10, 13, 16));
	}

	@Override
	protected void build() 
	{
		StatType sType = ObsVoucherMenu.voucherConfirm.get(player.getUniqueId());
		PlayerData data = PlayerData.getPlayer(player.getUniqueId());
		
		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE).setName("&a&lConfirm").setLore(
				"&r&a&lClick to confirm&r&f incrementing your &b&l"+sType.getFancyKey()+" &r&fstat by 5 levels.",
				"Doing so will use up this voucher. You may return to the previous page or exit to cancel.",
				"&f&lNote&r&f: &c&lThis action cannot be undone&r&f. Using the voucher while your &b&l"+sType.getFancyKey(),
				"&r&f is at &oLevel 100 will &c&lNOT&r&f result in consumption.","",
				"&r&fYour current &b&l"+sType.getFancyKey()+" &r&eLevel&f: &l"+data.getStat(sType, true))
		.place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.MASTER, 1, 1);
			if (data.getStat(sType, true) == 100)
			{
				ObsVoucherMenu.voucherConfirm.remove(p.getUniqueId());
				GuiHandler.changeMenu(p, new ObsVoucherMenu(p));
			}
			
			data.doXpNotification(false);
			data.doLevelUpSound(false);
			
			int prelevel = data.getStat(sType, true);
			
			PlayerData.incrementLevel(p, sType, 5);

			int abilityPoints = data.getAbilityPoints();
			
			ItemStack stack = ObsStatVoucher.voucherRegistry.get(p.getUniqueId());
			stack.setAmount(stack.getAmount() - 1);

			EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 1);
			PrintUtils.OBSFormatDebug(p, "Your "+sType.getFancyKey()+" stat has leveled up! | &7Lvl "+prelevel+" &r&7-> "+ "&f&lLvl &r&b&l" + data.getStat(sType, true)+"\n"+
					"&e&l+&f5&6AP&r&f | &nCurrent Ability Points&r&f: &6" + abilityPoints);
			
			ObsStatVoucher.voucherRegistry.remove(p.getUniqueId());
			
			GuiHandler.close(p);
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Ability Main Page").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			
			ObsVoucherMenu.voucherConfirm.remove(p.getUniqueId());
			
			GuiHandler.changeMenu(p, new ObsVoucherMenu(p));
		}); 
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			
			ObsStatVoucher.voucherRegistry.remove(p.getUniqueId());
			ObsVoucherMenu.voucherConfirm.remove(p.getUniqueId());
			
			GuiHandler.close(p);
		});
		paint();
	}
	
}
