package com.lol.spells.instances.mortio;

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
import org.bukkit.potion.PotionEffectType;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class Reap extends Spell
{

	public Reap() 
	{
		super("Reap", "reap", Material.OPEN_EYEBLOSSOM, SpellType.OFFENSIVE, SpellementType.MORTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.TWO, 25, 2, true,
				"&r&fSlash your target within &b&o12 meters&r&f dealing 3&c♥&f &e&lSever&r&f damage.",
				"&r&fIf that target is &4Cursed&f, remove it, and deal 6&c♥&f &4&lMortio&r&f damage instead:",
				"&r&aRestore &b&o50%&r&f of the damage to self, and inflict &4Doom &r&bI &7(30s | &cPVP&7: &c75%&7, &cDoom II&7)","",
				"&r&4Doom &eEffect&f: Doom applies a &dDOT&f effect equal to it's &b&omagnitude&r&f.",
				"&r&fAfflicted take &b&o1.25x &r&4&lMortio&r&f damage, and reapplying instantly kills them &7(NONPVP)",
				"&r&4&lMortio&r&f-based mobs are otherwise unaffected, and &a&ohealed&r&f instead.");
	}

	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Entity target = RayCastUtils.getNearestEntity(p, 12);
		
		if (target == null || !(target instanceof LivingEntity)) return false;
		boolean isCursed = EntityEffects.isCursed.contains(target.getUniqueId());
		
		if (isCursed)EntityEffects.playSound(p, Sound.ENTITY_WITHER_SHOOT, SoundCategory.AMBIENT);
		else EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		
		OBSParticles.drawLine(p.getLocation(), target.getLocation(), 3, 1, Particle.SWEEP_ATTACK, null);
		if (isCursed)
		{
			OBSParticles.drawMortioCastSigil((LivingEntity) target);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				OBSParticles.drawCylinder(target.getLocation(), target.getWidth(), 3, 6, 0.5, 0.5, Particle.SMOKE, null), 10);
			
			if (target instanceof Player pt)
			{
				PrintUtils.PrintToActionBar(pt, "&e&oThe curse subsides..");
				EntityEffects.playSound(pt, Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT);
			}
			
			for (PotionEffect pe : ((LivingEntity) target).getActivePotionEffects())
			{
				PotionEffectType eType = pe.getType();
				if (eType.equals(PotionEffectType.WEAKNESS)) ((LivingEntity) target).removePotionEffect(PotionEffectType.WEAKNESS);
				if (eType.equals(PotionEffectType.MINING_FATIGUE)) ((LivingEntity) target).removePotionEffect(PotionEffectType.MINING_FATIGUE);
				if (eType.equals(PotionEffectType.SLOWNESS)) ((LivingEntity) target).removePotionEffect(PotionEffectType.SLOWNESS);
			}
			
			EntityEffects.isCursed.remove(target.getUniqueId());
			MobData.damageUnnaturally(p, target, 6, true, ElementType.MORTIO);
			EntityEffects.heal(p, target instanceof Player ? (6 * .75) : 3);
			EntityEffects.addDoom((LivingEntity) target, target instanceof Player ? 2 : 0, 30);
			
			return true;
		}
		
		MobData.damageUnnaturally(p, target, 3, true, ElementType.SEVER);
		return true;
	}

}
