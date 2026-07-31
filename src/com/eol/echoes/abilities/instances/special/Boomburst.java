package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.EchoManager;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.echoes.records.EchoManifest;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Boomburst extends EchoAbility
{

	public Boomburst()
	{
		super("Boomburst", "seth_boomburst", Material.NETHER_STAR, StatType.MELEE, 0, 0, 20, AbilityType.ULTIMATE, ElementType.AERO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.POLEARM,
				"&r&fExpel a compressed, ultra-high-pitched sonic",
				"&r&fburst collapsing infront of you, dealing",
				"&r&b&l250% &r&f&oBase Atk &r&fas "+PrintUtils.color(ObsColors.AERO)+"&lAero&r&f dmg and &6&oBreak&r&f all &e->&f &d"+Symbols.CONAL+" &7(25m)");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		EchoManifest codec = EchoManager.getCodec(e.getItem());
		double dmg = codec.baseStats().getAttack() * 2.5;
		
		if (!RayCastUtils.getEntitiesInFov(p, 30, 90, target -> 
		{
			if (target == null || !(target instanceof LivingEntity le)) return;
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
			{
				le.getWorld().strikeLightningEffect(le.getLocation());
				ObsParticles.drawAeroCastSigil(le);
				MobData.damageUnnaturally(p, target, dmg, true, false, ElementType.AERO, codec);
				MobData.getMob(le.getUniqueId()).setBreak();
			}, 12);
			
		})) return -1;
		
		EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.AMBIENT);
		Block bTarget = RayCastUtils.rayTraceBlock(p, 30);
		Location targetLoc = bTarget != null ? bTarget.getLocation() : p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(25));
		ObsParticles.drawLine(p.getEyeLocation(), targetLoc, 1,   -.5, Particle.CRIT,      null);
		ObsParticles.drawLine(p.getEyeLocation(), targetLoc, 0.5, -.5, Particle.END_ROD,   null);
		ObsParticles.drawLine(p.getEyeLocation(), targetLoc, 6,   -.5, Particle.SONIC_BOOM,null);
		
		return 20;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 20;
	}

}
