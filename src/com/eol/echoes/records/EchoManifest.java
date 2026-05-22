package com.eol.echoes.records;

import java.util.Collections;
import java.util.List;

import com.eol.echoes.ArmorData;
import com.eol.echoes.EchoData;
import com.eol.enums.EchoForm;
import com.eol.enums.EchoMaterial;
import com.eol.enums.ElementiumSlotType;
import com.eol.enums.PassiveEchoEffect;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.Nullable;

/**
 * EchoManifest is the complete, immutable description of a procedurally generated Echo.
 * It is created once at forge time and serialized into the item's PersistentDataContainer.
 * All combat and modifier logic reads from the deserialized manifest at runtime.
 *
 * Weapon/tool Echoes populate {@code baseStats}; {@code armorStats} is null.
 * Armor Echoes populate {@code armorStats};  {@code baseStats}  is null.
 * Exactly one of the two stat blocks is non-null for any valid manifest.
 *
 * For EOL (Echo of Lumina) items the manifest is hand-authored rather than rolled,
 * but the same structure and codec are used.
 */
public record EchoManifest(
        String                 echoId,
        Rarity                 rarity,
        @Nullable EchoData     baseStats, 
        @Nullable ArmorData    armorStats,
        List<Modifier>         modifiers,
        ElementiumSlotType     slotType,
        @Nullable String       equippedAbilityKey,
        @Nullable String       lockedAbilityKey,
        @Nullable EchoForm     echoForm,
        @Nullable EchoMaterial echoMaterial)
{
    /** Compact constructor — defensively copies modifiers so the manifest is truly immutable. */
    public EchoManifest
    {
        modifiers = Collections.unmodifiableList(modifiers);
    }

    // -------------------------------------------------------------------------
    // Convenience constructors
    // -------------------------------------------------------------------------

    /** Weapon / tool Echo (no ability pre-equipped, no locked ability). */
    public EchoManifest(String echoId, Rarity rarity, EchoData baseStats,
                        List<Modifier> modifiers, ElementiumSlotType slotType,
                        EchoForm echoForm, EchoMaterial echoMaterial)
    {
        this(echoId, rarity, baseStats, null, modifiers, slotType,
                null, null, echoForm, echoMaterial);
    }

    /** Armor Echo (no abilities, no elementium slot by design). */
    public EchoManifest(String echoId, Rarity rarity, ArmorData armorStats,
                        List<Modifier> modifiers,
                        EchoForm echoForm, EchoMaterial echoMaterial)
    {
        this(echoId, rarity, null, armorStats, modifiers, ElementiumSlotType.NO_SLOT,
                null, null, echoForm, echoMaterial);
    }

    // -------------------------------------------------------------------------
    // Type discrimination
    // -------------------------------------------------------------------------

    /** Returns true if this is an armor Echo (helmet / chestplate / leggings / boots). */
    public boolean isArmorEcho()
    {
        return armorStats != null;
    }

    /** Returns true if this is a weapon or tool Echo. */
    public boolean isWeaponEcho()
    {
        return baseStats != null;
    }

    // -------------------------------------------------------------------------
    // Durability helpers (type-agnostic — prefer these over reaching into stat blocks)
    // -------------------------------------------------------------------------

    public int getCurrentDurability()
    {
        if (armorStats != null) return armorStats.getCurrentDurability();
        if (baseStats  != null) return baseStats.getCurrentDurability();
        return 0;
    }

    public int getMaxDurability()
    {
        if (armorStats != null) return armorStats.getMaxDurability();
        if (baseStats  != null) return baseStats.getMaxDurability();
        return 0;
    }

    // -------------------------------------------------------------------------
    // Ability helpers (armor echoes never have abilities)
    // -------------------------------------------------------------------------

    public boolean hasLockedAbility()
    {
        return !isArmorEcho() && lockedAbilityKey != null && !lockedAbilityKey.isBlank();
    }

    public boolean hasEquippedAbility()
    {
        return !isArmorEcho() && equippedAbilityKey != null && !equippedAbilityKey.isBlank();
    }

    public String getAbilityKey()
    {
        return hasEquippedAbility() ? equippedAbilityKey : null;
    }

    public String getLockedAbilityKey()
    {
        return hasLockedAbility() ? lockedAbilityKey : null;
    }

    // -------------------------------------------------------------------------
    // Slot helpers
    // -------------------------------------------------------------------------

    public ElementiumSlotType getElementiumSlotType()
    {
        return slotType;
    }

    public boolean hasElementiumSlot()
    {
        return !isArmorEcho() && slotType != null && slotType != ElementiumSlotType.NO_SLOT;
    }

    public EchoForm getEchoForm()
    {
        return echoForm;
    }

    // -------------------------------------------------------------------------
    // Modifier helpers
    // -------------------------------------------------------------------------

    public boolean hasActiveModifiers()
    {
        return modifiers.stream().anyMatch(Modifier::isActive);
    }

    public List<Modifier> getActiveModifiers()
    {
        return modifiers.stream().filter(Modifier::isActive).toList();
    }

    public List<Modifier> getPassiveModifiers()
    {
        return modifiers.stream().filter(m -> !m.isActive()).toList();
    }

    public boolean containsPassiveModifier(PassiveEchoEffect effect)
    {
        return modifiers().stream()
                .filter(m -> !m.isActive())
                .map(m -> (PassiveModifier) m)
                .anyMatch(m -> m.effectKey() == effect);
    }

    public PassiveModifier getPassiveModifier(PassiveEchoEffect effect)
    {
        return modifiers().stream()
                .filter(m -> !m.isActive())
                .map(m -> (PassiveModifier) m)
                .filter(m -> m.effectKey() == effect)
                .findFirst()
                .orElse(null);
    }
}