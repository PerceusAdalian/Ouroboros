package com.lol.spells.instances.glacio;

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
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GlacioEffects;

public class BubbleBeam extends Spell
{

	public BubbleBeam()
	{
		super("Bubble Beam", "bubble_beam", Material.POWDER_SNOW_BUCKET, SpellType.DEBUFF, SpellementType.GLACIO, CastConditions.MIXED, Rarity.THREE, 25, 1.5, true,
				false, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&bBubble Beam&r&f: &b&oOcean's Breath&r&f --",
				"&r&fExpell a burst of bubbles at &6"+Symbols.TARGET+" &finflicting &b&oFrosted &r&bIII &r&7(15m, 10s)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&bBubble Beam&r&f: &b&oOcean's Wrath&r&f --",
				"&r&fSuffocate &6"+Symbols.TARGET+" &fdealing &f&l30&r&c"+Symbols.HP+PrintUtils.color(ObsColors.GLACIO)+" &lGlacio&r&f damage,",
				"&r&finflicting &b&oChill &r&bII &7(20m, 10s)","",
				"&bFrosted&f Effect: &d&oSlows&r&f and &d&oWeakens&r&f those afflicted.",
				"&r&bChill &eEffect&f: &b&oSlows&r&f while inflicting a "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f DOT effect.",
				"&r&fReapplying &bChill&f increases the &b&omagnitude&r&f, while keeping initial duration.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getEntity(p, 20, target -> 
			{
				if (target == null || !(target instanceof LivingEntity le)) return;
				
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
				
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 3, -35, 1.5, Particle.DRIPPING_WATER, null);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 2, -45, 1.2, Particle.SPLASH, null);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 3, 210, 1.5, Particle.DRIPPING_WATER, null);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 2, 225, 1.2, Particle.SPLASH, null);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.3, Particle.SPLASH, null);
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(p, Sound.AMBIENT_UNDERWATER_EXIT, SoundCategory.MASTER);
					
					ObsParticles.drawDisc(le.getLocation(), le.getWidth(), 2, 15, 0.4, Particle.SPLASH, null);
					ObsParticles.drawCylinder(le.getLocation(), le.getWidth(), 4, 15, 0.4, 0.5, Particle.DRIPPING_WATER, null);
					
					MobData.damageUnnaturally(p, le, 30, true, true, ElementType.GLACIO, null);
					GlacioEffects.addChill(p, le, 2, 10);
				}, 12);
				
			})) return -1;
			
			return 25;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getEntity(p, 15, target -> 
			{
				if (target == null || !(target instanceof LivingEntity le)) return;
				
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
				
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 3, -35, 1.5, Particle.DRIPPING_WATER, null);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 2, -45, 1.2, Particle.SPLASH, null);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 3, 210, 1.5, Particle.DRIPPING_WATER, null);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 2, 225, 1.2, Particle.SPLASH, null);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.3, Particle.SPLASH, null);
				
				GlacioEffects.addFrosted(le, 2, 10);
				
			})) return -1;
			
			return 25;
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost()
	{
		return 25;
	}

}
