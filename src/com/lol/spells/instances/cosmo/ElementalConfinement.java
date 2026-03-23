package com.lol.spells.instances.cosmo;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;

public class ElementalConfinement extends Spell
{

	public ElementalConfinement() 
	{
		super("Elemental Confinement", "elemental_confinement", Material.ENDER_CHEST, SpellType.UTILITY, SpellementType.COSMO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 25, 1, false,
				"&r&fOpen a spatial rift to store items.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Inventory enderChest = p.getEnderChest();
		p.openInventory(enderChest);
		EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.AMBIENT);
		return 25;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 25;
	}

}
