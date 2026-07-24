package com.eol.echoes.abilities.instances.hatchet;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.EchoManager;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class HeavyChop extends EchoAbility
{

	public HeavyChop()
	{
		super("Heavy Chop", "heavy_chop", Material.IRON_AXE, StatType.WOODCUTTING, 5, 1, 15, AbilityType.OFFENSIVE, ElementType.MODULO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.HATCHET, 
				"&r&fDeal 50&c"+Symbols.HP+" &fBlunt damage to &6target &dMob &7(6m)");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 6, target ->
		{
			if (!(target instanceof LivingEntity le) || le instanceof Player) return;
			EntityEffects.rushEntity(p, le, 1.3);
			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.MASTER);
			ObsParticles.drawLandingDisc(le);
			MobData.damageUnnaturally(p, le, 50, true, true, ElementType.BLUNT, EchoManager.getCodec(e.getItem()));
		})) return -1;
		
		return 15;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 15;
	}

}