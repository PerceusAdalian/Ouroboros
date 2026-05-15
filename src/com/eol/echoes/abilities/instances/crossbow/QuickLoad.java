package com.eol.echoes.abilities.instances.crossbow;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.entityeffects.EntityEffects;

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
		if (!playAbilityEffect(p, item)) return -1;
		return 0;
	}

	@Override
	public int getFinalDurabilityCost() 
	{
		
		return 0;
	}
	
	public static boolean playAbilityEffect(Player p, ItemStack item)
	{
		if (item.getType() != Material.CROSSBOW) return false;
		
		CrossbowMeta meta = (CrossbowMeta) item.getItemMeta();
		Inventory inv = p.getInventory();

		if (!inv.contains(Material.ARROW)) return false;
		for (ItemStack itemStack : inv.getContents())
		{
		    if (itemStack == null || !itemStack.getType().equals(Material.ARROW)) continue;

		    ItemStack projectile = new ItemStack(Material.ARROW, 1);

		    if (itemStack.getAmount() > 1) itemStack.setAmount(itemStack.getAmount() - 1);
		    else inv.remove(itemStack);

		    meta.addChargedProjectile(projectile);
		    break;
		}
		EntityEffects.playSound(p, Sound.ITEM_CROSSBOW_QUICK_CHARGE_3, SoundCategory.AMBIENT);
		item.setItemMeta(meta);
		return true;
	}

}
