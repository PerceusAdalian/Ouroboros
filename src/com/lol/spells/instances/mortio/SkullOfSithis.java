package com.lol.spells.instances.mortio;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
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
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class SkullOfSithis extends Spell
{

	public SkullOfSithis() 
	{
		super("Skull of Sithis", "skull_of_sithis", Material.WITHER_SKELETON_SKULL, SpellType.OFFENSIVE, SpellementType.MORTIO, CastConditions.MIXED, Rarity.FOUR, 100, 1.5, false,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&fSummon a seething demon skull and launch it foward, exploding on impact.",
				"&r&fThe projectile may deal variable &4&lMortio&r&f damage, while the",
				"&r&fimpact may deal variable &e&lBlast&r&f and &c&lInferno&r&f damage.","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&fGaze upon your target &7(10m)&f, dealing 10&c♥&f &4&lMortio&r&f damage and inflicting &4Dread &7(30s)","",
				"&r&4Dread &eEffect&f: Applies a debilitation that causes &b&ohunger&r&f and &b&oblindness&r&f",
				"&r&fto those afflicted. Dread is &e&ocurable&r&f and does not stack, however",
				"&r&fsubsequent applications will inflict &4Doom&f after a second application.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			Entity target = RayCastUtils.getNearestEntity(p, 10);
			if (target == null || !(target instanceof LivingEntity le)) return false;
			
			EntityEffects.playSound(p, Sound.ENTITY_ILLUSIONER_CAST_SPELL, SoundCategory.AMBIENT);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.SMOKE, null);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.4, 0.6, Particle.ASH, null);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.3, 0.6, Particle.CRIMSON_SPORE, null);
			
			EntityEffects.addDread(le, 30);
			MobData.damageUnnaturally(p, target, 10, true, ElementType.MORTIO);
			return true;
		}
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			EntityEffects.playSound(p, Sound.ENTITY_WITHER_SHOOT, SoundCategory.AMBIENT);
			WitherSkull skull = p.launchProjectile(WitherSkull.class);
			skull.setVelocity(p.getLocation().getDirection().multiply(2.5));
			skull.setAcceleration(skull.getAcceleration().multiply(3));
			skull.setCharged(true);
			skull.setGravity(false);
			skull.setInvulnerable(true);
			return true;
		}
		return false;
	}

}
