package com.lol.spells.instances.aero;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class GalvanicNeedle extends Spell
{

	public GalvanicNeedle() 
	{
		super("Galvanic Needle", "galvanic_needle", Material.AMETHYST_SHARD, SpellType.OFFENSIVE, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 150, 2, true,
				"&r&fFire a charged needle that deals 5&c♥&f Pierce damage,",
				"&r&fand summons lightning after a short distance &7(2s | ~15m)",
				"&r&fThose within &b&o10 meters&r&f are inflicted &dShock&r&f &7(14s | &r&cPVP&7: &c4s&7)");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		EntityEffects.playSound(p, Sound.BLOCK_AMETHYST_BLOCK_FALL, SoundCategory.AMBIENT);
		EntityEffects.playSound(p, Sound.ENTITY_BREEZE_SHOOT, SoundCategory.AMBIENT);
		
		Arrow arrow = p.launchProjectile(Arrow.class);
		arrow.setGravity(false);
		arrow.setInvulnerable(true);
		arrow.setPickupStatus(PickupStatus.DISALLOWED);
		arrow.setColor(Color.PURPLE);
		arrow.setCritical(false);
		arrow.setDamage(5);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{	   
			arrow.getWorld().strikeLightning(arrow.getLocation());
			if(!RayCastUtils.getNearbyEntities(arrow, 10, (C)->
			{   
				OBSParticles.drawLine(arrow.getLocation(), C.getLocation(), 0.5, 0, Particle.ELECTRIC_SPARK, null);
				OBSParticles.drawLine(arrow.getLocation(), C.getLocation(), 1, 0, Particle.CRIT, null);
				EntityEffects.addShock(C, C instanceof Player ? 4 : 14);
				arrow.remove();					
			})) 
			{
				arrow.remove();
				return;
			}
		}, 40); 
			    
		return true;
	}

}
