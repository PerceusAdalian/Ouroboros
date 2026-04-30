package com.lol.spells.instances.glacio;

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
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityCategories;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GlacioEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class Scald extends Spell
{

	public Scald()
	{
		super("Scald", "scald", Material.LIGHT_BLUE_DYE, SpellType.OFFENSIVE, SpellementType.GLACIO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 50, 1.5, false, false,
				"&r&fExpels a blast of hot water &7(15m)&f, dealing 5&c♥ "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f damage to most targets.",
				"&r&fDamage becomes 10&c♥ "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f against "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f mobs, applying &cMelt&f,",
				"&r&fand 10&c♥ "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f against "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f mobs, applying &bQuench&f.","",
				"&r&cMelt &eEffect&f: Instantly &6Breaks&f "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f mobs.","",
				"&r&bQuench &eEffect&f: Instantly &6Breaks&f "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f mobs.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		EntityEffects.playSound(p, Sound.AMBIENT_UNDERWATER_EXIT, SoundCategory.AMBIENT);
		
		if (!RayCastUtils.getEntitiesInFov(p, 15, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			
			Particle p1 = Particle.SNOWFLAKE;
			Particle p2 = Particle.DRIPPING_WATER;
			
			if (EntityCategories.glacio_mobs.contains(le.getType()))
			{
			    p2 = Particle.FLAME;
			    MobData.damageUnnaturally(p, le, 10, true, false, ElementType.INFERNO);
			    InfernoEffects.setMelt(le);
			}
			else if (EntityCategories.inferno_mobs.contains(le.getType()))
			{
			    p1 = Particle.FLAME;
			    MobData.damageUnnaturally(p, le, 10, true, false, ElementType.GLACIO);
			    GlacioEffects.setQuench(le);
			}
			else
			{
			    MobData.damageUnnaturally(p, le, 5, true, false, ElementType.GLACIO);
			}

			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.4, 0.4, p1, null);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.4, 0.3, p2, null);
			
		})) return -1;
		return 50;
	}

	@Override
	public int getTotalManaCost()
	{
		return 50;
	}

}
