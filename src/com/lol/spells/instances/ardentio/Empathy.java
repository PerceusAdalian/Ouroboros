package com.lol.spells.instances.ardentio;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.lol.enums.SpellType;
import com.lol.enums.SpellementType;
import com.lol.spells.Spell;
import com.ouroboros.Ouroboros;
import com.ouroboros.accounts.PlayerData;
import com.ouroboros.accounts.PlayerHud;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;

public class Empathy extends Spell
{

	public Empathy()
	{
		super("Empathy", "empathy", Material.GHAST_TEAR, SpellType.SUPPORT, SpellementType.ARDENTIO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 0, 10, true,
				true, 
				"&r&fEqualize &6"+Symbols.TARGET+" &c&oPlayer&r&f's HP&c"+Symbols.HP+" &fto &6self &7(30m)",
				"&r&bEchoic Resonance&f: If &6"+Symbols.TARGET+" &fis &6&oBroken&r&f, &a&orestore &r&6AR"+Symbols.ARMOR+" &fto full.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 30, target ->
		{
			if (target == null || !(target instanceof Player pt)) return;
			
			double theta = ObsParticles.deriveDegreeTheta(p.getLocation(), pt.getLocation());
			ObsParticles.drawAngledArcLine(p.getLocation(), pt.getLocation(), 0.6, 9, theta, 0.3, Particle.CLOUD, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), pt.getLocation(), 0.8, 9, theta, 0.5, Particle.HAPPY_VILLAGER, null);
			ObsParticles.drawAngledArcLine(p.getLocation(), pt.getLocation(), 1, 9, theta, 0.6, Particle.BLOCK_CRUMBLE, Material.EMERALD_BLOCK.createBlockData());
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				if (!p.isOnline() || !pt.isOnline()) return;
				PlayerData data = PlayerData.getPlayer(p.getUniqueId());
				if (data == null) return;
				PlayerData tData = PlayerData.getPlayer(pt.getUniqueId());
				if (tData == null) return;
				
				ObsParticles.drawLandingWave(pt);
				ObsParticles.drawSpiralVortex(pt.getLocation(), 70, 3, 0.1, Particle.CLOUD, null);
				ObsParticles.drawWisps(pt.getLocation(), pt.getWidth(), pt.getHeight(), 7, Particle.HAPPY_VILLAGER, null);
				
				if (data.getHP() > tData.getDefaultHP())
					tData.setHP(tData.getDefaultHP());
				else tData.setHP(data.getHP());
				data.save();
				tData.save();
				PlayerHud.update(p);
				PlayerHud.update(pt);

				if (tData.isBreak()) 
				{
					tData.setBreak(false);
					PlayerData.restoreArmor(pt, tData.getDefaultArmor());
					tData.save();
					PlayerHud.update(p);
				}
			}, 12);
			
		})) return -1;
		
		return 0;
	}

	@Override
	public int getTotalManaCost()
	{
		return 0;
	}

}
