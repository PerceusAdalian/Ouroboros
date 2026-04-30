package com.lol.spells.instances.inferno;

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
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class Confringo extends Spell
{

	public Confringo()
	{
		super("Confringo", "confringo", Material.BLAZE_POWDER, SpellType.OFFENSIVE, SpellementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 50, 1, false, false,
				"&r&fExpell a heated blast at target dealing "+PrintUtils.color(ObsColors.COMBUST)+"Combust&f damage",
				"&r&fequal to their &6AR&r&7(&6⛨&7)&f inflicting &cCharred &7(10m, 20s)","",
				"&r&cCharred &eEffect&f: Causes &b&oHunger&r&f, &b&oFatigue&r&f, and &b&oSlowness&r&f, while affected take &b&o25%&r&f",
				"&r&fmore "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno &r&fdamage, and may cause &cBurn&f upon hit removing the effect.","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&c&oCombustion&r&7&o', however, colloquially known as '&r&c&oConfringo&r&7&o'.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 10, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			MobData data = MobData.getMob(le.getUniqueId());
			EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
			ObsParticles.drawSinLine(p.getLocation(), le.getLocation(), 0.4, Particle.LAVA, null);
			ObsParticles.drawCosLine(p.getLocation(), target.getLocation(), 0.5, Particle.FLAME, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawInfernoCastSigil(le);
				ObsParticles.drawWisps(le.getLocation(), le.getWidth(), le.getHeight(), 6, Particle.FLAME, null);
				MobData.damageUnnaturally(p, target, data != null ? data.getArmor(false) : 10, true, false, ElementType.COMBUST);
				EntityEffects.playSound(p, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.AMBIENT);
				InfernoEffects.addCharred(le, 20);
			}, 15);
		})) return -1;
		return 50;
	}

	@Override
	public int getTotalManaCost()
	{
		return 50;
	}

}
