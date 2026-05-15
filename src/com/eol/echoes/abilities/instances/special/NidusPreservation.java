package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GeoEffects;

public class NidusPreservation extends EchoAbility
{

	public NidusPreservation()
	{
		super("Nidus' Preservation", "nidus_preservation", Material.NETHER_STAR, StatType.MELEE, 0, 0, 100, AbilityType.SPECIALABILITY, ElementType.GEO, CastConditions.RIGHT_CLICK_AIR, EchoForm.HAMMER, 
				"&r&fApplies &6Guarded &bX&f and &6Barbed &bX&f to &6self&7 (1min)","",
				"&r&6Guarded &eEffect&f: Halves incoming damage for 10 instances.",
				"&r&6Barbed &eEffect&f: Incoming damage is redirected as "+PrintUtils.color(ObsColors.GEO)+"&lGeo&r&f damage",
				"&r&fequal to the &b&omagnitude&r&f of &6Barbed&f pre &6Guarded&f procs.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (!GeoEffects.addGuarded(p, 7, 60)) return -1;
		EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_TURTLE, SoundCategory.AMBIENT);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			GeoEffects.addBarbed(p, 5, 60);
			EntityEffects.playSound(p, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.AMBIENT);
			ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 5, 0.8, 30, 0.4, Particle.BLOCK_CRUMBLE, Material.DIRT.createBlockData());
		}, 20);
		return 100;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 100;
	}

}
