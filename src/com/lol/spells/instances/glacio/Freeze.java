package com.lol.spells.instances.glacio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GlacioEffects;

public class Freeze extends Spell
{

	public Freeze()
	{
		super("Freeze", "freeze", Material.ICE, SpellType.CONTROL, SpellementType.GLACIO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 100, 4.5, true, false,
				"&r&fInflict &bFrozen&f and &6Break&f on &6target &7(15m)","",
				"&r&bFrozen &eEffect&f: Those affected by &bChill&f remove their stacks, and instead,",
				"&r&f\"Freezes\" them. They are &b&oWeakened&r&f by the amount of &bChill&f stacks they had.",
				"&r&cPVP&f: Freeze effect &6Stuns&f for &b&o5 seconds&r&f and &b&oWeakens&r&f for &b&o20 seconds&r&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (!RayCastUtils.getEntity(p, 15, target ->
		{
			if (!(target instanceof LivingEntity)) return;
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.4, 0.4, Particle.SNOWFLAKE, null);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.4, 0.4, Particle.BLOCK_CRUMBLE, Material.ICE.createBlockData());
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.BLOCK_GLASS_BREAK, SoundCategory.AMBIENT);
				ObsParticles.drawDisc(target.getLocation(), target.getWidth(), 3, 7, 1.5, Particle.BLOCK_CRUMBLE, Material.ICE.createBlockData());
				ObsParticles.drawDisc(target.getLocation(), target.getWidth(), 3, 7, 1.5, Particle.SNOWFLAKE, null);
				GlacioEffects.addFrozen((LivingEntity) target);
			}, 15);
		}))return -1;
		return 100;
	}

	@Override
	public int getTotalManaCost()
	{
		return 100;
	}

}
