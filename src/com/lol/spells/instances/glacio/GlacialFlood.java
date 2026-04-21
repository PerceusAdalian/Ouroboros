package com.lol.spells.instances.glacio;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
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
import com.ouroboros.utils.entityeffects.GlacioEffects;

public class GlacialFlood extends Spell
{

	public GlacialFlood() 
	{
		super("Glacial Flood", "flood", Material.BLUE_DYE, SpellType.OFFENSIVE, SpellementType.GLACIO, CastConditions.MIXED, Rarity.FOUR, 50, 5, true,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&bGlacial Flood&f: &b&oChilling Cascade&r&f --",
				"&r&fEmit a burst of cold water about you in a radial &dAOE&f applying &bChill V &7(20m | 30s)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&bGlacial Flood&f: &b&oCrystalize&r&f --",
				"&r&fEmit a blast of cold air to &bFreeze&f &bChilled&f enemies and deal &b&lGlacio",
				"&r&fdamage equal to &o10 + (Chill stacks x 3) &r&7[Max: 70♥ | 20 meters]","",
				"&r&bChill &eEffect&f: &b&oSlows&r&f while inflicting a "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f DOT effect.",
				"&r&fReapplying &bChill&f increases the &b&omagnitude&r&f, while keeping initial duration.","",
				"&r&bFrozen &eEffect&f: Those affected by &bChill&f remove their stacks, and instead,",
				"&r&f\"Freezes\" them. They are &b&oWeakened&r&f by the amount of &bChill&f stacks they had.",
				"&r&cPVP&f: Freeze effect &6Stuns&f for &b&o5 seconds&r&f and &b&oWeakens&r&f for &b&o20 seconds&r&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();

		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{			
			EntityEffects.playSound(p, Sound.AMBIENT_UNDERWATER_EXIT, SoundCategory.AMBIENT);
			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_HURT_FREEZE, SoundCategory.AMBIENT);
			ObsParticles.drawWisps(p.getLocation(), p.getWidth() + 20, p.getHeight()+ 15, 20, Particle.SNOWFLAKE, null);
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 20, .25, 20, Particle.FISHING, null);
			
			if (!RayCastUtils.getNearbyEntities(p, 20, target ->
			{
				ObsParticles.drawGlacioCastSigil(target);
				ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 6, Particle.SNOWFLAKE, null);
				if (GlacioEffects.chillEffects.containsKey(target.getUniqueId()))
				{
					int damage = GlacioEffects.chillEffects.get(target.getUniqueId()).magnitude;
					MobData.damageUnnaturally(p, target, damage*3, false, true, ElementType.GLACIO);
				}
				GlacioEffects.addFrozen(target);
				MobData.damageUnnaturally(p, target, 10, false, true, ElementType.GLACIO);
			})) return -1;
			
			return this.getManacost();
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			EntityEffects.playSound(p, Sound.AMBIENT_UNDERWATER_ENTER, SoundCategory.AMBIENT);
			ObsParticles.drawWisps(p.getLocation(), p.getWidth() + 20, p.getHeight()+ 15, 20, Particle.FALLING_WATER, null);
			
			if (!RayCastUtils.getNearbyEntities(p, 20, target ->
			{
				ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 6, Particle.DRIPPING_WATER, null);
				ObsParticles.drawGlacioCastSigil(target);
				GlacioEffects.addChill(p, target, 5, 30);
			}))	return -1;
			
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
