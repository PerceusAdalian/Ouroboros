package com.eol.echoes.abilities.instances.hammer;

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
import com.ouroboros.utils.entityeffects.GeoEffects;

public class Bulwark extends EchoAbility
{
	
	public Bulwark() 
	{
		super("Bulwark", "bulwark", Material.LEATHER_CHESTPLATE, StatType.MELEE, 15, 3, 35, AbilityType.DEFENSIVE, ElementType.MODULO, CastConditions.RIGHT_CLICK_AIR, EchoForm.HAMMER, 
				"&r&fGrants &6Guarded &bV &fand &b&oResistance&r&f to &6self &7(30s)","",
				"&r&6Guarded &eEffect&f: Halves incoming damage for 5 instances.");
	}
	
	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (!GeoEffects.addGuarded(p, 5, 30)) return -1;
		EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.AMBIENT);
		EntityEffects.add(p, PotionEffectType.RESISTANCE, 600, 0);
		return 35;
	}
	
	@Override
	public int getFinalDurabilityCost() 
	{
		return 35;
	}
	
}
