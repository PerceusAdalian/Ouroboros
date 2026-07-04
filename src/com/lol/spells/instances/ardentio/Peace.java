package com.lol.spells.instances.ardentio;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Peace extends Spell
{

	public Peace()
	{
		super("Peace", "peace", Material.SUNFLOWER, SpellType.CONTROL, SpellementType.ARDENTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 100, 4.5, false,
				true, 
				"&r&fPacify all &dMobs&f around &6self&f and apply &b&oBlindness&r&f &7(30m, 15s)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getNearbyEntities(p, 30, target ->
		{
			if (target == null || !(target instanceof Mob m)) return;
			if (m.getTarget() != null) 
			{
				ObsParticles.drawCosLine(p.getLocation(), m.getLocation(), 0.7, Particle.CLOUD, null);
				ObsParticles.drawSinLine(p.getLocation(), m.getLocation(), 1, Particle.HAPPY_VILLAGER, null);
				m.setTarget(null);
			}
			if (!m.hasPotionEffect(PotionEffectType.BLINDNESS)) EntityEffects.add(m, PotionEffectType.BLINDNESS, 300, 2, true);
		})) return -1;
		
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		return 100;
	}

	@Override
	public int getTotalManaCost()
	{
		return 100;
	}

}
