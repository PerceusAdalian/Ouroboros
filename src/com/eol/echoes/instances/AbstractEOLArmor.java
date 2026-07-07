package com.eol.echoes.instances;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.eol.echoes.ArmorData;
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
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.Nullable;
import com.ouroboros.utils.PrintUtils;

public abstract class AbstractEOLArmor extends AbstractEOL
{
    private final ArmorData armorData;

    public AbstractEOLArmor(
            String displayName,
            String internalName,
            EOLRecipe recipe,
            ArmorData armorData,
            EchoForm form,
            List<Modifier> modifiers,
            boolean isIntegrityArmament,
            @Nullable String... description)
    {
        super(displayName, internalName, isIntegrityArmament, recipe, form, 
        		null, modifiers, null, description);
        this.armorData = armorData;
    }

    @Override
    public final ItemStack forge(Materia catalyst, Materia base, boolean isIntegrityArmament)
    {
        Rarity rarity             = catalyst.getRarity();
        EchoMaterial echoMaterial = MateriaTypeResolver.toEchoMaterial(base.getMateriaType());

        EchoManifest manifest = new EchoManifest(
                buildEchoId(),
                rarity,
                null,
                armorData,
                getModifiers(),
                ElementiumSlotType.NO_SLOT,
                null,
                null,
                getForm(),
                echoMaterial,
                getInternalName());

        return rebuildFromManifest(manifest);
    }
    
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

        stack.setItemMeta(meta);
        EchoManifestCodec.write(stack, manifest);

        return stack;
    }

    private List<String> buildLore(EchoManifest manifest, EchoMaterial echoMaterial, boolean isIntegrityArmament)
    {
        List<String> lore = new ArrayList<>(EchoLoreBuilder.buildArmor(manifest, echoMaterial));

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

    public ArmorData getArmorData() { return armorData; }
}