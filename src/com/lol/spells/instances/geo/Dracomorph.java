package com.lol.spells.instances.geo;

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
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GeoEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class Dracomorph extends Spell
{

	public Dracomorph() 
	{
		super("Dracomorph", "dracomorph", Material.NETHERITE_INGOT, SpellType.ULTIMATE, SpellementType.GEO, CastConditions.MIXED, Rarity.FIVE, 300, 120, false,
				"&r&b&oElemental Fusion&f: "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f --",
				"&r&e&oPrimary &r&f" + PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&6Dracomorph: &c&oCharred &r&6&oBulwark&r&f --",
				"&r&fApply &cInfernal Body&f, &6Guarded &bVIII&f, and &6Barbed &bX&f to &6self &7(1min)","",
				"&r&e&oSecondary &r&f" + PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&6Dracomorph&f: &c&oDraconic &6&oStomp&r&f --",
				"&r&fErrupt an earthquake that deals 30&c♥&f "+PrintUtils.color(ObsColors.GEO)+"&lGeo&r&f damage,",
				"&r&fand inflicts &cCharred&f and &6Sanded&f to all within a &dradial AOE &7(45m, 30s)","",
				"&r&6Guarded &eEffect&f: Halves incoming damage for 8 instances.",
				"&r&6Barbed &eEffect&f: Incoming damage is redirected as "+PrintUtils.color(ObsColors.GEO)+"&lGeo&r&f damage",
				"&r&fequal to the &b&omagnitude&r&f of &6Barbed&f pre &6Guarded&f procs.",
				"&r&6Sanded &eEffect&f: Affected are &b&oSlowed&r&f and &b&oBlinded&r&f.","",
				"&r&cInfernal Body &eEffect&f: Inflicts &cBurn&f for &b&o10 seconds&r&f when applicant is hit.",
				"&r&cCharred &eEffect&f: Causes &b&oHunger&r&f, &b&oFatigue&r&f, and &b&oSlowness&r&f, while affected take &b&o25%&r&f",
				"&r&fmore "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno &r&fdamage, and may cause &cBurn&f upon hit removing the effect.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getNearbyEntities(p, 45, target ->
			{
				if (!(target instanceof LivingEntity le) || target instanceof Player) return;
				
				MobData.damageUnnaturally(p, le, 30, true, true, ElementType.GEO);
				GeoEffects.addSanded(le, 30);
				InfernoEffects.addCharred(le, 30);
				ObsParticles.drawGeoCastSigil(le);
				ObsParticles.drawInfernoCastSigil(le);
			})) return -1;
			
			EntityEffects.playSound(p, Sound.ENTITY_RAVAGER_ROAR, SoundCategory.MASTER);
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 30, .45, 25, Particle.BLOCK_CRUMBLE, Material.DIRT.createBlockData());
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 30, .35, 25, Particle.BLOCK_CRUMBLE, Material.STONE.createBlockData());
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 30, .35, 25, Particle.SMOKE, null);
			
			return 300;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (!GeoEffects.addGuarded(p, 7, 60)) return -1;
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.MASTER);
			EntityEffects.playSound(p, Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT);
			GeoEffects.addBarbed(p, 10, 60);
			InfernoEffects.addInfernalBody(p, 60);
			return 300;
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 300;
	}

}
