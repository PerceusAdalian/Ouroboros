package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.EchoManager;
import com.eol.echoes.EchoManager.DurabilityOperation;
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
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;

public class BloodrotBlade extends EchoAbility
{

	public BloodrotBlade()
	{
		super("Bloodrot Blade", "bloodrot_blade", Material.NETHER_STAR, StatType.MELEE, 0, 0, 0, AbilityType.ULTIMATE, ElementType.MORTIO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.MORTIO)+"Bloodrot Blade&f: "+PrintUtils.color(ObsColors.MORTIO)+"&oBloodletting&r&f --",
				"&r&fDeal &l250&r&c"+Symbols.HP+" to &6self&f, and restore &b&oDurability&r&f to 100%.",
				"&r&fApply &e&oWard &r&bIII&f to &6self&f and gain &l3&r&f stacks of &4&oJinx&r&7 (10s)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				PrintUtils.color(ObsColors.MORTIO)+"Bloodrot Blade&f: "+PrintUtils.color(ObsColors.MORTIO)+"&oHemomancy&r&f -- &cRemove &f15 &b&oDurability",
				"&r&cRemove&r&f stacks of &4&oJinx&r&f and rush towards &6"+Symbols.TARGET+" &fdealing &b&o150%",
				"&r&f&oBase Atk x &4&oJynx Stacks&r&f as "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f damage &7(7m)");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		if (data == null) return -1;
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (data.getHP() < 250) return -1;
			PlayerData.damageUnnaturally(p, p, 250, false, false, ElementType.PURE, null);
			EchoManager.modifyDurability(p, e.getItem(), DurabilityOperation.SETMAX, 0, false);
			CelestioEffects.addWard(p, 2, 10);
			MortioEffects.addJinxStacks(p, 3);
			return 0;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getEntity(p, 7, target ->
			{
				if (target == null || !(target instanceof LivingEntity le)) return;
				
				boolean hasJinxStacks = MortioEffects.jinxRegistry.containsKey(p.getUniqueId());
				int jinxStacks = hasJinxStacks ? MortioEffects.jinxRegistry.get(p.getUniqueId()).magnitude : 0;
				if (hasJinxStacks) MortioEffects.jinxRegistry.remove(p.getUniqueId());
				EchoManifest codec = EchoManager.getCodec(e.getItem());
				double damage = codec.baseStats().getAttack()*1.5*jinxStacks;
				
				EntityEffects.playSound(p, Sound.ENTITY_BREEZE_JUMP, SoundCategory.AMBIENT);
				EntityEffects.rushEntity(p, le, 3);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->{
					ObsParticles.drawLandingWave(le);
					MobData.damageUnnaturally(p, target, damage, true, true, ElementType.MORTIO, codec);
					ObsParticles.drawMortioCastSigil(le);
					EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER);
				}, 12);
			})) return -1;
			
			return 15;
		}
		
		return 0;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 0;
	}
}
