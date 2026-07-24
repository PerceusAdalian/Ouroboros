package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.eol.echoes.EchoManager;
import com.eol.echoes.EchoManager.DurabilityOperation;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.echoes.records.EchoManifest;
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
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Annihilate extends EchoAbility
{

	public Annihilate()
	{
		super("Annihilate", "annihilate", Material.NETHER_STAR, StatType.MELEE, 0, 0, 500, AbilityType.ULTIMATE, ElementType.COSMO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.HATCHET,
				"&r&3Teleport&f to &6target &dMob&f dealing "+PrintUtils.color(ObsColors.COSMO)+"&lCosmo&r&f damage equal to",
				"&r&b&o350% &r&f&oBase Attack&r&f x &r&f&oBase Crit Modifier&r&f inflicting &3Voided &r&7(25, 10s)",
				"&r&fIf the target dies, &arestore &b&oDurability&r&f by the difference in negative &cHP&f.","",
				"&r&3Voided &eEffect&f: Neutralizes elemental affinity.");
	}
	
	@Override
	public int cast(PlayerInteractEvent e)
	{
	    Player p = e.getPlayer();
	    EquipmentSlot hand = e.getHand();

	    if (!RayCastUtils.getEntity(p, 25, target ->
	    {
	        if (!(target instanceof LivingEntity le) || target instanceof Player) return;

	        Location to = le.getLocation().clone();
	        Vector dir = p.getLocation().getDirection().clone();

	        EntityEffects.playSound(p, Sound.ENTITY_ILLUSIONER_PREPARE_MIRROR, SoundCategory.AMBIENT);
	        double theta = ObsParticles.deriveDegreeTheta(p.getLocation(), to);
	        ObsParticles.drawAngledArcLine(p.getLocation(), to, 0.6, 10, theta, 0.3, Particle.GLOW_SQUID_INK, null);
	        ObsParticles.drawAngledArcLine(p.getLocation(), to, 0.6, 10, theta, 0.3, Particle.END_ROD, null);

	        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	        {
	            p.teleport(to);
	            p.getLocation().setDirection(dir);
	            EntityEffects.playSound(p, Sound.ENTITY_PLAYER_TELEPORT, SoundCategory.AMBIENT);
	            EntityEffects.playSound(p, Sound.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS);
	            ObsParticles.drawLandingWave(le);

	            MobData data = MobData.getMob(le.getUniqueId());
	            
	            ItemStack liveItem = p.getInventory().getItem(hand);
	            if (liveItem == null) return;
	            
	            EchoManifest codec = EchoManager.getCodec(liveItem);

	            double damage = codec.baseStats().getCritModifier() * (codec.baseStats().getAttack() * 3.5);
	            boolean fatal = MobData.checkFatalDamage(data, damage);

	            MobData.damageUnnaturally(p, le, damage, true, true, ElementType.COSMO, EchoManager.getCodec(e.getItem()));

	            if (fatal)
	            {
	                ObsParticles.drawCosmoCastSigil(p);
	                int refundDurability = (int) MobData.getOverflowFatalDamage(data, damage);
	                Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	                {
	                    ItemStack itemAtWrite = p.getInventory().getItem(hand);
	                    if (itemAtWrite == null) return;
	                    EchoManager.modifyDurability(p, itemAtWrite, DurabilityOperation.ADD, refundDurability, false);
	                }, 1);
	            }
	        }, 10);
	    })) return -1;
	    return 500;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 500;
	}

}
