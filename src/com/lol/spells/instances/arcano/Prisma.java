package com.lol.spells.instances.arcano;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import com.ouroboros.mobs.EntityCategories;
import com.ouroboros.mobs.MobAffinity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Prisma extends Spell
{

	public Prisma() 
	{
		super("Prisma", "prisma", Material.NETHER_STAR, SpellType.UTILITY, SpellementType.ARCANO, CastConditions.MIXED, Rarity.FOUR, 0, 1, false,
				true, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Prisma&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oScry Element&r&f --",
				"&r&f1. Gain a charge of "+PrintUtils.color(ObsColors.ARCANO)+"&oPrisma&r&f from a &6target &dMob&f's core weakness &7(30m)",
				"&r&f2. Cycle through the eight elements to set your Prisma selection.",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.ARCANO)+"Aspect of Lordran&f: "+PrintUtils.color(ObsColors.ARCANO)+"&oThe King's Return&r&f --",
				"&r&f1. Absorb a charge of "+PrintUtils.color(ObsColors.ARCANO)+"&oPrisma&r&f from a &6target &dMob's&f &e&oAffinity &r&7(30m)",
				"&r&f2. Confirm selected element and gain a charge of "+PrintUtils.color(ObsColors.ARCANO)+"&oPrisma&r&f.","",
				PrintUtils.color(ObsColors.ARCANO)+"Prisma &eEffect&f: Override next outgoing damage type to the scried element.","",
				"&r&bEchoic Disonance&f: Mana Cost becomes 100 when gaining a charge of "+PrintUtils.color(ObsColors.ARCANO)+"&oPrisma&r&f.");
				
	}

	private static Map<UUID, ElementType> cycledElements = new HashMap<>();
	public static Map<UUID, ElementType> scriedElement = new HashMap<>();
	
	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (RayCastUtils.getEntity(p, 30, target ->
			{
				if (target == null || !(target instanceof LivingEntity le) || target instanceof Player) return;
				
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
				double theta = ObsParticles.deriveDegreeTheta(p.getLocation(), le.getLocation());
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 8, theta, 0.2, Particle.GLOW_SQUID_INK, null);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(p, Sound.ITEM_BONE_MEAL_USE, SoundCategory.AMBIENT);
					ObsParticles.drawArcanoCastSigil(le);
					ObsParticles.drawLine(le.getLocation(), p.getLocation(), 0.5, 0.4, Particle.GLOW_SQUID_INK, null);
					ObsParticles.drawLine(le.getLocation(), p.getLocation(), 0.5, 0.4, Particle.CRIT, null);
					ElementType element = ElementType.getFromEntityCategory(EntityCategories.getMobCategory(le.getType()));
					scriedElement.put(p.getUniqueId(), element);
					PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"&oScried Element&r&f: "+PrintUtils.getElementTypeColor(element)+element.getType());
				}, 12);
			})) return 100;
			else
			{
				if (!cycledElements.containsKey(p.getUniqueId())) return -1;
				
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
				ElementType element = cycledElements.get(p.getUniqueId());
				cycledElements.remove(p.getUniqueId());
				PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"&oScried Element&r&f: "+PrintUtils.getElementTypeColor(element)+element.getType());
				scriedElement.put(p.getUniqueId(), element);
				return 100;
			}
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{			
			if (RayCastUtils.getEntity(p, 30, target ->
			{
				if (target == null || !(target instanceof LivingEntity le) || target instanceof Player) return;
				
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
				double theta = ObsParticles.deriveDegreeTheta(p.getLocation(), le.getLocation());
				ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.7, 8, theta, 0.2, Particle.GLOW_SQUID_INK, null);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(p, Sound.ITEM_BONE_MEAL_USE, SoundCategory.AMBIENT);
					ObsParticles.drawArcanoCastSigil(le);
					ObsParticles.drawLine(le.getLocation(), p.getLocation(), 0.5, 0.4, Particle.GLOW_SQUID_INK, null);
					ObsParticles.drawLine(le.getLocation(), p.getLocation(), 0.5, 0.4, Particle.CRIT, null);
					ElementType element = MobAffinity.parseCoreWeakness(EntityCategories.getMobCategory(le.getType()));
					scriedElement.put(p.getUniqueId(), element);
					PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"&oScried Element&r&f: "+PrintUtils.getElementTypeColor(element)+element.getType());
				}, 12);
			})) return 100;
			else
			{
				
				EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
				if (!cycledElements.containsKey(p.getUniqueId()))
				{
					cycledElements.put(p.getUniqueId(), ElementType.CELESTIO);
					PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"&oCycled Element&r&f: "+PrintUtils.getElementTypeColor(ElementType.CELESTIO)+ElementType.CELESTIO.getType());
					return 0;
				}
				
				ElementType newElement = switch (cycledElements.get(p.getUniqueId()))
				{
					case CELESTIO -> ElementType.MORTIO;
					case MORTIO -> ElementType.INFERNO;
					case INFERNO -> ElementType.GLACIO;
					case GLACIO -> ElementType.AERO;
					case AERO -> ElementType.GEO;
					case GEO -> ElementType.COSMO;
					case COSMO -> ElementType.HERESIO;
					case HERESIO -> ElementType.CELESTIO;
					default -> ElementType.CELESTIO;
				};
				
				cycledElements.put(p.getUniqueId(), ElementType.CELESTIO);
				PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"&oCycled Element&r&f: "+PrintUtils.getElementTypeColor(newElement)+newElement.getType());
				return 0;
			}
		}
		
		return 0;
	}

	@Override
	public int getTotalManaCost() 
	{
		
		return 0;
	}

}
