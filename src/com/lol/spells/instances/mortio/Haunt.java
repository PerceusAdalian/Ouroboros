package com.lol.spells.instances.mortio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.GeneralEvents;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;

public class Haunt extends Spell
{

	public Haunt() 
	{
		super("Haunt", "haunt", Material.WITHER_ROSE, SpellType.ULTIMATE, SpellementType.MORTIO, CastConditions.SHIFT_RIGHT_CLICK_AIR, Rarity.FIVE, 200, 180, true,
				"&r&fReturn to last death location and apply &e&oWard&r&b III &7(15s)",
				"&r&c&lPVP&r&f: Summon yourself to your oppressor and inflict &4Doom&r &bIII &7(20s)",
				"&r&fAs an additional cost, return at &b&o75% &r&cHP&f. Must be cast within &b&o30s&r&f of death.",
				"&r&fIf the summoning fails, the normal effect takes place.","",
				"&r&4Doom &eEffect&f: Doom applies a &dDOT&f effect equal to it's &b&omagnitude&r&f.",
				"&r&fAfflicted take &b&o1.25x &r&4&lMortio&r&f damage, and reapplying instantly kills them &7(NONPVP)",
				"&r&4&lMortio&r&f-based mobs are otherwise unaffected, and &a&ohealed&r&f instead.");
		
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Player target = getRecentPlayerKiller(p);
		
		if (target != null)
		{
			OBSParticles.drawMortioCastSigil(p);
			OBSParticles.drawMortioCastSigil(target);
			PrintUtils.PrintToActionBar(target, "A soul haunts you!");
			PrintUtils.PrintToActionBar(p, "Returning to "+target.getName());
			EntityEffects.add(p, PotionEffectType.SLOWNESS, 80, 99);
			EntityEffects.add(p, PotionEffectType.BLINDNESS, 80, 9);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				if (!target.isOnline() || target == null)
				{
					PrintUtils.PrintToActionBar(p, "The summoning fails!");
					Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> PrintUtils.PrintToActionBar(p, "Returning to last death location.."), 20);
					p.teleport(p.getLastDeathLocation());
					EntityEffects.addWard(p, 2, 15);
					EntityEffects.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT);
					OBSParticles.drawCylinder(p.getLocation(), p.getWidth()+1, 3, 15, 0.5, 0.5, Particle.LARGE_SMOKE, null);
					return;
				}
				EntityEffects.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT);
				p.teleport(target);
				EntityEffects.addDoom(target, 2, 300);
				OBSParticles.drawCylinder(target.getLocation(), target.getWidth()+1, 3, 15, 0.5, 0.5, Particle.LARGE_SMOKE, null);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
					OBSParticles.drawCylinder(p.getLocation(), p.getWidth()+1, 3, 15, 0.5, 0.5, Particle.LARGE_SMOKE, null), 20);
				p.setHealth(p.getHealth() * .75);
				return;
			}, 60);
			return true;
		}
		OBSParticles.drawMortioCastSigil(p);
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		PrintUtils.PrintToActionBar(p, "Returning to last death location..");
		EntityEffects.add(p, PotionEffectType.SLOWNESS, 40, 99);
		EntityEffects.add(p, PotionEffectType.BLINDNESS, 40, 9);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			EntityEffects.playSound(p, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.AMBIENT);
			p.teleport(p.getLastDeathLocation());
			EntityEffects.addWard(p, 2, 15);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
				OBSParticles.drawCylinder(p.getLocation(), p.getWidth()+1, 3, 15, 0.5, 0.5, Particle.LARGE_SMOKE, null), 20);
		
		}, 20);
		
		return true;
	}
	
	public Player getRecentPlayerKiller(Player victim) 
	{
	    // Check if death was recent (e.g., within last 10 seconds)
	    Long deathTime = GeneralEvents.playerDeathRegistry.get(victim.getUniqueId());
	    if (deathTime == null) return null;
	    
	    long timeSinceDeath = System.currentTimeMillis() - deathTime;
	    if (timeSinceDeath > 30000) return null; // 10 seconds
	    
	    EntityDamageEvent lastDamage = victim.getLastDamageCause();
	    if (lastDamage == null) return null;
	    
	    // Check if killed by a player
	    if (lastDamage instanceof EntityDamageByEntityEvent) 
	    {
	        EntityDamageByEntityEvent event = (EntityDamageByEntityEvent) lastDamage;
	        Entity damager = event.getDamager();
	        
	        if (damager instanceof Player) 
	        	return (Player) damager;
	        
	        if (damager instanceof Projectile) 
	        {
	            Projectile projectile = (Projectile) damager;
	            if (projectile.getShooter() instanceof Player) 
	            {
	                return (Player) projectile.getShooter();
	            }
	        }
	    }
	    
	    return null;
	}

}
