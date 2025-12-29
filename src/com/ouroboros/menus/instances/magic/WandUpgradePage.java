package com.ouroboros.menus.instances.magic;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lol.wand.Wand;
import com.lol.wand.instances.Wand_3;
import com.lol.wand.instances.Wand_2;
import com.lol.wand.instances.ArchmageStaff;
import com.lol.wand.instances.LuminaCatalyst;
import com.lol.wand.instances.Wand_4;
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
			case 1 -> upgradeTo = new Wand_2();
			case 2 -> upgradeTo = new Wand_3();
			case 3 -> upgradeTo = new Wand_4();
			case 4 -> upgradeTo = new ArchmageStaff();
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
				 "&e&lLuminite Cost&r&f: "+luminiteCostManaUpgrade+"&b۞&7/"+PlayerData.getPlayer(player.getUniqueId()).getLuminite()+"۞")
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
				PrintUtils.OBSFormatPrint(p, "Upgrade Success! Wand Mana has been upgraded: &7"+currentMaxMana+" &e&l->&9&l "+currentWand.getMaxMana());
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
		.setName(currentWand.getRarity() == Rarity.SEVEN ? "&cUnable to Upgrade Wand Tier" : "&ePerform Echoic Liberation")
		.setLore(currentWand.getRarity() == Rarity.SEVEN ? null : "&r&fClick to advance your wand to the next rarity and casting tier.",
				"&r&fThis process is &cirreversible&f: all spells are removed,",
				"&r&fand spell slots and mana reset to the new tier’s default.",
				"&r&fUpon &eResonance Liberation&f, new spells become available.",
				"&r&fUnlocking a tier for the first time grants access to",
				"&r&f&eEchoic Recollection&f, allowing you to choose a new spell of that rarity.",
				"&e&lLuminite Cost&r&f: 100&b۞&7/"+currentLuminite+"۞")
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
			    p.getInventory().addItem(currentWand.getAsItemStack());
			    CollectWandData.wandCollector.remove(p.getUniqueId());
			    CollectWandData.pageController.remove(p.getUniqueId());
			    GuiHandler.close(p);
			    return;
			}
			PlayerData.subtractLuminite(p, 100);
			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
			boolean isNewTier = data.getMagicProficiency() < upgradeTo.getRarity().getRarity();

			if (isNewTier) 
			{
			    PrintUtils.OBSFormatPrint(p, "&e&oResonance Liberation Success! \n" + 
			        "&r&fYou can now access a new tier of magic and wands: " + PrintUtils.rarityToString(currentWand.getRarity()) +" &e&l->&r&f "+ PrintUtils.rarityToString(upgradeTo.getRarity()));
			    data.setMagicProficiency(upgradeTo.getRarity().getRarity());
			} 
			else 
			{
			    PrintUtils.OBSFormatPrint(p, "&e&oResonance Liberation Success!");
			}

			EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.MASTER);
			p.getInventory().addItem(upgradeTo.getAsItemStack());
			CollectWandData.wandCollector.remove(p.getUniqueId());
		    CollectWandData.pageController.remove(p.getUniqueId());
		    GuiHandler.close(p);
		});
		
		boolean fullSlots = currentWand.getCurrentMaxSpellSlots() == currentWand.getAbsoluteMaxSpellSlots();
		/**
		 * TODO: make this work.. it doesn't.. also, try and figure out a means to get the wand to render its lore properly when you craft it and while empty..
		 * 		-- Create a means to remove spells from a wand
		 *  	-- Fix the cooldown to accept spell swapping even while a spell has the wand on cooldown..
		 */
		GuiButton.button(fullSlots ? Material.BARRIER : Material.HEART_OF_THE_SEA)
		.setName(fullSlots ? "&cUnable to Upgrade Spell Slots" : "&eIncrease &b&lSpell Slot&r&f Capacity")
		.setLore(fullSlots ? null : "&r&fClick to upgrade your current maximum spell slots for this wand.",
				"&r&fPlease note this action removes all spells equipped,",
				"&r&fand cannot be undone. All wands may have a max of 10 slots.",
				"&r&fYour wand's current max spell slots: &b&l"+currentWand.getCurrentMaxSpellSlots()+"&7/10",
				"&e&lLuminite Cost&r&f: 25&b۞&7/"+currentLuminite+"۞")
		.place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			if (fullSlots)
			{
				EntityEffects.playSound(p, Sound.BLOCK_CHEST_LOCKED, SoundCategory.MASTER);
			    e.setCancelled(true);
			    return;
			}
			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
			if (data.getLuminite() < 25)
			{
				PrintUtils.OBSFormatError(p, "Insufficient Luminite to Upgrade Spell Slots. Please try again..");
			    EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER);
			    p.getInventory().addItem(currentWand.getAsItemStack());
			    CollectWandData.wandCollector.remove(p.getUniqueId());
			    CollectWandData.pageController.remove(p.getUniqueId());
			    GuiHandler.close(p);
			    return;
			}
			int currentSpellSlots = currentWand.getCurrentMaxSpellSlots();
			currentWand.setNewMaxSpellSlots(currentSpellSlots + 1);
			PrintUtils.OBSFormatPrint(p, "Upgrade Success! Spell Slots have been upgraded: &7"+currentSpellSlots+" &e&l->&r&b&l "+currentWand.getCurrentMaxSpellSlots()+"&r&f!");
		    EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER);
		    p.getInventory().addItem(currentWand.getAsItemStack());
		    CollectWandData.wandCollector.remove(p.getUniqueId());
		    CollectWandData.pageController.remove(p.getUniqueId());
		    GuiHandler.close(p);
		    return;
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
            CollectWandData.pageController.remove(p.getUniqueId());
			GuiHandler.close(p);
		});
		
		paint();
	}

}
