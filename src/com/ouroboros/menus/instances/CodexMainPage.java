package com.ouroboros.menus.instances;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;

public class CodexMainPage extends AbstractOBSGui
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
        
        GuiButton.button(Material.PAPER).setName("&e&lEntity Categories").setLore("Click to navigate to the &e&oEntity Categories&r&f guide.").place(this, 12, e->
        {
            Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });
        
        //Exits
		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE).setName("&a&lGo Back").setLore("Click to return to 'Obs Main Page'").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHISELED_BOOKSHELF_INSERT_ENCHANTED, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ObsMainMenu(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to exit").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
    }
    
}
