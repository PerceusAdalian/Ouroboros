package com.eol.echoes.instances;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.eol.echoes.instances.aero.BowOfKelligir;
import com.eol.echoes.instances.arcano.LanceOfLordran;
import com.eol.echoes.instances.arcano.SwordOfTheElements;
import com.eol.echoes.instances.celestio.Aion;
import com.eol.echoes.instances.celestio.Celestia;
import com.eol.echoes.instances.celestio.LuminusBoots;
import com.eol.echoes.instances.celestio.LuminusBroadsword;
import com.eol.echoes.instances.celestio.LuminusChestplate;
import com.eol.echoes.instances.celestio.LuminusHelmet;
import com.eol.echoes.instances.celestio.LuminusLeggings;
import com.eol.echoes.instances.cosmo.Axe84;
import com.eol.echoes.instances.cosmo.Bow97;
import com.eol.echoes.instances.cosmo.Sword14;
import com.eol.echoes.instances.geo.HammerOfNidus;
import com.eol.echoes.instances.glacio.AxeOfBjorn;
import com.eol.echoes.instances.heresio.GeneralFalricStave;
import com.eol.echoes.instances.heresio.LegionaireBattleaxe;
import com.eol.echoes.instances.heresio.LegionaireClub;
import com.eol.echoes.instances.heresio.LegionaireCrossbow;
import com.eol.echoes.instances.heresio.LegionaireCuirass;
import com.eol.echoes.instances.heresio.LegionaireCutlass;
import com.eol.echoes.instances.heresio.LegionaireGreaves;
import com.eol.echoes.instances.heresio.LegionaireHelm;
import com.eol.echoes.instances.heresio.LegionaireLongbow;
import com.eol.echoes.instances.heresio.LegionairePike;
import com.eol.echoes.instances.heresio.LegionaireSabatons;
import com.eol.echoes.instances.inferno.BowOfAgni;
import com.eol.echoes.instances.mortio.Plague;
import com.eol.echoes.instances.mortio.ScytheOfBelial;
import com.eol.enums.MateriaState;
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
 *   - All three true: EOL forge path
 *   - Any false:     procedural forge path
 *
 * Registration happens at plugin enable via EOLRegistry.register().
 */
public final class EOLRegistry
{
    private EOLRegistry() {}

    public static final NamespacedKey EOL_TARGET_KEY = new NamespacedKey(Ouroboros.instance, "echo_eol_target");
    public static final NamespacedKey EOL_ARMOR_TARGET_KEY = new NamespacedKey(Ouroboros.instance, "echo_eol_armor_targets");
    

    public static final Map<String, AbstractEOL> registry = new HashMap<>();

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
    
    public static List<String> getEOLTargets(ItemStack stack)
    {
        if (!isSpecialCatalyst(stack)) return List.of();

        String raw = stack.getItemMeta().getPersistentDataContainer()
            .get(EOL_TARGET_KEY, PersistentDataType.STRING);
        if (raw == null || raw.isBlank()) return List.of();

        return Arrays.stream(raw.split(",")).toList();
    }

    /**
     * Convenience: reads the EOL target from a Materia's ItemStack form.
     * Returns null if the Materia is not a special catalyst.
     */
    public static AbstractEOL resolveFromCatalyst(Materia catalyst)
    {
        if (catalyst == null) return null;
        ItemStack stack = catalyst.getAsItemStack(MateriaState.CATALYST);
        String target = getEOLTarget(stack);
        if (target == null) return null;
        return get(target);
    }
    
    public static List<AbstractEOLWeapon> resolveWeaponSetFromCatalyst(Materia catalyst)
    {
        if (catalyst == null) return List.of();
        ItemStack stack = catalyst.getAsItemStack(MateriaState.CATALYST);

        return getEOLTargets(stack).stream()
            .map(EOLRegistry::get)
            .filter(eol -> eol instanceof AbstractEOLWeapon)
            .map(eol -> (AbstractEOLWeapon) eol)
            .toList();
    }

    /**
     * Applies the EOL_TARGET_KEY to a catalyst ItemStack, marking it as a special catalyst.
     * Use this when constructing special catalyst Materia items for EOLs.
     *
     * Example:
     * 	 
     *   ItemStack catalystItem = luminus.getAsItemStack(MateriaState.CATALYST);
     *   EOLRegistry.markCatalyst(catalystItem, "luminus_broadsword");
     */
    public static ItemStack markCatalyst(ItemStack stack, List<String> weaponTargets)
    {
        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return stack;

        meta.getPersistentDataContainer().set(
            EOL_TARGET_KEY,
            PersistentDataType.STRING,
            String.join(",", weaponTargets));
        stack.setItemMeta(meta);

        return stack;
    }

	public static ItemStack markArmorCatalyst(ItemStack stack, List<String> eolNames)
	{
	    ItemMeta meta = stack.getItemMeta();
	    if (meta == null) return stack;
	    meta.getPersistentDataContainer().set(
	        EOL_ARMOR_TARGET_KEY, 
	        PersistentDataType.STRING, 
	        String.join(",", eolNames));
	    stack.setItemMeta(meta);
	    return stack;
	}

	public static List<AbstractEOLArmor> resolveArmorSetFromCatalyst(Materia catalyst)
	{
	    if (catalyst == null) return List.of();
	    ItemStack stack = catalyst.getAsItemStack(MateriaState.CATALYST);
	    ItemMeta meta = stack.getItemMeta();
	    if (meta == null) return List.of();

	    String raw = meta.getPersistentDataContainer()
	        .get(EOL_ARMOR_TARGET_KEY, PersistentDataType.STRING);
	    if (raw == null) return List.of();

	    return Arrays.stream(raw.split(","))
	        .map(EOLRegistry::get)
	        .filter(eol -> eol instanceof AbstractEOLArmor)
	        .map(eol -> (AbstractEOLArmor) eol)
	        .toList();
	}
    
    public static void itemInit() 
    {
        List<Class<? extends AbstractEOL>> itemClasses = Arrays.asList(
           // Weapons
        		
           // Celestio
           LuminusBroadsword.class, Celestia.class, Aion.class,
           
           // Mortio
           Plague.class, ScytheOfBelial.class,

           // Inferno
           BowOfAgni.class,
           
           // Glacio
           AxeOfBjorn.class,

           // Aero
           BowOfKelligir.class,
           
           // Geo
           HammerOfNidus.class,

           // Cosmo
           Sword14.class, Axe84.class, Bow97.class,
           
           // Heresio
           GeneralFalricStave.class,
           
           // Arcano
           LanceOfLordran.class, SwordOfTheElements.class,
           
           // Armor
           
           // Set: Luminus
           LuminusHelmet.class, LuminusChestplate.class, LuminusLeggings.class, LuminusBoots.class, 
           
           // Set: Legionaires Armorset/Weaponset
           LegionaireHelm.class, LegionaireCuirass.class, LegionaireGreaves.class, LegionaireSabatons.class,
           LegionaireCutlass.class, LegionairePike.class, LegionaireLongbow.class, LegionaireCrossbow.class,
           LegionaireClub.class, LegionaireBattleaxe.class
        );
        
        for (Class<? extends AbstractEOL> clazz : itemClasses) 
        {
            try 
            {
                Constructor<? extends AbstractEOL> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                AbstractEOL instance = constructor.newInstance();
                register(instance);
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }
}
