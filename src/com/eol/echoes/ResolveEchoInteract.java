package com.eol.echoes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.eol.echoes.records.ActiveModifier;
import com.eol.echoes.records.EchoManifest;
import com.eol.echoes.records.Modifier;
import com.eol.echoes.records.PassiveModifier;
import com.eol.enums.CombatStat;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class ResolveEchoInteract
{
	public static double resolveCombatModifiedDamage(Player p, LivingEntity target, EchoManifest codec, double initialDamage)
	{
	    double damage = initialDamage;

	    for (Modifier mod : codec.getActiveModifiers())
	    {
	        if (!(mod instanceof ActiveModifier active)) continue;
	        if (!active.condition().satisfies(p, target, p.getWorld())) continue;

	        switch (active.combatStat())
	        {
	            case ATTACK ->
	            {
	                if (active.isPercent()) damage *= (1.0 + active.getMagnitude());
	                else                   damage += active.getMagnitude();
	            }
	            case ATTACK_RATING -> damage *= (1.0 + active.getMagnitude());
	            default -> {} // CRIT_RATE and CRIT_MODIFIER handled separately
	        }
	    }

	    return damage;
	}

	public static double resolveCritRate(Player p, LivingEntity target, EchoManifest codec, double baseCritRate)
	{
	    double critRate = baseCritRate;
	    
	    for (Modifier mod : codec.getActiveModifiers())
	    {
	        if (!(mod instanceof ActiveModifier active)) continue;
	        if (!active.condition().satisfies(p, target, p.getWorld())) continue;
	        
	        if (active.combatStat() == CombatStat.CRIT_RATE)
	            critRate += active.getMagnitude();
	    }
	    
	    return Math.min(critRate, 1.0);
	}

	public static double resolveCritModifier(Player p, LivingEntity target, EchoManifest codec, double baseCritModifier)
	{
	    double critModifier = baseCritModifier;
	    
	    for (Modifier mod : codec.getActiveModifiers())
	    {
	        if (!(mod instanceof ActiveModifier active)) continue;
	        if (!active.condition().satisfies(p, target, p.getWorld())) continue;
	        
	        if (active.combatStat() == CombatStat.CRIT_MODIFIER)
	            critModifier += active.getMagnitude();
	    }
	    
	    return critModifier;
	}

	public static void resolvePassiveEffect(EchoManifest codec, Player p, LivingEntity target)
	{
		for (Modifier mod : codec.getPassiveModifiers())
	    {
	        if (!mod.condition().satisfies(p, target, p.getWorld())) continue;
	        int seconds = (int)(mod.getMagnitude() * 20);
	        switch (((PassiveModifier) mod).effectKey())
	        {
		        case EXPOSE -> CelestioEffects.addExposed(target, seconds);
		        case BURNING -> InfernoEffects.addBurn(target, seconds);
		        case SLOWING  -> EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds, 0, false);
		        case FATIGUING -> EntityEffects.add(target, PotionEffectType.MINING_FATIGUE, seconds, 0, false);
		        case STUNNING  -> EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds, 99, false);
		        case KNOCKBACK, POISONOUS, IGNORE_ARROW, SET_ATTACK_RATE, MOVEMENT_SPEED, LUCKY,
		        NIMBLE, INFINITY -> { /* handled elsewhere */ }
	        }
	    }
	}
	
	public static Set<UUID> ignore_arrow = new HashSet<>();
	public static Set<UUID> increase_movement_speed = new HashSet<>();
	public static Set<UUID> has_lucky = new HashSet<>();
	public static Set<UUID> has_nimble = new HashSet<>();
	public static Set<UUID> negate_arrow_consumption = new HashSet<>();
	
	public static void resolveHeldEffects(PassiveModifier mod, Player p)
	{
		UUID uuid = p.getUniqueId();
		switch(mod.effectKey())
		{
			case LUCKY -> has_lucky.add(uuid);
			case MOVEMENT_SPEED -> increase_movement_speed.add(uuid);
			case IGNORE_ARROW -> ignore_arrow.add(uuid);
			case NIMBLE -> has_nimble.add(uuid);
			case INFINITY -> negate_arrow_consumption.add(uuid);
			case EXPOSE, BURNING, POISONOUS, SLOWING, FATIGUING, STUNNING, KNOCKBACK, SET_ATTACK_RATE -> {}
		}
	}
	
	public static void removeHeldEffects(PassiveModifier mod, Player p)
	{
		UUID uuid = p.getUniqueId();
		switch(mod.effectKey())
		{
			case LUCKY -> has_lucky.remove(uuid);
			case MOVEMENT_SPEED -> increase_movement_speed.remove(uuid);
			case IGNORE_ARROW -> ignore_arrow.remove(uuid);
			case NIMBLE -> has_nimble.remove(uuid);
			case INFINITY -> negate_arrow_consumption.remove(uuid);
			case EXPOSE, BURNING, POISONOUS, SLOWING, FATIGUING, STUNNING, KNOCKBACK, SET_ATTACK_RATE -> {}
		}
	}
	
	
}
