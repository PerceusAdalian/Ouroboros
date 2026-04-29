package com.lol.spells.instances.inferno;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Explosion extends Spell
{

	public Explosion()
	{
		super("Explosion", "explosion", Material.TNT, SpellType.OFFENSIVE, SpellementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 200, 4.5, false,
				"&r&fCreate an &c&oexplosion&r&f at &6target &dMob&f dealing &r&fdealing 45&c♥ &e&lBlast ",
				"&r&fdamage causing &cBurn &7(40m, 20s)&r&f. If the &6target&f was &cPrimed&f and",
				"&r&fdied as a result of the &c&oexplosion&r&f, create another &c&oexplosion&r&f dealing",
				"&r&fvariable &e&lBlast&r&f damage &7(Yield: 5, Incendiary)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 40, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			
			MobData data = MobData.getMob(le.getUniqueId());
			Location leLoc = le.getLocation();
			
			boolean hasPrimed = Primer.primed.contains(le.getUniqueId());
			boolean doSecondExplosion = false;
			
			if (hasPrimed && data.getHp(false) - 45 < 0) doSecondExplosion = true;
			
			EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
			ObsParticles.drawCosLine(p.getLocation(), le.getLocation(), 0.7, doSecondExplosion ? Particle.LAVA : Particle.FLAME, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER);
				MobData.damageUnnaturally(p, le, 45, true, true, ElementType.BLAST);
			}, 10);
			
			if (doSecondExplosion)
			{
				ObsParticles.drawWisps(leLoc, 4, 4, 7, Particle.FLAME, null);
				ObsParticles.drawWisps(leLoc, 4, 4, 7, Particle.LARGE_SMOKE, null);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
					ObsParticles.drawSpiralVortex(leLoc, 75, 5, 0.1, Particle.LAVA, null);
					ObsParticles.drawWave(Ouroboros.instance, leLoc, 10, 0.85, 25, Particle.LAVA, null);
				}, 25);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
					leLoc.getWorld().createExplosion(leLoc, 5, true, false, p), 30);
			}
		})) return -1;
		return 200;
	}

	@Override
	public int getTotalManaCost()
	{
		return 200;
	}

}
