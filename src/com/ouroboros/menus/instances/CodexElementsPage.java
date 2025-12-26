package com.ouroboros.menus.instances;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;

public class CodexElementsPage extends AbstractOBSGui
{

    public CodexElementsPage(Player player) 
    {
        super(player, "Elements Guide", 54, Set.of(13,20,21,22,23,24,31,37,40,43));
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
        "&r&2&lHeresio&r&f: The &2Twilight&f or &2Black Magic&f element. &b&oSignature Effect&r&f: &2&oPenance&f","",
        "&r&fBelow are further details on each category. &e- The Curator").place(this, 3, e->
        {
	        Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_HANGING_SIGN_PLACE, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
        });

        GuiButton.button(Material.OAK_SIGN).setName("&7{&e&li&r&7}").setLore(
            "&r&bΩuroboros&f also features damage types that do not have unique elemental properties,",
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
        "&r&fBelow are further details on each category. &e- The Curator").place(this, 5, e->
        {
            Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.BLOCK_HANGING_SIGN_PLACE, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });

        GuiButton.button(Material.FIRE_CHARGE).setName("&eEchoic Analysis&f: &c&lInferno").setLore(
        		"&r&fThe timeless, all-consuming &cFire&f element,",  
        		"&r&f and known across &eFantasia&f as the most accessible form of magik&f.","",  
        		"&r&fWith a crack of wand or sword, the &c&lInferno&r&f arcana can reduce all it touches to ash.",  
        		"&r&fYet, despite its fame, it is not &o'Infernal'&r&f&l nor &r&f&o'Occultic'&r&f, but a discipline of balance.","",  
        		"&r&fBorn in the dunes of &e&oAigisva'ard&r&f, long before the &e&oLament of &r&7*&e&oAighil&r&f, this art is tied to",  
        		"&r&7**&c&lAgni&r&f, &7**&r&eArchon &fof &cFire&r&f, whose gentlest &7*&fdragon taught &opatience, wisdom, and authority&r&f.")
        .place(this, 20, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ITEM_FIRECHARGE_USE, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });
        
        GuiButton.button(Material.SNOWBALL).setName("&eEchoic Analysis&f: &b&lGlacio").setLore(
        		"&r&fThe elusive, and impenetrable, element of &9Water&f known across &eFantasia&f as",  
        		"&r&fthe hardest to master, yet most abundant form of magik.","",  
        		"&r&fLegends trace it to &e&oNifl'heimr VI&r&f, a pre-lament mining town in the northern parts of &eFantasia Prime&r&f.",
        		"&r&fAncient tomes spoke of &7*&b&oBjorn&r&f, an &bIce Giant&r&f, as the original",
        		"&7*&eEmanator&f seen alongside &7**&b&lGlacio&r &eArchon&f, &7**&b&lHræsvelgr&r&f, a Jötunn.","",  
        		"&r&b&lGlacio &r&fnurtures life as easily as it brings freezing temps,", 
        		"&r&for for drowning ones foes in chilling depths.","",  
        		"&r&fTo wield it is to brave debuffs and devastating magiks most students of",
        		"&r&eFantasia's Academy for the Natural Arts &r&fmust endure a trek of a thousand miles to learn.",  
        		"&r&fScholar freshman normally sign a waver with the first question: &f&oKnow any &r&cFire&f&o magik?")
        .place(this, 21, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ITEM_BUCKET_EMPTY_POWDER_SNOW, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });
        
        GuiButton.button(Material.BRICK).setName("&eEchoic Analysis&f: &6&lGeo").setLore(
                "&r&fThe most versatile school, &6&lGeo&r&f, embodies valor and the strength of &6Earth&f.",
                "&r&fFirst taught in the &e&oHollows of Aig'rsvheild&r&f,", 
                "&r&fit wields nature itself: Plants, stones, and the like.","",
                "&r&fMany botanists of &eFantasia&f begin here, drawn to its forgiving yet powerful nature.",
                "&r&fThe &7**&eArchon&f of &6Earth&f, &7**&6&lNidus&r&f, is known for wisdom and kindness,",
                "&r&fwhile his &7*&eEmanator&f, &7*&6Haephestus&f, stands as a human symbol of leadership and war.","",
                "&r&fRespect the land, for it is our only home;",
                "&r&fdisrespect it, and even &6&lNidus&r&f will not be so merciful.")
        .place(this, 23, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.BLOCK_NETHER_BRICKS_BREAK, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });
        
        GuiButton.button(Material.WIND_CHARGE).setName("&eEchoic Analysis&f: &d&lAero").setLore(
            "&r&fThe most volatile school, &d&lAero&r&f, commands the fury of &dStorms&f.",
            "&r&fIts &7**&eArchon&f, &7**&d&lSeth&r&f, first appeared only centuries ago,",
            "&r&fthough his &eEmanator&f remains unknown.","",
            "&r&fStudents of &d&lAero&r&f master both swift boons and devastating winds.",
            "&r&fMany novices miscast, often blown onto rooftops by failed incantations.",
            "&r&fThis all said, however, those who harness the true power of &dAir&f..",
            "&r&fThey stand among the mightiest in all of &eFantasia&f.")
        .place(this, 24, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_BREEZE_CHARGE, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.NETHER_STAR).setName("&eEchoic Analysis&f: &e&lCelestio").setLore(
            "&r&fThe school of &e&lCelestio&r&f channels the sacred power of &eLight&f.",
            "&r&fIts magic restores vigor, raises the fallen, and fortifies the soul.",
            "&r&fThough often seen as gentle, &eLight&f can unleash devastating force against its foes.","",
            "&r&fOnly &e&lCelestio&r&f's &7*&eEmanator, &7*&eArch Priestess Lumina&r&f,",
            "&r&fhas encountered its hidden &7**&eArchon&f, whose name She refuses to speak.",
            "&r&fTo study &e&lCelestio&r&f under &eLumina&f’s guidance is a rare blessing indeed,",
            "&r&freserved for that of only the worthy and purest of heart.")
        .place(this, 13, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.ECHO_SHARD).setName("&eEchoic Analysis&f: &3&lCosmo").setLore(
            "&r&fAnomalous and vast, &3&lCosmo&r&f channels the weight of the &3Cosmos&f itself.",
            "&r&fOften called the &3Astral&f school, little is truly known of its workings.",
            "&r&fUnlike other &barcana&f, it does not draw on &bAethereal&f energy,",
            "&r&fbut on the very fabric of reality.","",
            "&r&eArch Priestess Lumina&f’s records suggest the &7*&3&lNihility&r&f",
            "&r&fmay as well be both its &7*&eArchon&r&f and &7*&eEmanator&r&f.",
            "&r&fIts magic bends nature, summons storms, and twists time, but at dire cost.",
            "&r&fScholars risk madness in their studies,",
            "&r&ftheir notes devolving into illegible scrawl and occult ritual.","",
            "&r&e&oThe Fantasian Council&r&f heavily cautions against learning it,",
            "&r&fthough not &oexplicity&r&f forbidding texts, with whitelisted tomes featuring",
            "&r&fheaders warning of fates far worse than &4Death&f.")
        .place(this, 22, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_WARDEN_NEARBY_CLOSER, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.WITHER_ROSE).setName("&eEchoic Analysis&f: &4&lMortio").setLore(
        		"&r&4Death&f incarnate, the school of &4&lMortio&r&f embodies the power of &4The Anti-Light&f.",
        		"&r&fIt draws upon the essence of both &eliving&f and",
        		"&r&4undead&f to raise corpses, drain life, and cripple foes.","",
        		"&r&fThe &7**&eArchon&f of &4Death&f, &7**&4&lBelial&r&f, and his &7*&eEmanator, &7*&4General Falric&r&f,",
        		"&r&fmarch with goblin hordes and legions of the undead.","",
        		"&r&fThose who wield &4&lMortio&r&f are rarely seen alive; leaving only left in ruin, ",
        		"&r&fas corpses, or made as broken kinships in their wake.","",
        		"&r&fWhitelisted texts that highlight this school's history and power often",
        		"&r&fnecessitates excerpts at the top which reads clearly in bold: &c&nLearn at Your Own Risk&r&f.")
        .place(this, 31, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_AMBIENT, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.ENDER_EYE).setName("&eEchoic Analysis&f: &2&lHeresio&r&f").setLore(
        		"&r&fTrue to its name, &2&lHeresio&r&f is a blasphemous school of magic.",
        		"&r&fIts origins are unknown, though forbidden texts recently",
        		"&r&fsurfaced among occult circles in &eFantasia Minor&r&f.",
        		"&r&fEyewitness accounts describe &2&lHeresio&r&f spells as",
        		"&r&fancient and powerful, rivaling even &3&lCosmo&r&f magiks. ",
        		"",
        		"Rather than the elements or space-time,",
        		"&2&lHeresio&r&f appears to draw upon forces far less... natural, and far more &2umbral&r&f.",
        		"&r&fResidual &bEther Traces&f of pertinent victims were studied by alchemists in &eG'yr Arbania&r&f,",
        		"&r&fwho traced its most likely origin to &2The Kingdom of Twilight&r&f, a dimension outside this one.",
        		"&r&fThough its nature remains poorly understood, one truth is evident:",
        		"&r&f&oThis 'magik' stands in direct violation of the natural order.&r&f",
        		"",
        		"&r&fDue to its volatility, all circulated knowledge of &2&lHeresio&r&f",
        		"&r&fhas been henceforth collected, and remains strictly confidential.",
        		"&r&fUnsupervised study is often followed by catastrophic &e&oEchoic Anamolies&r&f,",
        		"&r&fresulting in mass casualties of all involved. ",
        		"&r&fCitizens are advised to alert &eHer Majesty&f's guards upon word of unsuspecting casters,",
        		"&r&fmany breathe their last with 'what could go wrong?', when it's already too late.",
        		"&r&fThe verdict has been made abundantly clear by all governing parties: &c&l&nDO NOT ATTEMPT&r&f!")
        .place(this, 40, e->
        {
        	Player p = (Player) e.getWhoClicked();
        	EntityEffects.playSound(p, Sound.ENTITY_ENDERMAN_SCREAM, SoundCategory.MASTER);
        	EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER);
        	e.setCancelled(true);
        });
        
        GuiButton.button(Material.IRON_SWORD).setName("&f&lMelee &r&fDamage Types").setLore(
                "&r&fMelee damage types modify the base damage of certain melee weapon interactions.",
                "&r&fThese types are applied based on the weapon used and the nature of the attack.","",
                "&r&f&lBlunt&r&f: Maces and certain melee attacks.",
                "&r&f&lPierce&r&f: Pickaxes and Tridents.",
                "&r&f&lSlash&r&f: Swords and Scythes.")
        .place(this, 19, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });       
        
        GuiButton.button(Material.BOW).setName("&f&lRanged &r&fDamage Types").setLore(
                "&r&fRanged damage types modify the base damage of certain ranged weapon interactions.",
                "&r&fThese types are applied based on the weapon used and the nature of the attack.","",
                "&r&fPuncture&r&f: Bows and Arrows",
                "&r&fCorrosive&r&f: Poison pots and &osome&r&f thrown items",
                "&r&fArcano&r&f: Generic magical projectiles/spells")
        .place(this, 25, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ENTITY_ARROW_SHOOT, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.HEART_OF_THE_SEA).setName("&a&lCondition Specific&r&f Damage Types").setLore(
                "&r&fCondition specific damage types modify the base damage of certain interactions",
                "&r&fthat meet specific conditions, often related to environmental or situational factors.",
                "&r&fThese types are applied based on the context of the attack rather than the weapon used,",
                "&r&fand typically involve area-of-effect or status-based damage with occasional pure-damage.","",
                "&r&aToxin: &r&fPoisons &7(e.g. &eAbilities&f/Spiders&7)&f: Permanently poison's applicant.",
                "&r&cCombust&r&f: Explosions &7(e.g. &dTNT&7)",
                "&r&eBlast&r&f: &7{&ePure Dmg Type&7} &fExplosions &7(e.g. &eAbilities&7)")
        .place(this, 38, e->
        {
        	Player p = (Player) e.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, SoundCategory.MASTER, 1, 1);
            e.setCancelled(true);
        });        
        
        GuiButton.button(Material.END_CRYSTAL).setName("&e&lAlmighty &r&7(Pure) &r&fDamage Types").setLore(
                "&r&e&lAlmighty &r&7(Pure)&r&f damage types are special instances related to &eAbilities&r&f",
                "&r&fthat do not fall under conventional damage categories and occasionally sourced from &e&lEOL&r&f weapons.",
                "&r&fThese damage types often bypass conventional resistances and defenses,",
                "&r&fmaking them particularly potent in combat scenarios.","",
                "&r&e&lCrush&r&f: A powerful, crushing force. Commonly associated with heavy impacts &7(Maces)",
                "&r&e&lSever&r&f: A sharp, slicing force. Commonly associated with cutting weapons &7(Swords/Axes)",
                "&r&e&lImpale&r&f: A piercing strike that ignores Armor. Commonly associated with pointed weapons &7(Tridents)")
        .place(this, 42, e->
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
