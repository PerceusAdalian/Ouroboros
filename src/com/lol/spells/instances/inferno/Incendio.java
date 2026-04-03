package com.lol.spells.instances.inferno;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Incendio extends Spell
{

	public Incendio() 
	{
		super("Incendio", "incendio", Material.FIRE_CHARGE, SpellType.OFFENSIVE, SpellementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 5, 1, false, 
				"&r&fSummon concentrated "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f energy expelling it forward.","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&c&oFireball&r&7&o', however, colloquially known as '&r&c&oIncendio&r&7&o'.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		OBSParticles.drawInfernoCastSigil(p);
		EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
		Fireball fireball = (Fireball) p.getWorld().spawnEntity(p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(1.5)), EntityType.FIREBALL);
		fireball.setYield(1);
		fireball.setIsIncendiary(true); 
		return this.getManacost();
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

}
