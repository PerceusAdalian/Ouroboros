package com.ouroboros.ability.instances.combat;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.enums.AbilityCategory;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsAbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class GeminiSlash extends AbstractOBSAbility
{

	public GeminiSlash() 
	{
		super("Gemini Slash", "gemini_slash_ability", Material.ECHO_SHARD, StatType.MELEE, 10, 5, ObsAbilityType.COMBAT, ElementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, AbilityCategory.SWORDS,
				"&r&fDash towards target mob and attack instantly, dealing",
				"&r&f&l5&r&c♥ &r&f&lCelestio&r&f damage and apply &eExposed&f.",
				"&r&f&lRange&r&f: &b5 meters &r&7| &r&f&lDuration&r&f: &b15 seconds",
				"&r&f&lCooldown: &b5 seconds");
	}

	@Override
	public boolean cast(Event e) 
	{
		if (e instanceof PlayerInteractEvent pie)
		{		
			Player p = pie.getPlayer();
			//Get a valid target
			Entity target = RayCastUtils.getNearestEntity(p, 5);
			if (target == null || target instanceof LivingEntity) return false;
			
			//Initalize vectors
			Vector v1 = target.getLocation().toVector();
			Vector v2 = p.getLocation().toVector();
			p.setVelocity(v1.subtract(v2).normalize().multiply(1.5));
			
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 1, 0.5, Particle.CLOUD, null);
			EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER, 0, 0);
			MobData.damageUnnaturally(p, target, 10, true);
			EntityEffects.addExposed((LivingEntity)target, 300);

			return true;			
		}
		return false;
	}
}
