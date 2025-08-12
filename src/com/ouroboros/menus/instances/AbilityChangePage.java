package com.ouroboros.menus.instances;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.PrintUtils;

public class AbilityChangePage extends AbstractOBSGui
{

	public AbilityChangePage(Player player) 
	{
		super(player, "Ability Confirmation", 27, Set.of(10,13,16));
	}

	@Override
	protected void build() 
	{
		
		PlayerData data = PlayerData.getPlayer(player.getUniqueId());
		AbstractOBSAbility ability = AbilityConfirmationPage.abilityChangeMap.get(player.getUniqueId());
		
		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE).setName("&a&lConfirm Change &r&f& &e&oActivate").setLore("&r&f&oYou already have an active combat ability: &b&l"+
		data.getActiveCombatAbility().getDisplayName()+"&r&f&o.","&r&f&oChange active combat ability to: &b&l"+ability.getDisplayName()+"&r&f&o?").place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.MASTER, 1, 1);
			PrintUtils.OBSFormatPrint(p, "Changed active combat ability: &b&o"+data.getActiveCombatAbility().getDisplayName()+" &r&e&l-> &r&b&o"+ability.getDisplayName());
			data.getAbility(data.getActiveCombatAbility()).setActive(false);
			data.setActiveCombatAbility(ability);
			data.getAbility(ability).setActive(true);
			data.save();
			AbilityConfirmationPage.abilityChangeMap.remove(p.getUniqueId());
			GuiHandler.close(p);
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("&e&lGo Back").setLore("Click to return to 'Ability Main Page'").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new AbilityMainPage(p));
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
