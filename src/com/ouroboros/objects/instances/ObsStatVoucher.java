package com.ouroboros.objects.instances;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.ouroboros.Ouroboros;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsVoucherMenu;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;

public class ObsStatVoucher extends AbstractObsObject
{

	public ObsStatVoucher() 
	{
		super("&r&bΩBS &eStat Voucher", "obs_stat_voucher", Material.FLOWER_BANNER_PATTERN, true, false, 
				"&r&fUsage: &d&oShift_Right-Click&r&f to open the voucher menu.",
				"&r&fThis object is &c&lIndestructible&r&f.");
	}

	public static final NamespacedKey voucherKey = new NamespacedKey(Ouroboros.instance, "voucher_key");
	public static Map<UUID, ItemStack> voucherRegistry = new HashMap<>();
	
	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (!PlayerActions.shiftRightClickAir(e)) return false;
		
		ItemStack held = e.getItem();
		ItemMeta meta = held.getItemMeta();
		
		String uuid = meta.getPersistentDataContainer().get(voucherKey, PersistentDataType.STRING);

		if (uuid == null) 
		{
		    PrintUtils.OBSFormatError(p, "This voucher has no owner bound; Adding your credentials.. Please try again.");
		    meta.getPersistentDataContainer().set(voucherKey, PersistentDataType.STRING, p.getUniqueId().toString());
		    held.setItemMeta(meta);
		    return true;
		}
		
		if (!UUID.fromString(uuid).equals(p.getUniqueId()))
		{
			PrintUtils.OBSFormatError(p, "This item is not bound to you. Owner: "+Bukkit.getPlayer(UUID.fromString(uuid)).getName());
			return false;
		}
		
		OBSParticles.drawCylinder(p.getLocation(), p.getWidth(), 4, 8, 0.5, 0.25, Particle.ENCHANT, null);
		OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 1, 8, 0.5, Particle.CLOUD, null);
		EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_PICKUP_ENCHANTED, SoundCategory.MASTER, 1, 1);
		
		voucherRegistry.put(UUID.fromString(uuid), held);
		GuiHandler.open(p, new ObsVoucherMenu(p));
		
		return true;
	}

}
