package com.lol.spells;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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
import com.lol.spells.instances.SpellRegistry;
import com.lol.wand.Wand;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
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
		        		PrintUtils.Print(p, "&b&lSpell Registered&r&f: "+spell.getName()+"&r&f. Check your &d&o/spellbook&r&f!");
		        		data.getSpell(spell).setRegistered(true);
		        		data.save();
		        		return;
					}
				}
			}
		}
		
		Wand wand = new Wand(held);
		
		if (!wand.hasSpell(wand.getSpellIndex())) return;
				
		if (cooldownPlayers.containsKey(p.getUniqueId()) && cooldownPlayers.containsValue(wand.getSpell(wand.getSpellIndex())))
		{
			PrintUtils.PrintToActionBar(p, "&cSpell on Cooldown!");
			return;
		}	
		
		if (wand.getCurrentMana() == 0)
		{
			PrintUtils.PrintToActionBar(p, "&r&fNot Enough &b&lMana&r&f!");
			return;
		}
		
		if (wand.getCurrentMana() > 0 && CastConditions.isValidAction(e, wand.getSpell(wand.getSpellIndex()).getCastCondition()))
		{
			wand.getSpell(wand.getSpellIndex()).Cast(e);
			wand.subtractMana(wand.getSpell(wand.getSpellIndex()).getManacost());
			cooldownPlayers.put(p.getUniqueId(), wand.getSpell(wand.getSpellIndex()));
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
				cooldownPlayers.remove(p.getUniqueId(), wand.getSpell(wand.getSpellIndex())), (long) (wand.getSpell(wand.getSpellIndex()).getCooldown()*20));
			e.getPlayer().getInventory().setItemInMainHand(wand.getAsItemStack());
			
			return;
		} 
		else if (CastConditions.isValidAction(e, CastConditions.LEFT_CLICK_AIR))
		{
			if (wand.getNextSpell() == null) return;
			int spellIndex = wand.getSpellIndex();
			String currentSpell = wand.getSpell(wand.getSpellIndex()).getName();
			wand.rotateSpells();
			int nextSpellIndex = wand.getSpellIndex();
			p.getInventory().setItemInMainHand(wand.getAsItemStack());
			PrintUtils.Print(p, "&b&lSpell Cycled&r&f: &7"+currentSpell+" ["+spellIndex+"] &e&l-> &b&l"+wand.getSpell(wand.getSpellIndex()).getName() + " &r&7["+nextSpellIndex+"]&r&f");
			return;
		}
		
		return;
	}
}
