package com.lol.spells.instances.celestio;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Revelio extends Spell
{

	public Revelio() 
	{
		super("Revelio", "revelio", Material.NETHER_STAR, SpellType.UTILITY, SpellementType.CELESTIO, CastConditions.SHIFT_RIGHT_CLICK_AIR, Rarity.THREE, 200, 5, false, false,
				"&r&fApplies &e&oExposed&r&7 (20s) &r&fto all nearby entities within 30 &b&ometers&r&f.","",
				"&r&e&oExposed &r&fEffect: Reveals an entity's location and &6&oBreaks &r&fthem.",
				"&r&fIf those affected are "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f-based, they instantly die.","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&e&oRadiance&r&7&o', however, colloquially known as '&r&e&oRevelio&r&7&o'.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		if (!RayCastUtils.getNearbyEntities(p, 30, (target)->
		{
			ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 8, Particle.CLOUD, null);
			CelestioEffects.addExposed(target, 20);	
		})) return -1;
		return this.getManacost();
	}

	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}
	
}
