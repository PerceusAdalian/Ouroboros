package com.ouroboros.menus.instances.abilities;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.ObsMainMenu;
import com.ouroboros.utils.EntityEffects;

public class AbilityMainPage extends AbstractOBSGui
{

	public AbilityMainPage(Player player) 
	{
		super(player, "Abilities", 54, Set.of(20,12,21,14,23,32,24,30,37,43));
	}

	public static Map<Player, AbstractOBSAbility> abilityConfirmMap = new HashMap<>();
	public static Map<Player, Boolean> confirmRegister = new HashMap<>();
	
	@Override
	protected void build() 
	{
		
		GuiButton.button(Material.FIRE_CHARGE).setName("&c&lInferno&r&e Abilities").setLore("Click to navigate to all &c&lInferno&r&e abilities.").place(this, 20, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_FIRECHARGE_USE, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new InfernoAbilitiesMenu(p));
		});

		GuiButton.button(Material.SNOWBALL).setName("&b&lGlacio&r&e Abilities").setLore("Click to navigate to all &b&lGlacio&r&e abilities.").place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_BUCKET_EMPTY_POWDER_SNOW, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new GlacioAbilitiesMenu(p));
		});

		GuiButton.button(Material.BRICK).setName("&6&lGeo&r&e Abilities").setLore("Click to navigate to all &6&lGeo&r&e abilities.").place(this, 21, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_NETHER_BRICKS_BREAK, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new GeoAbilitiesMenu(p));
		});

		GuiButton.button(Material.WIND_CHARGE).setName("&d&lAero&r&e Abilities").setLore("Click to navigate to all &d&lAero&r&e abilities.").place(this, 30, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new AeroAbilitiesMenu(p));
		});

		GuiButton.button(Material.NETHER_STAR).setName("&e&lCelestio&r&e Abilities").setLore("Click to navigate to all &e&lCelestio&r&e abilities.").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new CelestioAbilitiesMenu(p));
		});

		GuiButton.button(Material.ECHO_SHARD).setName("&3&lCosmo&r&e Abilities").setLore("Click to navigate to all &3&lCosmo&r&e abilities.").place(this, 23, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ENTITY_WARDEN_NEARBY_CLOSER, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new CosmoAbilitiesMenu(p));
		});

		GuiButton.button(Material.WITHER_ROSE).setName("&4&lMortio&r&e Abilities").setLore("Click to navigate to all &4&lMortio&r&e abilities.").place(this, 32, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ENTITY_WITHER_AMBIENT, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new MortioAbilitiesMenu(p));
		});
		
		GuiButton.button(Material.WRITABLE_BOOK).setName("&fPerks &r&fand &bUtility &aAbilities").setLore("Click to navigate to all &fPerks and &bUtility &eabilities.").place(this, 24, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new PerksAndUtilitiesMenu(p));
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Obs Main Page").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ObsMainMenu(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
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
	public static void placeAbilityButton(Player player, AbstractOBSAbility ability, int slot, AbstractOBSGui gui) 
	{
	    ItemStack stack = ability.toIcon(player);

	    GuiButton.button(stack.getType())
	        .setName(stack.getItemMeta().getDisplayName())
	        .setLore(stack.getItemMeta().getLore())
	        .place(gui, slot, e -> 
	        {
	            Player p = (Player) e.getWhoClicked();
	            if (PlayerData.canRegister(p.getUniqueId(), ability)) 
	    		{
	    	        abilityConfirmMap.put(p, ability.getInstance());
	    	        confirmRegister.put(p, true);
	    	        GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
	    	    }
	    	    else if (PlayerData.getPlayer(p.getUniqueId()).getAbility(ability).isRegistered()) 
	    	    {
	    	        abilityConfirmMap.put(p, ability.getInstance());
	    	        confirmRegister.put(p, false);
	    	        GuiHandler.changeMenu(p, new AbilityConfirmationPage(p));
	    	    }
	    	    else 
	    	    {
	    	        e.setCancelled(true);
	    	        p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
	    	    }
	        });
	}

}

