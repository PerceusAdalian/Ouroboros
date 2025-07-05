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
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ObsAbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.ValidObjectsHandler;

public class ImbueFire extends AbstractOBSAbility
{

	public ImbueFire() 
	{
		super("Imbue Fire", "imbuefire", Material.BLAZE_POWDER, StatType.MELEE, 3, 1, ObsAbilityType.COMBAT, null, 
				"&r&f&lRight_Click&r&f to enchant your blade with &c&lInferno&r&f energy.",
				"&r&f&lDuration&r&f: 30 seconds");
	}

	private static boolean hasEnchantPreviously = false;
	private static int previousEnchantLevel;
	public static Map<UUID, Boolean> castHandler = new HashMap<>();
	public static Map<UUID, Boolean> cleanUpPlayer = new HashMap<>();
	
	public final static NamespacedKey enchantKey = new NamespacedKey(Ouroboros.instance, "imbue_fire");
	
	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		if (!PlayerActions.rightClickAir(e)) return false;
		
		Player p = e.getPlayer();
		
		if (!PlayerData.getPlayer(p.getUniqueId()).getAbility(this).isActive()) return false;
		
		if (castHandler.containsKey(p.getUniqueId())) return false;
		
		ItemStack held = p.getInventory().getItem(EquipmentSlot.HAND);
		if (!ValidObjectsHandler.swords.contains(held.getType())) return false;
		
		ItemMeta meta = held.getItemMeta();
		if (meta.hasEnchant(Enchantment.FIRE_ASPECT)) 
		{
			hasEnchantPreviously = true;
			previousEnchantLevel = meta.getEnchants().get(Enchantment.FIRE_ASPECT);
		}
		
		meta.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
		meta.getPersistentDataContainer().set(enchantKey, PersistentDataType.INTEGER, 1);
		EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 1, 1);
		EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_BLAZE_BURN, SoundCategory.MASTER, 1, 1);
		held.setItemMeta(meta);
		
		Bukkit.getScheduler().runTaskLaterAsynchronously(Ouroboros.instance, ()->
		{
			if (!p.isOnline()) 
			{
				cleanUpPlayer.put(p.getUniqueId(), true);
				return;
			}
			
			removeBuff(p);
			
			castHandler.remove(p.getUniqueId());
		}, 600);
		
		castHandler.put(p.getUniqueId(), true);
		return true;
	}
	
	public static void registerCleanupHandler(JavaPlugin plugin) 
    {
        Bukkit.getPluginManager().registerEvents(new Listener() 
        {	
        	@EventHandler
        	public void onJoin(PlayerJoinEvent e) 
        	{
        		Player p = e.getPlayer();
        		if (cleanUpPlayer.containsKey(p.getUniqueId())) 
        		{
        			removeBuff(p);
        			cleanUpPlayer.remove(p.getUniqueId());
        			castHandler.remove(p.getUniqueId());
        		}
        	}

        	@EventHandler
        	public void onQuit(PlayerQuitEvent e) 
        	{
        		Player p = e.getPlayer();
        		removeBuff(p);
        		cleanUpPlayer.remove(p.getUniqueId());
        		castHandler.remove(p.getUniqueId());
        	}
        	
        }, plugin);
    }
	
	public static void removeBuff(Player p) 
	{
		for (ItemStack item : p.getInventory()) 
		{
	        if (item == null || !item.hasItemMeta()) continue;

	        ItemMeta m = item.getItemMeta();
	        if (m.getPersistentDataContainer().has(enchantKey, PersistentDataType.INTEGER)) 
	        {
	            m.removeEnchant(Enchantment.FIRE_ASPECT);
	            m.getPersistentDataContainer().remove(enchantKey);

	            if (hasEnchantPreviously) 
	            {
	                m.addEnchant(Enchantment.FIRE_ASPECT, previousEnchantLevel, true);
	            }

	            item.setItemMeta(m);
	            break;
	        }
	    }
	}

}
