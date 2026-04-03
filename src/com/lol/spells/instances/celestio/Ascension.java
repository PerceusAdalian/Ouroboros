package com.lol.spells.instances.celestio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.SpellCastHandler;
import com.lol.spells.instances.Spell;
import com.lol.wand.Wand;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.CelestioEffects;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Ascension extends Spell
{

	public Ascension() 
	{
		super("Ascension", "ascension", Material.ELYTRA, SpellType.UTILITY, SpellementType.CELESTIO, CastConditions.MIXED, Rarity.FIVE, 0, 1, false,
				"&r&e&oPrimary &r&f" + PrintUtils.assignCastCondition(CastConditions.RIGHT_CLICK_AIR),
				"&r&eAscension&r&f: &e&oJudgement&r&f --",
				"&r&fUnleash a beam of light at a target &7(45m)&f, dealing"+PrintUtils.color(ObsColors.CELESTIO)+"&lCelestio&r&f",
				"&r&fdamage equal to &b&o10 + 8% &r&fof the target's maximum &aHP&f while flying.",
				"&r&fOtherwise, damage is calculated by &b&o5 + 6%&r&f. Applies &eHumility &7(20s)&f.",
				"&r&b&lMana Cost&r&f: &b25 &lMana&r&f.","",
				"&r&e&oSecondary &r&f" + PrintUtils.assignCastCondition(CastConditions.SHIFT_RIGHT_CLICK_AIR),
				"&r&eAscension&r&f: &e&oAspect of the Seraphim&r&f --",
				"&r&fAscend and gain flight for up to &b2 minutes &r&7(CD: 30s)&r&f, and while",
				"&r&fairborne, you may only cast &e&oJudgement&r&f. Toggle Cost: &b50 &lMana&r&f.","",
				"&r&eHumility&r&f: Targets take an additional &b&o1.15x&r&f &e&lCelestio&r&f damage.");
	}

	private static final Map<UUID, BukkitTask> timer = new HashMap<>();
	private static final Set<UUID> cooldown = new HashSet<>();
	
	@Override
	public int Cast(PlayerInteractEvent e)
	{
	    Player p = e.getPlayer();

	    if (CastConditions.isValidAction(e, CastConditions.SHIFT_RIGHT_CLICK_AIR))
        {
	    	if (castAspectOfTheSeraphim(p))
	    	{
	    		PrintUtils.OBSFormatDebug(p, "Cast Ascend");
	    		Wand wand = new Wand(e.getItem());
	    		if (wand.getCurrentMana() < 50)
	    		{
	    			PrintUtils.PrintToActionBar(p, "&7&oInsufficient Mana for '&e&oAspect of Seraphim&r&7&o'");
	    			return -1;
	    		}
	    		  		
	    		return 50;
	    	}
        }

	    if (CastConditions.isValidAction(e, CastConditions.RIGHT_CLICK_AIR))
	    {
	    	if (castJudgement(p))
	    	{	  
	    		PrintUtils.OBSFormatDebug(p, "Cast Judgement");
	    		Wand wand = new Wand(e.getItem());
	    		if (wand.getCurrentMana() < 25)
	    		{
	    			PrintUtils.PrintToActionBar(p, "&7&oInsufficient Mana for '&e&oJudgement&r&7&o'");
	    			return -1;
	    		}
	    		
	    		return 25;
	    	}
	    }

	    return -1;
	}
	
	@Override
	public int getTotalManaCost() 
	{
		return 50;
	}

	private boolean castAspectOfTheSeraphim(Player p)
	{
	    if (!p.getGameMode().equals(GameMode.SURVIVAL))
	    {
	        PrintUtils.PrintToActionBar(p, "&c&oThis spell requires Survival Mode!");
	        return false;
	    }

	    if (cooldown.contains(p.getUniqueId()))
	    {
	    	PrintUtils.PrintToActionBar(p, "&7&oSecondary cast on cooldown..");
	    	return false;
	    }
	    
	    EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);

	    if (p.getAllowFlight())
	    {
	        deactivateAscension(p, true);
	        
	        return true;
	    }

	    ascend(p);
	    
	    return true;
	}

	private boolean castJudgement(Player p)
	{
	    
	    boolean isAirborne  = p.isFlying();
	    double  baseDamage  = isAirborne ? 10.0 : 6.0;
	    double  hpScaling   = isAirborne ? 0.08 : 0.06;

	    boolean hit = RayCastUtils.getEntity(p, 45, target ->
	    {
	        if (!(target instanceof LivingEntity)) return;

	        MobData data   = MobData.getMob(target.getUniqueId());
	        double  damage = (data == null)
	            ? ((Damageable) target).getHealth()
	            : data.getHp(true) * hpScaling;

	        EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
	        OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.5, 0.5, Particle.END_ROD, null);

	        Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	        {
	            if (data == null || data.isDead()) return;

	            EntityEffects.playSound(p, Sound.ENTITY_BREEZE_SHOOT, SoundCategory.AMBIENT);
	            OBSParticles.drawSpiralVortex(target.getLocation(), 110, 3, 0.5, Particle.CLOUD, null);
	            OBSParticles.drawSpiralVortex(target.getLocation(), 90, 4, 0.4, Particle.END_ROD, null);
	            MobData.damageUnnaturally(p, target, baseDamage + damage, true, true, ElementType.CELESTIO);
	            CelestioEffects.addHumility((LivingEntity) target, 20);
	        }, 15);
	    });

	    if (!hit) return false;
	    
	    return true;
	}
	
	private void activateAscension(Player p)
	{
	    p.setAllowFlight(true);
	    p.setFlying(true);
	    
	    SpellCastHandler.lockedCycling.add(p.getUniqueId());

	    BukkitTask warningTask = Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	        PrintUtils.PrintToActionBar(p, "&cWarning&r&f: &eAscension&f wears off in &b&l10s&r&f!"),2200);

	    BukkitTask expiryTask = Bukkit.getScheduler().runTaskLater(Ouroboros.instance, () ->
	        deactivateAscension(p, false),2400);

	    cancelTimers(p); // cancel any previously running ascension before storing the new one
	    spellTaskHandler(p, warningTask, expiryTask);
	}

	private void deactivateAscension(Player p, boolean manual)
	{
		if (manual) cancelTimers(p);
		
		cooldown.add(p.getUniqueId());
    	Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
    	{
    		cooldown.remove(p.getUniqueId());
    		PrintUtils.PrintToActionBar(p, "&7&oSecondary is now castable!");
    	}, 600);
    	
	    SpellCastHandler.lockedCycling.remove(p.getUniqueId());
	    p.setAllowFlight(false);
	    p.setFlying(false);
	    EntityEffects.playSound(p, Sound.BLOCK_VAULT_DEACTIVATE, SoundCategory.AMBIENT);
	    EntityEffects.add(p, PotionEffectType.SLOW_FALLING, 60, 0);
	    OBSParticles.drawCelestioCastSigil(p);
	}
	
	private void ascend(Player p)
	{
	    Location pLoc = p.getLocation();
	    boolean isGrounded = pLoc.clone().subtract(0, 1, 0).getBlock().getType().isSolid();

	    if (isGrounded)
	    {
	        // Walk upward until we find enough clearance for the player (2 blocks)
	        // or hit the world height cap; whichever comes first
	        Location safe = pLoc.clone();
	        
	        safe.add(0, 5, 0);
	        boolean headClear = safe.getBlock().getType().isAir();
	        boolean feetClear = safe.clone().subtract(0, 1, 0).getBlock().getType().isAir();
	        
	        // If we couldn't find clearance, abort rather than suffocate the player
	        if (!(headClear && feetClear))
	        {
	        	PrintUtils.PrintToActionBar(p, "&c&oNo clear space above to ascend!");
	        	return;
	        }
	    
	        p.teleport(safe);
	    }

	    Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->activateAscension(p), 5);
	}
	
	private void spellTaskHandler(Player p, BukkitTask warning, BukkitTask expiry)
	{
	    timer.put(p.getUniqueId(), new BukkitTask() 
	    {
	        @Override public int getTaskId()       { return expiry.getTaskId(); }
	        @Override public Plugin getOwner()      { return expiry.getOwner(); }
	        @Override public boolean isSync()       { return expiry.isSync(); }
	        @Override public boolean isCancelled()  { return expiry.isCancelled(); }
	        @Override public void cancel()          { warning.cancel(); expiry.cancel(); }
	    });
	}

	private void cancelTimers(Player p)
	{
	    BukkitTask existing = timer.remove(p.getUniqueId());
	    if (existing != null) existing.cancel();
	}
}
