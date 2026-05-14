package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.echoes.abilities.instances.crossbow.QuickLoad;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class PlaguesPrimer extends EchoAbility
{

	public PlaguesPrimer()
	{
		super("Plague's Corrosive Primer", "plague_corrosive_primer", Material.NETHER_STAR, StatType.RANGED, 0, 0, 0, AbilityType.SPECIALABILITY, ElementType.MORTIO,
				CastConditions.MIXED, EchoForm.CROSSBOW,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.LEFT_CLICK_AIR),
				PrintUtils.color(ObsColors.MORTIO)+"Plague's Corrosive Primer&f: "+PrintUtils.color(ObsColors.MORTIO)+"&oQuick Load&r&f --",
				"&r&fQuickly reload the next available &b&oarrow&r&f into this &b&oCrossbow&r&f.","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.MORTIO)+"Plague's Corrosive Primer&f: "+PrintUtils.color(ObsColors.MORTIO)+"&oInfect&r&f -- &cRemove &f15 &b&oDurability",
				"&r&fInflict "+PrintUtils.color(ObsColors.CORROSIVE)+"&oErosion &r&fon &6target &dMob &7(30m, 20s)","",
				PrintUtils.color(ObsColors.CORROSIVE)+"Erosion &r&eEffect&f: Applies a "+PrintUtils.color(ObsColors.CORROSIVE)+"&oCorrosive &r&dDOT&f dealing",
				"&r&f5&6"+Symbols.ARMOR+"&f dmg/s, and automatically &c&oends&r&f on &6Break&f.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getEntity(p, 30, target ->
			{
				if (!(target instanceof LivingEntity le) || target instanceof Player) return;
				EntityEffects.playSound(p, Sound.ITEM_CROSSBOW_QUICK_CHARGE_2, SoundCategory.AMBIENT);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{					
					EntityEffects.playSound(p, Sound.ITEM_CROSSBOW_SHOOT, SoundCategory.AMBIENT);
					double delta = ObsParticles.deriveDegreeTheta(p.getLocation(), le.getLocation());
					ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.8, 10, delta, 1, Particle.BLOCK_CRUMBLE, Material.EMERALD_BLOCK.createBlockData());
					ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 9, delta, 0.2, Particle.ASH, null);
					Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
					{
						EntityEffects.addErosion(le, 20);
						ObsParticles.drawLandingWave(le);
					}, 10);
				}, 10);
			})) return -1;
			
			return 15;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.LEFT_CLICK_AIR))
		{			
			if (!QuickLoad.playAbilityEffect(p, e.getItem())) return -1;
			return 0;
		}
		
		return -1;
		
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 0;
	}

}
