package com.lol.spells.instances.mortio;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;

public class Demonform extends Spell
{

	public Demonform() 
	{
		super("Demonform", "demonform", Material.ENDER_EYE, SpellType.BUFF, SpellementType.MORTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 150, 30, false,
				"&r&fAssume a bestial shadow form and gain &4Night-Shift &bI &7(30s)", "",
				"&r&4Night-Shift &eEffect&f: Grants increased &b&oSpeed&r&f and &b&oStrength&r&f",
				"&r&fper level of &4Night-Shift&f, plus &b&oNight Vision&r&f. Also reduces &b&oFall Damage",
				"&r&fby &b&o10% &r&fper level. If it's night-time, Night-Shift's effects are doubled.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		EntityEffects.addNightShift(p, 1, 30);
		return this.getManacost();
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}
}
