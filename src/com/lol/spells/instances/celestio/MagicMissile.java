package com.lol.spells.instances.celestio;

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
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class MagicMissile extends Spell
{

	public MagicMissile()
	{
		super("Magic Missile", "magic_missile", Material.SPECTRAL_ARROW, SpellType.OFFENSIVE, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 35, 1.0, true,
				"&r&fDeal 10&c♥ "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f damage to &6target &7(40m | &cPVP&7: &c2♥&7)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		
		if (!RayCastUtils.getEntity(p, 40, target ->
		{
			if (!(target instanceof LivingEntity le)) return;
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			if (p.getLocation().distance(target.getLocation()) < 20)
			{
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 2, 0.5, Particle.CLOUD, null);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.8, 0.5, Particle.END_ROD, null);
			}
			else
			{
				ObsParticles.drawAngledArcLine(p.getLocation(), target.getLocation(), 1.5, 5, ObsParticles.deriveDegreeTheta(p.getLocation(), target.getLocation()), 0.25, Particle.CLOUD, null);
				ObsParticles.drawAngledArcLine(p.getLocation(), target.getLocation(), .9, 5, ObsParticles.deriveDegreeTheta(p.getLocation(), target.getLocation()), 0.1, Particle.END_ROD, null);
			}
			MobData.damageUnnaturally(p, le, target instanceof Player ? 2 : 10, target instanceof Player ? false : true, true, ElementType.CELESTIO);
		})) return -1;
		
		return 35;
	}

	@Override
	public int getTotalManaCost()
	{
		return 35;
	}

}
