package com.ouroboros.accounts;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.eol.echoes.EchoData;
import com.eol.echoes.EchoManager;
import com.eol.echoes.ResolveEchoInteract;
import com.eol.echoes.records.EchoManifest;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.EntityCategories;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.Chance;
import com.ouroboros.utils.NumberUtils;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.ResolveCombatElement;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GeoEffects;
import com.ouroboros.utils.entityeffects.InfernoEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;

public class PlayerDamageEvent
{
	public static void register(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler(priority = EventPriority.HIGHEST)
        	public void playerDamage(EntityDamageEvent e)
        	{
        	    if (!(e.getEntity() instanceof Player p)) return;

        	    PlayerData data = PlayerData.getPlayer(p.getUniqueId());
        	    if (data == null) return;
        	    
        	    DamageCause cause = e.getCause();
        	    ElementType element = ElementType.PURE;
        	    double dmg = 0;

        	    // --- Resolve PVP Echoes & Spells ---
        	    
        	    // --- Melee Echoes ---
        	    if (e instanceof EntityDamageByEntityEvent dmgEvent && dmgEvent.getDamager() instanceof Player pSource && EchoManager.isEcho(pSource.getInventory().getItemInMainHand()))
        	    {
        	    	ItemStack echo = p.getInventory().getItemInMainHand();
        	    	EchoManifest codec = EchoManager.getCodec(echo);
        	    	if (codec == null) return;
        	    	if (codec.echoForm() == EchoForm.BOW || codec.echoForm() == EchoForm.CROSSBOW)
			        {
			            e.setCancelled(true);
			            return;
			        }
        	    	
        	    	element = ResolveCombatElement.getFromMaterial(echo.getType());

			        if (ResolveEchoInteract.celestio_armament.contains(pSource.getUniqueId())) element = ElementType.CELESTIO;
			        if (ResolveEchoInteract.mortio_armament.contains(pSource.getUniqueId())) element = ElementType.MORTIO;
			        if (ResolveEchoInteract.inferno_armament.contains(pSource.getUniqueId())) element = ElementType.INFERNO;
			        if (ResolveEchoInteract.glacio_armament.contains(pSource.getUniqueId())) element = ElementType.GLACIO;
			        if (ResolveEchoInteract.aero_armament.contains(pSource.getUniqueId())) element = ElementType.AERO;
			        if (ResolveEchoInteract.geo_armament.contains(pSource.getUniqueId())) element = ElementType.GEO;
			        if (ResolveEchoInteract.cosmo_armament.contains(pSource.getUniqueId())) element = ElementType.COSMO;
			        if (ResolveEchoInteract.heresio_armament.contains(pSource.getUniqueId())) element = ElementType.HERESIO;
			        if (ResolveEchoInteract.arcane_armament.contains(pSource.getUniqueId())) element = ElementType.ARCANO;
			        
			        EchoData echoData = codec.baseStats();
			        
			        dmg = ResolveEchoInteract.resolveCombatModifiedDamage(pSource, p, codec, echoData.getAttack());
			        
			        if (InfernoEffects.isImbuedPlayer.containsKey(pSource.getUniqueId()))
			        {
			            element = ElementType.INFERNO;
			            dmg *= 1.1;
			            p.setFireTicks(100);
			        }
			        
			        if (InfernoEffects.hasCharred.contains(p.getUniqueId()) && Chance.of(10))
			        {
			            InfernoEffects.addBurn((LivingEntity) p, 20);
			            InfernoEffects.hasCharred.remove(p.getUniqueId());
			        }
			        
			        boolean crit 		= false;
			        double critRate     = ResolveEchoInteract.resolveCritRate(pSource, p, codec, echoData.getCritRate());
			        double critModifier = ResolveEchoInteract.resolveCritModifier(pSource, p, codec, echoData.getCritModifier());
			        if (Chance.of(critRate * 100))
			        {
			            crit = true;
			            ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.CRIMSON_SPORE, null);
			            EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.AMBIENT);
			            dmg *= critModifier;
			        }
			        
			        EchoManager.modifyDurability(p, echo, EchoManager.DurabilityOperation.SUBTRACT, crit ? 2 : 1, false);
			        
			        if (PlayerData.getPlayer(pSource.getUniqueId()).isBreak()) dmg *= 0.5;
			        
			        ResolveEchoInteract.resolvePassiveEffect(codec, pSource, p);
			        if (ResolveEchoInteract.vampire.contains(p.getUniqueId())) 
			        {
			        	ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.CRIMSON_SPORE, null);
			        	PlayerData.heal(pSource, 25, crit);
			        }
        	    }
        	    
        	    // --- Ranged Echoes ---
        	    else if (e instanceof EntityDamageByEntityEvent dmgEvent
        	    		&& dmgEvent.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player pSource
        	    		&& EchoManager.isEcho(pSource.getInventory().getItemInMainHand()))
        	    {
        	    	ItemStack echo = pSource.getInventory().getItemInMainHand();
        	    	EchoManifest codec = EchoManager.getCodec(echo);
        	    	
        	    	if (codec == null) return;
        	    	if (codec.echoForm() != EchoForm.BOW && codec.echoForm() != EchoForm.CROSSBOW) return;
			        
			        e.setDamage(0);
			        
			        EchoData echoData = codec.baseStats();
			        element = ElementType.PUNCTURE;
			        
			        if (ResolveEchoInteract.celestio_armament.contains(pSource.getUniqueId())) element = ElementType.CELESTIO;
			        if (ResolveEchoInteract.mortio_armament.contains(pSource.getUniqueId())) element = ElementType.MORTIO;
			        if (ResolveEchoInteract.inferno_armament.contains(pSource.getUniqueId())) element = ElementType.INFERNO;
			        if (ResolveEchoInteract.glacio_armament.contains(pSource.getUniqueId())) element = ElementType.GLACIO;
			        if (ResolveEchoInteract.aero_armament.contains(pSource.getUniqueId())) element = ElementType.AERO;
			        if (ResolveEchoInteract.geo_armament.contains(pSource.getUniqueId())) element = ElementType.GEO;
			        if (ResolveEchoInteract.cosmo_armament.contains(pSource.getUniqueId())) element = ElementType.COSMO;
			        if (ResolveEchoInteract.heresio_armament.contains(pSource.getUniqueId())) element = ElementType.HERESIO;
			        if (ResolveEchoInteract.arcane_armament.contains(pSource.getUniqueId())) element = ElementType.ARCANO;
			        
			        dmg = ResolveEchoInteract.resolveCombatModifiedDamage(pSource, p, codec, echoData.getAttack());
			        boolean crit = false;
			        double critRate     = ResolveEchoInteract.resolveCritRate(pSource, p, codec, echoData.getCritRate());
			        double critModifier = ResolveEchoInteract.resolveCritModifier(pSource, p, codec, echoData.getCritModifier());
			        if (Chance.of(critRate * 100))
			        {
			            crit = true;
			            ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.CRIT, null);
			            EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.AMBIENT);
			            dmg *= critModifier;
			        }
			        
			        EchoManager.modifyDurability(p, echo, EchoManager.DurabilityOperation.SUBTRACT, crit ? 2 : 1, false);
			        
			        if (PlayerData.getPlayer(pSource.getUniqueId()).isBreak()) dmg *= 0.5;
			        
			        ResolveEchoInteract.resolvePassiveEffect(codec, pSource, p);
			        if (ResolveEchoInteract.vampire.contains(p.getUniqueId())) 
			        {
			        	ObsParticles.drawWisps(p.getLocation(), p.getWidth(), p.getHeight(), 5, Particle.CRIMSON_SPORE, null);
			        	PlayerData.heal(pSource, 25, crit);
			        }
        	    }
        	    
        	    // --- Non-Echo PVP Hit ---
        	    else if (e instanceof EntityDamageByEntityEvent dmgEvent
        	    		&& dmgEvent.getDamager() instanceof Player pSource
        	    		&& !EchoManager.isEcho(pSource.getInventory().getItemInMainHand()))
        	    {
        	    	e.setDamage(0);
        	    	dmg = dmgEvent.getFinalDamage();
        	    	if (PlayerData.getPlayer(p.getUniqueId()).isBreak()) dmg *= 0.5;
        	    }
        	    
        	    // --- Direct Melee Mob-Damage Hit ---
        	    else if (e instanceof EntityDamageByEntityEvent dmgEvent && dmgEvent.getDamager() instanceof LivingEntity le && !(le instanceof Player))
        	    {
        	    	MobData mData = MobData.getMob(le.getUniqueId());
        	    	if (mData == null)
        	    	{
        	    		e.setCancelled(true);
        	    		return;
        	    	}
        	    	
        	    	e.setDamage(0);
        	    	element = EntityCategories.parseElementType(le.getType());
        	    	dmg = switch (Rarity.getRarityForMobLevel(mData.getLevel()))
	    			{
		    			case 1 -> NumberUtils.lerp(1,   20,  mData.getLevel(), 1,  19);
		    		    case 2 -> NumberUtils.lerp(15,  40,  mData.getLevel(), 20, 29);
		    		    case 3 -> NumberUtils.lerp(35,  75,  mData.getLevel(), 30, 39);
		    		    case 4 -> NumberUtils.lerp(70,  100, mData.getLevel(), 40, 49);
		    		    case 5 -> NumberUtils.lerp(150, 450, mData.getLevel(), 50, 79);
		    		    case 6 -> NumberUtils.lerp(500, 750, mData.getLevel(), 80, 99);
	    			    case 7 -> 1000;
	    			    
	    			    default -> 1;
	    			};
        	    }
        	    else
        	    {
        	        element = switch (cause)
        	        {
        	            case ENTITY_EXPLOSION, BLOCK_EXPLOSION      -> ElementType.BLAST;
        	            case CAMPFIRE, FIRE, LAVA, FIRE_TICK        -> ElementType.INFERNO;
        	            case FALL, FALLING_BLOCK                    -> ElementType.GEO;
        	            case THORNS                                 -> ElementType.PIERCE;
        	            case PROJECTILE                             -> ElementType.PUNCTURE;
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
        	    }

        	    // --- Pre-damage modifiers ---

        	    // Barbed: reflect a portion back to the attacker before taking damage
        	    if (GeoEffects.isBarbed.containsKey(p.getUniqueId())
        	        && e instanceof EntityDamageByEntityEvent dmgEvent
        	        && dmgEvent.getDamager() instanceof LivingEntity attacker)
        	    {
        	        int barbedDamage = GeoEffects.isBarbed.get(p.getUniqueId());
        	        MobData.damageUnnaturally(p, attacker, barbedDamage, false, true, ElementType.GEO, null);
        	        dmg = Math.max(0, dmg - barbedDamage);
        	    }

        	    // Infernal Body: ignite the attacker
        	    if (InfernoEffects.hasInfernalBody.contains(p.getUniqueId())
        	        && e instanceof EntityDamageByEntityEvent dmgEvent
        	        && dmgEvent.getDamager() instanceof LivingEntity attacker)
        	    {
        	        InfernoEffects.addBurn(attacker, 10);
        	    }

        	    // Guarded: reduce incoming damage and consume a stack
        	    if (GeoEffects.guarded_registry.containsKey(p.getUniqueId()))
        	    {
        	    	dmg *= 0.5;
        	        GeoEffects.subGuarded(p);
        	    }

        	    // Night Shift: fall damage mitigation
        	    if (cause == DamageCause.FALL && MortioEffects.nightShifted.containsKey(p.getUniqueId()))
        	    	dmg = dmg * (0.1 * MortioEffects.nightShifted.get(p.getUniqueId()));

        	    // --- Route through PlayerData damage contract ---
        	    PlayerData.damageUnnaturally(null, p, dmg, false, element, null);
        	    PlayerData.syncVanillaHealth(p);
        	    PlayerHud.update(p);
        	    RestoreArmorTask.markHit(p);
        	    
        	    if (Ouroboros.debug)
        	        PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oPlayerDamageEvent&r&f -- &aOK&7 || &fPlayer: &f" + p.getName()
        	            + " &7|| &fDamage: &c" + dmg
        	            + " &7|| &6Element&f: " + (element == null ? ElementType.PURE.getKey() : element.getKey())
        	            + " &7|| &aHP&f: " + data.getHP() + "&7/&f" + data.getDefaultHP()
        	            + (data.isBreak() ? " &7|| &6Break&f: &cTRUE" : " &7|| &6AR&f: " + data.getArmor() + "&7/&f" + data.getDefaultArmor())
        	            + " &7|| &o&7END");
        	}
		}, plugin);
	}
}
