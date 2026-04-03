package com.ouroboros.utils.entityeffects;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityCategories;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.OBStandardTimer;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.helpers.JinxData;

public class MortioEffects
{
	public static Set<UUID> hasDread = new HashSet<>();
	/*
	 * "&r&4Dread &eEffect&f: Applies a debilitation that causes &b&ohunger&r&f and &b&oblindness&r&f",
					"&r&fto those afflicted. Dread is &e&ocurable&r&f and does not stack, however",
					"&r&fsubsequent applications will cause &4Doom&f after a second application.",
	 */
	public static void addDread(LivingEntity target, int seconds)
	{
		if (hasDread.contains(target.getUniqueId())) 
		{
			hasDread.remove(target.getUniqueId());
			addDoom(target, 0, seconds * 20);
			return;
		}
		
		hasDread.add(target.getUniqueId());
		EntityEffects.add(target, PotionEffectType.HUNGER, seconds * 20, 0, false);
		EntityEffects.add(target, PotionEffectType.BLINDNESS, seconds * 20, 0, false);
	}
	
	public static Map<UUID, Integer> nightShifted = new HashMap<>();
	/*
	  "&r&4Night-Shift &eEffect&f: Increased &b&oSpeed&r&f and &b&oStrength&r&f",
		"&r&fper level of &4Night-Shift&f, plus &b&oNight Vision&r&f.",
		"&r&fReduces &b&oFall Damage &r&fby &b&o10% &r&fper level.",
		"&r&fIf it's nighttime, Night-Shift's &b&omagnitude&r&f is doubled."
	 */
	/**
	 * @param target
	 * @param magnitude values of >=1 accepted.
	 * @param seconds
	 */
	public static void addNightShift(Player target, int magnitude, int seconds)
	{
		if (nightShifted.containsKey(target.getUniqueId())) return;
		
		nightShifted.put(target.getUniqueId(), magnitude);
		
		OBStandardTimer.runWithCancel(Ouroboros.instance, (e)->
		{
			if (!((Player) target).isOnline()) e.cancel();
			
			long time = target.getWorld().getTime();
			boolean isNight = time >= 13000 && time <= 23000;
			int amp = isNight ? (magnitude * 2) - 1 : magnitude - 1;

			EntityEffects.add(target, PotionEffectType.SPEED, 40, amp, false);
			EntityEffects.add(target, PotionEffectType.STRENGTH, 40, amp, false);
			EntityEffects.add(target, PotionEffectType.NIGHT_VISION, 300, 0, false);

			nightShifted.put(target.getUniqueId(), amp);
		}, 20, seconds * 20);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
		{
			if (target.isOnline()) 
			{	
				PrintUtils.PrintToActionBar(target, "&4&oNight-Shift ends..");
				EntityEffects.playSound(target, Sound.BLOCK_CHAIN_BREAK, SoundCategory.AMBIENT);
			}
			nightShifted.remove(target.getUniqueId());
		}, seconds * 20);
	}
	
	public static void addShroud(Player target, int magnitude, int seconds)
	{
		EntityEffects.add(target, PotionEffectType.SPEED, 20 * seconds, magnitude);
		EntityEffects.add(target, PotionEffectType.JUMP_BOOST, 20 * seconds, magnitude);
		EntityEffects.add(target, PotionEffectType.INVISIBILITY, 20 * seconds, magnitude);
	}
	
	public static Map<UUID, Boolean> hasDoom = new HashMap<>();
	/**
	 * @Description Mortio Signature Effect: 
	 * Doom applies a DOT effect equal to it's magnitude. 
	 * Afflicted take 1.25x Mortio damage, and reapplying instantly kills them (NONPVP).
	 * Mortio-based mobs are otherwise unaffected, and healed instead.
	 
	 * @param target    (LivingEntity)
	 * @param magnitude The stacks of doom that equate to the level of Wither (i.e.
	 *                  0 = Wither 1)
	 * @param seconds
	 */
	/*
	 * "&r&4Doom &eEffect&f: Doom applies a &dDOT&f effect equal to it's &b&omagnitude&r&f.",
				"&r&fAfflicted take &b&o1.25x &r&4&lMortio&r&f damage, and reapplying instantly kills them &7(NONPVP)",
				"&r&4&lMortio&r&f-based mobs are otherwise unaffected, and &a&ohealed&r&f instead."
	 */
	public static void addDoom(LivingEntity target, int magnitude, int seconds) 
	{
	    MobData data = MobData.getMob(target.getUniqueId());
	    if (data == null) 
	    {
	    	EntityEffects.add(target, PotionEffectType.WITHER, seconds * 20, magnitude, false);
	        return;
	    }
	    
	    boolean isMortio = EntityCategories.mortio_mobs.contains(target.getType());
	    UUID uuid = target.getUniqueId();
	    
	    if (hasDoom.containsKey(uuid)) 
	    {
	        hasDoom.remove(uuid);
	        if (!isMortio) data.kill();
	        return;
	    }
	    
	    hasDoom.put(uuid, true);
	    PotionEffectType effectType = isMortio ? PotionEffectType.HUNGER : PotionEffectType.WITHER;
	    int effectMagnitude = isMortio ? 0 : magnitude;
	    EntityEffects.add(target, effectType, seconds * 20, effectMagnitude, true);
	    
	    int duration = seconds * 20;
	    OBStandardTimer.runWithCancel(Ouroboros.instance, (r) -> 
	    {
	        if (!target.hasPotionEffect(effectType) || target.isDead()) 
	        {
	            r.cancel();
	            return;
	        }
	        if (isMortio) data.heal(1, false, true, false);
	        OBSParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.SCULK_SOUL, null);
	    }, 20, duration);
	    
	    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> hasDoom.remove(uuid), duration);
	}
	
	public static Map<UUID, JinxData> jinxRegistry = new HashMap<>();
	public static void addJinxStacks(Player target, int magnitude)
	{
		int registerMagnitude = magnitude;
		if (jinxRegistry.containsKey(target.getUniqueId()))
		{
			registerMagnitude = jinxRegistry.get(target.getUniqueId()).magnitude + magnitude;
		}
		
		OBSParticles.drawMortioCastSigil(target);
		jinxRegistry.put(target.getUniqueId(), new JinxData(registerMagnitude));
	}
}
