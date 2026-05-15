package com.eol.echoes.abilities.instances.sword;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class ImbueFire extends EchoAbility 
{

	public ImbueFire() 
	{
		super("Enchant Blade: "+PrintUtils.color(ObsColors.INFERNO)+"&lImbue Fire&r&f", "imbuefire", Material.BLAZE_POWDER, StatType.MELEE, 3, 1, 15, 
				AbilityType.COMBAT, ElementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD,
				"&r&fGrants "+PrintUtils.color(ObsColors.INFERNO)+"Fire Imbued&l&r&f to &6self &7(30s)","",
				PrintUtils.color(ObsColors.INFERNO)+"Fire Imbued &r&eEffect&f: Outgoing attacks",
				"&r&fbecome "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f damage.");
	}

	
	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (!InfernoEffects.addImbueFire(p, 30)) return -1;
		
		return 15;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 15;
	}
}
