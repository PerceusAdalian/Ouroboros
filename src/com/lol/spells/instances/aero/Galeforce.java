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
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Galeforce extends Spell
{

	public Galeforce() 
	{
		super("Galeforce", "galeforce", Material.STRING, SpellType.CONTROL, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 20, 1, false, false,
				"&r&fBlast your target &7(7m)&f with high pressurized air,",
				"&r&fdealing 4&c♥ "+PrintUtils.color(ObsColors.AERO)+"&lAero &r&fdamage, and blowing them away.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getEntity(p, 7);
		if (target == null || !(target instanceof LivingEntity) || target instanceof Player) return -1;
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.8, 0.5, Particle.GUST, null);
		MobData.damageUnnaturally(p, target, 4, true, true, ElementType.AERO);
		target.setVelocity(target.getLocation().toVector().subtract(p.getLocation().toVector()).multiply(1.5));
		return this.getManacost();
	}

	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}
}
