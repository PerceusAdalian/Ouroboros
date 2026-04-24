package com.lol.spells.instances.celestio;

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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.lol.wand.Wand;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.HeresioEffects;
import com.ouroboros.utils.entityeffects.helpers.DivineFavorData;

public class Pneuma extends Spell
{

	public Pneuma() 
	{
		super("Pneuma", "pneuma", Material.END_CRYSTAL, SpellType.ULTIMATE, SpellementType.CELESTIO, CastConditions.MIXED, Rarity.FIVE, 100, 5, false,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&ePneuma&r&f: &e&oCharge&r&f --",
				"&r&fGrants &eDivine Favor &bX&f and &b&oRegenerate&r &bIII&f to self &7(60s)",
				"&r&fSubsequently, all negative status effects, including &2Curses&f, are &e&oCured&r&f.","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&ePneuma&r&f: &e&oPhoton Beam&r&f --",
				"&r&fExpel a concentrated photon beam up to &b&o30 meters&r&f, applying &eExposed&f and dealing",
				PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f damage. Damage scales with &eDivine Favor&f stacks, with a max output of 100&c♥&f.","",
				"&r&e&oDivine Favor &r&eEffect&r&f: Grants &b&oAbsorption&f &r&fand &b&oResistance&f scaled to its magnitude. ",
				"&r&fDivine Favor deteriorates on hit, &e&onon-stackable&r&f, but can be reapplied.","",
				"&r&bEchoic Dissonance&r&f: &e&oSecondary Cast&r&f sets cooldown to &b&o3 minutes&f and costs an extra &b200 &9&lMana&r&f.");
	}

	private static Set<UUID> cooldown = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (cooldown.contains(p.getUniqueId())) 
			{
				PrintUtils.PrintToActionBar(p, "&7&o'&e&oPhoton Beam&7&o' on Cooldown..");
				return -1;
			}
			
			Wand wand = new Wand(e.getItem());
			if (wand.getCurrentMana() < 300)
			{
				PrintUtils.PrintToActionBar(p, "&7&oNot Enough Mana for '&e&oPhoton Beam&7&o'");
				return -1;
			}
			
			EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_CHARGE, SoundCategory.AMBIENT);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ENTITY_WARDEN_SONIC_BOOM, SoundCategory.AMBIENT);
				Block bTarget = RayCastUtils.rayTraceBlock(p, 30);
				Location targetLoc = bTarget != null ? bTarget.getLocation() : p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(30));
				ObsParticles.drawLine(p.getEyeLocation(), targetLoc, 1,   -.5, Particle.CRIT,      null);
				ObsParticles.drawLine(p.getEyeLocation(), targetLoc, 2,   -.6, Particle.WAX_ON,    null);
				ObsParticles.drawLine(p.getEyeLocation(), targetLoc, 3,   -.5, Particle.SQUID_INK, null);
				ObsParticles.drawLine(p.getEyeLocation(), targetLoc, 6,   -.5, Particle.SONIC_BOOM,null);
				ObsParticles.drawLine(p.getEyeLocation(), targetLoc, 0.5, -.5, Particle.END_ROD,   null);
				
				DivineFavorData data = CelestioEffects.divineFavorRegistry.get(p.getUniqueId());
				int divineFavorStacks = data != null ? data.magnitude : 0;
				
				if (!RayCastUtils.createHitBox(p, 6, 5, 30, entity -> 
				{
					if (!(entity instanceof LivingEntity)) return;
					
					MobData.damageUnnaturally(p, (LivingEntity) entity, (10 * divineFavorStacks) + 10, true, true, ElementType.CELESTIO);
					CelestioEffects.addExposed((LivingEntity) entity, 30);
				})) return;
				
				CelestioEffects.divineFavorRegistry.remove(p.getUniqueId());
				p.removePotionEffect(PotionEffectType.ABSORPTION);
				p.removePotionEffect(PotionEffectType.RESISTANCE);
			}, 15);
			
			if (Ouroboros.debug == false)
			{
				cooldown.add(p.getUniqueId());
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->{
					cooldown.remove(p.getUniqueId());
					PrintUtils.PrintToActionBar(p, "&7&oPneuma: Echoic Liberation is castable..");
				}, 3600);				
			}
			
			return 300;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			PrintUtils.PrintToActionBar(p, "&e&oPneuma: Charged");
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_ACTIVATE, SoundCategory.AMBIENT);
			CelestioEffects.addDivineFavor(p, 9, 60);
			EntityEffects.add(p, PotionEffectType.REGENERATION, 1200, 2);
			if (HeresioEffects.isCursed.contains(p.getUniqueId())) 
			{
				HeresioEffects.isCursed.remove(p.getUniqueId());
				PrintUtils.OBSFormatPrint(p, "Your &2Curse&f has been removed.");
			}
			Cure.playSpellEffect(p, p);
			return 100;
		}
		
		return -1;
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return this.getManacost();
	}
	
	public static void registerSpellHelper(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void onPlayerDamage(EntityDamageEvent event)
			{
				if (event.getEntity() instanceof Player p)
				{
					if (CelestioEffects.divineFavorRegistry.containsKey(p.getUniqueId()))
					{
						DivineFavorData data = CelestioEffects.divineFavorRegistry.get(p.getUniqueId());
						
						int magnitude = data.magnitude;
						int seconds = data.seconds;
						
						p.removePotionEffect(PotionEffectType.ABSORPTION);
						p.removePotionEffect(PotionEffectType.RESISTANCE);
						
						if (magnitude == 0) 
						{
							EntityEffects.playSound(p, Sound.BLOCK_CHAIN_BREAK, SoundCategory.AMBIENT);
							PrintUtils.PrintToActionBar(p, "&7&oDivine Favor dissipates..");
							CelestioEffects.divineFavorRegistry.remove(p.getUniqueId());
							return;
						}
						else magnitude = data.magnitude - 1;
						
						PrintUtils.PrintToActionBar(p, "&7&oDivine Favor weakens to "+(int)(magnitude+1)+"..");
						CelestioEffects.addDivineFavor(p, magnitude, seconds);
					}
				}
			}
		}, plugin);
	}

}
