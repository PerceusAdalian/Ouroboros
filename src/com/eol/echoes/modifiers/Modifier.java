package com.eol.echoes.modifiers;

import com.eol.enums.WeaponModifierCondition;

/**
 * Modifier is the sealed polymorphic base for all Echo modifiers.
 *
 * There are two concrete subtypes:
 *   - ActiveModifier:  directly mutates a combat stat (ATK, AR, CritRate, CritMod)
 *                      when its condition is met.
 *   - PassiveModifier: triggers a side effect (status application, movement bonus, etc.)
 *                      when its condition is met.
 *
 * Sealed so the compiler enforces exhaustive handling in switch expressions
 * throughout the combat pipeline.
 */
public sealed interface Modifier permits ActiveModifier, PassiveModifier
{
    /**
     * The condition under which this modifier applies.
     * e.g. PVE, DURING_NIGHT, UNDEAD, OVERWORLD
     */
    WeaponModifierCondition condition();

    /**
     * Human-readable label used for lore generation on the item.
     * e.g. "+15% Crit% (PVE: Undead)"
     */
    String loreLabel();

    /**
     * Returns true if this modifier directly mutates a combat stat.
     * Implemented by ActiveModifier; PassiveModifier returns false.
     */
    boolean isActive();
}
