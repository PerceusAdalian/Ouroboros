package com.ouroboros.abilities.instances.special;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.HeightMap;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import com.ouroboros.Ouroboros;
import com.ouroboros.abilities.instances.AbstractOBSAbility;
import com.ouroboros.enums.AbilityMaterialClass;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.AbilityType;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.EntityEffects;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.OBStandardTimer;
import com.ouroboros.utils.RayCastUtils;

public class Prometheus extends AbstractOBSAbility
{

	public Prometheus() 
	{
		super("Prometheus' Wrath", "prometheus_special", Material.BLAZE_POWDER, StatType.MAGIC, 20, 10, AbilityType.SPECIALABILITY, ElementType.INFERNO, CastConditions.SHIFT_RIGHT_CLICK_BLOCK,
				AbilityMaterialClass.SWORDS, "&r&fPlunge your sword firmly into the earth causing a &cHeat Wave&r&f",
				"&r&fthat releases a devastating explosion of ash and mantle around you.","",
				"&r&f&n&cHeat Wave&f Damage: &l20&r&c♥ &c&lInferno &r&7| &eExplosion &fDamage: &d&oVariable &r&e&lBlast",
				"&r&fAll affected are applied &cBurn&r&f &7&o(Range: 25m / Burn Duration: 20s / CD: 3 minutes)&r&f");
	}

	@Override
	public boolean cast(Event e) 
	{
		if (e instanceof PlayerInteractEvent pie)
		{
			Player p = pie.getPlayer();
			List<Block> blocks = RayCastUtils.getNearbyBlocks(p.getLocation(), 25)
			.stream()
			.filter(b ->
			{
				Block highest = b.getWorld().getHighestBlockAt(b.getX(), b.getZ(), HeightMap.MOTION_BLOCKING);
				return b.getY() == highest.getY();
			}).toList();
			if (blocks.isEmpty()) return false;
			
			EntityEffects.playSound(p, Sound.ITEM_MACE_SMASH_GROUND, SoundCategory.AMBIENT);
			
			OBStandardTimer.runWithCancel(Ouroboros.instance, (r)->
			{
				EntityEffects.add(p, PotionEffectType.SLOWNESS, 20, 99);
				for (Block b : blocks)
					OBSParticles.drawWisps(b.getLocation(), 1.1, 1.1, 4, Particle.BLOCK_CRUMBLE, Material.STONE.createBlockData());
			}, 10, 50);

			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ENTITY_BLAZE_AMBIENT, SoundCategory.AMBIENT);
				OBSParticles.drawInfernoCastSigil(p);
				RayCastUtils.getNearbyEntities(p, 25, target->
				{					
					OBSParticles.drawSinLine(p.getLocation(), target.getLocation(), 2, Particle.LAVA, null);
					OBSParticles.drawVerticalVortex(target.getLocation(), target.getWidth(), 5, target.getHeight()%2, 2, 5, 0.5, Particle.FLAME, null);
					MobData.damageUnnaturally(p, target, 20, true, ElementType.INFERNO);
				});
			}, 75);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				Iterator<Block> it = blocks.iterator();
				for (int i = 0; i < 5; ++i)
				{
					Block b = it.next();
					b.getWorld().createExplosion(b.getX(), b.getY(), b.getZ(), 5, true, false, p);
				}
			}, 100);
			
			return true;
			
		}
		return false;
	}

}
