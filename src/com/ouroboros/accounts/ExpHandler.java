package com.ouroboros.accounts;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Container;
import org.bukkit.block.data.Ageable;
import org.bukkit.craftbukkit.block.CraftChest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BrewingStartEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.loot.Lootable;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionType;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;

public class ExpHandler implements Listener
{ 
	public static void register(JavaPlugin plugin) 
	{
		
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void onCraft(CraftItemEvent e) 
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

				if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
					EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
				if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
					PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+XpUtils.getXp(result)*craftedAmount+" &b&o"+PrintUtils.printStatType(StatType.CRAFTING));
				PlayerData.addXP(p, StatType.CRAFTING, XpUtils.getXp(result)*craftedAmount);
			}
			
			public static final Map<Location, UUID> mapOfBrewers = new HashMap<>();
			@EventHandler
			public void onBrewingStart(BrewingStartEvent e) 
			{
				if (!(e.getBlock().getState() instanceof BrewingStand stand)) return;
				List<Player> nearby = stand.getWorld().getPlayers().stream()
						.filter(p -> p.getLocation().distance(stand.getLocation()) < 4)
						.collect(Collectors.toList());
				if (!nearby.isEmpty())
				{
					mapOfBrewers.put(stand.getLocation(), nearby.get(0).getUniqueId());
				}
			}
			
			@EventHandler
			public void onBrewingFinish(BrewEvent e) 
			{
				Location loc = e.getBlock().getLocation();
				UUID brewerId = mapOfBrewers.get(loc);
				Player p = Bukkit.getPlayer(brewerId);
				
				if (brewerId == null || p == null) return;
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, r->
				{					
					int xp = 10;
					for (int i = 0; i < 3; i++) 
					{
						ItemStack potion = e.getContents().getItem(i);
						if (potion == null || potion.getType() != Material.POTION) continue;
						
						PotionMeta meta = (PotionMeta) potion.getItemMeta();
						if (meta == null || !meta.hasBasePotionType()) continue;
						
						PotionType pType = meta.getBasePotionType();
						xp += XpUtils.getXp(pType);
					}
					if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
						EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
					if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
						PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+xp+" &b&o"+PrintUtils.printStatType(StatType.ALCHEMY));
					PlayerData.addXP(p, StatType.ALCHEMY, xp);
				}, 5);
			}
			
			@EventHandler
			public void onEnchant(EnchantItemEvent e) 
			{
				Player p = e.getEnchanter();
			    Map<Enchantment, Integer> enchantments = e.getEnchantsToAdd();
			    int xp = 0;
			    int stackXp = XpUtils.getXp(e.getItem());
			    
			    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) 
			    {
			        Enchantment enchant = entry.getKey();
			        
			        int enchantXp = XpUtils.getXp(enchant);

			        xp += enchantXp * entry.getValue() + stackXp;
			    }

				if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
					EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
				if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
					PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+xp+" &b&o"+PrintUtils.printStatType(StatType.ENCHANTING));
				PlayerData.addXP(p, StatType.ENCHANTING, xp);
			}
			
			@EventHandler
			public void onMove(PlayerMoveEvent e) 
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
			public void onCollect(BlockBreakEvent e) 
			{
				if (!(e.getPlayer() instanceof Player p)) return;

				Block block = e.getBlock();
				
				final Set<Material> validWoods = EnumSet.of(
					Material.OAK_LOG, Material.BIRCH_LOG, Material.SPRUCE_LOG, Material.JUNGLE_LOG, 
					Material.DARK_OAK_LOG, Material.ACACIA_LOG,Material.MANGROVE_LOG, Material.CHERRY_LOG);	
				
				final Set<Material> validOres = EnumSet.of(
					Material.COAL_ORE,Material.COPPER_ORE,Material.DEEPSLATE_COAL_ORE,Material.DEEPSLATE_COPPER_ORE,
					Material.DEEPSLATE_DIAMOND_ORE,Material.DEEPSLATE_EMERALD_ORE,Material.DEEPSLATE_GOLD_ORE,
					Material.DEEPSLATE_IRON_ORE,Material.DEEPSLATE_LAPIS_ORE,Material.DEEPSLATE_REDSTONE_ORE,
					Material.DIAMOND_ORE,Material.EMERALD_ORE,Material.GOLD_ORE,Material.IRON_ORE,Material.LAPIS_ORE,
					Material.NETHER_GOLD_ORE,Material.NETHER_QUARTZ_ORE,Material.REDSTONE_ORE,Material.ANCIENT_DEBRIS);
				
				final Set<Material> validCrops = EnumSet.of(
						Material.WHEAT,Material.CARROTS,Material.POTATOES,Material.BEETROOTS,
						Material.SWEET_BERRY_BUSH,Material.CAVE_VINES,Material.CAVE_VINES_PLANT,
						Material.TWISTING_VINES,Material.WEEPING_VINES,Material.NETHER_WART,Material.COCOA,
						Material.GLOW_BERRIES,Material.CHORUS_FLOWER,Material.CHORUS_PLANT,
						Material.BAMBOO,Material.CACTUS,Material.SUGAR_CANE,Material.PUMPKIN,Material.MELON,
						Material.ATTACHED_MELON_STEM,Material.MELON_STEM,Material.TORCHFLOWER_CROP,Material.TORCHFLOWER,
						Material.PITCHER_CROP,Material.PITCHER_PLANT,Material.RED_MUSHROOM,Material.BROWN_MUSHROOM);

				if (validWoods.contains(block.getType())) 
				{
					int xp = XpUtils.getXp(block.getType());
					if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
						EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
					if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
						PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f" + xp + " &b&o" + PrintUtils.printStatType(StatType.WOODCUTTING));
			    	PlayerData.addXP(p, StatType.WOODCUTTING, xp);	
			    }
				
				if (validOres.contains(block.getType())) 
				{
					int xp = XpUtils.getXp(block.getType());
					if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
						EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
					if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
						PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f" + xp + " &b&o" + PrintUtils.printStatType(StatType.MINING));
			    	PlayerData.addXP(p, StatType.MINING, xp);					
				}
				
			    if (validCrops.contains(block.getType())) 
			    {
			    	if (block.getBlockData() instanceof Ageable harvestable) 
			    	{
			    		if (harvestable.getAge() != harvestable.getMaximumAge()) return;
			    	}
			    	
			    	int xp = XpUtils.getXp(block);
					if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
						EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
					if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
						PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f" + xp + " &b&o" + PrintUtils.printStatType(StatType.FARMING));
			    	PlayerData.addXP(p, StatType.FARMING, xp);			    	
			    }
			}		
			
			@EventHandler
			public void onCatch(PlayerFishEvent e) 
			{
				Player p = e.getPlayer();
				
				Entity entity = e.getCaught();
				if (entity == null) return;
				
				int xp = e.getCaught().getName().length();
				if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
					EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
				if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
					PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+xp+" &b&o"+PrintUtils.printStatType(StatType.FISHING));
				PlayerData.addXP(p, StatType.FISHING, xp);
			}
			
			@EventHandler
			public void onCropHarvest(PlayerInteractEvent e) 
			{
				Block block = e.getClickedBlock();
				Player p = e.getPlayer();
				
				if (block == null || block.getType().equals(Material.AIR)) return;
				
				if (PlayerActions.leftClickBlock(e)) 
				{
					if (block.getType() == Material.SUGAR_CANE) 
					{
						int xp = XpUtils.getXp(block.getType());
						if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
							EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
						if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
							PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+xp+" &b&o"+PrintUtils.printStatType(StatType.FARMING));
					    PlayerData.addXP(p, StatType.FARMING, xp);
					}
				}
				
				if (PlayerActions.rightClickBlock(e) && e.getClickedBlock().getBlockData() instanceof Ageable crop) 
				{
					if (block.getType() == Material.SWEET_BERRY_BUSH && crop.getAge() == crop.getMaximumAge()) 
					{
						int xp = XpUtils.getXp(block.getType());
						if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
							EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
						if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
							PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+xp+" &b&o"+PrintUtils.printStatType(StatType.FARMING));
					    PlayerData.addXP(p, StatType.FARMING, xp);
					}
				}
			}
			
			@EventHandler
			public static void onBrush(EntityChangeBlockEvent e)
			{
				if (!(e.getEntity() instanceof Player p)) return;
				
				Block b = e.getBlock();
				Material bType = b.getType();
			    if (bType != Material.SUSPICIOUS_SAND && bType != Material.SUSPICIOUS_GRAVEL) return;
			    
			    ItemStack item = p.getInventory().getItemInMainHand();
			    if (item == null || item.getType() != Material.BRUSH) return;
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					Material newType = b.getBlockData().getMaterial();
					boolean finished = false;
					
					if (bType == Material.SUSPICIOUS_GRAVEL && newType == Material.GRAVEL)
						finished = true;
					else if (bType == Material.SUSPICIOUS_SAND && newType == Material.SAND)
						finished = true;
					if (finished)
					{
						int xp = XpUtils.getXp(b.getBiome());
						
						if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
							EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
						if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
							PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+xp+" &b&o"+PrintUtils.printStatType(StatType.DISCOVERY));
						
						PlayerData.addXP(p, StatType.DISCOVERY, xp);
					}
				}, 1);
			}
			
			private static final NamespacedKey checkChestID = new NamespacedKey(Ouroboros.instance, "chest_key");
			//For discovery/loot generation of chests found, add exp to the Discovery stat per item generated.
			@EventHandler
			public void onDiscoverLoot(PlayerInteractEvent e)
			{
				Player p = e.getPlayer();
				Block block = e.getClickedBlock();
				if (!PlayerActions.rightClickBlock(e)) return;
				if (block == null || block.getType() == Material.AIR) return;
				if (!(block.getState() instanceof Container container)) return;
				if (!(container instanceof CraftChest)) return;
				
				PersistentDataContainer pdc = container.getPersistentDataContainer();
				if (pdc.has(checkChestID, PersistentDataType.BYTE)) return;
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					int count = 0;
					if (container instanceof Lootable lootable && lootable.getLootTable() != null)	
						for (ItemStack item : container.getInventory().getContents())
							if (item != null && item.getType() != Material.AIR)
								count ++;
					int xp = count * 10;
					
					if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
						EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
					if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
						PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+xp+" &b&o"+PrintUtils.printStatType(StatType.DISCOVERY));
					
					PlayerData.addXP(p, StatType.DISCOVERY, xp);
					
					if (!Ouroboros.debug) 
					{
						pdc.set(checkChestID, PersistentDataType.BYTE, (byte) 1);
						container.update();
					}
					
				}, 1L);
			}

			@EventHandler
			public void onKill(EntityDamageByEntityEvent e) 
			{
				if (!(e.getEntity() instanceof LivingEntity target)) return;

			    Player p = null;
			    StatType sType = null;
			    
			    MobData data = MobData.getMob(target.getUniqueId());
			    if (data == null) return;
	
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
			    if (!data.isDead() && data.getHp(false) - e.getFinalDamage() <= 0.0) 
			    {
					if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
						EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
					if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification())
						PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+XpUtils.getXp(target)+" &b&o"+PrintUtils.printStatType(sType));
					PlayerData.addXP(p, sType, XpUtils.getXp(target));
			    }
			}
		}, plugin);
	}
}
