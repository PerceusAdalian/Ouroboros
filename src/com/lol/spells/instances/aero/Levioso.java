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
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class Levioso extends Spell
{

	public Levioso() 
	{
		super("Levioso", "levioso", Material.STRING, SpellType.CONTROL, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 10, 3, true,
				"&r&fApplies &3&oLevitate &r&bI&r&f to either target (&b&o20 meters&r&f) or to self &7(10s).","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&d&oLight Weighted&r&7&o', however, colloquially known as '&r&d&oLevioso&r&7&o'.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = (Player) e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 20);
		if (target instanceof LivingEntity) 
		{
			EntityEffects.playSound(p, Sound.ENTITY_BREEZE_SHOOT, SoundCategory.AMBIENT);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 3, 0.5, Particle.VIBRATION, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{				
				EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.AMBIENT);
				OBSParticles.drawDisc(target.getLocation(), target.getWidth(), 3, 5, 0.5, Particle.GUST_EMITTER_SMALL, null);
				EntityEffects.add((LivingEntity) target, PotionEffectType.LEVITATION, 200, 0, true);
			}, 10);
			return true;
		}
		else if(target == null)
		{
			EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.AMBIENT);
			OBSParticles.drawDisc(p.getLocation(), p.getWidth(), 3, 5, 0.5, Particle.GUST_EMITTER_SMALL, null);
			EntityEffects.add(p, PotionEffectType.LEVITATION, 200, 0, true);
			return true;
		}
		return false;
	}

}
