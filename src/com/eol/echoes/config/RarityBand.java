package com.eol.echoes.config;

/**
 * RarityBand is an immutable snapshot of one rarity tier's generation parameters,
 * loaded from echo_config.yml by EchoConfig.
 *
 * The ModifierPipeline reads this to know:
 *   - how many modifier slots to roll
 *   - what probability a slot becomes an ActiveModifier vs PassiveModifier
 *   - what magnitude range to roll within for any modifier on this tier
 */
public record RarityBand(int modifierSlots, double activeModifierChance, double magnitudeFloor, double magnitudeCeiling)
{
    /**
     * Rolls a random magnitude within this band's floor/ceiling range.
     * The result is suitable for use as a modifier magnitude directly.
     */
    public double rollMagnitude()
    {
        double range = magnitudeCeiling - magnitudeFloor;
        return magnitudeFloor + (Math.random() * range);
    }

    /**
     * Returns true if a randomly rolled slot should be an ActiveModifier
     * given this band's active_modifier_chance.
     */
    public boolean rollIsActive()
    {
        return Math.random() < activeModifierChance;
    }
}
