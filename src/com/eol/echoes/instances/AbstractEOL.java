package com.eol.echoes.instances;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.eol.echoes.records.EOLRecipe;
import com.eol.echoes.records.Modifier;
import com.eol.enums.EchoForm;
import com.eol.enums.ElementiumSlotType;
import com.eol.materia.Materia;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.Nullable;
import com.ouroboros.utils.PrintUtils;

/**
 * AbstractEOL is the authored base class for all Echo of Lumina (EOL) weapons.
 *
 * Subclasses define:
 *   - The display name and description
 *   - The EOLRecipe (required Materia types)
 *   - The EchoForm and ElementiumSlotType
 *   - The predetermined modifier list
 *   - The locked ability key (nullable)
 *
 * Stats (attack, attackRating, critRate, critModifier) are still rolled at forge
 * time from the provided Materia, so two instances of the same EOL can have
 * different stat rolls depending on the rarity of Materia used.
 *
 * EOLs are registered in EOLRegistry keyed on their special catalyst's internal name.
 */
public abstract class AbstractEOL
{
    private final String displayName;
    private final String internalName;
    private final EOLRecipe recipe;
    private final EchoForm form;
    private final ElementiumSlotType slotType;
    private final ElementType eType;
    private final List<Modifier> modifiers;
    private final String lockedAbilityKey;
    private final String[] description;
    private final boolean isIntegrityArmament;

    public static final NamespacedKey eolKey = new NamespacedKey(Ouroboros.instance, "eol");

    public AbstractEOL(
            String displayName,
            String internalName,
            boolean isIntegrityArmament,
            EOLRecipe recipe,
            EchoForm form,
            ElementiumSlotType slotType,
            ElementType eType,
            List<Modifier> modifiers,
            @Nullable String lockedAbilityKey,
            @Nullable String... description)
    {
        this.displayName         = displayName;
        this.internalName        = internalName;
        this.isIntegrityArmament = isIntegrityArmament;
        this.recipe              = recipe;
        this.form                = form;
        this.slotType            = slotType;
        this.eType 				 = eType;
        this.modifiers           = List.copyOf(modifiers);
        this.lockedAbilityKey    = lockedAbilityKey;
        this.description         = description;
    }
    
	// Subclasses own forge()
    public abstract ItemStack forge(Materia catalyst, Materia base, boolean isIntegrityArmament);

    // -------------------------------------------------------------------------
    // Shared lore header — called by both subclass buildLore() paths
    // -------------------------------------------------------------------------

    protected String armamentTag()
    {
        return PrintUtils.ColorParser(isIntegrityArmament
                ? PrintUtils.color(ObsColors.CELESTIO) + "&lIntegrity Armament&r&f"
                : PrintUtils.color(ObsColors.HERESIO)  + "&lTwilight Armament&r&f");
    }

    protected String[] getDescription() { return description; }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public String getDisplayName()          { return displayName; }
    public String getInternalName()         { return internalName; }
    public EOLRecipe getRecipe()            { return recipe; }
    public EchoForm getForm()               { return form; }
    public ElementiumSlotType getSlotType() { return slotType; }
    public ElementType getElementType()     { return eType; }
    public List<Modifier> getModifiers()    { return modifiers; }
    public String getLockedAbilityKey()     { return lockedAbilityKey; }
    public boolean hasLockedAbility()       { return lockedAbilityKey != null && !lockedAbilityKey.isBlank(); }
    public boolean isIntegrityArmament()    { return isIntegrityArmament; }
}