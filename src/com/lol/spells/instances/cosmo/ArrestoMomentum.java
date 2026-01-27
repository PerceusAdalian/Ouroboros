package com.lol.spells.instances.cosmo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.OBStandardTimer;
import com.ouroboros.utils.RayCastUtils;

public class ArrestoMomentum extends Spell implements Listener
{

	public ArrestoMomentum() 
	{
		super("Arresto Momentum", "arresto_momentum", Material.ENDER_PEARL, SpellType.CONTROL, SpellementType.COSMO, CastConditions.SHIFT_RIGHT_CLICK_AIR, Rarity.TWO, 50, 3, true,
				"&r&fFreezes target lifeform within &b&o20 meters&r&7 (20s | &cPVP&7: &c5s&7)","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&3&oHalt Life&r&7&o', however, colloquially known as '&r&3&oArresto Momentum&r&7&o'.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = (Player) e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 20);
		if (!(target instanceof LivingEntity) || target == null) return false;
		
		OBSParticles.drawCosmoCastSigil(p);
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 3, 0.5, Particle.SONIC_BOOM, null);
		OBSParticles.drawDisc(target.getLocation(), target.getWidth(), 2, 10, 0.5, Particle.GLOW_SQUID_INK, null);
		OBSParticles.drawCylinder(target.getLocation(), target.getWidth(), 3, 8, 1, 0.5, Particle.ENCHANT, null);
		
		if (target instanceof Mob)
		{
			Mob mob = (Mob) target;
		    Location frozenLoc = mob.getLocation();
		    
		    mob.setAware(false);
		    
		    OBStandardTimer.runWithCancel(Ouroboros.instance, (r)->
		    {
		        mob.setVelocity(new Vector(0,0,0));
		        mob.teleport(frozenLoc);

		        OBSParticles.drawWisps(mob.getLocation(), mob.getWidth(), mob.getHeight(), 4, Particle.SCULK_SOUL, null);
		    }, 1, 400);
		    
		    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
		        mob.setAware(true), 400L);
		}
		else if (target instanceof Player)
		{
		    Player pTarget = (Player) target;
		    Location frozenLoc = pTarget.getLocation();
		    
		    frozenPlayers.add(pTarget.getUniqueId());
		    
		    OBStandardTimer.runWithCancel(Ouroboros.instance, (r)->
		    {
		        pTarget.setGravity(false);
		        pTarget.setVelocity(new Vector(0,0,0));
		        pTarget.teleport(frozenLoc);
		        OBSParticles.drawWisps(pTarget.getLocation(), pTarget.getWidth(), pTarget.getHeight(), 4, Particle.END_ROD, null);
		    }, 1, 60);
		    
		    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
		    {
		        pTarget.setGravity(true);
		        frozenPlayers.remove(pTarget.getUniqueId());
		    }, 60L);
		    return true;
		}
		return false;
	}
	
	// In your main class or listener class
	private static Set<UUID> frozenPlayers = new HashSet<>();
	public static void registerSpellHelper(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void onPlayerMove(PlayerMoveEvent event) 
			{
			    if (frozenPlayers.contains(event.getPlayer().getUniqueId())) 
			    {
			        Location from = event.getFrom();
			        Location to = event.getTo();
			        
			        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) 
			        	event.setCancelled(true);
			    }
			}
		}, plugin);
	}
	
}
