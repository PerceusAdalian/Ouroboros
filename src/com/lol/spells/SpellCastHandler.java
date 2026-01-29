package com.lol.spells;

import java.util.HashMap;
import java.util.Map;
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

import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.lol.spells.instances.SpellRegistry;
import com.lol.wand.Wand;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;

public class SpellCastHandler implements Listener
{
	public static void register(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new SpellCastHandler(), plugin);
	}
	
	private static Map<UUID, Spell> cooldownPlayers = new HashMap<>();
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		ItemStack held = p.getInventory().getItemInMainHand();
		
		if (e.getHand() == null) return;
        if (e.getHand().equals(EquipmentSlot.OFF_HAND)) return;
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
				
		if (CastConditions.isValidAction(e, CastConditions.LEFT_CLICK_AIR))
		{
			if (wand.getNextSpell() == null) return;
			wand.rotateSpells();
			p.getInventory().setItemInMainHand(wand.getAsItemStack());
			PrintUtils.PrintToActionBar(p, "&b&lEquipped Spell&r&f: "+wand.getSpell(wand.getSpellIndex()).getName());
			return;
		}
		else if (wand.getCurrentMana() > 0 && CastConditions.isValidAction(e, wand.getSpell(wand.getSpellIndex()).getCastCondition()))
		{
			
			if (cooldownPlayers.containsKey(p.getUniqueId()) && cooldownPlayers.containsValue(wand.getSpell(wand.getSpellIndex())))
			{
				PrintUtils.PrintToActionBar(p, "&cSpell on Cooldown!");
				return;
			}	
			
			// You can only cast spells when your Magic Proficiency is equal to or higher than the wand's tier.
			// Likewise, you can technically equip spells of higher tier on your wand, but you can't cast them either if the wand's level is less than the spell rarity.
			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
			
			if (wand.getRarity().getRarity() < wand.getSpell(wand.getSpellIndex()).getRarity().getRarity() ||
					data.getMagicProficiency() < wand.getRarity().getRarity())
			{
				PrintUtils.PrintToActionBar(p, "&cFizzle!");
				return;
			}
			
			if (wand.getSpell(wand.getSpellIndex()).Cast(e))
			{
				if (PlayerData.getPlayer(p.getUniqueId()).doXpNotification()) 
					PrintUtils.PrintToActionBar(p, "&r&e&l+&r&f"+wand.getSpell(wand.getSpellIndex()).getManacost()+" &b&o"+PrintUtils.printStatType(StatType.MAGIC));
				if (PlayerData.getPlayer(p.getUniqueId()).doLevelUpSound()) 
					EntityEffects.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER);
				PlayerData.addXP(p, StatType.MAGIC, wand.getSpell(wand.getSpellIndex()).getManacost());
				
				playCastSigil(p, wand.getSpell(wand.getSpellIndex()).getElementType());
				wand.subtractMana(wand.getSpell(wand.getSpellIndex()).getManacost());
				cooldownPlayers.put(p.getUniqueId(), wand.getSpell(wand.getSpellIndex()));
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
				cooldownPlayers.remove(p.getUniqueId(), wand.getSpell(wand.getSpellIndex())), (long) (wand.getSpell(wand.getSpellIndex()).getCooldown()*20));
				e.getPlayer().getInventory().setItemInMainHand(wand.getAsItemStack());
			}
		} 
		else if (wand.getCurrentMana() < wand.getSpell(wand.getSpellIndex()).getManacost() && CastConditions.isValidAction(e, wand.getSpell(wand.getSpellIndex()).getCastCondition()))
		{
			PrintUtils.PrintToActionBar(p, "&r&fNot Enough &b&lMana&r&f!");
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.AMBIENT);
			return;
		}
		return;
	}
	
	private static void playCastSigil(Player player, SpellementType sType)
	{
		switch (sType)
		{
		case CELESTIO -> OBSParticles.drawCelestioCastSigil(player);
		case AERO -> OBSParticles.drawAeroCastSigil(player);
		case COSMO -> OBSParticles.drawCosmoCastSigil(player);
		case GEO -> OBSParticles.drawGeoCastSigil(player);
		case GLACIO -> OBSParticles.drawGlacioCastSigil(player);
		case HERESIO -> OBSParticles.drawHeresioCastSigil(player);
		case INFERNO -> OBSParticles.drawInfernoCastSigil(player);
		case MORTIO -> OBSParticles.drawMortioCastSigil(player);
		default -> throw new IllegalArgumentException("Unexpected value: " + sType);
		}
	}
}
