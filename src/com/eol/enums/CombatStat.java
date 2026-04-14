package com.eol.enums;

/**
 * CombatStat identifies which of the four core Echo combat stats
 * an ActiveModifier directly mutates.
 */
public enum CombatStat
{
    ATTACK("Atk"),

    ATTACK_RATING("Atk Rate"),

    CRIT_RATE("Crit%"),

    CRIT_MODIFIER("Crit Modifier");

    private final String displayName;

    CombatStat(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public static CombatStat fromString(String input)
    {
        try
        {
            return CombatStat.valueOf(input.toUpperCase());
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
    }
}
