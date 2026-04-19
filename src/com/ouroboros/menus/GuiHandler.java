package com.ouroboros.menus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GuiHandler implements Listener
{
	private static final Map<UUID, ObsGui> openGuis = new HashMap<>();
	private static final Set<UUID> transitioning = new HashSet<>();
	
    public static void open(Player player, ObsGui gui) 
    {
        openGuis.put(player.getUniqueId(), gui);
        gui.open();
    }
    
    public static void reload(Player player) 
    {
    	transitioning.add(player.getUniqueId());
    	openGuis.get(player.getUniqueId()).open();
    }

    public static ObsGui getOpenGui(Player player) 
    {
        return openGuis.get(player.getUniqueId());
    }

    public static void close(Player player) 
    {
        openGuis.remove(player.getUniqueId());
        player.closeInventory();
    }

    public static void changeMenu(Player player, ObsGui gui) 
    {
    	transitioning.add(player.getUniqueId());
    	close(player);
    	open(player, gui);
    }
    
    public static void registerEvent(JavaPlugin plugin) 
    {
        Bukkit.getPluginManager().registerEvents(new Listener() 
        {
            @EventHandler
            public void onClick(InventoryClickEvent e) 
            {
                if (!(e.getWhoClicked() instanceof Player p)) return;
                ObsGui gui = openGuis.get(p.getUniqueId());
                if (gui == null || !e.getView().getTitle().equals(gui.getGuiTitle())) return;

                gui.handleClick(e);
            }

            @EventHandler
            public void onClose(InventoryCloseEvent e) 
            {
                UUID uuid = e.getPlayer().getUniqueId();
                if (transitioning.remove(uuid)) return;
                openGuis.remove(uuid);
                ObsGui gui = openGuis.remove(uuid);
                if (gui != null) gui.close(e);
            }
        }, plugin);
    }
}
