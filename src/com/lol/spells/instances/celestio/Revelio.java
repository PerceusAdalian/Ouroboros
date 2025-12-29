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
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class Revelio extends Spell
{

	public Revelio() 
	{
		super("Revelio", "revelio", Material.NETHER_STAR, SpellType.UTILITY, SpellementType.CELESTIO, CastConditions.SHIFT_RIGHT_CLICK_AIR, Rarity.THREE, 200, 5, 
				"&r&fApplies &e&oExposed&r&7 (20s) &r&fto all nearby entities within 30 &b&ometers&r&f.","",
				"&r&e&oExposed &r&fEffect: Reveals an entity's location. If those affected are &d&oUndead&r&f, they instantly die.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		OBSParticles.drawCelestioCastSigil(p);
		if (!RayCastUtils.getNearbyEntities(p, 30, (target)->
		{
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 8, Particle.CLOUD, null);
			EntityEffects.addExposed(target, 20);	
		})) return false;
		return true;
	}

}
