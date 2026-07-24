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
import com.ouroboros.utils.entityeffects.GlacioEffects;

public class IcySlash extends EchoAbility
{

	public IcySlash() 
	{
		super("Icy Slash", "icyslash", Material.ICE, StatType.MELEE, 10, 2, 10, AbilityType.OFFENSIVE, ElementType.GLACIO, CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD,
				"&r&fDeal &l45&r&c♥ "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio &r&fdmg to &6"+Symbols.TARGET+"&f, inflicting &bChill I &7(7m, 10s)","",
				"&r&bChill &eEffect&f: &b&oSlows&r&f while inflicting a "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f DOT effect.",
				"&r&fReapplying &bChill&f increases the &b&omagnitude&r&f, while keeping initial duration.");
	}

	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 7, target ->
		{
			if (!(target instanceof LivingEntity le)) return;
			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), p.getLocation().distance(target.getLocation()), 0.1, Particle.SNOWFLAKE, null);
			EntityEffects.rushEntity(p, le, 2.5);

			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawSpiralVortex(target.getLocation(), target.getWidth(), Math.max(Math.min(1, target.getHeight()), 2), 0, Particle.SNOWFLAKE, null);
				MobData.damageUnnaturally(p, target, 45, true, true, ElementType.GLACIO, EchoManager.getCodec(e.getItem()));
				GlacioEffects.addChill(p, le, 0, 10);
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
