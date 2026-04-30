package com.lol.spells.instances.inferno;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class Primer extends Spell
{

	public Primer()
	{
		super("Primer", "primer", Material.FIREWORK_STAR, SpellType.DEBUFF, SpellementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 10, 3, false, false,
				"&r&fInflicts &6target&f with &cCharred&f and set &cPrimed&f &7(20m, 30s)","",
				"&r&cCharred &eEffect&f: Causes &b&oHunger&r&f, &b&oFatigue&r&f, and &b&oSlowness&r&f, while affected take &b&o25%&r&f",
				"&r&fmore "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno &r&fdamage, and may cause &cBurn&f upon hit removing the effect.");
	}

	public static Set<UUID> primed = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 10, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			UUID uuid = le.getUniqueId();
			if (primed.contains(uuid)) return;
			primed.add(uuid);
			
			EntityEffects.playSound(p, Sound.ITEM_FIRECHARGE_USE, SoundCategory.AMBIENT);
			ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.4, Particle.FLAME, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawInfernoCastSigil(le);
				ObsParticles.drawWisps(le.getLocation(), le.getWidth(), le.getHeight(), 6, Particle.LAVA, null);
				InfernoEffects.addCharred(le, 30);
			}, 15);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> primed.remove(uuid), 600);
		})) return -1;
		return 10;
	}

	@Override
	public int getTotalManaCost()
	{
		return 10;
	}

}
