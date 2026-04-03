package com.lol.spells.instances.cosmo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitTask;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class VoidForm extends Spell
{

	public VoidForm()
	{
		super("Void Form", "void_form", Material.ENDER_EYE, SpellType.DEFENSIVE, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 50, 30, false,
				"&r&fTemporarily shift into a limbic state of matter &7(15s)");
	}

	Map<UUID, Integer> timer = new HashMap<>();
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (!p.getGameMode().equals(GameMode.SURVIVAL)) 
		{
			PrintUtils.OBSFormatError(p, "Fizzle! This spell requires &d&oSurvival Mode&r&f!");
			return -1;
		}
		
		p.setGameMode(GameMode.SPECTATOR);
		timer.put(p.getUniqueId(), 15);

		int[][]  thresholds = {{13, 'a'}, {10, '2'}, {7, 'e'}, {4, '6'}};

		BukkitTask[] taskHolder = new BukkitTask[1];
		taskHolder[0] = Bukkit.getScheduler().runTaskTimer(Ouroboros.instance, () ->
		{
		    int seconds = timer.get(p.getUniqueId());

		    char color = 'c';
		    for (int[] t : thresholds) if (seconds >= t[0]) { color = (char) t[1]; break; }

		    if (seconds < 4) EntityEffects.playSound(p, Sound.BLOCK_NOTE_BLOCK_BIT, SoundCategory.AMBIENT);

		    PrintUtils.PrintToActionBar(p, "&" + color + seconds + "&7s remaining..");

		    --seconds;
		    timer.put(p.getUniqueId(), seconds);

		    if (seconds < 0)
		    {
		        timer.remove(p.getUniqueId());
		        taskHolder[0].cancel();
		        p.setGameMode(GameMode.SURVIVAL);
		        PrintUtils.PrintToActionBar(p, "&7&oVoid Form Ends..");
		    }
		}, 0, 20);
		
		return 50;
	}

	@Override
	public int getTotalManaCost()
	{
		return 50;
	}

}
