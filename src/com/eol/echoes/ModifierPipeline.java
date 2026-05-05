package com.eol.echoes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import com.eol.echoes.config.EchoConfig;
import com.eol.echoes.records.ActiveModifier;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.eol.echoes.records.RarityBand;
import com.eol.enums.CombatStat;
import com.eol.enums.EchoForm;
import com.eol.enums.PassiveEchoEffect;
import com.eol.enums.WeaponModifierCondition;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.NumberUtils;

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
    public static List<Modifier> roll(Rarity rarity, EchoForm form)
    {
        RarityBand band = EchoConfig.get().getRarityBand(rarity);
        List<Modifier> result = new ArrayList<>();
        Set<PassiveEchoEffect> rolledPassives = new HashSet<>();

        for (int i = 0; i < band.modifierSlots(); i++)
        {
            Modifier modifier;
            if (band.rollIsActive())
                modifier = rollActive(band, form, rarity);
            else
            {
                PassiveModifier passive = rollPassive(band, form, rolledPassives);
                modifier = passive != null ? passive : rollActive(band, form, rarity);
            }
            result.add(modifier);
        }

        return result;
    }

    // -------------------------------------------------------------------------
    // Active modifier rolling
    // -------------------------------------------------------------------------

    private static ActiveModifier rollActive(RarityBand band, EchoForm form, Rarity rarity)
    {
        CombatStat stat   = rollCombatStat(form);
        boolean isPercent = rollIsPercent(stat);
        WeaponModifierCondition condition = rollCondition(true);

        // Rarity bias: t=0 at rarity 1, t=1 at rarity 7
        // Low rarity rolls cluster near the floor, high rarity cluster near the ceiling
        double t = (rarity.getRarity() - 1) / 6.0;
        double magnitude = rollBiasedMagnitude(band, t);

        // Negative modifier chance scales inversely with rarity
        // Rarity 1: 40% chance of drawback, Rarity 7: 5% chance
        double negativeChance = NumberUtils.lerp(0.40, 0.05, t);
        if (ThreadLocalRandom.current().nextDouble() < negativeChance) magnitude = -magnitude;

        if (!isPercent && stat == CombatStat.ATTACK) magnitude = Math.round(magnitude * 3);
        else magnitude = Math.round(magnitude * 100.0) / 100.0;

        return new ActiveModifier(condition, stat, magnitude, isPercent);
    }
    
    private static double rollBiasedMagnitude(RarityBand band, double t)
    {
        double floor   = band.magnitudeFloor();
        double ceiling = band.magnitudeCeiling();

        // Mean slides from floor to ceiling as rarity increases
        double mean   = NumberUtils.lerp(floor, ceiling, t);
        double spread = (ceiling - floor) * 0.15; // tighter spread keeps rolls feeling intentional

        double roll = mean + (ThreadLocalRandom.current().nextGaussian() * spread);
        return Math.max(floor, Math.min(ceiling, roll)); // clamp to valid range
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
            case SCYTHE, SPADE, PICKAXE -> new CombatStat[]{ CombatStat.ATTACK, CombatStat.ATTACK_RATING };
            case BOW, CROSSBOW -> new CombatStat[] {CombatStat.ATTACK, CombatStat.CRIT_RATE, CombatStat.CRIT_MODIFIER};
            
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
            case CRIT_RATE 				  	  -> true;
            case ATTACK_RATING, CRIT_MODIFIER -> false;
            case ATTACK                   	  -> ThreadLocalRandom.current().nextDouble() < 0.60;
        };
    }

    // -------------------------------------------------------------------------
    // Passive modifier rolling
    // -------------------------------------------------------------------------

    private static PassiveModifier rollPassive(RarityBand band, EchoForm form, Set<PassiveEchoEffect> rolledPassives)
    {
        List<PassiveEchoEffect> pool = new ArrayList<>(UNIVERSAL_PASSIVES);
        pool.addAll(switch (form)
        {
            case SWORD, HATCHET, POLEARM, HAMMER, ARMAMENT -> MELEE_PASSIVES;
            case SCYTHE, SPADE, PICKAXE                    -> TOOL_PASSIVES;
            case BOW, CROSSBOW                             -> RANGED_PASSIVES;
        });

        // Remove already-rolled effects from the pool
        pool.removeAll(rolledPassives);

        PassiveEchoEffect effectKey = pool.get(ThreadLocalRandom.current().nextInt(pool.size()));
        
        // Infinity cannot be on a bow with arrow consumption mitigation, and vice versa.
        if (effectKey == PassiveEchoEffect.INFINITY) rolledPassives.add(PassiveEchoEffect.IGNORE_ARROW);
        else if (effectKey == PassiveEchoEffect.IGNORE_ARROW) rolledPassives.add(PassiveEchoEffect.INFINITY);
        
        rolledPassives.add(effectKey);

        double magnitude = rollPassiveMagnitude(effectKey, band);
        double roundedMagnitude = Math.round(magnitude * 100.0) / 100.0;
        WeaponModifierCondition condition = rollCondition(false);

        return new PassiveModifier(condition, effectKey, roundedMagnitude);
    }

    /**
     * Some passive effects have a meaningful magnitude (proc chance, set value),
     * others are binary (fire and forget on condition). This determines which.
     */
    private static double rollPassiveMagnitude(PassiveEchoEffect effectKey, RarityBand band)
    {
        return switch (effectKey)
        {
            // Proc-chance passives: magnitude is the chance [0.0-1.0]
            case EXPOSE, BURNING, POISONOUS, SLOWING,
        	FATIGUING, STUNNING, IGNORE_ARROW, KNOCKBACK -> band.rollMagnitude();

            // Set-value passives: magnitude is the new value
            case SET_ATTACK_RATE -> 1.0 + ThreadLocalRandom.current().nextDouble();

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
        WeaponModifierCondition.GLACIAL,
        WeaponModifierCondition.INFERNAL,
        WeaponModifierCondition.GROUNDED,
        WeaponModifierCondition.COSMIC,
        WeaponModifierCondition.OCCULTIC,
        WeaponModifierCondition.ELEMENTAL,
        WeaponModifierCondition.BUGS,
        WeaponModifierCondition.RAID,
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
    private static final List<PassiveEchoEffect> UNIVERSAL_PASSIVES = Arrays.asList(
    	PassiveEchoEffect.MOVEMENT_SPEED
    );

    /** Passive effects exclusive to melee weapon forms (SWORD, AXE, SPEAR). */
    private static final List<PassiveEchoEffect> MELEE_PASSIVES = Arrays.asList(
		PassiveEchoEffect.EXPOSE,
		PassiveEchoEffect.BURNING,
		PassiveEchoEffect.POISONOUS,
		PassiveEchoEffect.SLOWING,
		PassiveEchoEffect.FATIGUING,
		PassiveEchoEffect.STUNNING,
		PassiveEchoEffect.SET_ATTACK_RATE,
		PassiveEchoEffect.KNOCKBACK
    );

    /** Passive Effects exclusive to ranged weapon forms */
    private static final List<PassiveEchoEffect> RANGED_PASSIVES = Arrays.asList(
    		PassiveEchoEffect.IGNORE_ARROW,
    		PassiveEchoEffect.NIMBLE,
    		PassiveEchoEffect.INFINITY
    );
    
    /** Passive effects for tool forms (HOE, SHOVEL, PICKAXE). */
    private static final List<PassiveEchoEffect> TOOL_PASSIVES = List.of(
        PassiveEchoEffect.LUCKY
    );
}
