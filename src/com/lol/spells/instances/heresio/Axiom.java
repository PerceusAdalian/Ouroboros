package com.lol.spells.instances.heresio;

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
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Axiom extends Spell
{
	public Axiom()
	{
		super("Axiom", "axiom", Material.GREEN_DYE, SpellType.CANTRIP, SpellementType.HERESIO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 10, 1, false,
				"&r&fDeal 5&c♥&f of "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f damage to &6&otarget &r&dMob &7(15m)");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();

		if (!playSpellEffect(p, 5, 15)) return -1;
		
		return 10;
	}

	@Override
	public int getTotalManaCost()
	{
		return 10;
	}

	public static boolean playSpellEffect(Player p, double damage, int range)
	{
		if (!RayCastUtils.getEntity(p, range, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.7, 0.5, Particle.GLOW_SQUID_INK, null);
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.4, Particle.WARPED_SPORE, null);
			MobData.damageUnnaturally(p, le, damage, true, true, ElementType.HERESIO);
		})) return true;
		return false;
	}
	
}
