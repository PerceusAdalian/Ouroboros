package com.eol.echoes.abilities.instances.special;

import java.util.concurrent.atomic.AtomicInteger;

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
import com.google.common.util.concurrent.AtomicDouble;
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
import com.ouroboros.utils.entityeffects.EntityEffects;

public class DivineIntegrity extends EchoAbility
{

	public DivineIntegrity()
	{
		super("Divine Integrity", "divine_integrity", Material.NETHER_STAR, StatType.MELEE, 0, 0, 0, AbilityType.ULTIMATE, ElementType.CELESTIO,
				CastConditions.MIXED, EchoForm.HAMMER, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.CELESTIO)+"Divine Integrity&f: "+PrintUtils.color(ObsColors.CELESTIO)+"&oRighteousness&r&f -- &cRemove &f5 &bDurability",
				"&r&fRush &6"+Symbols.TARGET+" &fdealing "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f damage equal to",
				"&r&f&oBase Atk &r&fx &oCrit Modifier&r&f applying "+PrintUtils.color(ObsColors.CELESTIO)+"&oHumility &r&7(7m, 30s)",
				"&r&bEchoic Resonance&r&f: Increased damage multiplied by stacks of "+PrintUtils.color(ObsColors.CELESTIO)+"&oDivine Favor","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.CELESTIO)+"Divine Integrity&f: "+PrintUtils.color(ObsColors.CELESTIO)+"&oIustus' Prayer&r&f -- &cRemove &f45 &bDurability",
				"&r&fGrant &6Self&r&f stacks of "+PrintUtils.color(ObsColors.CELESTIO)+"&oDivine Favor&r&f for each &6"+Symbols.TARGET+" &fin &d"+Symbols.AOE+" &fwith "+PrintUtils.color(ObsColors.CELESTIO)+"&oHumility &r&7(20m)",
				"&r&bEchoic Resonance&r&f: If &6"+Symbols.TARGET+" &fis"+PrintUtils.color(ObsColors.MORTIO)+" &oUndead&r&e -> &a&oHeal &r&6Self &f&l25&c"+Symbols.HP);
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			int divineFavorStacks = CelestioEffects.divineFavorRegistry.containsKey(p.getUniqueId()) ? CelestioEffects.divineFavorRegistry.get(p.getUniqueId()).magnitude : 0;
			AtomicInteger stackCount = new AtomicInteger();
			AtomicInteger mortioCount = new AtomicInteger();
			if (!RayCastUtils.getNearbyEntities(p, 20, target -> 
			{
				if (target == null || !(target instanceof LivingEntity le)) return;
				
				stackCount.getAndIncrement();
				if (EntityCategories.canAccept(le, EntityCategory.MORTIO_MOBS)) mortioCount.getAndIncrement();
				ObsParticles.drawCelestioCastSigil(le);
				
			})) return -1;
			
			EntityEffects.playSound(p, Sound.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.AMBIENT);
			ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 10, Particle.BLOCK_CRUMBLE, Material.GOLD_BLOCK.createBlockData());
			int newStackCount = stackCount.get() + divineFavorStacks;
			CelestioEffects.addDivineFavor(p, newStackCount, 30);
			PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.CELESTIO)+"Divine Favor&f: &7"+divineFavorStacks+" -> "+PrintUtils.color(ObsColors.CELESTIO)+"&l"+newStackCount);
			if (mortioCount.get() > 0)
			{
				PlayerData.heal(p, mortioCount.get()*25, false);
				EntityEffects.playSound(p, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, SoundCategory.AMBIENT);
				ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 7, Particle.HAPPY_VILLAGER, null);
			}
			return 45;
		}

		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getEntity(p, 7, target -> 
			{
				if (target == null || !(target instanceof LivingEntity le)) return;
				
				int divineFavorStacks = CelestioEffects.divineFavorRegistry.containsKey(p.getUniqueId()) ? CelestioEffects.divineFavorRegistry.get(p.getUniqueId()).magnitude : 0;
				EchoManifest codec = EchoManager.getCodec(e.getItem());
				double damage = codec.baseStats().getAttack() * codec.baseStats().getCritModifier();
				if (divineFavorStacks > 0) damage *= divineFavorStacks;
				AtomicDouble damageDelta = new AtomicDouble();
				damageDelta.getAndAdd(damage);
				CelestioEffects.divineFavorRegistry.remove(p.getUniqueId());
				
				ObsParticles.drawCosLine(p.getLocation(), le.getLocation(), 0.5, Particle.WAX_ON, null);
				ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.8, Particle.CLOUD, null);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{	
					EntityEffects.rushEntity(p, le, 3);
				}, 6);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
				{
					EntityEffects.playSound(p, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, SoundCategory.AMBIENT);
					ObsParticles.drawLandingWave(le);
					ObsParticles.drawCelestioCastSigil(le);
					MobData.damageUnnaturally(p, le, damageDelta.get(), true, true, ElementType.CELESTIO, codec);
					CelestioEffects.addHumility(le, 30);
				}, 10);
				
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
