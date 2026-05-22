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
 * Weapon/tool forge: {@link #forge(Materia, Materia, Materia, Materia)}
 * Armor forge:       {@link #forgeArmorEcho(Materia, Materia, Materia, EchoForm)}
 *
 * EchoForge is stateless — all forge methods can be called from any context.
 */
public final class EchoForge
{
    private EchoForge() {}

    // -------------------------------------------------------------------------
    // Weapon / tool entry point
    // -------------------------------------------------------------------------

    public static ItemStack forge(Materia catalyst, Materia base, Materia binding, Materia elementCore)
    {
        if (!validate(catalyst, MateriaComponent.CATALYST, "catalyst")) return null;
        if (!validate(base,     MateriaComponent.BASE,     "base"))     return null;
        if (!validate(binding,  MateriaComponent.BINDING,  "binding"))  return null;
        if (elementCore != null && !validate(elementCore, MateriaComponent.ELEMENT_CORE, "elementCore")) return null;

        Rarity rarity = catalyst.getRarity();
        if (rarity == Rarity.NONE) { warn("Catalyst has NONE rarity. Cannot forge."); return null; }

        ItemStack catalystStack = catalyst.getAsItemStack();
        if (EOLRegistry.isSpecialCatalyst(catalystStack))
        {
            AbstractEOL eol = EOLRegistry.resolveFromCatalyst(catalyst);
            if (eol != null && eol.getRecipe().matches(base, binding, elementCore))
                return eol.forge(catalyst, base, eol.isIntegrityArmament());
        }

        if (base.getMateriaType() == MateriaType.LEATHER)
        {
            warn("LEATHER base passed to weapon forge. Use forgeArmorEcho() instead.");
            return null;
        }

        EchoMaterial echoMaterial = MateriaTypeResolver.toEchoMaterial(base.getMateriaType());
        if (echoMaterial == null)
        {
            warn("Base MateriaType '" + base.getMateriaType().name() + "' is not a valid weapon base.");
            return null;
        }

        return forgeWeapon(rarity, base, binding, elementCore, echoMaterial);
    }

    public static ItemStack forge(ItemStack markedCatalystStack, Materia base, Materia binding, Materia elementCore)
    {
        Materia catalyst = Materia.get(markedCatalystStack);
        if (catalyst == null) return null;

        Rarity rarity = catalyst.getRarity();
        if (rarity == Rarity.NONE) return null;

        if (EOLRegistry.isSpecialCatalyst(markedCatalystStack))
        {
            AbstractEOL eol = EOLRegistry.get(EOLRegistry.getEOLTarget(markedCatalystStack));
            if (eol != null && eol.getRecipe().matches(base, binding, elementCore))
                return eol.forge(catalyst, base, eol.isIntegrityArmament());
        }

        return forge(catalyst, base, binding, elementCore);
    }

    // -------------------------------------------------------------------------
    // Armor entry point
    // -------------------------------------------------------------------------

    /**
     * Forges an armor Echo. The player selects the armor form (HELMET / CHESTPLATE /
     * LEGGINGS / BOOTS) on the armor forge GUI before calling this.
     *
     * Modifiers are rolled via {@link ModifierPipeline#rollArmor} which produces
     * {@link com.eol.echoes.records.ActiveArmorModifier} instances keyed on
     * {@link com.eol.enums.ArmorStat} rather than {@link com.eol.enums.CombatStat}.
     *
     * @param catalyst  CATALYST — determines rarity
     * @param base      BASE     — must be a valid armor base (leather / iron / gold / diamond / netherite)
     * @param binding   BINDING  — determines durability scaling
     * @param armorForm One of EchoForm.HELMET, CHESTPLATE, LEGGINGS, BOOTS
     * @return Forged armor Echo ItemStack, or null on validation failure
     */
    public static ItemStack forgeArmorEcho(Materia catalyst, Materia base, Materia binding, EchoForm armorForm)
    {
        if (!validate(catalyst, MateriaComponent.CATALYST, "catalyst")) return null;
        if (!validate(base,     MateriaComponent.BASE,     "base"))     return null;
        if (!validate(binding,  MateriaComponent.BINDING,  "binding"))  return null;

        if (armorForm == null || !isArmorForm(armorForm))
        {
            warn("forgeArmorEcho: armorForm must be HELMET, CHESTPLATE, LEGGINGS, or BOOTS. Got: " + armorForm);
            return null;
        }

        Rarity rarity = catalyst.getRarity();
        if (rarity == Rarity.NONE) { warn("Catalyst has NONE rarity. Cannot forge armor Echo."); return null; }

        EchoMaterial echoMaterial = MateriaTypeResolver.toArmorEchoMaterial(base.getMateriaType());
        if (echoMaterial == null)
        {
            warn("Base MateriaType '" + base.getMateriaType().name() + "' is not a valid armor base.");
            return null;
        }

        ArmorData armorStats = StatResolver.resolveArmor(base, binding, rarity);
        if (armorStats == null)
        {
            warn("StatResolver.resolveArmor returned null for base='" + base.getInternalName()
                    + "' binding='" + binding.getInternalName() + "'");
            return null;
        }

        List<Modifier> modifiers = ModifierPipeline.rollArmor(rarity, armorForm);

        String echoId = generateEchoId(base, binding, rarity);
        EchoManifest manifest = new EchoManifest(echoId, rarity, armorStats, modifiers, armorForm, echoMaterial);

        return buildArmorItem(manifest, armorForm, echoMaterial);
    }

    // -------------------------------------------------------------------------
    // Weapon forge path (internal)
    // -------------------------------------------------------------------------

    private static ItemStack forgeWeapon(Rarity rarity, Materia base, Materia binding,
                                          Materia elementCore, EchoMaterial echoMaterial)
    {
        EchoData stats = StatResolver.resolve(base, binding, rarity);
        if (stats == null)
        {
            warn("StatResolver returned null for base='" + base.getInternalName()
                    + "' binding='" + binding.getInternalName() + "'");
            return null;
        }

        EchoForm       form      = rollForm(echoMaterial);
        List<Modifier> modifiers = ModifierPipeline.roll(rarity, form);  // weapon path
        ElementiumSlotType slotType = resolveSlot(elementCore);

        String echoId = generateEchoId(base, binding, rarity);
        EchoManifest manifest = new EchoManifest(echoId, rarity, stats, modifiers, slotType, form, echoMaterial);

        return buildWeaponItem(manifest, form, echoMaterial, base.getRarity());
    }

    // -------------------------------------------------------------------------
    // ItemStack construction — weapon
    // -------------------------------------------------------------------------

    private static ItemStack buildWeaponItem(EchoManifest manifest, EchoForm form,
                                              EchoMaterial echoMaterial, Rarity baseRarity)
    {
        Material material = EchoFormResolver.toBukkitMaterial(form, echoMaterial);
        if (material == null)
        {
            warn("EchoFormResolver returned null for form=" + form + " material=" + echoMaterial);
            return null;
        }

        ItemStack stack = new ItemStack(material, 1);
        ItemMeta  meta  = stack.getItemMeta();
        if (meta == null) return null;

        meta.setDisplayName(PrintUtils.ColorParser(buildWeaponDisplayName(manifest.rarity(), form, echoMaterial)));
        meta.setLore(EchoLoreBuilder.build(manifest, echoMaterial));
        meta.setEnchantmentGlintOverride(manifest.rarity().getRarity() >= 4);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);

        applyAttackSpeed(meta, manifest);
        stack.setItemMeta(meta);
        EchoManifestCodec.write(stack, manifest);

        return stack;
    }

    // -------------------------------------------------------------------------
    // ItemStack construction — armor
    // -------------------------------------------------------------------------

    private static ItemStack buildArmorItem(EchoManifest manifest, EchoForm armorForm, EchoMaterial echoMaterial)
    {
        Material material = EchoFormResolver.toBukkitMaterial(armorForm, echoMaterial);
        if (material == null)
        {
            warn("EchoFormResolver returned null for armorForm=" + armorForm + " material=" + echoMaterial);
            return null;
        }

        ItemStack stack = new ItemStack(material, 1);
        ItemMeta  meta  = stack.getItemMeta();
        if (meta == null) return null;

        meta.setDisplayName(PrintUtils.ColorParser(buildArmorDisplayName(manifest.rarity(), armorForm, echoMaterial)));
        meta.setLore(EchoLoreBuilder.buildArmor(manifest, echoMaterial));
        meta.setEnchantmentGlintOverride(manifest.rarity().getRarity() >= 4);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);

        stack.setItemMeta(meta);
        EchoManifestCodec.write(stack, manifest);

        return stack;
    }

    // -------------------------------------------------------------------------
    // Rebuild
    // -------------------------------------------------------------------------

    static ItemStack rebuild(EchoManifest manifest)
    {
        if (manifest.echoForm() == null || manifest.echoMaterial() == null) return null;

        if (manifest.isArmorEcho())
            return buildArmorItem(manifest, manifest.echoForm(), manifest.echoMaterial());
        else
            return buildWeaponItem(manifest, manifest.echoForm(), manifest.echoMaterial(), manifest.rarity());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static EchoForm rollForm(EchoMaterial echoMaterial)
    {
        EchoForm[] pool = switch (echoMaterial)
        {
            case HAMMER   -> new EchoForm[]{ EchoForm.HAMMER };
            case ARMAMENT -> new EchoForm[]{ EchoForm.ARMAMENT };
            case BOW      -> new EchoForm[]{ EchoForm.BOW };
            case CROSSBOW -> new EchoForm[]{ EchoForm.CROSSBOW };
            default       -> new EchoForm[]{ EchoForm.SWORD, EchoForm.HATCHET, EchoForm.POLEARM,
                                             EchoForm.PICKAXE, EchoForm.SPADE, EchoForm.SCYTHE };
        };
        return pool[(int)(Math.random() * pool.length)];
    }

    private static ElementiumSlotType resolveSlot(Materia elementCore)
    {
        if (elementCore == null) return ElementiumSlotType.NO_SLOT;
        return MateriaTypeResolver.toElementiumSlot(elementCore.getMateriaType());
    }

    private static String generateEchoId(Materia base, Materia binding, Rarity rarity)
    {
        String shortUUID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return base.getInternalNameAsID()
                + "-" + binding.getInternalNameAsID()
                + "-R" + rarity.getRarity()
                + "-" + shortUUID;
    }

    private static String buildWeaponDisplayName(Rarity rarity, EchoForm form, EchoMaterial echoMaterial)
    {
        char   rarityColor   = PrintUtils.getRarityColor(rarity);
        String formLabel     = PrintUtils.formatEnumName(form.name());
        String materialLabel = PrintUtils.formatEnumName(echoMaterial.name());

        if (form == EchoForm.HAMMER || form == EchoForm.ARMAMENT
                || form == EchoForm.BOW || form == EchoForm.CROSSBOW)
            return "&r&b&lΣcho&r&f: " + formLabel
                    + " &r&" + rarityColor + "&l" + PrintUtils.getRarityAsNumeralValue(rarity);

        return "&r&b&lΣcho&r&f: " + materialLabel + " " + formLabel
                + " &r&" + rarityColor + "&l" + PrintUtils.getRarityAsNumeralValue(rarity);
    }

    private static String buildArmorDisplayName(Rarity rarity, EchoForm armorForm, EchoMaterial echoMaterial)
    {
        char   rarityColor   = PrintUtils.getRarityColor(rarity);
        String formLabel     = PrintUtils.formatEnumName(armorForm.name());
        String materialLabel = PrintUtils.formatEnumName(echoMaterial.name());

        return "&r&b&lΣcho&r&f: " + materialLabel + " " + formLabel
                + " &r&" + rarityColor + "&l" + PrintUtils.getRarityAsNumeralValue(rarity);
    }

    private static void applyAttackSpeed(ItemMeta meta, EchoManifest manifest)
    {
        meta.removeAttributeModifier(Attribute.ATTACK_SPEED);

        if (manifest.baseStats() == null) return;
        double attacksPerSecond = manifest.baseStats().getAttackRating();
        if (attacksPerSecond <= 0) return;

        double delta = attacksPerSecond - 4.0;
        NamespacedKey key = new NamespacedKey(Ouroboros.instance, "echo_attack_speed");
        AttributeModifier mod = new AttributeModifier(key, delta, Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, mod);
    }

    private static boolean isArmorForm(EchoForm form)
    {
        return form == EchoForm.HELMET || form == EchoForm.CHESTPLATE
                || form == EchoForm.LEGGINGS || form == EchoForm.BOOTS;
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