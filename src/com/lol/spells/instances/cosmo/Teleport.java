package com.lol.spells.instances.cosmo;

import java.util.HashMap;
import java.util.Map;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.lol.wand.Wand;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CosmoEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Teleport extends Spell
{

	public Teleport()
	{
		super("Teleport", "teleport", Material.ENDER_PEARL, SpellType.CONTROL, SpellementType.COSMO, CastConditions.MIXED, Rarity.THREE, 50, 3, false, true,
				"&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&3Teleport&f: &3&oSpatial Jump&r&f --",
				"&r&3Teleport&f to &d&otarget block &7(50m)&f highlighted via a convergence reticle.","",
				"&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&3Teleport&f: &3&oWormhole&r&f --",
				"&r&fExchange places with target &7(30m)&f applying &3Voided&7 (20s)","",
				"&r&3Voided &eEffect&f: neutralizes affected entity's elemental affinity.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
		{
			if (!RayCastUtils.getEntity(p, 30, target ->
			{
			    if (target == null || !(target instanceof LivingEntity le)) return;

			    Location pLoc = p.getLocation().clone();
			    Location tLoc = le.getLocation().clone();

			    tLoc.setYaw(pLoc.getYaw());
			    tLoc.setPitch(pLoc.getPitch());

			    pLoc.setYaw(le.getLocation().getYaw());
			    pLoc.setPitch(le.getLocation().getPitch());
			    
			    EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			    ObsParticles.drawLine(pLoc, tLoc, 0.8, 0.5, Particle.END_ROD, null);
			    ObsParticles.drawSinLine(pLoc, tLoc, 0.6, Particle.DRAGON_BREATH, 0.5f);
			    ObsParticles.drawLine(pLoc, tLoc, 0.4, 0.5, Particle.ENCHANT, null);
			    
			    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			    {
			    	if (tLoc != null && pLoc != null)
			    	{
			    		if (le instanceof Player) 
			    			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
				    			EntityEffects.playSound((Player) le, Sound.ENTITY_PLAYER_TELEPORT, SoundCategory.AMBIENT), 5);
					    else CosmoEffects.addVoided(le, 20);
			    		
			    		ObsParticles.drawCosmoCastSigil(p);
			    		ObsParticles.drawCosmoCastSigil(le);
			    		
			    		p.teleport(tLoc);
			    		le.teleport(pLoc);			    		
			    	}
			    	else PrintUtils.PrintToActionBar(p, "&c&oTeleport Failed!");
			    	
			    }, 10);
			    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		    		EntityEffects.playSound(p, Sound.ENTITY_PLAYER_TELEPORT, SoundCategory.AMBIENT), 15);
		    
			})) return -1;
			
			return 50;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			Block block = RayCastUtils.rayTraceBlock(p, 50);
			if (block == null || block.getType().equals(Material.AIR)) return -1;
			
			Location bLoc = block.getLocation().clone().add(0.5, 1, 0.5);
			bLoc.setYaw(p.getLocation().getYaw());
			bLoc.setPitch(p.getLocation().getPitch());
			
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		    ObsParticles.drawLine(p.getLocation(), bLoc, 0.8, 0.5, Particle.END_ROD, null);
		    ObsParticles.drawSinLine(p.getLocation(), bLoc, 0.6, Particle.DRAGON_BREATH, 0.5f);
		    ObsParticles.drawLine(p.getLocation(), bLoc, 0.4, 0.5, Particle.ENCHANT, null);
		    
		    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		    {
		    	if (bLoc != null)
		    	{
		    		p.teleport(bLoc);
		    		ObsParticles.drawCosmoCastSigil(p);
			    }
		    	
		    }, 10);
		    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		    	EntityEffects.playSound(p, Sound.ENTITY_PLAYER_TELEPORT, SoundCategory.AMBIENT), 15);
		    
		    return 50;
		}
		
		return -1;
	}

	@Override
	public int getTotalManaCost()
	{
		return 50;
	}
	
	public static void registerSpellHelper(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			private final Map<UUID, BukkitTask> reticleTasks = new HashMap<>();

			@EventHandler
			public void onItemHeld(PlayerItemHeldEvent e)
			{
			    Player p = e.getPlayer();
			    ItemStack newItem = p.getInventory().getItem(e.getNewSlot());

			    BukkitTask existing = reticleTasks.remove(p.getUniqueId());
			    if (existing != null) existing.cancel();
			    
			    if (reticleTasks.containsKey(p.getUniqueId()))
			    {
			        reticleTasks.get(p.getUniqueId()).cancel();
			        reticleTasks.remove(p.getUniqueId());
			    }

			    if (!Wand.isWand(newItem)) return;
			    Wand wand = new Wand(newItem);
			    Spell spell = wand.getSpell(wand.getSpellIndex());
			    if (wand.getSpell(wand.getSpellIndex()) == null || !spell.getInternalName().equals("teleport")) return;

			    BukkitTask task = Bukkit.getScheduler().runTaskTimer(Ouroboros.instance, () ->
			    {
			        if (!p.isOnline()) return;

			        ItemStack held = p.getInventory().getItemInMainHand();
			        if (!Wand.isWand(held)) 
			        { 
			        	return; 
			        }

			        Block block = RayCastUtils.rayTraceBlock(p, 50);
			        if (block != null && !block.getType().equals(Material.AIR) && !p.isSneaking())
			        {
			        	Location centered = block.getLocation().clone().add(0.5, 0.5, 0.5);
			            ObsParticles.drawPoint(centered, Particle.CLOUD, 1.1, null);
			            ObsParticles.drawDisc(centered, 1, 1, 3, 1.1, Particle.END_ROD, null);
			            ObsParticles.drawDisc(centered, 1, 1, 3, 1.1, Particle.BLOCK_CRUMBLE, Material.END_STONE.createBlockData());
		            }
			        else
			        {
			            RayCastUtils.getEntity(p, 30, target ->
			            {
			                if (target == null || !(target instanceof LivingEntity le)) return;
			                ObsParticles.drawDisc(le.getLocation(), le.getWidth()+1.25, 2, 6, 0.5, Particle.PORTAL, null);
				    		ObsParticles.drawDisc(le.getLocation(), le.getWidth()+2,1,6,0.1,Particle.WITCH,null);
			            });
			        }

			    }, 0L, 20L); 

			    reticleTasks.put(p.getUniqueId(), task);
			}
			
			@EventHandler
			public void onQuit(PlayerQuitEvent e)
			{
				Player p = e.getPlayer();
				if (reticleTasks.containsKey(p.getUniqueId()) && Wand.isWand(p.getInventory().getItemInMainHand()))
				{
					BukkitTask reticleTask = reticleTasks.remove(e.getPlayer().getUniqueId());
					if (reticleTask != null) reticleTask.cancel();
				}
			}
		}, plugin);
	}

}
