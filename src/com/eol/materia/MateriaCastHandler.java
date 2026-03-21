package com.eol.materia;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.eol.enums.MateriaComponent;
import com.eol.materia.instances.Materia;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.utils.PrintUtils;

public class MateriaCastHandler implements Listener
{
	public static void register(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new MateriaCastHandler(), plugin);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		ItemStack held = p.getInventory().getItemInMainHand();
		
		if (e.getHand() == null) return;
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (held == null || held.getType() == Material.AIR) return;
		
        PersistentDataContainer pdc = held.getItemMeta().getPersistentDataContainer();
        
        boolean validInteractMethod = CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR)
    			|| CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_BLOCK)
    			|| CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR)
    			|| CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_BLOCK);
        
        if (pdc.has(Materia.materiaKey) && pdc.has(Materia.componentKey)) 
        {
            String component = pdc.get(Materia.componentKey, PersistentDataType.STRING);
            if (component == null || !component.equals(MateriaComponent.CATALYST.getKey())) return;
            
            if (validInteractMethod)
            {
                PrintUtils.Print(p, "Interact Event Logged");
            }
        }
        
	}
}
