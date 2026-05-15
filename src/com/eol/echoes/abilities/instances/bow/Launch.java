package com.eol.echoes.abilities.instances.bow;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Launch extends EchoAbility
{

	public Launch() 
	{
		super("Launch", "launch", Material.BOW, StatType.RANGED, 10, 1, 5, AbilityType.UTILITY, ElementType.MODULO,
				CastConditions.RIGHT_CLICK_BLOCK, EchoForm.BOW, 
				"&r&fLaunch backwards from &6target &dBlock&f.","",
				"&r&e&lEchoic Resonance&r&f: negates the next instance of",
				"&d&oFall Damage&r&f for &b&o10 seconds&r&f.");
	}

	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		
		if (b == null || b.getType().isAir()) return -1;
		
		EntityEffects.playSound(p, Sound.ENTITY_BREEZE_SHOOT, SoundCategory.AMBIENT);
		ObsParticles.drawDisc(p.getLocation(), p.getWidth(), 2, 4, 0.5, Particle.EXPLOSION, null);
		Vector away = p.getLocation().toVector().subtract(b.getLocation().toVector()).normalize();
		p.setVelocity(away.multiply(3.5));
		
		return 5;
	}

	@Override
	public int getFinalDurabilityCost() 
	{
		return 5;
	}

	

}
