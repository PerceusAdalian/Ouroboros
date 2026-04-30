package com.lol.spells.instances.glacio;

import org.bukkit.Bukkit;
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
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GlacioEffects;

public class Chill extends Spell
{

	public Chill()
	{
		super("Chill", "chill", Material.SNOWBALL, SpellType.CANTRIP, SpellementType.GLACIO, CastConditions.RIGHT_CLICK_AIR, Rarity.ONE, 20, 1, true, false,
				"&r&fInflicts &6target &fwith &bFrosted I &7(30s, 20m | &cPVP&7: &c15s&7)",
				"&r&fInflicts &bChill&f while targets are &bFrosted &7(20s | &cPVP&7: &c3s&7)","",
				"&bFrosted &eEffect&f: &b&oSlows&r&f and &b&oWeakens&r&f those afflicted.","",
				"&r&bChill &eEffect&f: &b&oSlows&r&f while inflicting a "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f DOT effect.",
				"&r&fReapplying &bChill&f increases the &b&omagnitude&r&f, while keeping initial duration.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		if (!RayCastUtils.getEntity(p, 20, target ->
		{
			if (!(target instanceof LivingEntity le)) return;
			EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.6, 0.4, Particle.SNOWFLAKE, null);
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				ObsParticles.drawDisc(le.getLocation(), le.getWidth(), 2, 5, 0.3, Particle.SNOWFLAKE, null);
				if (GlacioEffects.hasFrosted.contains(le.getUniqueId()))
				{
					GlacioEffects.addChill(p, le, 0, target instanceof Player ? 3 : 20);
					return;
				}
				
				GlacioEffects.addFrosted(le, 0, target instanceof Player ? 15 : 30);
			}, 15);
			
		})) return -1;
		return 25;
	}

	@Override
	public int getTotalManaCost()
	{
		return 25;
	}

}
