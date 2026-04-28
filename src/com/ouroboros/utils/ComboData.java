package com.ouroboros.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class ComboData
{
	public UUID uuid;
	public Spell spell;
	public int comboStage;
	
	public static Map<UUID, ComboData> active_combos = new HashMap<>();
	
	public ComboData(UUID uuid, Spell spell, int comboStage)
	{
		this.uuid = uuid;
		this.spell = spell;
		this.comboStage = comboStage;
	}
	
	public static ComboData get(UUID uuid)
	{
		return active_combos.get(uuid);
	}
	
	public static void set(UUID uuid, ComboData data)
	{
		active_combos.put(uuid, data);
	}
	
	public static boolean contains(UUID uuid)
	{
		return active_combos.containsKey(uuid);
	}
	
	public static boolean clear(UUID uuid)
	{
		if (!contains(uuid)) return false;
		return active_combos.remove(uuid) != null;
	}
	
	public static boolean build(UUID uuid, Spell spell, int comboStage, boolean isFinalStage, Consumer<PlayerInteractEvent> action, PlayerInteractEvent event)
	{
		Player player = Bukkit.getPlayer(uuid);
		
		// Initialize the combo for this new spell combo chain
		if (comboStage == 1 && !contains(uuid)) set(uuid, new ComboData(uuid, spell, 1));
		
	    ComboData current = get(uuid);
	    if (current == null) return false;
	    if (!current.spell.getInternalName().equals(spell.getInternalName()) || current.comboStage != comboStage) 
	    	return false; // Wrong spell chain or wrong stage, skip silently
	    
	    ObsTimer.cancelCountdown(player);
	    action.accept(event);
	    
	    if (isFinalStage)
	    {
	    	clear(uuid);
	        PrintUtils.PrintToActionBar(player, "&f&oCombo End!");
	        return true;
	    }
	    
	    current.comboStage++;
	    int expectedNextStage = current.comboStage;

	    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	    {
	        if (current.comboStage == expectedNextStage && player != null && player.isOnline() && active_combos.containsKey(uuid))
	        {
	            // Don't start a new countdown if one is already ticking for this player
	            if (!ObsTimer.hasActiveCountdown(player))
	            {
	                ObsTimer.startCountdown(player, 3, Ouroboros.instance, p ->
	                {
	                    EntityEffects.playSound(player, Sound.BLOCK_CHAIN_BREAK, SoundCategory.AMBIENT);
	                    PrintUtils.PrintToActionBar(player, "&f&oCombo End!");
	                    clear(uuid);
	                });
	            }
	        }
	    }, 240);
	    
	    return true;
	}
	
	public static boolean build(UUID uuid, Spell spell, int comboStage, boolean isFinalStage, Function<PlayerInteractEvent, Boolean> action, PlayerInteractEvent event)
	{
	    Player player = Bukkit.getPlayer(uuid);

	    if (comboStage == 1 && !contains(uuid)) set(uuid, new ComboData(uuid, spell, 1));

	    ComboData current = get(uuid);
	    if (current == null) return false;
	    if (!current.spell.getInternalName().equals(spell.getInternalName()) || current.comboStage != comboStage) 
	    	return false;

	    ObsTimer.cancelCountdown(player);

	    boolean actionSucceeded = action.apply(event);

	    if (!actionSucceeded) return false;

	    if (isFinalStage)
	    {
	        clear(uuid);
	        PrintUtils.PrintToActionBar(player, "&f&oCombo End!");
	        return true;
	    }
	    
	    current.comboStage++;
	    int expectedNextStage = current.comboStage;

	    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	    {
	        if (current.comboStage == expectedNextStage && player != null && player.isOnline() && active_combos.containsKey(uuid))
	        {
	            if (!ObsTimer.hasActiveCountdown(player))
	            {
	                ObsTimer.startCountdown(player, 3, Ouroboros.instance, p ->
	                {
	                    EntityEffects.playSound(player, Sound.BLOCK_CHAIN_BREAK, SoundCategory.AMBIENT);
	                    PrintUtils.PrintToActionBar(player, "&f&oCombo End!");
	                    clear(uuid);
	                });
	            }
	        }
	    }, 240);
	    
	    return true;
	}
}
