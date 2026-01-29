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

import com.ouroboros.accounts.PlayerData;
import com.ouroboros.hud.PlayerHud;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.EntityEffects.WildcardData;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;

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
        			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
        			if (data != null && !data.hasKitClaimed())
        			{
        				PrintUtils.OBSFormatPrint(p, "You have an unclaimed &b&okit&r&f! Type &b&o/obs welcomekit&r&f to claim!");
        			}
        			PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oOnJoin&r&f -- &aOK&7 || &o"+p.getName()+" joined the server.");
        		}
        		
        		PlayerData.loadPlayer(p.getUniqueId());
        		PlayerHud.createHud(p);
        	}
        	
        	@EventHandler
        	public void onDeath(PlayerDeathEvent e)
        	{
        		playerDeathRegistry.put(e.getEntity().getUniqueId(), System.currentTimeMillis());
        	}
        	
        	@EventHandler
        	public void itemUseEvent(PlayerInteractEvent e)
        	{
        		Player p = e.getPlayer();
        		
        		if (PlayerActions.rightClickAir(e) && p.getEquipment().getItemInMainHand().getType().equals(Material.MILK_BUCKET))
        		{
        			if (EntityEffects.isHexed.containsKey(p.getUniqueId()))
        			{
        				e.setCancelled(true);
        				OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 3, 15, 0.5, Particle.WARPED_SPORE, null);
						OBSParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.SCULK_SOUL, null);
						OBSParticles.drawHeresioCastSigil(p);
						PrintUtils.PrintToActionBar(p, "&2&oThe Hex Worsens..");
						EntityEffects.playSound(p, Sound.ENTITY_WARDEN_HEARTBEAT, SoundCategory.MASTER);
						
        				WildcardData data = EntityEffects.isHexed.get(p.getUniqueId());
						p.removePotionEffect(data.effect);
						EntityEffects.add(p, data.effect, PotionEffect.INFINITE_DURATION, data.magnitude == 9 ? 9 : data.magnitude+1, true);
						EntityEffects.isHexed.put(p.getUniqueId(), new WildcardData(data.effect, data.magnitude == 9 ? 9 : data.magnitude+1));
						return;
        			}
        		}
        	}
        	
        	@EventHandler
        	public void playerRespawn(PlayerRespawnEvent e)
        	{
        		Player p = e.getPlayer();
        		if (EntityEffects.isHexed.containsKey(p.getUniqueId()))
        		{
        			PrintUtils.PrintToActionBar(p, "&2&oThe Hex Came Back!");
        			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
        			{        				
        				OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 3, 15, 0.5, Particle.WARPED_SPORE, null);
        				OBSParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.SCULK_SOUL, null);
        				OBSParticles.drawHeresioCastSigil(p);
        				EntityEffects.playSound(p, Sound.ENTITY_WARDEN_HEARTBEAT, SoundCategory.MASTER);
        				
        				WildcardData data = EntityEffects.isHexed.get(p.getUniqueId());
        				p.addPotionEffect(new PotionEffect(data.effect, PotionEffect.INFINITE_DURATION, data.magnitude, true, true, true));
        			}, 20);
        		}
        	}
        	
        	@EventHandler
        	public void onQuit(PlayerQuitEvent e) 
        	{
        		Player p = e.getPlayer();
        		PlayerData.unloadPlayer(p.getUniqueId());
        		PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oOnQuit&r&f -- &aOK&7 || &o"+p.getName()+" left the server.");
        	}
        	
        }, plugin);
    }
}
