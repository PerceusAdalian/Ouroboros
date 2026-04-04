package com.lol.spells.instances.aero;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Tailwind extends Spell
{

	public Tailwind()
	{
		super("Tailwind", "tailwind", Material.FEATHER, SpellType.UTILITY, SpellementType.AERO, CastConditions.MIXED, Rarity.ONE, 5, 1.5, false,
				"&ePrimary&f "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&dTailwind&f: &d&oJetstream&r&f --",
				"&r&fGives a momentary boost of velocity towards target direction.","",
				"&eSecondary&f "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&dTailwind&f: &d&oBoost&r&f --",
				"&r&fProvides a temporary &b&oSpeed Boost&r&7 (20s)","",
				"&r&e&lEchoic Resonance&r&f: &e&oPrimary Cast&r&f negates the next instance of &d&oFall Damage&r&f.");
	}

	private static Set<UUID> tailwindRegistry = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			EntityEffects.add(p, PotionEffectType.SPEED, 400, 2, false);
			return 5;
		}
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (p.isSwimming() || p.isInWater()) return -1;
			EntityEffects.playSound(p, Sound.ENTITY_BREEZE_SHOOT, SoundCategory.AMBIENT);
			OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 2, 4, 0.5, Particle.EXPLOSION, null);
            Vector boost = p.getEyeLocation().getDirection().normalize().multiply(3.5);
			p.setVelocity(p.getVelocity().add(boost));
			if (!tailwindRegistry.contains(p.getUniqueId())) tailwindRegistry.add(p.getUniqueId());
			return 5;
		}
		return -1;
	}

	@Override
	public int getTotalManaCost()
	{
		return 5;
	}
	
	public static void registerSpellHelper(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void onFallDamage(EntityDamageEvent e)
			{
				if (e.getEntity() instanceof Player p 
						&& e.getCause() == DamageCause.FALL 
							&& tailwindRegistry.contains(p.getUniqueId())) 
				{
					e.setCancelled(true);
					tailwindRegistry.remove(p.getUniqueId());
				}
			}
		}, plugin);
	}

}
