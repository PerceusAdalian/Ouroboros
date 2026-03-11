package com.lol.wand;

import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;

public class WandRegistry 
{
	public static void load()
	{
		Wand.register(new Wand("Magic Wand", "wand_1", Rarity.ONE, null, 1, 100));
		Wand.register(new Wand("Magic Wand", "wand_2", Rarity.TWO, null, 2, 200));
		Wand.register(new Wand("Magic Wand", "wand_3", Rarity.THREE, null, 4, 400));
		Wand.register(new Wand("Magic Wand", "wand_4", Rarity.FOUR, null, 5, 1000));
		Wand.register(new Wand("&b&oArchmage's Staff&r&f", "wand_5", Rarity.FIVE, null, 5, 1500));
		Wand.register(new Wand("&eΣOL&f: &eLumina's Scepter&r&f", "luminas_wand", Rarity.SIX, ElementType.CELESTIO, 8, 2000));
		Wand.register(new Wand("&eΣOL&f: &2&lTwilight Catalyst&r&f", "twilight_catalyst", Rarity.SEVEN, ElementType.HERESIO, 10, 3000));
	}
}
