package com.lol.spells.instances.celestio;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;

public class Lumos extends Spell
{

	public Lumos() 
	{
		super("Lumos", "lumos", Material.SUNFLOWER, SpellType.UTILITY, SpellementType.CELESTIO, CastConditions.MIXED, Rarity.ONE, 5, 1, false,
				"&r&e&oPrimary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&fSummon a &e&lCelestio&r&f sprite to illuminate surroundings &7(60s)",
				"&r&fRecast to trigger &b&oEchoic Dissonance&r&f, exploding the sprite",
				"&r&fand applying &eExpose&f within &b&o10m&r&7 (15s)","",
				"&r&e&oSecondary "+PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_BLOCK),
				"&r&fCast a whisp of light at target block &7(30s)","",
				"&r&7&oIn &r&eFantasia's Academy for Mystical Arts&r&7&o, this spell is formally registered",
				"&r&7as '&e&oIlluminate&r&7&o', however, colloquially known as '&r&e&oLumos&r&7&o'.");
	}

	private HashMap<UUID, LumosTask> activeLumos = new HashMap<>();
	
	@Override
	public boolean Cast(PlayerInteractEvent e) 
	{
		Player p = e.getPlayer();
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
		{
			if (activeLumos.containsKey(p.getUniqueId()))
			{
			    LumosTask lumosData = activeLumos.get(p.getUniqueId()); // Store reference before scheduling
			    Location explosionLoc = lumosData.crystal.getLocation();
			    OBSParticles.drawWisps(explosionLoc, 5, 5, 3, Particle.GUST_EMITTER_LARGE, null);
			    OBSParticles.drawCylinder(explosionLoc, 2, 2, 5, 0.5, 0, Particle.ENCHANT, null);
			    EntityEffects.playSound(p, Sound.ENTITY_ALLAY_DEATH, SoundCategory.AMBIENT);
			    
			    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			    {
			        EntityEffects.playSound(p, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.AMBIENT);
			        RayCastUtils.getNearbyEntities(lumosData.stand, 10, (r)-> // Use stored reference
			        {
			            OBSParticles.drawLine(explosionLoc, r.getLocation(), 1, 0, Particle.END_ROD, null);
			            OBSParticles.drawCylinder(r.getLocation(), r.getWidth(), 3, 5, 0.5, 0.5, Particle.END_ROD, null);
			            EntityEffects.addExposed(r, 15);
			        });
			    }, 10);

			    cancelLumos(p.getUniqueId());
			    
			    return true;
			}
			
			ArmorStand stand = (ArmorStand) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.ARMOR_STAND);
	        EnderCrystal crystal = (EnderCrystal) p.getWorld().spawnEntity(p.getEyeLocation(), EntityType.END_CRYSTAL);
	        
	        crystal.setInvulnerable(true);
	        crystal.setShowingBottom(false); 
	        crystal.setGlowing(true);

	        stand.setCanPickupItems(false);
	        stand.setVisible(false);
	        stand.setGravity(false);
	        stand.setMarker(true);
	        stand.setInvulnerable(true);
	        stand.addPassenger(crystal);
	        
	        EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
	        
	        activeLumos.put(p.getUniqueId(), new LumosTask(stand, crystal, startLumosTask(p, stand)));
	        
	        return true;
		}
		
		if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_BLOCK))
		{
			Block block = e.getClickedBlock().getRelative(e.getBlockFace());
			if (!block.getType().isAir()) return false;

	        EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			
	        block.setType(Material.LIGHT);
			Levelled lightData = (Levelled) block.getBlockData();
            lightData.setLevel(15);
            block.setBlockData(lightData);
            p.sendBlockChange(block.getLocation(), lightData);
			
            Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()-> 
			{
				if (!block.getType().equals(Material.AIR)) return;
				block.setType(Material.AIR);
				OBSParticles.drawWisps(block.getLocation(), 2, 2, 4, Particle.END_ROD, null);
			}, 600);
			
			return true;
		}
			
		return false;
	}
	
	private BukkitTask startLumosTask(Player p, ArmorStand stand)
	{
		return new BukkitRunnable() 
		{
	        int duration = 0;
	        final int maxDuration = 20 * 60; // 60 seconds
	        
	        @Override
	        public void run() 
	        {

	            if (!p.isOnline()) 
	            {
	                cancelLumos(p.getUniqueId());
	                return;
	            }

	            if (duration >= maxDuration || activeLumos.get(p.getUniqueId()).crystal.isDead()) 
	            {
	                cancelLumos(p.getUniqueId());
	                OBSParticles.drawDisc(activeLumos.get(p.getUniqueId()).crystal.getLocation(), 3, 2, 3, 0, Particle.GUST_EMITTER_LARGE, null);
	                PrintUtils.PrintToActionBar(p, "&e&oLumos' &f&osprite fades away..");
	                EntityEffects.playSound(p, Sound.ENTITY_ALLAY_DEATH, SoundCategory.AMBIENT);
	                return;
	            }
	            
	            if (duration % 5 == 0) 
	            {
	                Location eyeLoc = p.getEyeLocation();
	                Vector direction = eyeLoc.getDirection().add(new Vector(4, 1, -1)).normalize().multiply(3);
	                Location orbLoc = eyeLoc.add(direction);
	                stand.teleport(orbLoc);
	            }
	            
	            if (duration % 300 == 0 && duration > 0)
	            {
	            	EntityEffects.playSound(p, Sound.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM, SoundCategory.AMBIENT);
	            }
	            
	            // Update light block at player location (every tick)
	            Location playerLoc = p.getLocation();
	            Block lightBlock = playerLoc.getBlock();
	            
	            LumosTask lumosData = activeLumos.get(p.getUniqueId());
	            if (lumosData != null) 
	            {
	                // Remove old light if it exists and player moved
	                if (lumosData.lightLocation != null && !lumosData.lightLocation.getBlock().equals(lightBlock)) 
	                {
	                    Block oldBlock = lumosData.lightLocation.getBlock();
	                    if (oldBlock.getType() == Material.LIGHT) 
	                    {
	                        oldBlock.setType(Material.AIR);
	                    }
	                }
	                
	                // Place new light if block is air
	                if (lightBlock.getType() == Material.AIR || lightBlock.getType() == Material.LIGHT) 
	                {
	                    lightBlock.setType(Material.LIGHT);
	                    Levelled lightData = (Levelled) lightBlock.getBlockData();
	                    lightData.setLevel(15);
	                    lightBlock.setBlockData(lightData);
	                    p.sendBlockChange(lightBlock.getLocation(), lightData);
	                    lumosData.lightLocation = playerLoc.clone();
	                }
	            }
	            
	            duration++;
	        }
	    }.runTaskTimer(Ouroboros.instance, 0L, 1L);
	}
	
	private void cancelLumos(UUID playerUUID) 
	{
	    LumosTask lumosData = activeLumos.remove(playerUUID);
	    if (lumosData != null) 
	    {
	        // Cancel the task
	        lumosData.task.cancel();
	        
	        // Remove entities
	        if (lumosData.stand != null && !lumosData.stand.isDead()) 
	        	lumosData.stand.remove();
	        if (lumosData.crystal != null && !lumosData.crystal.isDead()) 
	        	lumosData.crystal.remove();
	        
	        // Remove light block
	        if (lumosData.lightLocation != null) 
	        {
	            Block lightBlock = lumosData.lightLocation.getBlock();
	            if (lightBlock.getType() == Material.LIGHT) 
	            	lightBlock.setType(Material.AIR);
	        }
	    }
	}
	
	protected class LumosTask 
	{
	    ArmorStand stand;
	    EnderCrystal crystal;
	    BukkitTask task;
	    Location lightLocation;
	    
	    LumosTask(ArmorStand stand, EnderCrystal crystal, BukkitTask task) 
	    {
	        this.stand = stand;
	        this.crystal = crystal;
	        this.task = task;
	    }
	}

}
