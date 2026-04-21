package com.lol.spells.instances.aero;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Levioso extends Spell
{

	public Levioso() 
	{
		super("Levioso", "levioso", Material.STRING, SpellType.CONTROL, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 10, 3, true,
				"&r&fApplies &3&oLevitate &r&bI&r&f to either target &7(20m)&f or to self &7(8s | &cPVP&f: &c5s&7)","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&d&oLight Weighted&r&7&o', however, colloquially known as '&r&d&oLevioso&r&7&o'.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = (Player) e.getPlayer();
		Entity target = RayCastUtils.getEntity(p, 20);
		
		if (target instanceof LivingEntity) 
		{
			EntityEffects.playSound(p, Sound.ENTITY_BREEZE_SHOOT, SoundCategory.AMBIENT);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 3, 0.5, Particle.CRIT, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{				
				EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.AMBIENT);
				ObsParticles.drawDisc(target.getLocation(), target.getWidth(), 3, 5, 0.5, Particle.GUST_EMITTER_SMALL, null);
				EntityEffects.add((LivingEntity) target, PotionEffectType.LEVITATION, target instanceof Player ? 100 : 160, 0, true);
			}, 10);
			return this.getManacost();
		}
		else if(target == null)
		{
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawDisc(p.getLocation(), p.getWidth(), 3, 5, 0.5, Particle.GUST_EMITTER_SMALL, null);
			EntityEffects.add(p, PotionEffectType.LEVITATION, 160, 0, true);
			return this.getManacost();
		}
		return -1;
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

}
