package com.ouroboros.menus.instances.abilities;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.abilities.instances.combat.Flamelash;
import com.ouroboros.abilities.instances.combat.ImbueFire;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;

public class InfernoAbilitiesMenu extends AbstractOBSGui
{

	public InfernoAbilitiesMenu(Player player) 
	{
		super(player, "Inferno Abilities", 54, Set.of(10,11,37,43));
	}

	@Override
	protected void build() 
	{
		AbstractOBSAbility imbuefire = new ImbueFire();
		AbilityMainPage.placeAbilityButton(player, imbuefire, 10, this);
		
		AbstractOBSAbility flamelash = new Flamelash();
		AbilityMainPage.placeAbilityButton(player, flamelash, 11, this);
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Ability Main Page").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new AbilityMainPage(p));
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
