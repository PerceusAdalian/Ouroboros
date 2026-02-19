package com.lol.spells.instances.celestio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.EntityEffects.WildcardData;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class Diagnosis extends Spell
{

	public Diagnosis() 
	{
		super("Diagnosis", "diagnosis", Material.ENDER_EYE, SpellType.SUPPORT, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 50, 5, false,
				"&r&fDiagnose target &b&oPlayer&r &7(20m)&f or &6&oSelf&r&f to view current statuses.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 20);
		
		if (target == null)
		{
			spellEffect(p, p);
		}
		
		if (target instanceof Player)
		{
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.CLOUD, null);
			OBSParticles.drawCylinder(target.getLocation(), target.getWidth(), 3, 15, 0.75, 0.5, Particle.ENCHANT, null);
			spellEffect((Player) target, p);
		}
		
		return false;
	}

	private static void spellEffect(Player target, Player caster)
	{
		EntityEffects.playSound(caster, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);

		PrintUtils.OBSFormatDebug(caster, "Ωuroboros Entity Diagnostic Scan For Player: " + target.getName() + " Initializing..");

		StringBuilder effectsBuilder = new StringBuilder();
		for (PotionEffect effect : target.getActivePotionEffects()) 
		{
			String formattedName = PrintUtils.formatEnumName(effect.getType().getTranslationKey());
			String coloration = EntityEffects.debuffs.contains(effect.getType()) ? "&r&c&l" : "&a";
			
			if (effectsBuilder.length() > 0) effectsBuilder.append("&r&f, ");
			effectsBuilder.append(coloration).append(formattedName);
		}

		String effects = effectsBuilder.length() > 0 ? effectsBuilder.toString() : "&7None";

		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
		{
			PrintUtils.OBSFormatDebug(caster, "Scan Complete. " + target.getName() + "'s Results: " + effects);
		}, 20);
		
		if (EntityEffects.hasDread.contains(target.getUniqueId()))
		{
			PrintUtils.OBSFormatDebug(caster, "&r&fWarning: Target is affected by &4Dread&f. Priority Level: &6Medium");
		}
		if(EntityEffects.isCursed.contains(target.getUniqueId()))
		{
			PrintUtils.OBSFormatDebug(caster, "&r&fWarning: Target is affected by a &4Curse&f. Priority Level: &aLow");
		}
		if(EntityEffects.hasDoom.containsKey(target.getUniqueId()))
		{
			PrintUtils.OBSFormatDebug(caster, "&r&fWarning: Target is affected by &4Doom&f. Priority Level: &cHigh");
		}
		if (EntityEffects.isHexed.containsKey(target.getUniqueId()))
		{
			WildcardData data = EntityEffects.isHexed.get(target.getUniqueId());
			String effectName = PrintUtils.formatEnumName(data.effect.getTranslationKey());
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				PrintUtils.OBSFormatError(caster, "&cWARNING&f: A &2&lHex&r&f was found: &n"+effectName+"&r&f. Priority Level: &c&lHighest&r&f "+target.getName()+" can be &e&ocured&r&f safely now.");
				EntityEffects.isHexed.remove(target.getUniqueId());
				OBSParticles.drawCylinder(target.getLocation(), target.getWidth(), 3, 15, .75, .5, Particle.ENCHANT, null);
				OBSParticles.drawCelestioCastSigil(target);
				EntityEffects.playSound(target, Sound.ITEM_BONE_MEAL_USE, SoundCategory.AMBIENT);
			}, 30);
		}
		
		
	}
	
}
