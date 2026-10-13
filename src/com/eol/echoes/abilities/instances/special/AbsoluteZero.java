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
import com.eol.echoes.records.EchoManifest;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.EntityCategories;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GlacioEffects;

public class AbsoluteZero extends EchoAbility
{

	public AbsoluteZero()
	{
		super("Absolute Zero", "absolute_zero", Material.NETHER_STAR, StatType.MELEE, 0, 0, 20, AbilityType.ULTIMATE, ElementType.GLACIO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD, 
				"&r&fRush &6"+Symbols.TARGET+" &finflicting &b&oChill &r&bIII&r&f and dealing "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio &r&fdamage",
				"&r&fequal to &b&o225% &r&f&oBase Atk&r&f x &r&f&oCrit Modifer&r&7 (10m, 8s)","",
				"&r&bEchoic Resonance&r&f: If &6"+Symbols.TARGET+"&r&f is "+PrintUtils.color(ObsColors.INFERNO)+"&lInfernal&r&e -> &fInstantly kill &6"+Symbols.TARGET,
				"&r&bEchoic Dissonance&r&f: &cPVP&f Compatibility &e-> &c&lFalse");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 10, target ->
		{
			if (target == null || !(target instanceof LivingEntity le)) return;
			if (le instanceof Player) return;
			boolean isInfernal = EntityCategories.parseElementType(le.getType()).equals(ElementType.INFERNO);
			
			EntityEffects.rushEntity(p, le, 3.5);
			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.AMBIENT);
			ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.6, Particle.SNOWFLAKE, null);
			ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.6, Particle.CLOUD, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				if (isInfernal)
				{
					MobData data = MobData.getMob(le.getUniqueId());
					MobData.damageUnnaturally(p, target, data.getHp(true), true, false, ElementType.GLACIO, null);
					ObsParticles.drawWave(Ouroboros.instance, le.getLocation(), 10, 9, 30, 0.2, Particle.SNOWFLAKE, null);
					ObsParticles.drawWave(Ouroboros.instance, le.getLocation(), 10, 8, 12, 0.2, Particle.DUST_PILLAR, null);
					EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.AMBIENT);
					EntityEffects.playSound(p, Sound.BLOCK_SNOW_BREAK, SoundCategory.MASTER);
				}
				else 
				{
					EchoManifest codec = EchoManager.getCodec(e.getItem());
					if (codec == null) return;
					double damage = (codec.baseStats().getAttack()*2.25) * (codec.baseStats().getCritModifier());
					ObsParticles.drawGlacioCastSigil(le);
					MobData.damageUnnaturally(p, target, damage, true, true, ElementType.GLACIO, codec);
					GlacioEffects.addChill(p, le, 3, 8);
				}
			}, 12);
		})) return -1;
		
		return 20;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 20;
	}

}
