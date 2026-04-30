package com.lol.spells.instances.inferno;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.lol.wand.Wand;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ComboData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.TimeUtils;
import com.ouroboros.utils.TimeUtils.Timeframe;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class AspectOfAighil extends Spell
{

	public AspectOfAighil()
	{
		super("Aspect of Aighil, The Gentle", "aspect_of_aighil", Material.MUSIC_DISC_LAVA_CHICKEN, SpellType.ULTIMATE, SpellementType.INFERNO, CastConditions.MIXED, Rarity.SIX, 50, 0.1, false, true,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&cAspect of Aighil&f: &c&oDraconic Lore&r&f --",
				"&r&7&l┏--&r&7{&e✧ &oArbanian Combo&f: unleash a volley of "+PrintUtils.color(ObsColors.INFERNO)+"&lInfernal&r&f projectiles.",
				"&r&7&l┗┳- &r&f1. "+PrintUtils.color(ObsColors.INFERNO)+"Spitfire",
				"&r&7&l ┗┳- &r&f2. "+PrintUtils.color(ObsColors.INFERNO)+"Dragons' Flame",
				"&r&7&l  ┗┳- &r&f3. "+PrintUtils.color(ObsColors.INFERNO)+"Dragons' Glory",
				"&r&7&l   ┗--&r&7{&r&e✧ &f4. "+PrintUtils.color(ObsColors.INFERNO)+"Aighil's Meteor &r&7(&6Targeted&r&7: 30m, &e&oDay Bonus&7)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&cAspect of Aighil&f: &c&oAighil's Roar&r&f --",
				"&r&fErupt a deafening roar dealing 40&c♥ "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f damage,",
				"&r&fand inflict &cCharred &7(20s)&f to &6&oall targets&r&f around you within &b&o30 meters&r&f.","",
				"&r&cCharred &eEffect&f: Causes &b&oHunger&r&f, &b&oFatigue&r&f, and &b&oSlowness&r&f, while affected take &b&o25%&r&f",
				"&r&fmore "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno &r&fdamage, and may cause &cBurn&f upon hit removing the effect.","",
				"&r&bEchoic Dissonance&r&f: ",
				"&r&fCasting "+PrintUtils.color(ObsColors.INFERNO)+"Aighil's Meteor&r&f: &lCD &r&e->&r&f &b10s&f, &f&lMC &r&e->&r&b 200",
				"&r&e&oSecondary Cast&r&f: &lCD &r&e->&r&f &b15s&r&f, &f&lMC &r&e-> &b300&f. Requires an "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno &r&fattuned&r&f wand.");
	}

	private static Set<UUID> cooldown_primary = new HashSet<>();
	private static Set<UUID> cooldown_secondary = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			
			if (cooldown_secondary.contains(uuid))
			{
				PrintUtils.PrintToActionBar(p, "&r&c&oAighil's Roar&r&f on Cooldown!");
				return -1;
			}
			
			Wand wand = new Wand(e.getItem());
			if (wand.getElementType() == null || wand.getElementType() != ElementType.INFERNO) 
			{
				PrintUtils.PrintToActionBar(p, "&c&oFizzle!");
				return -1;
			}
			
			EntityEffects.playSound(p, Sound.ENTITY_ENDER_DRAGON_AMBIENT, SoundCategory.AMBIENT);
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 20, 0.30, 7, Particle.LAVA, null);
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 23, 0.25, 4, Particle.SMOKE, null); 
			
			if (!RayCastUtils.getNearbyEntities(p, 30, target ->
			{
				if (target == null || !(target instanceof LivingEntity le) || (target instanceof Player)) return;
				
				MobData.damageUnnaturally(p, le, 40, true, true, ElementType.INFERNO);
				InfernoEffects.addCharred(le, 20);
			})) return -1;
			
			cooldown_secondary.add(uuid);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> cooldown_secondary.remove(uuid), 300);
			return 300;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (cooldown_primary.contains(uuid))
			{
				PrintUtils.PrintToActionBar(p, "&r&c&oDraconic Lore&r&f on Cooldown!");
				return -1;
			}
			
			// Spitfire
			if(ComboData.build(uuid, this, 1, false, c ->
			{
				EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
				Fireball fb = (Fireball) p.getWorld().spawnEntity(p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(1.8)), EntityType.FIREBALL);
				fb.setYield(1);
			}, e)) return 50;
			
			// Dragons' Flame
			if (ComboData.build(uuid, this, 2, false, c ->
			{
				EntityEffects.playSound(p, Sound.ENTITY_WITHER_SHOOT, SoundCategory.AMBIENT);
				LargeFireball fb = (LargeFireball) p.getWorld().spawnEntity(p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(1.1)), EntityType.FIREBALL);
				fb.setYield(3);
				fb.setIsIncendiary(true);
			}, e)) return 50;
			
			// Dragons' Glory
			if(ComboData.build(uuid, this, 3, false, c ->
			{
				Location eyeLoc = p.getEyeLocation();
				Vector direction = eyeLoc.getDirection().normalize();
				
				Vector right = direction.clone().crossProduct(new Vector(0, 1, 0)).normalize();

				Vector offset1 = right.clone().multiply(2.5).add(new Vector(0, 2.15, 0));  // right + up
				Vector offset2 = right.clone().multiply(-2.5).add(new Vector(0, 2.15, 0)); // left + up
				Vector offset3 = right.clone().multiply(0).add(new Vector(0,2.5,0));
				
				Location spawnLoc1 = eyeLoc.clone().add(offset1);
				Location spawnLoc2 = eyeLoc.clone().add(offset2);
				Location spawnLoc3 = eyeLoc.clone().add(offset3);
				
				EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
				SmallFireball fb1 = (SmallFireball) p.getWorld().spawnEntity(spawnLoc2, EntityType.SMALL_FIREBALL);
				fb1.setVelocity(direction.clone().multiply(2));

				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
					SmallFireball fb2 = (SmallFireball) p.getWorld().spawnEntity(spawnLoc1, EntityType.SMALL_FIREBALL);
					fb2.setVelocity(direction.clone().multiply(2));
				}, 3);
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(p, Sound.ENTITY_WITHER_SHOOT, SoundCategory.AMBIENT);
					Fireball fb3 = (Fireball) p.getWorld().spawnEntity(spawnLoc3, EntityType.FIREBALL);
					fb3.setVelocity(direction.clone().multiply(2));
					fb3.setYield(2);
				}, 6);
			}, e)) return 50;
				
			// Aighil's Meteor
			if (ComboData.build(uuid, this, 4, true, c ->
			{
				Block target = RayCastUtils.rayTraceBlock(p, 30);
				if (target != null && !target.getType().equals(Material.AIR)) 
				{
					ObsParticles.drawSinLine(p.getLocation(), target.getLocation(), 0.6, Particle.LAVA, null);
					EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
					Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
					{
						EntityEffects.playSound(p, Sound.ENTITY_ENDER_DRAGON_GROWL, SoundCategory.AMBIENT);
						Meteor.playSpellEffects(p, target.getLocation(), 4, TimeUtils.checkTime(p.getWorld(), Timeframe.DAY), 3, true, Meteor.MeteorSize.LARGE);
					}, 15);
				}
			}, e)) {
				
				cooldown_primary.add(uuid);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> cooldown_primary.remove(uuid), 200);
				return 200;
			};
			
			return -1;
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost()
	{
		return 50;
	}

}
