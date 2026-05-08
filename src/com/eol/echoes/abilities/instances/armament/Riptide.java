package com.eol.echoes.abilities.instances.armament;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Riptide extends EchoAbility
{

	public Riptide() 
	{
		super("Riptide", "riptide", Material.LIGHT_BLUE_DYE, StatType.MELEE, 10, 1, 1, AbilityType.UTILITY, ElementType.MODULO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.ARMAMENT, 
				"&r&fGrants a momentary boosts to velocity while swimming.");
	}

	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (!p.isSwimming() || !p.isInWater()) return -1;
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->EntityEffects.playSound(p, Sound.ITEM_TRIDENT_RIPTIDE_3, SoundCategory.AMBIENT), 10);
		ObsParticles.drawPoint(p.getLocation(), Particle.EXPLOSION_EMITTER, 0, null);
        Vector boost = p.getEyeLocation().getDirection().normalize().multiply(4);
		p.setVelocity(p.getVelocity().add(boost));
		
		return 1;
	}

	@Override
	public int getFinalDurabilityCost() 
	{
		return 1;
	}

}
