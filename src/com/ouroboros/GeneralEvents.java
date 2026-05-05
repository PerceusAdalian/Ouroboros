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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
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
import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.ObsTimer;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GeoEffects;
import com.ouroboros.utils.entityeffects.HeresioEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;
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
        		if (HeresioEffects.isHexed.containsKey(p.getUniqueId()))
        		{
        			PrintUtils.PrintToActionBar(p, "&2&oThe Hex Came Back!");
        			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
        			{        				
        				ObsParticles.drawDisc(p.getLocation(), p.getWidth(), 3, 15, 0.5, Particle.WARPED_SPORE, null);
        				ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.SCULK_SOUL, null);
        				ObsParticles.drawHeresioCastSigil(p);
        				EntityEffects.playSound(p, Sound.ENTITY_WARDEN_HEARTBEAT, SoundCategory.MASTER);
        				
        				WildcardData data = HeresioEffects.isHexed.get(p.getUniqueId());
        				p.addPotionEffect(new PotionEffect(data.effect, PotionEffect.INFINITE_DURATION, data.magnitude, true, true, true));
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
        	}
        	
        	@EventHandler
        	public void playerDamage(EntityDamageEvent e)
        	{
        		if (e.getEntity() instanceof Player p)
        		{
        		    double finalDamage = e.getFinalDamage();

        		    if (GeoEffects.isBarbed.containsKey(p.getUniqueId()) && e.getDamageSource() instanceof LivingEntity le) // Mitigation on barbed
        		    {
        		        int barbedDamage = GeoEffects.isBarbed.get(p.getUniqueId());
        		        MobData.damageUnnaturally(p, le, barbedDamage, false, true, ElementType.GEO);
        		        finalDamage = Math.max(0, finalDamage - barbedDamage);
        		    }

        		    if (InfernoEffects.hasInfernalBody.contains(p.getUniqueId()) && e.getDamageSource() instanceof LivingEntity le)
        		    {
        		    	InfernoEffects.addBurn(le, 10);
        		    }
        		    
        		    if (GeoEffects.guarded_registry.containsKey(p.getUniqueId())) // Remove guarded stack
        		    {
        		        finalDamage *= 0.5;
        		        GeoEffects.subGuarded(p);
        		    }
        		    
        		    e.setDamage(finalDamage); // final damage for the player while guard is up + barbed is {finalDamage = (finalDamage - barbedDamage) * 0.5}
        		}
        		if (e.getCause().equals(DamageCause.FALL) && e.getEntity() instanceof Player p)
        		{
        			double initialFallDamage = e.getFinalDamage();
        			double mitigatedFallDamage = 0;
        			
        			if (MortioEffects.nightShifted.containsKey(p.getUniqueId()))
        			{
        				double finalFallDamage = initialFallDamage * (0.1 * MortioEffects.nightShifted.get(p.getUniqueId()));
        				e.setDamage(finalFallDamage);
        				mitigatedFallDamage += initialFallDamage - finalFallDamage;
        			}
        			
        			if (Ouroboros.debug)
        			{
        				PrintUtils.OBSConsoleDebug("Entity Damage Event: Fall Damage -- "+p.getName()+ ": took "+initialFallDamage+" fall damage."
        						+"\nPlayer was Night-Shifted: "+MortioEffects.nightShifted.containsKey(p.getUniqueId()) + " | Mitigated "+mitigatedFallDamage+" damage.");
        			}
        		}
        		
        	}
        	
        }, plugin);
    }
}
