package com.eol.echoes;

import java.util.ArrayList;
import java.util.List;

import com.eol.echoes.config.EchoConfig;
import com.eol.echoes.records.ActiveModifier;
import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.MaterialStatRange;
import com.eol.echoes.records.Modifier;
import com.eol.enums.EchoMaterial;
import com.ouroboros.abilities.instances.ObsAbility;
import com.ouroboros.utils.PrintUtils;

/**
 * EchoLoreBuilder assembles the lore List<String> for a forged Echo ItemStack
 * from its EchoManifest.
 * 
 * Example:
 * Rarity: «◆◆◆◆◆»
 *
 * Base Stats:
 * Attack Damage ........ 3.85
 * Attack Rating ........ 1.25
 * Critical Rating ...... 7%
 * Critical Modifier .... x1.1
 *
 *
 *   CRP Modifiers:
 *   <modifier lore lines>
 *
 *   Elementium Slot: <SlotType>     (omitted if NO_SLOT)
 *	 Skill: | Core Memory:
 *   Skill/Core Memory <description>
 *   
 *   Echo ID: Σ_<echoId>
 */
public final class EchoLoreBuilder
{
	// Constructor
    private EchoLoreBuilder() {}
 
    /**
     * Builds lore for a procedurally forged Echo.
     * Requires EchoMaterial to evaluate roll quality for stat coloring.
     */
    public static List<String> build(EchoManifest manifest, EchoMaterial echoMaterial)
    {
        List<String> lore = new ArrayList<>();
 
        MaterialStatRange range = EchoConfig.get().getMaterialStats(echoMaterial);
 
        // --- Rarity ---
        lore.add(PrintUtils.assignRarity(manifest.rarity()));
        lore.add("");
 
        // --- Base Stats ---
        lore.add(PrintUtils.ColorParser("&r&f&lBase Stats&r&f:"));
 
        EchoData stats = manifest.baseStats();
 
        lore.add(statLine("Attack Damage",
                rollQualityColor(stats.getAttack(), range.baseAttackMin(), range.baseAttackMax())
                + formatStat(stats.getAttack(), false)));
 
        // Attack Rating: binding-sourced. Color relative to [0.5, 2.0] as a universal AR range.
        lore.add(statLine("Attack Rate",
                rollQualityColor(stats.getAttackRating(), 0.5, 2.0)
                + formatStat(stats.getAttackRating(), false)));
 
        // Crit Rate: color relative to [0.0, 1.0]
        lore.add(statLine("Critical Rating",
                rollQualityColor(stats.getCritRate(), 0.0, 1.0)
                + formatPercent(stats.getCritRate())));
 
        // Crit Modifier: color relative to [1.0, material ceiling]
        lore.add(statLine("Critical Modifier",
                rollQualityColor(stats.getCritModifier(), 1.0, range.critModifierCeiling())
                + "x" + formatDecimal(stats.getCritModifier())));
 
        lore.add("");
 
        // --- CRP Modifiers ---
        if (!manifest.modifiers().isEmpty())
        {
            lore.add(PrintUtils.ColorParser("&r&f&lModifiers&r&f:"));
            for (Modifier mod : manifest.modifiers())
                lore.add(colorModifier(mod));
            lore.add("");
        }
 
        // --- Elementium Slot ---
        if (manifest.hasElementiumSlot())
        {
            String slotLabel = manifest.slotType().getElement();
            lore.add(PrintUtils.ColorParser("&r&f&lElementium Slot&r&f: " + slotLabel));
        }
 
        // --- Locked Ability (EOL only) ---
        if (manifest.hasLockedAbility())
        {
            ObsAbility ability = ObsAbility.fromInternalName(manifest.lockedAbilityKey());
            String abilityName = ability != null ? ability.getDisplayName() : manifest.lockedAbilityKey(); // fallback to key if not registered yet
            lore.add(PrintUtils.ColorParser("&r&f&lCore Memory&r&f: " + PrintUtils.getElementTypeColor(ability.getElementType()) + "&l" + abilityName + " &r&7(Locked)"));
            for (String line : ability.getDescription())
            	lore.add(line);
        }
        else
        {
        	ObsAbility ability = ObsAbility.fromInternalName(manifest.equippedAbilityKey());
        	String abilityName = ability != null ? ability.getDisplayName() : manifest.equippedAbilityKey();
        	lore.add(PrintUtils.ColorParser("&r&fSkill: "+ PrintUtils.getElementTypeColor(ability.getElementType()) + "&l" + abilityName));
        	for (String line : ability.getDescription())
        		lore.add(line);
        }
 
        if (manifest.hasElementiumSlot() || manifest.hasLockedAbility()) lore.add("");
 
        // --- Echo ID ---
        lore.add(PrintUtils.ColorParser("&r&7Echo ID: Σ_" + manifest.echoId()));
 
        return lore;
    }
 
    /**
     * Overload for contexts where EchoMaterial is unavailable (e.g. EOL hand-authored items).
     * Falls back to uncolored gold values.
     */
    public static List<String> build(EchoManifest manifest)
    {
        List<String> lore = new ArrayList<>();
 
        lore.add(PrintUtils.assignRarity(manifest.rarity()));
        lore.add("");
 
        lore.add(PrintUtils.ColorParser("&r&f&lBase Stats&r&f:"));
        EchoData stats = manifest.baseStats();
        lore.add(statLine("Attack Damage",    "&6" + formatStat(stats.getAttack(), false)));
        lore.add(statLine("Attack Rating",    "&6" + formatStat(stats.getAttackRating(), false)));
        lore.add(statLine("Critical Rating",  "&6" + formatPercent(stats.getCritRate())));
        lore.add(statLine("Critical Modifier","&6x" + formatDecimal(stats.getCritModifier())));
        lore.add("");
 
        if (!manifest.modifiers().isEmpty())
        {
            lore.add(PrintUtils.ColorParser("&r&f&lModifiers&r&f:"));
            for (Modifier mod : manifest.modifiers())
                lore.add(colorModifier(mod));
            lore.add("");
        }
 
        if (manifest.hasElementiumSlot())
        {
            lore.add(PrintUtils.ColorParser("&r&f&lElementium Slot&r&f: " + manifest.slotType().getElement()));
        }
 
        if (manifest.hasLockedAbility())
        {
            ObsAbility ability = ObsAbility.fromInternalName(manifest.lockedAbilityKey());
            String abilityName = ability != null ? ability.getDisplayName() : manifest.lockedAbilityKey();
            lore.add(PrintUtils.ColorParser("&r&f&lCore Memory&r&f: " + PrintUtils.getElementTypeColor(ability.getElementType()) + "&l" + abilityName + " &r&7(Locked)"));
            for (String line : ability.getDescription())
            	lore.add(line);
        }
 
        if (manifest.hasElementiumSlot() || manifest.hasLockedAbility()) lore.add("");
 
        lore.add(PrintUtils.ColorParser("&r&7Echo ID: Σ_" + manifest.echoId()));
 
        return lore;
    }
 
    /**
     * Returns a color based on where a rolled value sits within its possible range for that material tier:
     *
     *   Top 10%    → &b Blue   (Highest)
     *   60–90%     → &a Green  (High)
     *   30–60%     → &e Yellow (Middle)
     *   10–30%     → &6 Orange (Low)
     *   Bottom 10% → &c Red    (Lowest)
     */
    private static String rollQualityColor(double value, double min, double max)
    {
        if (max <= min) return "&6"; // degenerate range, default orange
        double ratio = (value - min) / (max - min);
        ratio = Math.max(0.0, Math.min(1.0, ratio));
 
        if (ratio >= 0.90) return "&b";
        if (ratio >= 0.60) return "&a";
        if (ratio >= 0.30) return "&e";
        if (ratio >= 0.10) return "&6";
        return "&c";
    }
 
    // -------------------------------------------------------------------------
    // Formatting helpers
    // -------------------------------------------------------------------------
 
    /**
     * Pads the stat label to a proportional dot/space leader that still allows for eye-tracking along
     * the horizontal length of the full stat label.
     * 
     * Example:
     * Attack Damage ........ 3.85
	 * Attack Rating ........ 1.25
	 * Critical Rating ...... 7%
	 * Critical Modifier .... x1.1
     *
     * This hopefully provides a more readable solution..
     */
    private static String statLine(String label, String value)
    {
        
    	int leaderLength = Math.max(1, 22 - label.length());
        String leader = " &8" + ".".repeat(leaderLength) + " ";

        return PrintUtils.ColorParser("&r&7" + label + leader + "&6" + value);
    }
 
    /**
     * Colors a modifier's lore label based on whether it's active or passive.
     * Active modifiers: green (stat bonuses, matching the mockup)
     * Passive modifiers: purple (side effects/triggers, matching the mockup)
     */
    private static String colorModifier(Modifier mod)
    {
        String label = mod.loreLabel();
        String color;
 
        if (mod.isActive())
        {
            // Cast is safe — isActive() guarantees ActiveModifier
            ActiveModifier active = (ActiveModifier) mod;
            color = active.magnitude() >= 0 ? "&a" : "&c";
        }
        else
        {
            // Passive modifiers: blue, matching the ">" trigger style in the mockups
            color = "&b";
        }
 
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
