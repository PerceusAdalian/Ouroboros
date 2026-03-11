package com.lol.spells.instances.arcano;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
import com.ouroboros.utils.PrintUtils;

public class Freecast extends Spell
{

	public Freecast() 
	{
		super("Freecast", "freecast", Material.FLOW_BANNER_PATTERN, SpellType.UTILITY, SpellementType.ARCANO, CastConditions.RIGHT_CLICK_AIR, Rarity.FIVE, 50, 10, false,
				"&r&fOverride the laylines around you allowing for unlimited &b&oEther&r&f draw.",
				"&r&fThe next &eSpell&f you cast has no &b&omana cost&f.");
	}

	public static Set<UUID> hasFreecast = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		OBSParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 7, 0.5, 0.5, Particle.ENCHANT, null);
		PrintUtils.PrintToActionBar(p, "&r&f&oYou feel a surge of &b&oEther&r&f..");
		hasFreecast.add(p.getUniqueId());
		return this.getManacost();
	}

	@Override
	public int getTotalManaCost() 
	{

		return this.getManacost();
	}

}
