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

public class Lemma extends Spell
{
	public Lemma()
	{
		super("Lemma", "lemma", Material.GREEN_DYE, SpellType.OFFENSIVE, SpellementType.HERESIO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 150, 1, false, false,
				"&r&fDeal 35&c♥&f of "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f damage to &6&otarget &r&dMob &7(30m)",
				"&r&fAlso inflicts &2Intimidated &bI&f for &b&o20 seconds&r&f.","",
				"&r&2Intimidated &eEffect&f: affected are &b&oFatigued&r&f and &b&oWeakened&r&f equal to",
				"&r&fthe &b&omagnitude&r&f of &2Intimidated&f, and take 20% more "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f damage.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 30, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 3, -35, 1.5, Particle.GLOW_SQUID_INK, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 2, -45, 1.2, Particle.WITCH, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 3, 215, 1.5, Particle.GLOW_SQUID_INK, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 2, 225, 1.2, Particle.WITCH, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.7, 0.5, Particle.GLOW_SQUID_INK, null);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.4, Particle.WARPED_SPORE, null);
				HeresioEffects.addIntimidate(le, 0, 20);
				MobData.damageUnnaturally(p, le, 15, true, true, ElementType.HERESIO);
			}, 4);
		})) return -1;
		
		return 150;
	}

	@Override
	public int getTotalManaCost()
	{
		return 150;
	}
}
