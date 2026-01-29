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
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.EntityEffects.WildcardData;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class Cure extends Spell
{

	public Cure() 
	{
		super("Cure", "cure", Material.NETHER_STAR, SpellType.SUPPORT, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 100, 5, false,
				"&r&fCures target &b&oPlayer&r&f &7(20m)&r&f or &6&oSelf&r&f of all negative statuses.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 20);
		
		if (target == null)
		{
			spellEffect(p, p);
			return true;
		}
		
		if (target instanceof Player)
		{
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.CLOUD, null);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 1, 0.5, Particle.WAX_ON, null);
			spellEffect((Player) target, p);
			return true;
		}
		
		return false;
	}

	private static void spellEffect(Player target, Player caster)
	{
		EntityEffects.playSound(caster, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		
		for (PotionEffect effect : target.getActivePotionEffects())
		{
			if (EntityEffects.debuffs.contains(effect.getType()))
			{
				if (EntityEffects.isHexed.get(target.getUniqueId()) != null && EntityEffects.isHexed.get(target.getUniqueId()).effect.equals(effect.getType()))
				{
					OBSParticles.drawLine(caster.getLocation(), target.getLocation(), 0.5, 0.5, Particle.WARPED_SPORE, null);
					PrintUtils.OBSFormatError(caster, "Attempted to &e&oCure&r&f "+PrintUtils.formatEnumName(effect.getType().getTranslationKey())+
							" from "+target.getName()+", but it appears to be a &2&lHex&r&f.");
					Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
					{
						OBSParticles.drawDisc(target.getLocation(), target.getWidth(), 3, 15, 0.5, Particle.WARPED_SPORE, null);
						OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.SCULK_SOUL, null);
						OBSParticles.drawHeresioCastSigil(target);
						PrintUtils.PrintToActionBar(target, "&2&oThe Hex Worsens..");
						EntityEffects.playSound(target, Sound.ENTITY_WARDEN_HEARTBEAT, SoundCategory.MASTER);
						
						WildcardData data = EntityEffects.isHexed.get(target.getUniqueId());
						target.removePotionEffect(data.effect);
						EntityEffects.add(target, data.effect, PotionEffect.INFINITE_DURATION, data.magnitude == 9 ? 9 : data.magnitude+1, true);
						EntityEffects.isHexed.put(target.getUniqueId(), new WildcardData(data.effect, data.magnitude == 9 ? 9 : data.magnitude+1));
					}, 15);
					continue;
				}
				target.removePotionEffect(effect.getType());
			}
		}
	}
}
