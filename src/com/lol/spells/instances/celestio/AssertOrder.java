package com.lol.spells.instances.celestio;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;

public class AssertOrder extends Spell
{

	public AssertOrder() 
	{
		super("Assert Order", "assert_order", Material.TOTEM_OF_UNDYING, SpellType.DEFENSIVE, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 225, 15, 
				"&r&fGrants '&e&oKarma&r&f' for the caster &7(15s)&r&f.",
				"&r&eKarma&f Effect: all incoming damage is mitigated by 50%.",
				"&r&fDamage received is redirected to the source, multiplied by a factor of &bx1.35&f,",
				"&r&fand is considered &e&lCelestio&r&f damage. Damage mitigated is restored by &b25%",
				"&r&fonce the effect expires, and applies &b&oAbsorption&r&f stacks depending on excess &aHP&f.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = (Player) e.getPlayer();
		
		OBSParticles.drawCelestioCastSigil(p);
		EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.AMBIENT);
		
		playersDamageMap.put(p.getUniqueId(), 0.0d);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
		{
		    double storedHealth = playersDamageMap.remove(p.getUniqueId());
		    double maxHealth = p.getAttribute(Attribute.MAX_HEALTH).getValue();
		    double currentHealth = p.getHealth();
		    
		    if (currentHealth >= maxHealth) return;
		    
		    EntityEffects.playSound(p, Sound.BLOCK_BELL_RESONATE, SoundCategory.AMBIENT);
		    OBSParticles.drawCelestioCastSigil(p);
		    
		    double overHealth = storedHealth - currentHealth;
		    if (overHealth > 0.0) 
		    {
		        p.setHealth(maxHealth);
		        EntityEffects.add(p, PotionEffectType.ABSORPTION, 600, Integer.max((int) overHealth / 4 + 1, 4));
		    } 
		    else 
		    {
		        p.setHealth(Math.min(currentHealth + storedHealth, maxHealth));
		    }
		}, 300);
		
		return true;
	}

	private static Map<UUID, Double> playersDamageMap = new HashMap<>();
	public static void registerSpellHelper(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler (priority = EventPriority.HIGH)
			public void onDamage(EntityDamageByEntityEvent e)
			{
				if (!(e.getEntity() instanceof Player p)) return;
				if (!(e.getDamager() instanceof LivingEntity le)) return;

				Double storedDamage = playersDamageMap.get(p.getUniqueId());
				if (storedDamage == null) return;
				
				double finalDamage = e.getFinalDamage();
				
				OBSParticles.drawLine(p.getLocation(), le.getLocation(), 1.5, 0.5, Particle.SONIC_BOOM, null);
				OBSParticles.drawWisps(le.getLocation(), le.getWidth(), le.getHeight(), 4, Particle.END_ROD, null);
				OBSParticles.drawWisps(le.getLocation(), le.getWidth(), le.getHeight(), 2, Particle.GUST_EMITTER_SMALL, null);
				EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER);
				
				e.setDamage(finalDamage * 0.5d); // Half incoming damage
				MobData.damageUnnaturally(p, le, finalDamage * 1.35d, true, ElementType.CELESTIO); // Redirect it to the source entity
				playersDamageMap.put(p.getUniqueId(), storedDamage + (finalDamage * 0.5d)); // Adds the damage for later healing
			}
		}, plugin);
	}
}
