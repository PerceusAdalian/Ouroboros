package com.eol.echoes;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;

import com.eol.enums.EchoForm;
import com.eol.enums.EchoMaterial;

/**
 * EchoFormResolver maps an EchoForm + EchoMaterial combination to the
 * corresponding Bukkit Material for use when constructing the Echo ItemStack.
 *
 * This is a pure lookup — no randomness, no config. The Bukkit Material
 * determines the item's base texture and vanilla behavior. Custom model data
 * can be layered on top of this later for resource pack overrides.
 *
 * Note: SPEAR does not have a vanilla Bukkit Material equivalent.
 * It reuses IRON_SWORD / DIAMOND_SWORD etc. visually and is differentiated
 * via display name and custom model data. Adjust when your resource pack is ready.
 */
public final class EchoFormResolver
{
    private EchoFormResolver() {}

    public static EchoForm fromBukkitMaterial(Material material)
    {
    	if (swords.contains(material)) return EchoForm.SWORD;
    	if (spears.contains(material)) return EchoForm.POLEARM;
    	if (axes.contains(material)) return EchoForm.HATCHET;
    	if (pickaxes.contains(material)) return EchoForm.PICKAXE;
    	if (shovels.contains(material)) return EchoForm.SPADE;
    	if (scythes.contains(material)) return EchoForm.SCYTHE;
    	if (bows.contains(material)) return EchoForm.BOW;
    	if (material == Material.TRIDENT) return EchoForm.ARMAMENT;
    	if (material == Material.MACE) return EchoForm.HAMMER;
		return null;
    }
    
    public static Material toBukkitMaterial(EchoForm form, EchoMaterial material)
    {
        return switch (form)
        {
            case SWORD    -> toSword(material);
            case HATCHET  -> toAxe(material);
            case POLEARM  -> toSpear(material);
            case PICKAXE  -> toPickaxe(material);
            case SPADE    -> toShovel(material);
            case SCYTHE   -> toHoe(material);
            case HAMMER   -> toMace(material);
            case ARMAMENT -> toTrident(material);
            case BOW      -> toBow(material);
            case CROSSBOW -> toCrossBow(material);
        };
    }

    private static Material toSword(EchoMaterial material)
    {
        return switch (material)
        {
            case WOODEN    -> Material.WOODEN_SWORD;
            case STONE     -> Material.STONE_SWORD;
            case COPPER    -> Material.COPPER_SWORD;
            case IRON      -> Material.IRON_SWORD;
            case GOLDEN    -> Material.GOLDEN_SWORD;
            case DIAMOND   -> Material.DIAMOND_SWORD;
            case NETHERITE -> Material.NETHERITE_SWORD;
            default -> throw new IllegalArgumentException("Unexpected value: " + material);
        };
    }

    private static Material toAxe(EchoMaterial material)
    {
        return switch (material)
        {
            case WOODEN    -> Material.WOODEN_AXE;
            case STONE     -> Material.STONE_AXE;
            case COPPER    -> Material.COPPER_AXE;
            case IRON      -> Material.IRON_AXE;
            case GOLDEN    -> Material.GOLDEN_AXE;
            case DIAMOND   -> Material.DIAMOND_AXE;
            case NETHERITE -> Material.NETHERITE_AXE;
            default -> throw new IllegalArgumentException("Unexpected value: " + material);
        };
    }

    private static Material toSpear(EchoMaterial material)
    {
        // Spear has no vanilla equivalent — uses sword model as placeholder.
        // Apply custom model data on top of this for resource pack differentiation.
        return switch (material)
        {
            case WOODEN    -> Material.WOODEN_SPEAR;
            case STONE     -> Material.STONE_SPEAR;
            case COPPER    -> Material.COPPER_SPEAR;
            case IRON      -> Material.IRON_SPEAR;
            case GOLDEN    -> Material.GOLDEN_SPEAR;
            case DIAMOND   -> Material.DIAMOND_SPEAR;
            case NETHERITE -> Material.NETHERITE_SPEAR;
            default -> throw new IllegalArgumentException("Unexpected value: " + material);
        };
    }

    private static Material toPickaxe(EchoMaterial material)
    {
        return switch (material)
        {
            case WOODEN    -> Material.WOODEN_PICKAXE;
            case STONE     -> Material.STONE_PICKAXE;
            case COPPER    -> Material.COPPER_PICKAXE;
            case IRON      -> Material.IRON_PICKAXE;
            case GOLDEN    -> Material.GOLDEN_PICKAXE;
            case DIAMOND   -> Material.DIAMOND_PICKAXE;
            case NETHERITE -> Material.NETHERITE_PICKAXE;
            default -> throw new IllegalArgumentException("Unexpected value: " + material);
        };
    }

    private static Material toShovel(EchoMaterial material)
    {
        return switch (material)
        {
            case WOODEN    -> Material.WOODEN_SHOVEL;
            case STONE     -> Material.STONE_SHOVEL;
            case COPPER    -> Material.COPPER_SHOVEL;
            case IRON      -> Material.IRON_SHOVEL;
            case GOLDEN    -> Material.GOLDEN_SHOVEL;
            case DIAMOND   -> Material.DIAMOND_SHOVEL;
            case NETHERITE -> Material.NETHERITE_SHOVEL;
            default -> throw new IllegalArgumentException("Unexpected value: " + material);
        };
    }

    private static Material toHoe(EchoMaterial material)
    {
        return switch (material)
        {
            case WOODEN    -> Material.WOODEN_HOE;
            case STONE     -> Material.STONE_HOE;
            case COPPER    -> Material.COPPER_HOE;
            case IRON      -> Material.IRON_HOE;
            case GOLDEN    -> Material.GOLDEN_HOE;
            case DIAMOND   -> Material.DIAMOND_HOE;
            case NETHERITE -> Material.NETHERITE_HOE;
            default -> throw new IllegalArgumentException("Unexpected value: " + material);
        };
    }
    
    private static Material toMace(EchoMaterial material)
    {
    	if (material == EchoMaterial.HAMMER) return Material.MACE;
		return null;
    }
    
    private static Material toTrident(EchoMaterial material)
    {
    	if (material == EchoMaterial.ARMAMENT) return Material.TRIDENT;
    	return null;
    }
    
    private static Material toBow(EchoMaterial material)
    {
    	if (material == EchoMaterial.BOW) return Material.BOW;
    	return null;
    }
    
    private static Material toCrossBow(EchoMaterial material)
    {
    	if (material == EchoMaterial.CROSSBOW) return Material.CROSSBOW;
    	return null;
    }
    
    public static final Set<Material> swords = EnumSet.of(
			Material.WOODEN_SWORD,Material.STONE_SWORD,Material.IRON_SWORD,Material.GOLDEN_SWORD,
			Material.DIAMOND_SWORD,Material.NETHERITE_SWORD);
    public static final Set<Material> spears = EnumSet.of(
			Material.WOODEN_SPEAR,Material.STONE_SPEAR,Material.IRON_SPEAR,Material.GOLDEN_SPEAR,
			Material.DIAMOND_SPEAR,Material.NETHERITE_SPEAR);
	public static final Set<Material> axes = EnumSet.of(
			Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE,
			Material.DIAMOND_AXE, Material.NETHERITE_AXE);
	public static final Set<Material> pickaxes = EnumSet.of(
			Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE,
			Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE);
	public static final Set<Material> shovels = EnumSet.of(
			Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL,
			Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL);
	public static final Set<Material> scythes = EnumSet.of(
			Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE,
			Material.DIAMOND_HOE, Material.NETHERITE_HOE);
	public static final Set<Material> bows = EnumSet.of(Material.BOW, Material.CROSSBOW);
	
	public static final Set<Material> ALL_ECHO_MATERIALS;
	static
	{
	    ALL_ECHO_MATERIALS = new HashSet<>();
	    ALL_ECHO_MATERIALS.addAll(swords);
	    ALL_ECHO_MATERIALS.addAll(spears);
	    ALL_ECHO_MATERIALS.addAll(axes);
	    ALL_ECHO_MATERIALS.addAll(pickaxes);
	    ALL_ECHO_MATERIALS.addAll(shovels);
	    ALL_ECHO_MATERIALS.addAll(scythes);
	    ALL_ECHO_MATERIALS.addAll(bows);
	    ALL_ECHO_MATERIALS.add(Material.TRIDENT);
	    ALL_ECHO_MATERIALS.add(Material.MACE);
	}
}
