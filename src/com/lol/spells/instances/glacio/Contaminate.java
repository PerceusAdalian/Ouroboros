package com.lol.spells.instances.glacio;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.instances.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Contaminate extends Spell
{

	public Contaminate()
	{
		super("Contaminate", "contaminate", Material.GREEN_DYE, SpellType.DEBUFF, SpellementType.GLACIO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 150, 1, false,
				true, "&r&fInflict "+PrintUtils.color(ObsColors.CORROSIVE)+"&oErosion&r&f on &6targets&f within FOV &7(20m, 15s)","",
				PrintUtils.color(ObsColors.CORROSIVE)+"Erosion &r&eEffect&f: Applies a "+PrintUtils.color(ObsColors.CORROSIVE)+"&oCorrosive &r&dDOT&f dealing",
				"&r&f5&6"+Symbols.ARMOR+"&f dmg/s, and automatically &c&oends&r&f on &6Break&f.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();

		if (!RayCastUtils.getEntitiesInFov(p, 20, target ->
		{
			if (!(target instanceof LivingEntity le) || target instanceof Player) return;
			Location to = le.getLocation();
			ObsParticles.drawSinLine(p.getLocation(), to, 0.4, Particle.FISHING, null);
			ObsParticles.drawCosLine(p.getLocation(), to, 0.4, Particle.ASH, null);
			ObsParticles.drawCosLine(p.getLocation(), to, 0.4, Particle.BLOCK_CRUMBLE, Material.EMERALD_BLOCK.createBlockData());
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawGlacioCastSigil(le);
				EntityEffects.addErosion(le, 15);
			}, 12);
			
		})) return -1;
		EntityEffects.playSound(p, Sound.AMBIENT_UNDERWATER_EXIT, SoundCategory.AMBIENT);
		
		return 150;
	}

	@Override
	public int getTotalManaCost()
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
