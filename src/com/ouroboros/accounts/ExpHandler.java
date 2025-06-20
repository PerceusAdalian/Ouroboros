package com.ouroboros.accounts;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.ouroboros.enums.StatType;

public class ExpHandler implements Listener
{ 
	public static void register(JavaPlugin plugin) 
	{
		
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void craftingXPHandler(CraftItemEvent e) 
			{
				if (!(e.getWhoClicked() instanceof Player)) return;
				
				Player p = (Player) e.getWhoClicked();
				
				ItemStack craftedItem = e.getRecipe().getResult();
				int craftedAmount = craftedItem.getAmount();
				int xp = craftedItem.getType().toString().length(); 
				
				PlayerData.addXP(p, StatType.CRAFTING, xp * craftedAmount);
			}
			
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
			    
			    if (distance < 0.01) return;

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
					int xp = e.getBlock().getType().toString().length();
					PlayerData.addXP(p, StatType.WOODCUTTING, xp);
			    }
				
				if (miningValidBlocks.contains(e.getBlock().getType())) 
				{
					int xp = e.getBlock().getType().toString().length();
					PlayerData.addXP(p, StatType.MINING, xp);
				}
			}		
			
			@EventHandler
			public void fishingXPHandler(PlayerFishEvent e) 
			{
				Player p = e.getPlayer();
				
				Entity entity = e.getCaught();
				if (entity == null) return;
				
				int xp = e.getCaught().getName().length();
				PlayerData.addXP(p, StatType.FISHING, xp);
			}
			
		}, plugin);
	}
}
