package com.eol.echoes.abilities.instances.sword;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Bolster extends EchoAbility 
{

	public Bolster()
	{
		super("Bolster", "bolster", Material.NETHER_STAR, StatType.MELEE, 5, 1, 5, AbilityType.BUFF, ElementType.GEO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD, 
				"&r&fGrants &b&oStrength&r&f to &6self &7(15s)");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (p.hasPotionEffect(PotionEffectType.STRENGTH)) return -1;
		EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_IRON, SoundCategory.AMBIENT);
		EntityEffects.add(p, PotionEffectType.STRENGTH, 300, 0, true);
		return 5;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 5;
	}
}
