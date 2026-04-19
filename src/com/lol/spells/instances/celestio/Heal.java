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
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Heal extends Spell
{

	public Heal()
	{
		super("Heal", "heal", Material.EMERALD, SpellType.CANTRIP, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 10, 1, true,
				"&r&aHeal &6self&f/&6target &c&oPlayer&r&f for 10&c♥ &7(25m)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getEntity(p, 25);
		
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		if (target == null || !(target instanceof Player))
		{
			OBSParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 7, Particle.HAPPY_VILLAGER, null);
			EntityEffects.heal(p, 10);
			return 25;
		}
		else if (target instanceof Player pTarget)
		{		
			OBSParticles.drawLine(p.getLocation(), pTarget.getLocation(), 0.4, 0.4, Particle.CLOUD, null);
			OBSParticles.drawSinLine(p.getLocation(), pTarget.getLocation(), 0.6, Particle.END_ROD, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				OBSParticles.drawWisps(pTarget.getLocation(), pTarget.getWidth(), pTarget.getHeight(), 7, Particle.HAPPY_VILLAGER, null);
				EntityEffects.heal(pTarget, 10);
			}, 15);
			return 25;
		}
		else return -1;
	}

	@Override
	public int getTotalManaCost()
	{
		return 10;
	}

}
