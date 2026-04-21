package com.ouroboros.menus.instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.lol.spells.instances.cosmo.Warp;
import com.lol.wand.Wand;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.menus.instances.magic.CollectWandData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.ObsTimer;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.WarpData;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class WarpMenu extends ObsGui
{
	private final Wand wand;
    private final boolean isDeleting;

	public WarpMenu(Player player, Wand wand, boolean isDeleting)
	{
		super(player, "Waypoint Selection", 27, Set.of(10,11,12,13,14,15,16));
		this.wand = wand;
		this.isDeleting = isDeleting;
	}

	
	@Override
	protected void build()
	{
		placeWarpDataButtons(player, this, wand, isDeleting);
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to close this menu.", "Doing so returns your wand if applicable.").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (!isDeleting)
			{
				p.getInventory().addItem(wand.getAsItemStack());
				CollectWandData.wandCollector.remove(p.getUniqueId());
			}
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to close this menu.", "Doing so returns your wand if applicable.").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (!isDeleting)
			{
				p.getInventory().addItem(wand.getAsItemStack());
				CollectWandData.wandCollector.remove(p.getUniqueId());
			}
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
	}

	private static void placeWarpDataButtons(Player player, ObsGui gui, Wand wand, boolean isDeleting)
	{
	    PlayerData data = PlayerData.getPlayer(player.getUniqueId());
	    int[] validSlots = {11, 12, 13, 14, 15};

	    for (int i = 0; i < validSlots.length; i++) // fix: < not <=
	    {
	        int guiSlot = validSlots[i];
	        int warpIndex = i + 1; // fix: 1-indexed (1–5)

	        if (data.getWarpData(warpIndex) == null)
	        {
	        	String lore = isDeleting
	        			? "&r&fThis Warp Core's Data is Null. Deletion Impossible."
	        			: "&r&fClick to set your current location to this Warp Core.";
	        	
	            GuiButton.button(Material.ENDER_PEARL)
                .setName("&r&fEmpty Warp Core #" + warpIndex)
                .setLore(lore)
                .place(gui, guiSlot, e ->
                {
                    Player p = (Player) e.getWhoClicked();
                 
                    if (!isDeleting)
                    {
                    	GuiHandler.close(player);
                    	Warp.awaitingWarpNickname.put(p.getUniqueId(), warpIndex);
                    	PrintUtils.Print(player, "&f&lWarp Core &b#&e&l" + warpIndex + "&r&f — Provide a nickname for your current location:");
                    	PrintUtils.Print(player, "&7(Max 16 characters. Type in chat to confirm.)");                    	
                    }
                    else
                    {
                    	e.setCancelled(true);
                    	EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.AMBIENT);
                    }
                });
	        }
	        else
	        {
	        	WarpData wData = data.getWarpData(warpIndex);
	        	
	        	List<String> lore = new ArrayList<>();
	        	if (isDeleting) lore.add("&r&fWarp Data already exists for this core. Click to initialize?");
	        	else
	        	{
	        	    lore.add("&r&fClick to &3teleport&f to this Warp Core's saved location.");
	        	    lore.add("&r&fNickname: " + PrintUtils.color(ObsColors.COSMO) + wData.getNickname());
	        	}

	        	GuiButton.button(Material.ENDER_EYE)
	        	.setName("&r&fInitialized Warp Core #" + warpIndex)
	        	.setLore(lore)
	        	.place(gui, guiSlot, e ->
	        	{
	        		Player p = (Player) e.getWhoClicked();
	        		if (isDeleting)
	        		{
	        			EntityEffects.playSound(p, Sound.ENTITY_ENDER_EYE_DEATH, SoundCategory.AMBIENT);
	        			data.clearWarpData(warpIndex);
	        			reload(p, wand);
	        		}
	        		else
	        		{
	        			if (wand.getCurrentMana() < 100)
	        			{
	        				EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.AMBIENT);
	        				e.setCancelled(true);
	        			}
	        			else
	        			{
	        				e.setCancelled(true);
	        				GuiHandler.close(p);
	        				
	        				if (CollectWandData.wandCollector.containsKey(p.getUniqueId()))
	        				{
	        					wand.subtractMana(100);
	        				    p.getInventory().addItem(wand.getAsItemStack());
	        				    CollectWandData.wandCollector.remove(p.getUniqueId());
	        				}
	        				p.getWorld().loadChunk(wData.getLocation().getChunk());
	        				
	        				EntityEffects.playSound(p, Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, SoundCategory.AMBIENT);
	        				PrintUtils.OBSFormatDebug(p, "&r&3Warp Core Data&f successfully loaded -- &3&lTeleporting&r&f shortly..");

	        				ObsTimer.startCountdown(p, 10, Ouroboros.instance, r ->
	        				{
	        					if (!r.isOnline() || r.isDead()) return;
	        					
	        					ObsParticles.drawCylinder(r.getLocation(), r.getWidth(), 4, 10, 0.5, 0.5, Particle.LARGE_SMOKE, null);
	        					r.teleport(wData.getLocation());
	        					
	        					Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
	        					{
	        						ObsParticles.drawCosmoCastSigil(r);
	        						EntityEffects.playSound(r, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, SoundCategory.AMBIENT);
	        					}, 10);
	        				});
	        			}
	        		}
	        	});
	        }
	    } 
	}
	
	/**
	 * @Note Only for reloading warp cell deletion.
	 */
	private static void reload(Player player, Wand wand)
	{
		GuiHandler.close(player);
		CollectWandData.wandCollector.put(player.getUniqueId(), wand);
		GuiHandler.open(player, new WarpMenu(player, wand, true));
	}
	
}
