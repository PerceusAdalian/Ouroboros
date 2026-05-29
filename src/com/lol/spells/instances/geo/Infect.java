package com.lol.spells.instances.geo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Infect extends Spell
{

	public Infect()
	{
		super("Infect", "infect", Material.GREEN_DYE, SpellType.DEBUFF, SpellementType.GEO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 50, 1, false,
				false, "&r&fInflict "+PrintUtils.color(ObsColors.CORROSIVE)+"&oErosion&r&f on &6target &7(30m, 25s)","",
				PrintUtils.color(ObsColors.CORROSIVE)+"Erosion &r&eEffect&f: Applies a "+PrintUtils.color(ObsColors.CORROSIVE)+"&oCorrosive &r&dDOT&f dealing",
				"&r&f5&6"+Symbols.ARMOR+"&f dmg/s, and automatically &c&oends&r&f on &6Break&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 30, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			
			Location targetLoc = le.getLocation();
			ObsParticles.drawLine(p.getLocation(), targetLoc, 1, 0.5, Particle.SPIT, null);
			EntityEffects.playSound(p, Sound.ENTITY_LLAMA_SPIT, SoundCategory.AMBIENT);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.addErosion(le, 25);
				ObsParticles.drawLandingWave(le);
				ObsParticles.drawGeoCastSigil(le);
			}, 12);
			
		})) return -1;
		
		return 50;
	}

	@Override
	public int getTotalManaCost()
	{
		return 50;
	}

}
