package com.eol.special_instances;

import com.eol.enums.MateriaType;
import com.eol.materia.Materia;
import com.ouroboros.utils.Nullable;

/**
 * EOLRecipe defines the required Materia types for an EOL to be triggered.
 * Rarity is intentionally not part of the recipe — any rarity of the correct
 * type is accepted. The provided Materia rarity still influences the rolled stats.
 *
 * requiredCore may be null, indicating this EOL expects no element core.
 * In that case, passing an element core through the forge will cause validation
 * to fail — the recipe must be followed exactly.
 */
public record EOLRecipe(MateriaType requiredBase, MateriaType requiredBinding, @Nullable MateriaType requiredCore)
{
    /**
     * Validates the provided Materia against this recipe.
     * Returns true only if all three types match exactly.
     * elementCore may be null — valid only if requiredCore is also null.
     */
    public boolean matches(Materia base, Materia binding, Materia elementCore)
    {
        if (base    == null || base.getMateriaType()    != requiredBase)    return false;
        if (binding == null || binding.getMateriaType() != requiredBinding) return false;

        // Both must be null, or both must match
        if (requiredCore == null) return elementCore == null;
        else return elementCore != null && elementCore.getMateriaType() == requiredCore;
    }

    /**
     * Convenience factory for EOLs that require no element core.
     */
    public static EOLRecipe of(MateriaType base, MateriaType binding)
    {
        return new EOLRecipe(base, binding, null);
    }

    /**
     * Convenience factory for EOLs that require a specific element core.
     */
    public static EOLRecipe of(MateriaType base, MateriaType binding, MateriaType core)
    {
        return new EOLRecipe(base, binding, core);
    }
}
