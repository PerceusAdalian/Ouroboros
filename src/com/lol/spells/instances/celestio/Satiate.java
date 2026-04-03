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
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Satiate extends Spell
{

	public Satiate()
	{
		super("Satiate", "satiate", Material.WHEAT, SpellType.SUPPORT, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 35, 5, false,
				"&r&fGrant &e&oSatiated&r&f to &6self&r&f or target &d&oPlayer&r&7 (20m)","",
				"&r&eSatiated Effect&f: grants momentary &b&oSaturation&r&f regen.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		Entity target = RayCastUtils.getEntity(p, 20);
		if (target == null)
		{
			CelestioEffects.addSatiated(p);
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			OBSParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 7, Particle.HAPPY_VILLAGER, null);
			return 35;
		}
		else if (target != null && target instanceof Player pTarget)
		{
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			OBSParticles.drawLine(p.getLocation(), pTarget.getLocation(), 0.5, 0.5, Particle.HAPPY_VILLAGER, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				OBSParticles.drawCelestioCastSigil(pTarget);
				OBSParticles.drawWisps(pTarget.getLocation(), pTarget.getWidth(), pTarget.getHeight(), 7, Particle.HAPPY_VILLAGER, null);
				CelestioEffects.addSatiated(pTarget);				
			}, 15);
			return 35;
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost()
	{
		return 35;
	}

}
