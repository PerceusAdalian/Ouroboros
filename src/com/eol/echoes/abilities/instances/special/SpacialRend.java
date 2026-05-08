package com.eol.echoes.abilities.instances.special;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.eol.echoes.abilities.enums.AbilityType;
import com.eol.echoes.abilities.instances.EchoAbility;
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
import com.ouroboros.utils.entityeffects.EntityEffects;

public class SpacialRend extends EchoAbility
{

	public SpacialRend() 
	{
		super("Spacial Rend", "spacial_rend", Material.NETHER_STAR, StatType.MELEE, 0, 0, 30, AbilityType.COMBAT, ElementType.COSMO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.SWORD, 
				"&r&3Teleport&r&f to &6target &dMob &fand deal 77&c"+Symbols.HP+PrintUtils.color(ObsColors.COSMO)+" &lCosmo&r&f damage &7(30m)",
				"&r&fIf the target doesn't die, return to previous location.");
	}

	@Override
	public int cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		Location prev = p.getLocation();
		AtomicBoolean fatalDamage = new AtomicBoolean(false);
		
		if (RayCastUtils.getEntity(p, 30, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			
			Location to = le.getLocation();
			Vector dir = p.getLocation().getDirection();
			
			EntityEffects.playSound(p, Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, SoundCategory.AMBIENT);
			
			MobData data = MobData.getMob(le.getUniqueId());
			if (data.getHp(false) - 77 <= 0) fatalDamage.set(true);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				p.teleport(to);
				p.getLocation().setDirection(dir);
				EntityEffects.playSound(p, Sound.ENTITY_PLAYER_TELEPORT, SoundCategory.AMBIENT);
				EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS);
				ObsParticles.drawLandingWave(le);
				MobData.damageUnnaturally(p, le, 77, true, true, ElementType.COSMO);
			}, 20);
		})) return -1;
		
		if (!fatalDamage.get())
		{
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			EntityEffects.playSound(p, Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, SoundCategory.AMBIENT), 40);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->{
				p.teleport(prev);
				EntityEffects.playSound(p, Sound.ENTITY_PLAYER_TELEPORT, SoundCategory.AMBIENT);
				ObsParticles.drawCosmoCastSigil(p);
			}, 50);
		}
		
		return 30;
	}

	@Override
	public int getFinalDurabilityCost() 
	{
		return 30;
	}

}
