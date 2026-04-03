package com.ouroboros.menus.instances.protocolecho;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.ouroboros.utils.InventoryUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class RefinementResultsPage extends AbstractOBSGui
{
	private final Map<ItemStack, Integer> results;
	private final int xp;
	
	public RefinementResultsPage(Player player, Map<ItemStack, Integer> results, int xp) 
	{
		super(player, "Protocol α - Refinement Results", 27, Set.of());
		this.results = results;
		this.xp = xp;
	}

	private static final List<Integer> RESULT_SLOTS = List.of(
		    10, 11, 12, 13, 14, 15);
	
	@Override
	protected void build() 
	{
		if (results.isEmpty())
		{
			GuiHandler.changeMenu(player, new RefinementPage(player));
		}
		
		int slotIndex = 0;
		for (Map.Entry<ItemStack, Integer> entry : results.entrySet())
		{
			if (slotIndex >= RESULT_SLOTS.size()) break;
			int slot = RESULT_SLOTS.get(slotIndex++);
			
			ItemStack item = entry.getKey().clone();
			item.setAmount(entry.getValue());
			
			GuiButton.button(item.getType())
			.setName(item.getItemMeta().getDisplayName())
			.setLore(item.getItemMeta().getLore())
			.place(this, slot, e->
			{
				e.setCancelled(true);
				Player p = (Player) e.getWhoClicked();
				InventoryUtils.add(p, item);
				results.remove(entry.getKey());
				
				PlayerData data = PlayerData.getPlayer(p.getUniqueId());
				boolean doXpNotif = data.doLevelUpSound();
				data.doXpNotification(false);
				PlayerData.addXP(p, StatType.REFINEMENT, xp);
				EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT);
				data.doXpNotification(doXpNotif);
				
				getInventory().clear();
				build();
			});
		}
		
		List<String> lore = new ArrayList<>();
		lore.add("&r&fClick to collect all:");
		for (ItemStack stack : results.keySet())
		{
			lore.add(stack.getItemMeta().getDisplayName() + " &r&fx&b&l"+ stack.getAmount());
		}
		lore.add("&r&fPlease ensure enough room is available before claiming.");
		
		GuiButton.button(Material.CHEST).setName("&a&lCollect All&r&f 📥").setLore(lore)
		.place(this, 16, e->
		{
			e.setCancelled(true);
			Player p = (Player) e.getWhoClicked();
			results.forEach((item, amount) ->
			{
				ItemStack result = item.clone();
				result.setAmount(amount); 
				InventoryUtils.add(p, result);
			});
			results.clear();
			
			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
			boolean doXpNotif = data.doLevelUpSound();
			data.doXpNotification(false);
			PlayerData.addXP(p, StatType.REFINEMENT, xp);
			EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.AMBIENT);
			data.doXpNotification(doXpNotif);
			
			GuiHandler.changeMenu(p, new RefinementPage(p));
		});
		
		paint();
	}

}
