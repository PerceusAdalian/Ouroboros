package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.EchoData;
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
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.AeroEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class AspectOfThor extends EchoAbility
{

	public AspectOfThor()
	{
		super("Aspect of Thor", "aspect_of_thor", Material.NETHER_STAR, StatType.MELEE, 0, 0, 0, AbilityType.ULTIMATE, ElementType.AERO,
				CastConditions.MIXED, EchoForm.HAMMER, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.AERO)+"Aspect of Thor&f: "+PrintUtils.color(ObsColors.AERO)+"&oMjolnir's Call&r&f -- &cRemove &f25 &b&oDurability",
				"&r&fRush &6"+Symbols.TARGET+" &finflicting &d&oShock&r&f and dealing "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f damage",
				"&r&fequal to &e&oMjolnir's &r&f&oBase Attack&r&f multiplied by its &oCritical Modifier&r&7 (30m, 10s)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.AERO)+"Aspect of Thor&f: "+PrintUtils.color(ObsColors.AERO)+"&oMjolnir's Wrath&r&f -- &cRemove &f50 &b&oDurability",
				"&r&d&lSmite &r&6"+Symbols.TARGET+" &finflicting &d&oStatic&r&f and dealing "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f damage",
				"&r&fequal to &b&o350% &e&oMjolnir's &f&oBase Attack&r&f multiplied by its &oCritical Modifier&r&7 (30m, 7s)","",
				"&r"+PrintUtils.color(ObsColors.AERO)+"Shock &eEffect&f: Affected are &6&oStunned&r&f, &e&oGlow&r&f, and take &b&o25% more "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f damage.",
				"&r"+PrintUtils.color(ObsColors.AERO)+"Static &eEffect&f: causes mobs to emit shockwaves in a 10m radius.",
				"&r&fAffected mobs receive &b&o1.25x &r"+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f damage.",
				PrintUtils.color(ObsColors.AERO)+"&lAero&r&f-based mobs are &e&oimmune&r&f, and &a&ohealed&r&f instead.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		// Should always be Mjolnir's cast on hand for the ability; no cheating.
		EchoManifest codec = EchoManager.getCodec(p.getInventory().getItemInMainHand());
		if (codec == null) return -1;
		EchoData stats = codec.baseStats();		
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getEntity(p, 30, target -> 
			{	
				if (target == null || !(target instanceof LivingEntity le)) return;
				
				EntityEffects.playSound(p, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.AMBIENT);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.4, Particle.CRIT, null);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.3, 0.4, Particle.CLOUD, null);
				ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.7, Particle.BLOCK_CRUMBLE, Material.AMETHYST_CLUSTER.createBlockData());
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					ObsParticles.drawAeroCastSigil(p);
					ObsParticles.drawWave(Ouroboros.instance, le.getLocation(), 30, 0.8, 45, 0.2, Particle.BLOCK_CRUMBLE, Material.AMETHYST_BLOCK.createBlockData());
					EntityEffects.playSound(p, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.MASTER);
					p.getWorld().strikeLightningEffect(le.getLocation());
					MobData.damageUnnaturally(p, target, (stats.getAttack()*3.5)*stats.getCritModifier(), true, true, ElementType.AERO, codec);
					AeroEffects.addStatic(le, p, 7);
				}, 12);
			})) return -1;
			
			return 50;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getEntity(p, 30, target -> 
			{
				if (target == null || !(target instanceof LivingEntity le)) return;
				
				EntityEffects.playSound(p, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.AMBIENT);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.4, Particle.CRIT, null);
				ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.3, 0.4, Particle.CLOUD, null);
				ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.7, Particle.BLOCK_CRUMBLE, Material.AMETHYST_CLUSTER.createBlockData());
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					ObsParticles.drawAeroCastSigil(p);
					ObsParticles.drawWave(Ouroboros.instance, le.getLocation(), 30, 0.8, 45, 0.2, Particle.BLOCK_CRUMBLE, Material.AMETHYST_BLOCK.createBlockData());
					EntityEffects.playSound(p, Sound.ITEM_MACE_SMASH_GROUND_HEAVY, SoundCategory.MASTER);
					p.teleport(le);
					MobData.damageUnnaturally(p, target, stats.getAttack()*stats.getCritModifier(), true, true, ElementType.AERO, codec);
					AeroEffects.addShock(le, 10);
				}, 12);
			})) return -1;
			
			return 25;
		}
		
		return -1;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 0;
	}

}
