package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.EchoManager;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class LanceFlammes extends EchoAbility
{

	public LanceFlammes()
	{
		super("Lance Flammes", "lance_flammes", Material.NETHER_STAR, StatType.RANGED, 0, 0, 10, AbilityType.SPECIALABILITY, ElementType.INFERNO,
				CastConditions.LEFT_CLICK_AIR, EchoForm.BOW, 
				"&r&fDeal 70&c"+Symbols.HP+PrintUtils.color(ObsColors.INFERNO)+" &lInferno&r&f damage in a &d&oconal AOE &r&7(20m)",
				"&r&fapplying &cCharred &fand &cPrimed&f to those hit &7(30s)","",
				"&bEchoic Ressonance&f: If &6"+Symbols.TARGET+" &fis &cPrimed&f, deal 135&c"+Symbols.HP+" &e&lBlast&r&f damage instead.",
				"&r&cCharred &eEffect&f: Causes &b&oHunger&r&f, &b&oFatigue&r&f, and &b&oSlowness&r&f, while affected take &b&o25%&r&f",
				"&r&fmore "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno &r&fdamage, and may cause &cBurn&f upon hit removing the effect.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntitiesInFov(p, 20, target ->
		{
			if (target == null || !(target instanceof LivingEntity le)) return;
			
			double theta = ObsParticles.deriveDegreeTheta(p.getLocation(), le.getLocation());
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 9, theta, 0.3, Particle.FLAME, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 9, theta, 0.3, Particle.SMOKE, null);
			
			double damage = 70;
			boolean primed = false;
			if (InfernoEffects.primed.contains(le.getUniqueId()))
			{
				primed = true;
				damage = 135;
				ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.6, Particle.COPPER_FIRE_FLAME, null);
				InfernoEffects.primed.remove(le.getUniqueId());
			}
			else InfernoEffects.addPrimer(le, 30);
			
			InfernoEffects.addCharred(le, 30);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> ObsParticles.drawSpiralVortex(le.getLocation(), 65, 4, 0.1, Particle.LAVA, null), 12);
			MobData.damageUnnaturally(p, le, damage, true, true, primed ? ElementType.BLAST : ElementType.INFERNO, EchoManager.getCodec(e.getItem()));
			
		})) return -1;
		
		EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
		
		return 10;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 10;
	}

}
