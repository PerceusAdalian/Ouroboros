package com.lol.spells.instances.geo;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.GeoEffects;

public class SandBlast extends Spell
{

	public SandBlast()
	{
		super("Sand Blast", "sand_blast", Material.GLOWSTONE_DUST, SpellType.OFFENSIVE, SpellementType.GEO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 10, 2, false,
				"&r&fSend a handful of sand infront of you as a &d&oConal&r&f AOE dealing",
				"&r&f4&c♥&f "+PrintUtils.color(ObsColors.GEO)+"&lGeo&r&f damage and inflict &6Sanded&7 (10m, 20s)","",
				"&6Sanded &eEffect&f: Afflicted are &b&oBlinded&r&f and &b&oSlowed&r&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntitiesInFov(p, 10, target ->
		{
			if (target == null || !(target instanceof LivingEntity le)) return;
			
			OBSParticles.drawLine(p.getLocation(), le.getLocation(), 0.5, 0.5, Particle.BLOCK_CRUMBLE, Material.SAND.createBlockData());
			OBSParticles.drawLine(p.getLocation(), le.getLocation(), 0.4, 0.4, Particle.ASH, null);
			
			MobData.damageUnnaturally(p, le, 4, false, true, ElementType.GEO);
			GeoEffects.addSanded(le, 20);
			
		})) return -1;
		
		return 10;
	}

	@Override
	public int getTotalManaCost()
	{
		return 10;
	}

}
