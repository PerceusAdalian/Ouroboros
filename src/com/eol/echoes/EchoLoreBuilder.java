package com.eol.echoes;

import java.util.ArrayList;
import java.util.List;

import com.eol.echoes.modifiers.WeaponModifier;
import com.ouroboros.utils.PrintUtils;

/**
 * EchoLoreBuilder assembles the lore List<String> for a forged Echo ItemStack
 * from its EchoManifest, matching the display style seen in the mockups.
 *
 * Lore structure (mirrors the mockup screenshots):
 *
 *   Rarity: «◆◆◆◆◆»
 *
 *   Base Stats:
 *   Attack Damage   | <value>
 *   Attack Rating   | <value>
 *   Critical Rating | <value>%
 *   Critical Modifier | x<value>
 *
 *   CRP Modifiers:
 *   <modifier lore lines>
 *
 *   Elementium Slot: <SlotType>     (omitted if NO_SLOT)
 *
 *   Echo ID: Σ_<echoId>
 */
public final class EchoLoreBuilder
{
    private EchoLoreBuilder() {}

    public static List<String> build(EchoManifest manifest)
    {
        List<String> lore = new ArrayList<>();

        // --- Rarity ---
        lore.add(PrintUtils.assignRarity(manifest.rarity()));
        lore.add("");

        // --- Base Stats ---
        lore.add(PrintUtils.ColorParser("&r&f&lBase Stats&r&f:"));
        
        /*
         *  @ TODO Add colors for the Base Stats depending on lowest, mid, or high or highest rolls:
         *  Blue   - Highest
         *  Green  - High
         *  Yellow - Middle
         *  Orange - Low
         *  Red    - Lowest	
         */
        
        lore.add(statLine("Attack Damage",    formatStat(manifest.baseStats().getAttack(), false)));
        lore.add(statLine("Attack Rating",    formatStat(manifest.baseStats().getAttackRating(), false)));
        lore.add(statLine("Critical Rating",  formatPercent(manifest.baseStats().getCritRate())));
        lore.add(statLine("Critical Modifier","x" + formatDecimal(manifest.baseStats().getCritModifier())));
        lore.add("");

        // --- CRP Modifiers ---
        if (!manifest.modifiers().isEmpty())
        {
            lore.add(PrintUtils.ColorParser("&r&f&lModifiers&r&f:"));
            for (WeaponModifier mod : manifest.modifiers())
                lore.add(colorModifier(mod));
            lore.add("");
        }

        // --- Elementium Slot ---
        if (manifest.hasElementiumSlot())
        {
            String slotLabel = manifest.slotType().getElement();
            lore.add(PrintUtils.ColorParser("&r&f&lElementium Slot&r&f: " + slotLabel));
            lore.add("");
        }

        // --- Echo ID ---
        lore.add(PrintUtils.ColorParser("&r&7Echo ID: Σ_" + manifest.echoId()));

        return lore;
    }

    // -------------------------------------------------------------------------
    // Formatting helpers
    // -------------------------------------------------------------------------

    /**
     * Formats a stat line with pipe-aligned label/value, matching the mockup style:
     * "Attack Damage   | 15"
     */
    private static String statLine(String label, String value)
    {
        // Pad label to 18 chars for alignment
        String padded = String.format("%-18s", label);
        return PrintUtils.ColorParser("&r&f" + padded + "| &6" + value);
    }

    /**
     * @TODO Add dynamic coloring based on whether it's a low or high roll; Green - Best, Orange - Not Lowest/Highest, NotRoll, Red - Lowest Possible
     * Colors a modifier's lore label based on whether it's active or passive.
     * Active modifiers: green (stat bonuses, matching the mockup)
     * Passive modifiers: purple (side effects/triggers, matching the mockup)
     */
    private static String colorModifier(WeaponModifier mod)
    {
        String label = mod.loreLabel();
        // Active → green; Passive → starts with ">" which mockups show in purple/pink
        String color = mod.isActive() ? "&a" : "&b";
        return PrintUtils.ColorParser("&r" + color + label);
    }

    private static String formatStat(double value, boolean isPercent)
    {
        if (isPercent) return formatPercent(value);
        // Whole numbers display without decimal
        if (value == (long) value) return String.valueOf((long) value);
        return String.format("%.2f", value);
    }

    private static String formatPercent(double value)
    {
        return (int)(value * 100) + "%";
    }

    private static String formatDecimal(double value)
    {
        if (value == (long) value) return String.valueOf((long) value) + ".0";
        return String.format("%.1f", value);
    }
}
