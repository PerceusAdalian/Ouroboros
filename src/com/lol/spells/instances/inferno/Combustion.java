package com.lol.spells.instances.inferno;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class Combustion extends Spell
{

	public Combustion()
	{
		super("Combustion", "combustion", Material.BLAZE_POWDER, SpellType.OFFENSIVE, SpellementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 20, 1, false,
				"&r&fExpell a heated blast at target dealing "+PrintUtils.color(ObsColors.COMBUST)+"Combust&f damage",
				"&r&fequal to their &6AR&r&7(&6⛨&7)&f inflicting &cCharred &7(10m, 20s)","",
				"&r&cCharred &eEffect&f: Causes those affected to experience &b&oHunger&f,",
				"&b&oFatigue&r&f, and &b&oSlowness&r&f while taking &b&o15%&r&f more &c&lInferno&r&f damage.",
				"&r&f&b&o20%&r&f chance to inflict &cBurn&f while &c&oCharred&r&f, removing the effect.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		Entity target = RayCastUtils.getEntity(p, 10);
		if (target == null || target instanceof Player) return -1;
		else if (target instanceof LivingEntity le)
		{
			MobData data = MobData.getMob(le.getUniqueId());
			EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
			OBSParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.4, Particle.LAVA, null);
			OBSParticles.drawCosLine(p.getLocation(), target.getLocation(), 0.5, Particle.FLAME, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				OBSParticles.drawInfernoCastSigil(le);
				OBSParticles.drawWisps(le.getLocation(), le.getWidth(), le.getHeight(), 6, Particle.FLAME, null);
				MobData.damageUnnaturally(p, target, data != null ? data.getArmor(false) : 10, true, false, ElementType.COMBUST);
				EntityEffects.playSound(p, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT);
				InfernoEffects.addCharred(le, 20);
			}, 15);
			return 20;
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost()
	{
		return 20;
	}

}
