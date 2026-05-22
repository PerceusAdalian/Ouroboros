package com.eol.echoes.config;

import java.util.concurrent.ThreadLocalRandom;

import com.eol.echoes.ArmorData;
import com.eol.echoes.EchoData;
import com.eol.echoes.MateriaTypeResolver;
import com.eol.echoes.records.ArmorStatBlock;
import com.eol.echoes.records.BindingStatBlock;
import com.eol.echoes.records.MaterialStatBlock;
import com.eol.enums.EchoMaterial;
import com.eol.enums.MateriaComponent;
import com.eol.materia.Materia;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.NumberUtils;

/**
 * StatResolver produces rolled stat blocks from Materia inputs.
 *
 * Weapon / tool Echoes: {@link #resolve(Materia, Materia, Rarity)} → {@link EchoData}
 * Armor Echoes:         {@link #resolveArmor(Materia, Materia, Rarity)} → {@link ArmorData}
 *
 * StatResolver is stateless — call the static methods directly.
 */
public final class StatResolver
{
    private StatResolver() {}

    // -------------------------------------------------------------------------
    // Weapon / tool path
    // -------------------------------------------------------------------------

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
                    + "' does not map to a valid EchoMaterial.");
            return null;
        }

        double tCatalyst    = (catalystRarity.getRarity() - 1) / 6.0;
        double tBase        = (base.getRarity().getRarity() - 1) / 6.0;
        double tBinding     = (binding.getRarity().getRarity() - 1) / 6.0;
        double tBaseRoll    = (tCatalyst * 0.50) + (tBase    * 0.50);
        double tBindingRoll = (tCatalyst * 0.50) + (tBinding * 0.50);

        EchoConfig        config    = EchoConfig.get();
        MaterialStatBlock matRange  = config.getMaterialStats(echoMaterial);
        BindingStatBlock  bindBlock = config.getBindingStats(binding.getMateriaType());

        double rolledAttack   = rollBiased(matRange.baseAttackMin(), matRange.baseAttackMax(), tBaseRoll);
        double rolledCritMod  = rollBiased(1.0, matRange.critModifierCeiling(), tBaseRoll);
        double rolledCritRate = matRange.critRateBase() * NumberUtils.lerp(0.60, 1.0, tBaseRoll);

        double attackRating  = rollBiased(bindBlock.attackRatingMin(), bindBlock.attackRatingMax(), tBindingRoll);
        double rawDurability = rollBiased(matRange.durabilityMin(), matRange.durabilityMax(), tBindingRoll);

        double finalAttack       = Math.round((rolledAttack + bindBlock.attackBonus()) * 100.0) / 100.0;
        double finalAttackRating = Math.round(attackRating * 100.0) / 100.0;
        double finalCritRate     = Math.round(Math.max(0.0, Math.min(1.0, rolledCritRate + bindBlock.critRateBonus())) * 100.0) / 100.0;
        double finalCritMod      = Math.round(rolledCritMod * 100.0) / 100.0;
        int    finalDurability   = (int) Math.round(rawDurability * bindBlock.durabilityMultiplier());

        return new EchoData(finalAttack, finalAttackRating, finalCritRate, finalCritMod,
                finalDurability, finalDurability);
    }

    // -------------------------------------------------------------------------
    // Armor path
    // -------------------------------------------------------------------------

    /**
     * Rolls and returns ArmorData for the given base + binding Materia pair.
     * The binding determines durability scaling and crit-block bonuses.
     *
     * @param base    A Materia with MateriaComponent.BASE (LEATHER type)
     * @param binding A Materia with MateriaComponent.BINDING
     * @param catalystRarity Rarity of the forge catalyst
     * @return Rolled ArmorData, or null if either Materia is invalid
     */
    public static ArmorData resolveArmor(Materia base, Materia binding, Rarity catalystRarity)
    {
        if (base == null || base.getMateriaComponent() != MateriaComponent.BASE)
        {
            warn("StatResolver.resolveArmor: base Materia is null or not a BASE component.");
            return null;
        }
        if (binding == null || binding.getMateriaComponent() != MateriaComponent.BINDING)
        {
            warn("StatResolver.resolveArmor: binding Materia is null or not a BINDING component.");
            return null;
        }

        EchoMaterial echoMaterial = MateriaTypeResolver.toArmorEchoMaterial(base.getMateriaType());
        if (echoMaterial == null)
        {
            warn("StatResolver.resolveArmor: MateriaType '" + base.getMateriaType().name()
                    + "' does not map to a valid armor EchoMaterial.");
            return null;
        }

        double tCatalyst = (catalystRarity.getRarity() - 1) / 6.0;
        double tBase     = (base.getRarity().getRarity() - 1)    / 6.0;
        double tBinding  = (binding.getRarity().getRarity() - 1) / 6.0;
        double tBaseRoll    = (tCatalyst * 0.50) + (tBase    * 0.50);
        double tBindingRoll = (tCatalyst * 0.50) + (tBinding * 0.50);

        EchoConfig     config   = EchoConfig.get();
        ArmorStatBlock range    = config.getArmorMaterialStats(echoMaterial);
        BindingStatBlock bindBlock = config.getBindingStats(binding.getMateriaType());

        int    armorRating       = (int) Math.round(rollBiased(range.armorRatingMin(),      range.armorRatingMax(),      tBaseRoll));
        double blockRate         =       Math.round(rollBiased(range.blockRateMin(),         range.blockRateMax(),         tBaseRoll) * 100.0) / 100.0;
        int    critArmorRating   = (int) Math.round(rollBiased(range.critArmorRatingMin(),   range.critArmorRatingMax(),   tBaseRoll));
        double critBlockRate     =       Math.round(rollBiased(range.critBlockRateMin(),      range.critBlockRateMax(),     tBaseRoll) * 100.0) / 100.0;
        double rawDurability     =       rollBiased(range.durabilityMin(), range.durabilityMax(), tBindingRoll);
        int    finalDurability   = (int) Math.round(rawDurability * bindBlock.durabilityMultiplier());

        return new ArmorData(armorRating, blockRate, critArmorRating, critBlockRate,
                finalDurability, finalDurability);
    }

    // -------------------------------------------------------------------------
    // Test / fallback overloads
    // -------------------------------------------------------------------------

    public static EchoData resolve(String baseInternalName, String bindingInternalName)
    {
        Materia base    = Materia.get(baseInternalName);
        Materia binding = Materia.get(bindingInternalName);
        return resolve(base, binding, Rarity.ONE);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Rolls a value biased toward min at low t and toward max at high t.
     * At t=1 the mean sits at 95% of the range so rarity-7 rolls are almost
     * always near the ceiling but can't guarantee an exact max.
     */
    private static double rollBiased(double min, double max, double t)
    {
        double mean   = NumberUtils.lerp(min, NumberUtils.lerp(min, max, 0.95), t);
        double spread = (max - min) * 0.10;
        double roll   = mean + (ThreadLocalRandom.current().nextGaussian() * spread);
        return Math.max(min, Math.min(max, roll));
    }

    private static void warn(String msg)
    {
        System.err.println("[StatResolver] " + msg);
    }
}