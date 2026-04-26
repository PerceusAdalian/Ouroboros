package com.lol.spells.instances.heresio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.lol.wand.Wand;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Penance extends Spell
{

	public Penance() 
	{
		super("Penance", "penance", Material.LEAD, SpellType.UTILITY, SpellementType.HERESIO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 0, 5, false,
				"&r&fInflict &cdamage&f to &6&oself&r&f and &arestore&f wand's &b&lMana&r&f by &l500&r&f.",
				"&r&fDamage dealt is equal to &b&o50%&r&f current &cHP&f.",
				"&r"+PrintUtils.color(ObsColors.HERESIO)+"&lPenance&r&f cannot be cast if &cHP&f < 5.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Wand wand = new Wand(e.getItem());
		
		if (p.getHealth() < 5)
		{
			PrintUtils.PrintToActionBar(p, "&cFizzle!");
			return -1;
		}
		
		EntityEffects.playSound(p, Sound.ITEM_ARMOR_EQUIP_CHAIN, SoundCategory.AMBIENT);
		p.setHealth(p.getHealth()/2.0); 
		ItemCollector.remove(e);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{			
			wand.addMana(500);
			p.getInventory().setItemInMainHand(wand.getAsItemStack());
		}, 1);
		
		return 0;
	}

	@Override
	public int getTotalManaCost() 
	{
		return 0;
	}

}
