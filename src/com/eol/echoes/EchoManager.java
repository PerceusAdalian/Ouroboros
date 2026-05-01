package com.eol.echoes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.eol.echoes.records.EchoManifest;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class EchoManager 
{
	private static final Set<UUID> durabilityLock = new HashSet<>();

	public enum DurabilityOperation
	{
		SUBTRACT,
		ADD,
		RESTORE,
		DIMINISH,
		SETMAX
	}
	
	/**
	 * @param player
	 * @param stack -- Assumes an item stack that may or may not be an echo.
	 * @param durabilityType -- An operation type: 
	 * @DurabilityOperation1 ADD -> Adds the value to the durability. 
	 * @DurabilityOperation2 SUBTRACT -> Subtracts the value from the durability. 
	 * @DurabilityOperation3 RESTORE -> Restores durability as a percent of the value back to the echo.
	 * @DurabilityOperation4 DIMINISH -> Reduces durability as a percent of the value from the echo.
	 * @DurabilityOperation5 SETMAX -> Sets the echo's durability to it's base default max.
	 * @param value
	 */
	public static void modifyDurability(Player player, ItemStack stack, DurabilityOperation operation, int value, boolean setOffhand)
	{
	    if (!EchoManifestCodec.isEcho(stack)) return;
	    
	    EchoManifest manifest = EchoManifestCodec.read(stack);
	    if (manifest == null) return;

	    EchoData data = manifest.baseStats();

	    int current = 0;
	    if (operation == DurabilityOperation.ADD)
	    	current = Math.min(data.getCurrentDurability() + value, data.getMaxDurability());
	    else if (operation == DurabilityOperation.SUBTRACT) 
	    	current = Math.max(0, data.getCurrentDurability() - value);
	    else if (operation == DurabilityOperation.RESTORE) 
	    	current = (int) Math.min(data.getCurrentDurability() + (value / 100.0) * data.getMaxDurability(), data.getMaxDurability());
	    else if (operation == DurabilityOperation.DIMINISH)
	    	current = (int) Math.max(0, data.getCurrentDurability() - (value / 100.0) * data.getMaxDurability());
	    else if (operation == DurabilityOperation.SETMAX)
	    	current = data.getMaxDurability();
	    
	    EchoData updatedEchoData = new EchoData(
	        data.getAttack(), data.getAttackRating(),
	        data.getCritRate(), data.getCritModifier(),
	        data.getMaxDurability(), current);

	    EchoManifest updatedManifest = new EchoManifest(
	        manifest.echoId(), manifest.rarity(), updatedEchoData,
	        manifest.modifiers(), manifest.slotType(),
	        manifest.equippedAbilityKey(), manifest.lockedAbilityKey(),
	        manifest.echoForm(), manifest.echoMaterial());

	    EchoManifestCodec.write(stack, updatedManifest);

	    ItemMeta meta = stack.getItemMeta();
	    if (meta != null)
	    {
	        List<String> lore = manifest.echoMaterial() != null
	            ? EchoLoreBuilder.build(updatedManifest, manifest.echoMaterial())
	            : EchoLoreBuilder.build(updatedManifest);
	        meta.setLore(lore);
	        stack.setItemMeta(meta);
	    }
	    
	    durabilityLock.add(player.getUniqueId());
	    Bukkit.getScheduler().runTask(Ouroboros.instance, () ->
	    {
	    	if (setOffhand) player.getInventory().setItemInOffHand(stack);
	    	else player.getInventory().setItemInMainHand(stack);
	        durabilityLock.remove(player.getUniqueId());
	    });
	    
	    if (current <= 0)
	    {
	        player.getInventory().setItemInMainHand(null);
	        EntityEffects.playSound(player, Sound.ENTITY_BREEZE_INHALE, SoundCategory.MASTER);
	        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	        {
	            EntityEffects.playSound(player, Sound.ITEM_SHIELD_BREAK, SoundCategory.MASTER);
	            PrintUtils.PrintToActionBar(player, PrintUtils.color(ObsColors.CELESTIO) + "&cYour Echo has shattered..");
	        }, 5);
	    }
	}
	
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
