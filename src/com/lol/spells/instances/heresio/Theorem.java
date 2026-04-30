package com.lol.spells.instances.heresio;

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
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.HeresioEffects;

public class Theorem extends Spell
{
	public Theorem()
	{
		super("Theorem", "theorem", Material.GREEN_DYE, SpellType.OFFENSIVE, SpellementType.HERESIO, CastConditions.RIGHT_CLICK_AIR, Rarity.FIVE, 200, 1.5, false, false,
				"&r&fDeal 50&c♥&f of "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f damage to &6&otarget &r&dMob &7(35m)",
				"&r&fAlso inflicts &2Intimidated &bII&f for &b&o30 seconds&r&f.","",
				"&r&2Intimidated &eEffect&f: affected are &b&oFatigued&r&f and &b&oWeakened&r&f equal to",
				"&r&fthe &b&omagnitude&r&f of &2Intimidated&f, and take 20% more "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f damage.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 35, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 3, -35, 1.5, Particle.GLOW_SQUID_INK, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 2, -45, 1.2, Particle.WITCH, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 3, 215, 1.5, Particle.GLOW_SQUID_INK, null);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 2, 225, 1.2, Particle.WITCH, null);
			}, 3);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.7, 0.5, Particle.GLOW_SQUID_INK, null);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.4, Particle.WARPED_SPORE, null);
				MobData.damageUnnaturally(p, le, 50, true, true, ElementType.HERESIO);
				HeresioEffects.addIntimidate(le, 1, 30);
			}, 5);
		})) return -1;
		
		return 200;
	}

	@Override
	public int getTotalManaCost()
	{
		return 200;
	}
}
