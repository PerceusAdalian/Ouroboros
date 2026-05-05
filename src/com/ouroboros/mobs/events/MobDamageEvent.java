package com.ouroboros.mobs.events;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import com.eol.echoes.EchoData;
import com.eol.echoes.EchoManager;
import com.eol.echoes.ResolveEchoInteract;
import com.eol.echoes.abilities.instances.combat.ImbueFire;
import com.eol.echoes.records.EchoManifest;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.mobs.utils.MobManager;
import com.ouroboros.mobs.utils.MobNameplate;
import com.ouroboros.utils.Chance;
import com.ouroboros.utils.EntityCategories;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.ResolveCombatElement;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;

public class MobDamageEvent implements Listener
{
	public static void register(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener()
		{
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onHit(EntityDamageEvent e)
			{
				Entity target = e.getEntity();
			    if (!(target instanceof LivingEntity)) return;
			    if (!target.getPersistentDataContainer().has(MobManager.MOB_DATA_KEY, PersistentDataType.STRING)) return;
			    if (target.hasMetadata("ouroboros_dying")) return;
			    MobData data = MobData.getMob(target.getUniqueId());
			    if (data == null) return;

			    ElementType element = ElementType.PURE;
			    double dmg;

			    // --- Echo Melee ---
			    if (e instanceof EntityDamageByEntityEvent dmgEvent
			        && dmgEvent.getDamager() instanceof Player p
			        && EchoManager.isEcho(p.getInventory().getItemInMainHand()))
			    {
			        ItemStack echo = p.getInventory().getItemInMainHand();
			        EchoManifest codec = EchoManager.getCodec(echo);
			        if (codec == null) return;

			        // Block bow melee hits entirely
			        if (codec.echoForm() == EchoForm.BOW || codec.echoForm() == EchoForm.CROSSBOW)
			        {
			            e.setCancelled(true);
			            return;
			        }

			        element = ResolveCombatElement.getFromMaterial(echo.getType());
			        EchoData echoData = codec.baseStats();

			        dmg = ResolveEchoInteract.resolveCombatModifiedDamage(p, (LivingEntity) target, codec, echoData.getAttack());
			        float charge = p.getAttackCooldown();
			        dmg *= 0.1 + (0.9 * charge);

			        if (ImbueFire.isImbuedPlayer.containsKey(p.getUniqueId()))
			        {
			            element = ElementType.INFERNO;
			            dmg *= 1.1;
			            target.setFireTicks(100);
			        }

			        if (InfernoEffects.hasCharred.contains(target.getUniqueId()) && Chance.of(10))
			        {
			            InfernoEffects.addBurn((LivingEntity) target, 20);
			            InfernoEffects.hasCharred.remove(target.getUniqueId());
			        }

			        boolean crit = false;
			        double critRate     = ResolveEchoInteract.resolveCritRate(p, (LivingEntity) target, codec, echoData.getCritRate());
			        double critModifier = ResolveEchoInteract.resolveCritModifier(p, (LivingEntity) target, codec, echoData.getCritModifier());
			        if (Chance.of(critRate * 100))
			        {
			            crit = true;
			            ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.CRIMSON_SPORE, null);
			            EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.AMBIENT);
			            dmg *= critModifier;
			        }

			        EchoManager.modifyDurability(p, echo, EchoManager.DurabilityOperation.SUBTRACT, crit ? 2 : 1, false);

			        if (data.isBreak()) data.breakDamage(dmg, element);
			        else                data.damage(dmg, true, element);

			        ResolveEchoInteract.resolvePassiveEffect(codec, p, (LivingEntity) target);
			    }

			    // --- Echo Bow / Arrow ---
			    else if (e instanceof EntityDamageByEntityEvent dmgEvent
			        && dmgEvent.getDamager() instanceof Arrow arrow
			        && arrow.getShooter() instanceof Player p
			        && EchoManager.isEcho(p.getInventory().getItemInMainHand()))
			    {
			        ItemStack echo = p.getInventory().getItemInMainHand();
			        EchoManifest codec = EchoManager.getCodec(echo);
			        if (codec == null) return;

			        // Only intercept if they're actually holding a bow echo
			        if (codec.echoForm() != EchoForm.BOW && codec.echoForm() != EchoForm.CROSSBOW) return;

			        e.setCancelled(true); // cancel vanilla arrow damage

			        EchoData echoData = codec.baseStats();
			        element = ElementType.PUNCTURE;

			        dmg = ResolveEchoInteract.resolveCombatModifiedDamage(p, (LivingEntity) target, codec, echoData.getAttack());

			        boolean crit = false;
			        double critRate     = ResolveEchoInteract.resolveCritRate(p, (LivingEntity) target, codec, echoData.getCritRate());
			        double critModifier = ResolveEchoInteract.resolveCritModifier(p, (LivingEntity) target, codec, echoData.getCritModifier());
			        if (Chance.of(critRate * 100))
			        {
			            crit = true;
			            ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.CRIT, null);
			            EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.AMBIENT);
			            dmg *= critModifier;
			        }

			        EchoManager.modifyDurability(p, echo, EchoManager.DurabilityOperation.SUBTRACT, crit ? 2 : 1, false);

			        if (data.isBreak()) data.breakDamage(dmg, element);
			        else                data.damage(dmg, true, element);

			        ResolveEchoInteract.resolvePassiveEffect(codec, p, (LivingEntity) target);
			    }

			    // --- Non-Echo Player Hit ---
			    else if (e instanceof EntityDamageByEntityEvent dmgEvent
			        && dmgEvent.getDamager() instanceof Player p
			        && !EchoManager.isEcho(p.getInventory().getItemInMainHand()))
			    {
			        dmgEvent.setDamage(1);
			        dmg = dmgEvent.getFinalDamage();

			        if (data.isBreak()) data.breakDamage(dmg, element);
			        else                data.damage(dmg, true, element);
			    }

			    // --- Environmental / Passive Damage ---
			    else
			    {
			        DamageCause cause = e.getCause();

			        element = switch (cause)
			        {
		            	case ENTITY_EXPLOSION, BLOCK_EXPLOSION      -> ElementType.BLAST;
			            case CAMPFIRE, FIRE, LAVA, FIRE_TICK        -> ElementType.INFERNO;
			            case FALL, FALLING_BLOCK                    -> ElementType.GEO;
			            case THORNS                     	 		-> ElementType.PIERCE;
			            case PROJECTILE								-> ElementType.PUNCTURE;
			            case DRAGON_BREATH, CRAMMING, WORLD_BORDER,
			                 VOID, SONIC_BOOM                       -> ElementType.COSMO;
			            case DROWNING, SUFFOCATION, WITHER,
			                 DRYOUT, STARVATION                     -> ElementType.MORTIO;
			            case FREEZE                                 -> ElementType.GLACIO;
			            case FLY_INTO_WALL, LIGHTNING               -> ElementType.AERO;
			            case POISON                                 -> ElementType.CORROSIVE;
			            case MAGIC                                  -> ElementType.ARCANO;
			            default                                     -> ElementType.PURE;
			        };
			        
			        dmg = e.getFinalDamage();

			        if (data.isBreak()) data.breakDamage(dmg, element);
			        else                data.damage(dmg, true, element);
			        
					if (Ouroboros.debug) 
					{
						String name = target.getCustomName();
						PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oNormalDamageEvent&r&f -- &aOK&7 || &fMob: "+
								(name!=null?name:PrintUtils.getFancyEntityName(data.getEntityType()))+"&7 || &fDamage Dealt: &c"+
								dmg+"&7 || &aHP: &f"+data.getHp(false)+"&7/&f"+data.getHp(true)+
								(data.isBreak()?" &7|| &6Break&f: &cTRUE&f":(" &7|| &6Break&f: &aFALSE&7 || &6AR&f: "+
								data.getArmor(false)+"&7/&f"+data.getArmor(true))+" || &o&7END"));
					}
				}
				
			    // --- Handle Nameplate Update, Data Saving, and Death Calls ---
			    if (target.getCustomName() == null)
			    {
			        MobNameplate.build((LivingEntity) target, true);
			        MobNameplate.update((LivingEntity) target);
			    }
			    else MobNameplate.update((LivingEntity) target);

			    data.save();

			    if (data.isDead())
			    {
			        LivingEntity le = (LivingEntity) target;
			        MobNameplate.remove(le);
			        data.save();
			        e.setDamage(0);
			        le.setMetadata("ouroboros_dying", new FixedMetadataValue(Ouroboros.instance, true));
			        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
			            le.damage(le.getHealth() + 1.0), 1L);
			        return;
			    }
				
				if (Ouroboros.debug) 
				{
					String name = target.getCustomName();
					PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oDamageEvent&r&f -- &aOK&7 || &fMob: "+
							(name!=null?name:PrintUtils.getFancyEntityName(data.getEntityType()))+
							"\n                          &7&f- Damage Dealt: &c"+
							dmg+"&7 || &aHP: &f"+data.getHp(false)+"&7/&f"+data.getHp(true)+
							(data.isBreak()?" &7|| &6Break&f: &aTRUE&f":("&7 || &6Break&f: &cFALSE&7 || &6AR&f: "+
							data.getArmor(false)+"&7/&f"+data.getArmor(true)))+
							"\n                          &b&o> DamageType&r&f: "+element.getKey()+
							"\n                          &b&o- Weakness Damage&r&f: "+(EntityCategories.parseUniversalWeakness(target, element)?"&aTRUE&f ":"&cFALSE&f ")+
							"\n                          &b&o- Resistance Damage&r&f: "+(EntityCategories.parseUniversalResistance(target, element)?"&aTRUE&f ":"&cFALSE&f ")+
							"\n                          &b&o- Immunity Damage&r&f: "+(EntityCategories.parseUniversalImmunity(target, element)?"&aTRUE&f ":"&cFALSE&f ")+"|| &o&7END");
				}
			}
		}, plugin);
	}
}
