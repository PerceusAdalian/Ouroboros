package com.ouroboros.objects.instances;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.ouroboros.Ouroboros;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsVoucherMenu;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;

public class ObsStatVoucher extends AbstractObsObject
{

	public ObsStatVoucher() 
	{
		super("&b&k&lo &r&bΩBS &eStat Voucher &b&k&lo", "obs_stat_voucher", Material.FLOWER_BANNER_PATTERN, true, false, 
				"&r&fUsage: &d&oShift_Right-Click&r&f to open the voucher menu.",
				"&r&fThis object is &c&lIndestructible&r&f.");
	}

	public static final NamespacedKey voucherKey = new NamespacedKey(Ouroboros.instance, "voucher_key");
	
	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (!PlayerActions.shiftRightClickAir(e)) return false;
		
		ItemStack held = e.getItem();
		ItemMeta meta = held.getItemMeta();
		if (meta.getPersistentDataContainer().get(voucherKey, PersistentDataType.STRING) != p.getUniqueId().toString())
		{
			PrintUtils.OBSFormatError(p, "This item is not bound to you. Owner: "+Bukkit.getPlayer(UUID.fromString(meta.getPersistentDataContainer().get(voucherKey, PersistentDataType.STRING))));
			return false;
		}
		
		GuiHandler.open(p, new ObsVoucherMenu(p));
		
		return true;
	}

}
