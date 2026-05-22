package com.eol.echoes;

public class ArmorData
{
    private final int    armorRating;
    private final double blockRate;
    private final int    criticalArmorRating;
    private final double criticalBlockRate;
    private final int    maxDurability;
    private final int    currentDurability;

    public ArmorData(int armorRating, double blockRate, int criticalArmorRating, double criticalBlockRate,
                     int maxDurability, int currentDurability)
    {
        this.armorRating         = armorRating;
        this.blockRate           = blockRate;
        this.criticalArmorRating = criticalArmorRating;
        this.criticalBlockRate   = criticalBlockRate;
        this.maxDurability       = maxDurability;
        this.currentDurability   = currentDurability;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    public int    getArmorRating()         { return armorRating; }
    public double getBlockRate()           { return blockRate; }
    public int    getCriticalArmorRating() { return criticalArmorRating; }
    public double getCriticalBlockRate()   { return criticalBlockRate; }
    public int    getMaxDurability()       { return maxDurability; }
    public int    getCurrentDurability()   { return currentDurability; }

    // -------------------------------------------------------------------------
    // Mutation helpers (return new instances — ArmorData is effectively immutable)
    // -------------------------------------------------------------------------

    /** Returns a copy with currentDurability replaced. */
    public ArmorData withDurability(int newCurrent)
    {
        return new ArmorData(armorRating, blockRate, criticalArmorRating, criticalBlockRate,
                maxDurability, Math.max(0, Math.min(newCurrent, maxDurability)));
    }
}