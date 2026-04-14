package com.eol.echoes;

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

    public static Material toBukkitMaterial(EchoForm form, EchoMaterial material)
    {
        return switch (form)
        {
            case SWORD   -> toSword(material);
            case AXE     -> toAxe(material);
            case SPEAR   -> toSpear(material);
            case PICKAXE -> toPickaxe(material);
            case SHOVEL  -> toShovel(material);
            case HOE     -> toHoe(material);
        };
    }

    private static Material toSword(EchoMaterial material)
    {
        return switch (material)
        {
            case WOODEN   -> Material.WOODEN_SWORD;
            case STONE    -> Material.STONE_SWORD;
            case COPPER   -> Material.COPPER_SWORD;
            case IRON     -> Material.IRON_SWORD;
            case GOLDEN   -> Material.GOLDEN_SWORD;
            case DIAMOND  -> Material.DIAMOND_SWORD;
            case NETHERITE -> Material.NETHERITE_SWORD;
        };
    }

    private static Material toAxe(EchoMaterial material)
    {
        return switch (material)
        {
            case WOODEN   -> Material.WOODEN_AXE;
            case STONE    -> Material.STONE_AXE;
            case COPPER   -> Material.COPPER_AXE;
            case IRON     -> Material.IRON_AXE;
            case GOLDEN   -> Material.GOLDEN_AXE;
            case DIAMOND  -> Material.DIAMOND_AXE;
            case NETHERITE -> Material.NETHERITE_AXE;
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
        };
    }

    private static Material toPickaxe(EchoMaterial material)
    {
        return switch (material)
        {
            case WOODEN   -> Material.WOODEN_PICKAXE;
            case STONE    -> Material.STONE_PICKAXE;
            case COPPER   -> Material.COPPER_PICKAXE;
            case IRON     -> Material.IRON_PICKAXE;
            case GOLDEN   -> Material.GOLDEN_PICKAXE;
            case DIAMOND  -> Material.DIAMOND_PICKAXE;
            case NETHERITE -> Material.NETHERITE_PICKAXE;
        };
    }

    private static Material toShovel(EchoMaterial material)
    {
        return switch (material)
        {
            case WOODEN   -> Material.WOODEN_SHOVEL;
            case STONE    -> Material.STONE_SHOVEL;
            case COPPER   -> Material.COPPER_SHOVEL;
            case IRON     -> Material.IRON_SHOVEL;
            case GOLDEN   -> Material.GOLDEN_SHOVEL;
            case DIAMOND  -> Material.DIAMOND_SHOVEL;
            case NETHERITE -> Material.NETHERITE_SHOVEL;
        };
    }

    private static Material toHoe(EchoMaterial material)
    {
        return switch (material)
        {
            case WOODEN   -> Material.WOODEN_HOE;
            case STONE    -> Material.STONE_HOE;
            case COPPER   -> Material.COPPER_HOE;
            case IRON     -> Material.IRON_HOE;
            case GOLDEN   -> Material.GOLDEN_HOE;
            case DIAMOND  -> Material.DIAMOND_HOE;
            case NETHERITE -> Material.NETHERITE_HOE;
        };
    }
}
