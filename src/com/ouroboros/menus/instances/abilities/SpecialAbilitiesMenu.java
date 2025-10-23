package com.ouroboros.menus.instances.abilities;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.abilities.instances.special.Prometheus;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;

public class SpecialAbilitiesMenu extends AbstractOBSGui
{

	public SpecialAbilitiesMenu(Player player) 
	{
		super(player, "Special Abilities", 54, Set.of(10,37,43));
	}

	@Override
	protected void build() 
	{
		GuiButton.placeAbilityButton(player, new Prometheus(), 10, this);
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Ability Main Page").place(this, 37, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.changeMenu(p, new AbilityMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 43, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			GuiHandler.close(p);
		});
		
		paint();
	}

}
