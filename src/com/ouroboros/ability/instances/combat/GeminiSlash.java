package com.ouroboros.ability.instances.combat;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.AbilityDamageType;
import com.ouroboros.enums.ObsAbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.RayCastUtils;

public class GeminiSlash extends AbstractOBSAbility
{

	public GeminiSlash() 
	{
		super("Gemini Slash", "gemini_slash_ability", Material.ECHO_SHARD, StatType.MELEE, 10, 5, ObsAbilityType.COMBAT, AbilityDamageType.CELESTIO, 
				"&r&f&lRight-Click&r&f target mob to dash forward, and attack",
				"&r&fdealing 5 hearts Celestio damage.");
	}

	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		//Access methods, params, and validation of ownership of the ability.
		if (!PlayerActions.rightClickAir(e)) return false;
		Player p = e.getPlayer();
		if (!PlayerData.getPlayer(p.getUniqueId()).getAbility(this).isActive()) return false;
		
		//Get a valid target
		Entity target = RayCastUtils.getNearestEntity(p, 5);
		if (target == null) return false;
		
		//Initalize vectors
		Vector v1 = target.getLocation().toVector();
		Vector v2 = p.getLocation().toVector();
		p.setVelocity(v1.subtract(v2).normalize().multiply(1.5));
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 1, 0.5, Particle.CLOUD, null);
		((Damageable) target).damage(10);
		
		return true;
	}
}
