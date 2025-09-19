package com.ouroboros.menus.instances;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;

public class AdminPanelMenu extends AbstractOBSGui
{

    public AdminPanelMenu(Player player) 
    {
        super(player, "Admin Panel", 27, Set.of(10,12,13, 14,16));
    }

    @Override
    protected void build() 
    {
        GuiButton.button(Material.NETHER_STAR).setName("Server Plugin Panel").setLore(
            "&r&fView plugin specific details and backend commands made interact.",
            "&r&fRequires OP, if you're reading this and you're not OP... There's a problem.",
            "&r&f&lIP&r&f: "+Bukkit.getServer().getIp().toString(),
            "&r&d&lAPI VERSION&r&f: "+Bukkit.getVersion().toString(),
            "&r&eLoaded Plugins&r&f: "+Bukkit.getPluginManager().getPlugins().length,
            "&r&fPlugins List: ",Bukkit.getPluginManager().getPlugins().toString())
        .place(this, 13, e->
        {
            Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });

        //Exits
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});

		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
        
		paint();
    }
    
}
