package com.lol.spells.instances.arcano;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class Mute extends Spell
{

	public Mute() 
	{
		super("Mute", "mute", Material.MUSIC_DISC_WAIT, SpellType.CONTROL, SpellementType.ARCANO, CastConditions.RIGHT_CLICK_AIR, Rarity.FIVE, 50, 20, true,
				"&r&fApply &b&oEther Disruption&r&f to target &cPlayer&f, or &b&oEther Overload&r&f otherwise &7(&cPVP&f: &c20s&7 | 60s)","",
				"&r&bEther Disruption &eEffect&f: affected &c&oPlayer&r&f(s) can't cast &e&oSpells&r&f.",
				"&r&bEther Overload &eEffect&f: those affected take an additional &b&o50% &r&e&oelemental &r&fdamage.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (RayCastUtils.getEntity(p, 30, target->
		{
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.GLOW, null);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 3, 0.5, Particle.SONIC_BOOM, null);
			OBSParticles.drawWisps(target.getLocation(), target.getHeight(), target.getWidth(), 4, Particle.WARPED_SPORE, null);
			if (target instanceof Player)
				EntityEffects.addEtherDisruption((Player) target, 20);
			if (target instanceof LivingEntity && !(target instanceof Player))
				EntityEffects.addEtherOverload((LivingEntity) target, 60);
		})) return true;
		return false;
	}

}
