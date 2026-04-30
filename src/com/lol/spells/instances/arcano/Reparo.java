package com.lol.spells.instances.arcano;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Reparo extends Spell
{

	public Reparo()
	{
		super("Reparo", "reparo", Material.MUSIC_DISC_11, SpellType.UTILITY, SpellementType.ARCANO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 150, 10, false, false,
				"&r&fRepairs &d&ooff-hand&r&f item to &b&o100%&r&f durability.","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '"+PrintUtils.color(ObsColors.ARCANO)+"&oRestore Armament&r&7&o', however, colloquially known as '&r"+PrintUtils.color(ObsColors.ARCANO)+"&oReparo&r&7&o'.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		ItemStack offhand = p.getInventory().getItemInOffHand();
		ItemMeta meta = offhand.getItemMeta();
		
		if (offhand == null || offhand.getType().equals(Material.AIR) || !(meta instanceof Damageable damageMeta)) return -1;
		if (!damageMeta.hasDamage()) return -1;
		
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		damageMeta.setDamage(0);
		offhand.setItemMeta(damageMeta);
		
		return 150;
	}

	@Override
	public int getTotalManaCost()
	{
		return 150;
	}

}
