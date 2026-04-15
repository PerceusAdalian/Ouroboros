package com.eol.echoes.records;

/**
 * MaterialStatRange is an immutable snapshot of one EchoMaterial tier's
 * base stat ranges, loaded from echo_config.yml by EchoConfig.
 *
 * StatResolver reads this to roll the base EchoData for a forged Echo.
 * Attack Rating and Durability are NOT here — those come from BindingStatBlock.
 */
public record MaterialStatRange(double baseAttackMin, double baseAttackMax, double critRateBase, double critModifierCeiling)
{
    /**
     * Rolls a random base attack value within this material's min/max range.
     */
    public double rollBaseAttack()
    {
        double range = baseAttackMax - baseAttackMin;
        return baseAttackMin + (Math.random() * range);
    }

    /**
     * Rolls a crit modifier between 1.0 (no bonus) and this material's ceiling.
     * A minimum of 1.0 ensures the modifier is never a penalty.
     */
    public double rollCritModifier()
    {
        double range = critModifierCeiling - 1.0;
        return 1.0 + (Math.random() * range);
    }
}
