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
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.Chance;
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
		MobData data = MobData.getMob(target.getUniqueId());
		for (Modifier mod : codec.getPassiveModifiers())
	    {
	        if (!mod.condition().satisfies(p, target, p.getWorld())) continue;
	        if (!Chance.of(mod.getMagnitude()*100)) continue;
	        
	        int seconds = 30;
	        switch (((PassiveModifier) mod).effectKey())
	        {
		        case EXPOSE -> CelestioEffects.addExposed(target, seconds);
		        case BURNING -> InfernoEffects.addBurn(target, seconds);
		        case POISONOUS -> EntityEffects.addErosion(target, 10);
		        case SLOWING  -> EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds * 20, 0, false);
		        case FATIGUING -> EntityEffects.add(target, PotionEffectType.MINING_FATIGUE, seconds * 20, 0, false);
		        case STUNNING  -> data.setBreak();
		        case KNOCKBACK, IGNORE_ARROW, RECYCLE_ARROWS, SET_ATTACK_RATE, INCREASED_MOVEMENT_SPEED, DECREASED_MOVEMENT_SPEED, PROTECTIVE,
		        LUCKY,NIMBLE, INFINITY, NIGHTSIGHT, VAMPIRE, HERESIO_ARMAMENT, COSMO_ARMAMENT, CELESTIO_ARMAMENT,
		        MORTIO_ARMAMENT, GEO_ARMAMENT, GLACIO_ARMAMENT, AERO_ARMAMENT, INFERNO_ARMAMENT,
		        ARCANO_ARMAMENT -> { /* handled elsewhere */ }
	        }
	    }
	}
	
	public static void resolvePassiveEffectToPlayer(EchoManifest codec, Player source, Player target)
	{
		PlayerData data = PlayerData.getPlayer(target.getUniqueId());
		for (Modifier mod : codec.getPassiveModifiers())
	    {
	        if (!mod.condition().satisfies(source, target, source.getWorld())) continue;
	        if (!Chance.of(mod.getMagnitude()*100)) continue;
	        
	        int seconds = 30;
	        switch (((PassiveModifier) mod).effectKey())
	        {
		        case EXPOSE -> CelestioEffects.addExposed(target, seconds);
		        case BURNING -> InfernoEffects.addBurn(target, seconds);
		        case POISONOUS -> EntityEffects.addErosion(target, 10);
		        case SLOWING  -> EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds * 20, 0, false);
		        case FATIGUING -> EntityEffects.add(target, PotionEffectType.MINING_FATIGUE, seconds * 20, 0, false);
		        case STUNNING  -> data.setBreak();
		        case KNOCKBACK, IGNORE_ARROW, RECYCLE_ARROWS, SET_ATTACK_RATE, INCREASED_MOVEMENT_SPEED, DECREASED_MOVEMENT_SPEED, PROTECTIVE,
		        LUCKY,NIMBLE, INFINITY, NIGHTSIGHT, VAMPIRE, HERESIO_ARMAMENT, COSMO_ARMAMENT, CELESTIO_ARMAMENT,
		        MORTIO_ARMAMENT, GEO_ARMAMENT, GLACIO_ARMAMENT, AERO_ARMAMENT, INFERNO_ARMAMENT,
		        ARCANO_ARMAMENT -> { /* handled elsewhere */ }
	        }
	    }
	}
	
	public static Set<UUID> ignore_arrow = new HashSet<>();
	public static Set<UUID> recycle_arrows = new HashSet<>();
	public static Set<UUID> increase_movement_speed = new HashSet<>();
	public static Set<UUID> decrease_movement_speed = new HashSet<>();
	public static Set<UUID> has_protected = new HashSet<>();
	public static Set<UUID> has_lucky = new HashSet<>();
	public static Set<UUID> has_nimble = new HashSet<>();
	public static Set<UUID> negate_arrow_consumption = new HashSet<>();
	public static Set<UUID> nightsight = new HashSet<>();
	public static Set<UUID> vampire = new HashSet<>();
	public static Set<UUID> celestio_armament = new HashSet<>();
	public static Set<UUID> mortio_armament = new HashSet<>();
	public static Set<UUID> inferno_armament = new HashSet<>();
	public static Set<UUID> glacio_armament = new HashSet<>();
	public static Set<UUID> aero_armament = new HashSet<>();
	public static Set<UUID> geo_armament = new HashSet<>();
	public static Set<UUID> cosmo_armament = new HashSet<>();
	public static Set<UUID> heresio_armament = new HashSet<>();
	public static Set<UUID> arcane_armament = new HashSet<>();
	
	public static void resolveHeldEffects(PassiveModifier mod, Player p)
	{
		UUID uuid = p.getUniqueId();
		switch(mod.effectKey())
		{
			case LUCKY -> has_lucky.add(uuid);
			case INCREASED_MOVEMENT_SPEED -> increase_movement_speed.add(uuid);
			case DECREASED_MOVEMENT_SPEED -> decrease_movement_speed.add(uuid);
			case PROTECTIVE -> has_protected.add(uuid);
			case IGNORE_ARROW -> ignore_arrow.add(uuid);
			case RECYCLE_ARROWS -> recycle_arrows.add(uuid);
			case NIMBLE -> has_nimble.add(uuid);
			case INFINITY -> negate_arrow_consumption.add(uuid);
			case NIGHTSIGHT -> nightsight.add(uuid);
			case VAMPIRE -> vampire.add(uuid);
			case CELESTIO_ARMAMENT -> celestio_armament.add(uuid);
			case MORTIO_ARMAMENT -> mortio_armament.add(uuid);
			case INFERNO_ARMAMENT -> inferno_armament.add(uuid);
			case GLACIO_ARMAMENT -> glacio_armament.add(uuid);
			case GEO_ARMAMENT -> geo_armament.add(uuid);
			case AERO_ARMAMENT -> aero_armament.add(uuid);
			case COSMO_ARMAMENT -> cosmo_armament.add(uuid);
			case HERESIO_ARMAMENT -> heresio_armament.add(uuid);
			case ARCANO_ARMAMENT -> arcane_armament.add(uuid);
			case EXPOSE, BURNING, POISONOUS, SLOWING, FATIGUING, STUNNING, KNOCKBACK, SET_ATTACK_RATE -> {}
		}
	}
	
	public static void removeHeldEffects(PassiveModifier mod, Player p)
	{
		UUID uuid = p.getUniqueId();
		switch(mod.effectKey())
		{
			case LUCKY -> has_lucky.remove(uuid);
			case INCREASED_MOVEMENT_SPEED -> increase_movement_speed.remove(uuid);
			case DECREASED_MOVEMENT_SPEED -> decrease_movement_speed.remove(uuid);
			case PROTECTIVE -> has_protected.remove(uuid);
			case IGNORE_ARROW -> ignore_arrow.remove(uuid);
			case RECYCLE_ARROWS -> recycle_arrows.remove(uuid);
			case NIMBLE -> has_nimble.remove(uuid);
			case INFINITY -> negate_arrow_consumption.remove(uuid);
			case NIGHTSIGHT -> nightsight.remove(uuid);
			case VAMPIRE -> vampire.remove(uuid);
			case CELESTIO_ARMAMENT -> celestio_armament.remove(uuid);
			case MORTIO_ARMAMENT -> mortio_armament.remove(uuid);
			case INFERNO_ARMAMENT -> inferno_armament.remove(uuid);
			case GLACIO_ARMAMENT -> glacio_armament.remove(uuid);
			case GEO_ARMAMENT -> geo_armament.remove(uuid);
			case AERO_ARMAMENT -> aero_armament.remove(uuid);
			case COSMO_ARMAMENT -> cosmo_armament.remove(uuid);
			case HERESIO_ARMAMENT -> heresio_armament.remove(uuid);
			case ARCANO_ARMAMENT -> arcane_armament.remove(uuid);
			case EXPOSE, BURNING, POISONOUS, SLOWING, FATIGUING, STUNNING, KNOCKBACK, SET_ATTACK_RATE -> {}
		}
	}
	
	public static void clearEffects(Player p)
	{
		UUID uuid = p.getUniqueId();
	    ignore_arrow.remove(uuid);
	    recycle_arrows.remove(uuid);
	    negate_arrow_consumption.remove(uuid);
	    increase_movement_speed.remove(uuid);
	    decrease_movement_speed.remove(uuid);
	    has_protected.remove(uuid);
	    has_lucky.remove(uuid);
	    has_nimble.remove(uuid);
	    nightsight.remove(uuid);
	    vampire.remove(uuid);
		celestio_armament.remove(uuid);
		mortio_armament.remove(uuid);
		inferno_armament.remove(uuid);
		glacio_armament.remove(uuid);
		geo_armament.remove(uuid);
		aero_armament.remove(uuid);
		cosmo_armament.remove(uuid);
		heresio_armament.remove(uuid);
		arcane_armament.remove(uuid);
	}
	
	
}
