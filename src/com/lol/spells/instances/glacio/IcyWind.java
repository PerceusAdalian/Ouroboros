package com.lol.spells.instances.glacio;

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
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.Rarity;
import com.ouroboros.utils.OBSParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.GlacioEffects;

public class IcyWind extends Spell
{

	public IcyWind()
	{
		super("Icy Wind", "icy_wind", Material.STRING, SpellType.CONTROL, SpellementType.GLACIO, CastConditions.RIGHT_CLICK_AIR, Rarity.THREE, 75, 2, true,
				"&r&fApplies &bFrosted II&f  in a &dconal AOE &7(30s, 20m | &cPVP&7: &cFrosted I&7)",
				"&r&fInflicts &bChill&f while targets are &bFrosted &7(20s | &cPVP&7: &c6s&7).","",
				"&bFrosted&f Effect: &d&oSlows&r&f and &d&oWeakens&r&f those afflicted.","",
				"&r&bChill &eEffect&f: &b&oSlows&r&f while inflicting a "+PrintUtils.color(ObsColors.GLACIO)+"&lGlacio&r&f DOT effect.",
				"&r&fReapplying &bChill&f increases the &b&omagnitude&r&f, while keeping initial duration.");
	}

	@Override
	public int Cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		EntityEffects.playSound(p, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.AMBIENT);
		if (!RayCastUtils.getEntitiesInFov(p, 20, target ->
		{
			if (!(target instanceof LivingEntity le)) return;
			
			boolean pTarget = target instanceof Player;

			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.4, 0.4, Particle.SNOWFLAKE, null);
			OBSParticles.drawLine(p.getLocation(), target.getLocation(), 0.4, 0.3, Particle.BLOCK_CRUMBLE, Material.SNOW.createBlockData());
			
			if (GlacioEffects.hasFrosted.contains(target.getUniqueId())) 
			{
				GlacioEffects.addChill(p, le, 1, pTarget ? 6 : 20);
				return;
			}
			GlacioEffects.addFrosted(le, pTarget ? 0 : 1, getTotalManaCost());
			
		})) return -1;
		return 75;
	}

	@Override
	public int getTotalManaCost()
	{
		return 75;
	}

}
