package com.ouroboros.accounts;

import java.util.Set;

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
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.eol.echoes.ArmorData;
import com.eol.echoes.EchoData;
import com.eol.echoes.EchoManager;
import com.eol.echoes.EchoManager.DurabilityOperation;
import com.eol.echoes.ResolveEchoInteract;
import com.eol.echoes.records.EchoManifest;
import com.eol.enums.EchoForm;
import com.lol.spells.instances.aero.Tailwind;
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
import com.ouroboros.utils.entityeffects.CosmoEffects;
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
        	    if (!(e.getEntity() instanceof Player target)) return;

        	    if (target.hasMetadata("ouroboros_death"))
        	    {
        	        target.removeMetadata("ouroboros_death", Ouroboros.instance);
        	        return;
        	    }
        	    
        	    PlayerData data = PlayerData.getPlayer(target.getUniqueId());
        	    if (data == null) return;
        	    
        	    Player pSourceFinal = null;
        	    DamageCause cause = e.getCause();
        	    ElementType element = ElementType.PURE;
        	    double dmg = 0;
        	    boolean criticalPlayerAttack = false;
        	    // --- Resolve PVP Echoes & Spells ---
        	    
        	    // --- Melee Echoes ---
        	    if (e instanceof EntityDamageByEntityEvent dmgEvent && dmgEvent.getDamager() instanceof Player pSource
        	    		&& EchoManager.isEcho(pSource.getInventory().getItemInMainHand()) && EchoManager.isWeaponEcho(pSource.getInventory().getItemInMainHand()))
        	    {
        	    	pSourceFinal = pSource;
        	    	
        	    	ItemStack echo = pSource.getInventory().getItemInMainHand();
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
			        
			        dmg = ResolveEchoInteract.resolveCombatModifiedDamage(pSource, target, codec, echoData.getAttack());
			        
			        if (InfernoEffects.isImbuedPlayer.containsKey(pSource.getUniqueId()))
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
			        double critRate     = ResolveEchoInteract.resolveCritRate(pSource, target, codec, echoData.getCritRate());
			        double critModifier = ResolveEchoInteract.resolveCritModifier(pSource, target, codec, echoData.getCritModifier());
			        if (Chance.of(critRate * 100))
			        {
			            crit = true;
			            criticalPlayerAttack = true;
			            ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.CRIMSON_SPORE, null);
			            EntityEffects.playSound(target, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.AMBIENT);
			            dmg *= critModifier;
			        }
			        
			        EchoManager.modifyDurability(pSource, echo, EchoManager.DurabilityOperation.SUBTRACT, crit ? 2 : 1, false);
			        
			        if (PlayerData.getPlayer(pSource.getUniqueId()).isBreak()) dmg *= 0.5;
			        
			        ResolveEchoInteract.resolvePassiveEffect(codec, pSource, target);
			        if (ResolveEchoInteract.vampire.contains(pSource.getUniqueId())) 
			        {
			        	ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.CRIMSON_SPORE, null);
			        	PlayerData.heal(pSource, 25, crit);
			        }
        	    }
        	    
        	    // --- Ranged Echoes ---
        	    else if (e instanceof EntityDamageByEntityEvent dmgEvent
        	    		&& dmgEvent.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player pSource
        	    		&& EchoManager.isEcho(pSource.getInventory().getItemInMainHand()) && EchoManager.isWeaponEcho(pSource.getInventory().getItemInMainHand()))
        	    {
        	    	pSourceFinal = pSource;
        	    	
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
			        
			        dmg = ResolveEchoInteract.resolveCombatModifiedDamage(pSource, target, codec, echoData.getAttack());
			        boolean crit = false;
			        double critRate     = ResolveEchoInteract.resolveCritRate(pSource, target, codec, echoData.getCritRate());
			        double critModifier = ResolveEchoInteract.resolveCritModifier(pSource, target, codec, echoData.getCritModifier());
			        if (Chance.of(critRate * 100))
			        {
			            crit = true;
			            criticalPlayerAttack = true;
			            ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.CRIT, null);
			            EntityEffects.playSound(pSource, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.AMBIENT);
			            dmg *= critModifier;
			        }
			        
			        EchoManager.modifyDurability(pSource, echo, EchoManager.DurabilityOperation.SUBTRACT, crit ? 2 : 1, false);
			        
			        if (PlayerData.getPlayer(pSource.getUniqueId()).isBreak()) dmg *= 0.5;
			        
			        ResolveEchoInteract.resolvePassiveEffect(codec, pSource, target);
			        if (ResolveEchoInteract.vampire.contains(pSource.getUniqueId())) 
			        {
			        	ObsParticles.drawWisps(target.getLocation(), target.getWidth(), target.getHeight(), 5, Particle.CRIMSON_SPORE, null);
			        	PlayerData.heal(pSource, 25, crit);
			        }
        	    }
        	    
        	    // --- Non-Echo PVP Hit ---
        	    else if (e instanceof EntityDamageByEntityEvent dmgEvent
        	    		&& dmgEvent.getDamager() instanceof Player pSource
        	    		&& !EchoManager.isEcho(pSource.getInventory().getItemInMainHand()))
        	    {
        	    	pSourceFinal = pSource;
        	    	
        	    	e.setDamage(0);
        	    	dmg = dmgEvent.getFinalDamage();
        	    	if (PlayerData.getPlayer(target.getUniqueId()).isBreak()) dmg *= 0.5;
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
		    			case 1 -> NumberUtils.lerp(10,   50,  mData.getLevel(), 1,  19);
		    		    case 2 -> NumberUtils.lerp(55,  100,  mData.getLevel(), 20, 29);
		    		    case 3 -> NumberUtils.lerp(110,  175,  mData.getLevel(), 30, 39);
		    		    case 4 -> NumberUtils.lerp(200,  350, mData.getLevel(), 40, 49);
		    		    case 5 -> NumberUtils.lerp(400, 750, mData.getLevel(), 50, 79);
		    		    case 6 -> NumberUtils.lerp(800, 950, mData.getLevel(), 80, 99);
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
        	                 DRYOUT                                 -> ElementType.MORTIO;
        	            case FREEZE                                 -> ElementType.GLACIO;
        	            case FLY_INTO_WALL, LIGHTNING               -> ElementType.AERO;
        	            case POISON                                 -> ElementType.CORROSIVE;
        	            case MAGIC                                  -> ElementType.ARCANO;
        	            default                                     -> ElementType.PURE;
        	        };

        	        if (cause == DamageCause.FALL && Tailwind.tailwindRegistry.contains(target.getUniqueId()))
        	        {
        	            Tailwind.tailwindRegistry.remove(target.getUniqueId());
        	            e.setCancelled(true);
        	            return;
        	        }
        	        
        	        dmg = e.getFinalDamage();
        	        e.setDamage(0);

        	        dmg = EntityEffects.resolveEffectModifiedDamage(target, element, dmg, cause);
        	        if (dmg < 0) return;

        	        if (CRITICAL_DAMAGE_CAUSES.contains(cause)) dmg = 150;
        	        if (BYPASS_ARMOR_CAUSES.contains(cause))
        	            PlayerData.damageUnnaturally(null, target, dmg, false, false, element, null);
        	        else
        	            PlayerData.damageUnnaturally(null, target, dmg, false, true, element, null);

        	        if (data.getHP() > 0)
        	        {
        	            PlayerData.syncVanillaHealth(target);
        	            PlayerHud.update(target);
        	        }
        	        RestoreArmorTask.markHit(target);
        	        return;
        	    }

		    	// Resolve general effect mitigated damage (Fire Resistance, Resistance, Absorption, Etc)
		    	dmg = EntityEffects.resolveEffectModifiedDamage(target, element, dmg, cause);
		    	if (dmg < 0) return;
		    	
        	 	// --- Pre-damage modifiers (Barbed, Guarded, etc.) ---
        	    
        	    // Barbed: reflect a portion back to the attacker before taking damage
        	    if (GeoEffects.isBarbed.containsKey(target.getUniqueId())
        	        && e instanceof EntityDamageByEntityEvent dmgEvent
        	        && dmgEvent.getDamager() instanceof LivingEntity attacker)
        	    {
        	        int barbedDamage = GeoEffects.isBarbed.get(target.getUniqueId());
        	        MobData.damageUnnaturally(target, attacker, barbedDamage, false, true, ElementType.GEO, null);
        	        dmg = Math.max(0, dmg - barbedDamage);
        	    }

        	    // Infernal Body: ignite the attacker
        	    if (InfernoEffects.hasInfernalBody.contains(target.getUniqueId())
        	        && e instanceof EntityDamageByEntityEvent dmgEvent
        	        && dmgEvent.getDamager() instanceof LivingEntity attacker)
        	    {
        	        InfernoEffects.addBurn(attacker, 10);
        	    }

        	    // Guarded: reduce incoming damage and consume a stack
        	    if (GeoEffects.guarded_registry.containsKey(target.getUniqueId()))
        	    {
        	    	dmg *= 0.5;
        	        GeoEffects.subGuarded(target);
        	    }

        	    // Night Shift: fall damage mitigation
        	    if (cause == DamageCause.FALL && MortioEffects.nightShifted.containsKey(target.getUniqueId()))
        	    	dmg = dmg * (0.1 * MortioEffects.nightShifted.get(target.getUniqueId()));
        	    
        	    // --- Echo Armor Calculations ---
        	    double mitigatedDmgTotal = 0;
        	    double mitigatedCriticalDmg = 0;
        	    double blockChance = 0;
        	    double criticalBlockChance = 0;
        	    int durabilityTotal = 1;
        	    boolean blocked = false;
        	    
        	    ItemStack helmet = target.getInventory().getHelmet();
        	    ItemStack chestplate = target.getInventory().getChestplate();
        	    ItemStack leggings = target.getInventory().getLeggings();
        	    ItemStack boots = target.getInventory().getBoots();
        	    
        	    if (EchoManager.isArmorEcho(helmet))
        	    {
        	    	ArmorData helmetData = EchoManager.getArmorData(helmet);
        	    	mitigatedDmgTotal += helmetData.getArmorRating();
        	    	mitigatedCriticalDmg += helmetData.getCriticalArmorRating();
        	    	blockChance += helmetData.getBlockRate();
        	    	criticalBlockChance += helmetData.getCriticalBlockRate();
        	    }
        	    if (EchoManager.isArmorEcho(chestplate))
        	    {
        	    	ArmorData chestplateData = EchoManager.getArmorData(chestplate);
        	    	mitigatedDmgTotal += chestplateData.getArmorRating();
        	    	mitigatedCriticalDmg += chestplateData.getCriticalArmorRating();
        	    	blockChance += chestplateData.getBlockRate();
        	    	criticalBlockChance += chestplateData.getCriticalBlockRate();
        	    }
        	    if (EchoManager.isArmorEcho(leggings))
        	    {
        	    	ArmorData leggingsData = EchoManager.getArmorData(leggings);
        	    	mitigatedDmgTotal += leggingsData.getArmorRating();
        	    	mitigatedCriticalDmg += leggingsData.getCriticalArmorRating();
        	    	blockChance += leggingsData.getBlockRate();
        	    	criticalBlockChance += leggingsData.getCriticalBlockRate();
        	    }
        	    if (EchoManager.isArmorEcho(boots))
        	    {
        	    	ArmorData bootsData = EchoManager.getArmorData(boots);
        	    	mitigatedDmgTotal += bootsData.getArmorRating();
        	    	mitigatedCriticalDmg += bootsData.getCriticalArmorRating();
        	    	blockChance += bootsData.getBlockRate();
        	    	criticalBlockChance += bootsData.getCriticalBlockRate();
        	    }
        	    
        	    if (criticalPlayerAttack && Chance.of(criticalBlockChance * 100))
        	    {
        	    	mitigatedDmgTotal += mitigatedCriticalDmg;
        	    	EntityEffects.playSound(target, Sound.BLOCK_METAL_STEP, SoundCategory.MASTER);
        	    	if (pSourceFinal != null) EntityEffects.playSound(pSourceFinal, Sound.BLOCK_METAL_HIT, SoundCategory.MASTER);
        	    	durabilityTotal++;
        	    }
        	    if (Chance.of(blockChance * 100))
        	    {
        	    	blocked = true;
        	    	EntityEffects.playSound(target, Sound.ITEM_SHIELD_BLOCK, SoundCategory.AMBIENT);
        	    	durabilityTotal++;
        	    }
        	    
        	    // --- Route through PlayerData damage contract ---
        	    if (CosmoEffects.hasNegate.contains(target.getUniqueId())) // Checks for potential damage negation due to the "Negate" effect.
        	    {
        	    	CosmoEffects.hasNegate.remove(target.getUniqueId());
        	    	EntityEffects.playSound(target, Sound.ITEM_SHIELD_BREAK, SoundCategory.MASTER);
        	    	dmg = 0;
        	    }
        	    
    	        if (BYPASS_ARMOR_CAUSES.contains(cause)) // Damage mitigation (Armor) doesn't apply here.
    	        	PlayerData.damageUnnaturally(pSourceFinal, target, dmg, false, false, element, null);
    	        else
    	        {
    	        	// Only Armor Applicable Damage Calculations are eligible for mitigation via Defending Player's Echo Armor.
    	        	dmg = blocked ? 0 : dmg - mitigatedDmgTotal;
            	    if (dmg < 0) dmg = 0;
    	        	PlayerData.damageUnnaturally(pSourceFinal, target, dmg, false, true, element, null);
    	        }
    	        
    	        // Finally, deduct durability from applicable Echo Armor:
    	        ItemStack helmetRebuild = EchoManager.modifyDurabilityAndReturn(helmet, DurabilityOperation.SUBTRACT, durabilityTotal);
    	        ItemStack chestplateRebuild = EchoManager.modifyDurabilityAndReturn(chestplate, DurabilityOperation.SUBTRACT, durabilityTotal);
    	        ItemStack leggingsRebuild = EchoManager.modifyDurabilityAndReturn(leggings, DurabilityOperation.SUBTRACT, durabilityTotal);
    	        ItemStack bootsRebuild = EchoManager.modifyDurabilityAndReturn(boots, DurabilityOperation.SUBTRACT, durabilityTotal);
    	        
    	        EntityEquipment armor = target.getEquipment();
    	        armor.setHelmet(helmetRebuild);
    	        armor.setChestplate(chestplateRebuild);
    	        armor.setLeggings(leggingsRebuild);
    	        armor.setBoots(bootsRebuild);
    	        
    	        if (data.getHP() > 0)
    	        {
    	            PlayerData.syncVanillaHealth(target);
    	            PlayerHud.update(target);
    	        }
    	        RestoreArmorTask.markHit(target);
    	        
        	    if (Ouroboros.debug)
        	        PrintUtils.OBSConsoleDebug("&e&lEvent&r&f: &b&oPlayerDamageEvent&r&f -- &aOK&7 || &fPlayer: &f" + target.getName()
        	            + " &7|| &fDamage: &c" + dmg
        	            + " &7|| &6Element&f: " + (element == null ? ElementType.PURE.getKey() : element.getKey())
        	            + " &7|| &aHP&f: " + data.getHP() + "&7/&f" + data.getDefaultHP()
        	            + (data.isBreak() ? " &7|| &6Break&f: &cTRUE" : " &7|| &6AR&f: " + data.getArmor() + "&7/&f" + data.getDefaultArmor())
        	            + " &7|| &o&7END");
        	    	
        	}
		}, plugin);
	}
	
	private static final Set<DamageCause> BYPASS_ARMOR_CAUSES = Set.of(
			DamageCause.FALL,
		    DamageCause.WITHER,
		    DamageCause.POISON,
		    DamageCause.STARVATION,
		    DamageCause.DRYOUT,
		    DamageCause.FREEZE,
		    DamageCause.MAGIC
		);
	
	private static final Set<DamageCause> CRITICAL_DAMAGE_CAUSES = Set.of(
		    DamageCause.DROWNING,
		    DamageCause.SUFFOCATION,
		    DamageCause.CRAMMING,
		    DamageCause.VOID,
		    DamageCause.WORLD_BORDER);
}
