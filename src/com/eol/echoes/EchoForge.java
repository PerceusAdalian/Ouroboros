package com.eol.echoes;

import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.eol.echoes.config.StatResolver;
import com.eol.echoes.instances.AbstractEOL;
import com.eol.echoes.instances.EOLRegistry;
import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.Modifier;
import com.eol.enums.EchoForm;
import com.eol.enums.EchoMaterial;
import com.eol.enums.ElementiumSlotType;
import com.eol.enums.MateriaComponent;
import com.eol.enums.MateriaType;
import com.eol.materia.Materia;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.PrintUtils;

/**
 * EchoForge is the single entry point for procedural Echo generation.
 *
 * It accepts the four forge inputs, validates them, routes to the correct
 * pipeline (weapon vs armor), and returns a fully built ItemStack with
 * the EchoManifest baked into its PersistentDataContainer.
 *
 * Usage:
 *   ItemStack echo = EchoForge.forge(catalyst, base, binding, elementCore);
 *   ItemStack echo = EchoForge.forge(catalyst, base, binding, null); // no element slot
 *
 * EchoForge is stateless — forge() can be called from any context.
 */
public final class EchoForge
{
    private EchoForge() {}
 
    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------
 
    /**
     * Forges an Echo from the four Materia components.
     *
     * @param catalyst    MateriaComponent.CATALYST  — determines rarity and slot count
     * @param base        MateriaComponent.BASE      — determines material tier and weapon/armor path
     * @param binding     MateriaComponent.BINDING   — determines attack rating, durability, crit delta
     * @param elementCore MateriaComponent.ELEMENT_CORE — optional; determines ElementiumSlotType
     * @return A forged Echo ItemStack, or null if any input is invalid
     */
    public static ItemStack forge(Materia catalyst, Materia base, Materia binding, Materia elementCore)
    {
        // --- Validate components ---
        if (!validate(catalyst, MateriaComponent.CATALYST, "catalyst")) return null;
        if (!validate(base,     MateriaComponent.BASE,     "base"))     return null;
        if (!validate(binding,  MateriaComponent.BINDING,  "binding"))  return null;
        // elementCore is optional — null is valid
        if (elementCore != null && !validate(elementCore, MateriaComponent.ELEMENT_CORE, "elementCore")) return null;
 
        // --- Resolve rarity from catalyst ---
        Rarity rarity = catalyst.getRarity();
        if (rarity == Rarity.NONE)
        {
            warn("Catalyst has NONE rarity. Cannot forge.");
            return null;
        }
 
        // --- Check for special EOL catalyst ---
        ItemStack catalystStack = catalyst.getAsItemStack();
        if (EOLRegistry.isSpecialCatalyst(catalystStack))
        {
            AbstractEOL eol = EOLRegistry.resolveFromCatalyst(catalyst);
            if (eol != null && eol.getRecipe().matches(base, binding, elementCore))
                return eol.forge(base, binding, elementCore, catalyst);
            // Special catalyst present but recipe doesn't match — fall through to procedural
        }
 
        // --- Route by base material type ---
        MateriaType baseType = base.getMateriaType();
 
        if (baseType == MateriaType.LEATHER)
            return forgeArmor(rarity, base, binding, elementCore);
 
        EchoMaterial echoMaterial = MateriaTypeResolver.toEchoMaterial(baseType);
        if (echoMaterial == null)
        {
            warn("Base MateriaType '" + baseType.name() + "' is not a valid weapon base.");
            return null;
        }
 
        return forgeWeapon(rarity, base, binding, elementCore, echoMaterial);
    }
 
    // -------------------------------------------------------------------------
    // Weapon forge path
    // -------------------------------------------------------------------------
 
    private static ItemStack forgeWeapon(Rarity rarity, Materia base, Materia binding, Materia elementCore, EchoMaterial echoMaterial)
    {
        // --- Roll stats ---
        EchoData stats = StatResolver.resolve(base, binding);
        if (stats == null)
        {
            warn("StatResolver returned null for base='" + base.getInternalName()
                    + "' binding='" + binding.getInternalName() + "'");
            return null;
        }
 
        // --- Roll EchoForm from the material tier ---
        EchoForm form = rollForm(echoMaterial);
 
        // --- Roll modifiers ---
        List<Modifier> modifiers = ModifierPipeline.roll(rarity, form);
 
        // --- Resolve element slot ---
        ElementiumSlotType slotType = resolveSlot(elementCore);
 
        // --- Build manifest ---
        String echoId = generateEchoId(base, binding, rarity);
        EchoManifest manifest = new EchoManifest(echoId, rarity, stats, modifiers, slotType, form, echoMaterial);
 
        // --- Build ItemStack ---
        return buildItem(manifest, form, echoMaterial, base.getRarity());
    }
 
    // -------------------------------------------------------------------------
    // Armor forge path (stubbed — implement when armor Echo design is finalized)
    // -------------------------------------------------------------------------
 
    private static ItemStack forgeArmor(Rarity rarity, Materia base, Materia binding, Materia elementCore)
    {
        // @ TODO: Implement armor Echo pipeline when EchoArmorForm is designed.
        // The leather base routes here. Stub returns null gracefully for now.
        warn("Armor Echo forging is not yet implemented. Leather base received.");
        return null;
    }
 
    // -------------------------------------------------------------------------
    // ItemStack construction
    // -------------------------------------------------------------------------
 
    private static ItemStack buildItem(EchoManifest manifest, EchoForm form, EchoMaterial echoMaterial, Rarity baseRarity)
    {
        // Resolve the Bukkit Material from EchoForm + EchoMaterial
        Material material = EchoFormResolver.toBukkitMaterial(form, echoMaterial);
        if (material == null)
        {
            warn("EchoFormResolver returned null for form=" + form + " material=" + echoMaterial);
            return null;
        }
 
        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta   = stack.getItemMeta();
        if (meta == null) return null;
 
        // Display name: rarity color + "Echo" label
        String displayName = buildDisplayName(manifest.rarity(), form, echoMaterial);
        meta.setDisplayName(PrintUtils.ColorParser(displayName));
        meta.setLore(EchoLoreBuilder.build(manifest, echoMaterial));
        meta.setEnchantmentGlintOverride(manifest.rarity().getRarity() >= 4);
        
        // Echoes manage their own durability via PDC — lock out vanilla durability system
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        
        applyAttackSpeed(meta, manifest);
        stack.setItemMeta(meta);

        // Bake manifest into PDC
        EchoManifestCodec.write(stack, manifest);
 
        return stack;
    }
 
    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
 
    /**
     * Rebuilds the ItemStack for an existing manifest — used when the manifest
     * has been mutated (e.g. ability equipped) and the item needs to reflect the change.
     * Returns null if the manifest is missing form/material data (e.g. EOL echoes).
     */
    static ItemStack rebuild(EchoManifest manifest)
    {
        if (manifest.echoForm() == null || manifest.echoMaterial() == null) return null;
        return buildItem(manifest, manifest.echoForm(), manifest.echoMaterial(), manifest.rarity());
    }
    
    /**
     * Rolls an EchoForm appropriate for the material tier.
     * All material tiers can produce any form — form is rolled uniformly.
     * Extend this if certain tiers should be biased toward certain forms.
     */
    private static EchoForm rollForm(EchoMaterial echoMaterial)
    {
        EchoForm[] pool = switch (echoMaterial)
        {
            case MACE    -> new EchoForm[]{ EchoForm.MACE };
            case TRIDENT -> new EchoForm[]{ EchoForm.TRIDENT };
            default      -> new EchoForm[]{ EchoForm.SWORD, EchoForm.AXE, EchoForm.SPEAR,
                                            EchoForm.PICKAXE, EchoForm.SHOVEL, EchoForm.HOE };
        };
        return pool[(int)(Math.random() * pool.length)];
    }
 
    /**
     * Resolves the ElementiumSlotType from an optional element core Materia.
     * Returns NO_SLOT if elementCore is null.
     */
    private static ElementiumSlotType resolveSlot(Materia elementCore)
    {
        if (elementCore == null) return ElementiumSlotType.NO_SLOT;
        return MateriaTypeResolver.toElementiumSlot(elementCore.getMateriaType());
    }
 
    /**
     * Generates a stable echo ID from the inputs.
     * Format: <baseID>-<bindingID>-<rarityTier>-<shortUUID>
     * The short UUID ensures two identical inputs still produce unique IDs.
     */
    private static String generateEchoId(Materia base, Materia binding, Rarity rarity)
    {
        String shortUUID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return base.getInternalNameAsID()
                + "-" + binding.getInternalNameAsID()
                + "-R" + rarity.getRarity()
                + "-" + shortUUID;
    }
 
    /**
     * Builds the display name for the forged Echo.
     * Example: "&e&lRare &6Iron Sword Echo"
     */
    private static String buildDisplayName(Rarity rarity, EchoForm form, EchoMaterial echoMaterial)
    {
        char rarityColor = PrintUtils.getRarityColor(rarity);
        
        String formLabel     = PrintUtils.formatEnumName(form.name());
        String materialLabel = PrintUtils.formatEnumName(echoMaterial.name());
        String displayNameLabel;
        
        if (form == EchoForm.MACE || form == EchoForm.TRIDENT)
        {
        	displayNameLabel = "&r&b&lΣcho&r&f: " + formLabel + " &r&" + rarityColor + "&l" + PrintUtils.getRarityAsNumeralValue(rarity);
        }
        else displayNameLabel = "&r&b&lΣcho&r&f: " + materialLabel + " " + formLabel + " &r&" + rarityColor + "&l" + PrintUtils.getRarityAsNumeralValue(rarity);
        
        return displayNameLabel;
    }
 
    /**
     * Stamps ATTACK_SPEED onto the item's meta using the manifest's attackRating.
     * attackRating is stored as attacks-per-second (matching vanilla's unit).
     *
     * ATTACK_SPEED base value is 4.0. We use ADD_NUMBER to set an absolute
     * target by supplying (target - 4.0) as the delta. The modifier UUID is derived
     * deterministically from the echoId so re-forging never accumulates duplicates.
     */
    private static void applyAttackSpeed(ItemMeta meta, EchoManifest manifest)
    {
        meta.removeAttributeModifier(Attribute.ATTACK_SPEED);
        
        double attacksPerSecond = manifest.baseStats().getAttackRating();
        if (attacksPerSecond <= 0) return;
        
        double delta = attacksPerSecond - 4.0;
        
        NamespacedKey key = new NamespacedKey(Ouroboros.instance, "echo_attack_speed");
        AttributeModifier mod = new AttributeModifier(key, delta, Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);

        meta.addAttributeModifier(Attribute.ATTACK_SPEED, mod);
    }

    private static boolean validate(Materia materia, MateriaComponent expected, String label)
    {
        if (materia == null)
        {
            warn("Forge input '" + label + "' is null.");
            return false;
        }
        if (materia.getMateriaComponent() != expected)
        {
            warn("Forge input '" + label + "' has wrong component type. Expected: "
                    + expected.name() + ", got: " + materia.getMateriaComponent().name());
            return false;
        }
        return true;
    }

    private static void warn(String msg)
    {
        System.err.println("[EchoForge] " + msg);
    }
}
