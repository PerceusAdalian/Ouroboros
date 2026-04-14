package com.eol.echoes.config;

import com.eol.echoes.EchoData;
import com.eol.echoes.MateriaTypeResolver;
import com.eol.enums.EchoMaterial;
import com.eol.enums.MateriaComponent;
import com.eol.materia.Materia;

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
    public static EchoData resolve(Materia base, Materia binding)
    {
        // --- Validate components ---
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

        // --- Resolve base MateriaType → EchoMaterial ---
        EchoMaterial echoMaterial = MateriaTypeResolver.toEchoMaterial(base.getMateriaType());
        if (echoMaterial == null)
        {
            warn("StatResolver: MateriaType '" + base.getMateriaType().name()
                    + "' does not map to a valid EchoMaterial. Cannot resolve stats.");
            return null;
        }

        // --- Load stat ranges from config ---
        EchoConfig 		  config     = EchoConfig.get();
        MaterialStatRange matRange   = config.getMaterialStats(echoMaterial);
        BindingStatBlock  bindBlock  = config.getBindingStats(binding.getMateriaType());

        // --- Roll base stats from material range ---
        double rolledAttack   		 = matRange.rollBaseAttack();
        double rolledCritRate 		 = matRange.critRateBase();
        double rolledCritMod  		 = matRange.rollCritModifier();

        // --- Apply binding deltas ---
        double finalAttack       	 = rolledAttack   + bindBlock.attackBonus();
        double finalAttackRating 	 = bindBlock.attackRating();            		// AR comes entirely from binding
        double finalCritRate     	 = rolledCritRate + bindBlock.critRateBonus();
        double finalCritMod      	 = rolledCritMod;                       		// binding doesn't touch crit mod

        // Clamp crit rate to [0.0, 1.0]
        finalCritRate = Math.max(0.0, Math.min(1.0, finalCritRate));

        return new EchoData(finalAttack, finalAttackRating, finalCritRate, finalCritMod);
    }

    /**
     * Convenience overload: resolve using internal names looked up from the Materia registry.
     * Useful for testing or command-driven forge calls.
     */
    public static EchoData resolve(String baseInternalName, String bindingInternalName)
    {
        Materia base    = Materia.get(baseInternalName);
        Materia binding = Materia.get(bindingInternalName);
        return resolve(base, binding);
    }

    private static void warn(String msg)
    {
        // Mirrors the pattern used elsewhere in the codebase — no hard dependency on a logger field
        System.err.println("[StatResolver] " + msg);
    }
}
