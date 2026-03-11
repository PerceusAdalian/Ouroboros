package com.lol.spells.instances.aero;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class Gust extends Spell
{

	public Gust() 
	{
		super("Gust", "gust", Material.STRING, SpellType.CONTROL, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 10, 1, false,
				"&r&fSend a gust of wind at your target &7(7m)&f blowing them away.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getEntity(p, 7);
		if (target == null || !(target instanceof LivingEntity)) return -1;
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.8, 0.5, Particle.GUST, null);
		target.setVelocity(target.getLocation().toVector().subtract(p.getLocation().toVector()).multiply(1.25));
		return this.getManacost();
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}
}
