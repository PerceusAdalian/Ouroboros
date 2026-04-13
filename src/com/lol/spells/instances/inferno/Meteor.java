package com.lol.spells.instances.inferno;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.BiomeUtils;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Meteor extends Spell
{

	public Meteor()
	{
		super("Meteor", "meteor", Material.FIRE_CHARGE, SpellType.OFFENSIVE, SpellementType.INFERNO, CastConditions.RIGHT_CLICK_AIR, Rarity.FOUR, 100, 5, false,
				"&r&fSummon a meteor at target &d&oBlock&r&7 (50m) &for &d&oMob&r&7 (30m)",
				"&r&fMeteor's yeild = 4 &r&f&l± &r&e2 bonus&r&f in &d&ohot&r&f/&d&ocold climates&r&f,",
				"&r&fand deals variable "+PrintUtils.color(ObsColors.INFERNO)+"&lInferno&r&f and &e&lBlast&r&f damage.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
	    Player p = e.getPlayer();
	    Entity target = RayCastUtils.getEntity(p, 30);

	    if (target instanceof LivingEntity)
	    {
	        playSpellEffects(p, target.getLocation(), 4, true, 2, true, MeteorSize.LARGE);
	        return 100;
	    }

	    Block bTarget = RayCastUtils.rayTraceBlock(p, 50);
	    if (bTarget == null || bTarget.getType() == Material.AIR) return -1;

	    playSpellEffects(p, bTarget.getLocation(), 4, true, 2, true, MeteorSize.LARGE);
	    return 100;
	}

	@Override
	public int getTotalManaCost()
	{
		return 100;
	}
	
	public static void summonMeteor(Player p, Location loc, int baseYield, boolean temperatureBonus, int bonusModifier, boolean isIncendiary, MeteorSize meteorSize)
	{
	    Location spawnLoc = loc.clone().add(0, 45, 0);
	    
	    Fireball fb = null;
	    if (meteorSize == MeteorSize.NORMAL) fb = (Fireball) loc.getWorld().spawnEntity(spawnLoc, EntityType.FIREBALL);
	    else if (meteorSize == MeteorSize.LARGE) fb = (LargeFireball) loc.getWorld().spawnEntity(spawnLoc, EntityType.FIREBALL);
	    
	    int bonusYield = 0;
	    if (temperatureBonus)
	    {
	    	boolean isHot = BiomeUtils.getTempCategory(p) == BiomeUtils.BiomeTemperatureCategory.HOT;
	    	boolean isCold = BiomeUtils.getTempCategory(p) == BiomeUtils.BiomeTemperatureCategory.COLD;
	    	bonusYield = isHot ? bonusModifier : isCold ? -bonusModifier : 0;	    	
	    }

	    fb.setShooter(p);
	    fb.setDirection(new Vector(0, -5, 0));
	    fb.setYield(baseYield + bonusYield);
	    fb.setIsIncendiary(isIncendiary);
	}
	
	public static void playSpellEffects(Player p, Location loc, int baseYield, boolean temperatureBonus, int bonusModifier, boolean isIncendiary, MeteorSize meteorSize)
	{
		OBSParticles.drawLine(p.getLocation(), loc, 0.5, 0.5, Particle.LAVA, null);
		EntityEffects.playSound(p, Sound.ENTITY_BLAZE_SHOOT, SoundCategory.AMBIENT);
		Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
		{
			OBSParticles.drawSpiralVortex(loc, 75, 10, 0.1, Particle.LAVA, null);
			summonMeteor(p, loc, baseYield, temperatureBonus, bonusModifier, isIncendiary, MeteorSize.NORMAL);
		}, 20);
	}
	
	public enum MeteorSize
	{
		NORMAL,
		LARGE
	}
}
