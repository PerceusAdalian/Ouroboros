package com.lol.spells.instances.aero;

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
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.AeroEffects;
import com.ouroboros.utils.entityeffects.ArcanoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Vaporize extends Spell 
{

	public Vaporize()
	{
		super("Vaporize", "vaporize", Material.AMETHYST_CLUSTER, SpellType.ULTIMATE, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.FIVE, 450, 3.5, true, true,
				"&r&fSend a bolt of kilowatt lightning at your &6&otarget&r&7 (40m)",
				"&r&fdealing 100&c♥&f of "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f damage &7(&cPVP&7: &c18♥&7)",
				"&r&fIf the &6&otarget&r&f survives, inflict &bEther Overload&f, &bEther Disruption&f, and &dShock&7 (30s)","",
				"&r&bEther Overload &eEffect&f: affected take an additional &b&o50% &r&e&oelemental &r&fdamage.",
				"&r&bEther Disruption &eEffect&f: affected &c&oPlayer&r&f(s) can't cast &e&oSpells&r&f.",
				"&r&dShock &eEffect&f: Affected are &6&oStunned&r&f, &e&oGlow&r&f, and take &b&o25% more "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f damage.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (!RayCastUtils.getEntity(p, 40, target ->
		{
			if (target == null || !(target instanceof LivingEntity)) return;
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawCosLine(p.getLocation(), target.getLocation(), 0.6, Particle.CRIT, null);
			ObsParticles.drawSinLine(p.getLocation(), target.getLocation(), 0.7, Particle.BLOCK_CRUMBLE, Material.AMETHYST_CLUSTER.createBlockData());
			ObsParticles.drawCosLine(p.getLocation(), target.getLocation(), 0.9, Particle.CLOUD, null);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.6, 0.4, Particle.END_ROD, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				for (int i = 0; i < 4; i++)
				{
					target.getWorld().strikeLightningEffect(target.getLocation());
				}
				
				MobData.damageUnnaturally(p, target, target instanceof Player ? 18 : 100, true, true, ElementType.AERO);
				
				if (!target.isDead() || MobData.getMob(target.getUniqueId()) != null)
				{
					if (target instanceof Player) ArcanoEffects.addEtherDisruption((Player) target, 30);
					ArcanoEffects.addEtherOverload((LivingEntity) target, 30);
					AeroEffects.addShock((LivingEntity) target, 30);
				}
				
			}, 15);
			
		})) return -1;
		return 450;
	}

	@Override
	public int getTotalManaCost()
	{
		return 450;
	}

}
