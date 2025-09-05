package com.ouroboros.menus.instances;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;

public class CodexElementsPage extends AbstractOBSGui
{

    public CodexElementsPage(Player player) 
    {
        super(player, "Elements Guide", 54, Set.of(4,37,43));
    }

    @Override
    protected void build() 
    {
        GuiButton.button(Material.OAK_SIGN).setName("&7{&e&li&r&7}").setLore(
            "&r&bΩuroboros&f elements have unique properties in combat depending on how you use them.",
        "&r&fEach element has their own signature effects, applications,",
        "&r&fand conditions that will change how you navigate combat scenarios.",
        "&r&fThe relative elements and their signature effects are as follows:",
        "&r&c&lInferno&r&f: The &cFire&f element. &b&oSignature Effect&r&f: &c&oBurn&f",
        "&r&b&lGlacio&r7f: The &bIce&f or &9Water&f element. &b&oSignature Effect&r&f: &b&oChill&f",
        "&r&6&lGeo&r&f: The &6Earth&f or &aNatural&f element. &b&oSignature Effect&r&f: &6&oSanded&f",
        "&r&d&lAero&r&f: The &dAir&f or &dElectricity&f element. &b&oSignature Effect&r&f: &d&oStatic&f",
        "&r&f&lCelestio&r&f: The &eLight&f or &eLife&f element. &b&oSignature Effect&r&f: &e&oExposed&f",
        "&r&4&lMortio&r&f: The &4Dark&f or &4Death&f element. &b&oSignature Effect&r&f: &4&oDoom&f",
        "&r&3&lCosmo&r&f: The &3Space&f or &3Cosmic&f element. &b&oSignature Effect&r&f: &3&oVoided&f","",
        "&r&fBelow are further details on each category.").place(this, 3, e->
        {
	        Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });

        GuiButton.button(Material.OAK_SIGN).setName("&7{&e&li&r&7}").setLore(
            "&r&bΩuroboros&f also features elements that do not have unique elemental properties,",
        "&r&finstead, modify base damage of certain weapon interactions.",
        "&r&f&lBelow&r&f are those elements outlined with how they'll manifest in combat:",
        "&r&f&lMelee Categories:",
        "&r&b>&f Blunt -> Maces "+
        "&r&b>&f Pierce -> Pickaxes and Trident"+
        "&r&b>&f Slash -> Swords and Scythes (hoes)",
        "&r&f&lRanged Categories: ",
        "&r&b>&f Puncture -> Bows/Arrows "+
        "&r&b>&f Corrosive -> Poison pots and other sources",
        "&r&b>&f Arcano -> Generic magical projectiles/spells",
        "&r&f&lCondition Specific",
        "&r&b>&f Combust -> Explosions &7(e.g. &dTNT&7)",
        "&r&b>&f Blast -> {&ePure Dmg Type&f} Explosions &7(e.g. &eAbilities&7)",
        "&r&e&lPure Damage Types&r&f: Special instances related to &eAbilities&f",
        "&r&b>&e&lCrush&r&b >&e&lSever&r&b >&e&lImpale&r&f").place(this, 5, e->
        {
            Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true); 
        });

        //Exits
		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE).setName("&a&lGo Back").setLore("Click to return to 'Codex Main Page'").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new CodexMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to exit").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
    }
    
}
