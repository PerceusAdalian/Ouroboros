package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.MortioEffects;

public class CausticArrow extends EchoAbility
{

	public CausticArrow()
	{
		super("Caustic Arrow", "caustic_arrow", Material.NETHER_STAR, StatType.RANGED, 0, 0, 10, AbilityType.ULTIMATE, ElementType.HERESIO,
				CastConditions.LEFT_CLICK_AIR, EchoForm.BOW, 
				"&r&fLaunch a "+PrintUtils.color(ObsColors.CORROSIVE)+"&oCorrosive&r&f arrow that applies &4Toxin &bII &7(7m, 30s)","",
				"&r&4Toxin &eEffect&f: Affected are "+PrintUtils.color(ObsColors.CORROSIVE)+"&oPoisoned&r&f in severity",
				"&r&fscaled with &4Toxin&f's &b&omagnitude&r&f. This effect &c&odoes not &r&fstack.");
	}

	private static NamespacedKey arrowKey = new NamespacedKey(Ouroboros.instance, "caustic_arrow");
	
	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		EntityEffects.playSound(p, Sound.ENTITY_ARROW_SHOOT, SoundCategory.AMBIENT);
		Arrow arrow = p.launchProjectile(Arrow.class);
		arrow.getPersistentDataContainer().set(arrowKey, PersistentDataType.BYTE, (byte) 1);
		arrow.setColor(Color.GREEN);
		arrow.setDamage(0);
		arrow.setCritical(false);
		arrow.setGravity(false);
		arrow.setPickupStatus(PickupStatus.DISALLOWED);
		arrow.setShooter(p);
		arrow.setVelocity(arrow.getVelocity().multiply(2.0));
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> arrow.remove(), 40);
		
		return 10;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 10;
	}

	public static void registerAbilityHelper(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public static void arrowCollide(EntityDamageByEntityEvent e)
			{
				if (!(e.getEntity() instanceof LivingEntity le)) return;
				if (e.getDamager() instanceof Arrow arrow && arrow.getShooter() instanceof Player pSource)
				{
					if (arrow.getPersistentDataContainer().has(arrowKey))
					{
						double theta = ObsParticles.deriveDegreeTheta(pSource.getLocation(), le.getLocation());
						ObsParticles.drawAngledArcLine(pSource.getLocation(), le.getLocation(), 0.5, 9, theta, 0.6, Particle.BLOCK_CRUMBLE, Material.EMERALD_BLOCK.createBlockData());
						MortioEffects.addToxin(le, 1, 30);
					}
				}
			}
		}, plugin);
	}
	
}
