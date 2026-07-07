package com.eol.echoes.abilities.instances.sword;

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

public class SerratedBlade extends EchoAbility
{

	public SerratedBlade() 
	{
		super("Serrated Blade", "serrated_blade", Material.NETHERITE_SWORD, StatType.MELEE, 15, 5, 10, AbilityType.COMBAT, ElementType.MORTIO, CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD,
				"&r&fDeal &l50&r&c♥ "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio &r&fdmg to &6"+Symbols.TARGET+"&f, inflicting &4Dread &7(7m, 10s)","",
				"&r&4Dread &eEffect&f: Applies a debilitation that causes &b&ohunger&r&f and &b&oblindness&r&f",
				"&r&fto those afflicted. Dread is &e&ocurable&r&f and does not stack, however",
				"&r&fsubsequent applications will inflict &4Doom&f after a second application.");
	}

	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 7, target ->
		{
			if (!(target instanceof LivingEntity le)) return;
			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), p.getLocation().distance(target.getLocation()), 0.1, Particle.SMOKE, null);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), p.getLocation().distance(target.getLocation()), 0.2, Particle.CRIT, null);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), p.getLocation().distance(target.getLocation()), 0.2, Particle.CRIMSON_SPORE, null);
			EntityEffects.rushEntity(p, le, 2.5);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawMortioCastSigil(le);
				MobData.damageUnnaturally(p, target, 5, true, true, ElementType.MORTIO, EchoManager.getCodec(e.getItem()));
				MortioEffects.addDread(le, 10);
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