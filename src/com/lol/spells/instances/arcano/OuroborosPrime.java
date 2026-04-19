package com.lol.spells.instances.arcano;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.ArcanoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class OuroborosPrime extends Spell
{

	public OuroborosPrime()
	{
		super("Ωuroboros", "ouroboros_spell", Material.BREWING_STAND, SpellType.SIGNATURE, SpellementType.ARCANO, CastConditions.RIGHT_CLICK_AIR, Rarity.SEVEN, 2000, 480, false,
				"&r&fGrants "+PrintUtils.color(ObsColors.ARCANO)+"&bLayline Override&f for &b&o30 seconds&r&f.","",
				"&r&bEchoic Dissonance&r&f: Applies &b&oEther Disruption&r&f for &b&o3 minutes&r&f.","",
				"&r"+PrintUtils.color(ObsColors.ARCANO)+"&bLayline Override &eEffect&f: ",
				"&r&fSpells cost &cHP&f instead of &b&lMana&r&f and ignores &lCooldowns&r&f.",
				"&r&cHP&f cost equals your wand's total &eSpell Slots&f.",
				"&r&fThis spell is unaffected by its own effect.","",
				"&r&bEther Disruption &eEffect&f: affected &c&oPlayer&r&f(s) can't cast &e&oSpells&r&f.");
	}

	public static Set<UUID> ouroboros_registry = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (ouroboros_registry.contains(p.getUniqueId())) return -1;
		
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		EntityEffects.playSound(p, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.AMBIENT);
		
		ouroboros_registry.add(p.getUniqueId());
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			OBSParticles.drawArcanoCastSigil(p);
			PrintUtils.PrintToActionBar(p, "&b&lΩuroboros &r&7&oexpired, &b&oEther Disruption&r&7&o added..");
			EntityEffects.playSound(p, Sound.BLOCK_BEACON_DEACTIVATE, SoundCategory.AMBIENT);
			ouroboros_registry.remove(p.getUniqueId());
			ArcanoEffects.hasEtherDisruption.add(p.getUniqueId());
		}, 600);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
		{
			ArcanoEffects.hasEtherDisruption.remove(p.getUniqueId());
			EntityEffects.playSound(p, Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.AMBIENT);
			PrintUtils.PrintToActionBar(p, "&b&oEther Disruption&r&7&o wore off..");
		}, 4200);
		
		return 2000;
	}

	@Override
	public int getTotalManaCost()
	{
		// TODO Auto-generated method stub
		return 2000;
	}

}
