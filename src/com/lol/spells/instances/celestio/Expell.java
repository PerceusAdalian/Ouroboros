package com.lol.spells.instances.celestio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class Expell extends Spell
{

	public Expell() 
	{
		super("Expose", "expose", Material.NETHER_STAR, SpellType.OFFENSIVE, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 25, 3, false,
				"&r&fApply &e&oExposed&r&f to target within &b&o25 meters &r&7(30s)","",
				"&r&eExposed Effect&r&f: Reveals an entity's location and &6&oBreaks &r&fthem.",
				"&r&fIf those affected are "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f-based, they instantly die.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getEntity(p, 25);
		if (target == null || !(target instanceof LivingEntity le)) return -1;
		
		OBSParticles.drawLine(p.getLocation(), le.getLocation(), 0.6, 0.5, Particle.WAX_ON, null);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			EntityEffects.addExposed(le, 30);
			OBSParticles.drawCylinder(le.getLocation(), le.getWidth(), (int) le.getHeight(), 4, 0.25, 0.5, Particle.WAX_ON, null);
			OBSParticles.drawCylinder(le.getLocation(), le.getWidth(), (int) le.getHeight(), 8, 0.5, 0.5, Particle.CLOUD, null);
		}, 15);
		
		return this.getManacost();
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

}
