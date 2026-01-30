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
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class Thunderbolt extends Spell
{

	public Thunderbolt() 
	{
		super("Thunderbolt", "thunderbolt", Material.LARGE_AMETHYST_BUD, SpellType.OFFENSIVE, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 100, 3, true,
				"&r&fStrike your target with &d&lAero&r&f energy &7(30m)&f inflicting &dStatic &7(20s / &r&cPVP&7:&r&c 10s&r&7)","",
				"&r&dStatic &eEffect&f: causes mobs to emit shockwaves in a 10m radius.",
				"&r&fAffected mobs receive &b&o1.25x &r&d&lAero&r&f damage.",
				"&d&lAero&r&f-based mobs are &e&oimmune&r&f, and &a&ohealed&r&f instead.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 30);
		if (!(target instanceof LivingEntity) || target == null) return false;

		EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.AMBIENT);
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.ELECTRIC_SPARK, null);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			target.getWorld().strikeLightning(target.getLocation()), 15);
		EntityEffects.addStatic((LivingEntity) target, p, target instanceof Player ? 10 : 20);
		
		return true;
	}

}
