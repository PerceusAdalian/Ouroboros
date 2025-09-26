package com.ouroboros.menus.instances.abilities;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsMainMenu;
import com.ouroboros.utils.EntityEffects;

public class AbilityMainPage extends AbstractOBSGui
{

	public AbilityMainPage(Player player) 
	{
		super(player, "Abilities", 54, Set.of(20,12,22,21,14,23,32,24,30,37,43));
	}
	
	@Override
	protected void build() 
	{
		
		GuiButton.button(Material.FIRE_CHARGE).setName("&c&lInferno&r&e Abilities").setLore("Click to navigate to all &c&lInferno&r&e abilities.").place(this, 20, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new InfernoAbilitiesMenu(p));
		});

		GuiButton.button(Material.SNOWBALL).setName("&b&lGlacio&r&e Abilities").setLore("Click to navigate to all &b&lGlacio&r&e abilities.").place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new GlacioAbilitiesMenu(p));
		});

		GuiButton.button(Material.BRICK).setName("&6&lGeo&r&e Abilities").setLore("Click to navigate to all &6&lGeo&r&e abilities.").place(this, 21, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new GeoAbilitiesMenu(p));
		});

		GuiButton.button(Material.WIND_CHARGE).setName("&d&lAero&r&e Abilities").setLore("Click to navigate to all &d&lAero&r&e abilities.").place(this, 30, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new AeroAbilitiesMenu(p));
		});

		GuiButton.button(Material.NETHER_STAR).setName("&e&lCelestio&r&e Abilities").setLore("Click to navigate to all &e&lCelestio&r&e abilities.").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new CelestioAbilitiesMenu(p));
		});

		GuiButton.button(Material.ECHO_SHARD).setName("&3&lCosmo&r&e Abilities").setLore("Click to navigate to all &3&lCosmo&r&e abilities.").place(this, 23, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new CosmoAbilitiesMenu(p));
		});

		GuiButton.button(Material.WITHER_ROSE).setName("&4&lMortio&r&e Abilities").setLore("Click to navigate to all &4&lMortio&r&e abilities.").place(this, 32, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new MortioAbilitiesMenu(p));
		});
		
		GuiButton.button(Material.WRITABLE_BOOK).setName("&fPerks &r&fand &bUtility &aAbilities").setLore("Click to navigate to all &fPerks and &bUtility &eabilities.").place(this, 24, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new PerksAndUtilitiesMenu(p));
		});
		
		GuiButton.button(Material.FLOWER_BANNER_PATTERN).setName("&eSpecial Abilities").setLore("Click to navigate to all &eSpecial Abilities").place(this, 22, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new SpecialAbilitiesMenu(p));
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Obs Main Page").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new ObsMainMenu(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.close(p);
		});
		
		paint();
	}

//	public void renderAbilities(Player player, Set<Integer> whitelistedSlots) 
//	{
//	    Iterator<Integer> validSlots = whitelistedSlots.iterator();
//	    
//	    for (AbstractOBSAbility ability : AbilityRegistry.abilityRegistry.values()) 
//	    {
//	        if (!validSlots.hasNext()) break;
//	        int slot = validSlots.next();
//	        if (slot == 37 || slot == 43) slot = validSlots.next();
//	        placeAbilityButton(player, ability, slot);
//	    }
//	}
//	

}

