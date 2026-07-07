package com.eol.echoes;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.eol.echoes.records.ActiveArmorModifier;
import com.eol.echoes.records.ActiveEchoModifier;
import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.eol.enums.ArmorStat;
import com.eol.enums.CombatStat;
import com.eol.enums.EchoForm;
import com.eol.enums.EchoMaterial;
import com.eol.enums.ElementiumSlotType;
import com.eol.enums.ModifierCondition;
import com.eol.enums.PassiveEchoEffect;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.Rarity;

/**
 * EchoManifestCodec handles serialization and deserialization of EchoManifest
 * to and from the item's PersistentDataContainer.
 *
 * The manifest is stored as a single JSON string under the key "echo_manifest".
 *
 * Modifier type tags:
 *   "active_weapon" — ActiveEchoModifier  (CombatStat,  weapon/tool echoes)
 *   "active_armor"  — ActiveArmorModifier (ArmorStat,   armor echoes)
 *   "passive"       — PassiveModifier     (both types)
 *
 * Legacy manifests written with "active" are deserialized as ActiveEchoModifier
 * so existing weapon echoes on existing items keep working.
 */
public class EchoManifestCodec
{
    private static final Gson GSON = new GsonBuilder().create();

    public static final NamespacedKey MANIFEST_KEY = new NamespacedKey(Ouroboros.instance, "echo_manifest");

    // -------------------------------------------------------------------------
    // Write
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // Serialization
    // -------------------------------------------------------------------------

    public static String toJson(EchoManifest manifest)
    {
        JsonObject root = new JsonObject();

        root.addProperty("echoId",       manifest.echoId());
        root.addProperty("rarity",       manifest.rarity().name());
        root.addProperty("echoType",     manifest.isArmorEcho() ? "ARMOR" : "WEAPON");
        root.addProperty("slotType", manifest.slotType() != null 
                ? manifest.slotType().name() 
                : ElementiumSlotType.NO_SLOT.name());
        root.addProperty("echoForm",     manifest.echoForm()     != null ? manifest.echoForm().name()     : null);
        root.addProperty("echoMaterial", manifest.echoMaterial() != null ? manifest.echoMaterial().name() : null);
        if (manifest.eolInternalName() != null)
            root.addProperty("eolInternalName", manifest.eolInternalName());
        
        // Stat block — only the relevant one is written
        if (manifest.isArmorEcho())
        {
            ArmorData a = manifest.armorStats();
            JsonObject armorStats = new JsonObject();
            armorStats.addProperty("armorRating",         a.getArmorRating());
            armorStats.addProperty("blockRate",           a.getBlockRate());
            armorStats.addProperty("criticalArmorRating", a.getCriticalArmorRating());
            armorStats.addProperty("criticalBlockRate",   a.getCriticalBlockRate());
            armorStats.addProperty("maxDurability",       a.getMaxDurability());
            armorStats.addProperty("currentDurability",   a.getCurrentDurability());
            root.add("armorStats", armorStats);
        }
        else
        {
            EchoData d = manifest.baseStats();
            JsonObject baseStats = new JsonObject();
            baseStats.addProperty("attack",            d.getAttack());
            baseStats.addProperty("attackRating",      d.getAttackRating());
            baseStats.addProperty("critRate",          d.getCritRate());
            baseStats.addProperty("critModifier",      d.getCritModifier());
            baseStats.addProperty("maxDurability",     d.getMaxDurability());
            baseStats.addProperty("currentDurability", d.getCurrentDurability());
            root.add("baseStats", baseStats);

            root.addProperty("lockedAbilityKey", manifest.lockedAbilityKey());
            root.addProperty("equippedAbility",  manifest.equippedAbilityKey());
        }

        // Modifiers — type tag distinguishes the three concrete Modifier subtypes
        JsonArray mods = new JsonArray();
        for (Modifier mod : manifest.modifiers())
        {
            JsonObject m = new JsonObject();

            if (mod instanceof ActiveArmorModifier armor)
            {
                m.addProperty("type",       "active_armor");
                m.addProperty("condition",  armor.condition().name());
                m.addProperty("armorStat",  armor.armorStat().name());
                m.addProperty("magnitude",  armor.magnitude());
                m.addProperty("isPercent",  armor.isPercent());
                m.addProperty("isNegative", armor.isNegative());
            }
            else if (mod instanceof ActiveEchoModifier weapon)
            {
                m.addProperty("type",       "active_weapon");
                m.addProperty("condition",  weapon.condition().name());
                m.addProperty("combatStat", weapon.combatStat().name());
                m.addProperty("magnitude",  weapon.magnitude());
                m.addProperty("isPercent",  weapon.isPercent());
                m.addProperty("isNegative", weapon.isNegative());
            }
            else if (mod instanceof PassiveModifier passive)
            {
                m.addProperty("type",      "passive");
                m.addProperty("condition", passive.condition().name());
                m.addProperty("effectKey", passive.effectKey().name());
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

        String             echoId = root.get("echoId").getAsString();
        Rarity             rarity = Rarity.valueOf(root.get("rarity").getAsString());
        ElementiumSlotType slot   = ElementiumSlotType.valueOf(root.get("slotType").getAsString());

        JsonElement echoTypeEl = root.get("echoType");
        boolean isArmor = echoTypeEl != null && !echoTypeEl.isJsonNull()
                && "ARMOR".equals(echoTypeEl.getAsString());

        JsonElement echoFormEl = root.get("echoForm");
        EchoForm echoForm = (echoFormEl == null || echoFormEl.isJsonNull())
                ? null : EchoForm.valueOf(echoFormEl.getAsString());

        JsonElement echoMaterialEl = root.get("echoMaterial");
        EchoMaterial echoMaterial = (echoMaterialEl == null || echoMaterialEl.isJsonNull())
                ? null : EchoMaterial.valueOf(echoMaterialEl.getAsString());
        
        JsonElement eolNameEl = root.get("eolInternalName");
        String eolInternalName = (eolNameEl == null || eolNameEl.isJsonNull()) ? null : eolNameEl.getAsString();
        
        // Modifiers
        List<Modifier> modifiers = new ArrayList<>();
        JsonArray modsArr = root.getAsJsonArray("modifiers");
        for (JsonElement el : modsArr)
        {
            JsonObject m    = el.getAsJsonObject();
            String     type = m.get("type").getAsString();
            ModifierCondition condition = ModifierCondition.valueOf(m.get("condition").getAsString());

            switch (type)
            {
                case "active_armor" ->
                {
                    ArmorStat armorStat = ArmorStat.valueOf(m.get("armorStat").getAsString());
                    double    magnitude = m.get("magnitude").getAsDouble();
                    boolean   isPercent = m.get("isPercent").getAsBoolean();
                    boolean   isNegative = m.get("isNegative").getAsBoolean();
                    modifiers.add(new ActiveArmorModifier(condition, armorStat, magnitude, isPercent, isNegative));
                }
                // "active_weapon" and legacy "active" both deserialize as ActiveEchoModifier
                case "active_weapon", "active" ->
                {
                    CombatStat combatStat = CombatStat.valueOf(m.get("combatStat").getAsString());
                    double     magnitude  = m.get("magnitude").getAsDouble();
                    boolean    isPercent  = m.get("isPercent").getAsBoolean();
                    boolean    isNegative = m.get("isNegative").getAsBoolean();
                    modifiers.add(new ActiveEchoModifier(condition, combatStat, magnitude, isPercent, isNegative));
                }
                case "passive" ->
                {
                    PassiveEchoEffect effectKey = PassiveEchoEffect.valueOf(m.get("effectKey").getAsString());
                    double magnitude = m.get("magnitude").getAsDouble();
                    modifiers.add(new PassiveModifier(condition, effectKey, magnitude));
                }
            }
        }

        if (isArmor)
        {
            JsonObject a = root.getAsJsonObject("armorStats");

            JsonElement maxDurEl  = a.get("maxDurability");
            JsonElement curDurEl  = a.get("currentDurability");
            int maxDurability     = (maxDurEl == null || maxDurEl.isJsonNull())  ? 200 : maxDurEl.getAsInt();
            int currentDurability = (curDurEl == null || curDurEl.isJsonNull())  ? maxDurability : curDurEl.getAsInt();

            ArmorData armorStats = new ArmorData(
                    a.get("armorRating").getAsInt(),
                    a.get("blockRate").getAsDouble(),
                    a.get("criticalArmorRating").getAsInt(),
                    a.get("criticalBlockRate").getAsDouble(),
                    maxDurability, currentDurability);

            return new EchoManifest(echoId, rarity, null, armorStats, modifiers, 
            		ElementiumSlotType.NO_SLOT, null, null, echoForm, echoMaterial, eolInternalName);
        }
        else
        {
            JsonObject s = root.getAsJsonObject("baseStats");

            JsonElement maxDurEl  = s.get("maxDurability");
            JsonElement curDurEl  = s.get("currentDurability");
            int maxDurability     = (maxDurEl == null || maxDurEl.isJsonNull())  ? 200 : maxDurEl.getAsInt();
            int currentDurability = (curDurEl == null || curDurEl.isJsonNull())  ? maxDurability : curDurEl.getAsInt();

            EchoData baseStats = new EchoData(
                    s.get("attack").getAsDouble(),
                    s.get("attackRating").getAsDouble(),
                    s.get("critRate").getAsDouble(),
                    s.get("critModifier").getAsDouble(),
                    maxDurability, currentDurability);

            JsonElement lockedEl   = root.get("lockedAbilityKey");
            JsonElement equippedEl = root.get("equippedAbility");
            String lockedAbilityKey = (lockedEl   == null || lockedEl.isJsonNull())   ? null : lockedEl.getAsString();
            String abilityKey       = (equippedEl == null || equippedEl.isJsonNull()) ? null : equippedEl.getAsString();

            return new EchoManifest(echoId, rarity, baseStats, null, modifiers, slot, 
            		abilityKey, lockedAbilityKey, echoForm, echoMaterial, eolInternalName);
        }
    }
}