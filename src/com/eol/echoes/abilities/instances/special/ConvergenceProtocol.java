package com.eol.echoes.abilities.instances.special;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.EchoManager;
import com.eol.echoes.EchoManager.DurabilityOperation;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.EntityCategory;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.EntityCategories;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.CosmoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ConvergenceProtocol extends EchoAbility
{

	public ConvergenceProtocol()
	{
		super("Convergence Protocol", "convergence_protocol", Material.NETHER_STAR, StatType.MELEE, 0, 0, 0, AbilityType.ULTIMATE, ElementType.COSMO,
				CastConditions.MIXED, EchoForm.POLEARM, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.COSMO)+"Convergence Protocol&f: "+PrintUtils.color(ObsColors.COSMO)+"&oData Corruption&r&f -- &cRemove &f5 &bDurability",
				"&r&fDeal &l300&r&c"+Symbols.HP+PrintUtils.color(ObsColors.COSMO)+" &lCosmo&r&f to &6"+Symbols.TARGET+"&r&f, then",
				"&r&fapply "+PrintUtils.color(ObsColors.CELESTIO)+"&oExpose &r&fand "+PrintUtils.color(ObsColors.COSMO)+"&oVoided &r&7(25m, 20s)",
				"&bEchoic Resonance&f: If &6"+Symbols.TARGET+" &fis not "+PrintUtils.color(ObsColors.COSMO)+"&oCosmic&r&e -> &aRestore &f15 &bDurability","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.COSMO)+"Convergence Protocol&f: "+PrintUtils.color(ObsColors.COSMO)+"&oSingularity&r&f -- &cRemove &f350 &bDurability",
				"&r&fDeal &l2500&r&c"+Symbols.HP+PrintUtils.color(ObsColors.COSMO)+" &lCosmo&r&f in &d"+Symbols.AOE+" &finflicting "+PrintUtils.color(ObsColors.COSMO)+"&oRadiated &r&bIII &7(30m, 20s)",
				"&bEchoic Resonance&f: If &6"+Symbols.TARGET+" &fis "+PrintUtils.color(ObsColors.CELESTIO)+"&oExposed&r &e-> &6"+Symbols.TARGET+"&f instantly dies.",
				"&r&a&oRestore &r&f25 &bDurability&f & HP&c"+Symbols.HP+"&f for each &6"+Symbols.TARGET+"&f killed this way.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 30, 0.8, 50, 0.2, Particle.DRAGON_BREATH, 1f);
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 20, 0.9, 45, 0.2, Particle.DRAGON_BREATH, 0.5f);
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 10, 0.85, 30, 0.2, Particle.DRAGON_BREATH, 0.25f);
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			EntityEffects.playSound(p, Sound.ENTITY_ENDERMAN_SCREAM, SoundCategory.MASTER);
			
			AtomicInteger aInt = new AtomicInteger();
			if (!RayCastUtils.getNearbyEntities(p, 30, target -> 
			{
				if (target == null || !(target instanceof LivingEntity le)) return;
				MobData.damageUnnaturally(p, le, 2500, true, true, ElementType.COSMO, null);
				CosmoEffects.addRadiated(le, 3, 20);
				ObsParticles.drawCosmoCastSigil(le);
				if (CelestioEffects.exposedEntity.contains(le.getUniqueId())) 
				{
					MobData.getMob(le.getUniqueId()).kill();
					aInt.getAndIncrement();
					ObsParticles.drawSpiralVortex(le.getLocation(), 45, 5, 0.1, Particle.DRAGON_BREATH, 1f);
					ObsParticles.drawSpiralVortex(le.getLocation(), 45, 5, 0.1, Particle.BLOCK_CRUMBLE, Material.END_STONE.createBlockData());
					ObsParticles.drawCylinder(le.getLocation(), le.getWidth(), 3, 15, 0.5, 0.1, Particle.ENCHANT, null);
					ObsParticles.drawLandingWave(le);
				}
				
			})) return -1;

			if (aInt.get() != 0)
			{				
				EchoManager.modifyDurability(p, p.getInventory().getItemInMainHand(), DurabilityOperation.ADD, aInt.get()*25, false);
				ObsParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 15, 0.5, 0.1, Particle.ENCHANT, null);
				if (PlayerData.heal(p, aInt.get()*25, false)) ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 8, Particle.HAPPY_VILLAGER, null);
			}
			
			return 350;
		}
		
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getEntity(p, 25, target -> 
			{
				if (target == null || !(target instanceof LivingEntity le)) return;

				EntityEffects.playSound(p, Sound.ITEM_SPEAR_LUNGE_3, SoundCategory.MASTER);
				ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.8, Particle.DRAGON_BREATH, 1f);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.4, Particle.END_ROD, null);
				
				MobData.damageUnnaturally(p, le, 300, true, true, ElementType.COSMO, EchoManager.getCodec(e.getItem()));
				CelestioEffects.addExposed(le, 20);
				CosmoEffects.addVoided(le, 20);
				
				if (!EntityCategories.canAccept(le, EntityCategory.COSMO_MOBS))
				{
					EntityEffects.playSound(p, Sound.BLOCK_RESPAWN_ANCHOR_CHARGE, SoundCategory.AMBIENT);
					ObsParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 15, 0.5, 0.1, Particle.ENCHANT, null);
					EchoManager.modifyDurability(p, p.getInventory().getItemInMainHand(), DurabilityOperation.ADD, 15, false);
				}
			})) return -1;
			
			return 5;
		}
		
		return -1;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 0;
	}

}
