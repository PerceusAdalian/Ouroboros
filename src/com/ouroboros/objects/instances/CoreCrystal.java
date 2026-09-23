package com.ouroboros.objects.instances;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.eol.echoes.EchoForge;
import com.eol.echoes.EchoManager;
import com.eol.materia.Materia;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.InventoryUtils;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class CoreCrystal extends AbstractObsObject
{
    public static final NamespacedKey RARITY_KEY = new NamespacedKey(Ouroboros.instance, "core_crystal_rarity");

    public CoreCrystal()
    {
        super("&e&lCore Crystal", "core_crystal", Material.NETHER_STAR, false, true, buildDescription(Rarity.ONE));
    }

    private static String[] buildDescription(Rarity rarity)
    {
        return new String[] 
        {
            PrintUtils.assignRarity(rarity), "",
            "&r&fA resonant relic bound by a Soul-Core contract,",
            "&r&fand a sealed identity engram lies dormant within it:", "",
            "&r&b&lSoul-Core Magnitude&r&f: " + PrintUtils.getRarityAsNumeralValue(rarity), "",
            "&r&fUsage: &d&oShift_Right-Click&r&f to summon an &b&o"+Symbols.EOL+"cho&r&f of its &b&omagnitude&r&f.", "",
            "&6&lTags&r&f: &c&lIndestructible&r&f, &b&oStackable&r&f, &a&oConsumable"
        };
    }

    public static ItemStack createItem(Rarity rarity)
    {
        ItemStack stack = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = new ArrayList<>();

        meta.setDisplayName(PrintUtils.ColorParser("&e&lCore Crystal &r"+PrintUtils.getRarityAsNumeralValue(rarity)));

        lore.add("\n");
        for (String line : buildDescription(rarity))
        {
            lore.add(PrintUtils.ColorParser("&r&f" + line) + "\n");
        }
        lore.add("\n");
        lore.add(PrintUtils.ColorParser("&r&7&oOBS Object ID: Ω_" + new CoreCrystal().getInternalNameAsID()));

        meta.setLore(lore);
        meta.getPersistentDataContainer().set(obsObject, PersistentDataType.STRING, "core_crystal");
        meta.getPersistentDataContainer().set(RARITY_KEY, PersistentDataType.STRING, rarity.name());

        stack.setItemMeta(meta);
        return stack;
    }

    public static Rarity decodeRarity(ItemStack item)
    {
        if (item == null || !item.hasItemMeta()) return null;
        String raw = item.getItemMeta().getPersistentDataContainer().get(RARITY_KEY, PersistentDataType.STRING);
        return raw == null ? null : Rarity.valueOf(raw);
    }

    @Override
    public boolean cast(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();
        if (!PlayerActions.rightClickAir(e)) return false;

        Rarity rarity = decodeRarity(p.getInventory().getItemInMainHand());
        if (rarity == null) return false;

        ItemStack forgedEcho = EchoForge.forge(Materia.getCatalyst(rarity), Materia.randomizeBase(rarity), Materia.randomizeBinding(rarity), Materia.randomizeElementCore());
        ItemCollector.remove(e);
        EntityEffects.playSound(p, Sound.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.AMBIENT);

        ElementType eType = ElementType.getFromElementiumSlotType(EchoManager.getCodec(forgedEcho).getElementiumSlotType());

        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
        {
            if (eType == null) ObsParticles.drawCylinder(p.getLocation(), p.getWidth(), 3, 15, 0.45, 0.2, Particle.ENCHANT, null);
            else ObsParticles.playCastSigil(p, eType);
            EntityEffects.playSound(p, Sound.BLOCK_VAULT_EJECT_ITEM, SoundCategory.MASTER);
            InventoryUtils.add(p, forgedEcho);
        }, 10);

        return true;
    }
}