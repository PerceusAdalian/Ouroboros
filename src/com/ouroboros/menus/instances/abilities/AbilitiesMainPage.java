package com.ouroboros.menus.instances.abilities;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.menus.instances.ObsMainMenu;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class AbilitiesMainPage extends ObsGui
{

	public AbilitiesMainPage(Player player) 
	{
		super(player, "Abilities", 54, Set.of(37,43));
	}
	
	@Override
	protected void build() 
	{
		
		GuiButton.button(Material.NETHERITE_SWORD).setName("&r&c&lSword &r&eAbilities").setLore("Click to view all &c&lSword &r&eAbilities").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new SwordAbilitiesPage(p));
		});
		
		GuiButton.button(Material.NETHERITE_SPEAR).setName("&r&c&lPolearm &r&eAbilities").setLore("Click to view all &c&lPolearm &r&eAbilities").place(this, 19, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new PolearmAbilitiesPage(p));
		});
		
		GuiButton.button(Material.NETHERITE_AXE).setName("&r&c&lHatchet &r&eAbilities").setLore("Click to view all &c&lHatchet &r&eAbilities").place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new HatchetAbilitiesPage(p));
		});
		
		GuiButton.button(Material.NETHERITE_HOE).setName("&r&6&lScythe &r&eAbilities").setLore("Click to view all &6&lScythe &r&eAbilities").place(this, 21, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new ScytheAbilitiesPage(p));
		});
		
		GuiButton.button(Material.NETHERITE_PICKAXE).setName("&r&6&lPickaxe &r&eAbilities").setLore("Click to view all &6&lPickaxe &r&eAbilities").place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new PickaxeAbilitiesPage(p));
		});
		
		GuiButton.button(Material.NETHERITE_SHOVEL).setName("&r&6&lSpade &r&eAbilities").setLore("Click to view all &6&lSpade &r&eAbilities").place(this, 23, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new SpadeAbilitiesPage(p));
		});
		
		GuiButton.button(Material.BOW).setName("&r&d&lBow &r&eAbilities").setLore("Click to view all &d&lBow &r&eAbilities").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new BowAbilitiesPage(p));
		});
		
		GuiButton.button(Material.CROSSBOW).setName("&r&d&lCrossbow &r&eAbilities").setLore("Click to view all &d&lCrossbow &r&eAbilities").place(this, 25, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new CrossbowAbilitiesPage(p));
		});
		
		GuiButton.button(Material.MACE).setName("&r&b&lHammer &r&eAbilities").setLore("Click to view all &b&lHammer &r&eAbilities").place(this, 31, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new HammerAbilitiesPage(p));
		});

		GuiButton.button(Material.TRIDENT).setName("&r&b&lArmament &r&eAbilities").setLore("Click to view all &b&lArmament &r&eAbilities").place(this, 40, e->
		{
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.AMBIENT);
			GuiHandler.changeMenu(p, new ArmamentAbilitiesPage(p));
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
			EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER);
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

