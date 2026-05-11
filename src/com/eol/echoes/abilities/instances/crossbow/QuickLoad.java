package com.eol.echoes.abilities.instances.crossbow;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;

public class QuickLoad extends EchoAbility
{

	public QuickLoad() 
	{
		super("Quick Load", "quick_load", Material.CROSSBOW, StatType.RANGED, 5, 1, 0, AbilityType.UTILITY, ElementType.MODULO, CastConditions.LEFT_CLICK_AIR, EchoForm.CROSSBOW, 
				"&r&fQuickly reload the next available &b&oarrow&r&f into this &b&oCrossbow&r&f.");
	}

	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		if (item.getType() != Material.CROSSBOW) return -1;

		CrossbowMeta meta = (CrossbowMeta) item.getItemMeta();
		Inventory inv = p.getInventory();

		if (!inv.contains(Material.ARROW)) return -1;
		for (ItemStack itemStack : inv.getContents())
		{
		    if (itemStack == null || !itemStack.getType().equals(Material.ARROW)) continue;

		    ItemStack projectile = new ItemStack(Material.ARROW, 1);

		    if (itemStack.getAmount() > 1) itemStack.setAmount(itemStack.getAmount() - 1);
		    else inv.remove(itemStack);

		    meta.addChargedProjectile(projectile);
		    break;
		}

		item.setItemMeta(meta);
		return 0;
	}

	@Override
	public int getFinalDurabilityCost() 
	{
		
		return 0;
	}

}
