package com.eol.echoes.instances;

import java.util.ArrayList;
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
import org.bukkit.persistence.PersistentDataType;

import com.eol.echoes.EchoData;
import com.eol.echoes.EchoFormResolver;
import com.eol.echoes.EchoLoreBuilder;
import com.eol.echoes.EchoManifestCodec;
import com.eol.echoes.MateriaTypeResolver;
import com.eol.echoes.records.EOLRecipe;
import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.Modifier;
import com.eol.enums.EchoForm;
import com.eol.enums.EchoMaterial;
import com.eol.enums.ElementiumSlotType;
import com.eol.materia.Materia;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.Nullable;
import com.ouroboros.utils.PrintUtils;

public abstract class AbstractEOLWeapon extends AbstractEOL
{
    private final EchoData echoData;

    public AbstractEOLWeapon(
            String displayName,
            String internalName,
            boolean isIntegrityArmament,
            EOLRecipe recipe,
            EchoForm form,
            ElementiumSlotType slotType,
            List<Modifier> modifiers,
            EchoData echoData,
            @Nullable String lockedAbilityKey,
            @Nullable String... description)
    {
        super(displayName, internalName, isIntegrityArmament,
              recipe, form, slotType, modifiers, lockedAbilityKey, description);
        this.echoData = echoData;
    }

    @Override
    public final ItemStack forge(Materia catalyst, Materia base, boolean isIntegrityArmament)
    {
        Rarity rarity             = catalyst.getRarity();
        EchoMaterial echoMaterial = MateriaTypeResolver.toEchoMaterial(base.getMateriaType());

        EchoManifest manifest = new EchoManifest(
                buildEchoId(),
                rarity,
                echoData,
                null,
                getModifiers(),
                getSlotType(),
                null,
                getLockedAbilityKey(),
                getForm(),
                echoMaterial,
                getInternalName());

        return rebuildFromManifest(manifest);
    }

    /** Rebuilds the ItemStack from an existing manifest (ability equip/remove, durability patch, etc.)
     * without re-rolling stats. Keeps the EOL's authored name, lore, and eolKey intact. */
    public ItemStack rebuildFromManifest(EchoManifest manifest)
    {
        EchoMaterial echoMaterial = manifest.echoMaterial();
        Material material = EchoFormResolver.toBukkitMaterial(getForm(), echoMaterial);
        if (material == null) return null;

        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta   = stack.getItemMeta();
        if (meta == null) return null;

        meta.setDisplayName(PrintUtils.ColorParser(getDisplayName()));
        meta.setLore(buildLore(manifest, echoMaterial, isIntegrityArmament()));
        meta.setEnchantmentGlintOverride(true);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        meta.getPersistentDataContainer().set(eolKey, PersistentDataType.STRING, getInternalName());

        applyAttackSpeed(meta, manifest);

        stack.setItemMeta(meta);
        EchoManifestCodec.write(stack, manifest);

        return stack;
    }
    
    private void applyAttackSpeed(ItemMeta meta, EchoManifest manifest)
    {
        meta.removeAttributeModifier(Attribute.ATTACK_SPEED);
        double attacksPerSecond = manifest.baseStats().getAttackRating();
        if (attacksPerSecond <= 0) return;
        double delta = attacksPerSecond - 4.0;
        NamespacedKey key = new NamespacedKey(Ouroboros.instance, "echo_attack_speed");
        AttributeModifier mod = new AttributeModifier(key, delta, Operation.ADD_NUMBER, EquipmentSlotGroup.ANY);
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, mod);
    }

    private List<String> buildLore(EchoManifest manifest, EchoMaterial echoMaterial, boolean isIntegrityArmament)
    {
        List<String> lore = new ArrayList<>(EchoLoreBuilder.build(manifest, echoMaterial));

        String[] description = getDescription();
        if (description != null && description.length > 0)
        {
            int idIndex = lore.size() - 1;
            lore.add(idIndex, "");
            for (int i = description.length - 1; i >= 0; i--)
                lore.add(idIndex, PrintUtils.ColorParser("&r&7&o" + description[i]));
        }

        lore.add(0, armamentTag());
        return lore;
    }

    private String buildEchoId()
    {
        String shortUUID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return getInternalName().toUpperCase().replace(" ", "_") + "-" + shortUUID;
    }

    public EchoData getEchoData() { return echoData; }
}