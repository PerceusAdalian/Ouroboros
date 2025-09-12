package com.ouroboros.ability.instances.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.Ouroboros;
import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.enums.AbilityCategory;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsAbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;

public class ImbueFire extends AbstractOBSAbility {

	public ImbueFire() {
		super("Imbue Fire", "imbuefire", Material.BLAZE_POWDER, StatType.MELEE, 3, 1, ObsAbilityType.COMBAT,
				ElementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, AbilityCategory.SWORDS,
				"&r&d&oEnchant &r&fyour blade with &c&lInferno&r&f energy.",
				"&r&f&lDuration&r&f: 30 seconds");
	}

	public static Map<UUID, Boolean> isImbuedPlayer = new HashMap<>();

	@Override
	public boolean cast(Event e) {
		if (e instanceof PlayerInteractEvent pie) 
		{
			Player p = pie.getPlayer();

			EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1, 1);
			EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_BLAZE_BURN, SoundCategory.MASTER, 1, 1);
			OBSParticles.drawInfernoCastSigil(p);
			isImbuedPlayer.put(p.getUniqueId(), true);

			Bukkit.getScheduler().runTaskLater(JavaPlugin.getPlugin(Ouroboros.class), () -> 
			{
				isImbuedPlayer.remove(p.getUniqueId());
				EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.MASTER, 1, 1);
				OBSParticles.drawInfernoCastSigil(p);
			}, 600);

			return true;
		}
		return false;
	}
}
