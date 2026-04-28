package com.lol.spells.instances.arcano;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.lol.spells.instances.celestio.Cure;
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
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class AspectOfLordran extends Spell
{

	public AspectOfLordran() 
	{
		super("Aspect of Lordran, Forgotten King", "aspect_of_lordran", Material.GLOBE_BANNER_PATTERN, 
				SpellType.ULTIMATE, SpellementType.ARCANO, CastConditions.MIXED, Rarity.SEVEN, 50, 0.1, false,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Aspect of Lordan&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oNobility Incarnate&r&f --",
				"&r&7&l┏--&r&7{&e✧ &oArbanian Combo&f: unleash a volley of "+PrintUtils.color(ObsColors.ARCANO)+"&lArcane&r&f spells.",
				"&r&7&l┗┳- &r&f1. "+PrintUtils.color(ObsColors.ARCANO)+"Regal Axiom &7(&6⌖&7: 25&c♥&7, 30m)",
				"&r&7&l ┗┳- &r&f2. "+PrintUtils.color(ObsColors.ARCANO)+"Royal Escapade &7(&6⌖&7: 35&c♥&7, 30m)",
				"&r&7&l  ┗┳- &r&f3. "+PrintUtils.color(ObsColors.ARCANO)+"Dominion Cascade &7(&6⌖&7: 20&c♥&7, 30m)",
				"&r&7&l   ┗--&r&7{&r&e✧ &f4. "+PrintUtils.color(ObsColors.ARCANO)+"Crowned Imperion &7(&6⌖&7: 50&c♥&7, 30m, &6Break&7)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Aspect of Lordran&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oThe King's Return&r&f --",
				"&r&fIncoming damage that would otherwise cause &4&lDeath&r&f is &b&onullified&r&f &7(30s)",
				"&r&fUpon resurrection, activate &e&oCure&r&f to self, &arestore &cHP&f to &b&o100%&r&f and gain &eWard &bV &7(20s)","",
				"&r&bEchoic Dissonance&r&f: ",
				"&r&fCasting "+PrintUtils.color(ObsColors.ARCANO)+"Crowned Imperion&r&f: &lCD &r&e->&r&f &b4.5s&f, &f&lMC &r&e->&r&b 200",
				"&r&e&oSecondary Cast&r&f: &lCD &r&e->&r&f &b3min&r&f, &f&lMC &r&e-> &b300&f. Requires an "+PrintUtils.color(ObsColors.ARCANO)+"&lArcano &r&fattuned&r&f wand.");
	}

	private static Set<UUID> cooldown_primary = new HashSet<>();
	private static Set<UUID> cooldown_secondary = new HashSet<>();
	private static Set<UUID> shouldRevive = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (cooldown_secondary.contains(uuid))
			{
				PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"&oThe King's Return &r&f on Cooldown!");
				return -1;
			}
			if (shouldRevive.contains(uuid)) return -1;
			
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_ACTIVATE, SoundCategory.MASTER);
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 15, 0.8, 20, Particle.BLOCK_CRUMBLE, Material.PRISMARINE_BRICKS.createBlockData());
			ObsParticles.drawCylinder(p.getLocation(), p.getWidth()+3, 3, 10, 0.9, 0.1, Particle.ENCHANT, null);
			ObsParticles.drawCylinder(p.getLocation(), p.getWidth()+2, 2, 15, 0.6, 0.1, Particle.ENCHANT, null);
			
			shouldRevive.add(uuid);
			cooldown_secondary.add(uuid);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				if (shouldRevive.contains(uuid))
				{					
					PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"&oThe King's Return&r&7: effect ended.");
					EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.AMBIENT);
					ObsParticles.drawArcanoCastSigil(p);
					shouldRevive.remove(uuid);
				}
			}, 600);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> cooldown_secondary.remove(uuid), 3600);
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (cooldown_primary.contains(uuid))
			{
				PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"&oNobility Incarnate&r&f on Cooldown!");
				return -1;
			}
			
			// Regal Axiom
			if (ComboData.build(uuid, this, 1, false, c -> 
			{
				Entity target = RayCastUtils.getEntity(p, 30);
				if (target == null || !(target instanceof LivingEntity le) || target instanceof Player) return false;
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
				ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.6, Particle.GLOW_SQUID_INK, null);
				ObsParticles.drawCosLine(p.getLocation(), le.getLocation(), 0.4, Particle.BUBBLE_COLUMN_UP, null);
				MobData.damageUnnaturally(p, le, 25, true, true, ElementType.ARCANO);
				return true;
			}, e)) return 50;
			
			// Royal Escapade
			if (ComboData.build(uuid, this, 2, false, c -> 
			{
				Entity target = RayCastUtils.getEntity(p, 30);
				if (target == null || !(target instanceof LivingEntity le) || target instanceof Player) return false;
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 3, -35, 1.5, Particle.GLOW_SQUID_INK, null);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 2, -45, 1.2, Particle.BUBBLE_COLUMN_UP, null);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.6, 3, 215, 1.5, Particle.GLOW_SQUID_INK, null);
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 2, 225, 1.2, Particle.BUBBLE_COLUMN_UP, null);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.7, 0.5, Particle.CRIT, null);
					ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.4, Particle.BUBBLE_COLUMN_UP, null);
					MobData.damageUnnaturally(p, le, 35, true, true, ElementType.ARCANO);
				}, 4);
				return true;
			}, e)) return 50;
		
			// Dominion Cascade
			if (ComboData.build(uuid, this, 3, false, c -> 
			{
				Entity target = RayCastUtils.getEntity(p, 30);
				if (target == null || !(target instanceof LivingEntity le) || target instanceof Player) return false;
				EntityEffects.playSound(p, Sound.ENTITY_BREEZE_SHOOT, SoundCategory.AMBIENT);
				ObsParticles.drawX(le.getLocation(), p.getEyeLocation().toVector(), 10, 0.8, 0.4, false, Particle.GLOW_SQUID_INK, null);
				ObsParticles.drawX(le.getLocation(), p.getEyeLocation().toVector(), 8.5, 0.8, 0.5, false, Particle.BUBBLE_COLUMN_UP, null);
				ObsParticles.drawX(le.getLocation(), p.getEyeLocation().toVector(), 8.5, 0.8, 0.45, false, Particle.CRIT, null);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					MobData.damageUnnaturally(p, le, 20, true, true, ElementType.ARCANO);
				}, 4);
				return true;
			}, e)) return 50;
			
			if (ComboData.build(uuid, this, 4, true, c -> 
			{
				Entity target = RayCastUtils.getEntity(p, 30);
				if (target == null || !(target instanceof LivingEntity le) || target instanceof Player) return false;
				
				EntityEffects.playSound(p, Sound.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.AMBIENT);
				ObsParticles.drawSpiralVortex(le.getLocation(), 45, 3, 0.1, Particle.GLOW_SQUID_INK, null);
				ObsParticles.drawSpiralVortex(le.getLocation(), 30, 2, 0.1, Particle.CRIT, null);
				MobData.damageUnnaturally(p, le, 50, true, true, ElementType.ARCANO);

				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					ObsParticles.drawWave(Ouroboros.instance, le.getLocation(), 10, 0.65, 20, Particle.BLOCK_CRUMBLE, Material.CYAN_TERRACOTTA.createBlockData());
					ObsParticles.drawWave(Ouroboros.instance, le.getLocation(), 11, 0.65, 20, Particle.BLOCK_CRUMBLE, Material.BLUE_WOOL.createBlockData());
					ObsParticles.drawWave(Ouroboros.instance, le.getLocation(), 9, 0.65, 20, Particle.BLOCK_CRUMBLE, Material.PRISMARINE.createBlockData());
					ObsParticles.drawCylinder(le.getLocation(), le.getWidth()+4, 5, 15, 0.5, 0.1, Particle.ENCHANT, null);
					if (MobData.getMob(le.getUniqueId()) != null) MobData.getMob(le.getUniqueId()).setBreak();
				}, 4);
				
				return true;
			}, e)) {
				cooldown_primary.add(uuid);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> cooldown_primary.remove(uuid), 90);
				return 200;
			};
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 50;
	}

	public static void registerSpellHelper(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void onDeath(EntityDamageByEntityEvent e)
			{
				if (e.getEntity() instanceof Player p && shouldRevive.contains(p.getUniqueId()))
				{
					if (e.getFinalDamage() > p.getHealth())
					{
						e.setCancelled(true);
						PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"&oThe King's Return &r&a&oactivated&r&f!");
						EntityEffects.playSound(p, Sound.ITEM_TOTEM_USE, SoundCategory.AMBIENT);
						Cure.playSpellEffect(p, p);
						for (PotionEffect effect : p.getActivePotionEffects())
						{
							if (EntityEffects.debuffs.contains(effect.getType())) continue;
							p.removePotionEffect(effect.getType());
						}
						p.setHealth(((Attributable) p).getAttribute(Attribute.MAX_HEALTH).getBaseValue());
						CelestioEffects.addWard(p, 4, 20);
						shouldRevive.remove(p.getUniqueId());
					}
				}
			}
		}, plugin);
	}
	
}
