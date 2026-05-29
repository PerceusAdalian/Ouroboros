package com.ouroboros.menus.instances;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;

public class CodexMainPage extends ObsGui
{

    public CodexMainPage(Player player) 
    {
        super(player, "Codex Main Menu", 27, Set.of(10,16));
    }

    @Override
    protected void build() 
    {

        GuiButton.button(Material.PAPER).setName("&e&lElement Categories").setLore("Click to navigate to the &bΩuroboros &eElement &fguide.").place(this, 12, e->
        {
            Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1, 1);
            GuiHandler.changeMenu(p, new CodexElementsPage(p));
        });
        
        GuiButton.button(Material.PAPER).setName("&e&lEntity Categories").setLore("Click to navigate to the &e&oEntity Categories&r&f guide.").place(this, 14, e->
        {
            Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });
        
        //Exits
        GuiButton.placeGoBack(10, this, new ObsMainMenu(player));
		GuiButton.placeExit(16, this);
		
		paint();
    }
    
}
