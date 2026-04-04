package com.lol.spells.instances.glacio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Riptide extends Spell
{

	public Riptide()
	{
		super("Riptide", "riptide", Material.LAPIS_LAZULI, SpellType.UTILITY, SpellementType.GLACIO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 5, 1.5, false,
				"&r&fGrants a momentary boosts to velocity while swimming.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!p.isSwimming() || !p.isInWater()) return -1;
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->EntityEffects.playSound(p, Sound.ITEM_TRIDENT_RIPTIDE_3, SoundCategory.AMBIENT), 10);
		OBSParticles.drawPoint(p.getLocation(), Particle.EXPLOSION_EMITTER, 0, null);
        Vector boost = p.getEyeLocation().getDirection().normalize().multiply(4);
		p.setVelocity(p.getVelocity().add(boost));
		return 5;
	}

	@Override
	public int getTotalManaCost()
	{
		return 5;
	}

}
