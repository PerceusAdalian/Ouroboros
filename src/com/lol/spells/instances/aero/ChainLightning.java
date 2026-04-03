package com.lol.spells.instances.aero;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
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
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.AeroEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ChainLightning extends Spell
{

	public ChainLightning()
	{
		super("Chain Lightning", "chain_lightning", Material.LIGHTNING_ROD, SpellType.OFFENSIVE, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 100, 5, false,
				"&r&fExpel lightning that chains between up to &l5&r&f targets.",
				"&r&fDeals 15&c♥&f "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f damage to the first and 10&c♥&f to all others.",
				"&r&fInflicts &dShock &7(10s)&f on each target hit.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{	    
		Player p = e.getPlayer();
	 
		List<Entity> mobs = RayCastUtils.getEntitiesInFov(p, 20.0, 5);
	    
		if (mobs == null || mobs.isEmpty()) return -1;

		EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.AMBIENT);
	    
	    for (int i = 0; i < mobs.size(); i++)
	    {
	        if (!(mobs.get(i) instanceof LivingEntity le)) continue;
	        
	        Location from = i == 0 ? p.getLocation() : ((LivingEntity) mobs.get(i - 1)).getLocation();
	        Location to = le.getLocation();

	        OBSParticles.drawLine(from, to, 0.6, 0.5, Particle.END_ROD, null);
	        OBSParticles.drawLine(from, to, 0.4, 0.4, Particle.CRIT, null);
	        OBSParticles.drawLine(from, to, 0.7, 0.4, Particle.DUST, new DustOptions(Color.PURPLE, 1f));
	        OBSParticles.drawAeroCastSigil(le);
	        
	        MobData.damageUnnaturally(p, le, i == 0 ? 15 : 10, true, ElementType.AERO);
	        AeroEffects.addShock(le, 10);
	        
	        if (i == mobs.size() - 1) le.getWorld().strikeLightningEffect(le.getLocation());
	    }

	    return 100;
	}

	@Override
	public int getTotalManaCost()
	{
		return 100;
	}

}
