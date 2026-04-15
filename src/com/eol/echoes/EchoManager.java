package com.eol.echoes;

import org.bukkit.inventory.ItemStack;

import com.eol.echoes.records.EchoManifest;

public class EchoManager 
{
	
	/**
     * Equips an ability onto a procedural Echo, and attempts to rebuild the item stack.
     * Fails silently (returns false) if the echo has a locked ability, no slot, or the key is blank.
     * Also fails if the item could not be rebuilt; but that's an edge case.
     */
	public ItemStack equipAbility(ItemStack item, String abilityKey)
	{
	    if (item == null || abilityKey == null || abilityKey.isBlank()) return null;

	    EchoManifest manifest = EchoManifestCodec.read(item);
	    if (manifest == null) return null;
	    if (manifest.hasLockedAbility()) return null;
	    if (!manifest.hasElementiumSlot()) return null;

	    EchoManifest updated = withEquippedAbility(manifest, abilityKey);

	    ItemStack rebuilt = EchoForge.rebuild(updated);
	    if (rebuilt == null) return null;

	    // Copy rebuilt item back into the original stack
	    item.setType(rebuilt.getType());
	    item.setItemMeta(rebuilt.getItemMeta());
	    return item;
	}
    
    /**
     * Removes the equipped ability from a procedural Echo.
     * Will not remove a locked ability.
     * Attempts to also rebuild the item, and silently fails if any operation results in a null item stack.
     */
	public ItemStack removeAbility(ItemStack item)
	{
	    if (item == null) return null;

	    EchoManifest manifest = EchoManifestCodec.read(item);
	    if (manifest == null) return null;
	    if (manifest.hasLockedAbility()) return null;
	    if (!manifest.hasEquippedAbility()) return null;

	    EchoManifest updated = withEquippedAbility(manifest, null);

	    ItemStack rebuilt = EchoForge.rebuild(updated);
	    if (rebuilt == null) return null;

	    item.setType(rebuilt.getType());
	    item.setItemMeta(rebuilt.getItemMeta());
	    return item;
	}

    /**
     * Simply returns an echo manifest with the old data wrapped alongside an input ability's key (internal name).
     */
    private EchoManifest withEquippedAbility(EchoManifest old, String abilityKey) 
    {
        return new EchoManifest(
            old.echoId(),
            old.rarity(),
            old.baseStats(),
            old.modifiers(),
            old.slotType(),
            abilityKey,
            old.lockedAbilityKey(),
            old.echoForm(),
            old.echoMaterial()
        );
    }
}
