package com.lol.spells.instances.cosmo;

import org.bukkit.Bukkit;
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
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CosmoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Nullify extends Spell
{

	public Nullify() 
	{
		super("Nullify", "nullify", Material.ENDER_PEARL, SpellType.CONTROL, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 20, 3, false,
				"&r&fNullify target's &7(20m)&f defenses applying &3&oVoided&r&7 (30s)&f and &6&oBreak&r&f.","",
				"&r&3Voided &eEffect&f: neutralizes affected entity's elemental affinity.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (!RayCastUtils.getEntity(p, 20, target ->
		{
			if (target instanceof Player || MobData.getMob(target.getUniqueId()) == null) return;
			
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.END_ROD, null);
			EntityEffects.playSound(p, Sound.ENTITY_SHULKER_SHOOT, SoundCategory.AMBIENT);
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				OBSParticles.drawVerticalVortex(target.getLocation(), target.getWidth(), (int) target.getHeight()+1, 0.5, 45, 6, 0.1, Particle.SCULK_SOUL, null);
				OBSParticles.drawCylinder(target.getLocation(), target.getWidth(), (int) target.getHeight(), 5, 0.5, 0.1, Particle.ENCHANT, null);
				EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_CLOSE_SHUTTER, SoundCategory.AMBIENT);
				CosmoEffects.addVoided((LivingEntity) target, 30); 
				MobData.getMob(target.getUniqueId()).setBreak();
			}, 15);
			
		})) return -1;
		return this.getManacost();
	}

	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

}
