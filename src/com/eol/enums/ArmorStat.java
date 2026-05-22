package com.eol.enums;

import com.ouroboros.utils.Symbols;

/**
 * ArmorStat identifies which of the four core Echo armor stats
 * an ActiveModifier directly mutates.
 */
public enum ArmorStat 
{
    ARMOR_RATING(Symbols.ARMOR+"AR"),
    BLOCK_RATE("Blk Rate"),
    CRITICAL_ARMOR_RATING("Crit "+Symbols.ARMOR),
    CRITICAL_BLOCK_RATE("Crit Blk%");

    private final String displayName;

    ArmorStat(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public static ArmorStat fromString(String input)
    {
        try
        {
            return ArmorStat.valueOf(input.toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }

}
