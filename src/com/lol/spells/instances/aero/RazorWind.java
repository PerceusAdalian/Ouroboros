package com.lol.spells.instances.aero;

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
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class RazorWind extends Spell
{

	public RazorWind()
	{
		super("Razor Wind", "razor_wind", Material.WIND_CHARGE, SpellType.OFFENSIVE, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 100, 3, false, true,
				"&r&fDeal 20&c♥&f "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f damage to &6target&f inflicting "+PrintUtils.color(ObsColors.CORROSIVE)+"&oErosion &r&7(30m, 20s)","",
				PrintUtils.color(ObsColors.CORROSIVE)+"Erosion &r&eEffect&f: Applies a "+PrintUtils.color(ObsColors.CORROSIVE)+"&oCorrosive &r&dDOT&f dealing",
				"&r&f5&6"+Symbols.ARMOR+"&f dmg/s, and automatically &c&oends&r&f on &6Break&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 30, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			
			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.AMBIENT);
			
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.7, 0.4, Particle.SWEEP_ATTACK, null);
			double delta = ObsParticles.deriveDegreeTheta(p.getLocation(), le.getLocation());
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.8, 10, delta, 1, Particle.BLOCK_CRUMBLE, Material.EMERALD_BLOCK.createBlockData());
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 9, delta, 0.2, Particle.ASH, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ENTITY_BREEZE_WHIRL, SoundCategory.AMBIENT);
				MobData.damageUnnaturally(p, le, 20, true, false, ElementType.AERO);
				EntityEffects.addErosion(le, 20);
			}, 12);
			
		})) return -1;
		
		return 100;
	}

	@Override
	public int getTotalManaCost()
	{
		return 100;
	}

}
