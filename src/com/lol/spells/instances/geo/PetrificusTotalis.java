package com.lol.spells.instances.geo;

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
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GeoEffects;

public class PetrificusTotalis extends Spell
{

	public PetrificusTotalis() 
	{
		super("Petrificus Totalis", "petrificus_totalis", Material.CLAY_BALL, SpellType.DEBUFF, SpellementType.GEO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 50, 5, false,
				"&r&fApplies &6&oVulnerable&r&f to &6&otarget &r&dMob &7(20m, 25s)","",
				"&r&6Vulnerable &eEffect&f: Physical damage resistance is reduced by &b&o50%&r&f.","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&6&oPetrify&r&7&o', however, colloquially known as '&r&6&oPetrificus Totalis&r&7&o'.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 20, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.4, Particle.BLOCK_CRUMBLE, Material.DIRT.createBlockData());
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.4, Particle.CRIT, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{	
				EntityEffects.playSound(p, Sound.BLOCK_MUD_BREAK, SoundCategory.AMBIENT);
				ObsParticles.drawWisps(le.getLocation(), le.getWidth(), le.getHeight(), 8, Particle.BLOCK_CRUMBLE, Material.DIRT.createBlockData());
				ObsParticles.drawGeoCastSigil(le);
				GeoEffects.addVulnerable(le, 25);
			}, 15);
		})) return -1;
		
		return 50;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 50;
	}

}
