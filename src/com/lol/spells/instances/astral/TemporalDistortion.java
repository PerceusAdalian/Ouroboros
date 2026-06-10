package com.lol.spells.instances.astral;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.TimeUtils;
import com.ouroboros.utils.TimeUtils.Timeframe;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class TemporalDistortion extends Spell
{

	public TemporalDistortion()
	{
		super("Temporal Distortion", "temporal_distortion", Material.CLOCK, SpellType.ULTIMATE, SpellementType.ASTRAL, CastConditions.RIGHT_CLICK_AIR, Rarity.SEVEN, 5000, 600, false,
				true, PrintUtils.assignAstralVariant("Temporal Distortion", true) + " &r&e&oLunar Eclipse&r&f --",
				"&r&fPhase change current &b&oWorld&r&f from &e&oDaytime&r&f to &9&oNighttime&r&f.","",
				PrintUtils.assignAstralVariant("Temporal Distortion", false) + " &r&9&oSolar Eclipse&r&f --",
				"&r&fPhase change current &b&oWorld&r&f from &9&oNighttime&r&f to &e&oDaytime&r&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (TimeUtils.checkTime(p.getWorld(), Timeframe.DAY))
		{
			Bukkit.broadcastMessage("&r&fA &e&oLunar Eclipse&r&f has occured. You feel energized all of a sudden.");
			for (Player target : p.getWorld().getPlayers())
			{
				EntityEffects.playSound(target, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.MASTER);
				ObsParticles.drawAstralCastSigil(target, false);
				EntityEffects.add(target, PotionEffectType.SPEED, 600, 2, true);
				EntityEffects.add(target, PotionEffectType.HASTE, 600, 2, true);
				EntityEffects.add(target, PotionEffectType.NIGHT_VISION, 600, 0, true);
			}
			TimeUtils.setTime(p.getWorld(), Timeframe.NIGHT);
		}
		else 
		{
			Bukkit.broadcastMessage("&r&fA &9&oSolar Eclipse&r&f has occured. You feel energized all of a sudden.");
			for (Player target : p.getWorld().getPlayers())
			{
				EntityEffects.playSound(target, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.MASTER);
				ObsParticles.drawAstralCastSigil(target, false);
				EntityEffects.add(target, PotionEffectType.SPEED, 600, 2, true);
				EntityEffects.add(target, PotionEffectType.HASTE, 600, 2, true);
				EntityEffects.add(target, PotionEffectType.RESISTANCE, 600, 4, true);
			}
			TimeUtils.setTime(p.getWorld(), Timeframe.DAY);
		}
		
		return 5000;
	}

	@Override
	public int getTotalManaCost()
	{
		return 5000;
	}

}
