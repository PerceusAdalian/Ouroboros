package com.lol.spells.instances.heresio;

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

public class Hypothesis extends Spell
{
	public Hypothesis()
	{
		super("Hypothesis", "hypothesis", Material.GREEN_DYE, SpellType.OFFENSIVE, SpellementType.HERESIO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 70, 1, false, false,
				"&r&fDeal 25&c♥&f of "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f damage to &6&otarget &r&dMob &7(25m)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 25, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 3, -30, 1.5, Particle.GLOW_SQUID_INK, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 2, -210, 1.2, Particle.WITCH, null);
			MobData.damageUnnaturally(p, le, 25, true, true, ElementType.HERESIO);
		})) return -1;
		
		return 70;
	}

	@Override
	public int getTotalManaCost()
	{
		return 70;
	}
}
