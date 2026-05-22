package com.eol.echoes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import com.eol.echoes.records.ActiveEchoModifier;
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
    // -------------------------------------------------------------------------
    // Combat stat resolution (weapon/tool echoes only)
    // -------------------------------------------------------------------------

    public static double resolveCombatModifiedDamage(Player p, LivingEntity target, EchoManifest codec, double initialDamage)
    {
        double damage = initialDamage;
        for (Modifier mod : codec.getActiveModifiers())
        {
            if (!(mod instanceof ActiveEchoModifier active)) continue;
            if (!active.condition().satisfies(p, target, p.getWorld())) continue;
            switch (active.combatStat())
            {
                case ATTACK ->
                {
                    if (active.isPercent()) damage *= (1.0 + active.getMagnitude());
                    else                   damage += active.getMagnitude();
                }
                case ATTACK_RATING -> damage *= (1.0 + active.getMagnitude());
                default -> {}
            }
        }
        return damage;
    }

    public static double resolveCritRate(Player p, LivingEntity target, EchoManifest codec, double baseCritRate)
    {
        double critRate = baseCritRate;
        for (Modifier mod : codec.getActiveModifiers())
        {
            if (!(mod instanceof ActiveEchoModifier active)) continue;
            if (!active.condition().satisfies(p, target, p.getWorld())) continue;
            if (active.combatStat() == CombatStat.CRIT_RATE) critRate += active.getMagnitude();
        }
        return Math.min(critRate, 1.0);
    }

    public static double resolveCritModifier(Player p, LivingEntity target, EchoManifest codec, double baseCritModifier)
    {
        double critModifier = baseCritModifier;
        for (Modifier mod : codec.getActiveModifiers())
        {
            if (!(mod instanceof ActiveEchoModifier active)) continue;
            if (!active.condition().satisfies(p, target, p.getWorld())) continue;
            if (active.combatStat() == CombatStat.CRIT_MODIFIER) critModifier += active.getMagnitude();
        }
        return critModifier;
    }

    // -------------------------------------------------------------------------
    // Passive proc resolution (on-hit, called by combat event)
    // -------------------------------------------------------------------------

    public static void resolvePassiveEffect(EchoManifest codec, Player p, LivingEntity target)
    {
        MobData data = MobData.getMob(target.getUniqueId());
        for (Modifier mod : codec.getPassiveModifiers())
        {
            if (!mod.condition().satisfies(p, target, p.getWorld())) continue;
            if (!Chance.of(mod.getMagnitude() * 100)) continue;
            int seconds = 30;
            switch (((PassiveModifier) mod).effectKey())
            {
                case EXPOSE    -> CelestioEffects.addExposed(target, seconds);
                case BURNING   -> InfernoEffects.addBurn(target, seconds);
                case POISONOUS -> EntityEffects.addErosion(target, 10);
                case SLOWING   -> EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds * 20, 0, false);
                case FATIGUING -> EntityEffects.add(target, PotionEffectType.MINING_FATIGUE, seconds * 20, 0, false);
                case STUNNING  -> data.setBreak();
                case KNOCKBACK, IGNORE_ARROW, RECYCLE_ARROWS, SET_ATTACK_RATE,
                     INCREASED_MOVEMENT_SPEED, DECREASED_MOVEMENT_SPEED, PROTECTIVE,
                     LUCKY, NIMBLE, INFINITY, NIGHTSIGHT, VAMPIRE,
                     HERESIO_ARMAMENT, COSMO_ARMAMENT, CELESTIO_ARMAMENT, MORTIO_ARMAMENT,
                     GEO_ARMAMENT, GLACIO_ARMAMENT, AERO_ARMAMENT, INFERNO_ARMAMENT,
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
            if (!Chance.of(mod.getMagnitude() * 100)) continue;
            int seconds = 30;
            switch (((PassiveModifier) mod).effectKey())
            {
                case EXPOSE    -> CelestioEffects.addExposed(target, seconds);
                case BURNING   -> InfernoEffects.addBurn(target, seconds);
                case POISONOUS -> EntityEffects.addErosion(target, 10);
                case SLOWING   -> EntityEffects.add(target, PotionEffectType.SLOWNESS, seconds * 20, 0, false);
                case FATIGUING -> EntityEffects.add(target, PotionEffectType.MINING_FATIGUE, seconds * 20, 0, false);
                case STUNNING  -> data.setBreak();
                case KNOCKBACK, IGNORE_ARROW, RECYCLE_ARROWS, SET_ATTACK_RATE,
                     INCREASED_MOVEMENT_SPEED, DECREASED_MOVEMENT_SPEED, PROTECTIVE,
                     LUCKY, NIMBLE, INFINITY, NIGHTSIGHT, VAMPIRE,
                     HERESIO_ARMAMENT, COSMO_ARMAMENT, CELESTIO_ARMAMENT, MORTIO_ARMAMENT,
                     GEO_ARMAMENT, GLACIO_ARMAMENT, AERO_ARMAMENT, INFERNO_ARMAMENT,
                     ARCANO_ARMAMENT -> { /* handled elsewhere */ }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Active effect state sets
    // -------------------------------------------------------------------------

    public static Set<UUID> ignore_arrow             = new HashSet<>();
    public static Set<UUID> recycle_arrows           = new HashSet<>();
    public static Set<UUID> increase_movement_speed  = new HashSet<>();
    public static Set<UUID> decrease_movement_speed  = new HashSet<>();
    public static Set<UUID> has_protected            = new HashSet<>();
    public static Set<UUID> has_lucky                = new HashSet<>();
    public static Set<UUID> has_nimble               = new HashSet<>();
    public static Set<UUID> negate_arrow_consumption = new HashSet<>();
    public static Set<UUID> nightsight               = new HashSet<>();
    public static Set<UUID> vampire                  = new HashSet<>();
    public static Set<UUID> celestio_armament        = new HashSet<>();
    public static Set<UUID> mortio_armament          = new HashSet<>();
    public static Set<UUID> inferno_armament         = new HashSet<>();
    public static Set<UUID> glacio_armament          = new HashSet<>();
    public static Set<UUID> aero_armament            = new HashSet<>();
    public static Set<UUID> geo_armament             = new HashSet<>();
    public static Set<UUID> cosmo_armament           = new HashSet<>();
    public static Set<UUID> heresio_armament         = new HashSet<>();
    public static Set<UUID> arcane_armament          = new HashSet<>();

    // -------------------------------------------------------------------------
    // Held effect lifecycle (weapon/tool echoes — EchoHeldEvent)
    // -------------------------------------------------------------------------

    public static void resolveHeldEffects(PassiveModifier mod, Player p)
    {
        addEffect(mod, p.getUniqueId());
    }

    public static void removeHeldEffects(PassiveModifier mod, Player p)
    {
        removeEffect(mod, p.getUniqueId());
    }

    /** Clears all held-weapon passive effects for the player. Called on quit/death by EchoHeldEvent. */
    public static void clearEffects(Player p)
    {
        clearAll(p.getUniqueId());
    }

    // -------------------------------------------------------------------------
    // Equipped effect lifecycle (armor echoes — EchoArmorEvent)
    // -------------------------------------------------------------------------

    public static void resolveEquippedEffects(PassiveModifier mod, Player p)
    {
        addEffect(mod, p.getUniqueId());
    }

    public static void removeEquippedEffects(PassiveModifier mod, Player p)
    {
        removeEffect(mod, p.getUniqueId());
    }

    /**
     * Clears only armor-sourced passive effects.
     * Called on quit/death by EchoArmorEvent independently of EchoHeldEvent,
     * so the two systems don't race to clear each other's effects.
     *
     * Since the same state sets are shared between held and equipped effects,
     * "armor-only" clearing isn't distinguishable at the set level — we clear
     * everything that armor pieces can contribute. Weapon effects are re-applied
     * by EchoHeldEvent's buff task on the next tick anyway, so there is no
     * observable gap for the player.
     */
    public static void clearArmorEffects(Player p)
    {
        // Armor pieces can contribute any universal passive (movement, protection, nightsight)
        // but never weapon-exclusive passives (ignore_arrow, infinity, recycle_arrows, nimble).
        // We only clear what armor can actually have set, keeping weapon state intact.
        UUID uuid = p.getUniqueId();
        increase_movement_speed.remove(uuid);
        decrease_movement_speed.remove(uuid);
        has_protected.remove(uuid);
        has_lucky.remove(uuid);
        nightsight.remove(uuid);
        // Armament passives can appear on armor pieces once that pool is populated
        celestio_armament.remove(uuid);
        mortio_armament.remove(uuid);
        inferno_armament.remove(uuid);
        glacio_armament.remove(uuid);
        aero_armament.remove(uuid);
        geo_armament.remove(uuid);
        cosmo_armament.remove(uuid);
        heresio_armament.remove(uuid);
        arcane_armament.remove(uuid);
    }

    // -------------------------------------------------------------------------
    // Internal helpers — single add/remove path shared by held and equipped
    // -------------------------------------------------------------------------

    private static void addEffect(PassiveModifier mod, UUID uuid)
    {
        switch (mod.effectKey())
        {
            case LUCKY                 -> has_lucky.add(uuid);
            case INCREASED_MOVEMENT_SPEED -> increase_movement_speed.add(uuid);
            case DECREASED_MOVEMENT_SPEED -> decrease_movement_speed.add(uuid);
            case PROTECTIVE            -> has_protected.add(uuid);
            case IGNORE_ARROW          -> ignore_arrow.add(uuid);
            case RECYCLE_ARROWS        -> recycle_arrows.add(uuid);
            case NIMBLE                -> has_nimble.add(uuid);
            case INFINITY              -> negate_arrow_consumption.add(uuid);
            case NIGHTSIGHT            -> nightsight.add(uuid);
            case VAMPIRE               -> vampire.add(uuid);
            case CELESTIO_ARMAMENT     -> celestio_armament.add(uuid);
            case MORTIO_ARMAMENT       -> mortio_armament.add(uuid);
            case INFERNO_ARMAMENT      -> inferno_armament.add(uuid);
            case GLACIO_ARMAMENT       -> glacio_armament.add(uuid);
            case GEO_ARMAMENT          -> geo_armament.add(uuid);
            case AERO_ARMAMENT         -> aero_armament.add(uuid);
            case COSMO_ARMAMENT        -> cosmo_armament.add(uuid);
            case HERESIO_ARMAMENT      -> heresio_armament.add(uuid);
            case ARCANO_ARMAMENT       -> arcane_armament.add(uuid);
            case EXPOSE, BURNING, POISONOUS, SLOWING, FATIGUING,
                 STUNNING, KNOCKBACK, SET_ATTACK_RATE -> {}
        }
    }

    private static void removeEffect(PassiveModifier mod, UUID uuid)
    {
        switch (mod.effectKey())
        {
            case LUCKY                 -> has_lucky.remove(uuid);
            case INCREASED_MOVEMENT_SPEED -> increase_movement_speed.remove(uuid);
            case DECREASED_MOVEMENT_SPEED -> decrease_movement_speed.remove(uuid);
            case PROTECTIVE            -> has_protected.remove(uuid);
            case IGNORE_ARROW          -> ignore_arrow.remove(uuid);
            case RECYCLE_ARROWS        -> recycle_arrows.remove(uuid);
            case NIMBLE                -> has_nimble.remove(uuid);
            case INFINITY              -> negate_arrow_consumption.remove(uuid);
            case NIGHTSIGHT            -> nightsight.remove(uuid);
            case VAMPIRE               -> vampire.remove(uuid);
            case CELESTIO_ARMAMENT     -> celestio_armament.remove(uuid);
            case MORTIO_ARMAMENT       -> mortio_armament.remove(uuid);
            case INFERNO_ARMAMENT      -> inferno_armament.remove(uuid);
            case GLACIO_ARMAMENT       -> glacio_armament.remove(uuid);
            case GEO_ARMAMENT          -> geo_armament.remove(uuid);
            case AERO_ARMAMENT         -> aero_armament.remove(uuid);
            case COSMO_ARMAMENT        -> cosmo_armament.remove(uuid);
            case HERESIO_ARMAMENT      -> heresio_armament.remove(uuid);
            case ARCANO_ARMAMENT       -> arcane_armament.remove(uuid);
            case EXPOSE, BURNING, POISONOUS, SLOWING, FATIGUING,
                 STUNNING, KNOCKBACK, SET_ATTACK_RATE -> {}
        }
    }

    private static void clearAll(UUID uuid)
    {
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