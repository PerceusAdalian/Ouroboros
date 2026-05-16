package com.lol.spells.instances.cosmo;

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
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Disintegrate extends Spell
{
	public Disintegrate()
	{
		super("Disintegrate", "disintegrate", Material.GREEN_DYE, SpellType.OFFENSIVE, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 100, 1, false,
				true, "&r&fInflict "+PrintUtils.color(ObsColors.CORROSIVE)+"&oErosion&r&f and deal 30&c♥&f "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f damage to &6target &7(30m, 20s)","",
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
			
			Location targetLoc = le.getLocation();
			
			double delta = ObsParticles.deriveDegreeTheta(p.getLocation(), le.getLocation());
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.8, 10, delta, 1, Particle.BLOCK_CRUMBLE, Material.EMERALD_BLOCK.createBlockData());
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 9, delta, 0.2, Particle.ASH, null);
			ObsParticles.drawCosLine(targetLoc, p.getLocation(), 0.5, Particle.DRAGON_BREATH, 1.0f);
			EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.AMBIENT);
			EntityEffects.addErosion(le, 20);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.MASTER);
				ObsParticles.drawLine(p.getLocation(), targetLoc, 4, 0.5, Particle.SONIC_BOOM, null);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 1, 0.5, Particle.END_ROD, null);
				ObsParticles.drawCosmoCastSigil(le);
				MobData.damageUnnaturally(p, le, 30, true, false, ElementType.COSMO, null);
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
