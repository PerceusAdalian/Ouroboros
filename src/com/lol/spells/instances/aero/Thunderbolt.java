package com.lol.spells.instances.aero;

import org.bukkit.Bukkit;
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
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.AeroEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Thunderbolt extends Spell
{

	public Thunderbolt() 
	{
		super("Thunderbolt", "thunderbolt", Material.LARGE_AMETHYST_BUD, SpellType.OFFENSIVE, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 100, 3, true,
				"&r&fStrike your target with &d&lAero&r&f energy &7(30m)&f inflicting &dStatic &7(10s / &r&cPVP&7:&r&c 5s&r&7)","",
				"&r&dStatic &eEffect&f: causes mobs to emit shockwaves in a 10m radius.",
				"&r&fAffected mobs receive &b&o1.25x &r&d&lAero&r&f damage.",
				PrintUtils.color(ObsColors.AERO)+"&lAero&r&f-based mobs are &e&oimmune&r&f, and &a&ohealed&r&f instead.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getEntity(p, 30);
		if (!(target instanceof LivingEntity) || target == null) return -1;

		EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.AMBIENT);
		ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.ELECTRIC_SPARK, null);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			target.getWorld().strikeLightning(target.getLocation()), 15);
		AeroEffects.addStatic((LivingEntity) target, p, target instanceof Player ? 5 : 10);
		
		return this.getManacost();
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

}
