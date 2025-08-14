package com.ouroboros.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.utils.AbilityObjectCategory;

public class AbilityCastHandler implements Listener 
{

    public static void register(JavaPlugin plugin) 
    {
        Bukkit.getPluginManager().registerEvents(new AbilityCastHandler(), plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) 
    {
        Player p = e.getPlayer();
        ItemStack held = p.getInventory().getItem(EquipmentSlot.HAND);

        if (e.getHand() == null) return;
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (held == null || held.getType() == Material.AIR) return;

        for (AbstractOBSAbility ability : AbilityRegistry.abilityRegistry.values()) 
        {
            if (PlayerData.getPlayer(p.getUniqueId()).getAbility(ability).isActive()) 
            {
            	if (!CastConditions.isValidAction(e, ability.getCastCondition())) return;
            	if (!AbilityObjectCategory.canAccept(p, ability.getAbilityCategory())) return;
            	e.setCancelled(false);
                ability.cast(e);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) 
    {
        if (!(e.getEntity() instanceof Player p)) return;

        for (AbstractOBSAbility ability : AbilityRegistry.abilityRegistry.values()) 
        {
            if (PlayerData.getPlayer(p.getUniqueId()).getAbility(ability).isActive())
            {
                ability.cast(e);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) 
    {
        if (!(e.getEntity() instanceof Player p)) return;

        for (AbstractOBSAbility ability : AbilityRegistry.abilityRegistry.values())
        {
            if (PlayerData.getPlayer(p.getUniqueId()).getAbility(ability).isActive()) 
            {
                ability.cast(e);
            }
        }
    }
}

