package com.ouroboros;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.lol.spells.instances.aero.Fly;
import com.lol.spells.instances.arcano.Sigil;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.accounts.PlayerHud;
import com.ouroboros.accounts.RestoreArmorTask;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.ObsTimer;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.HeresioEffects;
import com.ouroboros.utils.entityeffects.helpers.WildcardData;

public class GeneralEvents implements Listener
{
	public static Map<UUID, Long> playerDeathRegistry = new HashMap<>();
	
	public static void register(JavaPlugin plugin) 
    {
        Bukkit.getPluginManager().registerEvents(new Listener() 
        {
        	@EventHandler 
        	public void onJoin(PlayerJoinEvent e) 
        	{
        		Player p = e.getPlayer();
        		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
        		
        		if (p.isOp()) 
        		{
        			p.setGameMode(GameMode.CREATIVE);
        		}
        		
        		if (!p.hasPlayedBefore()) 
        		{
        			PrintUtils.OBSFormatPrint(p, "Welcome to our server, "+p.getName()+"!");
        			PrintUtils.OBSFormatPrint(p, "Type &b&o/obs welcomkit&r&f to get first-timer goodies!");
        			PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oOnJoin&r&f -- &aOK&7 || &o"+p.getName()+" joined for the first time.");
        		}
        		else 
        		{        			
        			PrintUtils.OBSFormatPrint(p, "Welcome Back "+p.getName());
        			if (data != null && !data.hasKitClaimed())
        			{
        				PrintUtils.OBSFormatPrint(p, "You have an unclaimed &b&okit&r&f! Type &b&o/obs welcomekit&r&f to claim!");
        			}
        			PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oOnJoin&r&f -- &aOK&7 || &o"+p.getName()+" joined the server.");
        		}
        		
        		PlayerData.loadPlayer(p.getUniqueId());

        		if (data != null && data.getHP() <= 0) 
        		{
        		    data.setHP(data.getDefaultHP());
        		    data.setArmor(data.getDefaultArmor());
        		    data.setBreak(false);
        		    data.save();
        		}

        		Bukkit.getScheduler().runTaskLater(plugin, () -> PlayerData.syncVanillaHealth(p), 1L);

        		PlayerHud.create(p);
        	}
        	
        	@EventHandler
        	public void onDeath(PlayerDeathEvent e)
        	{
        	    Player p = e.getEntity();
        	    PlayerData data = PlayerData.getPlayer(p.getUniqueId());
        	    if (data != null)
        	    {
        	        data.setBreak(false);
        	        data.save();
        	    }
        	    playerDeathRegistry.put(p.getUniqueId(), System.currentTimeMillis());
        	}
        	
        	@EventHandler
        	public void itemUseEvent(PlayerInteractEvent e)
        	{
        		Player p = e.getPlayer();
        		
        		if (PlayerActions.rightClickAir(e) && p.getEquipment().getItemInMainHand().getType().equals(Material.MILK_BUCKET))
        		{
        			if (HeresioEffects.isHexed.containsKey(p.getUniqueId()))
        			{
        				e.setCancelled(true);
        				ObsParticles.drawDisc(p.getLocation(), p.getWidth(), 3, 15, 0.5, Particle.WARPED_SPORE, null);
						ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.SCULK_SOUL, null);
						ObsParticles.drawHeresioCastSigil(p);
						PrintUtils.PrintToActionBar(p, "&2&oThe Hex Worsens..");
						EntityEffects.playSound(p, Sound.ENTITY_WARDEN_HEARTBEAT, SoundCategory.MASTER);
						
        				WildcardData data = HeresioEffects.isHexed.get(p.getUniqueId());
						p.removePotionEffect(data.effect);
						EntityEffects.add(p, data.effect, PotionEffect.INFINITE_DURATION, data.magnitude == 9 ? 9 : data.magnitude+1, true);
						HeresioEffects.isHexed.put(p.getUniqueId(), new WildcardData(data.effect, data.magnitude == 9 ? 9 : data.magnitude+1));
						return;
        			}
        		}
        	}
        	
        	@EventHandler
        	public void playerRespawn(PlayerRespawnEvent e) 
        	{
        	    Player p = e.getPlayer();
        	    PlayerData data = PlayerData.getPlayer(p.getUniqueId());
        	    if (data == null) return;

        	    data.setHP(data.getDefaultHP());
        	    data.setArmor(data.getDefaultArmor());
        	    data.setBreak(false);
        	    data.save();
        	    playerDeathRegistry.remove(p.getUniqueId());

        	    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
        	    {
        	        if (p.isOnline())
        	        {
        	            PlayerData.syncVanillaHealth(p);
        	            PlayerHud.update(p);
        	        }
        	    }, 1L);

        	    if (HeresioEffects.isHexed.containsKey(p.getUniqueId()))
        		{
        			PrintUtils.PrintToActionBar(p, "&2&oThe Hex Came Back!");
        			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
        			{        				
        				ObsParticles.drawDisc(p.getLocation(), p.getWidth(), 3, 15, 0.5, Particle.WARPED_SPORE, null);
        				ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.SCULK_SOUL, null);
        				ObsParticles.drawHeresioCastSigil(p);
        				EntityEffects.playSound(p, Sound.ENTITY_WARDEN_HEARTBEAT, SoundCategory.MASTER);
        				
        				WildcardData wcData = HeresioEffects.isHexed.get(p.getUniqueId());
        				p.addPotionEffect(new PotionEffect(wcData.effect, PotionEffect.INFINITE_DURATION, wcData.magnitude, true, true, true));
        			}, 20);
        		}
        	}
        	
        	@EventHandler
        	public void onQuit(PlayerQuitEvent e) 
        	{
        		Player p = e.getPlayer();
        		PlayerData.unloadPlayer(p.getUniqueId());
        		
        		if (Sigil.awaitingTrade.containsKey(p.getUniqueId()))
        		{
        			Sigil.cancelTrade(p, Bukkit.getPlayer(Sigil.awaitingTrade.get(p.getUniqueId())));
        		}
        		if (Fly.flyers.containsKey(p.getUniqueId()))
        		{        			
        			p.setFlying(false);
        			p.setAllowFlight(Fly.flyers.get(p.getUniqueId()));
        			Fly.flyers.remove(p.getUniqueId());
        		}
        		
        		PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oOnQuit&r&f -- &aOK&7 || &o"+p.getName()+" left the server.");
        		ObsTimer.cancelCountdown(p);
        		RestoreArmorTask.lastHitTick.remove(p.getUniqueId());
        	}
        	
        }, plugin);
    }
}
