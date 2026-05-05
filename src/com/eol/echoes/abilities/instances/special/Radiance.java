package com.eol.echoes.abilities.instances.special;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Radiance extends EchoAbility
{

	public Radiance()
	{
		super("Radiance", "radiance", Material.NETHER_STAR, StatType.MELEE, 0, 0, AbilityType.SPECIALABILITY, ElementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD,
				"&r&fRemove &e&l50 &r&b&oDurability&r&f:",
				"&r&fApplies &e&oExposed&r&7 (10s) &r&fto all nearby entities within 25 &b&ometers&r&f.","",
				"&r&e&oExposed &r&fEffect: Reveals an entity's location and &6&oBreaks &r&fthem.",
				"&r&fIf those affected are "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f-based, they instantly die.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		EntityEffects.playSound(p, Sound.ENTITY_ELDER_GUARDIAN_CURSE, SoundCategory.AMBIENT);
		if (!RayCastUtils.getNearbyEntities(p, 25, (target)->
		{
			CelestioEffects.addExposed(target, 10);	
		})) return -1;
		return 50;
	}

}
