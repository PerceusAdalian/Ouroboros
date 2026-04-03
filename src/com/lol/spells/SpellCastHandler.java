package com.lol.spells;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
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

import com.lol.spells.instances.Spell;
import com.lol.spells.instances.arcano.Freecast;
import com.lol.wand.Wand;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.magic.CollectWandData;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.ArcanoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class SpellCastHandler implements Listener
{
	public static void register(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new SpellCastHandler(), plugin);
	}
	
	private static Map<UUID, Set<Spell>> cooldownPlayers = new HashMap<>();
	public static Set<UUID> lockedCycling = new HashSet<>();
	public static Set<UUID> recentlyOverloaded = new HashSet<>();
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		ItemStack held = p.getInventory().getItemInMainHand();
		
		if (e.getHand() == null) return;
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) 
        {
        	e.setCancelled(true);
        	return;
        }
        if (held == null || held.getType() == Material.AIR) return;
		
        if (!Wand.isWand(held) && held.getItemMeta().getPersistentDataContainer().has(Spell.LOLSPELLBOOK)) 
		{
        	if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
        	{        		
        		for (Spell spell : SpellRegistry.spellRegistry.values())
        		{
        			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
        			ItemStack item = e.getItem();
        			if (item != null && item.hasItemMeta())
        			{
        				ItemMeta meta = item.getItemMeta();
        				String spellID = meta.getPersistentDataContainer().get(Spell.LOLSPELLBOOK, PersistentDataType.STRING);
        				
        				if (spellID != null && spellID.equals(spell.getInternalName()) && !data.getSpell(spell).isRegistered())
        				{
        					e.setCancelled(true);
        					EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, null);
        					OBSParticles.drawCylinder(p.getLocation(), p.getWidth(), 4, 10, 0.5, 0.5, Particle.ENCHANT, null);
        					PrintUtils.OBSFormatDebug(p, "&b&lSpell Registered&r&f: "+spell.getName()+"&r&f. Check your &d&o/obs spellbook&r&f!");
        					data.getSpell(spell).setRegistered(true);
        					data.save();
        					return;
        				}
        			}
        		}
        	}
        	
        	return;
		}

        if (!Wand.isWand(held)) return;
        
        Wand wand = new Wand(held);
        if (!wand.hasSpell(wand.getSpellIndex())) return;

        Spell currentSpell = wand.getSpell(wand.getSpellIndex());

        // Handle cycling — no cast involved at all
        if (CastConditions.isValidAction(e, CastConditions.SHIFT_LEFT_CLICK_AIR))
        {
            if (lockedCycling.contains(p.getUniqueId())) return;
            EntityEffects.playSound(p, Sound.ENTITY_BREEZE_CHARGE, SoundCategory.AMBIENT);
            CollectWandData.pageController.put(p.getUniqueId(), "removespell");
            GuiHandler.open(p, new CollectWandData(p));
            return;
        }
        if (CastConditions.isValidAction(e, CastConditions.LEFT_CLICK_AIR))
        {
            if (lockedCycling.contains(p.getUniqueId())) return;
            if (wand.getNextSpell() == null) return;
            wand.rotateSpells();
            p.getInventory().setItemInMainHand(wand.getAsItemStack());
            PrintUtils.PrintToActionBar(p, "&b&lEquipped Spell&r&f: " + wand.getSpell(wand.getSpellIndex()).getName());
            EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_NAUTILUS, SoundCategory.AMBIENT);
            return;
        }

        if (!CastConditions.isValidAction(e, currentSpell.getCastCondition())) return; // Only valid cast conditions allowed.

        Set<Spell> onCooldown = cooldownPlayers.get(p.getUniqueId());
        if (onCooldown != null && onCooldown.contains(currentSpell))
        {
            PrintUtils.PrintToActionBar(p, "&cSpell on Cooldown!");
            return;
        }

        PlayerData data = PlayerData.getPlayer(p.getUniqueId());
        if (wand.getRarity().getRarity() < currentSpell.getRarity().getRarity() ||
                data.getMagicProficiency() < wand.getRarity().getRarity())
        {
            PrintUtils.PrintToActionBar(p, "&cFizzle!");
            return;
        }

        if (ArcanoEffects.hasEtherDisruption.contains(p.getUniqueId()))
        {
            PrintUtils.PrintToActionBar(p, "&cFizzle: Ether Disrupted!");
            return;
        }

        int manaCost = currentSpell.getTotalManaCost();
        boolean usingFreecast = Freecast.hasFreecast.contains(p.getUniqueId()) && !(currentSpell instanceof Freecast);
        if (usingFreecast)
        {
            manaCost = 0;
            Freecast.hasFreecast.remove(p.getUniqueId());
        }
        if (wand.getCurrentMana() < manaCost)
        {
            PrintUtils.PrintToActionBar(p, "&r&fNot Enough &b&lMana&r&f!");
            EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.AMBIENT);
            return;
        }

        int actualCost = currentSpell.Cast(e);
        if (actualCost < 0) return;
        if (usingFreecast) actualCost = 0;

        // All checks passed — now actually cast
        
        if (wand.getElementType() != null)
        {
            ElementType spellementType = ElementType.getFromSpellement(currentSpell.getElementType());
            if (spellementType == wand.getElementType()) actualCost -= actualCost * .2;
            if (actualCost < 0) actualCost = 0;
        }
        
        boolean overloaded = actualCost > wand.getCurrentMana();
        long cooldown = (long)(overloaded ? Math.max(1200, currentSpell.getCooldown() * 3 * 20) : (currentSpell.getCooldown() * 20));
        /**
         * Final check for how much mana the player actually spent casting a spell.
         * New: if the player overloads, add them to a registry in which case, if they overload again (casts 2 free spells
         * within the time frame of 2 x cooldown (either base within 2 minutes, or the base cooldown x 3 seconds),
         * they'll gain EtherDisruption, in which case, they can't cast spells for 5 minutes.
         * This ensures fairness, while allowing for some interesting combat, and forgiveness.
         */
        if (actualCost > wand.getCurrentMana())
        {
        	if (recentlyOverloaded.contains(p.getUniqueId()))
        	{
        		ArcanoEffects.hasEtherDisruption.add(p.getUniqueId());
        		recentlyOverloaded.remove(p.getUniqueId());
        		PrintUtils.PrintToActionBar(p, "&b&oOverload&r&f! Ether Disruption added for 5 minutes..");
        		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> ArcanoEffects.hasEtherDisruption.remove(p.getUniqueId()), 6000);
        	}
        	else 
        	{
        		PrintUtils.PrintToActionBar(p, "&b&oEther Overload&r&f! &eSpell &f&lCD &r&f-> &b&o"+(int)cooldown/20+" seconds");
        		recentlyOverloaded.add(p.getUniqueId());
        		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->recentlyOverloaded.remove(p.getUniqueId()), cooldown * 2);
        	}
        	EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.AMBIENT);
        	wand.subtractMana(wand.getCurrentMana());
        }
        else wand.subtractMana(actualCost);
        
        if (data.doXpNotification()) PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f" + currentSpell.getManacost() + " &b&o" + PrintUtils.printStatType(StatType.MAGIC));
        if (data.doLevelUpSound()) EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
        PlayerData.addXP(p, StatType.MAGIC, currentSpell.getManacost());
        OBSParticles.playCastSigil(p, currentSpell.getElementType());

        cooldownPlayers.computeIfAbsent(p.getUniqueId(), k -> new HashSet<>()).add(currentSpell);
        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
        {
            Set<Spell> cd = cooldownPlayers.get(p.getUniqueId());
            if (cd != null) cd.remove(currentSpell);
        }, cooldown);

        p.getInventory().setItemInMainHand(wand.getAsItemStack());
	}
	
}
