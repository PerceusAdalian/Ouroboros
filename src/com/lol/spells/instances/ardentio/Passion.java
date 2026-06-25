package com.lol.spells.instances.ardentio;

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

public class Passion extends Spell
{

	public Passion()
	{
		super("Passion", "passion", Material.NETHER_STAR, SpellType.OFFENSIVE, SpellementType.ARDENTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 25, 1, true,
				true, 
				"&r&fOverwhelm &6"+Symbols.TARGET+" &dMob&f/&c&oPlayer&r&f dealing 100&c"+Symbols.HP+PrintUtils.color(ObsColors.ARDENTIO)+" &lArdentio",
				"&r&fdamage and inflicting "+PrintUtils.color(ObsColors.CORROSIVE)+"&oErosion &r&7(30m, 20s | &cPVP&7: &c75"+Symbols.HP+"&7, &c10s&7)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 30, target ->
		{
			if (target == null || !(target instanceof LivingEntity le)) return;
			double theta = ObsParticles.deriveDegreeTheta(p.getLocation(), le.getLocation());
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.4, 8, theta, 0.45, Particle.HAPPY_VILLAGER, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.35, 8.5, theta, 0.3, Particle.CRIT, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.45, 7.5, theta, 0.35, Particle.BLOCK_CRUMBLE, Material.EMERALD_BLOCK.createBlockData());
			
			MobData.damageUnnaturally(p, target, le instanceof Player ? 75 : 100, true, false, ElementType.ARDENTIO, null);
			EntityEffects.addErosion(le, le instanceof Player ? 10 : 20);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				ObsParticles.drawWave(Ouroboros.instance, le.getLocation(), 5, .8, 20, 0.1, Particle.BLOCK_CRUMBLE, Material.EMERALD_BLOCK.createBlockData()), 12);
		})) return -1;
		return 25;
	}

	@Override
	public int getTotalManaCost()
	{
		return 25;
	}

}
