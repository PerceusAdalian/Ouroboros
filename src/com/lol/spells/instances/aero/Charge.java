package com.lol.spells.instances.aero;

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

public class Charge extends Spell
{

	public Charge() 
	{
		super("Charge", "charge", Material.STRING, SpellType.BUFF, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 10, 15, false,
				"&r&fGrant &dCharged &bIII &fto self &7(15s)");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		EntityEffects.addCharged(p, 2, 15);
		EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.AMBIENT);
		OBSParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 6, Particle.ELECTRIC_SPARK, null);
		return true;
	}

}
