package com.ouroboros.menus.instances.protocolecho;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaState;
import com.eol.enums.MateriaType;
import com.eol.materia.Materia;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.Rarity;
import com.ouroboros.enums.StatType;
import com.ouroboros.menus.GuiButton;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.ObsGui;
import com.ouroboros.objects.instances.ScrapMateria;
import com.ouroboros.utils.Chance;
import com.ouroboros.utils.NumberUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class RefinementPage extends ObsGui
{

	public RefinementPage(Player player) 
	{
		super(player, "Protocol α - Refinement", 27, Set.of(10,13,16));
	}

	public static Map<UUID, Map<ItemStack, Integer>> refineryResult = new HashMap<>();
	public static Map<UUID, Integer> refineryXp = new HashMap<>();

	@Override
	protected void build() 
	{
		//Main
		
		GuiButton.button(Material.HEART_OF_THE_SEA).setName("Refinement Capsule").setLore("Place your unrefined materials here.")
		.place(this, 13, e ->
		{
		    Player p = (Player) e.getWhoClicked();
		    ItemStack stack = p.getItemOnCursor();

		    // Guard: nothing on cursor
		    if (stack == null || stack.getType().isAir())
		    {
		        e.setCancelled(true);
		        return;
		    }

		    PersistentDataContainer meta = stack.getItemMeta().getPersistentDataContainer();
		    PlayerData data = PlayerData.getPlayer(p.getUniqueId());
		    int refinementLevel = data.getStat(StatType.REFINEMENT, true);

		    // Already refined: attempt upgrade
		    if (meta.has(Materia.materiaKey))
		    {
		        for (int i = 0; i < stack.getAmount(); i++)
		        {
		            RefinementResult result = rollRefinement(refinementLevel, true);
		            if (!result.failed())
		            {
		                // Pull existing materia data and re-refine at a higher rarity
		                MateriaType materiaType = MateriaType.fromString(meta.get(Materia.materiaTypeKey, PersistentDataType.STRING));
		                MateriaComponent componentType = MateriaComponent.fromString(meta.get(Materia.componentKey, PersistentDataType.STRING));
		                Materia upgraded = Materia.refine(materiaType, componentType, result.rarity());
		                addResult(p.getUniqueId(), upgraded.getAsItemStack(MateriaState.NORMAL), result.xp());
		            }
		            else
		            {
		                // Failed upgrade — yield scrap
		                addResult(p.getUniqueId(), new ScrapMateria().toItemStack(), 0);
		            }
		        }
		    }
		    // Unrefined: first-time refinement
		    else if (meta.has(Materia.materiaTypeKey) && 
		    		meta.has(Materia.materiaStateKey) && 
		    		MateriaState.fromString(meta.get(Materia.materiaStateKey, PersistentDataType.STRING)) == MateriaState.UNREFINED)
		    {
		        MateriaType materiaType = MateriaType.fromString(meta.get(Materia.materiaTypeKey, PersistentDataType.STRING));
		        
		        // Leather rolls a 70/30 for base vs binding component
		        for (int i = 0; i < stack.getAmount(); i++)
		        {
		            RefinementResult result = rollRefinement(refinementLevel, false);
		            if (!result.failed())
		            {
		                Materia refined = Materia.refine(materiaType, 
		                		materiaType.equals(MateriaType.LEATHER) ? (Chance.of(70) ? MateriaComponent.BINDING : MateriaComponent.BASE) : null, 
		                		result.rarity());
		                addResult(p.getUniqueId(), refined.getAsItemStack(MateriaState.NORMAL), result.xp());
		            }
		            // First-time refinement can't fail, so no else needed here
		        }
		    }
		    else
		    {
		        // Invalid item
		        EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.AMBIENT);
		        e.setCancelled(true);
		        return;
		    }

		    // Clear cursor and open results
		    p.setItemOnCursor(null);
		    Map<ItemStack, Integer> finalResults = refineryResult.getOrDefault(p.getUniqueId(), new HashMap<>());
		    int finalXp = refineryXp.getOrDefault(p.getUniqueId(), 0);
		    EntityEffects.playSound(p, Sound.BLOCK_VAULT_EJECT_ITEM, SoundCategory.AMBIENT);
		    GuiHandler.changeMenu(p, new RefinementResultsPage(p, finalResults, finalXp));
		});
		
		//Reticle
		GuiButton.button(Material.OAK_SIGN).setName("&7[&ei&7]").setLore(
				"&r&fInsert your unrefined materials into the slot below.",
				"&r&fValid materials will be transformed into their respective &bMateria&r&f.",
				"&r&bMateria &r&fserve as components &osolely&r&f in &bProtocol&f: &eΣ&f.C.H.O.","",
				"&r&fYou &omay&r&f attempt further refinement to increase a &bMateria&r&f's rarity,",
				"&r&fthough failure is possible, and will destroy it, yielding &bScrap&f.","",
				"&r&fRarity and failure chance scales inversely by your &e&lRefinement&r&f stat.")
		.place(this, 4, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 12, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 14, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		GuiButton.button(Material.LIGHT_BLUE_STAINED_GLASS_PANE).setName(" ")
		.place(this, 22, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			e.setCancelled(true);
		});
		//Exits
		GuiButton.button(Material.YELLOW_STAINED_GLASS_PANE).setName("<- &e&lGo Back").setLore("Click to return to the Echo Main Page").place(this, 10, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, SoundCategory.MASTER, 1, 1);
			GuiHandler.changeMenu(p, new ProtocolEchoMainPage(p));
		});
		GuiButton.button(Material.RED_STAINED_GLASS_PANE).setName("&c&lExit Menu").setLore("").place(this, 16, e->
		{
			Player p = (Player) e.getWhoClicked();
			p.playSound(p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
			GuiHandler.close(p);
		});
		paint();
	}
	
	public static RefinementResult rollRefinement(int refinementLevel, boolean isUpgrade)
	{
		double failChance;

		if (!isUpgrade) failChance = 0.0; // First-time refinement never fails
		else if (refinementLevel < 10) failChance = 1.0; // For first 10 levels, fail rate is 100%.
		else // Otherwise, failure rate decreases in the mid levels quickly, and then flattens out to 5% @ lvl 100.
		{
			double t = Math.sqrt((refinementLevel - 10.0) / 90.0);
			failChance = 1.0 - (t * 0.95);
		}
		
		boolean failed = Math.random() < failChance;
	    if (failed) return new RefinementResult(null, true, 0);

	    // Roll rarity -- weighted toward higher rarities as refinementLevel grows
	    Rarity rarity = rollRarity(refinementLevel);
	    int xp = calculateXp(refinementLevel, rarity, isUpgrade);

	    return new RefinementResult(rarity, false, xp);
	}

	private static Rarity rollRarity(int refinementLevel)
	{
	    // 5-star: unlocks at 80, scales from 25% -> 80% by level 100
	    if (refinementLevel >= 80)
	    {
	        int chance = (int) NumberUtils.lerp(25, 80, refinementLevel, 80, 100);
	        if (Chance.of(chance)) return Rarity.FIVE;
	    }

	    // 4-star: unlocks at 60, scales from 55% -> 75% by level 100
	    if (refinementLevel >= 60)
	    {
	        int chance = (int) NumberUtils.lerp(55, 75, refinementLevel, 60, 100);
	        if (Chance.of(chance)) return Rarity.FOUR;
	    }

	    // 3-star: unlocks at 40, scales from 75% -> 55% by level 100
	    if (refinementLevel >= 40)
	    {
	        int chance = (int) NumberUtils.lerp(75, 55, refinementLevel, 40, 100);
	        if (Chance.of(chance)) return Rarity.THREE;
	    }

	    // 2-star: unlocks at 30, scales from 80% -> 25% by level 100
	    if (refinementLevel >= 30)
	    {
	        int chance = (int) NumberUtils.lerp(80, 25, refinementLevel, 30, 100);
	        if (Chance.of(chance)) return Rarity.TWO;
	    }

	    // 1-star: fallback, always possible but less likely at higher levels
	    return Rarity.ONE;
	}

	private static int calculateXp(int refinementLevel, Rarity rarity, boolean isUpgrade)
	{
	    // Base XP scales with rarity + a small bonus for upgrades
	    int base = switch (rarity)
	    {
	        case ONE   -> 10;
	        case TWO   -> 25;
	        case THREE -> 50;
	        case FOUR  -> 100;
	        case FIVE  -> 200;
	        default    -> 10;
	    };

	    // Diminishing returns at higher levels so XP isn't runaway
	    double levelMod = 1.0 + (refinementLevel * 0.01);
	    int xp = (int) (base * levelMod);

	    return isUpgrade ? (int) (xp * 1.5) : xp;
	}

	// Lightweight result carrier
	public record RefinementResult(Rarity rarity, boolean failed, int xp) {}
	
	private void addResult(UUID uuid, ItemStack item, int xp)
	{
	    refineryResult.computeIfAbsent(uuid, k -> new HashMap<>())
	                  .merge(item, 1, Integer::sum);
	    refineryXp.merge(uuid, xp, Integer::sum);
	}
}
