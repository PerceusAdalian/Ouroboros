package com.ouroboros.mobs;

import java.util.List;

import org.bukkit.entity.EntityType;

import com.ouroboros.mobs.Outbreak.OutbreakEntry;

public class OutbreakRegistry
{

	Outbreak inferno_elementals_0 = new Outbreak("Heat Wave",List.of(
			new OutbreakEntry(EntityType.BLAZE, 10, "Inferno Elemental"),
			new OutbreakEntry(EntityType.BLAZE, 10, "Inferno Elemental"),
			new OutbreakEntry(EntityType.BLAZE, 10, "Inferno Elemental"),
			new OutbreakEntry(EntityType.BLAZE, 10, "Inferno Elemental"),
			new OutbreakEntry(EntityType.BLAZE, 10, "Inferno Elemental")));
	
	Outbreak inferno_elementals_1 = new Outbreak("Heat Exhaustion",List.of(
			new OutbreakEntry(EntityType.BLAZE, 10, "Inferno Elemental"),
			new OutbreakEntry(EntityType.MAGMA_CUBE, 10, "Lava Cube"),
			new OutbreakEntry(EntityType.MAGMA_CUBE, 10, "Lava Cube"),
			new OutbreakEntry(EntityType.BLAZE, 10, "Inferno Elemental"),
			new OutbreakEntry(EntityType.BLAZE, 13, "Inferno Elemental"),
			new OutbreakEntry(EntityType.PIGLIN, 14, "Infernal Warrior"),
			new OutbreakEntry(EntityType.BLAZE, 15, "Inferno Elemental")));
	
	Outbreak inferno_elementals_2 = new Outbreak("Drought",List.of(
			new OutbreakEntry(EntityType.MAGMA_CUBE, 20, "Lava Cube"),
			new OutbreakEntry(EntityType.PIGLIN, 20, "Infernal Warrior"),
			new OutbreakEntry(EntityType.ZOGLIN, 20, "Infernal Cattle"),
			new OutbreakEntry(EntityType.BLAZE, 22, "Inferno Elemental"),
			new OutbreakEntry(EntityType.BLAZE, 25, "Inferno Elemental"),
			new OutbreakEntry(EntityType.BLAZE, 28, "Inferno Elemental"),
			new OutbreakEntry(EntityType.PIGLIN_BRUTE, 30, "Infernal General")));
	
	Outbreak inferno_elementals_3 = new Outbreak("Infernal Surge",List.of(
			new OutbreakEntry(EntityType.PIGLIN, 30, "Infernal Warrior"),
			new OutbreakEntry(EntityType.PIGLIN, 30, "Infernal Warrior"),
			new OutbreakEntry(EntityType.MAGMA_CUBE, 30, "Lava Cube"),
			new OutbreakEntry(EntityType.PIGLIN_BRUTE, 33, "Infernal General"),
			new OutbreakEntry(EntityType.ZOGLIN, 35, "Infernal Cattle"),
			new OutbreakEntry(EntityType.BLAZE, 37, "Inferno Elemental"),
			new OutbreakEntry(EntityType.BLAZE, 40, "Inferno Elemental")));
	
	Outbreak inferno_elementals_4 = new Outbreak("Infernal Bastion",List.of(
			new OutbreakEntry(EntityType.MAGMA_CUBE, 40, "Lava Cube"),
			new OutbreakEntry(EntityType.MAGMA_CUBE, 40, "Lava Cube"),
			new OutbreakEntry(EntityType.ZOGLIN, 40, "Infernal Cattle"),
			new OutbreakEntry(EntityType.PIGLIN, 44, "Infernal Warrior"),
			new OutbreakEntry(EntityType.PIGLIN, 47, "Infernal Warrior"),
			new OutbreakEntry(EntityType.PIGLIN_BRUTE, 50, "Infernal General"),
			new OutbreakEntry(EntityType.BLAZE, 55, "Inferno Elemental")));
	
	Outbreak inferno_elementals_5 = new Outbreak("Infernal Incursion",List.of(
			new OutbreakEntry(EntityType.PIGLIN, 60, "Inferno Elemental"),
			new OutbreakEntry(EntityType.PIGLIN, 65, "Inferno Elemental"),
			new OutbreakEntry(EntityType.PIGLIN_BRUTE, 65, "Infernal General"),
			new OutbreakEntry(EntityType.MAGMA_CUBE, 70, "Lava Cube"),
			new OutbreakEntry(EntityType.BLAZE, 70, "Inferno Elemental"),
			new OutbreakEntry(EntityType.BLAZE, 72, "Inferno Elemental"),
			new OutbreakEntry(EntityType.PIGLIN_BRUTE, 75, "Infernal General")));
	
	
}
