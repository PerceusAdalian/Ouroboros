package com.lol.spells.instances.celestio;

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

public class Radiate extends Spell
{

	public Radiate()
	{
		super("Radiate", "radiate", Material.GREEN_DYE, SpellType.OFFENSIVE, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 100, 1, false,
				true, "&r&fDeal 10&c♥&f "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f damage to &6target&f inflicting "+PrintUtils.color(ObsColors.CORROSIVE)+"&oErosion &r&7(30m, 20s)","",
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
			ObsParticles.drawAngledArcLine(targetLoc, p.getLocation(), 0.6, 3, -35, 1.5, Particle.CLOUD, null);
			ObsParticles.drawAngledArcLine(targetLoc, p.getLocation(), 0.6, 3, 215, 1.5, Particle.CLOUD, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawLine(p.getLocation(), targetLoc, 0.5, 0.4, Particle.BLOCK_CRUMBLE, Material.EMERALD_BLOCK.createBlockData());
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 1, 0.4, Particle.END_ROD, null);
				EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.AMBIENT);
				ObsParticles.drawCelestioCastSigil(le);
				MobData.damageUnnaturally(p, le, 10, true, false, ElementType.CELESTIO, null);
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
