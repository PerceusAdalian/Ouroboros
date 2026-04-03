package com.lol.spells.instances.arcano;

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
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.ArcanoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ArcaneBolt extends Spell
{

	public ArcaneBolt() 
	{
		super("Arcane Bolt", "arcane_bolt", Material.ECHO_SHARD, SpellType.OFFENSIVE, SpellementType.ARCANO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 25, 1, true,
				"&r&fDeal 4&c♥ "+PrintUtils.color(ObsColors.ARCANO)+"&lArcano&r&f damage to target &7(25m)&f and apply &bEther Overload &7(20s)","",
				"&r&bEther Overload &eEffect&f: those affected take an additional &b&o50% &r&e&oelemental &r&fdamage.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 25, target->
		{
			if (target == null || !(target instanceof LivingEntity)) return;
			MobData.damageUnnaturally(p, target, 4, true, true, ElementType.ARCANO);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.GLOW_SQUID_INK, null);
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ArcanoEffects.addEtherOverload((LivingEntity) target, 20);			
		})) return -1;
		
		return this.getManacost();
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

}
