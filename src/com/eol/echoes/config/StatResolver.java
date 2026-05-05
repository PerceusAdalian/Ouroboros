package com.eol.echoes.config;

import java.util.concurrent.ThreadLocalRandom;

import com.eol.echoes.EchoData;
import com.eol.echoes.MateriaTypeResolver;
import com.eol.echoes.records.BindingStatBlock;
import com.eol.echoes.records.MaterialStatRange;
import com.eol.enums.EchoMaterial;
import com.eol.enums.MateriaComponent;
import com.eol.materia.Materia;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.NumberUtils;

/**
 * StatResolver produces a rolled EchoData from a base Materia and a binding Materia.
 *
 * Responsibilities:
 *   1. Validate that both Materia are the correct components (BASE, BINDING)
 *   2. Resolve the base Materia's MateriaType to an EchoMaterial
 *   3. Look up the MaterialStatRange and BindingStatBlock from EchoConfig
 *   4. Roll the base stats, then apply binding deltas on top
 *   5. Return the final EchoData
 *
 * StatResolver is stateless — call resolve() directly. It does not hold
 * any rolled state between calls.
 */
public final class StatResolver
{
    private StatResolver() {}

    /**
     * Rolls and returns EchoData for the given base + binding Materia pair.
     *
     * @param base    A Materia with MateriaComponent.BASE
     * @param binding A Materia with MateriaComponent.BINDING
     * @return Rolled EchoData, or null if either Materia is invalid
     */
    public static EchoData resolve(Materia base, Materia binding, Rarity catalystRarity)
    {
        if (base == null || base.getMateriaComponent() != MateriaComponent.BASE)
        {
            warn("StatResolver: base Materia is null or not a BASE component.");
            return null;
        }
        if (binding == null || binding.getMateriaComponent() != MateriaComponent.BINDING)
        {
            warn("StatResolver: binding Materia is null or not a BINDING component.");
            return null;
        }

        EchoMaterial echoMaterial = MateriaTypeResolver.toEchoMaterial(base.getMateriaType());
        if (echoMaterial == null)
        {
            warn("StatResolver: MateriaType '" + base.getMateriaType().name()
                    + "' does not map to a valid EchoMaterial. Cannot resolve stats.");
            return null;
        }

        // t=0 at rarity 1, t=1 at rarity 7
        double t = (catalystRarity.getRarity() - 1) / 6.0;

        EchoConfig        config    = EchoConfig.get();
        MaterialStatRange matRange  = config.getMaterialStats(echoMaterial);
        BindingStatBlock  bindBlock = config.getBindingStats(binding.getMateriaType());

        // --- Roll base stats with rarity bias ---
        double rolledAttack  = rollBiased(matRange.baseAttackMin(), matRange.baseAttackMax(), t);
        double rolledCritMod = rollBiased(1.0, matRange.critModifierCeiling(), t);

        // Crit rate is fixed per material tier - bias just scales how close to the base you get
        // Rarity 1 gets ~60% of the base crit rate, rarity 7 gets the full value
        double critRateScale = NumberUtils.lerp(0.60, 1.0, t);
        double rolledCritRate = matRange.critRateBase() * critRateScale;

        // --- Roll attack rating with bias ---
        double arRoll = rollBiased(bindBlock.attackRatingMin(), bindBlock.attackRatingMax(), t);
        double finalAttackRating = Math.round(arRoll * 100.0) / 100.0;

        // --- Roll durability with bias ---
        double rawDurability  = rollBiased(matRange.durabilityMin(), matRange.durabilityMax(), t);
        int finalDurability   = (int) Math.round(rawDurability * bindBlock.durabilityMultiplier());

        // --- Apply binding deltas ---
        double finalAttack   = rolledAttack + bindBlock.attackBonus();
        double finalCritRate = Math.max(0.0, Math.min(1.0, rolledCritRate + bindBlock.critRateBonus()));
        double finalCritMod  = rolledCritMod;

        return new EchoData(
            Math.round(finalAttack * 100.0) / 100.0,
            finalAttackRating,
            Math.round(finalCritRate * 100.0) / 100.0,
            Math.round(finalCritMod * 100.0) / 100.0,
            finalDurability,
            finalDurability);
    }

    /**
     * Rolls a value biased toward min at low t and toward max at high t.
     * At t=1 the mean sits at 95% of the range so rarity 7 rolls are
     * almost always near the ceiling but can't guarantee an exact max.
     */
    private static double rollBiased(double min, double max, double t)
    {
        double mean   = NumberUtils.lerp(min, NumberUtils.lerp(min, max, 0.95), t);
        double spread = (max - min) * 0.10;
        double roll   = mean + (ThreadLocalRandom.current().nextGaussian() * spread);
        return Math.max(min, Math.min(max, roll));
    }

    public static EchoData resolve(String baseInternalName, String bindingInternalName)
    {
        Materia base    = Materia.get(baseInternalName);
        Materia binding = Materia.get(bindingInternalName);
        return resolve(base, binding, Rarity.ONE); // fallback for test calls
    }

    private static void warn(String msg)
    {
        // Mirrors the pattern used elsewhere in the codebase — no hard dependency on a logger field
        System.err.println("[StatResolver] " + msg);
    }
}
