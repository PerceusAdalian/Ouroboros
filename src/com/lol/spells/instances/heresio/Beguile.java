package com.lol.spells.instances.heresio;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;

public class Beguile extends Spell
{
	// TODO
	public Beguile() 
	{
		super("Beguile", "beguile", Material.ENDER_EYE, SpellType.CURSE, SpellementType.HERESIO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 50, 2, true,
				"&r&fInflict &2Intimidated &bII &fon &6target &dMob&f/&c&oPlayer&r&7 (20m, 30s | &cPVP&7: 20s)","",
				"&r&2Intimidated &eEffect&f: affected are &b&oFatigued&r&f, &b&oSlowed&r&f, and &b&oWeakened&r&f",
				"&r&fequal to the &b&omagnitude&r&f of &2Intimidated&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalManaCost() {
		// TODO Auto-generated method stub
		return 0;
	}

}
