package com.lol.spells.instances.aero;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.WeatherUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class HealingCurrent extends Spell
{

	public HealingCurrent()
	{
		super("Healing Current", "healing_current", Material.NETHER_STAR, SpellType.SUPPORT, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 150, 3, false,
				"&r&aHeal &6self&f/&6target player&f equal to &b&o50%&r&f of current &cHP &7(25m)","",
				"&r&e&lEchoic Resonance&r&f: Restores &cHP&f to &b&o100%&r&f while &d&oStorming&r&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if(!RayCastUtils.getEntity(p, 25, target ->
		{
			if (!(target instanceof Player pTarget)) return;
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.CRIT, null);

			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->playSpellEffect(p, pTarget, true), 15);
		})) {
			playSpellEffect(p, p, false);
		};
		return 150;
	}

	@Override
	public int getTotalManaCost()
	{
		return 150;
	}
	
	public static void playSpellEffect(Player caster, Player target, boolean targetted)
	{
		if (WeatherUtils.checkWeather(target.getWorld(), WeatherUtils.WeatherType.STORMING))
		{
			EntityEffects.heal(target);
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 6, Particle.HAPPY_VILLAGER, null);
			if (!targetted)
				EntityEffects.playSound(caster, caster == target ? Sound.BLOCK_BEACON_POWER_SELECT : Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			target.getWorld().strikeLightningEffect(target.getLocation());
			return;
		}
		EntityEffects.heal(target, target.getHealth() * 0.5);
		OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 6, Particle.HAPPY_VILLAGER, null);
		EntityEffects.playSound(caster, caster == target ? Sound.BLOCK_BEACON_POWER_SELECT : Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		OBSParticles.drawWave(Ouroboros.instance, target.getLocation(), 7, 0.5, 20, Particle.BLOCK_CRUMBLE, Material.AMETHYST_CLUSTER.createBlockData());
	}

}
