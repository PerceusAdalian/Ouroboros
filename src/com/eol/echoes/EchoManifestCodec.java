package com.eol.echoes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.eol.echoes.modifiers.ActiveModifier;
import com.eol.echoes.modifiers.PassiveModifier;
import com.eol.echoes.modifiers.WeaponModifier;
import com.eol.enums.CombatStat;
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
 
    public static final NamespacedKey MANIFEST_KEY = new NamespacedKey(Ouroboros.instance, "echo:manifest");
 
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
 
        root.addProperty("echoId",   manifest.echoId());
        root.addProperty("rarity",   manifest.rarity().toString());
        root.addProperty("slotType", manifest.slotType().name());
 
        // Base stats
        JsonObject stats = new JsonObject();
        EchoData data = manifest.baseStats();
        
        stats.addProperty("attack",        data.getAttack());
        stats.addProperty("attackRating",  data.getAttackRating());
        stats.addProperty("critRate",      data.getCritRate());
        stats.addProperty("critModifier",  data.getCritModifier());
 
        root.add("baseStats", 			   stats);

        // Modifiers
        JsonArray mods = new JsonArray();
        for (WeaponModifier mod : manifest.modifiers())
        {
            JsonObject m = new JsonObject();
            if (mod instanceof ActiveModifier activeModifier)
            {
                m.addProperty("type",       "active");
                m.addProperty("condition",  activeModifier.condition().name());
                m.addProperty("combatStat", activeModifier.combatStat().name());
                m.addProperty("magnitude",  activeModifier.magnitude());
                m.addProperty("isPercent",  activeModifier.isPercent());
            }
            else if (mod instanceof PassiveModifier passiveModifier)
            {
                m.addProperty("type",       "passive");
                m.addProperty("condition",  passiveModifier.condition().name());
                m.addProperty("effectKey",  passiveModifier.effectKey());
                m.addProperty("magnitude",  passiveModifier.magnitude());
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
 
        String             echoId   = root.get("echoId").getAsString();
        Rarity             rarity   = Rarity.valueOf(root.get("rarity").getAsString());
        ElementiumSlotType slot     = ElementiumSlotType.valueOf(root.get("slotType").getAsString());
 
        JsonObject statsObj = root.getAsJsonObject("baseStats");
        EchoData baseStats = new EchoData(
                statsObj.get("attack").getAsDouble(),
                statsObj.get("attackRating").getAsDouble(),
                statsObj.get("critRate").getAsDouble(),
                statsObj.get("critModifier").getAsDouble());
 
        List<WeaponModifier> modifiers = new ArrayList<>();
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
 
        return new EchoManifest(echoId, rarity, baseStats, modifiers, slot);
    }
}
