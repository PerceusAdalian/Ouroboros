package com.ouroboros.ability.instances.perks;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;
import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.AbilityCategory;
import com.ouroboros.enums.AbilityDamageType;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsAbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;

public class RejuvenateWounds extends AbstractOBSAbility
{

	public RejuvenateWounds() 
	{
		super("Rejuvenate Wounds", "random_rejuvenate_perk", Material.EMERALD, StatType.MAGIC, 20, 10, ObsAbilityType.PERK, AbilityDamageType.NONE, CastConditions.PASSIVE, AbilityCategory.ANY,
				"","&r&fUpon taking damage, invoke a small chance",
				"&r&fto begin &a&oregenerating&r&f HP over 15s.",
				"","&r&f&lActivation Chance&r&f: &b30%&f for &d&oMob&r&f & &d&oPlayer&r&f Damage,",
				"&r&fand &b20%&f for all &d&oother damage sources&r&f.",
				"","&r&6&lCooldown&r&f: &b&oOne &7(1)&b&o minute&r&f.");
	}

	private static final Random rand = new Random();
	private static final Set<UUID> cooldownSet = new HashSet<>();
	
	@Override
	public boolean cast(Event e) 
	{
		
		if (e instanceof EntityDamageByEntityEvent de && de.getDamager() instanceof LivingEntity)
		{
			if (de.getEntity() instanceof Player p)
			{
				if (!PlayerData.getPlayer(p.getUniqueId()).getAbility(this).isActive()) return false;
				
				double newHealth = p.getHealth() - de.getFinalDamage();
				if (newHealth > 0 && newHealth < p.getAttribute(Attribute.MAX_HEALTH).getValue())
				{
					if (rand.nextDouble() >= 0.3d)
					{
						if (cooldownSet.contains(p.getUniqueId())) return false;
						cooldownSet.add(p.getUniqueId());
						Bukkit.getScheduler().runTaskLaterAsynchronously(Ouroboros.instance, ()-> cooldownSet.remove(p.getUniqueId()), 1200);
						EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.MASTER, 1, 1);
						OBSParticles.drawDisc(p.getLocation(), p.getWidth()+1, 2, 20, 0.5, Particle.CRIMSON_SPORE, null);
						OBSParticles.drawDisc(p.getLocation(), p.getWidth()+0.5, 2, 20, 0.5, Particle.SMOKE, null);
						EntityEffects.add(p, PotionEffectType.REGENERATION, 300, 0);
						return true;
					}
				}
			}
		}
		
		if (e instanceof EntityDamageEvent de && de.getEntity() instanceof Player p)
		{
			if (!PlayerData.getPlayer(p.getUniqueId()).getAbility(this).isActive()) return false;
			
			double newHealth = p.getHealth() - de.getFinalDamage();
			if (newHealth > 0 && newHealth < p.getAttribute(Attribute.MAX_HEALTH).getValue())
			{
				if (rand.nextDouble() >= 0.2d)
				{
					if (cooldownSet.contains(p.getUniqueId())) return false;
					cooldownSet.add(p.getUniqueId());
					Bukkit.getScheduler().runTaskLaterAsynchronously(Ouroboros.instance, ()-> cooldownSet.remove(p.getUniqueId()), 1200);
					EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.MASTER, 1, 1);
					OBSParticles.drawDisc(p.getLocation(), p.getWidth()+1, 2, 20, 0.5, Particle.CRIMSON_SPORE, null);
					OBSParticles.drawDisc(p.getLocation(), p.getWidth()+0.5, 2, 20, 0.5, Particle.SMOKE, null);
					EntityEffects.add(p, PotionEffectType.REGENERATION, 300, 0);
					return true;
				}
			}
		}
		
		return false;
	}

}
