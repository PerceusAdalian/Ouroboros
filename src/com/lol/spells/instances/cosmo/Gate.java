package com.lol.spells.instances.cosmo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.lol.wand.Wand;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.GateCodes;
import com.ouroboros.enums.Rarity;
import com.ouroboros.menus.GuiHandler;
import com.ouroboros.menus.instances.GateMenu;
import com.ouroboros.menus.instances.magic.CollectWandData;
import com.ouroboros.utils.ItemCollector;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Gate extends Spell
{

	public Gate() 
	{
		super("Gate", "gate", Material.OMINOUS_TRIAL_KEY, SpellType.ULTIMATE, SpellementType.COSMO, CastConditions.MIXED, Rarity.FIVE, 50, 5, false,
				"&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&3Gate&f: &3&oSpatial Convergence Protocol&r&f --",
				"&r&fSets your &b&oAnchor Location&r&f in the current dimension.",
				"&r&fDefault locations are that world's spawnpoint, and locations saved are",
				"&r&fused &n&oonly&r&f for this spell. &7&o(Multiple &b&oBed States&r&7&o are allowed.)","",
				"&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&3Gate&f: &3&oQuantum Leap&r&f --",
				"&r&fTeleport between saved &a&lOverworld&r&f, &c&lNether&r&f, and &3&lEnd&r&f locations.","",
				"&r&bEchoic Dissonance&r&f: &e&oSecondary Cast&r&f sets cooldown",
				"&r&fto &b&o1 minute&f and costs an extra &b150 &9&lMana&r&f.");
	}

	public static Set<UUID> cooldown = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			Wand wand = new Wand(e.getItem());
			if (cooldown.contains(p.getUniqueId()))
			{
				PrintUtils.PrintToActionBar(p, "&7&o'&r&3&oQuantum Leap&r&7&o' is on cooldown..");
				return -1;
			}
			
			if (wand.getCurrentMana() < 200)
			{
				PrintUtils.PrintToActionBar(p, "&7&oInsufficient Mana for '&3&oQuantum Leap&r&7&o'");
				return -1;
			}
			
			EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_OMINOUS_ACTIVATE, SoundCategory.AMBIENT);
			CollectWandData.wandCollector.put(p.getUniqueId(), wand);
			ItemCollector.remove(e);
			GuiHandler.open(p, new GateMenu(p));
			return -1;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			GateCodes code;
			if (p.getWorld().getName().equals("world")) code = GateCodes.OVERWORLD;
			else if (p.getWorld().getName().equals("world_nether")) code = GateCodes.NETHER;
			else if (p.getWorld().getName().equals("world_the_end")) code = GateCodes.END;
			else code = null;
			
			if (code == null) 
			{
				PrintUtils.OBSFormatError(p, "This world isn't recognized. Please try again..");
				return -1;
			}
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			EntityEffects.playSound(p, Sound.BLOCK_TRIAL_SPAWNER_AMBIENT, SoundCategory.AMBIENT);
			PlayerData data = PlayerData.getPlayer(p.getUniqueId());
			data.setGate(code, p.getLocation());
			PrintUtils.PrintToActionBar(p, "&7&oAnchor location set!");
			return this.getManacost();
		}
		
		return -1;
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

}
