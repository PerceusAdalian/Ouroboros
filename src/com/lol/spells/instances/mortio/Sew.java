package com.lol.spells.instances.mortio;

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
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class Sew extends Spell
{

	public Sew() 
	{
		super("Sew", "sew", Material.CLOSED_EYEBLOSSOM, SpellType.CURSE, SpellementType.MORTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 5, 1, true,
				"&r&fInflict &4Curse&r&f &bI&r&f to target within &b&o7 meters &r&7(1min | &cPVP&7: &c3min&7)",
				"&r&4Curse &eEffect&f: Applies a debilitation that &b&oslows&r&f, &b&oweakens&r&f,",
				"&r&fand &b&ofatigues&r&f those afflicted. Curses are &e&ocurable&r&f and do not stack.",
				"&r&fThose afflicted by a &4&ocurse&r&f also receive &b&o20%&r&f more &4&lMortio&r&f damage.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 7);
		if (target == null || !(target instanceof LivingEntity)) return false;
		
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.SMOKE, null);
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.4, 0.6, Particle.ASH, null);
		if(!EntityEffects.addCurse((LivingEntity) target, 0, target instanceof Player ? 14 : 20))
		{
			PrintUtils.PrintToActionBar(p, "&cTarget already cursed!");
			return false;
		}
		return true;			
	}

}
