package com.ouroboros.ability.instances.combat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.ouroboros.Ouroboros;
import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.AbilityDamageType;
import com.ouroboros.enums.ObsAbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.RayCastUtils;

public class Flamelash extends AbstractOBSAbility
{

	public Flamelash() 
	{
		super("Flamelash", "flamelash", Material.BLAZE_ROD, StatType.MELEE, 5, 1, ObsAbilityType.COMBAT, AbilityDamageType.INFERNO, 
				"&r&f&lRight-Click&r&f to slash upwards to calcinate",
				"&r&ftarget mob dealing &l5&r&c♥ &lInferno&r&f damage.",
				"&r&f&lRange: &b7 meters",
				"&r&f&lCooldown: &b5 seconds");
	}

	public static Set<UUID> cooldownTimer = new HashSet<>();
	
	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		if (!PlayerActions.rightClickAir(e)) return false;
				
		Player p = e.getPlayer();
		if (!PlayerData.getPlayer(p.getUniqueId()).getAbility(this).isActive()) return false;
		if (cooldownTimer.contains(p.getUniqueId())) return false;
		
		Entity target = RayCastUtils.getNearestEntity(p, 7);
		if (target == null || !(target instanceof LivingEntity)) return false;
		
		OBSParticles.drawSpiralVortex(target.getLocation(), 5, 3, 0.1, Particle.LAVA, null);
		EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, SoundCategory.MASTER, 1, 1);
		((Damageable)target).damage(10);
		target.setFireTicks(200);
		
		cooldownTimer.add(p.getUniqueId());
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> cooldownTimer.remove(p.getUniqueId()), 100);
		
		return true;
	}
	
}
