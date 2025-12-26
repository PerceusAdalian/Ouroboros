package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lol.wand.Wand;
import com.lol.wand.instances.AdeptWand;
import com.lol.wand.instances.ApprenticeWand;
import com.lol.wand.instances.GrandmasterWand;
import com.lol.wand.instances.LuminaCatalyst;
import com.lol.wand.instances.MasterWand;
import com.lol.wand.instances.TwilightCatalyst;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.Rarity;
import com.ouroboros.menus.AbstractOBSGui;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.PrintUtils;

public class WandUpgradePage extends AbstractOBSGui
{

	public WandUpgradePage(Player player) 
	{
		super(player, "Wand Upgrade", 27, Set.of(10,16));
	}

	@Override
	protected void build() 
	{
		int currentLuminite = PlayerData.getPlayer(player.getUniqueId()).getLuminite();
		Wand currentWand = CollectWandData.wandCollector.get(player.getUniqueId());
		Rarity rarity = currentWand.getRarity();
		Wand upgradeTo = switch(rarity.getRarity())
		{
			case 1 -> upgradeTo = new ApprenticeWand();
			case 2 -> upgradeTo = new AdeptWand();
			case 3 -> upgradeTo = new MasterWand();
			case 4 -> upgradeTo = new GrandmasterWand();
			case 5 -> upgradeTo = new LuminaCatalyst();
			case 6 -> upgradeTo = new TwilightCatalyst();
			default -> throw new IllegalArgumentException("Unexpected value: " + rarity.getRarity());
		};
		
		int baseCost = 10 * rarity.getRarity();
		int manaCostFactor = (int) Math.pow(currentWand.getMaxMana() / currentWand.getAbsoluteMaxMana(), 2);
		int luminiteCostManaUpgrade = (int) Math.ceil(baseCost + (100 - baseCost) * manaCostFactor);
		
		GuiButton.button(currentWand.getMaxMana() == currentWand.getAbsoluteMaxMana() ? Material.BARRIER : Material.LAPIS_LAZULI)
		.setName(currentWand.getMaxMana() == currentWand.getAbsoluteMaxMana() ? "&cUnable to Upgrade Mana" : "&eIncrease &9&lMana Capacity")
		.setLore(currentWand.getMaxMana() == currentWand.getAbsoluteMaxMana() ? null : "&r&fClick to increase your current wand's mana capacity by 100 points.",
				 "&r&fPlease note that your wand cannot exceed a reserve of &n5000 &9Mana&r&f.",
				 "&r&fYour wand's current mana capacity: &9"+currentWand.getMaxMana()+"&7/ 5000",
				 "&r&e&lLuminite Cost&r&f: "+luminiteCostManaUpgrade+"&b۞",
				 "&r&e&lCurrent Luminite&r&f: "+currentLuminite+"&b۞")
		.place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (currentWand.getMaxMana() == currentWand.getAbsoluteMaxMana())
			{
				EntityEffects.playSound(p, Sound.BLOCK_CHEST_LOCKED, SoundCategory.MASTER);
				e.setCancelled(true);
				return;
			}
			
			if (currentLuminite < luminiteCostManaUpgrade)
			{
				PrintUtils.OBSFormatError(p, "Insufficient Luminite to Upgrade Wand Mana. Please try again..");
				EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER);
				ItemStack returnedWand = currentWand.getAsItemStack();
				p.getInventory().addItem(returnedWand);
				CollectWandData.wandCollector.remove(p.getUniqueId());
	            CollectWandData.pageController.remove(p.getUniqueId());
	            GuiHandler.close(p);
	            return;
			} 
			else
			{				
				int currentMaxMana = currentWand.getMaxMana();
				currentWand.setNewMaxMana(currentMaxMana + 100);
				EntityEffects.playSound(p, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.AMBIENT);
				PrintUtils.OBSFormatPrint(p, "Wand Mana Upgraded: &7"+currentMaxMana+" &e&l->&9&l "+currentWand.getMaxMana());
				PlayerData.subtractLuminite(p, luminiteCostManaUpgrade);
				ItemStack returnedWand = currentWand.getAsItemStack();
				p.getInventory().addItem(returnedWand);
				CollectWandData.wandCollector.remove(p.getUniqueId());
				CollectWandData.pageController.remove(p.getUniqueId());
				GuiHandler.close(p);
			}
			return;
		});
		
		GuiButton.button(currentWand.getRarity() == Rarity.SEVEN ? Material.BARRIER : Material.END_CRYSTAL)
		.setName(currentWand.getRarity() == Rarity.SEVEN ? "Unable to Upgrade Wand Tier" : "&ePerform Echoic Liberation")
		.setLore(currentWand.getRarity() == Rarity.SEVEN ? null : "&r&fClick to advance your wand to the next rarity and casting tier.",
				"&r&fThis process is &cirreversible&f: all spells are removed,",
				"&r&fand spell slots and mana reset to the new tier’s default.",
				"&r&fUpon &eResonance Liberation&f, new spells become available.",
				"&r&fUnlocking a tier for the first time grants access to",
				"&r&f&eEchoic Recollection&f, allowing you to choose a new spell of that rarity.",
				"&r&eLuminite Cost: 100&b۞",
				"&r&eCurrent Luminite: "+currentLuminite+"&b۞")
		.place(this, 13, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (currentWand.getRarity() == Rarity.SEVEN)
			{
				EntityEffects.playSound(p, Sound.BLOCK_CHEST_LOCKED, SoundCategory.MASTER);
				e.setCancelled(true);
				return;
			}
			
			if (currentLuminite < 100)
			{
				PrintUtils.OBSFormatError(p, "Insufficient Luminite to Upgrade Wand Tier. Please try again..");
				EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER);
				ItemStack returnedWand = currentWand.getAsItemStack();
				p.getInventory().addItem(returnedWand);
				CollectWandData.wandCollector.remove(p.getUniqueId());
	            CollectWandData.pageController.remove(p.getUniqueId());
	            GuiHandler.close(p);
	            return;
			}
			else
			{
				PlayerData data = PlayerData.getPlayer(p.getUniqueId());
				if (data.getMagicProficiency() >= upgradeTo.getRarity().getRarity())
				{
					PrintUtils.OBSFormatPrint(p, "&e&oResonance Liberation Success!");
					EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.MASTER);
					
					// Track two branches: one for updating the proficiency tier, and one to keep it where it is. 
					// Make more spells, and make the Echoic Recollection page for the player to pick preset spells 
					//    based on their wand's new rank (one of each for Inferno, Glacio, Aero, and Geo).
					// Also, hook it up so that the craft wands page now displays the wands equal to their current proficiency rank (equal to the last highest rarity wand they upgraded to)
					// Finally, make spells higher than the rank of wand currently casting from "Fizzle!"
					
				}
				PrintUtils.OBSFormatError(p, "&e&oResonance Liberation Success!", "&r&fYou can now access a new tier of magic and wands: " + PrintUtils.rarityToString(upgradeTo.getRarity()));
				EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.MASTER);
				
			}
		});
		
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to 'Wand Main Page'. Doing so will return your wand.").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER);
			Wand storedWand = CollectWandData.wandCollector.get(p.getUniqueId());
			ItemStack returnedWand = storedWand.getAsItemStack();
			p.getInventory().addItem(returnedWand);
			CollectWandData.wandCollector.remove(p.getUniqueId());
            GuiButton.spellActivateConfirm.remove(p.getUniqueId());
            CollectWandData.pageController.remove(p.getUniqueId());
			GuiHandler.changeMenu(p, new WandMainPage(p));
		});
		
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("Click to exit this menu. Doing so will return your wand.").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER);
			Wand storedWand = CollectWandData.wandCollector.get(p.getUniqueId());
			ItemStack returnedWand = storedWand.getAsItemStack();
			p.getInventory().addItem(returnedWand);
			CollectWandData.wandCollector.remove(p.getUniqueId());
            GuiButton.spellActivateConfirm.remove(p.getUniqueId());
            CollectWandData.pageController.remove(p.getUniqueId());
			GuiHandler.close(p);
		});
		
		paint();
	}

}
