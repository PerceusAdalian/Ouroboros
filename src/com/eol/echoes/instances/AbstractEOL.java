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

import com.eol.echoes.EchoData;
import com.eol.echoes.EchoFormResolver;
import com.eol.echoes.EchoLoreBuilder;
import com.eol.echoes.EchoManifestCodec;
import com.eol.echoes.MateriaTypeResolver;
import com.eol.echoes.config.StatResolver;
import com.eol.echoes.records.EOLRecipe;
import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.Modifier;
import com.eol.enums.EchoForm;
import com.eol.enums.EchoMaterial;
import com.eol.enums.ElementiumSlotType;
import com.eol.materia.Materia;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.Nullable;
import com.ouroboros.utils.PrintUtils;

/**
 * AbstractEOL is the authored base class for all Echo of Lumina (EOL) weapons.
 *
 * Subclasses define:
 *   - The display name and description
 *   - The EOLRecipe (required Materia types)
 *   - The EchoForm and ElementiumSlotType
 *   - The predetermined modifier list
 *   - The locked ability key (nullable)
 *
 * Stats (attack, attackRating, critRate, critModifier) are still rolled at forge
 * time from the provided Materia, so two instances of the same EOL can have
 * different stat rolls depending on the rarity of Materia used.
 *
 * EOLs are registered in EOLRegistry keyed on their special catalyst's internal name.
 */
public abstract class AbstractEOL
{
    private final String displayName;
    private final String internalName;
    private final EOLRecipe recipe;
    private final EchoForm form;
    private final ElementiumSlotType slotType;
    private final List<Modifier> modifiers;
    private final String lockedAbilityKey;   // nullable
    private final String[] description;
    private boolean isIntegrityArmament;
    protected AbstractEOL(
    		String displayName,
            String internalName,
            boolean isIntegrityArmament,
            EOLRecipe recipe,
            EchoForm form,
            ElementiumSlotType slotType,
            List<Modifier> modifiers,
            @Nullable String lockedAbilityKey,
            String... description)
    {
        this.displayName      	 = displayName;
        this.internalName        = internalName;
        this.isIntegrityArmament = isIntegrityArmament;
        this.recipe           	 = recipe;
        this.form                = form;
        this.slotType            = slotType;
        this.modifiers        	 = List.copyOf(modifiers);
        this.lockedAbilityKey 	 = lockedAbilityKey;
        this.description      	 = description;
    }

    // -------------------------------------------------------------------------
    // Forge
    // -------------------------------------------------------------------------

    /**
     * Forges this EOL into an ItemStack using the provided Materia for stat rolling.
     * The caller (EchoForge) is responsible for recipe validation before calling this.
     *
     * @param base        The BASE Materia (rarity influences stat roll)
     * @param binding     The BINDING Materia (rarity influences stat roll)
     * @param elementCore The ELEMENT_CORE Materia, or null if recipe requires none
     * @param catalyst    The special catalyst Materia (rarity used as EOL rarity)
     * @return The forged EOL ItemStack, or null if stat resolution fails
     */
    public final ItemStack forge(Materia catalyst, Materia base, Materia binding, Materia elementCore, boolean isIntegrityArmament)
    {
        EchoData stats = StatResolver.resolve(base, binding, catalyst.getRarity());
        if (stats == null) return null;

        Rarity rarity = catalyst.getRarity();
        EchoMaterial echoMaterial = MateriaTypeResolver.toEchoMaterial(base.getMateriaType());

        EchoManifest manifest = new EchoManifest(
                buildEchoId(catalyst),
                rarity,
                stats,
                modifiers,
                slotType,
                null,
                lockedAbilityKey,
                form,
                echoMaterial);

        Material material = EchoFormResolver.toBukkitMaterial(form, echoMaterial);
        if (material == null) return null;

        ItemStack stack = new ItemStack(material, 1);
        ItemMeta meta   = stack.getItemMeta();
        if (meta == null) return null;

        meta.setDisplayName(PrintUtils.ColorParser(displayName));
        meta.setLore(buildLore(manifest, echoMaterial, isIntegrityArmament));
        meta.setEnchantmentGlintOverride(true);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);

        // Apply attack speed — EOLs bypass EchoForge.buildItem so this must live here too
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

    // -------------------------------------------------------------------------
    // Lore construction
    // -------------------------------------------------------------------------

    private List<String> buildLore(EchoManifest manifest, EchoMaterial echoMaterial, boolean isIntegrityArmament)
    {
        List<String> lore = new ArrayList<>(EchoLoreBuilder.build(manifest, echoMaterial));

        // Append authored flavour description before the Echo ID line
        if (description != null && description.length > 0)
        {
            // Insert description before the last line (Echo ID)
            int idIndex = lore.size() - 1;
            lore.add(idIndex, "");
            for (int i = description.length - 1; i >= 0; i--)
                lore.add(idIndex, PrintUtils.ColorParser("&r&7&o" + description[i]));
        }

        // Mark as EOL at the top
        lore.add(0, PrintUtils.ColorParser(isIntegrityArmament 
        		? PrintUtils.color(ObsColors.CELESTIO)+"&lIntegrity Armament&r&f" 
        		: PrintUtils.color(ObsColors.HERESIO) + "&lTwilight Armament&r&f"));

        return lore;
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Generates a stable echo ID incorporating the catalyst's internal name
     * so two EOL instances from the same catalyst are distinguishable.
     */
    private String buildEchoId(Materia catalyst)
    {
        String shortUUID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return internalName.toUpperCase().replace(" ", "_")
                + "-" + shortUUID;
    }

    // -------------------------------------------------------------------------
    // Accessors
    // -------------------------------------------------------------------------

    public String getDisplayName()     
    { 
    	return displayName;
    }
    
    public String getInternalName()    
    { 
    	return internalName; 
    }
    
    public EOLRecipe getRecipe()       
    { 
    	return recipe; 
    }
    
    public EchoForm getForm()          
    { 
    	return form; 
    }
    
    public ElementiumSlotType getSlotType() 
    { 
    	return slotType; 
    }
    
    public List<Modifier> getModifiers() 
    { 
    	return modifiers; 
    }
    
    public String getLockedAbilityKey()
    { 
    	return lockedAbilityKey; 
    }
    
    public boolean hasLockedAbility()  
    { 
    	return lockedAbilityKey != null && !lockedAbilityKey.isBlank(); 
    }

	public boolean isIntegrityArmament()
	{
		return isIntegrityArmament;
	}
    
}
