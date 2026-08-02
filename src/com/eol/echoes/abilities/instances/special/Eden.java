package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.joml.Math;

import com.eol.echoes.EchoManager;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.echoes.records.EchoManifest;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.EntityCategories;
import com.ouroboros.mobs.MobAffinity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Eden extends EchoAbility
{

	public Eden()
	{
		super("Eden", "eden", Material.NETHER_STAR, StatType.MELEE, 0, 0, 40, AbilityType.ULTIMATE, ElementType.COSMO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.POLEARM, 
				"&r&fRush and inflict &l500%&r&f of &b&oBase Atk&r&f as &6"+Symbols.TARGET+"&f's main &b&oWeakness &r&7(10m)",
				"&a&oHeal&r&f &l30%&r&f of damage dealt and grant "+PrintUtils.color(ObsColors.CELESTIO)+"&oEmpowered &r&bIII&f to &6self &7(20s)");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 10, target -> 
		{
			if (target == null || !(target instanceof LivingEntity le)) return;
			
			EntityEffects.rushEntity(p, le, 3);
			EntityEffects.playSound(p, Sound.ITEM_SPEAR_LUNGE_3, SoundCategory.AMBIENT);
			
			EchoManifest codec = EchoManager.getCodec(e.getItem());
			
			double dmg = codec.baseStats().getAttack() * 5;
			ElementType dType = MobAffinity.parseCoreWeakness(EntityCategories.getMobCategory(le.getType()));
			double healAmount = Math.ceil(dmg * 0.3);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ITEM_SPEAR_HIT, SoundCategory.MASTER);
				ObsParticles.drawLandingWave(le);
				ObsParticles.drawCosmoCastSigil(le);
				MobData.damageUnnaturally(p, target, dmg, true, true, dType, codec);
				PlayerData.heal(p, healAmount, false);
				CelestioEffects.addEmpowered(p, 2, 20);
			}, 10);
		})) return -1;
		
		return 40;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 40;
	}

}
