package com.ouroboros.mobs;

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
import com.eol.echoes.records.EchoManifest;
import com.eol.enums.EchoForm;
import com.lol.spells.instances.arcano.Prisma;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.enums.ElementType;
import com.ouroboros.utils.Chance;
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
			    if (MobData.isDying(target.getUniqueId())) return;

			    MobData data = MobData.getMob(target.getUniqueId());
			    if (data == null) return;
			    ElementType element = ElementType.PURE;
			    double dmg;
			    
			    // --- Echo Melee ---
			    if (e instanceof EntityDamageByEntityEvent dmgEvent && dmgEvent.getDamager() instanceof Player p 
			    		&& EchoManager.isEcho(p.getInventory().getItemInMainHand()) && EchoManager.isWeaponEcho(p.getInventory().getItemInMainHand()))
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

			        if (ResolveEchoInteract.celestio_armament.contains(p.getUniqueId())) element = ElementType.CELESTIO;
			        if (ResolveEchoInteract.mortio_armament.contains(p.getUniqueId())) element = ElementType.MORTIO;
			        if (ResolveEchoInteract.inferno_armament.contains(p.getUniqueId())) element = ElementType.INFERNO;
			        if (ResolveEchoInteract.glacio_armament.contains(p.getUniqueId())) element = ElementType.GLACIO;
			        if (ResolveEchoInteract.aero_armament.contains(p.getUniqueId())) element = ElementType.AERO;
			        if (ResolveEchoInteract.geo_armament.contains(p.getUniqueId())) element = ElementType.GEO;
			        if (ResolveEchoInteract.cosmo_armament.contains(p.getUniqueId())) element = ElementType.COSMO;
			        if (ResolveEchoInteract.heresio_armament.contains(p.getUniqueId())) element = ElementType.HERESIO;
			        if (ResolveEchoInteract.arcane_armament.contains(p.getUniqueId())) element = ElementType.ARCANO;
			        
			        EchoData echoData = codec.baseStats();

			        dmg = ResolveEchoInteract.resolveCombatModifiedDamage(p, (LivingEntity) target, codec, echoData.getAttack());
			        
			        if (InfernoEffects.isImbuedPlayer.containsKey(p.getUniqueId()))
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
			        
			        boolean crit 		= false;
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
			        
			        if (PlayerData.getPlayer(p.getUniqueId()).isBreak()) dmg *= 0.5;

			        if (Prisma.scriedElement.containsKey(p.getUniqueId()))
			        {
			        	element = Prisma.scriedElement.get(p.getUniqueId());
			        	Prisma.scriedElement.remove(p.getUniqueId());
			        }
			        
			        if (data.isBreak()) data.breakDamage(dmg, element);
			        else                data.damage(dmg, true, element);
			        
			        ResolveEchoInteract.resolvePassiveEffect(codec, p, (LivingEntity) target);
			        if (ResolveEchoInteract.vampire.contains(p.getUniqueId())) 
			        {
			        	ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.CRIMSON_SPORE, null);
			            PlayerData.heal(p, 25, crit);
			        }
			    }

			    // --- Echo Bow / Arrow ---
			    else if (e instanceof EntityDamageByEntityEvent dmgEvent
			        && dmgEvent.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player p
			        && EchoManager.isEcho(p.getInventory().getItemInMainHand()) && EchoManager.isWeaponEcho(p.getInventory().getItemInMainHand()))
			    {
			        ItemStack echo = p.getInventory().getItemInMainHand();
			        EchoManifest codec = EchoManager.getCodec(echo);
			        if (codec == null) return;
			        
			        if (codec.echoForm() != EchoForm.BOW && codec.echoForm() != EchoForm.CROSSBOW) return;
			        
			        e.setDamage(0);
			        
			        EchoData echoData = codec.baseStats();
			        element = ElementType.PUNCTURE;
			        
			        if (ResolveEchoInteract.celestio_armament.contains(p.getUniqueId())) element = ElementType.CELESTIO;
			        if (ResolveEchoInteract.mortio_armament.contains(p.getUniqueId())) element = ElementType.MORTIO;
			        if (ResolveEchoInteract.inferno_armament.contains(p.getUniqueId())) element = ElementType.INFERNO;
			        if (ResolveEchoInteract.glacio_armament.contains(p.getUniqueId())) element = ElementType.GLACIO;
			        if (ResolveEchoInteract.aero_armament.contains(p.getUniqueId())) element = ElementType.AERO;
			        if (ResolveEchoInteract.geo_armament.contains(p.getUniqueId())) element = ElementType.GEO;
			        if (ResolveEchoInteract.cosmo_armament.contains(p.getUniqueId())) element = ElementType.COSMO;
			        if (ResolveEchoInteract.heresio_armament.contains(p.getUniqueId())) element = ElementType.HERESIO;
			        if (ResolveEchoInteract.arcane_armament.contains(p.getUniqueId())) element = ElementType.ARCANO;
			        
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
			        
			        if (PlayerData.getPlayer(p.getUniqueId()).isBreak()) dmg *= 0.5;

			        if (Prisma.scriedElement.containsKey(p.getUniqueId()))
			        {
			        	element = Prisma.scriedElement.get(p.getUniqueId());
			        	Prisma.scriedElement.remove(p.getUniqueId());
			        }
			        
			        if (data.isBreak()) data.breakDamage(dmg, element);
			        else                data.damage(dmg, true, element);
			        
			        ResolveEchoInteract.resolvePassiveEffect(codec, p, (LivingEntity) target);
			        if (ResolveEchoInteract.vampire.contains(p.getUniqueId())) 
			        {
			        	ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.CRIMSON_SPORE, null);
			        	PlayerData.heal(p, 25, crit);
			        }
			    }

			    // --- Non-Echo Player Hit ---
			    else if (e instanceof EntityDamageByEntityEvent dmgEvent && dmgEvent.getDamager() instanceof Player p && !EchoManager.isEcho(p.getInventory().getItemInMainHand()))
			    {
			        dmgEvent.setDamage(1);
			        dmg = dmgEvent.getFinalDamage();

			        if (Prisma.scriedElement.containsKey(p.getUniqueId()))
			        {
			        	element = Prisma.scriedElement.get(p.getUniqueId());
			        	Prisma.scriedElement.remove(p.getUniqueId());
			        }
			        
			        if (PlayerData.getPlayer(p.getUniqueId()).isBreak()) dmg *= 0.5;
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
				
			    // --- Nameplate, Save, Death ---

			    data.save();

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
			    			"\n                          &b&o- Weakness Damage&r&f: "+(MobAffinity.parseUniversalWeakness(target, element)?"&aTRUE&f ":"&cFALSE&f ")+
			    			"\n                          &b&o- Resistance Damage&r&f: "+(MobAffinity.parseUniversalResistance(target, element)?"&aTRUE&f ":"&cFALSE&f ")+
			    			"\n                          &b&o- Immunity Damage&r&f: "+(MobAffinity.parseUniversalImmunity(target, element)?"&aTRUE&f ":"&cFALSE&f ")+"|| &o&7END");
			    }
			    
			    if (data.isDead())
			    {
			        LivingEntity le = (LivingEntity) target;
			        data.save();
			        e.setDamage(0);

			        MobData.dyingRegistry.add(le.getUniqueId());
			        le.setMetadata("ouroboros_dying", new FixedMetadataValue(Ouroboros.instance, true));

			        if (e instanceof EntityDamageByEntityEvent damageEvent && damageEvent.getDamager() instanceof Player killer)
			            le.setMetadata("ouroboros_killer", new FixedMetadataValue(Ouroboros.instance, killer.getUniqueId().toString()));

			        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () -> le.damage(le.getHealth() + 1.0), 1L);
			        return;
			    }
				
				if (target.getCustomName() == null && !MobData.isDying(target.getUniqueId()))
				{
					MobNameplate.build((LivingEntity) target, true);
					MobNameplate.update((LivingEntity) target);
				}
				else MobNameplate.update((LivingEntity) target);
			}
		}, plugin);
	}
}