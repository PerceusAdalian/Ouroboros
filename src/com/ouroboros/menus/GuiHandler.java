package com.ouroboros.menus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.joml.Random;

import com.ouroboros.Ouroboros;

public class GuiHandler implements Listener
{
	private static final Map<UUID, ObsGui> openGuis = new HashMap<>();
	private static final Set<UUID> transitioning = new HashSet<>();
	private static final Map<UUID, Long> sessionTimestamps = new HashMap<>();
	
	private static final NamespacedKey NOISE_KEY = new NamespacedKey(Ouroboros.instance, "noise");
	private static final NamespacedKey SESSION_KEY = new NamespacedKey(Ouroboros.instance, "session_open");

	public static void open(Player player, ObsGui gui)
	{
	    long sessionTime = System.currentTimeMillis();
	    
	    openGuis.put(player.getUniqueId(), gui);
	    sessionTimestamps.put(player.getUniqueId(), sessionTime);
	    gui.open();

	    ItemStack[] contents = gui.getInventory().getContents();
	    
	    for (ItemStack stack : contents)
	    {
	        if (stack == null || stack.getType().isAir()) continue;
	        ItemMeta meta = stack.getItemMeta();
	        if (meta == null) continue;

	        PersistentDataContainer pdc = meta.getPersistentDataContainer();
	        pdc.set(NOISE_KEY, PersistentDataType.LONG, Random.newSeed());
	        pdc.set(SESSION_KEY, PersistentDataType.LONG, sessionTime);
	        stack.setItemMeta(meta);
	    }
	    
	    gui.getInventory().setContents(contents);
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

        	    ItemStack clicked = e.getCurrentItem();
        	    if (clicked == null || clicked.getType().isAir()) return;

        	    ItemMeta meta = clicked.getItemMeta();
        	    if (meta == null) return;

        	    PersistentDataContainer pdc = meta.getPersistentDataContainer();
        	    if (!pdc.has(SESSION_KEY, PersistentDataType.LONG)) return;

        	    long itemSession = pdc.get(SESSION_KEY, PersistentDataType.LONG);
        	    long playerSession = sessionTimestamps.getOrDefault(p.getUniqueId(), -1L);

        	    if (itemSession != playerSession) return;

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
