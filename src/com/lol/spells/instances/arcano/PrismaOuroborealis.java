package com.lol.spells.instances.arcano;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class PrismaOuroborealis extends Spell
{

	public PrismaOuroborealis()
	{
		super("Prisma Ouroborealis", "prisma_ouroborealis", Material.NETHER_STAR, SpellType.BUFF, SpellementType.ARCANO, CastConditions.RIGHT_CLICK_AIR, Rarity.FIVE, 350, 240, false,
				"&r&fGrants "+PrintUtils.color(ObsColors.ARCANO)+"Arcane Prisma&r&f for &b&o30 seconds&r&f.","",
				"&r"+PrintUtils.color(ObsColors.ARCANO)+"Arcane Prisma &eEffect&f: Boosts drop rate of certain items by &b&o20%&r&f.",
				"&r&fLuck-based buffs/items stack, and may contribute to these select items:");
	}

	public static Set<UUID> arcane_prisma_registry = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (arcane_prisma_registry.contains(p.getUniqueId())) return -1;
		ObsParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 7, 0.75, 0.1, Particle.ENCHANT, null);
		ObsParticles.drawWave(Ouroboros.instance, p.getLocation(), 9, 0.3, 8, Particle.BLOCK_CRUMBLE, Material.PRISMARINE_BRICKS.createBlockData());
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		arcane_prisma_registry.add(p.getUniqueId());
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			ObsParticles.drawArcanoCastSigil(p);
			PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.ARCANO)+"Arcane Prisma&r&7&o expired..");
			EntityEffects.playSound(p, Sound.BLOCK_BEACON_DEACTIVATE, SoundCategory.AMBIENT);
			if (arcane_prisma_registry.contains(p.getUniqueId())) arcane_prisma_registry.remove(p.getUniqueId());
		}, 600);
		
		return 350;
	}

	@Override
	public int getTotalManaCost()
	{
		return 350;
	}

}
