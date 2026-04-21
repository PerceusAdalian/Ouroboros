package com.lol.spells.instances.heresio;

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
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.HeresioEffects;

public class Beguile extends Spell
{
	public Beguile() 
	{
		super("Beguile", "beguile", Material.ENDER_EYE, SpellType.CURSE, SpellementType.HERESIO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 50, 2, true,
				"&r&fInflict &2Intimidated &bII &fon &6target &dMob&f/&c&oPlayer&r&7 (20m, 30s | &cPVP&7: 20s)","",
				"&r&2Intimidated &eEffect&f: affected are &b&oFatigued&r&f and &b&oWeakened&r&f equal to",
				"&r&fthe &b&omagnitude&r&f of &2Intimidated&f, and take 20% more "+PrintUtils.color(ObsColors.HERESIO)+"&lHeresio&r&f damage.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 30, target ->
		{
			if (!(target instanceof LivingEntity le)) return;
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.4, Particle.WITCH, null);
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.4, Particle.TRIAL_OMEN, null);
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.4, Particle.WARPED_SPORE, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawHeresioCastSigil(p);
				HeresioEffects.addIntimidate(le, 1, target instanceof Player ? 20 : 30);
			}, 20);
		})) return -1;
		
		return 50;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 50;
	}

}
