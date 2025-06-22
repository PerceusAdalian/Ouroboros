package com.ouroboros.accounts;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.enums.StatType;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.XpUtils;

public class ExpHandler implements Listener
{ 
	public static void register(JavaPlugin plugin) 
	{
		
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void craftingXPHandler(CraftItemEvent e) 
			{
				if (!(e.getWhoClicked() instanceof Player p)) return;
				
				ItemStack result = e.getRecipe().getResult(); // Get the crafted item
				int eResultCount = result.getAmount(); // Event result amount, not the itemstack count.
				int craftedAmount = 1;
				
				ItemStack cursor = p.getItemOnCursor();
				if (cursor != null && cursor.getType() != Material.AIR) 
				{
			        if (!cursor.isSimilar(result)) return; // Different items: can't stack

			        int totalAmount = cursor.getAmount() + result.getAmount();
			        if (totalAmount > cursor.getMaxStackSize()) return; // Would exceed max stack
			    }
				
				if (e.isShiftClick()) 
				{
			        CraftingInventory inv = (CraftingInventory) e.getInventory();
			        ItemStack[] matrix = inv.getMatrix();

			        int possibleCrafts = Integer.MAX_VALUE; // Default possible maxCrafts, even when stack size max is 1;16;32;64.
			        for (ItemStack ingredient : matrix) // Gets every ingredient in the crafting matrix
			        {
			            if (ingredient == null || ingredient.getType() == Material.AIR) continue; // If there's an empty slot, continue iterating
			            possibleCrafts = Math.min(possibleCrafts, ingredient.getAmount()); // Reassign to the actual number of crafted items
			        }

			        craftedAmount = eResultCount * possibleCrafts; // Reassign to the total items crafted
			    } 
				else // Normal click handling (right/left+!shift)
				{
			        if (e.getCurrentItem() != null) // Gets the actual singular item crafted
			        {
			            craftedAmount = e.getCurrentItem().getAmount();
			        }
			        else // Handles nullpointer
			        {
			            craftedAmount = eResultCount; // Fallback crafted amount IS the event result count
			        }
			    }
				
				EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 1);
				PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+XpUtils.getXp(result)+" &b&o"+PrintUtils.printStatType(StatType.CRAFTING));
				PlayerData.addXP(p, StatType.CRAFTING, XpUtils.getXp(result) * craftedAmount);
			}
			
			//Implement Plz ;/
			@EventHandler
			public void alchemyXPHandler(BrewEvent e) 
			{
				
			}
			
			@EventHandler
			public void travelXPHandler(PlayerMoveEvent e) 
			{
				Player p = e.getPlayer();
			    Location from = e.getFrom();
			    Location to = e.getTo();

			    if (to == null || from.getWorld() != to.getWorld()) return;

			    double distance = from.distance(to);
			    
			    if (distance < 0.01) return; // Player is likely rotating

			    if (distance >= 1.0) // Awards xp per full traveled block
			    {
			        int xp = (int) Math.floor(distance);
			        PlayerData.addXP(p, StatType.TRAVEL, xp);
			    }
			}
			
			@EventHandler
			public void woodCuttingMiningXPHandler(BlockBreakEvent e) 
			{
				Player p = e.getPlayer();
				
				final Set<Material> WoodCuttingValidBlocks = EnumSet.of(
					Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.JUNGLE_LOG, 
					Material.DARK_OAK_LOG, Material.ACACIA_LOG,Material.MANGROVE_LOG, Material.CHERRY_LOG);	
				
				final Set<Material> miningValidBlocks = EnumSet.of(
					Material.COAL_ORE,Material.COPPER_ORE,Material.DEEPSLATE_COAL_ORE,Material.DEEPSLATE_COPPER_ORE,
					Material.DEEPSLATE_DIAMOND_ORE,Material.DEEPSLATE_EMERALD_ORE,Material.DEEPSLATE_GOLD_ORE,
					Material.DEEPSLATE_IRON_ORE,Material.DEEPSLATE_LAPIS_ORE,Material.DEEPSLATE_REDSTONE_ORE,
					Material.DIAMOND_ORE,Material.EMERALD_ORE,Material.GOLD_ORE,Material.IRON_ORE,Material.LAPIS_ORE,
					Material.NETHER_GOLD_ORE,Material.NETHER_QUARTZ_ORE,Material.REDSTONE_ORE,Material.ANCIENT_DEBRIS);
				
				if (WoodCuttingValidBlocks.contains(e.getBlock().getType())) 
				{
					PlayerData.addXP(p, StatType.WOODCUTTING, XpUtils.getXp(e.getBlock().getType()));
			    }
				
				if (miningValidBlocks.contains(e.getBlock().getType())) 
				{
					PlayerData.addXP(p, StatType.MINING, XpUtils.getXp(e.getBlock().getType()));					
				}
			}		
			
			@EventHandler
			public void fishingXPHandler(PlayerFishEvent e) 
			{
				Player p = e.getPlayer();
				
				Entity entity = e.getCaught();
				if (entity == null) return;
				
				int xp = e.getCaught().getName().length();
				PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+xp+" &b&o"+PrintUtils.printStatType(StatType.FISHING));
				EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 1);
				PlayerData.addXP(p, StatType.FISHING, xp);
			}
			
			@EventHandler
			public void combatXPHandler(EntityDamageByEntityEvent e) 
			{
				if (!(e.getEntity() instanceof LivingEntity target)) return;

			    Player p = null;
			    StatType sType = null;
			    
			    if (e.getDamager() instanceof Player player) 
			    {
			        p = player;
			        sType = StatType.MELEE;
			    }
			    else if (e.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player shooter) 
			    {
			        p = shooter;
			        sType = StatType.RANGED;
			    }
			    else if (e.getDamager() instanceof ThrownPotion potion && potion.getShooter() instanceof Player thrower) 
			    {
			        p = thrower;
			        sType = StatType.MAGIC;
			    }

			    if (p == null || sType == null) return;

			    // Only award XP if the attack will kill the entity
			    if (!target.isDead() && target.getHealth() - e.getFinalDamage() <= 0.0) 
			    {
			    	PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+XpUtils.getXp(target)+" &b&o"+PrintUtils.printStatType(sType));
			    	EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1, 1);
					PlayerData.addXP(p, sType, XpUtils.getXp(target));
			    }
			}
		}, plugin);
	}
}
