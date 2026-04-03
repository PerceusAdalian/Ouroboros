package com.ouroboros.menus;

import java.util.HashMap;
import java.util.Map;
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

    public static void open(Player player, ObsGui gui) 
    {
        openGuis.put(player.getUniqueId(), gui);
        gui.open();
    }
    
    public static void reload(Player player) 
    {
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
    	close(player);
    	open(player, gui);
//    	Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->open(player, gui), 1);
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
            	ObsGui gui = openGuis.remove(e.getPlayer().getUniqueId());
                if (gui != null) gui.close(e);
            }
        }, plugin);
    }
}
