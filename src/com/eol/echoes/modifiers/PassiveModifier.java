package com.eol.echoes.modifiers;

import com.eol.enums.WeaponModifierCondition;

/**
 * PassiveModifier triggers a side effect when its condition is satisfied.
 * Unlike ActiveModifier, it does not mutate a core stat directly.
 *
 * effectKey is a string identifier that maps to a registered effect handler
 * in the combat pipeline (e.g. "apply_burn", "apply_expose", "movement_speed").
 * This keeps PassiveModifier data-only and decouples it from Bukkit event logic.
 *
 * magnitude is optional context for the effect (e.g. proc chance, duration scalar).
 * A magnitude of 0.0 means the effect fires unconditionally on condition match
 * with no scalar applied (the effect handler decides its own defaults).
 *
 * Examples from the mockups:
 *   ">Arrows Apply Expose"                  → effectKey="apply_expose",    magnitude=0.0
 *   ">35% Chance to Ignore Arrow Consumption"→ effectKey="ignore_arrow",   magnitude=0.35
 *   ">Atk Rate become 2.0 in The End"       → effectKey="set_attack_rate", magnitude=2.0,  condition=END
 *   ">Increased Movement Speed (Nether)"    → effectKey="movement_speed",  magnitude=0.0,  condition=NETHER
 */
public record PassiveModifier(WeaponModifierCondition condition, String effectKey, double magnitude) implements WeaponModifier
{
    @Override
    public boolean isActive()
    {
        return false;
    }

    @Override
    public String loreLabel()
    {
        // Passive modifier lore uses the ">" prefix convention seen in the mockups.
        // The effectKey is transformed into a human-readable label here.
        // For hand-authored EOL modifiers, this can be overridden by supplying
        // a custom display string via PassiveModifier.withLabel().
        String base = ">" + formatEffect();

        if (condition != WeaponModifierCondition.PASSIVE)
            base += " (" + formatCondition() + ")";

        return base;
    }

    private String formatEffect()
    {
        // Simple key-to-display mapping. Extend as effects are added.
        return switch (effectKey)
        {
            case "apply_expose"    -> "Attacks Apply Expose";
            case "apply_burn"      -> "Attacks Apply Burn";
            case "apply_poison"    -> "Attacks Apply Poison";
            case "apply_slowness"  -> "Attacks Apply Slowness";
            case "apply_fatigue"   -> "Attacks Apply Fatigue";
            case "apply_stun"      -> "Attacks Apply Stun";
            case "ignore_arrow"    -> (int)(magnitude * 100) + "% Chance to Ignore Arrow Consumption";
            case "set_attack_rate" -> "Atk Rate becomes " + magnitude;
            case "movement_speed"  -> "Increased Movement Speed";
            case "knockback"       -> "Increased Knockback";
            default                -> effectKey; // fallback: raw key shown as-is
        };
    }

    private String formatCondition()
    {
        return switch (condition)
        {
            case PVE       -> "PVE";
            case PVP       -> "PVP";
            case PASSIVE   -> "Passive";
            case UNDEAD    -> "PVE: Undead";
            case LIVING    -> "PVE: Living";
            case FLYING    -> "PVE: Flying";
            case AQUATIC   -> "PVE: Aquatic";
            case BUGS      -> "PVE: Bugs";
            case RAID      -> "PVE: Raid";
            case OVERWORLD -> "Overworld";
            case DURING_DAY   -> "Overworld: Daytime";
            case DURING_NIGHT -> "Overworld: Nighttime";
            case CLEAR_WEATHER  -> "Overworld: Clear";
            case RAINY_WEATHER  -> "Overworld: Rain";
            case STORMY_WEATHER -> "Overworld: Storm";
            case NETHER       -> "World: The Nether";
            case END          -> "World: The End";
            case COLDBIOMES   -> "Biome: Cold";
            case HOTBIOMES    -> "Biome: Hot";
            case GENERICBIOMES -> "Biome";
        };
    }
}
