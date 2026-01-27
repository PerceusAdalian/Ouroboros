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

public class Protego extends Spell
{

	public Protego() 
	{
		super("Protego", "protego", Material.NETHER_STAR, SpellType.DEFENSIVE, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 25, 15, true,
				"&r&fGrants &eWard &bIII to self &7(15s)&r&f.","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&e&oBarrier&r&7&o', however, colloquially known as '&r&e&oProtego&r&7&o'.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = (Player) e.getPlayer();
		
		OBSParticles.drawCelestioCastSigil(p);
		OBSParticles.drawCylinder(p.getLocation(), p.getWidth()+1, 3, 7, 0.5, 0.5, Particle.ENCHANT, null);
		
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		EntityEffects.addWard(p, 2, 300);
		return true;
	}

}
