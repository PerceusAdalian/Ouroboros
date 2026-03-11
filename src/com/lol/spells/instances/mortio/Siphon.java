package com.lol.spells.instances.mortio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class Siphon extends Spell
{

	public Siphon() 
	{
		super("Siphon Life", "siphon", Material.RED_DYE, SpellType.OFFENSIVE, SpellementType.MORTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 10, 1, true,
				"&r&4&oDrain &r&b&o20%&r&7* &fof your target's current max HP within &b&o20 meters&r&f.",
				"&r&f&a&oRestore &r&b&o50%&r&7** &r&fof the damage dealt to self &7(&cPVP&f: &c15%&7*&f, &c30%&7**)");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getEntity(p, 20);
		
		if (target == null || !(target instanceof LivingEntity)) return -1;
		
		MobData data = MobData.getMob(target.getUniqueId());
		if (data == null) return -1;
		
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		OBSParticles.drawLine(target.getLocation(), p.getLocation(), 0.3, 0.4, Particle.CRIMSON_SPORE, null);
		OBSParticles.drawLine(target.getLocation(), p.getLocation(), 0.7, 0.4, Particle.SMOKE, null);
		
		double damage;
		if (target instanceof Player player) 
		{
		    damage = player.getHealth() * 0.15d;
		    player.damage(damage);
		} 
		else damage = MobData.damageUnnaturally(p, target, data.getHp(true) * 0.2d, true, ElementType.MORTIO);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			OBSParticles.drawArcLine(target.getLocation(), p.getLocation(), 0.3, 3, Particle.ASH, null);
			OBSParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 6, Particle.HAPPY_VILLAGER, null);
			EntityEffects.playSound(p, Sound.ITEM_BONE_MEAL_USE, SoundCategory.AMBIENT);
			EntityEffects.heal(p, target instanceof Player ? damage * 0.3d : damage * 0.5d);			
		}, 15);
		
		return this.getManacost();
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

}
