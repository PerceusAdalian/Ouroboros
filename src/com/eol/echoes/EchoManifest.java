package com.eol.echoes;

import java.util.Collections;
import java.util.List;

import com.eol.echoes.modifiers.Modifier;
import com.eol.enums.ElementiumSlotType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.Nullable;

/**
 * EchoManifest is the complete, immutable description of a procedurally generated Echo.
 * It is created once at forge time and then serialized into the item's PersistentDataContainer.
 * All combat and modifier logic reads from the deserialized manifest at runtime — nothing is re-rolled.
 *
 * For EOL (Echo of Lumina) items, the manifest is hand-authored rather than rolled,
 * but the same structure and codec are used.
 */
public record EchoManifest(String echoId, Rarity rarity, EchoData baseStats, List<Modifier> modifiers, ElementiumSlotType slotType, @Nullable String lockedAbilityKey)
{
    /**
     * Compact constructor — defensively copies the modifier list so the manifest
     * is truly immutable after construction.
     */
    public EchoManifest
    {
        modifiers = Collections.unmodifiableList(modifiers);
    }
 
    /**
     * Convenience constructor for procedural Echoes (no locked ability).
     */
    public EchoManifest(String echoId, Rarity rarity, EchoData baseStats, List<Modifier> modifiers, ElementiumSlotType slotType)
    {
        this(echoId, rarity, baseStats, modifiers, slotType, null);
    }
 
    /**
     * Returns true if this Echo has a permanently locked ability (EOL only).
     */
    public boolean hasLockedAbility()
    {
        return lockedAbilityKey != null && !lockedAbilityKey.isBlank();
    }
    
    public boolean hasElementiumSlot()
    {
        return slotType != null && slotType != ElementiumSlotType.NO_SLOT;
    }
 
    /**
     * Returns true if any modifier on this Echo is active (i.e. directly mutates combat stats).
     */
    public boolean hasActiveModifiers()
    {
        return modifiers.stream().anyMatch(Modifier::isActive);
    }
 
    /**
     * Filters and returns only the active modifiers on this manifest.
     */
    public List<Modifier> getActiveModifiers()
    {
        return modifiers.stream().filter(Modifier::isActive).toList();
    }
 
    /**
     * Filters and returns only the passive modifiers on this manifest.
     */
    public List<Modifier> getPassiveModifiers()
    {
        return modifiers.stream().filter(m -> !m.isActive()).toList();
    }
}
