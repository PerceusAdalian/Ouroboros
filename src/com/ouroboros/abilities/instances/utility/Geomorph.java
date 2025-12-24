package com.ouroboros.abilities.instances.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;
import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.enums.AbilityMaterialClass;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.AbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;

public class Geomorph extends AbstractOBSAbility
{

	public Geomorph() 
	{
		super("Geomorph", "geomorph", Material.ARMS_UP_POTTERY_SHERD, StatType.TRAVEL, 5, 2, AbilityType.UTILITY, ElementType.GEO, CastConditions.RIGHT_CLICK_BLOCK,
				AbilityMaterialClass.ANY, "Bolter your defenses by using clay as an insulator.",
				"&r&fGrants &e&oWard &r&b&lI&r&7 to self (30s/CD: 1 minute)");
	}

	private static Map<UUID, Boolean> geomorphCooldown = new HashMap<>();
	
	@Override
	public boolean cast(Event e) 
	{
		if (e instanceof PlayerInteractEvent pie)
		{
			Player p = pie.getPlayer();
			
			ItemStack held = p.getInventory().getItem(EquipmentSlot.HAND);
			if (!held.getType().equals(Material.CLAY_BALL)) return false;
			
			if (geomorphCooldown.containsKey(p.getUniqueId()))
			{
				PrintUtils.PrintToActionBar(p, "&f&oGeomorph Cooldown");
				EntityEffects.playSound(p, Sound.BLOCK_DEEPSLATE_BRICKS_BREAK, SoundCategory.AMBIENT);
				return false;
			}
			
			OBSParticles.drawGeoCastSigil(p);
			EntityEffects.add(p, PotionEffectType.RESISTANCE, 600, 2);
			EntityEffects.add(p, PotionEffectType.ABSORPTION, 600, 2);
			EntityEffects.add(p, PotionEffectType.FIRE_RESISTANCE, 600, 2);
			
			ItemCollector.remove(pie);
			
			geomorphCooldown.put(p.getUniqueId(), true);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				geomorphCooldown.remove(p.getUniqueId());
				EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.AMBIENT);
			}, 1200);
		}
		return false;
	}

}
