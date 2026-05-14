package com.eol.echoes.abilities.instances.special;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;

public class KelligirAeroMastery extends EchoAbility
{

	public KelligirAeroMastery()
	{
		super("Aero Mastery", "kelligir_wind_shot", Material.NETHER_STAR, StatType.RANGED, 0, 0, 0, AbilityType.SPECIALABILITY, ElementType.AERO,
				CastConditions.MIXED, EchoForm.BOW,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.LEFT_CLICK_AIR),
				"&r&dAero Mastery&f: &d&oWind Shot&r&f -- &cRemove &f5 &b&oDurability",
				"&r&fExpell a burst of wind infront of you.","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&dAero Mastery&f: &d&oStalk Prey&r&f -- &cRemove &f10 &b&oDurability",
				"&r&fGrant &4Shroud &bII &fto &6self &7(15s)","",
				"&r&4Shroud &eEffect&f: Grants an increase to &b&oSpeed&r&f and &b&oJump Height&r&f",
				"&r&fequal to the magnitude of Shroud, plus &b&oInvisibility&r&f.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 6, .85, 20, 0.3, Particle.LARGE_SMOKE, null);
			MortioEffects.addShroud(p, 1, 15);
			EntityEffects.playSound(p, Sound.ENTITY_BREEZE_WHIRL, SoundCategory.AMBIENT);
			return 10;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.LEFT_CLICK_AIR))
		{			
			WindCharge wc = (WindCharge) p.getWorld().spawnEntity(p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(1.5)), EntityType.WIND_CHARGE);
			wc.setShooter(p);
			wc.setYield(5);
			EntityEffects.playSound(p, Sound.ENTITY_BREEZE_SHOOT, SoundCategory.AMBIENT);
			return 5;
		}
		
		return -1;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 5;
	}

}
