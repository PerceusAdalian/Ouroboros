package com.lol.spells.instances.mortio;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;

public class Voodoo extends Spell
{

	public Voodoo()
	{
		super("Voodoo", "voodoo", Material.TOTEM_OF_UNDYING, SpellType.CURSE, SpellementType.MORTIO, CastConditions.MIXED, Rarity.FIVE, 100, 2, false,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&4Voodoo&f: &4&oSoul Harvest&r&f --",
				"&r&fHarvest the souls of targets infront of you &7(15m)&f as a &d&oConal AOE&r&f",
				"&r&fdealing "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f damage equal to 10 + &b&o20%&r&f current &aHP&f.",
				"&r&fGain stacks of &4Jinx&f equal to number of targets hit.","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&4Voodoo&f: &4&oHarrowing Miasma&r&f --",
				"&r&fUnleash a burst of toxic miasma that deals "+PrintUtils.color(ObsColors.CORROSIVE)+"Corrosive&f damage",
				"&r&fscaled with the number of &4Jinx&f stacks, removing them, and inflicting &4Dread &7(25m, 20s)","",
				"&4Dread &eEffect&f: Applies a debilitation that causes &b&ohunger&r&f and &b&oblindness&r&f",
				"&r&fto those afflicted. Dread is &e&ocurable&r&f and does not stack, however",
				"&r&fsubsequent applications will cause &4Doom&f after a second application.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getNearbyEntities(p, 25, target ->
			{
				if (target == null || !(target instanceof LivingEntity le) || target instanceof Player) return;
				
				int damage = MortioEffects.jinxRegistry.containsKey(p.getUniqueId()) ? MortioEffects.jinxRegistry.get(p.getUniqueId()).magnitude : 0;
				OBSParticles.drawCosLine(p.getLocation(), le.getLocation(), 0.4, Particle.CRIMSON_SPORE, null);
				OBSParticles.drawDisc(le.getLocation(), le.getWidth(), 1, 6, 0.5, Particle.DUST_PLUME, null);
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_AMBIENT_OMINOUS, SoundCategory.AMBIENT);
					OBSParticles.drawMortioCastSigil(le);
					OBSParticles.drawWisps(le.getLocation(), le.getWidth(), le.getHeight(), 8, Particle.POOF, null);
					OBSParticles.drawWisps(le.getLocation(), le.getWidth(), le.getHeight(), 3, Particle.SMOKE, null);
					OBSParticles.drawWisps(le.getLocation(), le.getWidth(), le.getHeight(), 5, Particle.ASH, null);
					MobData.damageUnnaturally(p, le, damage, true, true, ElementType.CORROSIVE);
					MortioEffects.addDread(target, 20);
				}, 15);
			})) return -1;
			return 100;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			AtomicInteger targetCount = new AtomicInteger(0);
			
			if (!RayCastUtils.getEntitiesInFov(p, 25, target -> 
			{
				if (target == null || !(target instanceof LivingEntity le) || target instanceof Player) return;
				
				targetCount.incrementAndGet();
				
				OBSParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.5, Particle.CRIMSON_SPORE, null);
				OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.6, 0.4, Particle.ASH, null);
				OBSParticles.drawLine(p.getLocation(), le.getLocation(), 0.3, 0.6, Particle.SMOKE, null);
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
				MobData data = MobData.getMob(le.getUniqueId());
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					if (data == null || data.isDead()) return;
					EntityEffects.playSound(p, Sound.ITEM_BONE_MEAL_USE, SoundCategory.AMBIENT);
					OBSParticles.drawMortioCastSigil(le);
					OBSParticles.drawCosLine(le.getLocation(), p.getLocation(), 0.6, Particle.CRIMSON_SPORE, null);
					OBSParticles.drawCosLine(le.getLocation(), p.getLocation(), 0.6, Particle.SMOKE, null);
					MobData.damageUnnaturally(p, le, (data.getHp(false) * 0.2) + 10, true, true, ElementType.MORTIO);
				}, 15);
			})) return -1;
			
			MortioEffects.addJinxStacks(p, targetCount.get());
			return 100;
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost()
	{
		return 100;
	}

}
