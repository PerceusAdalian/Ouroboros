package com.lol.spells.instances.celestio;

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
import com.lol.spells.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Holy extends Spell
{

	public Holy()
	{
		super("Holy", "holy", Material.SUNFLOWER, SpellType.CONTROL, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 300, 5, true,
				true, "&r&fDeal &l250&c"+Symbols.HP+" "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f damage &e&l-> &r&d"+Symbols.AOE+"&r&f,",
				"&6Break&f all &6"+Symbols.TARGET+" &7(25m | &cPVP&7: &cDamage Type &7-> &c&lPure&r&7)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (!RayCastUtils.getNearbyEntities(p, 25, c -> {
			if (!(c instanceof LivingEntity le) || c == null) return;
			double delta = ObsParticles.deriveDegreeTheta(le.getLocation(), p.getLocation());
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.85, 9, delta, 0.75, Particle.END_ROD, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), le.getLocation(), 0.75, 8, delta, 0.75, Particle.CLOUD, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawSpiralVortex(le.getLocation(), delta, le.getHeight(), 0.1, Particle.CLOUD, null);
				ObsParticles.drawSpiralVortex(le.getLocation(), delta, le.getHeight(), 0.1, Particle.END_ROD, null);
				if (le instanceof Player pTarget) 
				{
					MobData.damageUnnaturally(p, le, 250, true, true, ElementType.PURE, null);
					PlayerData.getPlayer(pTarget.getUniqueId()).setBreak();
				}
				else 
				{
					MobData.damageUnnaturally(p, le, 250, true, true, ElementType.CELESTIO, null);
					MobData.getMob(le.getUniqueId()).setBreak();
				}
			}, 20);
		})) return -1;
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> EntityEffects.playSound(p, Sound.ENTITY_BREEZE_SHOOT, SoundCategory.AMBIENT), 20);
		return 300;
	}

	@Override
	public int getTotalManaCost()
	{
		return 300;
	}

}
