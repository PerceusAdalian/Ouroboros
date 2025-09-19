package com.ouroboros.menus.instances;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ObsAbilityType;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.PrintUtils;

public class AbilityConfirmationPage extends AbstractOBSGui
{

	public AbilityConfirmationPage(Player player) 
	{
		super(player, "Ability Confirmation", 27, Set.of(10,13,16));
	}

	public static Map<UUID, AbstractOBSAbility> abilityChangeMap = new HashMap<>();

	@Override
	protected void build() 
	{
		
		String confirmActivate = "&a&lConfirm Activation Toggle&r&f?";
		String confirmRegister = "&a&lConfirm Registration&r&f?";
		boolean isRegistering = AbilityMainPage.confirmRegister.get(player);
		AbstractOBSAbility ability = AbilityMainPage.abilityConfirmMap.get(player);
		
		GuiButton.button(Material.GREEN_STAINED_GLASS_PANE).setName(isRegistering ? confirmRegister : confirmActivate).setLore("Click to Confirm Action").place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.MASTER);
			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
			
			if (isRegistering) 
			{
				int ap = data.getAbilityPoints();
				ap -= ability.getAPCost();
				data.setAbilityPoints(ap);
				data.getAbility(ability).setRegistered(true);
				data.getAbility(ability).setActive(false);
				data.save();
				PrintUtils.OBSFormatPrint(p, "&r&fRegistered Ability: &b&o"+ability.getDisplayName()+"&r&f! &7| &c&oDeducted &r&f"+ability.getAPCost()+"&6AP");
				GuiHandler.changeMenu(p, new AbilityMainPage(p));
			}
			else 
			{
				if (ability.getAbilityType().equals(ObsAbilityType.COMBAT)) 
				{
				    if (data.getActiveCombatAbility() != null) 
				    {
				        var combatAbility = data.getAbility(data.getActiveCombatAbility());
				        if (combatAbility != null && combatAbility.getInstance() != null) 
				        {
				            String combatAbilityName = combatAbility.getInstance().getInternalName();
				            String abilityName = data.getAbility(ability).getInstance().getInternalName();

				            if (!combatAbilityName.equals(abilityName) && !data.getAbility(ability).isActive()) 
				            {
				                abilityChangeMap.put(p.getUniqueId(), ability);
				                GuiHandler.changeMenu(p, new AbilityChangePage(p));
				                return;
				            }
				            if (combatAbilityName.equals(abilityName)) 
				            {
				                PlayerData.deactivateAbility(p, ability);
				                GuiHandler.changeMenu(p, new AbilityMainPage(p));
				                return;
				            }
				        }
				    }

				    PlayerData.activateAbility(p, ability);
				    GuiHandler.changeMenu(p, new AbilityMainPage(p));
				}
				else if (data.getAbility(ability).isActive()) 
				{
					PlayerData.deactivateAbility(p, ability);
					GuiHandler.changeMenu(p, new AbilityMainPage(p));
				}
				else
				{
					PlayerData.activateAbility(p, ability);
					GuiHandler.changeMenu(p, new AbilityMainPage(p));
				}
			}
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to Ability Main Page").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new AbilityMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
	}

}
