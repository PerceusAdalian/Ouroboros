package com.eol.echoes.abilities.instances.sword;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class GeminiSlash extends EchoAbility
{

	public GeminiSlash() 
	{
		super("Gemini Slash", "gemini_slash_ability", Material.ECHO_SHARD, StatType.MELEE, 10, 5, 30, AbilityType.COMBAT, ElementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD,
				"&r&fDeal &l5&r&c♥ "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f damage applying &eExposed &7(5m, 15s)","",
				"&r&eExposed Effect&r&f: Reveals an entity's location and &6&oBreaks &r&fthem.",
				"&r&fIf those affected are "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f-based, they instantly die.");
	}

	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 5, target ->
		{
			if (!(target instanceof LivingEntity le) || (target instanceof Player)) return;
			EntityEffects.rushEntity(p, le, 2);
			
			ObsParticles.drawLine(p.getLocation(), target.getLocation(), 1, 0.5, Particle.CLOUD, null);
			EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER);
			MobData.damageUnnaturally(p, target, 10, true, true, ElementType.CELESTIO);
			CelestioEffects.addExposed(le, 300);
		})) return -1;
		return 30;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 30;
	}
}
