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
		// ═══════════════════════════════════════════════════════════════════════════════
		// TIER REFERENCE
		//   Calamity : 100
		//   Royal    : 80 – 99
		//   Elite    : 60 – 79
		//   Uncommon : 40 – 59
		//   Common   : 20 – 39
		//   Fodder   : 1  – 19
		// ═══════════════════════════════════════════════════════════════════════════════

		// ───────────────────────────────────────────────────────────────────────────────
		// BASE TABLES
		// These reusable tables are shared by biomes of equivalent difficulty.
		// Biomes with unique flavour get their own dedicated table below.
		// ───────────────────────────────────────────────────────────────────────────────

		// FODDER TABLE
		// Heavy low-end skew. The rare common/uncommon cameo rewards exploration
		// and reminds the player that the world has teeth even here.
		WeightedTable<Integer> fodderTable = new WeightedTable<Integer>()
		    .add(1,  15).add(2,  13).add(3,  11).add(4,   9).add(5,   8)
		    .add(6,   7).add(7,   6).add(8,   5).add(9,   4).add(10,  3)
		    .add(11,  3).add(12,  2).add(13,  2).add(14,  1).add(15,  1)
		    .add(16,  1).add(17,  1).add(18,  1).add(19,  1)
		    .add(20,  2).add(21,  2)               // Common cameo  (rare)
		    .add(40,  1)                           // Uncommon cameo (very rare)
		    .bake();

		// COMMON TABLE
		// Bell curve centered around 25–28. Low fodder bleed-in at the bottom;
		// a couple of uncommon outliers at the top to keep players alert.
		WeightedTable<Integer> commonTable = new WeightedTable<Integer>()
		    .add(15,  1).add(16,  1).add(17,  1).add(18,  1).add(19,  1) // Fodder bleed
		    .add(20,  4).add(21,  7).add(22, 10).add(23, 12).add(24, 13)
		    .add(25, 13).add(26, 12).add(27, 10).add(28,  8).add(29,  6)
		    .add(30,  4).add(31,  3).add(32,  2).add(33,  1).add(34,  1)
		    .add(40,  2).add(41,  2)               // Uncommon cameo (rare)
		    .bake();

		// UNCOMMON TABLE
		// Bell curve centered around 45–48. Common bleed at the bottom;
		// occasional elite spike at the top for adrenaline moments.
		WeightedTable<Integer> uncommonTable = new WeightedTable<Integer>()
		    .add(36,  1).add(37,  1).add(38,  2).add(39,  2)             // Common bleed
		    .add(40,  5).add(41,  8).add(42, 10).add(43, 12).add(44, 13)
		    .add(45, 13).add(46, 12).add(47, 10).add(48,  8).add(49,  6)
		    .add(50,  4).add(51,  3).add(52,  2).add(53,  1).add(54,  1)
		    .add(60,  2).add(61,  1)               // Elite cameo   (rare)
		    .bake();

		// ELITE TABLE
		// Flatter distribution — flatter = more chaotic, more threatening.
		// Uncommon bleed at the bottom; royal spike to remind you this isn't safe.
		WeightedTable<Integer> eliteTable = new WeightedTable<Integer>()
		    .add(56,  1).add(57,  1).add(58,  2).add(59,  2)             // Uncommon bleed
		    .add(60,  6).add(61,  7).add(62,  8).add(63,  9).add(64,  9)
		    .add(65,  9).add(66,  8).add(67,  7).add(68,  6).add(69,  5)
		    .add(70,  4).add(71,  3).add(72,  2).add(73,  2).add(74,  1)
		    .add(75,  1).add(76,  1).add(77,  1).add(78,  1).add(79,  1)
		    .add(80,  2).add(81,  1)               // Royal cameo   (rare)
		    .bake();

		// ROYAL TABLE
		// Very flat — this is endgame territory. Everything is a threat.
		// Elite bleed at the bottom; calamity to underscore the stakes.
		WeightedTable<Integer> royalTable = new WeightedTable<Integer>()
		    .add(76,  1).add(77,  1).add(78,  2).add(79,  2)             // Elite bleed
		    .add(80,  6).add(81,  7).add(82,  8).add(83,  8).add(84,  8)
		    .add(85,  8).add(86,  8).add(87,  7).add(88,  6).add(89,  5)
		    .add(90,  4).add(91,  3).add(92,  2).add(93,  2).add(94,  1)
		    .add(95,  1).add(96,  1).add(97,  1).add(98,  1).add(99,  1)
		    .add(100, 2)                           // Calamity cameo (rare)
		    .bake();

		// CALAMITY TABLE
		// Fully flat across the royal/calamity boundary — nothing is safe.
		// Used only for the most extreme biomes.
		WeightedTable<Integer> calamityTable = new WeightedTable<Integer>()
		    .add(90,  1).add(91,  1).add(92,  1).add(93,  1).add(94,  1)
		    .add(95,  2).add(96,  2).add(97,  2).add(98,  2).add(99,  2)
		    .add(100, 4)                           // Calamity is king here
		    .bake();

		// ───────────────────────────────────────────────────────────────────────────────
		// BIOME-SPECIFIC TABLES
		// These have a unique difficulty identity that doesn't map cleanly to one tier.
		// ───────────────────────────────────────────────────────────────────────────────

		// DESERT — Common is the baseline; two uncommon weight bands; one elite cameo.
		// Harsh environment = mobs trend harder than other Common biomes.
		WeightedTable<Integer> desertTable = new WeightedTable<Integer>()
		    .add(16,  1).add(17,  1).add(18,  1).add(19,  1)             // Fodder bleed
		    .add(20,  5).add(21,  8).add(22, 11).add(23, 12).add(24, 12) // Common (baseline)
		    .add(25, 11).add(26,  9).add(27,  7).add(28,  5).add(29,  3)
		    .add(30,  2).add(31,  1)
		    .add(40,  5).add(41,  5)               // Uncommon band 1
		    .add(42,  4).add(43,  3)               // Uncommon band 2
		    .add(60,  1)                           // Elite cameo
		    .bake();

		// OCEAN — Vast and unpredictable. Uncommon baseline with a real elite presence.
		WeightedTable<Integer> oceanTable = new WeightedTable<Integer>()
		    .add(30,  1).add(31,  1).add(32,  1)                         // Common bleed
		    .add(40,  6).add(41,  8).add(42, 10).add(43, 10).add(44,  9) // Uncommon (baseline)
		    .add(45,  7).add(46,  5).add(47,  4).add(48,  3).add(49,  2)
		    .add(60,  5).add(61,  5).add(62,  4).add(63,  3).add(64,  2) // Elite presence
		    .add(80,  1)                           // Royal cameo
		    .bake();

		// DEEP OCEAN — Darker, more threatening than ocean. Elite is the real baseline.
		WeightedTable<Integer> deepOceanTable = new WeightedTable<Integer>()
		    .add(40,  2).add(41,  2).add(42,  2)                         // Uncommon bleed
		    .add(60,  8).add(61, 10).add(62, 11).add(63, 11).add(64, 10) // Elite (baseline)
		    .add(65,  8).add(66,  6).add(67,  5).add(68,  4).add(69,  3)
		    .add(70,  2).add(71,  1)
		    .add(80,  3).add(81,  2).add(82,  1)   // Royal cameo
		    .bake();

		// JUNGLE — Dense and hostile. Common baseline; strong uncommon presence;
		// a couple of elite cameos to reflect the legendary danger of jungles.
		WeightedTable<Integer> jungleTable = new WeightedTable<Integer>()
		    .add(16,  1).add(17,  1).add(18,  1).add(19,  1)
		    .add(20,  5).add(21,  8).add(22, 10).add(23, 10).add(24,  9) // Common (baseline)
		    .add(25,  7).add(26,  5).add(27,  3).add(28,  2)
		    .add(40,  5).add(41,  5).add(42,  4).add(43,  3)             // Uncommon (strong)
		    .add(44,  2).add(45,  1)
		    .add(60,  2).add(61,  1)               // Elite cameo
		    .bake();

		// SWAMP — Murky and foreboding. Common baseline with a notable uncommon tail.
		WeightedTable<Integer> swampTable = new WeightedTable<Integer>()
		    .add(12,  1).add(13,  1).add(14,  1).add(15,  1)
		    .add(16,  1).add(17,  1).add(18,  1).add(19,  1)             // Fodder bleed
		    .add(20,  6).add(21,  9).add(22, 11).add(23, 11).add(24, 10) // Common (baseline)
		    .add(25,  8).add(26,  6).add(27,  4).add(28,  2).add(29,  1)
		    .add(40,  4).add(41,  4).add(42,  3).add(43,  2).add(44,  1) // Uncommon tail
		    .bake();

		// DARK FOREST — Sinister. Common dominant but uncommon is meaningful.
		WeightedTable<Integer> darkForestTable = new WeightedTable<Integer>()
		    .add(13,  1).add(14,  1).add(15,  1).add(16,  1)
		    .add(17,  1).add(18,  1).add(19,  1)                         // Fodder bleed
		    .add(20,  6).add(21,  9).add(22, 11).add(23, 12).add(24, 11) // Common (dominant)
		    .add(25,  9).add(26,  7).add(27,  5).add(28,  3).add(29,  2)
		    .add(40,  4).add(41,  3).add(42,  2)   // Uncommon (occasional)
		    .add(60,  1)                           // Elite cameo (lurking dread)
		    .bake();

		// DEEP DARK — Ancient, oppressive. Elite floor; royal dominant; calamity looms.
		WeightedTable<Integer> deepDarkTable = new WeightedTable<Integer>()
		    .add(60,  4).add(61,  5).add(62,  5).add(63,  4).add(64,  3) // Elite (floor)
		    .add(80,  8).add(81,  9).add(82, 10).add(83, 10).add(84,  9) // Royal (dominant)
		    .add(85,  8).add(86,  6).add(87,  5).add(88,  4).add(89,  3)
		    .add(90,  2).add(91,  1)
		    .add(100, 3)                           // Calamity (ever-present threat)
		    .bake();

		// LUSH CAVES — Beautiful but underground. Common baseline; uncommon lurks.
		WeightedTable<Integer> lushCavesTable = new WeightedTable<Integer>()
		    .add(15,  1).add(16,  1).add(17,  1).add(18,  1).add(19,  1)
		    .add(20,  7).add(21,  9).add(22, 11).add(23, 11).add(24,  9)
		    .add(25,  7).add(26,  5).add(27,  3).add(28,  2)
		    .add(40,  4).add(41,  4).add(42,  3).add(43,  2).add(44,  1) // Uncommon
		    .add(60,  1)                           // Elite cameo
		    .bake();

		// DRIPSTONE CAVES — Rough underground. Uncommon dominant; elite possible.
		WeightedTable<Integer> dripstoneCavesTable = new WeightedTable<Integer>()
		    .add(18,  1).add(19,  1)
		    .add(20,  3).add(21,  3).add(22,  2)                         // Common bleed
		    .add(40,  7).add(41, 10).add(42, 11).add(43, 11).add(44, 10) // Uncommon (dominant)
		    .add(45,  8).add(46,  6).add(47,  4).add(48,  3).add(49,  2)
		    .add(60,  3).add(61,  2).add(62,  1)   // Elite (occasional)
		    .bake();

		// BADLANDS — Scarred and hostile. Uncommon dominant; elite meaningful.
		WeightedTable<Integer> badlandsTable = new WeightedTable<Integer>()
		    .add(18,  1).add(19,  1)
		    .add(20,  3).add(21,  3).add(22,  2)                         // Common bleed
		    .add(40,  7).add(41,  9).add(42, 11).add(43, 11).add(44, 10) // Uncommon (dominant)
		    .add(45,  8).add(46,  6).add(47,  4).add(48,  3).add(49,  2)
		    .add(60,  4).add(61,  3).add(62,  2).add(63,  1)             // Elite presence
		    .bake();

		// FROZEN PEAKS / JAGGED PEAKS / STONY PEAKS — High altitude, brutal exposure.
		// Uncommon dominant; elite is real; common barely clings on.
		WeightedTable<Integer> mountainPeaksTable = new WeightedTable<Integer>()
		    .add(18,  1).add(19,  1)
		    .add(20,  3).add(21,  3).add(22,  2)                         // Common bleed
		    .add(40,  7).add(41,  9).add(42, 11).add(43, 11).add(44, 10) // Uncommon (dominant)
		    .add(45,  8).add(46,  6).add(47,  4).add(48,  3).add(49,  2)
		    .add(60,  4).add(61,  3).add(62,  2).add(63,  1)             // Elite (occasional)
		    .bake();

		// NETHER WASTES — Standard Nether. Elite dominant; royal frequent; calamity lurks.
		WeightedTable<Integer> netherGeneralTable = new WeightedTable<Integer>()
		    .add(58,  1).add(59,  1)                                      // Elite bleed-in
		    .add(60,  7).add(61,  8).add(62,  9).add(63,  9).add(64,  8) // Elite (dominant)
		    .add(65,  7).add(66,  6).add(67,  5).add(68,  4).add(69,  3)
		    .add(70,  2).add(71,  1)
		    .add(80,  5).add(81,  5).add(82,  4).add(83,  3).add(84,  2) // Royal (frequent)
		    .add(85,  1)
		    .add(100, 1)                           // Calamity (very rare)
		    .bake();

		// THE END (SMALL ISLANDS) — The void and its inhabitants. Royal baseline.
		WeightedTable<Integer> theEndTable = new WeightedTable<Integer>()
		    .add(78,  1).add(79,  1)                                      // Elite bleed
		    .add(80,  7).add(81,  9).add(82, 11).add(83, 11).add(84, 10) // Royal (dominant)
		    .add(85,  8).add(86,  6).add(87,  5).add(88,  4).add(89,  3)
		    .add(90,  2).add(91,  1)
		    .add(100, 2)                           // Calamity cameo
		    .bake();

		// ═══════════════════════════════════════════════════════════════════════════════
		// BIOME → TABLE ASSIGNMENTS
		//
		// Difficulty philosophy per region:
		//
		//   OVERWORLD (temperate)  → fodder / common
		//   OVERWORLD (harsh)      → common / uncommon
		//   OVERWORLD (caves)      → uncommon / elite
		//   OCEAN                  → uncommon / elite
		//   NETHER                 → elite minimum (no exceptions)
		//   THE END                → royal minimum (no exceptions)
		// ═══════════════════════════════════════════════════════════════════════════════

		// ── OVERWORLD : TEMPERATE ────────────────────────────────────────────────────

		LEVEL_RANGES.put(Biome.PLAINS,              						fodderTable);
		LEVEL_RANGES.put(Biome.SUNFLOWER_PLAINS,    						fodderTable);
		LEVEL_RANGES.put(Biome.BEACH,               						fodderTable);
		LEVEL_RANGES.put(Biome.SNOWY_BEACH,         						commonTable);    // Cold = slightly harder
		LEVEL_RANGES.put(Biome.STONY_SHORE,        						 	commonTable);    // Rocky and exposed
		LEVEL_RANGES.put(Biome.MEADOW,             						 	fodderTable);
		LEVEL_RANGES.put(Biome.FOREST,              						commonTable);
		LEVEL_RANGES.put(Biome.FLOWER_FOREST,       						uncommonTable);
		LEVEL_RANGES.put(Biome.BIRCH_FOREST,        						commonTable);
		LEVEL_RANGES.put(Biome.OLD_GROWTH_BIRCH_FOREST, 					commonTable);
		LEVEL_RANGES.put(Biome.DARK_FOREST,         						darkForestTable);
		LEVEL_RANGES.put(Biome.CHERRY_GROVE,        						commonTable);    // Whimsical but not safe
		LEVEL_RANGES.put(Biome.RIVER,               						fodderTable);
		LEVEL_RANGES.put(Biome.MUSHROOM_FIELDS,     						commonTable);    // Peaceful, slight threat at night

		// ── OVERWORLD : COLD / HIGHLAND ─────────────────────────────────────────────

		LEVEL_RANGES.put(Biome.TAIGA,               						commonTable);
		LEVEL_RANGES.put(Biome.OLD_GROWTH_PINE_TAIGA,  						commonTable);
		LEVEL_RANGES.put(Biome.OLD_GROWTH_SPRUCE_TAIGA, 					uncommonTable);
		LEVEL_RANGES.put(Biome.SNOWY_PLAINS,        						commonTable);
		LEVEL_RANGES.put(Biome.SNOWY_TAIGA,         						commonTable);
		LEVEL_RANGES.put(Biome.GROVE,               						commonTable);
		LEVEL_RANGES.put(Biome.SNOWY_SLOPES,        						uncommonTable);
		LEVEL_RANGES.put(Biome.FROZEN_RIVER,        						commonTable);
		LEVEL_RANGES.put(Biome.ICE_SPIKES,          						uncommonTable);  // Harsh, rare terrain
		LEVEL_RANGES.put(Biome.FROZEN_PEAKS,        						mountainPeaksTable);
		LEVEL_RANGES.put(Biome.JAGGED_PEAKS,        						mountainPeaksTable);
		LEVEL_RANGES.put(Biome.STONY_PEAKS,         						mountainPeaksTable);
		LEVEL_RANGES.put(Biome.WINDSWEPT_HILLS,     						uncommonTable);
		LEVEL_RANGES.put(Biome.WINDSWEPT_GRAVELLY_HILLS, 					uncommonTable);
		LEVEL_RANGES.put(Biome.WINDSWEPT_FOREST,    						uncommonTable);
		LEVEL_RANGES.put(Biome.WINDSWEPT_SAVANNA,   						uncommonTable);

		// ── OVERWORLD : HOT / DRY ────────────────────────────────────────────────────

		LEVEL_RANGES.put(Biome.DESERT,              						desertTable);
		LEVEL_RANGES.put(Biome.SAVANNA,             						commonTable);
		LEVEL_RANGES.put(Biome.SAVANNA_PLATEAU,     						uncommonTable);  // Elevated = harder
		LEVEL_RANGES.put(Biome.BADLANDS,            						badlandsTable);
		LEVEL_RANGES.put(Biome.ERODED_BADLANDS,     						badlandsTable);
		LEVEL_RANGES.put(Biome.WOODED_BADLANDS,     						uncommonTable);

		// ── OVERWORLD : WET / LUSH ───────────────────────────────────────────────────

		LEVEL_RANGES.put(Biome.JUNGLE,              						jungleTable);
		LEVEL_RANGES.put(Biome.SPARSE_JUNGLE,       						commonTable);    // Less dense = less danger
		LEVEL_RANGES.put(Biome.BAMBOO_JUNGLE,       						jungleTable);    // Dense = full jungle rules
		LEVEL_RANGES.put(Biome.SWAMP,               						swampTable);
		LEVEL_RANGES.put(Biome.MANGROVE_SWAMP,      						uncommonTable);  // More oppressive than swamp

		// ── OVERWORLD : OCEAN ────────────────────────────────────────────────────────

		LEVEL_RANGES.put(Biome.OCEAN,               						oceanTable);
		LEVEL_RANGES.put(Biome.DEEP_OCEAN,          						deepOceanTable);
		LEVEL_RANGES.put(Biome.COLD_OCEAN,          						oceanTable);
		LEVEL_RANGES.put(Biome.DEEP_COLD_OCEAN,     						deepOceanTable);
		LEVEL_RANGES.put(Biome.LUKEWARM_OCEAN,      						oceanTable);
		LEVEL_RANGES.put(Biome.DEEP_LUKEWARM_OCEAN, 						deepOceanTable);
		LEVEL_RANGES.put(Biome.WARM_OCEAN,          						oceanTable);
		LEVEL_RANGES.put(Biome.FROZEN_OCEAN,        						uncommonTable);  // Hostile surface conditions
		LEVEL_RANGES.put(Biome.DEEP_FROZEN_OCEAN,   						eliteTable);     // Frozen + deep = brutal

		// ── OVERWORLD : UNDERGROUND ──────────────────────────────────────────────────

		LEVEL_RANGES.put(Biome.LUSH_CAVES,          						lushCavesTable);
		LEVEL_RANGES.put(Biome.DRIPSTONE_CAVES,     						dripstoneCavesTable);
		LEVEL_RANGES.put(Biome.DEEP_DARK,           						deepDarkTable);

		// ── NETHER — Minimum tier: Elite ─────────────────────────────────────────────

		LEVEL_RANGES.put(Biome.NETHER_WASTES,       						netherGeneralTable);
		LEVEL_RANGES.put(Biome.SOUL_SAND_VALLEY,    						netherGeneralTable);
		LEVEL_RANGES.put(Biome.CRIMSON_FOREST,      						royalTable);
		LEVEL_RANGES.put(Biome.WARPED_FOREST,       						royalTable);
		LEVEL_RANGES.put(Biome.BASALT_DELTAS,       						netherGeneralTable);

		// ── THE END — Minimum tier: Royal ────────────────────────────────────────────

		LEVEL_RANGES.put(Biome.THE_END,             						theEndTable);
		LEVEL_RANGES.put(Biome.END_MIDLANDS,        						calamityTable);
		LEVEL_RANGES.put(Biome.END_HIGHLANDS,       						calamityTable);
		LEVEL_RANGES.put(Biome.END_BARRENS,         						calamityTable);
		LEVEL_RANGES.put(Biome.SMALL_END_ISLANDS,   						theEndTable);

		// Default out anything not listen above to common level table
		for (Biome b : Registry.BIOME)
		{
			if (!LEVEL_RANGES.containsKey(b))
			{
				LEVEL_RANGES.put(b, fodderTable);
			}
		}
	}
	
	public static int getLevel(Biome biome) 
	{
		return LEVEL_RANGES.get(biome).poll();
	}
}
