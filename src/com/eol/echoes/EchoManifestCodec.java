package com.eol.echoes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.eol.echoes.records.ActiveModifier;
import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.eol.enums.CombatStat;
import com.eol.enums.EchoForm;
import com.eol.enums.EchoMaterial;
import com.eol.enums.ElementiumSlotType;
import com.eol.enums.WeaponModifierCondition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.Rarity;

/**
 * EchoManifestCodec handles all serialization and deserialization of EchoManifest
 * to and from the item's PersistentDataContainer.
 *
 * The manifest is stored as a single JSON string under the key "echo:manifest".
 * This keeps the PDC footprint to one key regardless of modifier count.
 *
 * JSON structure:
 * {
 *   "echoId":   "string",
 *   "rarity":   "LEGENDARY",
 *   "slotType": "INFERNO",
 *   "baseStats": {
 *     "attack": 15.0, "attackRating": 0.75, "critRate": 0.25, "critModifier": 2.0
 *   },
 *   "modifiers": [
 *     { "type": "active",  "condition": "UNDEAD", "statType": "MELEE",  "magnitude": 0.50, "isPercent": true },
 *     { "type": "passive", "condition": "PASSIVE","effectKey": "apply_expose", "magnitude": 0.0 }
 *   ]
 * }
 */
public class EchoManifestCodec
{
    private static final Gson GSON = new GsonBuilder().create();
 
    public static final NamespacedKey MANIFEST_KEY = new NamespacedKey(Ouroboros.instance, "echo_manifest");
 
    // -------------------------------------------------------------------------
    // Write
    // -------------------------------------------------------------------------
 
    /**
     * Serializes the manifest to JSON and writes it into the item's PDC.
     * Returns the same ItemStack for chaining.
     */
    public static ItemStack write(ItemStack item, EchoManifest manifest)
    {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;
 
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(MANIFEST_KEY, PersistentDataType.STRING, toJson(manifest));
 
        item.setItemMeta(meta);
        return item;
    }
 
    // -------------------------------------------------------------------------
    // Read
    // -------------------------------------------------------------------------
 
    /**
     * Reads and deserializes the manifest from the item's PDC.
     * Returns null if the item has no manifest (i.e. is not an Echo).
     */
    public static EchoManifest read(ItemStack item)
    {
        if (item == null) return null;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
 
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(MANIFEST_KEY, PersistentDataType.STRING)) return null;
 
        String json = pdc.get(MANIFEST_KEY, PersistentDataType.STRING);
        return fromJson(json);
    }
 
    /**
     * Convenience: returns true if the item carries a manifest (i.e. is an Echo).
     */
    public static boolean isEcho(ItemStack item)
    {
        if (item == null) return false;
        
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        
        return meta.getPersistentDataContainer().has(MANIFEST_KEY, PersistentDataType.STRING);
    }
 
    // -------------------------------------------------------------------------
    // Serialization
    // -------------------------------------------------------------------------
 
    public static String toJson(EchoManifest manifest)
    {
        JsonObject root = new JsonObject();
 
        root.addProperty("echoId",           manifest.echoId());
        root.addProperty("rarity",           manifest.rarity().name());
        root.addProperty("slotType",         manifest.slotType().name());
        root.addProperty("lockedAbilityKey", manifest.lockedAbilityKey()); // null-safe — Gson writes JSON null
        root.addProperty("equippedAbility",  manifest.equippedAbilityKey());
        root.addProperty("echoForm",     	 manifest.echoForm()     != null ? manifest.echoForm().name()     : null);
        root.addProperty("echoMaterial", 	 manifest.echoMaterial() != null ? manifest.echoMaterial().name() : null);
        // Base stats
        JsonObject stats = new JsonObject();
        EchoData data = manifest.baseStats();
        stats.addProperty("attack",        data.getAttack());
        stats.addProperty("attackRating",  data.getAttackRating());
        stats.addProperty("critRate",      data.getCritRate());
        stats.addProperty("critModifier",  data.getCritModifier());
        stats.addProperty("maxDurability",     data.getMaxDurability());
        stats.addProperty("currentDurability", data.getCurrentDurability());
        root.add("baseStats", stats);
 
        // Modifiers
        JsonArray mods = new JsonArray();
        for (Modifier mod : manifest.modifiers())
        {
            JsonObject m = new JsonObject();
            if (mod instanceof ActiveModifier active)
            {
                m.addProperty("type",       "active");
                m.addProperty("condition",  active.condition().name());
                m.addProperty("combatStat", active.combatStat().name());
                m.addProperty("magnitude",  active.magnitude());
                m.addProperty("isPercent",  active.isPercent());
            }
            else if (mod instanceof PassiveModifier passive)
            {
                m.addProperty("type",      "passive");
                m.addProperty("condition", passive.condition().name());
                m.addProperty("effectKey", passive.effectKey());
                m.addProperty("magnitude", passive.magnitude());
            }
            mods.add(m);
        }
        root.add("modifiers", mods);

        return GSON.toJson(root);
    }
 
    // -------------------------------------------------------------------------
    // Deserialization
    // -------------------------------------------------------------------------
 
    public static EchoManifest fromJson(String json)
    {
        JsonObject root = GSON.fromJson(json, JsonObject.class);
 
        String             echoId   	= root.get("echoId").getAsString();
        Rarity             rarity   	= Rarity.valueOf(root.get("rarity").getAsString());
        ElementiumSlotType slot     	= ElementiumSlotType.valueOf(root.get("slotType").getAsString());
        
        JsonElement echoFormElement = root.get("echoForm");
        EchoForm echoForm = (echoFormElement == null || echoFormElement.isJsonNull())
                ? null 
                : EchoForm.valueOf(echoFormElement.getAsString());

        JsonElement echoMaterialElement = root.get("echoMaterial");
        EchoMaterial echoMaterial = (echoMaterialElement == null || echoMaterialElement.isJsonNull())
                ? null 
                : EchoMaterial.valueOf(echoMaterialElement.getAsString());
        
        // lockedAbilityKey is nullable — check before calling getAsString()
        JsonElement lockedEl = root.get("lockedAbilityKey");
        String lockedAbilityKey = (lockedEl == null || lockedEl.isJsonNull())
                ? null 
                : lockedEl.getAsString();
 
        /**
         *  If the object that's being deserialized isn't an EOL echo, 
         *  We'll want to attempt to get an equipped ability, and if applicable, return that internal name to register.
         */
        
        JsonElement equippedAbility = root.get("equippedAbility");
        String abilityKey = (equippedAbility == null || equippedAbility.isJsonNull())
        		? null 
        		: equippedAbility.getAsString();
        
        JsonObject statsObj = root.getAsJsonObject("baseStats");
        
        JsonElement maxDurabilityElement = statsObj.get("maxDurability");
        int maxDurability = (maxDurabilityElement == null || maxDurabilityElement.isJsonNull()) ? 200 : maxDurabilityElement.getAsInt();

        JsonElement currentDurabilityElement = statsObj.get("currentDurability");
        int currentDurability = (currentDurabilityElement == null || currentDurabilityElement.isJsonNull()) ? maxDurability : currentDurabilityElement.getAsInt();

        EchoData baseStats = new EchoData(
                statsObj.get("attack").getAsDouble(),
                statsObj.get("attackRating").getAsDouble(),
                statsObj.get("critRate").getAsDouble(),
                statsObj.get("critModifier").getAsDouble(),
                maxDurability,
                currentDurability);
 
        List<Modifier> modifiers = new ArrayList<>();
        JsonArray modsArr = root.getAsJsonArray("modifiers");
        for (JsonElement el : modsArr)
        {
            JsonObject m = el.getAsJsonObject();
            String type = m.get("type").getAsString();
            WeaponModifierCondition condition = WeaponModifierCondition.valueOf(m.get("condition").getAsString());
 
            if (type.equals("active"))
            {
                CombatStat combatStat = CombatStat.valueOf(m.get("combatStat").getAsString());
                double     magnitude  = m.get("magnitude").getAsDouble();
                boolean    isPercent  = m.get("isPercent").getAsBoolean();
                modifiers.add(new ActiveModifier(condition, combatStat, magnitude, isPercent));
            }
            else if (type.equals("passive"))
            {
                String effectKey = m.get("effectKey").getAsString();
                double magnitude = m.get("magnitude").getAsDouble();
                modifiers.add(new PassiveModifier(condition, effectKey, magnitude));
            }
        }
 
        return new EchoManifest(echoId, rarity, baseStats, modifiers, slot, abilityKey, lockedAbilityKey, echoForm, echoMaterial);
    }
}