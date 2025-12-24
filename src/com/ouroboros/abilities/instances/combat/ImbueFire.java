package com.ouroboros.abilities.instances.combat;

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
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.enums.AbilityMaterialClass;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.AbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;

public class ImbueFire extends AbstractOBSAbility 
{

	public ImbueFire() 
	{
		super("Imbue Fire", "imbuefire", Material.BLAZE_POWDER, StatType.MELEE, 3, 1, AbilityType.COMBAT,
				ElementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, AbilityMaterialClass.ANY,
				"&r&fGrants self the &c&oImbued&r&f effect.","",
				"&r&c&oImbued&r&f: Surrounds you in &c&lInferno&r&f energy.",
				"&r&fAll attacks deal &c&lInferno&r&f damage in addition to the normal damage.",
				"&r&fTargets hit are afflicted with &cBurn &7(5s)&r&f, and damage dealt is increased by &e+&f&l1.1&r&fx.",
				"&r&eAbility Duration&r&f: 30s");
	}

	public static Map<UUID, Boolean> isImbuedPlayer = new HashMap<>();

	@Override
	public boolean cast(Event e) 
	{
		if (e instanceof PlayerInteractEvent pie) 
		{
			Player p = pie.getPlayer();

			if (isImbuedPlayer.containsKey(p.getUniqueId()))
			{
				PrintUtils.PrintToActionBar(p, "Already Imbued");
				return false;
			}
			
			EntityEffects.playSound(p, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER);
			EntityEffects.playSound(p, Sound.ENTITY_BLAZE_BURN, SoundCategory.MASTER);
			OBSParticles.drawInfernoCastSigil(p);
			isImbuedPlayer.put(p.getUniqueId(), true);

			Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(Ouroboros.class), () -> 
			{
				isImbuedPlayer.remove(p.getUniqueId());
				EntityEffects.playSound(p, Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.MASTER);
				OBSParticles.drawInfernoCastSigil(p);
			}, 600);

			return true;
		}
		return false;
	}
}
