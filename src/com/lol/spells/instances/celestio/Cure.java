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
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.HeresioEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;
import com.ouroboros.utils.entityeffects.helpers.WildcardData;

public class Cure extends Spell
{

	public Cure() 
	{
		super("Cure", "cure", Material.NETHER_STAR, SpellType.SUPPORT, SpellementType.CELESTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 100, 5, false,
				"&r&fCures target &b&oPlayer&r&f &7(20m)&r&f or &6&oSelf&r&f of all negative statuses.");
	}

	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getEntity(p, 20);
		
		if (target == null)
		{
			cureHelper(p, p);
			return this.getManacost();
		}
		
		if (target instanceof Player)
		{
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.CLOUD, null);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 1, 0.5, Particle.WAX_ON, null);
			cureHelper((Player) target, p);
			return this.getManacost();
		}
		
		return -1;
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}

	public static void cureHelper(Player target, Player caster)
	{
		EntityEffects.playSound(caster, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		
		if (HeresioEffects.isCursed.contains(target.getUniqueId()))
		{
			HeresioEffects.isCursed.remove(target.getUniqueId());
			PrintUtils.PrintToActionBar(target, "&e&oThe curse subsides..");
			EntityEffects.playSound(target, Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT);
		}
		if (MortioEffects.hasDread.contains(target.getUniqueId())) MortioEffects.hasDread.remove(target.getUniqueId());
		if (MortioEffects.hasDoom.containsKey(target.getUniqueId())) MortioEffects.hasDoom.remove(target.getUniqueId());
		
		for (PotionEffect effect : target.getActivePotionEffects())
		{
			if (EntityEffects.debuffs.contains(effect.getType()))
			{
				if (HeresioEffects.isHexed.get(target.getUniqueId()) != null && HeresioEffects.isHexed.get(target.getUniqueId()).effect.equals(effect.getType()))
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
						
						WildcardData data = HeresioEffects.isHexed.get(target.getUniqueId());
						target.removePotionEffect(data.effect);
						EntityEffects.add(target, data.effect, PotionEffect.INFINITE_DURATION, data.magnitude == 9 ? 9 : data.magnitude+1, true);
						HeresioEffects.isHexed.put(target.getUniqueId(), new WildcardData(data.effect, data.magnitude == 9 ? 9 : data.magnitude+1));
					}, 15);
					continue;
				}
				
				target.removePotionEffect(effect.getType());
			}
		}
	}
}
