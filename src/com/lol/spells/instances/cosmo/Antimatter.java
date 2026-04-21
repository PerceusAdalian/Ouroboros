package com.lol.spells.instances.cosmo;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CosmoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Antimatter extends Spell
{

	public Antimatter()
	{
		super("Antimatter", "antimatter", Material.ENDER_PEARL, SpellType.OFFENSIVE, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 25, 2, false,
				"&r&fDecay target's life dealing "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f damage",
				"&r&fequal to &b&o3 + 25% &r&aHP &7(20m)&f and applies &3Voided &7(20s)","",
				"&r&3Voided &eEffect&f: neutralizes affected entity's elemental affinity.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (!RayCastUtils.getEntity(p, 20, target ->
		{
			if (!(target instanceof LivingEntity)) return;
			MobData data = MobData.getMob(target.getUniqueId());
			EntityEffects.playSound(p, Sound.ENTITY_SHULKER_SHOOT, SoundCategory.AMBIENT);
			ObsParticles.drawCosLine(p.getLocation(), target.getLocation(), 0.7, Particle.DRAGON_BREATH, 0.5f);
			ObsParticles.drawSinLine(p.getLocation(), target.getLocation(), 1, Particle.END_ROD, null);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.BLOCK_SCULK_CATALYST_BLOOM, SoundCategory.AMBIENT);
				ObsParticles.drawDisc(target.getLocation(), target.getWidth(), 4, 10, 0.5, Particle.WARPED_SPORE, null);
				if (target instanceof Player)
				{
					((Damageable) target).damage(((Damageable)target).getHealth()*0.25);
				}
				else if (data != null)
				{
					MobData.damageUnnaturally(p, target, 3 + data.getHp(false) * 0.25, true, true, ElementType.COSMO);
					CosmoEffects.addVoided((LivingEntity) target, 20);
				}
			}, 10);
			
		})) return -1;
		return this.getManacost();
	}

	@Override
	public int getTotalManaCost()
	{
		return this.getManacost();
	}

}
