package com.lol.spells.instances.aero;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.AeroEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Thunderstorm extends Spell
{

	public Thunderstorm() 
	{
		super("Thunderstorm", "thunderstorm", Material.AMETHYST_CLUSTER, SpellType.OFFENSIVE, SpellementType.AERO, CastConditions.MIXED, Rarity.FIVE, 250, 5, true, true,
				"&ePrimary&f "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&dThunderstorm&f: &d&oElectric Expanse&r&f --",
				"&r&fSummon an &dAOE&f storm &7(20m)&f inflicting &dShock &7(20s / &cPVP&7: &c3s&r&7)","",
				"&eSecondary&f "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&dThunderstorm&f: &d&oLightning Rod&r&f --",
				"&r&fStrike your target &7(40m)&f with lightning, inflicting &dStatic &7(10s / &cPVP&7: &c5s&r&7)",
				"&r&fwhich then spreads to nearby enemies &7(25m)&f applying &dShock &7(20s / &cPVP&7: &c3s&r&7)","",
				"&r&dStatic &eEffect&f: causes mobs to emit shockwaves in a 10m radius.",
				"&r&fAffected mobs receive &b&o1.25x &r"+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f damage.",
				PrintUtils.color(ObsColors.AERO)+"&lAero&r&f-based mobs are &e&oimmune&r&f, and &a&ohealed&r&f instead.","",
				"&r&dShock &eEffect&f: Affected are &6&oStunned&r&f, &e&oGlow&r&f, and take &b&o25% more "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f damage.");
	}

	boolean nullEntities = false;
	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			Entity target = RayCastUtils.getEntity(p, 40);
			if (target == null || !(target instanceof LivingEntity)) return -1;

			EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.AMBIENT);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.ELECTRIC_SPARK, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				target.getWorld().strikeLightning(target.getLocation());
				ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.ELECTRIC_SPARK, null);
				AeroEffects.addStatic((LivingEntity) target, p, target instanceof Player ? 5 : 10);
			}, 15);
			RayCastUtils.getNearbyEntities(target, 25, (C)->
			{
				ObsParticles.drawLine(target.getLocation(), C.getLocation(), 0.5, 0.5, Particle.ELECTRIC_SPARK, null);
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.ELECTRIC_SPARK, null);
					C.getWorld().strikeLightning(C.getLocation());
					AeroEffects.addShock(C, C instanceof Player ? 3 : 20);						
				}, 30);
			});
			return this.getManacost();
		}
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.AMBIENT);
			
			if (!RayCastUtils.getNearbyEntities(p, 20, (C)->
			{
				ObsParticles.drawLine(p.getLocation(), C.getLocation(), 0.5, 0.5, Particle.ELECTRIC_SPARK, null);
				C.getWorld().strikeLightning(C.getLocation());
				AeroEffects.addShock(C, C instanceof Player ? 3 : 20);
			})) 
			{
				PrintUtils.PrintToActionBar(p, "Fizzle!");
				return -1;
			}
			return this.getManacost();
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}
}
