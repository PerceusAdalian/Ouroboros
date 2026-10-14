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
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.BiomeUtils;
import com.ouroboros.utils.BiomeUtils.BiomeTemperatureCategory;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class GeniesWrath extends EchoAbility
{

	public GeniesWrath()
	{
		super("Genie's Wrath", "genies_wrath", Material.NETHER_STAR, StatType.MELEE, 0, 0, 15, AbilityType.ULTIMATE, ElementType.INFERNO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD, 
				"&r&fRush &6"+Symbols.TARGET+"&r&f dealing "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f damage",
				"&r&fequal to &b350% &f&oBase Atk&r&f x &oCrit Modifier&r&f and inflict &c&oBurn&r&7 (10m, 15s)","",
				"&r&bEchoic Resonance&r&f: While in &c&oHot Biomes&r&e&l -> &r&fApply &c&oMelt&r&f");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 10, target -> 
		{
			if (target == null || !(target instanceof LivingEntity le)) return;
			EchoManifest codec = EchoManager.getCodec(e.getItem());
			if (codec == null) return;
			
			EntityEffects.rushEntity(p, le, 3.5);
			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER);
			ObsParticles.drawCosLine(p.getLocation(), le.getLocation(), 0.5, Particle.LAVA, null);
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.4, Particle.FLAME, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawInfernoCastSigil(le);
				MobData.damageUnnaturally(p, target, codec.baseStats().getAttack()*3.5*codec.baseStats().getCritModifier(), true, true, ElementType.INFERNO, codec);
				InfernoEffects.addBurn(le, 15);
				if (BiomeUtils.getTempCategory(p).equals(BiomeTemperatureCategory.HOT)) 
				{
					EntityEffects.playSound(p, Sound.ITEM_FIRECHARGE_USE, SoundCategory.MASTER);
					InfernoEffects.setMelt(le);
					ObsParticles.drawWave(Ouroboros.instance, le.getLocation(), 6.5, 8.5, 25, 0.4, Particle.LAVA, null);
				}	
			}, 12);
			
		})) return -1;
		
		return 15;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 15;
	}

}
