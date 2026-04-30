package com.lol.spells.instances.celestio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class MinorBlessing extends Spell
{

	public MinorBlessing()
	{
		super("Minor Blessing", "minor_blessing", Material.EMERALD, SpellType.SUPPORT, SpellementType.CELESTIO, CastConditions.MIXED, Rarity.TWO, 100, 3, true, true,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&eMinor Blessing&f: &e&oGrace&r&f --",
				"&r&aHeal&f &6self&f/&6target &c&oPlayer&r&f by &b&o50%&r&f current &cHP&f,",
				"&r&fand apply &eWard &bIII &7(25m, 30s | &cPVP&7: &c&o65%&r&7, &c20s&7)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&eMinor Blessing&f: &e&oEmpower&r&f --",
				"&r&fGrant &6self&f/&6target &c&oPlayer&r &e&oEmpowered&r&b II &7(45s | &cPVP&7: &c30s&7)","",
				"&r&eWard Effect&r&f: grants &b&oAbsorption&r&f, &b&oFire Resistance&r&f,",
				"&r&fand &b&oResistance&r&f equal to the magnitude of &eWard&f.","",
				"&eEmpowered Effect&f: Boosts overall attack &c&odamage&r&f and &b&ospeed&r&f.");
		
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			Entity target = RayCastUtils.getEntity(p, 25);
			if (target == null || !(target instanceof Player))
			{
				if (CelestioEffects.hasEmpowered.contains(p.getUniqueId())) return -1;
				ObsParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 6, 0.5, 0.5, Particle.ENCHANT, null);
				CelestioEffects.addEmpowered(p, 1, 45);
				return 100;
			}
			else if (target instanceof Player pTarget)
			{		
				if (CelestioEffects.hasEmpowered.contains(pTarget.getUniqueId()))
				{
					PrintUtils.PrintToActionBar(p, "&7&oPlayer already buffed..");
					return -1;		
				}
				ObsParticles.drawLine(p.getLocation(), pTarget.getLocation(), 0.4, 0.4, Particle.CLOUD, null);
				ObsParticles.drawSinLine(p.getLocation(), pTarget.getLocation(), 0.6, Particle.END_ROD, null);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(pTarget, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.AMBIENT);
					ObsParticles.drawCelestioCastSigil(pTarget);
					ObsParticles.drawCylinder(pTarget.getLocation(), pTarget.getWidth(), 3, 6, 0.5, 0.5, Particle.ENCHANT, null);
					CelestioEffects.addEmpowered(pTarget, 1, 30);
				}, 15);
				return 100;
			}
			else return -1;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			Entity target = RayCastUtils.getEntity(p, 25);
			if (target == null || !(target instanceof Player))
			{
				if (CelestioEffects.hasWard.contains(p.getUniqueId())) return -1;
				ObsParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 6, 0.5, 0.5, Particle.ENCHANT, null);
				CelestioEffects.addWard(p, 2, 30);
				EntityEffects.heal(p, p.getHealth()*0.5);
				return 25;
			}
			else if (target instanceof Player pTarget)
			{		
				if (CelestioEffects.hasWard.contains(pTarget.getUniqueId())) 
				{
					PrintUtils.PrintToActionBar(p, "&7&oPlayer already buffed..");
					return -1;		
				}
				ObsParticles.drawLine(p.getLocation(), pTarget.getLocation(), 0.4, 0.4, Particle.CLOUD, null);
				ObsParticles.drawSinLine(p.getLocation(), pTarget.getLocation(), 0.6, Particle.END_ROD, null);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(pTarget, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.AMBIENT);
					ObsParticles.drawCelestioCastSigil(pTarget);
					ObsParticles.drawCylinder(pTarget.getLocation(), pTarget.getWidth(), 3, 6, 0.5, 0.5, Particle.ENCHANT, null);
					CelestioEffects.addWard(pTarget, 2, 20);
					EntityEffects.heal(pTarget, pTarget.getHealth()*0.65);
				}, 15);
				return 25;
			}
			else return -1;
		}
		
		return 0;
	}

	@Override
	public int getTotalManaCost()
	{
		return 25;
	}

}
