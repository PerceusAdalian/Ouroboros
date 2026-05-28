package com.eol.echoes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.eol.echoes.records.ActiveArmorModifier;
import com.eol.echoes.records.ActiveEchoModifier;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.eol.echoes.records.RarityBand;
import com.eol.enums.ArmorStat;
import com.eol.enums.CombatStat;
import com.eol.enums.EchoForm;
import com.eol.enums.ModifierCondition;
import com.eol.enums.PassiveEchoEffect;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.NumberUtils;

/**
 * ModifierPipeline rolls the modifier list for a procedurally forged Echo.
 *
 * Weapon / tool Echoes roll {@link ActiveEchoModifier} (CombatStat) and {@link PassiveModifier}.
 * Armor Echoes         roll {@link ActiveArmorModifier} (ArmorStat) and {@link PassiveModifier}.
 *
 * The two entry points are:
 *   {@link #roll(Rarity, EchoForm)}      — weapon/tool forms (SWORD, HATCHET, BOW, etc.)
 *   {@link #rollArmor(Rarity, EchoForm)} — armor forms (HELMET, CHESTPLATE, LEGGINGS, BOOTS)
 *
 * Both share the same passive pool logic and the same RarityBand magnitude range.
 * ModifierPipeline is stateless — call the static methods directly.
 */
public final class ModifierPipeline
{
    private ModifierPipeline() {}

    // -------------------------------------------------------------------------
    // Entry points
    // -------------------------------------------------------------------------

    /**
     * Rolls modifiers for a weapon or tool Echo.
     * Active slots produce {@link ActiveEchoModifier}; passive slots produce {@link PassiveModifier}.
     */
    public static List<Modifier> roll(Rarity rarity, EchoForm form)
    {
        RarityBand     band          = EchoConfig.get().getRarityBand(rarity);
        List<Modifier> result        = new ArrayList<>();
        Set<PassiveEchoEffect> rolled = new HashSet<>();

        for (int i = 0; i < band.modifierSlots(); i++)
        {
            Modifier modifier;
            if (band.rollIsActive())
                modifier = rollActiveWeapon(band, form, rarity);
            else
            {
                PassiveModifier passive = rollPassive(band, form, rolled);
                modifier = passive != null ? passive : rollActiveWeapon(band, form, rarity);
            }
            result.add(modifier);
        }

        return result;
    }

    /**
     * Rolls modifiers for an armor Echo.
     * Active slots produce {@link ActiveArmorModifier}; passive slots produce {@link PassiveModifier}.
     */
    public static List<Modifier> rollArmor(Rarity rarity, EchoForm armorForm)
    {
        RarityBand     band          = EchoConfig.get().getRarityBand(rarity);
        List<Modifier> result        = new ArrayList<>();
        Set<PassiveEchoEffect> rolled = new HashSet<>();

        for (int i = 0; i < band.modifierSlots(); i++)
        {
            Modifier modifier;
            if (band.rollIsActive())
                modifier = rollActiveArmor(band, rarity);
            else
            {
                PassiveModifier passive = rollPassive(band, armorForm, rolled);
                modifier = passive != null ? passive : rollActiveArmor(band, rarity);
            }
            result.add(modifier);
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Weapon active modifier rolling
    // -------------------------------------------------------------------------

    private static ActiveEchoModifier rollActiveWeapon(RarityBand band, EchoForm form, Rarity rarity)
    {
        CombatStat        stat      = rollCombatStat(form);
        boolean           isPercent = rollWeaponIsPercent(stat);
        ModifierCondition condition = rollCondition(true);

        double t         = (rarity.getRarity() - 1) / 6.0;
        double magnitude = rollBiasedMagnitude(band, t);

        double  negativeChance = NumberUtils.lerp(0.20, 0.01, t);
        boolean isNegative     = false;
        if (ThreadLocalRandom.current().nextDouble() < negativeChance)
        {
            isNegative = true;
            magnitude  = -magnitude;
        }

        if (!isPercent && stat == CombatStat.ATTACK) magnitude = Math.round(magnitude * 3);
        else magnitude = Math.round(magnitude * 100.0) / 100.0;

        return new ActiveEchoModifier(condition, stat, magnitude, isPercent, isNegative);
    }

    private static CombatStat rollCombatStat(EchoForm form)
    {
        CombatStat[] pool = switch (form)
        {
            case SCYTHE, SPADE, PICKAXE -> new CombatStat[]{ CombatStat.ATTACK, CombatStat.ATTACK_RATING };
            case BOW, CROSSBOW          -> new CombatStat[]{ CombatStat.ATTACK, CombatStat.CRIT_RATE, CombatStat.CRIT_MODIFIER };
            default                     -> CombatStat.values();
        };
        return pool[ThreadLocalRandom.current().nextInt(pool.length)];
    }

    private static boolean rollWeaponIsPercent(CombatStat stat)
    {
        return switch (stat)
        {
            case CRIT_RATE                  -> true;
            case ATTACK_RATING, CRIT_MODIFIER -> false;
            case ATTACK                     -> ThreadLocalRandom.current().nextDouble() < 0.60;
        };
    }

    // -------------------------------------------------------------------------
    // Armor active modifier rolling
    // -------------------------------------------------------------------------

    private static ActiveArmorModifier rollActiveArmor(RarityBand band, Rarity rarity)
    {
        ArmorStat         stat      = rollArmorStat();
        boolean           isPercent = rollArmorIsPercent(stat);
        ModifierCondition condition = rollCondition(true);

        double t         = (rarity.getRarity() - 1) / 6.0;
        double magnitude = rollBiasedMagnitude(band, t);

        double  negativeChance = NumberUtils.lerp(0.20, 0.01, t); 
        boolean isNegative     = false;
        if (ThreadLocalRandom.current().nextDouble() < negativeChance)
        {
            isNegative = true;
            magnitude  = -magnitude;
        }

        if (!isPercent && (stat == ArmorStat.ARMOR_RATING || stat == ArmorStat.CRITICAL_ARMOR_RATING))
            magnitude = Math.round(magnitude * 5);
        else
            magnitude = Math.round(magnitude * 100.0) / 100.0;

        return new ActiveArmorModifier(condition, stat, magnitude, isPercent, isNegative);
    }

    private static ArmorStat rollArmorStat()
    {
        ArmorStat[] pool = ArmorStat.values();
        return pool[ThreadLocalRandom.current().nextInt(pool.length)];
    }

    private static boolean rollArmorIsPercent(ArmorStat stat)
    {
        return switch (stat)
        {
            case ARMOR_RATING, CRITICAL_ARMOR_RATING -> false;
            case BLOCK_RATE, CRITICAL_BLOCK_RATE     -> true;
        };
    }

    // -------------------------------------------------------------------------
    // Passive modifier rolling (shared by both paths)
    // -------------------------------------------------------------------------

    private static PassiveModifier rollPassive(RarityBand band, EchoForm form,
                                               Set<PassiveEchoEffect> rolledPassives)
    {
        List<PassiveEchoEffect> pool = new ArrayList<>(UNIVERSAL_PASSIVES);
        pool.addAll(switch (form)
        {
            case SWORD, HATCHET, POLEARM, HAMMER, ARMAMENT -> MELEE_PASSIVES;
            case SCYTHE, SPADE, PICKAXE                    -> TOOL_PASSIVES;
            case BOW, CROSSBOW                             -> RANGED_PASSIVES;
            case HELMET, CHESTPLATE, LEGGINGS, BOOTS       -> ARMOR_PASSIVES;
        });

        pool.removeAll(rolledPassives);
        if (pool.isEmpty()) return null;

        PassiveEchoEffect effectKey = pool.get(ThreadLocalRandom.current().nextInt(pool.size()));

        // Infinity and ignore-arrow are mutually exclusive on bows
        if (effectKey == PassiveEchoEffect.INFINITY)    rolledPassives.add(PassiveEchoEffect.IGNORE_ARROW);
        else if (effectKey == PassiveEchoEffect.IGNORE_ARROW) rolledPassives.add(PassiveEchoEffect.INFINITY);

        rolledPassives.add(effectKey);

        double magnitude        = rollPassiveMagnitude(effectKey, band);
        double roundedMagnitude = Math.round(magnitude * 100.0) / 100.0;
        ModifierCondition condition = rollCondition(false);

        return new PassiveModifier(condition, effectKey, roundedMagnitude);
    }

    private static double rollPassiveMagnitude(PassiveEchoEffect effectKey, RarityBand band)
    {
        return switch (effectKey)
        {
            case EXPOSE, BURNING, POISONOUS, SLOWING,
                 FATIGUING, STUNNING, IGNORE_ARROW, KNOCKBACK -> band.rollMagnitude();
            case SET_ATTACK_RATE -> 1.0 + ThreadLocalRandom.current().nextDouble();
            default              -> 0.0;
        };
    }

    // -------------------------------------------------------------------------
    // Condition rolling
    // -------------------------------------------------------------------------

    private static ModifierCondition rollCondition(boolean isActive)
    {
        ModifierCondition[] pool = isActive ? ACTIVE_CONDITIONS : PASSIVE_CONDITIONS;
        return pool[ThreadLocalRandom.current().nextInt(pool.length)];
    }

    // -------------------------------------------------------------------------
    // Shared helpers
    // -------------------------------------------------------------------------

    private static double rollBiasedMagnitude(RarityBand band, double t)
    {
        double floor   = band.magnitudeFloor();
        double ceiling = band.magnitudeCeiling();
        double mean    = NumberUtils.lerp(floor, ceiling, t);
        double spread  = (ceiling - floor) * 0.15;
        double roll    = mean + (ThreadLocalRandom.current().nextGaussian() * spread);
        return Math.max(floor, Math.min(ceiling, roll));
    }

    // -------------------------------------------------------------------------
    // Condition pools
    // -------------------------------------------------------------------------

    private static final ModifierCondition[] ACTIVE_CONDITIONS =
    {
        ModifierCondition.PVE,       ModifierCondition.PVP,      ModifierCondition.UNDEAD,
        ModifierCondition.LIVING,    ModifierCondition.FLYING,   ModifierCondition.GLACIAL,
        ModifierCondition.INFERNAL,  ModifierCondition.GROUNDED, ModifierCondition.COSMIC,
        ModifierCondition.OCCULTIC,  ModifierCondition.ELEMENTAL,ModifierCondition.BUGS,
        ModifierCondition.RAID,
    };

    private static final ModifierCondition[] PASSIVE_CONDITIONS =
    {
        ModifierCondition.PASSIVE,        ModifierCondition.OVERWORLD,
        ModifierCondition.DURING_DAY,     ModifierCondition.DURING_NIGHT,
        ModifierCondition.CLEAR_WEATHER,  ModifierCondition.RAINY_WEATHER,
        ModifierCondition.STORMY_WEATHER, ModifierCondition.NETHER,
        ModifierCondition.END,            ModifierCondition.COLDBIOMES,
        ModifierCondition.HOTBIOMES,
    };

    // -------------------------------------------------------------------------
    // Passive effect pools
    // -------------------------------------------------------------------------

    private static final List<PassiveEchoEffect> UNIVERSAL_PASSIVES = Arrays.asList(
        PassiveEchoEffect.INCREASED_MOVEMENT_SPEED,
        PassiveEchoEffect.DECREASED_MOVEMENT_SPEED,
        PassiveEchoEffect.PROTECTIVE,
        PassiveEchoEffect.NIGHTSIGHT
    );

    private static final List<PassiveEchoEffect> MELEE_PASSIVES = Arrays.asList(
        PassiveEchoEffect.EXPOSE,      PassiveEchoEffect.BURNING,
        PassiveEchoEffect.POISONOUS,   PassiveEchoEffect.SLOWING,
        PassiveEchoEffect.FATIGUING,   PassiveEchoEffect.STUNNING,
        PassiveEchoEffect.SET_ATTACK_RATE, PassiveEchoEffect.KNOCKBACK
    );

    private static final List<PassiveEchoEffect> RANGED_PASSIVES = Arrays.asList(
        PassiveEchoEffect.IGNORE_ARROW, PassiveEchoEffect.NIMBLE,
        PassiveEchoEffect.INFINITY,     PassiveEchoEffect.RECYCLE_ARROWS
    );

    private static final List<PassiveEchoEffect> TOOL_PASSIVES = List.of(
        PassiveEchoEffect.LUCKY
    );

    private static final List<PassiveEchoEffect> ARMOR_PASSIVES = List.of();
}