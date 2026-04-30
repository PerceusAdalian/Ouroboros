package com.lol.spells.instances.aero;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.SpellCastHandler;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Fly extends Spell
{

	public Fly()
	{
		super("Fly", "fly", Material.ELYTRA, SpellType.UTILITY, SpellementType.AERO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 100, 1, false, true,
				"&r&e&oToggled&r&f: Grants flight to &6self&f. &c&oDeactivating&r&f doesn't cost &b&lMana&r&f.",
				"&r&fAll other &eSpellcasting&f is &c&odisabled&r&f while active.",
				"&r&fLanding automatically toggles this &eSpell&f.",
				"&r&cWarning&f: You may receive fall damage if toggling mid-air.","",
				"&r&b&oEchoic Dissonance&r&f: On toggle, &lCD &r&e-> &b&o30 seconds&r&f");
			
	}

	public static Map<UUID, Boolean> flyers = new HashMap<>();
	public static Set<UUID> lockedCasters = new HashSet<>();
	public static Set<UUID> cooldown = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		return playSpellEffect(p, 100, false, false);
	}

	@Override
	public int getTotalManaCost()
	{
		return 0;
	}
	
	public static int playSpellEffect(Player p, int finalManaCost, boolean lockCycling, boolean canCastSpells)
	{
		if (cooldown.contains(p.getUniqueId()))
	    {
	        PrintUtils.PrintToActionBar(p, PrintUtils.color(ObsColors.AERO)+"&lFly&r&7&o effect on cooldown!");
	        return -1;
	    }
		
		if (flyers.containsKey(p.getUniqueId()))
		{
			EntityEffects.playSound(p, Sound.BLOCK_CONDUIT_DEACTIVATE, SoundCategory.AMBIENT);
			p.setAllowFlight(false);
			p.setFlying(false);
			if (SpellCastHandler.lockedCycling.contains(p.getUniqueId())) SpellCastHandler.lockedCycling.remove(p.getUniqueId());
			if (lockedCasters.contains(p.getUniqueId())) lockedCasters.remove(p.getUniqueId());
			flyers.remove(p.getUniqueId());
			cooldown.add(p.getUniqueId());
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->cooldown.remove(p.getUniqueId()), 600);
			return 0;
		}
        
		EntityEffects.playSound(p, Sound.ENTITY_BREEZE_JUMP, null);
		p.setVelocity(p.getVelocity().setY(3).multiply(1.1));
		ObsParticles.drawDisc(p.getLocation(), p.getWidth(), 2, 4, 0.5, Particle.EXPLOSION, null);
		
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			p.setAllowFlight(true);
			p.setFlying(true);
			flyers.put(p.getUniqueId(), p.getAllowFlight());
			EntityEffects.playSound(p, Sound.ENTITY_BREEZE_IDLE_AIR, SoundCategory.AMBIENT);
		}, 20);
		
		if (lockCycling)
		{
			if (!SpellCastHandler.lockedCycling.contains(p.getUniqueId())) SpellCastHandler.lockedCycling.add(p.getUniqueId());
	    	else SpellCastHandler.lockedCycling.remove(p.getUniqueId());
		}
		
		if (!canCastSpells)
		{
			if (!lockedCasters.contains(p.getUniqueId())) lockedCasters.add(p.getUniqueId());
			else lockedCasters.remove(p.getUniqueId());
		}
		
		return finalManaCost;
	}
	
	public static void registerSpellHelper(Plugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(new Listener() 
		{
			@EventHandler
			public void onLand(PlayerMoveEvent e)
			{
				Player p = e.getPlayer();
				Block blockBelow = p.getWorld().getBlockAt(
						p.getLocation().getBlockX(), 
						p.getLocation().getBlockY() - 1,
					    p.getLocation().getBlockZ());

				if (flyers.containsKey(p.getUniqueId())	&& blockBelow.getType().isSolid())
			    {
			        p.setFlying(false);
			        p.setAllowFlight(false);
			        flyers.remove(p.getUniqueId());
			        cooldown.add(p.getUniqueId());
			        if (SpellCastHandler.lockedCycling.contains(p.getUniqueId())) SpellCastHandler.lockedCycling.remove(p.getUniqueId());
			        if (lockedCasters.contains(p.getUniqueId())) lockedCasters.remove(p.getUniqueId());
					Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->cooldown.remove(p.getUniqueId()), 600);
			        EntityEffects.playSound(p, Sound.ENTITY_BREEZE_LAND, SoundCategory.AMBIENT);
			        ObsParticles.drawAeroCastSigil(p);
			    }
			}
			
			
		}, plugin);
	}

}
