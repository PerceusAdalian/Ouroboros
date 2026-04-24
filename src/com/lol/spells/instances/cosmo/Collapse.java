package com.lol.spells.instances.cosmo;

import java.util.List;

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

public class Collapse extends Spell
{
	// TODO -- Review
	public Collapse()
	{
		super("Collapse", "collapse", Material.ENDER_PEARL, SpellType.OFFENSIVE, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 150, 2.5, false,
				"&r&fPull &b&onearby targets&r&f inward toward the &6&otarget &r&dMob &7(&b20m&7, &640m&7)",
				"&r&fDeals "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f damage equal to &6&otarget mob's &r&b&olevel&f.",
				"&r&fDeals &e&lCrush&r&f damage equal to the sum of pulled targets' levels ÷ 4");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getEntity(p, 30);
		if (target == null || !(target instanceof LivingEntity initialTarget)) return -1;
		MobData data = MobData.getMob(initialTarget.getUniqueId());
		EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.AMBIENT);
		ObsParticles.drawLine(p.getLocation(), initialTarget.getLocation(), 0.6, 0.4, Particle.END_ROD, null);
		ObsParticles.drawLine(p.getLocation(), initialTarget.getLocation(), 1, 0.5, Particle.SONIC_BOOM, null);
		
		List<Entity> nearby = RayCastUtils.getNearbyEntities(p, 30);
		if (nearby.isEmpty()) // No damage possible; there aren't any mobs to deal damage/crush inward towards the mob, so it just "fails"
		{
			EntityEffects.playSound(p, Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.AMBIENT);
			ObsParticles.drawCosmoCastSigil(initialTarget);
		}
		else
		{
			double damageToNearby = data != null ? data.getLevel() : 1;
			double levelsSummed = 0;
			for (Entity nTarget : nearby)
			{
				if (!(nTarget instanceof LivingEntity)) continue;
				MobData deltaData = MobData.getMob(nTarget.getUniqueId());
				levelsSummed += deltaData != null ? deltaData.getLevel() : 0;
				double deltaAngle = ObsParticles.deriveDegreeTheta(initialTarget.getLocation(), nTarget.getLocation());
				ObsParticles.drawAngledArcLine(initialTarget.getLocation(), nTarget.getLocation(), 0.6, 10, deltaAngle, 0.5, Particle.END_ROD, null);
				nTarget.setVelocity(initialTarget.getLocation().toVector().normalize().subtract(nTarget.getLocation().toVector()).multiply(3));
				MobData.damageUnnaturally(p, nTarget, damageToNearby, true, true, ElementType.COSMO);
			}
			MobData.damageUnnaturally(p, initialTarget, levelsSummed / 4.0, true, true, ElementType.CRUSH);
		}
		
		return 150;
	}

	@Override
	public int getTotalManaCost()
	{
		return 150;
	}

}
