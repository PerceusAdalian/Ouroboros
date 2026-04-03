package com.lol.spells.instances.glacio;

import org.bukkit.Material;
import org.bukkit.Particle;
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
import com.ouroboros.utils.entityeffects.GlacioEffects;

public class Glacius extends Spell
{
	
	public Glacius() 
	{
		
		super("Glacius", "glacius", Material.TIPPED_ARROW, SpellType.OFFENSIVE, SpellementType.GLACIO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 20, 1.5, false,
				"&r&fSend a frozen lance of ice at your target &7(20m)&f dealing",
				"&r&f5&c♥&r"+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f damage &r&fapplying &bFrosted I &7(10s)",
				"&bFrosted&f Effect: &d&oSlows&r&f and &d&oWeakens&r&f those afflicted.","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&b&oIce Spike&r&7&o', however, colloquially known as '&r&b&oGlacius&r&7&o'.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (!RayCastUtils.getEntity(p, 20, target ->
		{
			if (target == null || !(target instanceof LivingEntity)) return;
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.2, 0.5, Particle.SNOWFLAKE, null);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.7, 0.5, Particle.CRIT, null);
			MobData.damageUnnaturally(p, target, 5, true, ElementType.GLACIO);
			GlacioEffects.addFrosted((LivingEntity) target, 0, 10);
		})) return -1;
		return this.getManacost();
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

}
