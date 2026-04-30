package com.lol.spells.instances.cosmo;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsTimer;
import com.ouroboros.utils.PrintUtils;

public class VoidForm extends Spell
{

	public VoidForm()
	{
		super("Void Form", "void_form", Material.ENDER_EYE, SpellType.DEFENSIVE, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 50, 30, false, false,
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
		
		ObsTimer.startCountdown(p, 15, Ouroboros.instance, player ->
		{
			player.setGameMode(GameMode.SURVIVAL);
	        PrintUtils.PrintToActionBar(player, "&7&oVoid Form Ends..");
		});
		
		return 50;
	}

	@Override
	public int getTotalManaCost()
	{
		return 50;
	}

}
