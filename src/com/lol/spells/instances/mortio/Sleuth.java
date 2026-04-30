package com.lol.spells.instances.mortio;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Sleuth extends Spell
{

	public Sleuth()
	{
		super("Sleuth", "sleuth", Material.ENDER_EYE, SpellType.BUFF, SpellementType.MORTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 50, 1, false, false,
				"&r&fGrants &b&oNight Vision&r&f to &6self &7(1min)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) return -1;
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		EntityEffects.add(p, PotionEffectType.NIGHT_VISION, 1200, 0);
		return 50;
	}

	@Override
	public int getTotalManaCost()
	{
		return 50;
	}

}
