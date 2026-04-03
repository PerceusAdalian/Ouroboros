package com.lol.spells.instances.astral;

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
import com.lol.spells.instances.inferno.Meteor;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.TimeUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GlacioEffects;

public class Starfall extends Spell
{

	public Starfall()
	{
		super("Starfall", "starfall", Material.FIREWORK_ROCKET, SpellType.OFFENSIVE, SpellementType.ASTRAL, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 20, 1, false,
				PrintUtils.assignAstralVariant("Starfall", true) + " &r&e&oMeteor β&r&f --",
				"&r&fCast down a &cMeteor&f at target &7(20m) dealing minor "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f damage.",
				PrintUtils.assignAstralVariant("Starfall", false) + " &r&9&oCooling Mist&r&f --",
				"&r&fApply &bChill I&f to targets in a &dConal AOE &7(20s, 15m)","",
				"&r&bChill &eEffect&f: inflicts a "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f &dDOT&f effect.",
				"&r&fReapplying &bChill&f increases the magnitude, while keeping initial duration.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (TimeUtils.checkTime(p.getWorld(), TimeUtils.Timeframe.DAY))
		{
			if(!RayCastUtils.getEntity(p, 20, target ->
			{
				if (target == null || !(target instanceof LivingEntity le)) return;
				Meteor.playSpellEffects(p, le.getLocation(), 1, false, 0, true);
			})) return -1;
		}
		else if (TimeUtils.checkTime(p.getWorld(), TimeUtils.Timeframe.NIGHT))
		{
			if (!RayCastUtils.getEntitiesInFov(p, 15, target -> 
			{
				if (target == null || !(target instanceof LivingEntity le)) return;
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
				OBSParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.4, Particle.SNOWFLAKE, null);
				OBSParticles.drawGlacioCastSigil(le);
				GlacioEffects.addChill(p, le, 1, 20);
			}));
		}
		return 0;
	}

	@Override
	public int getTotalManaCost()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
