package com.lol.spells.instances.aero;

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
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Diffindo extends Spell
{

	public Diffindo() 
	{
		super("Diffindo", "diffindo", Material.WIND_CHARGE, SpellType.OFFENSIVE, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.FIVE, 150, 3.5, true, false,
				"&r&fDeal 45&c♥&f &e&lSever&r&f damage to &6target &7(30m | &cPVP&7: &c15♥&7)","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&d&oSlicing Tempest&r&7&o', however, colloquially known as '&r&d&oDiffindo&r&7&o'.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 30, target ->
		{
			if (!(target instanceof LivingEntity) || target == null) return;
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 3, 1, Particle.SWEEP_ATTACK, null);
			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.AMBIENT);
			MobData.damageUnnaturally(p, target, target instanceof Player ? 15 : 45, true, true, ElementType.SEVER);
		})) return -1;

		return 150;
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return 150;
	}
	
}
