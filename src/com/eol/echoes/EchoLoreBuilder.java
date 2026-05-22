package com.eol.echoes;

import java.util.ArrayList;
import java.util.List;

import com.eol.echoes.abilities.EchoAbility;
import com.eol.echoes.config.EchoConfig;
import com.eol.echoes.records.ArmorStatBlock;
import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.MaterialStatBlock;
import com.eol.echoes.records.Modifier;
import com.eol.enums.EchoMaterial;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;

/**
 * EchoLoreBuilder assembles the lore List<String> for a forged Echo ItemStack.
 *
 * Weapon / tool Echoes: {@link #build(EchoManifest, EchoMaterial)}
 * Armor Echoes:         {@link #buildArmor(EchoManifest, EchoMaterial)}
 *
 * Active modifiers render via their own {@link Modifier#loreLabel()} — this class
 * does not need to know which concrete type they are, only whether to call the
 * shared {@link #appendModifiers} helper.
 */
public final class EchoLoreBuilder
{
    private EchoLoreBuilder() {}

    // =========================================================================
    // Weapon / tool lore
    // =========================================================================

    public static List<String> build(EchoManifest manifest, EchoMaterial echoMaterial)
    {
        List<String> lore = new ArrayList<>();
        boolean isBow = echoMaterial == EchoMaterial.BOW || echoMaterial == EchoMaterial.CROSSBOW;

        MaterialStatBlock range = EchoConfig.get().getMaterialStats(echoMaterial);

        lore.add(PrintUtils.assignRarity(manifest.rarity()));
        lore.add("");

        lore.add(PrintUtils.ColorParser("&r&f&lBase Stats&r&f:"));
        EchoData stats = manifest.baseStats();

        lore.add(statLine(isBow ? Symbols.BOW+" Ranged Damage" : Symbols.SWORDS+" Attack Damage",
                rollQualityColor(stats.getAttack(), range.baseAttackMin(), range.baseAttackMax())
                        + formatStat(stats.getAttack(), false)));

        lore.add(statLine(isBow ? "Draw Rate" : "Attack Rate",
                rollQualityColor(stats.getAttackRating(), range.baseAttackRateMin(), range.baseAttackRateMax())
                        + formatStat(stats.getAttackRating(), false)));

        lore.add(statLine(Symbols.CRITICAL+" Critical Rating",
                rollQualityColor(stats.getCritRate(), 0.0, 1.0)
                        + formatPercent(stats.getCritRate())));

        lore.add(statLine(Symbols.CRITICAL+" Critical Modifier",
                rollQualityColor(stats.getCritModifier(), 1.0, range.critModifierCeiling())
                        + "x" + formatDecimal(stats.getCritModifier())));

        lore.add("");

        appendModifiers(lore, manifest);
        appendElementiumSlot(lore, manifest);
        appendAbility(lore, manifest);
        appendDurability(lore, manifest);
        appendEchoId(lore, manifest);

        return lore;
    }

    /**
     * Fallback for weapon/tool Echoes where EchoMaterial is unavailable (e.g. EOL hand-authored).
     */
    public static List<String> build(EchoManifest manifest, boolean isBow)
    {
        List<String> lore = new ArrayList<>();

        lore.add(PrintUtils.assignRarity(manifest.rarity()));
        lore.add("");

        lore.add(PrintUtils.ColorParser("&r&f&lBase Stats&r&f:"));
        EchoData stats = manifest.baseStats();
        lore.add(statLine(isBow ? Symbols.BOW+" Ranged Damage" : Symbols.SWORDS+" Attack Damage", "&6" + formatStat(stats.getAttack(), false)));
        lore.add(statLine(isBow ? "Draw Rate"       : "Attack Rate",   "&6" + formatStat(stats.getAttackRating(), false)));
        lore.add(statLine(Symbols.CRITICAL+" Critical Rating", "&6" + formatPercent(stats.getCritRate())));
        lore.add(statLine(Symbols.CRITICAL+" Critical Modifier", "&6x" + formatDecimal(stats.getCritModifier())));
        lore.add("");

        appendModifiers(lore, manifest);
        appendElementiumSlot(lore, manifest);
        appendAbility(lore, manifest);
        appendDurability(lore, manifest);
        appendEchoId(lore, manifest);

        return lore;
    }

    // =========================================================================
    // Armor lore
    // =========================================================================

    /**
     * Builds lore for a procedurally forged armor Echo.
     * Armor Echoes have no ability or elementium slot section.
     */
    public static List<String> buildArmor(EchoManifest manifest, EchoMaterial echoMaterial)
    {
        List<String> lore = new ArrayList<>();

        ArmorStatBlock range = EchoConfig.get().getArmorMaterialStats(echoMaterial);
        ArmorData      armor = manifest.armorStats();

        lore.add(PrintUtils.assignRarity(manifest.rarity()));
        lore.add("");

        lore.add(PrintUtils.ColorParser("&r&f&lDefense Stats&r&f:"));

        lore.add(statLine(Symbols.ARMOR+" Armor Rating",
                rollQualityColor(armor.getArmorRating(), range.armorRatingMin(), range.armorRatingMax())
                        + armor.getArmorRating()));

        lore.add(statLine("Block Rate",
                rollQualityColor(armor.getBlockRate(), range.blockRateMin(), range.blockRateMax())
                        + formatPercent(armor.getBlockRate())));

        lore.add(statLine(Symbols.CRITICAL+" Crit Mitigation",
                rollQualityColor(armor.getCriticalArmorRating(), range.critArmorRatingMin(), range.critArmorRatingMax())
                        + armor.getCriticalArmorRating()));

        lore.add(statLine(Symbols.CRITICAL+" Crit Block Rate",
                rollQualityColor(armor.getCriticalBlockRate(), range.critBlockRateMin(), range.critBlockRateMax())
                        + formatPercent(armor.getCriticalBlockRate())));

        lore.add("");

        appendModifiers(lore, manifest);
        appendDurability(lore, manifest);
        appendEchoId(lore, manifest);

        return lore;
    }

    // =========================================================================
    // Shared section builders
    // =========================================================================

    private static void appendModifiers(List<String> lore, EchoManifest manifest)
    {
        if (!manifest.modifiers().isEmpty())
        {
            lore.add(PrintUtils.ColorParser("&r&f&lModifiers&r&f:"));
            for (Modifier mod : manifest.modifiers())
                lore.add(PrintUtils.ColorParser(mod.loreLabel()));
            lore.add("");
        }
    }

    private static void appendElementiumSlot(List<String> lore, EchoManifest manifest)
    {
        if (manifest.hasElementiumSlot())
            lore.add(PrintUtils.ColorParser("&r&f&lElementium Slot&r&f: " + manifest.slotType().getElement()));
    }

    private static void appendAbility(List<String> lore, EchoManifest manifest)
    {
        if (manifest.hasLockedAbility())
        {
            EchoAbility ability = EchoAbility.fromInternalName(manifest.lockedAbilityKey());
            String abilityName  = ability != null ? ability.getDisplayName() : manifest.lockedAbilityKey();
            String elementColor = ability != null ? PrintUtils.getElementTypeColor(ability.getElementType()) : "&f";
            lore.add(PrintUtils.ColorParser("&r&bCore Memory&r&f: " + elementColor + "&l" + abilityName + " &r&7(Locked)"));
            if (ability != null)
            {
                if (ability.getDurabilityCost() > 0)
                    lore.add(PrintUtils.assignDurabilityCost(ability.getDurabilityCost()));
                for (String line : ability.getDescription())
                    lore.add(PrintUtils.ColorParser(line));
                lore.add("");
            }
        }
        else
        {
            EchoAbility ability = EchoAbility.fromInternalName(manifest.equippedAbilityKey());
            if (ability != null)
            {
                String elementColor = PrintUtils.getElementTypeColor(ability.getElementType());
                lore.add(PrintUtils.ColorParser("&r&fSkill: " + elementColor + ability.getDisplayName()));
                if (ability.getDurabilityCost() > 0)
                    lore.add(PrintUtils.assignDurabilityCost(ability.getDurabilityCost()));
                for (String line : ability.getDescription())
                    lore.add(PrintUtils.ColorParser(line));
                lore.add("");
            }
        }
    }

    private static void appendDurability(List<String> lore, EchoManifest manifest)
    {
        int current = manifest.getCurrentDurability();
        int max     = manifest.getMaxDurability();
        lore.add(PrintUtils.ColorParser("&b&lDurability&r&f: "
                + rollQualityColor(current, 0, max) + current + "&r&7/" + max));
    }

    private static void appendEchoId(List<String> lore, EchoManifest manifest)
    {
        lore.add(PrintUtils.ColorParser("&r&7Echo ID: Σ_" + manifest.echoId()));
    }

    // =========================================================================
    // Color helpers
    // =========================================================================

    /**
     * Returns a color code based on where value sits in [min, max]:
     *   ≥90% → &b blue,  ≥60% → &a green, ≥30% → &e yellow,
     *   ≥10% → &6 orange, <10% → &c red
     */
    static String rollQualityColor(double value, double min, double max)
    {
        if (max <= min) return "&6";
        double ratio = Math.max(0.0, Math.min(1.0, (value - min) / (max - min)));
        if (ratio >= 0.90) return "&b";
        if (ratio >= 0.60) return "&a";
        if (ratio >= 0.30) return "&e";
        if (ratio >= 0.10) return "&6";
        return "&c";
    }

    // =========================================================================
    // Formatting helpers
    // =========================================================================

    private static String statLine(String label, String value)
    {
        int    leaderLength = Math.max(1, 22 - label.length());
        String leader       = " &8" + ".".repeat(leaderLength) + " ";
        return PrintUtils.ColorParser("&r&7" + label + leader + "&6" + value);
    }

    private static String formatStat(double value, boolean isPercent)
    {
        if (isPercent) return formatPercent(value);
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