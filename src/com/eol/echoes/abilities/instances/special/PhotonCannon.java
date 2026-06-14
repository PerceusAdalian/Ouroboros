package com.eol.echoes.abilities.instances.special;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.eol.echoes.EchoManager;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class PhotonCannon extends EchoAbility
{

	public PhotonCannon()
	{
		super("Photon Cannon", "photon_cannon", Material.NETHER_STAR, StatType.MELEE, 0, 0, 0, AbilityType.SPECIALABILITY, ElementType.CELESTIO,
				CastConditions.MIXED, EchoForm.POLEARM, 
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&ePhoton Cannon&f: &e&oCharge&r&f -- &cRemove &f5 &b&oDurability",
				"&r&fGrant &eWard &bV &fand &eEmpowered &bIII &r&fto &6self &7(30s)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&ePhoton Cannon&f: &e&oA.I.O.N. Discharge&r&f -- &cRemove &f15 &b&oDurability",
				"&r&fDeal 150&c"+Symbols.HP+" &e&lImpale&r&f damage as a &d&oLinear AOE &r&7(30m)",
				"&r&bEchoic Resonance&f: If A.I.O.N. is &e&oCharged&r&f, &e&oSecondary Cast&r&f damage",
				"&r&fincreases to &b&o250&r&f, and applies &eHumility &7(20s)","",
				"&eEmpowered Effect&f: Boosts overall attack &c&odamage&r&f and &b&omovement speed&r&f.",
				"&r&eHumility Effect&r&f: Affected take &b&o15%&r&f more "+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f damage.",
				"&r&bEchoic Dissonance&f: On &e&oSecondary Cast &r&b&l-> &r&fEffects Nullified from &e&oPrimary Cast&r&f.");
	}

	public static Set<UUID> charged = new HashSet<>();

	@Override
	public int cast(PlayerInteractEvent e)
	{
	    Player p = e.getPlayer();
	    UUID uid = p.getUniqueId();

	    if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
	    {
	        boolean hasCharge = charged.contains(uid);
	        double damage = hasCharge ? 250 : 150;

	        Location eyeLoc = p.getEyeLocation();
	        Block bTarget = RayCastUtils.rayTraceBlock(p, 30);
	        Location targetLoc = bTarget != null
	            ? bTarget.getLocation()
	            : eyeLoc.clone().add(eyeLoc.getDirection().multiply(30));

	        EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.AMBIENT);
	        ObsParticles.drawAngledArcLine(targetLoc, p.getLocation(), 0.6, 3, -35,  1.5, Particle.CLOUD, null);
	        ObsParticles.drawAngledArcLine(targetLoc, p.getLocation(), 0.6, 3,  215, 1.5, Particle.CLOUD, null);

	        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	        {
	            RayCastUtils.createHitBox(p, 5, 3, 30, target ->
	            {
	                if (!(target instanceof LivingEntity le) || target instanceof Player) return;
	                MobData.damageUnnaturally(p, le, damage, true, true, ElementType.CELESTIO, EchoManager.getCodec(e.getItem()));
	                if (hasCharge) CelestioEffects.addHumility(le, 15);
	            });

	            EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.AMBIENT);
	            ObsParticles.drawLine(eyeLoc, targetLoc, 1.0, -0.5, Particle.CRIT,    null);
	            ObsParticles.drawLine(eyeLoc, targetLoc, 0.5, -0.5, Particle.END_ROD, null);
	            if (hasCharge) ObsParticles.drawLine(eyeLoc, targetLoc, 2.0, -0.6, Particle.WAX_ON, null);
	        }, 18);

	        if (hasCharge)
	        {
	            charged.remove(uid);
	            if (CelestioEffects.hasWard.contains(uid))      CelestioEffects.hasWard.remove(uid);
	            if (CelestioEffects.hasEmpowered.contains(uid)) CelestioEffects.hasEmpowered.remove(uid);

	            for (PotionEffectType effect : new PotionEffectType[]{
	                PotionEffectType.ABSORPTION, PotionEffectType.RESISTANCE,
	                PotionEffectType.FIRE_RESISTANCE, PotionEffectType.HASTE,
	                PotionEffectType.STRENGTH,   PotionEffectType.GLOWING
	            }) if (p.hasPotionEffect(effect)) p.removePotionEffect(effect);
	        }

	        return 15;
	    }

	    if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
	    {
	        if (!CelestioEffects.addWard(p, 4, 30)) return -1;
	        charged.add(uid);
	        CelestioEffects.addEmpowered(p, getFinalDurabilityCost(), getFinalDurabilityCost());
	        return 5;
	    }

	    return -1;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 0;
	}

}
