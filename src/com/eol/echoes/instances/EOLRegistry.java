package com.eol.echoes.instances;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.eol.materia.Materia;
import com.ouroboros.Ouroboros;

/**
 * EOLRegistry maps special catalyst internal names to their AbstractEOL definitions.
 *
 * A special catalyst is a Materia with MateriaComponent.CATALYST that additionally
 * carries the NamespacedKey "echo:eol_target" in its PDC, storing the internal name
 * of the EOL it can unlock.
 *
 * EchoForge checks this registry before routing to the procedural path:
 *   1. Is the catalyst marked with echo:eol_target?
 *   2. Does the target EOL exist in the registry?
 *   3. Does the provided Materia match the EOL's recipe?
 *   → All three true: EOL forge path
 *   → Any false:     procedural forge path
 *
 * Registration happens at plugin enable via EOLRegistry.register().
 */
public final class EOLRegistry
{
    private EOLRegistry() {}

    public static final NamespacedKey EOL_TARGET_KEY = new NamespacedKey(Ouroboros.instance, "echo_eol_target");

    private static final Map<String, AbstractEOL> registry = new HashMap<>();

    // -------------------------------------------------------------------------
    // Registration
    // -------------------------------------------------------------------------

    /**
     * Registers an EOL definition keyed on its internal name.
     * The special catalyst for this EOL must carry EOL_TARGET_KEY = internalName.
     */
    public static void register(AbstractEOL eol)
    {
        registry.put(eol.getInternalName(), eol);
    }

    /**
     * Returns the AbstractEOL registered under the given internal name,
     * or null if none is registered.
     */
    public static AbstractEOL get(String internalName)
    {
        return registry.getOrDefault(internalName, null);
    }

    // -------------------------------------------------------------------------
    // Catalyst inspection
    // -------------------------------------------------------------------------

    /**
     * Returns true if the given ItemStack is a special EOL catalyst
     * (i.e. carries the EOL_TARGET_KEY in its PDC).
     */
    public static boolean isSpecialCatalyst(ItemStack stack)
    {
        if (stack == null) return false;
        
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return false;
        
        return meta.getPersistentDataContainer().has(EOL_TARGET_KEY, PersistentDataType.STRING);
    }

    /**
     * Returns the EOL target internal name stored on the catalyst's PDC,
     * or null if the catalyst is not a special catalyst.
     */
    public static String getEOLTarget(ItemStack stack)
    {
        if (!isSpecialCatalyst(stack)) return null;
        
        return stack.getItemMeta().getPersistentDataContainer().get(EOL_TARGET_KEY, PersistentDataType.STRING);
    }

    /**
     * Convenience: reads the EOL target from a Materia's ItemStack form.
     * Returns null if the Materia is not a special catalyst.
     */
    public static AbstractEOL resolveFromCatalyst(Materia catalyst)
    {
        if (catalyst == null) return null;
        ItemStack stack = catalyst.getAsItemStack();
        String target = getEOLTarget(stack);
        if (target == null) return null;
        return get(target);
    }

    /**
     * Applies the EOL_TARGET_KEY to a catalyst ItemStack, marking it as a special catalyst.
     * Use this when constructing special catalyst Materia items for EOLs.
     *
     * Example:
     *   ItemStack catalystItem = luminus.getAsItemStack(MateriaState.CATALYST);
     *   EOLRegistry.markCatalyst(catalystItem, "luminus_broadsword");
     */
    public static ItemStack markCatalyst(ItemStack stack, String eolInternalName)
    {
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return stack;
        
        meta.getPersistentDataContainer().set(EOL_TARGET_KEY, PersistentDataType.STRING, eolInternalName);
        stack.setItemMeta(meta);
        
        return stack;
    }
}
