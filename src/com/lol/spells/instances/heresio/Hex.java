package com.lol.spells.instances.heresio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.Chance;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.EntityEffects.WildcardData;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.OBStandardTimer;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class Hex extends Spell
{

	public Hex() 
	{
		super("Hex", "hex", Material.ENDER_EYE, SpellType.HEX, SpellementType.HERESIO, CastConditions.RIGHT_CLICK_AIR, Rarity.SEVEN, 200, 5, true,
				"&r&2&lHex&r&f target entity &7(&b&o20m&r&7)&f with the &2Wildcard&r&f debuff.",
				"&r&fThe effects of Hex can Backfire, are &b&opermanent&r&f and &c&ocannot be removed&r&f.",
				"&r&fAny attemps made to &e&oCure&r&f the affliction without a &e&oDiagnosis&r&f",
				"&r&fwill result in the effect worsening up to &b&o10 levels&r&f.",
				"&r&c&lPVP&r&f: The Hex will potentially worsen to maximum stacks &7(lvl10)&f within &b&o5min&r &7(14.5%)","",
				"&r&2WildCard&e Effect&r&f: applies a random debuff on the target.",
				"&r&2Backfire &bChance&f: &b&o2.25%&r&7 / &c&o5.25% &r&7(&cPVP&7)",
				"&r&2Backfire &eEffect&f: The spell fizzles, and affects the caster instead.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 20);
		
		if (!(target instanceof LivingEntity)) return false;
		if (EntityEffects.isHexed.containsKey(target.getUniqueId()))
		{
			PrintUtils.PrintToActionBar(p, "&f&oTarget is already hexed!");
			return false;
		}
		
		boolean isPlayer = target instanceof Player;
		double backfireChance = isPlayer ? 5.25 : 2.25;
		
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.GLOW_SQUID_INK, null);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> 
		{
			if (Chance.of(Ouroboros.debug ? 100 : backfireChance)) 
			{
				OBSParticles.drawLine(target.getLocation(), p.getLocation(), 0.5, 0.5, Particle.GLOW_SQUID_INK, null);
				playBackfire(p);
			} 
			else 
			{
				spellEffect((LivingEntity) target, p);
				if (isPlayer) playerTimer((Player) target);
			}
		}, 15);
		
		return false;
	}

	private static void spellEffect(LivingEntity entity, Player caster)
	{
		OBSParticles.drawAngledCircle(entity.getEyeLocation(), 2, 8, 45, -0.1, Particle.WARPED_SPORE, null);
		OBSParticles.drawCylinder(entity.getLocation(), entity.getWidth(), (int)(entity.getHeight()+1), 15, 0.5, 0.5, Particle.ENCHANT, null);
		EntityEffects.playSound(caster, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		EntityEffects.addWildcard(entity, 0);
		
		if (entity instanceof Player p) 
		{
			PrintUtils.PrintToActionBar(p, "You've Been Hexed!");
			EntityEffects.playSound(p, Sound.ENTITY_WARDEN_HEARTBEAT, SoundCategory.MASTER);
		}
	}

	private static void playBackfire(Player player)
	{
		PrintUtils.PrintToActionBar(player, "&cThe Spell Backfired!");
		if (EntityEffects.isHexed.containsKey(player.getUniqueId())) 
		{
			PrintUtils.PrintToActionBar(player, "&c..But you've already been hexed!");
			return;
		}
		EntityEffects.playSound(player, Sound.BLOCK_CHAIN_BREAK, SoundCategory.AMBIENT);
		EntityEffects.playSound(player, Sound.ENTITY_ENDERMAN_AMBIENT, SoundCategory.AMBIENT);
		OBSParticles.drawLine(player.getLocation(), player.getLocation(), 0.5, 0.5, Particle.WARPED_SPORE, null);
		spellEffect(player, player);
		playerTimer(player);
	}

	private static void playerTimer(Player player)
	{
		OBStandardTimer.runWithCancel(Ouroboros.instance, (C) -> 
		{
			if (Chance.of(14.5)) 
			{
				WildcardData data = EntityEffects.isHexed.get(player.getUniqueId());
				if (data == null || !player.isOnline()) return;
				
				OBSParticles.drawHeresioCastSigil(player);
				PrintUtils.PrintToActionBar(player, "&2&oThe Hex Worsens..");
				
				player.removePotionEffect(data.effect);
				EntityEffects.add(player, data.effect, PotionEffect.INFINITE_DURATION, data.magnitude == 9 ? 9 : data.magnitude + 1, true);
				EntityEffects.isHexed.put(player.getUniqueId(), new WildcardData(data.effect, data.magnitude == 9 ? 9 : data.magnitude + 1));
				return;
			}
			
		}, 1200, 6000);
	}
}
