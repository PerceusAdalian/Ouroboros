package com.eol.echoes;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;

import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.ouroboros.utils.TimeUtils;
import com.ouroboros.utils.TimeUtils.Timeframe;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class EchoHeldEvent
{
	
	public static void register(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void onItemHeld(PlayerItemHeldEvent e)
			{
			    Player p = e.getPlayer();
			    PlayerInventory inv = p.getInventory();

			    // Remove effects from previously held item
			    ItemStack prev = inv.getItem(e.getPreviousSlot());
			    if (EchoManager.isEcho(prev))
			    {
			        EchoManifest prevCodec = EchoManager.getCodec(prev);
			        if (prevCodec != null)
			        {
			        	for (Modifier mod : prevCodec.getPassiveModifiers())
			        	{
			        		if (mod.condition().satisfies(p, null, p.getWorld()))
			        		{
			        			ResolveEchoInteract.removeHeldEffects((PassiveModifier) mod, p);
			        		}
			        	}
			        }
			    }
			    
			    // Add effects from newly held item
			    ItemStack next = inv.getItem(e.getNewSlot());
			    if (EchoManager.isEcho(next))
			    {
			        EchoManifest nextCodec = EchoManager.getCodec(next);
			        if (nextCodec != null)
			        {
			        	for (Modifier mod : nextCodec.getPassiveModifiers())
			        	{
			        		if (mod.condition().satisfies(p, null, p.getWorld()))
			        		{			            		
			        			ResolveEchoInteract.resolveHeldEffects((PassiveModifier) mod, p);
			        		}
			        	}
			        }
			    }
			}
			
			@EventHandler
			public void onItemDrop(PlayerDropItemEvent e)
			{
				Player p = e.getPlayer();
				
				ItemStack stack = e.getItemDrop().getItemStack();
				if (!EchoManager.isEcho(stack)) return;
				
				EchoManifest codec = EchoManager.getCodec(stack);
				if(codec == null) return;
				
				for (Modifier mod : codec.getPassiveModifiers())
				{
					ResolveEchoInteract.removeHeldEffects((PassiveModifier) mod, p);
				}
			}
			
			@EventHandler
			public void onItemPickup(EntityPickupItemEvent e)
			{
				if (!(e.getEntity() instanceof Player p)) return;
				ItemStack stack = e.getItem().getItemStack();
				if (!EchoManager.isEcho(stack)) return;
				
				Bukkit.getScheduler().runTaskLater(plugin, () ->
			    {
			        ItemStack held = p.getInventory().getItemInMainHand();
			        if (!EchoManager.isEcho(held)) return;
			        EchoManifest codec = EchoManager.getCodec(held);
			        if (codec == null) return;
			        for (Modifier mod : codec.getPassiveModifiers())
			            ResolveEchoInteract.resolveHeldEffects((PassiveModifier) mod, p);
			    }, 1L);
			}
			
			@EventHandler
			public void onQuit(PlayerQuitEvent e)
			{
				ResolveEchoInteract.clearEffects(e.getPlayer());
			}

			@EventHandler
			public void onDeath(PlayerDeathEvent e)
			{
				ResolveEchoInteract.clearEffects(e.getEntity());
			}
			
		}, plugin);
	}
	
	@SuppressWarnings("null")
	public static void queueBuffTask(Plugin plugin)
	{
		Bukkit.getScheduler().runTaskTimer(plugin, () ->
	    {
	        for (Player p : Bukkit.getOnlinePlayers())
	        {
	            UUID uuid = p.getUniqueId();
	            if (uuid == null) return;
	            
	            if (ResolveEchoInteract.increase_movement_speed.contains(uuid))
	            	EntityEffects.add(p, PotionEffectType.SPEED, 100, 0, false);
	            
	            if (ResolveEchoInteract.has_nimble.contains(uuid))
	            	EntityEffects.add(p, PotionEffectType.JUMP_BOOST, 100, 0, false);
	            
	            if (ResolveEchoInteract.decrease_movement_speed.contains(uuid))
	            	EntityEffects.add(p, PotionEffectType.SLOWNESS, 100, 0, false);
	            
	            if (ResolveEchoInteract.has_protected.contains(uuid))
	            	EntityEffects.add(p, PotionEffectType.RESISTANCE, 100, 0, false);
	            
	            if (ResolveEchoInteract.nightsight.contains(uuid) && TimeUtils.checkTime(p.getWorld(), Timeframe.NIGHT))
	            	EntityEffects.add(p, PotionEffectType.NIGHT_VISION, 400, 0, false);
	            
	        }
	    }, 0, 20);
	}
	
}
