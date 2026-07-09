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
import com.ouroboros.utils.entityeffects.MortioEffects;

public class SpiritBreak extends EchoAbility
{

	public SpiritBreak()
	{
		super("Spirit Break", "spirit_break", Material.NETHER_STAR, StatType.MELEE, 0, 0, 10, AbilityType.SPECIALABILITY, ElementType.HERESIO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.HAMMER, 
				"&r&fLunge towards &6"+Symbols.TARGET+" &fdealing &l300%&r&b&o Base Atk",
				"&r&fas &e&lCrush&r&f dmg, inflicting &4Toxin &bIII &7(7m, 30s)","",
				"&r&4Toxin &eEffect&f: Affected are "+PrintUtils.color(ObsColors.CORROSIVE)+"&oPoisoned&r&f in severity",
				"&r&fscaled with &4Toxin&f's &b&omagnitude&r&f. This effect &c&odoes not &r&fstack.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 7, target ->
		{
			if (target == null || !(target instanceof LivingEntity le)) return;
			
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.3, 0.3, Particle.CRIMSON_SPORE, null);
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.3, 0.3, Particle.SMOKE, null);
			EntityEffects.rushEntity(p, le, 2.5);
			
			double dmg = EchoManager.getCodec(e.getItem()).baseStats().getAttack() * 3.0;
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, SoundCategory.AMBIENT);
				ObsParticles.drawLandingWave(le);
				MobData.damageUnnaturally(p, le, dmg, true, true, ElementType.CRUSH, EchoManager.getCodec(e.getItem()));
				MortioEffects.addToxin(le, 2, 30);
				ObsParticles.drawMortioCastSigil(le);
			}, 7);
		})) return -1;
		
		return 10;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 10;
	}

}
