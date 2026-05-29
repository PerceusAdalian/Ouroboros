package com.lol.spells.instances.geo;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Cultivate extends Spell
{

	public Cultivate() 
	{
		super("Cultivate", "cultivate", Material.WHEAT, SpellType.CANTRIP, SpellementType.GEO, CastConditions.MIXED, Rarity.ONE, 15, 0.1, false, false,
				"&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_BLOCK),
				"&r&6Cultivate&f: &6&oFertilize&r&f --",
				"&r&fFully grow &6target &b&oCrop&r&f.","",
				"&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_BLOCK),
				"&r&6Cultivate&f: &6&oTill Earth&r&f --",
				"&r&fTransform &6target &b&oSoil&r&f into &b&oFarmland&r&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_BLOCK))
		{
			Set<Material> soilBlocks = Set.of(Material.DIRT, Material.GRASS_BLOCK, Material.COARSE_DIRT, Material.PODZOL, 
					Material.MYCELIUM, Material.DIRT_PATH, Material.ROOTED_DIRT);
			Block b = e.getClickedBlock();
			if (!soilBlocks.contains(b.getType())) return -1;
			
			EntityEffects.playSound(p, Sound.ITEM_HOE_TILL, SoundCategory.MASTER);
			ObsParticles.drawWisps(b.getLocation().add(0,0.5,0), 1, 1, 4, Particle.HAPPY_VILLAGER, null);
		    
			b.setType(Material.FARMLAND);

			return 15;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_BLOCK))
		{			
			Block b = e.getClickedBlock();
			
			if (!(b.getBlockData() instanceof Ageable crop)) return -1;
			if (crop.getAge() == crop.getMaximumAge()) return -1;
			
			EntityEffects.playSound(p, Sound.ITEM_BONE_MEAL_USE, SoundCategory.MASTER);
			ObsParticles.drawWisps(b.getLocation().add(0,0.5,0), 1, 1, 4, Particle.HAPPY_VILLAGER, null);
			
			crop.setAge(crop.getMaximumAge());
			b.setBlockData(crop);
			
			return 15;
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 15;
	}

}
