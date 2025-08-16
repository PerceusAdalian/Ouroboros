package com.ouroboros.mobs.utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Registry;
import org.bukkit.block.Biome;

import com.ouroboros.utils.WeightedTable;

public class LevelTable
{
	
	private static final Map<Biome, WeightedTable<Integer>> LEVEL_RANGES = new HashMap<>();

	static {
		// Instead of distributing mobs leveled evenly, we can artificially increase the rarity of high levelled mobs by 
		// weighing levels, to incentivize spending time in more difficult areas.
		WeightedTable<Integer> lowLevelTable = new WeightedTable<Integer>()
			.add(1, 15)
			.add(2, 10)
			.add(3, 8)
			.add(4, 5)
			.add(5, 3)
			.add(6, 1)
			.bake();

		// In more common areas, we want to set up something closer to a bell curve, where low level enemies are less common, 
		// moderate leveled enemies are very common, and high level enemies are rare.
		//
		// This helps make areas feel more connected.
		WeightedTable<Integer> commonLevelTable = new WeightedTable<Integer>()
			.add(4, 2)
			.add(5, 4)
			.add(6, 8)
			.add(7, 10)
			.add(8, 12)
			.add(9, 12)
			.add(10, 12)
			.add(11, 12)
			.add(12, 8)
			.add(13, 4)
			.add(14, 3)
			.add(15, 2)
			.add(16, 1)
			.add(17, 1)
			.bake();

		// Like above.
		WeightedTable<Integer> eliteLevelTable = new WeightedTable<Integer>()
			.add(16, 2)
			.add(17, 3)
			.add(18, 4)
			.add(19, 8)
			.add(20, 10)
			.add(21, 10)
			.add(22, 10)
			.add(23, 10)
			.add(24, 8)
			.add(25, 6)
			.add(26, 5)
			.add(27, 4)
			.add(28, 2)
			.add(29, 1)
			.add(30, 1)
			.bake();
		
		// For difficult areas, flattening out the distribution makes the player feel like the area is more dangerous/chaotic.
		WeightedTable<Integer> endgameLevelTable = new WeightedTable<Integer>()
			.add(50, 1)
			.add(51, 1)
			.add(52, 1)
			.add(53, 1)
			.add(54, 1)
			.add(55, 1)
			.add(56, 1)
			.add(57, 1)
			.add(58, 1)
			.add(59, 1)
			.add(60, 1)
			.bake();
		
		LEVEL_RANGES.put(Biome.PLAINS, lowLevelTable);
		LEVEL_RANGES.put(Biome.BEACH, lowLevelTable);
		LEVEL_RANGES.put(Biome.OCEAN, eliteLevelTable);
		LEVEL_RANGES.put(Biome.DESERT, eliteLevelTable);
		LEVEL_RANGES.put(Biome.JUNGLE, eliteLevelTable);
		LEVEL_RANGES.put(Biome.DEEP_DARK, endgameLevelTable);
		LEVEL_RANGES.put(Biome.END_MIDLANDS, endgameLevelTable);
		LEVEL_RANGES.put(Biome.END_BARRENS, endgameLevelTable);
		LEVEL_RANGES.put(Biome.END_HIGHLANDS, endgameLevelTable);
		LEVEL_RANGES.put(Biome.NETHER_WASTES, eliteLevelTable);
		LEVEL_RANGES.put(Biome.BASALT_DELTAS, eliteLevelTable);
		LEVEL_RANGES.put(Biome.SOUL_SAND_VALLEY, eliteLevelTable);
		LEVEL_RANGES.put(Biome.CRIMSON_FOREST, eliteLevelTable);
		LEVEL_RANGES.put(Biome.WARPED_FOREST, eliteLevelTable);

		// Default out anything not listen above to common level table
		for (Biome b : Registry.BIOME)
		{
			if (!LEVEL_RANGES.containsKey(b))
			{
				LEVEL_RANGES.put(b, commonLevelTable);
			}
		}
	}
	
	public static int getLevel(Biome biome) 
	{
		return LEVEL_RANGES.get(biome).poll();
	}
}
