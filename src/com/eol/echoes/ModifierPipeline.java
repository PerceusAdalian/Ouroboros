package com.eol.echoes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.eol.echoes.config.EchoConfig;
import com.eol.echoes.config.RarityBand;
import com.eol.echoes.modifiers.ActiveModifier;
import com.eol.echoes.modifiers.PassiveModifier;
import com.eol.echoes.modifiers.WeaponModifier;
import com.eol.enums.CombatStat;
import com.eol.enums.EchoForm;
import com.eol.enums.WeaponModifierCondition;
import com.ouroboros.enums.Rarity;

/**
 * ModifierPipeline is the gacha-style roller that generates a List of WeaponModifiers
 * for a procedurally forged Echo.
 *
 * Pipeline per slot:
 *   1. Read the RarityBand to determine slot count and magnitude range
 *   2. Per slot: roll Active vs Passive via band.rollIsActive()
 *   3. Active:   roll a CombatStat from the eligible pool, roll magnitude, roll isPercent
 *   4. Passive:  roll an effectKey from the eligible pool for the EchoForm, roll magnitude
 *   5. Roll a WeaponModifierCondition appropriate for the modifier type
 *
 * Modifier pools are filtered by EchoForm so that ranged-specific passives (e.g. arrow consumption)
 * don't appear on swords, and melee passives don't appear on bows.
 *
 * ModifierPipeline is stateless — call roll() directly.
 */
public final class ModifierPipeline
{
    private ModifierPipeline() {}

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    /**
     * Rolls and returns a list of WeaponModifiers for the given rarity and weapon form.
     * The list size matches the RarityBand's modifierSlots count.
     */
    public static List<WeaponModifier> roll(Rarity rarity, EchoForm form)
    {
        RarityBand band = EchoConfig.get().getRarityBand(rarity);
        List<WeaponModifier> result = new ArrayList<>();

        for (int i = 0; i < band.modifierSlots(); i++)
        {
            WeaponModifier modifier = band.rollIsActive()
                    ? rollActive(band, form)
                    : rollPassive(band, form);
            result.add(modifier);
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Active modifier rolling
    // -------------------------------------------------------------------------

    private static ActiveModifier rollActive(RarityBand band, EchoForm form)
    {
        CombatStat stat   = rollCombatStat(form);
        double magnitude  = band.rollMagnitude();
        boolean isPercent = rollIsPercent(stat);
        WeaponModifierCondition condition = rollCondition(true);
        

        // Flat attack magnitudes need to be scaled up from the [0.0-1.0] band range
        // to a meaningful flat value. We scale by a factor relative to the material tier
        // ceiling. Since we don't have material context here, we use a flat scalar of 20
        // (roughly half of a mid-tier Netherite weapon's attack floor) as a reasonable
        // ceiling for rolled flat bonuses. This keeps them impactful but not overwhelming.
        if (!isPercent && stat == CombatStat.ATTACK)
            magnitude = Math.round(magnitude * 20);

        return new ActiveModifier(condition, stat, magnitude, isPercent);
    }

    /**
     * Rolls which CombatStat this active modifier targets.
     * EchoForm context could be used to bias certain stats (e.g. bows prefer CritRate)
     * but for now all four stats are equally weighted.
     */
    private static CombatStat rollCombatStat(EchoForm form)
    {
        CombatStat[] pool = switch (form)
        {
            // Ranged weapons bias toward crit and attack; attack rating is handled
            // differently for bows (draw speed) so we exclude ATTACK_RATING here
            // to avoid generating nonsensical "draw speed" active modifiers.
            case HOE, SHOVEL, PICKAXE ->
                new CombatStat[]{ CombatStat.ATTACK, CombatStat.CRIT_RATE };

            // Melee and default: all four stats available
            default -> CombatStat.values();
        };

        return pool[ThreadLocalRandom.current().nextInt(pool.length)];
    }

    /**
     * Determines if the rolled magnitude is a percentage or flat bonus.
     * CritRate and CritModifier are always percentage/additive by nature.
     * Attack has a 60% chance to be a percentage bonus, 40% flat.
     * AttackRating is always flat (it's a multiplier, not a percentage).
     */
    private static boolean rollIsPercent(CombatStat stat)
    {
        return switch (stat)
        {
            case CRIT_RATE, CRIT_MODIFIER -> true;
            case ATTACK_RATING            -> false;
            case ATTACK                   -> ThreadLocalRandom.current().nextDouble() < 0.60;
        };
    }

    // -------------------------------------------------------------------------
    // Passive modifier rolling
    // -------------------------------------------------------------------------

    private static PassiveModifier rollPassive(RarityBand band, EchoForm form)
    {
        String effectKey = rollEffectKey(form);
        double magnitude = rollPassiveMagnitude(effectKey, band);
        WeaponModifierCondition condition = rollCondition(false);

        return new PassiveModifier(condition, effectKey, magnitude);
    }

    /**
     * Rolls an effectKey from the pool valid for the given EchoForm.
     * Ranged-only effects (arrow consumption) are excluded from melee forms,
     * and melee-only effects are excluded from tools.
     */
    private static String rollEffectKey(EchoForm form)
    {
        List<String> pool = new ArrayList<>(UNIVERSAL_PASSIVES);

        pool.addAll(switch (form)
        {
            case SWORD, AXE, SPEAR -> MELEE_PASSIVES;
            case HOE, SHOVEL, PICKAXE -> TOOL_PASSIVES;
        });

        return pool.get(ThreadLocalRandom.current().nextInt(pool.size()));
    }

    /**
     * Some passive effects have a meaningful magnitude (proc chance, set value),
     * others are binary (fire and forget on condition). This determines which.
     */
    private static double rollPassiveMagnitude(String effectKey, RarityBand band)
    {
        return switch (effectKey)
        {
            // Proc-chance passives: magnitude is the chance [0.0-1.0]
            case "apply_burn", "apply_poison", "apply_expose",
                 "apply_slowness", "apply_stun", "apply_fatigue" ->
                band.rollMagnitude();

            // Set-value passives: magnitude is the new value
            case "set_attack_rate" ->
                1.0 + ThreadLocalRandom.current().nextDouble(); // 1.0 - 2.0 range

            // Binary passives: magnitude is unused
            default -> 0.0;
        };
    }

    // -------------------------------------------------------------------------
    // Condition rolling
    // -------------------------------------------------------------------------

    /**
     * Rolls a WeaponModifierCondition appropriate for the modifier type.
     * Active modifiers favor entity-type conditions (PVE, UNDEAD, etc.)
     * Passive modifiers favor world/environment conditions.
     */
    private static WeaponModifierCondition rollCondition(boolean isActive)
    {
        WeaponModifierCondition[] pool = isActive ? ACTIVE_CONDITIONS : PASSIVE_CONDITIONS;
        return pool[ThreadLocalRandom.current().nextInt(pool.length)];
    }

    // -------------------------------------------------------------------------
    // Condition and effect pools
    // -------------------------------------------------------------------------

    /** Conditions that make sense for active stat modifiers (combat-oriented). */
    private static final WeaponModifierCondition[] ACTIVE_CONDITIONS =
    {
        WeaponModifierCondition.PVE,
        WeaponModifierCondition.PVP,
        WeaponModifierCondition.UNDEAD,
        WeaponModifierCondition.LIVING,
        WeaponModifierCondition.FLYING,
        WeaponModifierCondition.AQUATIC,
        WeaponModifierCondition.BUGS,
        WeaponModifierCondition.RAID,
        WeaponModifierCondition.END,
        WeaponModifierCondition.NETHER,
    };

    /** Conditions that make sense for passive/environmental modifiers. */
    private static final WeaponModifierCondition[] PASSIVE_CONDITIONS =
    {
        WeaponModifierCondition.PASSIVE,
        WeaponModifierCondition.OVERWORLD,
        WeaponModifierCondition.DURING_DAY,
        WeaponModifierCondition.DURING_NIGHT,
        WeaponModifierCondition.CLEAR_WEATHER,
        WeaponModifierCondition.RAINY_WEATHER,
        WeaponModifierCondition.STORMY_WEATHER,
        WeaponModifierCondition.NETHER,
        WeaponModifierCondition.END,
        WeaponModifierCondition.COLDBIOMES,
        WeaponModifierCondition.HOTBIOMES,
    };

    /** Passive effects available to all weapon/tool forms. */
    private static final List<String> UNIVERSAL_PASSIVES = Arrays.asList(
        "apply_burn",
        "apply_poison",
        "apply_expose",
        "apply_slowness",
        "apply_fatigue",
        "movement_speed",
        "knockback"
    );

    /** Passive effects exclusive to melee weapon forms (SWORD, AXE, SPEAR). */
    private static final List<String> MELEE_PASSIVES = Arrays.asList(
        "apply_stun",
        "set_attack_rate"
    );

    /** Passive effects for tool forms (HOE, SHOVEL, PICKAXE). */
    private static final List<String> TOOL_PASSIVES = List.of(
        // Tools share universal passives only for now.
        // Extend here when tool-specific passives are designed.
    );
}
