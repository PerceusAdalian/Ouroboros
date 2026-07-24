package com.eol.echoes.abilities.instances.special;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.eol.echoes.EchoManager;
import com.eol.echoes.abilities.AbilityType;
import com.eol.echoes.abilities.EchoAbility;
import com.eol.enums.EchoForm;
import com.ouroboros.Ouroboros;
import com.ouroboros.enums.CastConditions;
import com.ouroboros.enums.ElementType;
import com.ouroboros.enums.ObsColors;
import com.ouroboros.enums.StatType;
import com.ouroboros.mobs.MobData;
import com.ouroboros.utils.ObsParticles;
import com.ouroboros.utils.PrintUtils;
import com.ouroboros.utils.RayCastUtils;
import com.ouroboros.utils.Symbols;
import com.ouroboros.utils.entityeffects.EntityEffects;
import com.ouroboros.utils.entityeffects.HeresioEffects;

public class VexingMalice extends EchoAbility
{

	public VexingMalice()
	{
		super("Vexing Malice", "vexing_malice", Material.NETHER_STAR, StatType.MELEE, 0, 0, 10, AbilityType.ULTIMATE, ElementType.HERESIO,
				CastConditions.RIGHT_CLICK_AIR, EchoForm.HATCHET, 
				"&r&fLunge towards &6"+Symbols.TARGET+" &fdealing &l400%&r&b&o Base Atk",
				"&r&fas "+PrintUtils.color(ObsColors.MORTIO)+"&lMortio&r&f dmg, inflicting &4Curse &bIII &7(7m, 45s)","",
				"&r&4Curse &eEffect&f: Applies a debilitation that &b&oslows&r&f, &b&oweakens&r&f,",
				"&r&fand &b&ofatigues&r&f those afflicted. Curses are &e&ocurable&r&f and do not stack.");
	}

	@Override
	public int cast(PlayerInteractEvent e)
	{
		Player p = e.getPlayer();
		
		if (!RayCastUtils.getEntity(p, 7, target ->
		{
			if (target == null || !(target instanceof LivingEntity le)) return;
			
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.3, 0.3, Particle.CRIMSON_SPORE, null);
			ObsParticles.drawLine(p.getLocation(), le.getLocation(), 0.3, 0.3, Particle.SMOKE, null);
			EntityEffects.rushEntity(p, le, 2.5);
			
			double dmg = EchoManager.getCodec(e.getItem()).baseStats().getAttack() * 4.0;
			Bukkit.getScheduler().runTaskLater(Ouroboros.instance, ()->
			{
				EntityEffects.playSound(p, Sound.ITEM_MACE_SMASH_GROUND, SoundCategory.AMBIENT);
				ObsParticles.drawLandingWave(le);
				MobData.damageUnnaturally(p, le, dmg, true, true, ElementType.MORTIO, EchoManager.getCodec(e.getItem()));
				HeresioEffects.addCurse(le, 2, 45);
				ObsParticles.drawMortioCastSigil(le);
			}, 7);
		})) return -1;
		
		return 10;
	}

	@Override
	public int getFinalDurabilityCost()
	{
		return 10;
	}

}
