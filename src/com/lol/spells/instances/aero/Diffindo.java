package com.lol.spells.instances.aero;

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
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;

public class Diffindo extends Spell
{

	public Diffindo() 
	{
		super("Diffindo", "diffindo", Material.WIND_CHARGE, SpellType.OFFENSIVE, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.FIVE, 150, 5, 
				"&r&fConcentrate &d&lAero&r&f energy and expell it forward at a target",
				"&r&fdealing 20&c♥&f of &e&lSever&r&f damage. Applies &dCharge &bI &fto self &7(10s)",
				"&r&f&lRange&r&f: 30 &b&ometers&r&f");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 30);
		if (!(target instanceof LivingEntity) || target == null) return false;
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 3, 1, Particle.SWEEP_ATTACK, null);
		EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.AMBIENT);
		MobData.damageUnnaturally(p, target, 20, true, ElementType.SEVER);
		return true;
	}
	
}
