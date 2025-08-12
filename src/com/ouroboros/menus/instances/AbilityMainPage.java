package com.ouroboros.menus.instances;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.ability.instances.combat.Flamelash;
import com.ouroboros.ability.instances.combat.GeminiSlash;
import com.ouroboros.ability.instances.combat.ImbueFire;
import com.ouroboros.ability.instances.perks.RejuvenateWounds;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;

public class AbilityMainPage extends AbstractOBSGui
{

	public AbilityMainPage(Player player) 
	{
		super(player, "Abilities", 54, Set.of(10,11,12,14,37,43));
	}

	public static Map<Player, AbstractOBSAbility> abilityConfirmMap = new HashMap<>();
	public static Map<Player, Boolean> confirmRegister = new HashMap<>();
	
	@Override
	protected void build() 
	{
		//Abilities First Page
		
		AbstractOBSAbility imbuefire = new ImbueFire();
		ItemStack imbuefirestack = imbuefire.toIcon(player);
		GuiButton.button(imbuefirestack.getType()).setName(imbuefirestack.getItemMeta().getDisplayName()).setLore(imbuefirestack.getItemMeta().getLore()).place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (PlayerData.canRegister(p.getUniqueId(), imbuefire)) 
			{
				abilityConfirmMap.put(p, imbuefire.getInstance());
				confirmRegister.put(p, true);
				GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
			}
			else if (PlayerData.getPlayer(p.getUniqueId()).getAbility(imbuefire).isRegistered()) 
			{
				abilityConfirmMap.put(p, imbuefire.getInstance());
				confirmRegister.put(p, false);
				GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
			}
			else
			{
				e.setCancelled(true);
				p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			}
		});
		
		AbstractOBSAbility flamelash = new Flamelash();
		ItemStack flamelashStack = flamelash.toIcon(player);
		GuiButton.button(flamelashStack.getType()).setName(flamelashStack.getItemMeta().getDisplayName()).setLore(flamelashStack.getItemMeta().getLore()).place(this, 11, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (PlayerData.canRegister(p.getUniqueId(), flamelash)) 
			{
				abilityConfirmMap.put(p, flamelash.getInstance());
				confirmRegister.put(p, true);
				GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
			}
			
			else if (PlayerData.getPlayer(p.getUniqueId()).getAbility(flamelash).isRegistered()) 
			{
				abilityConfirmMap.put(p, flamelash.getInstance());
				confirmRegister.put(p, false);
				GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
			}
			else
			{
				e.setCancelled(true);
				p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			}
		});
		
		AbstractOBSAbility geminislash = new GeminiSlash();
		ItemStack geminislashstack = geminislash.toIcon(player);
		GuiButton.button(geminislashstack.getType()).setName(geminislashstack.getItemMeta().getDisplayName()).setLore(geminislashstack.getItemMeta().getLore()).place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (PlayerData.canRegister(p.getUniqueId(), geminislash)) 
			{
				abilityConfirmMap.put(p, geminislash.getInstance());
				confirmRegister.put(p, true);
				GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
			}
			
			else if (PlayerData.getPlayer(p.getUniqueId()).getAbility(geminislash).isRegistered()) 
			{
				abilityConfirmMap.put(p, geminislash.getInstance());
				confirmRegister.put(p, false);
				GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
			}
			else
			{
				e.setCancelled(true);
				p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			}	
		});
		
		AbstractOBSAbility regenperk = new RejuvenateWounds();
		ItemStack regenperkstack = regenperk.toIcon(player);
		GuiButton.button(regenperkstack.getType()).setName(regenperkstack.getItemMeta().getDisplayName()).setLore(regenperkstack.getItemMeta().getLore()).place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (PlayerData.canRegister(p.getUniqueId(), regenperk)) 
			{
				abilityConfirmMap.put(p, regenperk.getInstance());
				confirmRegister.put(p, true);
				GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
			}
			
			else if (PlayerData.getPlayer(p.getUniqueId()).getAbility(regenperk).isRegistered()) 
			{
				abilityConfirmMap.put(p, regenperk.getInstance());
				confirmRegister.put(p, false);
				GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
			}
			else
			{
				e.setCancelled(true);
				p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			}	
		});
		
		//Exits
		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE).setName("&a&lGo Back").setLore("Click to return to 'Obs Main Page'").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ObsMainMenu(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to exit").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
	}

}
