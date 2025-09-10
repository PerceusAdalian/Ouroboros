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
        super(player, "Elements Guide", 54, Set.of(10,13,19,16,21,22,23,25,39,40,41,37,43));
    }

    @Override
    protected void build() 
    {
        GuiButton.button(Material.OAK_SIGN).setName("&7{&e&li&r&7}").setLore(
            "&r&bΩuroboros&f elements have unique properties in combat depending on how you use them.",
        "&r&fEach element has their own signature effects, applications,",
        "&r&fand conditions that will change how you navigate combat scenarios.",
        "&r&fThe relative elements and their signature effects are as follows:","",
        "&r&c&lInferno&r&f: The &cFire&f element. &b&oSignature Effect&r&f: &c&o&nBurn&f",
        "&r&b&lGlacio&r&f: The &bIce&f or &9Water&f element. &b&oSignature Effect&r&f: &b&o&nChill&f",
        "&r&6&lGeo&r&f: The &6Earth&f or &aNatural&f element. &b&oSignature Effect&r&f: &6&o&nSanded&f",
        "&r&d&lAero&r&f: The &dAir&f or &dElectricity&f element. &b&oSignature Effect&r&f: &d&o&nStatic&f",
        "&r&f&lCelestio&r&f: The &eLight&f or &eLife&f element. &b&oSignature Effect&r&f: &e&o&nExposed&f",
        "&r&4&lMortio&r&f: The &4Dark&f or &4Death&f element. &b&oSignature Effect&r&f: &4&o&nDoom&f",
        "&r&3&lCosmo&r&f: The &3Space&f or &3Cosmic&f element. &b&oSignature Effect&r&f: &3&o&nVoided&f","",
        "&r&fBelow are further details on each category.").place(this, 3, e->
        {
	        Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_HANGING_SIGN_PLACE, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
        });

        GuiButton.button(Material.OAK_SIGN).setName("&7{&e&li&r&7}").setLore(
            "&r&bΩuroboros&f also features elements that do not have unique elemental properties,",
        "&r&finstead, modify base damage of certain weapon interactions.",
        "&r&f&lBelow&r&f are those types outlined and where they'll show up:","",
        "&r&f&lMelee Categories&r&f:",
        "&r&b>&f &lBlunt &r&f-> Maces & certain melee",
        "&r&b>&f &lPierce &r&f-> Pickaxes & Tridents",
        "&r&b>&f &lSlash &r&f-> Swords & Scythes","",
        "&r&f&lRanged Categories&r&f: ",
        "&r&b>&f Puncture &r&f-> Bows & Arrows",
        "&r&b>&a Corrosive &r&f-> Poison pots and thrown items",
        "&r&b>&d&l Arcano &r&f-> Generic magical projectiles/spells","",
        "&r&f&lCondition Specific&r&f:",
        "&r&b>&c Combust &r&f-> Explosions &7(e.g. &dTNT&7)",
        "&r&b>&e&l Blast &r&f-> &7{&ePure Dmg Type&7} &fExplosions &7(e.g. &eAbilities&7)","",
        "&r&e&lAlmighty &7(Pure) &r&fDamage Types&r&f:", "&r&f&oSpecial instances related to &eAbilities&f",
        "&r&b> &e&lCrush","&r&b> &e&lSever", "&r&b> &e&lImpale&r&f","",
        "&r&fBelow are further details on each category.").place(this, 5, e->
        {
            Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.BLOCK_HANGING_SIGN_PLACE, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });

        GuiButton.button(Material.FIRE_CHARGE).setName("&c&lInferno").setLore(
        		"&r&fThe timeless, all-consuming &cFire&f element, and known across &eFantasia&f as the most ",  
        		"&r&fdestructive and accessible form of &dmagik&f.","",  
        		"&r&fWith a crack of wand or sword, the &c&lInferno&r&f arcana can reduce all it touches to ash.",  
        		"&r&fYet, despite its fame, it is not &o'Infernal'&r&f&l nor &r&f&o'Occultic'&r&f, but a discipline of balance.","",  
        		"&r&fBorn in the dunes of &e&oAigisva'ard&r&f, long before the &e&oLament of &r&7*&e&oAighil&r&f, this art is tied to",  
        		"&r&7**&c&lAgni&r&f, &7**&r&eArchon &fof &cFire&r&f, whose gentlest &7*&fdragon taught &opatience, wisdom, and authority&r&f.")
        .place(this, 10, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });
        
        GuiButton.button(Material.SNOWBALL).setName("&b&lGlacio").setLore(
        		"&r&fThe elusive, impenetrable &9Water&f element, and is known across &eFantasia&f as the hardest art to master,",  
        		"&r&fyet the most stable form of &dmagik&f.","",  
        		"&r&fLegends trace it to &e&oNifl’draug&r&f, a pre-lament mining town in the northern parts of &eFantasia Prime&r&f.",
        		"&r&fAncient tomes spoke of &7*&b&oBjorn&r&f, an &bIce Giant&r&f, as the original",
        		"&7*&eemanator&f of the infamous &7**&b&lGlacio&r &eArchon&f, &7**&b&lHræsvelgr&r&f, a Jötunn.","",  
        		"&r&b&lGlacio &r&fnurtures life as easily as it brings death—freezing,", 
        		"&r&ficicle impaling, or drowning ones foes in chilling depths.","",  
        		"&r&fTo wield it is to brave debuffs and devastating magiks most students of",
        		"&r&eFantasia's Academy for the Natural Arts &r&fmust endure a trek of a thousand miles to learn.",  
        		"&r&fSo I bid thee answer: &b&ocan you stand the unyielding cold?")
        .place(this, 19, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ITEM_BUCKET_EMPTY_POWDER_SNOW, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });
        
        GuiButton.button(Material.BRICK).setName("&6&lGeo").setLore("")
        .place(this, 16, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.BLOCK_NETHER_BRICKS_BREAK, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });
        
        GuiButton.button(Material.WIND_CHARGE).setName("&d&lAero").setLore("")
        .place(this, 25, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_BREEZE_CHARGE, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.NETHER_STAR).setName("&f&lCelestio").setLore("")
        .place(this, 39, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.ECHO_SHARD).setName("&3&lCosmo").setLore("")
        .place(this, 40, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_WARDEN_NEARBY_CLOSER, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.WITHER_ROSE).setName("&4&lMortio").setLore("")
        .place(this, 41, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.IRON_SWORD).setName("&f&lMelee &r&fDamage Types").setLore("")
        .place(this, 13, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });       
        
        GuiButton.button(Material.BOW).setName("&f&lRanged &r&fDamage Types").setLore("")
        .place(this, 21, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.HEART_OF_THE_SEA).setName("&a&lCondition Specific&r&f Damage Types").setLore("")
        .place(this, 22, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.END_CRYSTAL).setName("&e&lAlmighty &r&7(Pure) &r&fDamage Types").setLore("")
        .place(this, 23, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        //Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Codex Main Page").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new CodexMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
    }
    
}
