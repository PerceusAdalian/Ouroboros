package com.lol.spells.instances.celestio;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;

public class Force extends Spell
{

	public Force(String name, String internalName, Material icon, SpellType sType, SpellementType eType,
			CastConditions castCondition, Rarity spellTier, int manacost, double cooldown, boolean pvpCompatible,
			String[] spellDescription)
	{
		super(name, internalName, icon, sType, eType, castCondition, spellTier, manacost, cooldown, pvpCompatible,
				spellDescription);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalManaCost()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
