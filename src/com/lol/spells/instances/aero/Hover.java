package com.lol.spells.instances.aero;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Hover extends Spell
{

	public Hover()
	{
		super("Hover", "hover", Material.FEATHER, SpellType.UTILITY, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 50, 1, false,
				"&r&fSuspends &6self&f in mid-air for up to 30 seconds. Touching ground",
				"&r&for recasting triggers a slow descent and doesn't cost &b&lMana&r&f.","",
				"&r&bEchoic Dissonance&r&f: natural effect expiry sets cooldown to &b&o15 seconds&r&f.");
	}

	private static Set<UUID> cooldown = new HashSet<>();
	public static final Map<UUID, HoverSession> activeSessions = new HashMap<>();

	public record HoverSession(BukkitTask task, double hoverY, boolean hadFlight, boolean wasFlying) {}
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		
		if (cooldown.contains(uuid)) return -1;
		EntityEffects.playSound(p, Sound.ENTITY_BLAZE_AMBIENT, SoundCategory.AMBIENT);
		
		double hoverY = p.getLocation().getY();
		boolean hadFlight = p.getAllowFlight();
		boolean wasFlying = p.isFlying();
		
		if (activeSessions.containsKey(uuid))
		{
			endSession(uuid, false, hadFlight, wasFlying);
			return 0;
		}

		p.setVelocity(new Vector(0,0,0));
		
		int[] ticksElapsed = {0};
		BukkitTask task = Bukkit.getScheduler().runTaskTimer(Ouroboros.instance, ()->
		{
			if (!p.isOnline()) 
			{
				endSession(uuid, false, hadFlight, wasFlying);
				return;
			}
			
			ticksElapsed[0]++;
			double currentY = p.getLocation().getY();
			double difference = currentY - hoverY;
			
			OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 1, 5, 0.5, Particle.EXPLOSION, null);
			OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 1, 5, 0.5, Particle.POOF, null);
			
			if (Math.abs(difference) > 0.15)
			{
				Vector v = p.getVelocity();
				double correction = -difference * 0.3;
				p.setVelocity(new Vector(v.getX(), correction, v.getZ()));
			}
			else
			{
				Vector v = p.getVelocity();
				p.setVelocity(new Vector(v.getX(), 0, v.getZ()));
			}
			
			Block b = p.getLocation().subtract(0, 0.1, 0).getBlock();
			if (!p.isOnline() || !b.getType().isAir() && b.getType().isSolid()) 
			{
				endSession(uuid, false, hadFlight, wasFlying);
				return;
			}
			
			if (ticksElapsed[0] >= 300)
			{
				endSession(uuid, true, hadFlight, wasFlying);
				return;
			}
			
		}, 0L, 5L);
		
		activeSessions.put(uuid, new HoverSession(task, hoverY, hadFlight, wasFlying));
		
		return 50;
	}

	@Override
	public int getTotalManaCost()
	{
		return 0;
	}

	public static void endSession(UUID uid, boolean applyPenalty, boolean hadFlight, boolean wasFlying) 
	{
        HoverSession session = activeSessions.remove(uid);
        if (session != null) session.task().cancel();
        
        Player p = Bukkit.getPlayer(uid);
        if (p == null || !p.isOnline()) return;

        p.setFlying(false);
        p.setAllowFlight(hadFlight);

        softLand(p);

        if (applyPenalty) 
        {
        	cooldown.add(uid);
        	Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> cooldown.remove(uid), 300);
        }
    }
	
	private static void softLand(Player p) 
	{
		Bukkit.getScheduler().runTaskTimer(Ouroboros.instance, ()->
		{
			double force = 0.08 * 6;
			Block b = p.getLocation().subtract(0, 0.1, 0).getBlock();
			if (!p.isOnline() || !b.getType().isAir() && b.getType().isSolid()) return;
			
			force -= 0.08;
            if (force <= 0) return;
            
            Vector vel = p.getVelocity();
            p.setVelocity(new Vector(vel.getX(), force * 0.5, vel.getZ()));

		}, 0L, 3L);
    }
	
	public static void registerSpellHelper(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
		    public void onFlightToggle(PlayerToggleFlightEvent e) 
			{
		        UUID uid = e.getPlayer().getUniqueId();
		        if (Hover.activeSessions.containsKey(uid)) e.setCancelled(true);
		    }
			
			@EventHandler
		    public void onMove(PlayerMoveEvent e) 
			{
		        UUID uid = e.getPlayer().getUniqueId();
		        Hover.HoverSession session = Hover.activeSessions.get(uid);
		        if (session == null) return;

		        if (e.getFrom().getY() == e.getTo().getY()) return;

		        double diff = e.getTo().getY() - session.hoverY();
		        if (Math.abs(diff) > 1.5) e.getTo().setY(session.hoverY());
		    }
			
			@EventHandler
		    public void onQuit(PlayerQuitEvent e) 
			{
		        UUID uid = e.getPlayer().getUniqueId();
		        Hover.HoverSession session = Hover.activeSessions.get(uid);
		        if (session != null) Hover.endSession(uid, false, session.hadFlight(), session.wasFlying());
		    }
		}, plugin);
	}
	
}
