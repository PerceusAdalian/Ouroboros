package com.ouroboros.menus.instances;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.cosmo.Gate;
import com.lol.wand.Wand;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.GateCodes;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.magic.CollectWandData;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;

public class GateMenu extends AbstractOBSGui
{

	public GateMenu(Player player) 
	{
		super(player, "Gate - Location Selector", 27, Set.of(10, 16));
	}

	@Override
	protected void build() 
	{
		Wand wand = CollectWandData.wandCollector.get(player.getUniqueId());
		
		GuiButton.button(Material.GRASS_BLOCK)
		.setName("&a&lOverworld")
		.setLore("&r&fClick to &3&oteleport&r&f to your saved &a&lOverworld&r&f location.")
		.place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.AMBIENT);
			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
			Location overworldLoc = data.getGate(GateCodes.OVERWORLD);
			
			if (data != null && overworldLoc != null) 
			{		
				p.teleport(overworldLoc);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					PrintUtils.PrintToActionBar(p, "&7&oYou've arrived in a &a&ofamiliar world&r&7&o..");
					OBSParticles.drawCosmoCastSigil(p);
					EntityEffects.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT);
				}, 25);
				
				cleanup(p, wand);
			}
		});
		
		GuiButton.button(Material.NETHERRACK)
		.setName("&c&lNether")
		.setLore("&r&fClick to &3&oteleport&r&f to your saved &c&lNether&r&f location.")
		.place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.AMBIENT);
			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
			Location netherLoc = data.getGate(GateCodes.NETHER);
			
			if (data != null && netherLoc != null) 
			{				
				p.teleport(netherLoc);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					PrintUtils.PrintToActionBar(p, "&7&oYou've arrived in &c&oHell&r&7&o..");
					OBSParticles.drawCosmoCastSigil(p);
					EntityEffects.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT);
					EntityEffects.playSound(p, Sound.AMBIENT_NETHER_WASTES_ADDITIONS, SoundCategory.AMBIENT);
				}, 25);
				
				cleanup(p, wand);
			}
		});
		
		GuiButton.button(Material.END_STONE)
		.setName("&3&lThe End")
		.setLore("&r&fClick to &3&oteleport&r&f to your saved &3&lEnd&r&f location.")
		.place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.AMBIENT);
			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
			Location endLoc = data.getGate(GateCodes.END);

			if (data != null && endLoc != null)
			{				
				p.teleport(endLoc);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					PrintUtils.PrintToActionBar(p, "&7&oYou've arrived in a &3&oforeign dimension&r&f..");
					OBSParticles.drawCosmoCastSigil(p);
					EntityEffects.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT);
					EntityEffects.playSound(p, Sound.ENTITY_ENDERMAN_AMBIENT, SoundCategory.AMBIENT);
				}, 25);
				
				cleanup(p, wand);
			}
		});
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to close this menu. Doing so will return your wand.").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			p.getInventory().addItem(wand.getAsItemStack());
			CollectWandData.wandCollector.remove(p.getUniqueId());
			GuiHandler.close(p);
		});
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to close this menu. Doing so will return your wand.").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			p.getInventory().addItem(wand.getAsItemStack());
			CollectWandData.wandCollector.remove(p.getUniqueId());
			GuiHandler.close(p);
		});
		paint();
	}

	private static void cleanup(Player player, Wand wand)
	{
		wand.subtractMana(200);
		player.getInventory().addItem(wand.getAsItemStack());
		CollectWandData.wandCollector.remove(player.getUniqueId());
		if (Ouroboros.debug == false)
		{
			Gate.cooldown.add(player.getUniqueId());
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> Gate.cooldown.remove(player.getUniqueId()), 1200);
		}
		GuiHandler.close(player);
	}
	
}
