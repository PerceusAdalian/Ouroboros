package com.eol.echoes.abilities.instances.polearm;

import org.bukkit.Bukkit;
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
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Lunge extends EchoAbility
{

	public Lunge()
	{
		super("Lunge", "lunge_polearm", Material.IRON_SPEAR, StatType.MELEE, 5, 1, 15, AbilityType.COMBAT, ElementType.MODULO, CastConditions.RIGHT_CLICK_AIR, EchoForm.POLEARM,
				"&r&fLunge at &6target &dMob&f, dealing 50&c"+Symbols.HP+" &e&lImpale&r&f damage &7(15m)");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 15, target ->
		{
			if (!(target instanceof LivingEntity le) || le instanceof Player) return;
			EntityEffects.rushEntity(p, le, 1.5);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{				
				EntityEffects.playSound(p, Sound.ITEM_SPEAR_LUNGE_1, SoundCategory.MASTER);
				ObsParticles.drawLandingWave(le);
				MobData.damageUnnaturally(p, le, 50, true, true, ElementType.IMPALE, EchoManager.getCodec(e.getItem()));
			}, 10);
		})) return -1;
		
		return 15;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 15;
	}

}
