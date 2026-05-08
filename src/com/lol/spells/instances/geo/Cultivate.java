package com.lol.spells.instances.geo;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Cultivate extends Spell
{

	public Cultivate() 
	{
		super("Cultivate", "cultivate", Material.WHEAT, SpellType.CANTRIP, SpellementType.GEO, CastConditions.RIGHT_CLICK_BLOCK, Rarity.ONE, 15, 1, false, false,
				"&r&fCultivate &6target &b&oCrop&r&f to fully grown.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		
		if (!(b.getBlockData() instanceof Ageable crop)) return -1;
		
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		EntityEffects.playSound(p, Sound.ITEM_BONE_MEAL_USE, SoundCategory.MASTER);
		ObsParticles.drawWisps(b.getLocation().add(0,0.5,0), 1, 1, 4, Particle.HAPPY_VILLAGER, null);
		
		crop.setAge(crop.getMaximumAge());
		b.setBlockData(crop);
		
		return 15;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 15;
	}

}
