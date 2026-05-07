package com.eol.echoes.abilities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.eol.echoes.EchoData;
import com.eol.echoes.EchoManager;
import com.eol.echoes.EchoManifestCodec;
import com.eol.echoes.EchoManager.DurabilityOperation;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.echoes.records.EchoManifest;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class AbilityCastHandler implements Listener 
{

    public static void register(JavaPlugin plugin) 
    {
        Bukkit.getPluginManager().registerEvents(new AbilityCastHandler(), plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) 
    {
        Player p = e.getPlayer();
        ItemStack held = p.getInventory().getItem(EquipmentSlot.HAND);

        if (e.getHand() == null) return;
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
        if (held == null || held.getType() == Material.AIR) return;
        if (!EchoManager.isEcho(held)) return;
        
        ItemMeta meta = held.getItemMeta();
        if (meta == null) return;
        
        String echokey = meta.getPersistentDataContainer().get(EchoManifestCodec.MANIFEST_KEY, PersistentDataType.STRING);
        if (echokey == null) return;
        
        EchoManifest codec = EchoManifestCodec.fromJson(echokey);
        if (codec == null) return;
        
        EchoData data = codec.baseStats();
        
        String abilityKey = codec.hasEquippedAbility() ? codec.getAbilityKey()
                : codec.hasLockedAbility() ? codec.getLockedAbilityKey() : null;
		if (abilityKey == null) return;
		EchoAbility ability = EchoAbility.get(abilityKey);
		if (ability == null) return;
        
        if (CastConditions.isValidAction(e, ability.getCastCondition()))
        {
        	int currentDurability = data.getCurrentDurability();
        	if (currentDurability < ability.getFinalDurabilityCost()) 
        	{
        		EntityEffects.playSound(p, Sound.BLOCK_CHAIN_STEP, SoundCategory.AMBIENT);
        		PrintUtils.PrintToActionBar(p, "&r&fNot Enough &bDurability&f!");
        		return;
        	}
        	
        	int cost = ability.cast(e);
        	if (cost < 0) return;
        	e.setCancelled(true);
        	EchoManager.modifyDurability(p, held, DurabilityOperation.SUBTRACT, cost, false);
        }
    }
}

