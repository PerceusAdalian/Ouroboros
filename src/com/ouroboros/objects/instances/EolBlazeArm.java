package com.ouroboros.objects.instances;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ouroboros.Ouroboros;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.Rarity;
import com.ouroboros.objects.AbstractObsObject;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PlayerActions;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class EolBlazeArm extends AbstractObsObject
{

	public EolBlazeArm() 
	{
		super("ΣOL: Blaze Arm", "eol_blaze_arm", Material.BLAZE_ROD, true, false, 
				PrintUtils.assignRarity(Rarity.FIVE),
				PrintUtils.assignElementType(ElementType.INFERNO),
				"&r&f&l&nUsage&r&f: ", 
				"&d&oLeft-Click&r&f to cast &e&oBlazing Rage &r&fcombo&r&f",
				"&r&e&oBlazing Rage &r&fSpells&r&f: ",
				"&r&f&l1. &r&e&oFlame Whip &r&7| &r&f&l2. &r&e&oFireball &r&b&lII &r&7| &r&f&l3. &r&e&oInferno",
				"&d&oRight-Click&r&f target block to cast &e&oEruption &lα &7&o(Range: 20m\\CD:30s)",
				"&r&f&lAlt-Fire: Shift_Right-Click to cast &e&oWard &r&b&lIII &r&7&o(20s\\CD:30s)",
				"",
				"&r&b> &oPassive&r&f: Grants &c&oFire Immunity &r&fwhile held.",
				"",
				"&r&7&oA remnant of a Blaze Elemental.. It radiates with &c&oInferno&r&7&o energy..");
	}

	private static Map<UUID, Integer> comboTier = new HashMap<>();
	private static Map<UUID, Boolean> comboEndNaturally = new HashMap<>();
	private static Map<UUID, Boolean> EruptionCooldown = new HashMap<>();
	private static Map<UUID, Boolean> WardCooldown = new HashMap<>();
	
	@Override
	public boolean cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		
		if (PlayerActions.shiftRightClickAir(e))
		{
			if (WardCooldown.containsKey(p.getUniqueId())) 
			{
				EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1, 1);
				PrintUtils.PrintToActionBar(p, "&r&f&oWard Cooldown");
				return false;
			}
			
			if (p.hasPotionEffect(PotionEffectType.ABSORPTION)) return false;
			EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, SoundCategory.AMBIENT, 1, 1);
			OBSParticles.drawInfernoCastSigil(p);
			EntityEffects.add(p, PotionEffectType.ABSORPTION, 400, 2, true);
			WardCooldown.put(p.getUniqueId(), true);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.AMBIENT, 1, 1);
				PrintUtils.PrintToActionBar(p, "&r&f&oWard is now castable");
				EruptionCooldown.remove(p.getUniqueId());
			}, 600);
			
			return true;
		}
		
		if (PlayerActions.leftClickAir(e))
		{
			UUID uuid = p.getUniqueId();
			if (!comboTier.containsKey(uuid))
			{
				if (comboEndNaturally.containsKey(uuid)) 
				{
					comboEndNaturally.remove(uuid);
				}
				
				EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, SoundCategory.MASTER, 1, 1);
				OBSParticles.drawInfernoCastSigil(p);
				SmallFireball fb = (SmallFireball) p.getWorld().spawnEntity(p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(1.5)), EntityType.SMALL_FIREBALL);
				fb.setShooter(p);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, SoundCategory.MASTER, 1, 1);
					SmallFireball fb2 = (SmallFireball) p.getWorld().spawnEntity(p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(1.5)), EntityType.SMALL_FIREBALL);
					fb2.setShooter(p);
				}, 3);
				
				comboTier.put(uuid, 1);
				comboEndNaturally.put(uuid, false);
				
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
					PrintUtils.PrintToActionBar(p, "Combo Start"), 5);
				
				Bukkit.getScheduler().runTaskLaterAsynchronously(Ouroboros.instance, ()->
				{
					if (comboEndNaturally.get(uuid).equals(true)) return;
					if (comboTier.containsKey(uuid)) 
					{	
						comboTier.remove(uuid);
						EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
						PrintUtils.PrintToActionBar(p, "Combo Break");
					}
				}, 205);
				
				return true;
			}
			
			if (comboTier.containsKey(uuid) && comboTier.get(uuid).equals(1)) 
			{
				
				EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, SoundCategory.MASTER, 1, 1);
				OBSParticles.drawInfernoCastSigil(p);
				Fireball fb = (Fireball) p.getWorld().spawnEntity(p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(1.5)), EntityType.FIREBALL);
				fb.setYield(2);
				comboTier.put(uuid, 2);
				return true;
			}
			
			if (comboTier.containsKey(uuid) && comboTier.get(uuid).equals(2)) 
			{
				EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_WITHER_SHOOT, SoundCategory.MASTER, 1, 1);
				OBSParticles.drawInfernoCastSigil(p);					
				LargeFireball fb = (LargeFireball) p.getWorld().spawnEntity(p.getEyeLocation().add(p.getEyeLocation().getDirection().normalize().multiply(2.0)), EntityType.FIREBALL);
				fb.setShooter(p);
				fb.setYield(4);
				fb.setIsIncendiary(true);
				Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				{
					EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_CHAIN_BREAK, SoundCategory.MASTER, 1, 1);
					PrintUtils.PrintToActionBar(p, "Combo End");
				}, 5);
				comboTier.remove(uuid);
				comboEndNaturally.put(uuid, true);
				return true;
			}
		}
		
		if (PlayerActions.rightClickAir(e))
		{
			if (EruptionCooldown.containsKey(p.getUniqueId())) 
			{
				EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, SoundCategory.AMBIENT, 1, 1);
				PrintUtils.PrintToActionBar(p, "&r&f&oEruption Cooldown");
				return false;
			}
			Block b = RayCastUtils.rayTraceBlock(p, 30);
			if (b == null || b.getType().equals(Material.AIR)) return false;
			
			EntityEffects.playSound(p, p.getLocation(), Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT, 1, 1);
			OBSParticles.drawLine(p.getLocation(), b.getLocation(), 4, 0.1, Particle.LAVA, null);
			OBSParticles.drawSpiralVortex(b.getLocation(), 2, 4, -0.1, Particle.LAVA, null);
			
			double ticks = b.getLocation().distance(p.getLocation());
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				p.getWorld().createExplosion(b.getLocation(), 4, true, false, p);
				EntityEffects.playSound(p, b.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.AMBIENT, 1, 1);
			}, (long) ticks+40);
			
			EruptionCooldown.put(p.getUniqueId(), true);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, p.getLocation(), Sound.BLOCK_BLASTFURNACE_FIRE_CRACKLE, SoundCategory.AMBIENT, 1, 1);
				PrintUtils.PrintToActionBar(p, "&r&f&oEruption is now castable");
				EruptionCooldown.remove(p.getUniqueId());
			}, 600);
			
			return true;
		}

		return false;
	}
	
	public static void registerHeldEvent(JavaPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
        {	
        	@EventHandler
        	public void onJoin(PlayerItemHeldEvent e) 
        	{
        		Player p = e.getPlayer();
        		AbstractObsObject blazearm = new EolBlazeArm();
        		ItemStack blazearmStack = blazearm.toItemStack();
        		
        		new BukkitRunnable()
        		{
        			@Override
        			public void run()
        			{
        				ItemStack held = p.getInventory().getItemInMainHand();
            			if (held == null || !held.isSimilar(blazearmStack))
            			{
            				cancel();
            				return;
            			}
            			
            			EntityEffects.add(p, PotionEffectType.FIRE_RESISTANCE, 100, 0, true);
        			}
        		}.runTaskTimer(Ouroboros.instance, 0L, 20L);
        	}
        	
        }, plugin);
	}

}
