package com.eol.echoes;

import com.eol.enums.EchoMaterial;
import com.eol.enums.ElementiumSlotType;
import com.eol.enums.MateriaType;

/**
 * MateriaTypeResolver bridges the Materia system's MateriaType enum
 * to the Echo system's EchoMaterial and ElementiumSlotType enums.
 *
 * This is the single authoritative mapping between the two systems.
 * All forge pipeline stages call through here rather than doing their
 * own ad-hoc switches.
 */
public final class MateriaTypeResolver
{
    private MateriaTypeResolver() {}

    /**
     * Maps a BASE Materia's MateriaType to the corresponding EchoMaterial tier.
     * Returns null if the MateriaType is not a valid weapon base material.
     */
    public static EchoMaterial toEchoMaterial(MateriaType type)
    {
        return switch (type)
        {
            case WOOD      -> EchoMaterial.WOODEN;
            case COPPER    -> EchoMaterial.COPPER;
            case STONE     -> EchoMaterial.STONE;
            case IRON      -> EchoMaterial.IRON;
            case GOLD      -> EchoMaterial.GOLDEN;
            case DIAMOND   -> EchoMaterial.DIAMOND;
            case NETHERITE -> EchoMaterial.NETHERITE;
            default        -> null; // LEATHER, COPPER, STRING, PELT, element types — not weapon bases
        };
    }

    /**
     * Maps an ELEMENT_CORE Materia's MateriaType to the corresponding ElementiumSlotType.
     * Returns ElementiumSlotType.NO_SLOT if the type is not an element core.
     */
    public static ElementiumSlotType toElementiumSlot(MateriaType type)
    {
        return switch (type)
        {
            case CELESTIO -> ElementiumSlotType.CELESTIO;
            case MORTIO   -> ElementiumSlotType.MORTIO;
            case INFERNO  -> ElementiumSlotType.INFERNO;
            case GLACIO   -> ElementiumSlotType.GLACIO;
            case AERO     -> ElementiumSlotType.AERO;
            case GEO      -> ElementiumSlotType.GEO;
            case COSMO    -> ElementiumSlotType.COSMO;
            case HERESIO  -> ElementiumSlotType.HERESIO;
            default       -> ElementiumSlotType.NO_SLOT;
        };
    }

    /**
     * Returns true if the given MateriaType is a valid weapon base.
     */
    public static boolean isValidBase(MateriaType type)
    {
        return toEchoMaterial(type) != null;
    }

    /**
     * Returns true if the given MateriaType is a valid element core.
     */
    public static boolean isValidElementCore(MateriaType type)
    {
        return toElementiumSlot(type) != ElementiumSlotType.NO_SLOT;
    }
}
