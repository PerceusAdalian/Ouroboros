package com.lol.spells.instances.admin;

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
import com.ouroboros.ObsCommand;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class RemoveEntity extends Spell
{

	public RemoveEntity() 
	{
		super("Remove Entity", "admin_remove_entity", Material.SPECTRAL_ARROW, SpellType.DEBUG, SpellementType.NULL, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 0, 1, false, true,
				"&r&fKill target mob within &b&o50 meters&r&f.","",
				"&r&c&lWarning&r&f: This spell is intended for use by admins only.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (ObsCommand.affirmOP(p)) return -1;
		
		if (!RayCastUtils.getEntity(p, 50, target ->
		{
			if (!(target instanceof LivingEntity) || target instanceof Player) return;
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.CRIMSON_SPORE, null);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 1, 0.5, Particle.ASH, null);
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 1.5, 0.5, Particle.SMOKE, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ENTITY_ENDERMAN_HURT, SoundCategory.AMBIENT);
				ObsParticles.drawAdminCastSigil((LivingEntity) target);
				MobData data = MobData.getMob(target.getUniqueId());
				if (data != null) data.kill();
				else target.remove();
			}, 15);
		})) return -1;
		return 0;
	}

	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}
	
}
