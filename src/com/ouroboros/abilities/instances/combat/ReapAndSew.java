package com.ouroboros.abilities.instances.combat;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;
import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.enums.AbilityCategory;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsAbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.OBStandardTimer;
import com.ouroboros.utils.RayCastUtils;

public class ReapAndSew extends AbstractOBSAbility
{

    public ReapAndSew() // Check for functionality
    {
        super("Reap and Sew", "reap_and_sew", Material.BLACK_DYE, StatType.MELEE, 20, 10, ObsAbilityType.COMBAT, ElementType.MORTIO, CastConditions.RIGHT_CLICK_AIR,
                AbilityCategory.SCYTHES, 
                "&r&b&oAbility&r&f: &d&l&oSew&r&f", 
                "&r&fUsage: &d&oRight-Click&r&f target mob to inject them with",
                "&4&lMortio&r&f energy, inflicting &e&oDoom &r&b&lIII &7&o(10m/20s)&r&f",
                "&r&b&oAbility&r&f: &d&l&oReap&r&f",
                "&r&fUsage: &d&oRight-Click&r&f target mob inflicted ",
                "&r&fwith &e&oDoom&r&f to catalyze &e&oechoic synergy&r&f.",
                "&r&d&oReap&r&f what has been &d&oSewn&r&f to immediately inflict all remaining",
                "&r&fseconds as &4&lMortio&r&f &cdmg&f and &a&oheal&r&f self for half the &cdmg&f dealt.");
    }

    private static Map<UUID, Integer> remainingSeconds = new HashMap<>();

    @Override
    public boolean cast(Event e) 
    {
        if (e instanceof PlayerInteractEvent pie)
        {
            int seconds = 20;
            Player p = pie.getPlayer();
            Entity target = RayCastUtils.getNearestEntity(p, 10);
            if (target == null || !(target instanceof LivingEntity)) return false;

            if (!remainingSeconds.containsKey(target.getUniqueId()))
            {
                OBSParticles.drawMortioCastSigil(p);
                EntityEffects.addDoom((LivingEntity) target, 2, 20);
                remainingSeconds.put(target.getUniqueId(), seconds);
                OBStandardTimer.runWithCancel(Ouroboros.instance, (r) -> 
                {
                    Integer secondsRemaining = remainingSeconds.get(target.getUniqueId());
                    if (secondsRemaining == null || secondsRemaining <= 0) {
                        r.cancel();
                        return;
                    }
                    remainingSeconds.put(target.getUniqueId(), secondsRemaining - 1);
                }, 20, 400);

                return true;
            }
            
            else if (EntityEffects.hasDoom.containsKey(target.getUniqueId()))
    {
                OBSParticles.drawMortioCastSigil(p);
                int dmg = remainingSeconds.get(target.getUniqueId());
                OBSParticles.drawLine(p.getLocation(), target.getLocation(), p.getLocation().distance(target.getLocation())%2+1, 0.5, Particle.SMOKE, null);
                Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
                {
                    EntityEffects.playSound(p, target.getLocation(), Sound.ENTITY_WITHER_AMBIENT, SoundCategory.MASTER, 1, 1);
                    OBSParticles.drawVerticalVortex(target.getLocation(), target.getWidth(), target.getHeight()+1, target.getHeight()%2, 3, 6, 0.1, Particle.SMOKE, null);
                    MobData.damageUnnaturally(p, target, dmg, true, ElementType.MORTIO);
                }, 20);
                Bukkit.getScheduler().runTaskLater(Ouroboros.instance,()->
                {
                    OBSParticles.drawLine(target.getLocation(), p.getLocation(), Math.max(1, (int) target.getLocation().distance(p.getLocation())), 0.5, Particle.HAPPY_VILLAGER, null);
                    EntityEffects.playSound(p, p.getLocation(), Sound.ITEM_BONE_MEAL_USE, SoundCategory.AMBIENT, 1, 1);
                    OBSParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.HAPPY_VILLAGER, null);
                    
                    double maxHealth = ((Attributable) p).getAttribute(Attribute.MAX_HEALTH).getValue();
                    p.setHealth(Math.min(p.getHealth() + (dmg / 2.0), maxHealth));
                }, 40);
                remainingSeconds.remove(target.getUniqueId());
                EntityEffects.hasDoom.remove(target.getUniqueId());
                if (((LivingEntity) target).hasPotionEffect(PotionEffectType.WITHER))
                    ((LivingEntity) target).removePotionEffect(PotionEffectType.WITHER);
                return true;   
            }
        }
        return false;
    }
}

/**
 * "&r&eDoom&r&f Description: Target non-&4&lMortio&r&f becomes marked,", 
                "&r&fsustaining a &cDOT&f with severity equal to its &bmagnitude&f.",
                "&r&fNon-&4&lMortio&r&f mobs take an additional &b&l1.25x &r&4&lMortio&r&f &cdmg&f,",
                "&r&fotherwise the inflicted target is &a&ohealed&r&f."
 */