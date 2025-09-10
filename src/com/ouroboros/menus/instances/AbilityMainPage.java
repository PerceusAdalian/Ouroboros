package com.ouroboros.menus.instances;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.ouroboros.abilities.AbilityRegistry;
import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;

public class AbilityMainPage extends AbstractOBSGui
{

	public AbilityMainPage(Player player) 
	{
		super(player, "Abilities", 54, Set.of(10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43));
	}

	public static Map<Player, AbstractOBSAbility> abilityConfirmMap = new HashMap<>();
	public static Map<Player, Boolean> confirmRegister = new HashMap<>();
	
	@Override
	protected void build() 
	{
		renderAbilities(player, whitelistedSlots);
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Obs Main Page").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ObsMainMenu(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		
		paint();
	}

	public void renderAbilities(Player player, Set<Integer> whitelistedSlots) 
	{
	    Iterator<Integer> validSlots = whitelistedSlots.iterator();
	    
	    for (AbstractOBSAbility ability : AbilityRegistry.abilityRegistry.values()) 
	    {
	        if (!validSlots.hasNext()) break;
	        int slot = validSlots.next();
	        if (slot == 37 || slot == 43) slot = validSlots.next();
	        placeAbilityButton(player, ability, slot);
	    }
	}
	
	private void placeAbilityButton(Player player, AbstractOBSAbility ability, int slot) 
	{
	    ItemStack stack = ability.toIcon(player);

	    GuiButton.button(stack.getType())
	        .setName(stack.getItemMeta().getDisplayName())
	        .setLore(stack.getItemMeta().getLore())
	        .place(this, slot, e -> 
	        {
	            Player p = (Player) e.getWhoClicked();
	            handleAbilityClick(p, ability, e);
	        });
	}
	
	private void handleAbilityClick(Player p, AbstractOBSAbility ability, InventoryClickEvent e)
	{
		if (PlayerData.canRegister(p.getUniqueId(), ability)) 
		{
	        abilityConfirmMap.put(p, ability.getInstance());
	        confirmRegister.put(p, true);
	        GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
	    }
	    else if (PlayerData.getPlayer(p.getUniqueId()).getAbility(ability).isRegistered()) 
	    {
	        abilityConfirmMap.put(p, ability.getInstance());
	        confirmRegister.put(p, false);
	        GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
	    }
	    else 
	    {
	        e.setCancelled(true);
	        p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
	    }
	}
	
}

