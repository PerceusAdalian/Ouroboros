package com.lol.spells.instances.astral;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.TimeUtils;
import com.ouroboros.utils.TimeUtils.Timeframe;
import com.ouroboros.utils.entityeffects.CosmoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GeoEffects;

public class Attenuate extends Spell
{

	public Attenuate()
	{
		super("Attenuate", "attenuate", Material.FIREWORK_STAR, SpellType.DEBUFF, SpellementType.ASTRAL, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 50, 3, true,
				true, 
				PrintUtils.assignAstralVariant("Attenuate", true)+" &r&e&oAmplify&r&f --",
				"&r&fApply &6Vulnerable&r&f to &6target &dMob&f/&c&oPlayer &r&7(25m, 20s | &cPVP&7: &c12s&7)","",
				PrintUtils.assignAstralVariant("Attenuate", false)+" &r&9&oNullify&r&f --",
				"&r&fApply &3Voided&f to &6target &dMob&f/&c&oPlayer &r&7(25m, 20s | &cPVP&7: &c8s&7)","",
				"&r&6Vulnerable &eEffect&f: Physical damage resistance is reduced by &b&o50%&r&f.",
				"&r&3Voided &eEffect&f: Neutralizes elemental affinity.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 25, target ->
		{
			if (target == null || !(target instanceof LivingEntity le)) return;
			
			double theta = ObsParticles.deriveDegreeTheta(p.getLocation(), le.getLocation());
			if(TimeUtils.checkTime(p.getWorld(), Timeframe.DAY))
			{
				EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.4, 9, theta, 0.3, Particle.COPPER_FIRE_FLAME, null);
				ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.6, Particle.FLAME, null);
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					ObsParticles.drawDisc(le.getLocation(), le.getWidth()+3, 1, 6, 0.5, Particle.LAVA, null);
					ObsParticles.drawSpiralVortex(le.getLocation(), 65, 3, 0.1, Particle.CRIT, null);
					GeoEffects.addVulnerable(le, le instanceof Player ? 12 : 20);
				}, 12);
			}
			else
			{
				EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.4, 9, theta, 0.3, Particle.COPPER_FIRE_FLAME, null);
				ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.6, Particle.FLAME, null);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					ObsParticles.drawDisc(le.getLocation(), le.getWidth()+3, 1, 6, 0.5, Particle.END_ROD, null);
					ObsParticles.drawSpiralVortex(le.getLocation(), 65, 3, 0.1, Particle.GLOW_SQUID_INK, null);
					CosmoEffects.addVoided(le, le instanceof Player ? 8 : 20);
				}, 12);
			}
		})) return -1;

		return 0;
	}

	@Override
	public int getTotalManaCost()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
