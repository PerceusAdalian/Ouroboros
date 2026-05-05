package com.eol.echoes.abilities.instances.combat;

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.ObsTimer;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;

public class ReapAndSew extends EchoAbility
{

    public ReapAndSew() // Check for functionality
    {
        super("Reap and Sew", "reap_and_sew", Material.BLACK_DYE, StatType.MELEE, 20, 10, AbilityType.COMBAT, ElementType.MORTIO, CastConditions.RIGHT_CLICK_AIR,
        		EchoForm.SCYTHE, 
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
    public int cast(PlayerInteractEvent e) 
    {
        if (e instanceof PlayerInteractEvent pie)
        {
            int seconds = 20;
            Player p = pie.getPlayer();
            Entity target = RayCastUtils.getEntity(p, 10);
            if (target == null || !(target instanceof LivingEntity) || target instanceof Player) return -1;

            if (!remainingSeconds.containsKey(target.getUniqueId()))
            {
                ObsParticles.drawMortioCastSigil(p);
                ObsParticles.drawLine(p.getLocation(), target.getLocation(), 3, 0.5, Particle.SMOKE, null);
                MortioEffects.addDoom((LivingEntity) target, 2, 20);
                remainingSeconds.put(target.getUniqueId(), seconds);
                ObsTimer.runWithCancel(Ouroboros.instance, (r) -> 
                {
                    Integer secondsRemaining = remainingSeconds.get(target.getUniqueId());
                    if (secondsRemaining == null || secondsRemaining <= 0) {
                        r.cancel();
                        return;
                    }
                    remainingSeconds.put(target.getUniqueId(), secondsRemaining - 1);
                }, 20, 400);

                return -1;
            }
            
            else if (MortioEffects.hasDoom.containsKey(target.getUniqueId()))
            {
                ObsParticles.drawMortioCastSigil(p);
                ObsParticles.drawLine(p.getLocation(), target.getLocation(), 3, 0.5, Particle.SMOKE, null);
                ObsParticles.drawLine(p.getLocation(), target.getLocation(), 3, 0.5, Particle.ASH, null);

                int dmg = remainingSeconds.get(target.getUniqueId());
                
                Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
                {
                	p.setVelocity(target.getLocation().toVector().subtract(p.getLocation().toVector()).normalize());
                    EntityEffects.playSound(p, Sound.ENTITY_WITHER_AMBIENT, SoundCategory.MASTER);
                    ObsParticles.drawVerticalVortex(target.getLocation(), target.getWidth(), target.getHeight()+1, target.getHeight()+2, 3, 6, 0.1, Particle.SMOKE, null);
                    ObsParticles.drawVerticalVortex(target.getLocation(), target.getWidth(), target.getHeight()+1, target.getHeight()+2, 3, 6, 0.1, Particle.SCULK_SOUL, null);
                    MobData.damageUnnaturally(p, target, dmg, true, true, ElementType.MORTIO);
                }, 10);
                Bukkit.getScheduler().runTaskLater(Ouroboros.instance,()->
                {
                    ObsParticles.drawLine(target.getLocation(), p.getLocation(), Math.max(1, (int) target.getLocation().distance(p.getLocation())), 0.5, Particle.HAPPY_VILLAGER, null);
                    EntityEffects.playSound(p, Sound.ITEM_BONE_MEAL_USE, SoundCategory.AMBIENT);
                    ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.HAPPY_VILLAGER, null);
                    
                    double maxHealth = ((Attributable) p).getAttribute(Attribute.MAX_HEALTH).getValue();
                    p.setHealth(Math.min(p.getHealth() + (dmg / 2.0), maxHealth));
                }, 20);
                remainingSeconds.remove(target.getUniqueId());
                MortioEffects.hasDoom.remove(target.getUniqueId());
                if (((LivingEntity) target).hasPotionEffect(PotionEffectType.WITHER))
                    ((LivingEntity) target).removePotionEffect(PotionEffectType.WITHER);
                return -1;   
            }
        }
        return -1;
    }
}

/**
 * "&r&eDoom&r&f Description: Target non-&4&lMortio&r&f becomes marked,", 
                "&r&fsustaining a &cDOT&f with severity equal to its &bmagnitude&f.",
                "&r&fNon-&4&lMortio&r&f mobs take an additional &b&l1.25x &r&4&lMortio&r&f &cdmg&f,",
                "&r&fotherwise the inflicted target is &a&ohealed&r&f."
 */